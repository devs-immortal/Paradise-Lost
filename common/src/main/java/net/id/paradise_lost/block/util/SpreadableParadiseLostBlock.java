package net.id.paradise_lost.block.util;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public abstract class SpreadableParadiseLostBlock extends SnowyDirtBlock {
    protected SpreadableParadiseLostBlock(Properties settings) {
        super(settings);
    }

    private static boolean canGrassSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getAmount() == 8) {
            return false;
        }
        int i = LightEngine.getLightBlockInto(state, blockState, Direction.UP, blockState.getLightBlock());
        return i < LightEngine.MAX_LEVEL;
    }

    private static boolean canSpread(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return canGrassSurvive(state, world, pos) && !world.getFluidState(blockPos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canGrassSurvive(state, world, pos)) {
            world.setBlockAndUpdate(pos, BlockRegistry.DIRT.get().defaultBlockState());
        } else {
            if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockState = this.defaultBlockState();

                for (int i = 0; i < 4; ++i) {
                    BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos).is(BlockRegistry.DIRT.get()) && canSpread(blockState, world, blockPos))
                        world.setBlockAndUpdate(blockPos, blockState.setValue(SNOWY, world.getBlockState(blockPos.above()).is(Blocks.SNOW)));
                }
            }
        }
    }
}
