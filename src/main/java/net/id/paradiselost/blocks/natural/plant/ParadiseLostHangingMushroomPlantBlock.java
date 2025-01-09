package net.id.paradiselost.blocks.natural.plant;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class ParadiseLostHangingMushroomPlantBlock extends ParadiseLostMushroomPlantBlock {
    public ParadiseLostHangingMushroomPlantBlock(TagKey<Block> plantableOn, Settings settings) {
        super(plantableOn, null, settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(5.0D, 10.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState ceil, BlockView world, BlockPos pos) {
        return ceil.isOpaqueFullCube(world, pos);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isIn(plantableOn)) {
            return true;
        } else {
            return world.getBaseLightLevel(pos, 0) < 11 && canPlantOnTop(blockState, world, blockPos);
        }
    }

    @Override
    public void grow(ServerWorld world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        int mushroomHeight = random.nextInt(4)+4;
        BlockState stemState = Blocks.MUSHROOM_STEM.getDefaultState().with(MushroomBlock.DOWN, false);
        BlockState capState = ParadiseLostBlocks.PINK_SPORECAP_BLOCK.getDefaultState().with(MushroomBlock.UP, false);
        if (this.hasRoomToGrow(mushroomHeight+1, world, pos)) {
            // stem
            for (int i = 0; i < mushroomHeight; i++) {
                world.setBlockState(pos.down(i), stemState);
            }
            // layers
            for (int i = 1; i <= 2; i++) {
                for (Direction d : ConnectingBlock.FACING_PROPERTIES.keySet()) {
                    if (!d.getAxis().isHorizontal()) continue;
                    BlockPos layer = pos.down(mushroomHeight - i).offset(d, 2);
                    BlockState insideCapState = capState.with(ConnectingBlock.FACING_PROPERTIES.get(d.getOpposite()), false);
                    world.setBlockState(layer, insideCapState);
                    world.setBlockState(layer.offset(d.rotateClockwise(Direction.Axis.Y)), insideCapState);
                    world.setBlockState(layer.offset(d.rotateCounterclockwise(Direction.Axis.Y)), insideCapState);
                }
            }
            // middle level
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    world.setBlockState(pos.down(mushroomHeight).north(i).east(j), capState);
                }
            }
            // extra detail
            if (mushroomHeight >= 5 && random.nextBoolean()) {
                BlockPos layer = pos.down(random.nextBetween(1, mushroomHeight-4));
                Direction d = (Direction) ConnectingBlock.FACING_PROPERTIES.keySet().toArray()[random.nextBetween(2, 5)];
                world.setBlockState(layer.offset(d), capState);
                world.setBlockState(layer.offset(d).offset(d.rotateClockwise(Direction.Axis.Y)), capState);
                world.setBlockState(layer.offset(d).offset(d.rotateClockwise(Direction.Axis.Y)).offset(d.getOpposite()), capState);
            }
        }
    }

    private boolean hasRoomToGrow(int mushroomHeight, ServerWorld world, BlockPos pos) {
        for (int j = -1; j >= -mushroomHeight; j--) {
            int k = 3;

            for (int l = -k; l <= k; l++) {
                for (int m = -k; m <= k; m++) {
                    BlockState blockState2 = world.getBlockState(pos.add(l, j, m));
                    if (!blockState2.isAir() && !blockState2.isIn(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
