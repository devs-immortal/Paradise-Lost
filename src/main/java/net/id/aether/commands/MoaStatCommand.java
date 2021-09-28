package net.id.aether.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.id.aether.api.MoaAPI;
import net.id.aether.entities.passive.moa.MoaAttributes;
import net.id.aether.component.AetherComponents;
import net.id.aether.component.MoaGenes;
import net.id.aether.entities.passive.moa.MoaEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MoaStatCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("moastat")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(argument("target", EntityArgumentType.entities())
                                .then(literal("query")
                                        .then(argument("attribute", StringArgumentType.word())
                                                .executes((context -> printStat(context.getSource(), EntityArgumentType.getEntities(context, "target"), StringArgumentType.getString(context, "attribute"))))))
                                .then(literal("assign").then(argument("attribute", StringArgumentType.word())
                                        .then(argument("value", FloatArgumentType.floatArg())
                                                .executes(context -> setStat(context.getSource(), EntityArgumentType.getEntity(context, "target"), StringArgumentType.getString(context, "attribute"), FloatArgumentType.getFloat(context, "value")))))))
        );
    }

    private static int printStat(ServerCommandSource source, Collection<? extends Entity> entities, String attributeId) {
        entities.forEach(entity -> {
            if (entity instanceof MoaEntity moa) {
                MoaGenes genes = moa.getGenes();
                source.sendFeedback(new TranslatableText("commands.aether.moastat.name", moa.getDisplayName()).formatted(Formatting.LIGHT_PURPLE), false);
                source.sendFeedback(new TranslatableText("commands.aether.moastat.race", new TranslatableText(MoaAPI.formatForTranslation(genes.getRace()))).formatted(Formatting.LIGHT_PURPLE), false);
                if (attributeId.equals("HUNGER")) {
                    source.sendFeedback(new LiteralText("Hunger: " + String.format("%.2f", genes.getHunger())).formatted(Formatting.GOLD, Formatting.ITALIC), false);
                } else if (attributeId.equals("ALL")) {
                    for (MoaAttributes attribute : MoaAttributes.values()) {
                        source.sendFeedback(new LiteralText(attribute.name() + " = " + genes.getAttribute(attribute)).formatted(Formatting.GOLD, Formatting.ITALIC), false);
                    }
                } else {
                    try {
                        MoaAttributes attribute = MoaAttributes.valueOf(attributeId);
                        source.sendFeedback(new LiteralText(attribute.name() + " = " + genes.getAttribute(attribute)).formatted(Formatting.GOLD, Formatting.ITALIC), false);
                    } catch (IllegalArgumentException e) {
                        source.sendError(new TranslatableText("commands.aether.moastat.failure.attribute"));
                    }
                }
            } else {
                source.sendError(new TranslatableText("commands.aether.moastat.failure.entity", entity.getType().getName()));
            }
        });
        return 1;
    }

    private static int setStat(ServerCommandSource source, Entity entity, String attributeId, float value) {
        if (entity instanceof MoaEntity moa) {
            MoaGenes genes = moa.getGenes();
            source.sendFeedback(new TranslatableText("commands.aether.moastat.name", moa.getDisplayName()).formatted(Formatting.LIGHT_PURPLE), false);
            source.sendFeedback(new TranslatableText("commands.aether.moastat.race", new TranslatableText(MoaAPI.formatForTranslation(genes.getRace()))).formatted(Formatting.LIGHT_PURPLE), false);
            if (attributeId.equals("HUNGER")) {
                genes.setHunger(Math.min(Math.max(value, 100), 0));
                source.sendFeedback(new LiteralText("SET Hunger TO " + String.format("%.2f", genes.getHunger())).formatted(Formatting.AQUA, Formatting.ITALIC), false);
            } else if (attributeId.equals("ALL")) {
                for (MoaAttributes attribute : MoaAttributes.values()) {
                    genes.setAttribute(attribute, value);
                    source.sendFeedback(new LiteralText("SET " + attribute.name() + " TO " + genes.getAttribute(attribute)).formatted(Formatting.AQUA, Formatting.ITALIC), false);
                }
            } else {
                try {
                    MoaAttributes attribute = MoaAttributes.valueOf(attributeId);
                    genes.setAttribute(attribute, value);
                    source.sendFeedback(new LiteralText("SET " + attribute.name() + " TO " + genes.getAttribute(attribute)).formatted(Formatting.AQUA, Formatting.ITALIC), false);
                } catch (IllegalArgumentException e) {
                    source.sendError(new TranslatableText("commands.aether.moastat.failure.attribute"));
                }
            }
            AetherComponents.MOA_GENETICS_KEY.sync(moa);
        } else {
            source.sendError(new TranslatableText("commands.aether.moastat.failure.entity", entity.getType().getName()));
        }
        return 1;
    }
}
