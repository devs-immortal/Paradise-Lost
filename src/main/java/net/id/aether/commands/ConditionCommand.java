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
import net.id.aether.component.ConditionManager;
import net.id.aether.effect.condition.Condition;
import net.id.aether.effect.condition.Persistence;
import net.id.aether.effect.condition.Severity;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConditionCommand {

    public static final ConditionSuggester CONDITION_SUGGESTER = new ConditionSuggester();
    public static final SeveritySuggester SEVERITY_SUGGESTER = new SeveritySuggester();
    public static final PersistenceSuggester PERSISTENCE_SUGGESTER = new PersistenceSuggester();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("condition")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(literal("query")
                                .executes(context -> printCondition(context.getSource(), null, null))
                                .then(argument("target", EntityArgumentType.entities())
                                        .executes(context -> printCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), null))
                                        .then(argument("condition", IdentifierArgumentType.identifier()).suggests(CONDITION_SUGGESTER)
                                                .executes((context -> printCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), IdentifierArgumentType.getIdentifier(context, "condition")))))))
                        .then(literal("assign")
                                .then(argument("target", EntityArgumentType.entities())
                                        .then(argument("condition", IdentifierArgumentType.identifier()).suggests(CONDITION_SUGGESTER)
                                                .then(argument("persistence", StringArgumentType.word()).suggests(PERSISTENCE_SUGGESTER)
                                                        .then(argument("value", FloatArgumentType.floatArg()).suggests(SEVERITY_SUGGESTER)
                                                                .executes(context -> setCondition(context.getSource(), EntityArgumentType.getEntity(context, "target"), IdentifierArgumentType.getIdentifier(context, "condition"), FloatArgumentType.getFloat(context, "value"), StringArgumentType.getString(context, "persistence"))))))))
                        .then(literal("clear")
                                .executes(context -> clearCondition(context.getSource(), null, null))
                                .then(argument("target", EntityArgumentType.entities())
                                        .executes(context -> clearCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), null))
                                        .then(argument("condition", IdentifierArgumentType.identifier()).suggests(CONDITION_SUGGESTER)
                                                .executes(context -> clearCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), IdentifierArgumentType.getIdentifier(context, "condition"))))))
        );
    }

    private static int clearCondition(ServerCommandSource source, Collection<? extends Entity> entities, Identifier attributeId) {
        entities = handleNullEntity(source, entities);
        entities.forEach(entity -> {
            if(entity instanceof LivingEntity target) {
                var conditions = handleNullCondition(source, attributeId, target);
                if (!conditions.isEmpty()) {
                    conditions.forEach(condition -> {
                        ConditionManager manager = ConditionAPI.getConditionManager(target);
                        manager.set(condition, Persistence.TEMPORARY, 0);
                        manager.set(condition, Persistence.CHRONIC, 0);

                        ConditionAPI.trySync(target);
                    });

                    source.sendFeedback(
                            Text.translatable(
                                    "commands.the_aether.condition.success.clear.individual",
                                    conditions.size(), entity.getDisplayName()
                            ),
                            true
                    );
                }
            }
        });
        source.sendFeedback(Text.translatable("commands.the_aether.condition.success.clear"), true);
        return 1;
    }

    private static int printCondition(ServerCommandSource source, @Nullable Collection<? extends Entity> entities, Identifier attributeId) {
        entities = handleNullEntity(source, entities);
        entities.forEach(entity -> {
            if(entity instanceof LivingEntity target) {
                var conditions = handleNullCondition(source, attributeId, target);
                conditions.forEach(condition -> {
                    var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                    var severity = Severity.getSeverity(rawSeverity);

                    if (!condition.isExempt(target)) {
                        // todo: also print who is being queried
                        source.sendFeedback(Text.translatable("commands.the_aether.condition.success.query", Text.translatable(ConditionAPI.getTranslationString(condition)), Text.translatable(severity.getTranslationKey()), rawSeverity), false);
                    } else {
                        source.sendError(Text.translatable("commands.the_aether.condition.failure.query", Text.translatable(ConditionAPI.getTranslationString(condition))));
                    }
                });
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
            } catch (NoSuchElementException e) {
                source.sendError(Text.translatable("commands.the_aether.condition.failure.get_condition", attributeId));
                return 1;
            }
            try {
                persistence = Persistence.valueOf(persistenceString);
            } catch (NoSuchElementException e) {
                source.sendError(Text.translatable("commands.the_aether.condition.failure.get_persistence", persistenceString));
                return 1;
            }

            var manager = ConditionAPI.getConditionManager(target);

            if(!condition.isExempt(target)) {
                if(manager.set(condition, persistence, value)) {
                    var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                    var severity = Severity.getSeverity(rawSeverity);

                    // todo: also print who the condition is being assigned to
                    source.sendFeedback(Text.translatable("commands.the_aether.condition.success.assign", Text.translatable(ConditionAPI.getTranslationString(condition)), Text.translatable(severity.getTranslationKey()), rawSeverity), false);
                    ConditionAPI.trySync(target);
                }
                else {
                    source.sendError(Text.translatable("commands.the_aether.condition.failure.assign"));
                }
            }
        }
        return 1;
    }

    private static Collection<? extends Entity> handleNullEntity(ServerCommandSource source, Collection<? extends Entity> entities){
        if (entities == null) {
            try {
                entities = List.of(source.getEntityOrThrow());
            } catch (Exception e) {
                source.sendError(Text.translatable("commands.the_aether.condition.failure.get_entity"));
                entities = List.of();
            }
        }
        return entities;
    }

    private static Collection<Condition> handleNullCondition(ServerCommandSource source, Identifier attributeId, LivingEntity target){
        Collection<Condition> conditions;
        if (attributeId != null) {
            try {
                conditions = List.of(ConditionAPI.getOrThrow(attributeId));
            } catch (NoSuchElementException e) {
                source.sendError(Text.translatable("commands.the_aether.condition.failure.get_condition", attributeId));
                conditions = List.of();
            }
        } else {
            conditions = ConditionAPI.getValidConditions(target.getType());
        }
        return conditions;
    }

    public static class ConditionSuggester implements SuggestionProvider<ServerCommandSource> {
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
                condition = ConditionAPI.getOrThrow(IdentifierArgumentType.getIdentifier(context, "condition"));
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
