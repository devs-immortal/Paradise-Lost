package net.id.paradiselost.blocks.natural.crop;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class SwetrootCropBlock extends CropBlock {
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[] {
        Block.createCuboidShape(0, 14, 0, 16, 16, 16),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16),
        Block.createCuboidShape(0, 10, 0, 16, 16, 16),
        Block.createCuboidShape(0, 8, 0, 16, 16, 16),
        Block.createCuboidShape(0, 6, 0, 16, 16, 16),
        Block.createCuboidShape(0, 4, 0, 16, 16, 16),
        Block.createCuboidShape(0, 2, 0, 16, 16, 16),
        Block.createCuboidShape(0, 0, 0, 16, 16, 16)
    };
    
    public SwetrootCropBlock(Settings settings) {
        super(settings.offsetType(OffsetType.XZ));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(getAgeProperty())];
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.up().up()).isOf(Blocks.WATER) && world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                if (random.nextInt(4) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }
    
    @Override
    protected ItemConvertible getSeedsItem() {
        return ParadiseLostItems.SWEDROOT;
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlantOnTop(Blocks.AIR.getDefaultState(), world, pos);
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isIn(ParadiseLostBlockTags.SWEDROOT_PLANTABLE);
    }

}