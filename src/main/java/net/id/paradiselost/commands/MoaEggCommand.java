package net.id.paradiselost.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.component.MoaGenes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MoaEggCommand {

    private static final RaceSuggester RACES = new RaceSuggester();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("moaegg")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(argument("target", EntityArgumentType.players()).then((argument("race", IdentifierArgumentType.identifier()).suggests(RACES)
                                .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "target"), IdentifierArgumentType.getIdentifier(context, "race"), false)))
                                .then(literal("asBaby")
                                        .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "target"), IdentifierArgumentType.getIdentifier(context, "race"), true)))))
        );
    }

    private static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Identifier raceId, boolean baby) {
        var race = MoaAPI.getRace(raceId);

        if (race == MoaAPI.FALLBACK_MOA) {
            source.sendError(Text.translatable("commands.paradise_lost.moaegg.failure", raceId.toString()));
        } else {
            ItemStack template = MoaGenes.getEggForCommand(race, source.getWorld(), baby);
            targets.forEach(player -> {
                ItemStack egg = template.copy();
                if (!player.getInventory().insertStack(template)) {
                    ItemScatterer.spawn(source.getWorld(), player.getX(), player.getY(), player.getZ(), egg);
                }
                source.sendFeedback(Text.translatable("commands.paradise_lost.moaegg.success", egg.toHoverableText(), targets.iterator().next().getDisplayName()), true);
            });
        }

        return targets.size();
    }

    public static class RaceSuggester implements SuggestionProvider<ServerCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
            MoaAPI.getRegisteredRaces().forEachRemaining(race -> builder.suggest(race.getId().toString()));
            return builder.buildFuture();
        }
    }
}
