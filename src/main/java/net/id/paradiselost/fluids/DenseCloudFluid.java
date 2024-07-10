package net.id.paradiselost.fluids;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class DenseCloudFluid extends FlowableFluid {

    @Override
    public Fluid getFlowing() {
        return ParadiseLostFluids.DENSE_CLOUD;
    }

    @Override
    public Fluid getStill() {
        return ParadiseLostFluids.DENSE_CLOUD;
    }

    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
    }

    @Override
    protected int getFlowSpeed(WorldView world) {
        return 0;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 0;
    }

    @Override
    public Item getBucketItem() {
        return Items.BUCKET;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 0;
    }

    @Override
    protected float getBlastResistance() {
        return 100F;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ParadiseLostBlocks.DENSE_CLOUD.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
        builder.add(LEVEL);
    }

    @Override
    public boolean isStill(FluidState state) {
        return true;
    }

    @Override
    public int getLevel(FluidState state) {
        return 8;
    }

    @Override
    protected void tryFlow(World world, BlockPos fluidPos, FluidState state) {
    }
}
