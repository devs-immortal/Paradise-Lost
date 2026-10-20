package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.entity.projectile.ThrownNitraEntity;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class NitraTests {
    private static BlockPos pos;
    private static int entityId;

    private NitraTests() {
    }

    @ClientTest("burning arrow explodes for the client")
    private static List<Step> nitraArrow() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    pos = ahead(player, 10, 0);
                    world.setBlockAndUpdate(pos, BlockRegistry.NITRA_BUNCH.get().defaultBlockState());
                    Arrow arrow = new Arrow(world, player, new ItemStack(Items.ARROW), null);
                    Vec3 at = Vec3.atCenterOf(pos).add(0, 3, 0);
                    arrow.moveTo(at.x, at.y, at.z, 0, 90);
                    arrow.setDeltaMovement(0, -1, 0);
                    arrow.igniteForSeconds(10);
                    world.addFreshEntity(arrow);
                })),
                Step.until("nitra to be set off", 40, () -> nitraGone(pos)),
                Step.until("explosion on the client", 20, () -> Recorder.explosionsNear(pos, 3) >= 1),
                explosionCount(1, () -> pos, 3)
        );
    }

    @ClientTest("flint and steel explodes for the client")
    private static List<Step> nitraFlint() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.NITRA_BUNCH.get().defaultBlockState());
                    hold(player, Items.FLINT_AND_STEEL);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("nitra to be set off", 20, () -> nitraGone(pos)),
                Step.until("explosion on the client", 20, () -> Recorder.explosionsNear(pos, 3) >= 1),
                explosionCount(1, () -> pos, 3)
        );
    }

    @ClientTest("chain reaction explodes each bunch and drops no nitra")
    private static List<Step> nitraChain() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    pos = ahead(player, 10, 0);
                    Direction side = player.getDirection().getClockWise();
                    for (int i = 0; i < 3; i++) {
                        world.setBlockAndUpdate(pos.relative(side, i), BlockRegistry.NITRA_BUNCH.get().defaultBlockState());
                    }
                    world.setBlockAndUpdate(pos.relative(side, -1), Blocks.REDSTONE_BLOCK.defaultBlockState());
                })),
                Step.until("all 3 bunches to be set off", 40, () -> fromServer(server -> {
                    Direction side = player(server).getDirection().getClockWise();
                    for (int i = 0; i < 3; i++) {
                        if (!world(server).getBlockState(pos.relative(side, i)).isAir()) return false;
                    }
                    return true;
                })),
                Step.until("3 explosions on the client", 20, () -> fromServer(server ->
                        Recorder.explosionsNear(pos.relative(player(server).getDirection().getClockWise()), 4) >= 3)),
                explosionCount(3, () -> fromServer(server -> pos.relative(player(server).getDirection().getClockWise())), 4),
                Step.run(0, () -> onServer(server -> {
                    Map<Item, Integer> dropped = drops(world(server), pos, 5);
                    check(!dropped.containsKey(BlockRegistry.NITRA_BUNCH.get().asItem()), "exploded nitra dropped itself: " + describe(dropped));
                }))
        );
    }

    @ClientTest("redstone makes one explosion at the bunch's center")
    private static List<Step> nitraExplodesOnce() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    pos = ahead(player, 10, 0);
                    world.setBlockAndUpdate(pos, BlockRegistry.NITRA_BUNCH.get().defaultBlockState());
                    world.setBlockAndUpdate(pos.above(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                })),
                Step.until("nitra to be set off", 20, () -> nitraGone(pos)),
                explosionCount(1, () -> pos, 3),
                Step.run(0, () -> check(Recorder.explosionsNear(pos, 0.1) == 1, "explosion is not at the center of the bunch: " + Recorder.explosions))
        );
    }

    @ClientTest("thrown nitra explodes on the client")
    private static List<Step> thrownNitra() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    ThrownNitraEntity nitra = new ThrownNitraEntity(world, player);
                    nitra.setItem(new ItemStack(NITRA_BULB.get()));
                    Vec3 at = Vec3.atCenterOf(ahead(player, 6, 0)).add(0, 2, 0);
                    nitra.moveTo(at.x, at.y, at.z, 0, 0);
                    nitra.setDeltaMovement(0, -0.5, 0);
                    world.addFreshEntity(nitra);
                    entityId = nitra.getId();
                })),
                Step.until("nitra to hit the ground", 60, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.until("level event 2400 on the client", 20, () -> Recorder.levelEvents.stream().anyMatch(e -> e.startsWith("2400@"))),

                Step.run(10, () -> check(client().level.getEntity(entityId) == null, "client still has the thrown nitra"))
        );
    }

    private static Step explosionCount(int bunches, Supplier<BlockPos> center, double radius) {
        return Step.run(20, () -> {
            long explosions = Recorder.explosionsNear(center.get(), radius);
            check(explosions == bunches, explosions + " explosions on the client from " + bunches + " nitra bunches");
        });
    }

    private static boolean nitraGone(BlockPos nitraPos) {
        return fromServer(server -> !world(server).getBlockState(nitraPos).is(BlockRegistry.NITRA_BUNCH.get()));
    }
}
