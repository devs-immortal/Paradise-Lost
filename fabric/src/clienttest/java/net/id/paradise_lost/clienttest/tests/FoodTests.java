package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.clienttest.Recorder;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.id.paradise_lost.clienttest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class FoodTests {
    private static final String GROUP = "Food";
    private static BlockPos pos;

    private FoodTests() {
    }

    public static List<Test> all() {
        return List.of(
                blackcurrantEatTime(),
                heals("popom jelly heals", POPOM_JELLY.get()),
                heals("jelly-filled roll heals", AMADRYS_BREAD_GLAZED_FILLED.get()),
                aurelMilkSips(),
                cheesecakeBites()
        );
    }

    private static Test blackcurrantEatTime() {
        return new Test(GROUP, "blackcurrant eat time is 16", Step.run(0, () -> {
            int ticks = new ItemStack(BLACKCURRANT.get()).getUseDuration(client().player);
            check(ticks == 16, "eat time is " + ticks + " ticks");
        }));
    }

    private static Test heals(String name, Item food) {
        return new Test(GROUP, name,
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setHealth(10);
                    new ItemStack(food).finishUsingItem(player.serverLevel(), player);
                })),
                Step.until("health above 10", 20, () -> fromServer(server -> player(server).getHealth() > 10))
        );
    }

    private static Test aurelMilkSips() {
        return new Test(GROUP, "aurel milk has 7 sips like vanilla milk",
                Step.run(0, () -> onServer(server -> hold(player(server), AUREL_MILK_BUCKET.get()))),
                Step.run(10, () -> client().options.keyUse.setDown(true)),
                Step.until("drinking to finish", 80, () -> fromServer(server -> player(server).getMainHandItem().is(AUREL_BUCKET.get()))),
                Step.run(5, () -> {
                    client().options.keyUse.setDown(false);
                    int sips = Recorder.sips.get();
                    int finalSips = Recorder.finalSips.get();
                    check(sips >= 6 && finalSips == 1, sips + " sips while drinking and " + finalSips + " final sip");
                })
        );
    }

    private static Test cheesecakeBites() {
        return new Test(GROUP, "4 bites of halflight cheesecake feed and regenerate",
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
