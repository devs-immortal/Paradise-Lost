package net.id.paradiselost.blocks.natural.plant;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class TallWaterPlantBlock extends TallPlantBlock implements Waterloggable {

    public TallWaterPlantBlock(Settings settings) {
        super(settings.offset(OffsetType.XYZ));
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ParadiseLostBlocks.FLOESTONE) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            return world.isWater(pos) && super.canPlaceAt(state, world, pos);
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.15F;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.WATERLOGGED);
    }
}
