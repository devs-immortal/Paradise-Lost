package net.id.paradise_lost.test.server;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class NitraTests {
    private static BlockPos pos;

    private NitraTests() {
    }

    @ServerTest("explosion breaks a nearby block and drops it")
    private static List<Step> nitraBreaksBlocks() {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    pos = ahead(10, 0);
                    world.setBlockAndUpdate(pos.relative(FACING.getClockWise()), Blocks.SAND.defaultBlockState());
                    world.setBlockAndUpdate(pos, BlockRegistry.NITRA_BUNCH.get().defaultBlockState());
                    world.setBlockAndUpdate(pos.above(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                }),
                Step.until("nitra to be set off", 20, () -> !level().getBlockState(pos).is(BlockRegistry.NITRA_BUNCH.get())),
                Step.run(5, () -> {
                    ServerLevel world = level();
                    check(!world.getBlockState(pos.relative(FACING.getClockWise())).is(Blocks.SAND), "sand next to the nitra was not destroyed");
                    Map<Item, Integer> dropped = drops(world, pos, 5);
                    check(dropped.containsKey(Items.SAND), "explosion dropped " + describe(dropped) + " no sand");
                })
        );
    }
}
