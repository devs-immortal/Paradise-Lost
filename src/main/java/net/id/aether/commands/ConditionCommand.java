package net.id.aether.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.aether.api.ConditionAPI;
import net.id.aether.effect.condition.Condition;
import net.id.aether.effect.condition.Persistence;
import net.id.aether.effect.condition.Severity;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConditionCommand {

    public static final ConditionProcessorSuggester REGISTERED_CONDITIONS = new ConditionProcessorSuggester();
    public static final SeveritySuggester SEVERITY_SUGGESTER = new SeveritySuggester();
    public static final PersistenceSuggester PERSISTENCE_SUGGESTER = new PersistenceSuggester();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("condition")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(literal("query")
                                .then(argument("target", EntityArgumentType.entities())
                                        .then(argument("processor", IdentifierArgumentType.identifier()).suggests(REGISTERED_CONDITIONS)
                                                .executes((context -> printCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), IdentifierArgumentType.getIdentifier(context, "processor")))))))
                        .then(literal("assign")
                                .then(argument("target", EntityArgumentType.entities())
                                        .then(argument("processor", IdentifierArgumentType.identifier()).suggests(REGISTERED_CONDITIONS)
                                                .then(argument("persistence", StringArgumentType.word()).suggests(PERSISTENCE_SUGGESTER)
                                                        .then(argument("value", FloatArgumentType.floatArg()).suggests(SEVERITY_SUGGESTER)
                                                                .executes(context -> setCondition(context.getSource(), EntityArgumentType.getEntity(context, "target"), IdentifierArgumentType.getIdentifier(context, "processor"), FloatArgumentType.getFloat(context, "value"), StringArgumentType.getString(context, "persistence"))))))))
                        .then(literal("clear")
                                .then(argument("target", EntityArgumentType.entities())
                                        .executes(context -> clearConditions(context.getSource(), EntityArgumentType.getEntities(context, "target")))))
        );
    }

    private static int clearConditions(ServerCommandSource source, Collection<? extends Entity> entities) {
        if (entities.stream().allMatch(entity -> {
            if(entity instanceof LivingEntity target) {
                var manager = ConditionAPI.getConditionManager(target);
                if (manager.removeAll()) {
                    ConditionAPI.trySync(target);
                    return true;
                } else {
                    return false;
                }
            } else {
                return true; // This lets @e work
            }
        })) {
            source.sendFeedback(new LiteralText("Successfully cleared all conditions."), true);
        } else {
            source.sendError(new LiteralText("Command couldn't complete. This should be impossible. Please report this bug."));
        }
        return 1;
    }

    private static int printCondition(ServerCommandSource source, Collection<? extends Entity> entities, Identifier attributeId) {
        entities.forEach(entity -> {
            if(entity instanceof LivingEntity target) {
                Condition condition;

                try {
                    condition = ConditionAPI.getOrThrow(attributeId);
                } catch (NoSuchElementException e) {
                    source.sendError(new LiteralText(e.getMessage()));
                    return;
                }

                var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                var severity = Severity.getSeverity(rawSeverity);

                if (!condition.isExempt(target)) {
                    source.sendFeedback(new TranslatableText("commands.aether.condition.success.query", new TranslatableText(ConditionAPI.getTranslationString(condition)), new TranslatableText(severity.translation), rawSeverity), false);
                }
                else {
                    source.sendError(new TranslatableText("commands.aether.condition.failure", new TranslatableText(ConditionAPI.getTranslationString(condition))));
                }
            }
        });
        return 1;
    }

    private static int setCondition(ServerCommandSource source, Entity entity, Identifier attributeId, float value, String persistenceString) {
        if(entity instanceof LivingEntity target) {
            Condition condition;
            Persistence persistence;

            try {
                condition = ConditionAPI.getOrThrow(attributeId);
                persistence = Persistence.valueOf(persistenceString);
            } catch (NoSuchElementException e) {
                source.sendError(new LiteralText(e.getMessage()));
                return 0;
            }

            var manager = ConditionAPI.getConditionManager(target);

            if(!condition.isExempt(target)) {
                if(manager.set(attributeId, persistence, value)) {
                    var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                    var severity = Severity.getSeverity(rawSeverity);

                    source.sendFeedback(new TranslatableText("commands.aether.condition.success.assign", new TranslatableText(ConditionAPI.getTranslationString(condition)), new TranslatableText(severity.translation), rawSeverity), false);
                    ConditionAPI.trySync(target);
                }
                else {
                    source.sendError(new LiteralText("...you tried setting a constant condition, didn't you?"));
                }
            }
        }
        return 1;
    }

    public static class ConditionProcessorSuggester implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            AetherRegistries.CONDITION_REGISTRY.getIds().forEach(id -> builder.suggest(id.toString()));
            return builder.buildFuture();
        }
    }

    public static class SeveritySuggester implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            Condition condition;

            try {
                condition = ConditionAPI.getOrThrow(IdentifierArgumentType.getIdentifier(context, "processor"));
            } catch (Exception e){
                return builder.suggest(0).buildFuture();
            }

            float max = condition.scalingValue;

            Arrays.stream(Severity.values()).sorted().forEach((severity) -> builder.suggest(Float.toString(Math.round(max * severity.triggerPercent))));
            builder.suggest(Float.toString(max));

            return builder.buildFuture();
        }
    }

    public static class PersistenceSuggester implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest(Persistence.TEMPORARY.name());
            builder.suggest(Persistence.CHRONIC.name());
            return builder.buildFuture();
        }
    }
}
