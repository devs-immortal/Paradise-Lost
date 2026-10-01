package net.id.paradise_lost.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.api.MoaAPI;
import net.id.paradise_lost.component.MoaGenes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MoaEggCommand {

    private static final RaceSuggester RACES = new RaceSuggester();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("moaegg")
                        .requires((source) -> source.hasPermission(2))
                        .then(argument("target", EntityArgument.players()).then((argument("race", ResourceLocationArgument.id()).suggests(RACES)
                                .executes(context -> execute(context.getSource(), EntityArgument.getPlayers(context, "target"), ResourceLocationArgument.getId(context, "race"), false)))
                                .then(literal("asBaby")
                                        .executes(context -> execute(context.getSource(), EntityArgument.getPlayers(context, "target"), ResourceLocationArgument.getId(context, "race"), true)))))
        );
    }

    private static int execute(CommandSourceStack source, Collection<ServerPlayer> targets, ResourceLocation raceId, boolean baby) {

        var race = MoaAPI.findRace(raceId).orElse(null);
        if (race == null && raceId.getNamespace().equals("minecraft")) {

            race = MoaAPI.findRace(ModConstants.id(raceId.getPath())).orElse(null);
        }
        if (race == null) {

            source.sendFailure(Component.translatable("commands.paradise_lost.moaegg.fallback", raceId.toString()));
            race = MoaAPI.getFallbackRace();
        }

        ItemStack template = MoaGenes.getEggForCommand(race, source.getLevel(), baby);
        targets.forEach(player -> {

            ItemStack egg = template.copy();
            if (!player.getInventory().add(egg)) {
                Containers.dropItemStack(source.getLevel(), player.getX(), player.getY(), player.getZ(), egg);
            }
            source.sendSuccess(() -> Component.translatable("commands.paradise_lost.moaegg.success", egg.getDisplayName(), player.getDisplayName()), true);
        });

        return targets.size();
    }

    public static class RaceSuggester implements SuggestionProvider<CommandSourceStack> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
            MoaAPI.getRegisteredRaces().forEachRemaining(race -> builder.suggest(race.getId().toString()));
            return builder.buildFuture();
        }
    }
}
