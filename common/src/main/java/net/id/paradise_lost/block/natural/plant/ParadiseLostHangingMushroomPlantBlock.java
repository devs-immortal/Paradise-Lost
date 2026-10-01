package net.id.paradise_lost.block.natural.plant;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;

public class ParadiseLostHangingMushroomPlantBlock extends ParadiseLostMushroomPlantBlock {
    public ParadiseLostHangingMushroomPlantBlock(TagKey<Block> plantableOn, Properties settings) {
        super(plantableOn, null, settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(5.0D, 10.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(plantableOn)) {
            return true;
        } else {
            return world.getRawBrightness(pos, 0) < 11 && mayPlaceOn(blockState, world, blockPos);
        }
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        int mushroomHeight = random.nextInt(4) + 4;
        BlockState stemState = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false);
        BlockState capState = BlockRegistry.PINK_SPORECAP_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.UP, false);
        if (this.hasRoomToGrow(mushroomHeight + 1, world, pos)) {

            for (int i = 0; i < mushroomHeight; i++) {
                world.setBlockAndUpdate(pos.below(i), stemState);
            }

            for (int i = 1; i <= 2; i++) {
                for (Direction d : PipeBlock.PROPERTY_BY_DIRECTION.keySet()) {
                    if (!d.getAxis().isHorizontal()) continue;
                    BlockPos layer = pos.below(mushroomHeight - i).relative(d, 2);
                    BlockState insideCapState = capState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(d.getOpposite()), false);
                    world.setBlockAndUpdate(layer, insideCapState);
                    world.setBlockAndUpdate(layer.relative(d.getClockWise(Direction.Axis.Y)), insideCapState);
                    world.setBlockAndUpdate(layer.relative(d.getCounterClockWise(Direction.Axis.Y)), insideCapState);
                }
            }

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    world.setBlockAndUpdate(pos.below(mushroomHeight).north(i).east(j), capState);
                }
            }

            if (mushroomHeight >= 5 && random.nextBoolean()) {
                BlockPos layer = pos.below(random.nextIntBetweenInclusive(1, mushroomHeight - 4));
                Direction d = (Direction) PipeBlock.PROPERTY_BY_DIRECTION.keySet().toArray()[random.nextIntBetweenInclusive(2, 5)];
                world.setBlockAndUpdate(layer.relative(d), capState);
                world.setBlockAndUpdate(layer.relative(d).relative(d.getClockWise(Direction.Axis.Y)), capState);
                world.setBlockAndUpdate(layer.relative(d).relative(d.getClockWise(Direction.Axis.Y)).relative(d.getOpposite()), capState);
            }
        }
    }

    private boolean hasRoomToGrow(int mushroomHeight, ServerLevel world, BlockPos pos) {
        for (int j = -1; j >= -mushroomHeight; j--) {
            int k = 3;

            for (int l = -k; l <= k; l++) {
                for (int m = -k; m <= k; m++) {
                    BlockState blockState2 = world.getBlockState(pos.offset(l, j, m));
                    if (!blockState2.isAir() && !blockState2.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
