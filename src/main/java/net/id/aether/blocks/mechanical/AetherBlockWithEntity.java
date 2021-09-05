package net.id.aether.blocks.mechanical;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AetherBlockWithEntity extends BlockWithEntity implements Waterloggable {

    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final boolean loggable;

    protected AetherBlockWithEntity(Settings settings, boolean loggable) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false).with(POWERED, false));
        this.loggable = loggable;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return ctx.getWorld().isWater(ctx.getBlockPos()) ? super.getPlacementState(ctx).with(WATERLOGGED, true) : super.getPlacementState(ctx);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return loggable && !(Boolean) state.get(Properties.WATERLOGGED) && fluid == Fluids.WATER;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        if (powered != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, powered), 3);
            pulseUpdate(state, world, pos, powered);
        }
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    protected void pulseUpdate(BlockState state, World world, BlockPos pos, boolean on) {
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, WATERLOGGED);
    }
}
