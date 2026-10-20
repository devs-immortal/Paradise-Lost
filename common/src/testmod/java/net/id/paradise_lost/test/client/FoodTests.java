package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class FoodTests {
    private static BlockPos pos;

    private FoodTests() {
    }

    @ClientTest("blackcurrant eat time is 16")
    private static Step blackcurrantEatTime() {
        return Step.run(0, () -> {
            int ticks = new ItemStack(BLACKCURRANT.get()).getUseDuration(client().player);
            check(ticks == 16, "eat time is " + ticks + " ticks");
        });
    }

    @ClientTest("popom jelly heals")
    private static List<Step> popomJellyHeals() {
        return heals(POPOM_JELLY.get());
    }

    @ClientTest("jelly-filled roll heals")
    private static List<Step> jellyFilledRollHeals() {
        return heals(AMADRYS_BREAD_GLAZED_FILLED.get());
    }

    private static List<Step> heals(Item food) {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setHealth(10);
                    new ItemStack(food).finishUsingItem(player.serverLevel(), player);
                })),
                Step.until("health above 10", 20, () -> fromServer(server -> player(server).getHealth() > 10))
        );
    }

    @ClientTest("aurel milk has 7 sips like vanilla milk")
    private static List<Step> aurelMilkSips() {
        return List.of(
                Step.run(0, () -> onServer(server -> hold(player(server), AUREL_MILK_BUCKET.get()))),
                Step.run(10, () -> client().options.keyUse.setDown(true)),
                Step.until("drinking to finish", 80, () -> fromServer(server -> player(server).getMainHandItem().is(AUREL_BUCKET.get()))),
                Step.run(5, () -> {
                    client().options.keyUse.setDown(false);
                    int sips = Recorder.sips.get();
                    int finalSips = Recorder.finalSips.get();
                    check(sips == 6 && finalSips == 1, sips + " sips while drinking and " + finalSips + " final sip");
                })
        );
    }

    @ClientTest("4 bites of halflight cheesecake feed and regenerate")
    private static List<Step> cheesecakeBites() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.CHEESECAKE.get().defaultBlockState());
                    player.getFoodData().setFoodLevel(2);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.until("cheesecake eaten", 20, () -> fromServer(server -> world(server).getBlockState(pos).isAir())),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(player.getFoodData().getFoodLevel() == 18, "food level " + player.getFoodData().getFoodLevel() + " expected 18");
                    check(player.hasEffect(MobEffects.REGENERATION), "no regeneration");
                }))
        );
    }
}
