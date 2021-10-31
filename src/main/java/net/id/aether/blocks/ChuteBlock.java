package net.id.aether.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class ChuteBlock extends PillarBlock implements Waterloggable {

    protected static final Map<Direction.Axis, VoxelShape> SHAPES;

    public ChuteBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(WATERLOGGED, ctx.getWorld().isWater(ctx.getBlockPos()));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(AXIS));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    static {
        var builder = ImmutableMap.<Direction.Axis, VoxelShape>builder();
        builder.put(Direction.Axis.X, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(0, 2, 2, 16, 14, 14), BooleanBiFunction.ONLY_FIRST));
        builder.put(Direction.Axis.Y, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(2, 0, 2, 14, 16, 14), BooleanBiFunction.ONLY_FIRST));
        builder.put(Direction.Axis.Z, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(2, 2, 0, 14, 14, 16), BooleanBiFunction.ONLY_FIRST));
        SHAPES = builder.build();
    }
}
