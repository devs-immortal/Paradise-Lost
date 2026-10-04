package net.id.paradise_lost.block.natural.crop;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AmadrysCropBlock extends CropBlock {

    public AmadrysCropBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        final int[] waterSpots = {0};
        BlockPos.withinManhattan(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            if (world.isWaterAt(check)) {
                waterSpots[0]++;
            }
        });
        if (waterSpots[0] > 1) {
            super.randomTick(state, world, pos, random);
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ItemRegistry.AMADRYS_BUSHEL.get();
    }
}
