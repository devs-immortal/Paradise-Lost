package net.id.paradise_lost.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.paradise_lost.component.MoaGenes;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MoaStatCommand {

    public static final AttributeSuggester ATTRIBUTE_SUGGESTER = new AttributeSuggester();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("moastat")
                        .requires((source) -> source.hasPermission(2))
                        .then(argument("target", EntityArgument.entities())
                                .then(literal("query")
                                        .executes(context -> printStat(context.getSource(), EntityArgument.getEntities(context, "target"), null))
                                        .then(argument("attribute", StringArgumentType.word()).suggests(ATTRIBUTE_SUGGESTER)
                                                .executes((context -> printStat(context.getSource(), EntityArgument.getEntities(context, "target"), StringArgumentType.getString(context, "attribute"))))))
                                .then(literal("assign").then(argument("attribute", StringArgumentType.word())
                                        .then(argument("value", FloatArgumentType.floatArg())
                                                .executes(context -> setStat(context.getSource(), EntityArgument.getEntity(context, "target"), StringArgumentType.getString(context, "attribute"), FloatArgumentType.getFloat(context, "value")))))))
        );
    }

    private static int printStat(CommandSourceStack source, Collection<? extends Entity> entities, String rawAttribute) {
        String attributeId = rawAttribute == null ? "ALL" : rawAttribute;
        entities.forEach(entity -> {
            if (entity instanceof MoaEntity moa) {
                MoaGenes genes = moa.getGenes();
                source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.name", moa.getDisplayName()).withStyle(ChatFormatting.LIGHT_PURPLE), false);
                source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.race", Component.translatable(genes.getRaceTranslationKey())).withStyle(ChatFormatting.LIGHT_PURPLE), false);
                if (attributeId.equals("HUNGER")) {
                    source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.print", Component.translatable("commands.paradise_lost.moastat.hunger"), String.format("%.2f", genes.getHunger())).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC), false);
                } else if (attributeId.equals("ALL")) {
                    for (MoaAttributes attribute : MoaAttributes.values()) {
                        source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.print", Component.translatable(attribute.getTranslationKey()), genes.getAttribute(attribute)).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC), false);
                    }
                } else {
                    try {
                        MoaAttributes attribute = MoaAttributes.valueOf(attributeId);
                        source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.print", Component.translatable(attribute.getTranslationKey()), genes.getAttribute(attribute)).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC), false);
                    } catch (IllegalArgumentException e) {
                        source.sendFailure(Component.translatable("commands.paradise_lost.moastat.failure.attribute"));
                    }
                }
            } else {
                source.sendFailure(Component.translatable("commands.paradise_lost.moastat.failure.entity", entity.getType().getDescription()));
            }
        });
        return 1;
    }

    private static int setStat(CommandSourceStack source, Entity entity, String attributeId, float value) {
        if (entity instanceof MoaEntity moa) {
            MoaGenes genes = moa.getGenes();
            source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.name", moa.getDisplayName()).withStyle(ChatFormatting.LIGHT_PURPLE), false);
            source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.race", Component.translatable(genes.getRaceTranslationKey())).withStyle(ChatFormatting.LIGHT_PURPLE), false);
            if (attributeId.equals("HUNGER")) {
                genes.setHunger(Math.min(Math.max(value, 100), 0));
                source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.set", Component.translatable("commands.paradise_lost.moastat.hunger"), String.format("%.2f", genes.getHunger())).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), false);
            } else if (attributeId.equals("ALL")) {
                for (MoaAttributes attribute : MoaAttributes.values()) {
                    genes.setAttribute(attribute, value);
                    source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.set", Component.translatable(attribute.getTranslationKey()), genes.getAttribute(attribute)).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), false);
                }
            } else {
                try {
                    MoaAttributes attribute = MoaAttributes.valueOf(attributeId);
                    genes.setAttribute(attribute, value);
                    source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moastat.set", Component.translatable(attribute.getTranslationKey()), genes.getAttribute(attribute)).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), false);
                } catch (IllegalArgumentException e) {
                    source.sendFailure(Component.translatable("commands.paradise_lost.moastat.failure.attribute"));
                }
            }
        } else {
            source.sendFailure(Component.translatable("commands.paradise_lost.moastat.failure.entity", entity.getType().getDescription()));
        }
        return 1;
    }

    public static class AttributeSuggester implements SuggestionProvider<CommandSourceStack> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> builder.suggest(attribute.name()));
            builder.suggest("HUNGER");
            builder.suggest("ALL");
            return builder.buildFuture();
        }
    }
}
