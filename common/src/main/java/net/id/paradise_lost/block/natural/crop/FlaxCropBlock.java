package net.id.paradise_lost.block.natural.crop;

import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class FlaxCropBlock extends TallCropBlock {

    public FlaxCropBlock(Properties settings) {
        super(settings, 2);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (this.getHalf(state) == DoubleBlockHalf.UPPER) {
            return;
        }
        var data = new Object() {
            int stoneSpots = 0;
        };
        BlockPos.withinManhattan(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            BlockState checkState = world.getBlockState(check);
            if (checkState.is(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE) || checkState.is(BlockTags.BASE_STONE_OVERWORLD) || checkState.is(Blocks.GRAVEL)) {
                data.stoneSpots++;
            }
        });
        if (data.stoneSpots == 0) {
            tryGrow(state, world, pos, random, 20F);
        } else {
            tryGrow(state, world, pos, random, 7F + 16F / data.stoneSpots);
        }
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ItemRegistry.FLAXSEED.get();
    }

}
