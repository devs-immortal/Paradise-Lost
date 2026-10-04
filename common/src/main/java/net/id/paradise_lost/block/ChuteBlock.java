package net.id.paradise_lost.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class ChuteBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    protected static final Map<Direction.Axis, VoxelShape> SHAPES;

    public ChuteBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(WATERLOGGED, ctx.getLevel().isWaterAt(ctx.getClickedPos()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(AXIS));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    static {
        var builder = ImmutableMap.<Direction.Axis, VoxelShape>builder();
        builder.put(Direction.Axis.X, Shapes.join(Shapes.block(), Block.box(0, 2, 2, 16, 14, 14), BooleanOp.ONLY_FIRST));
        builder.put(Direction.Axis.Y, Shapes.join(Shapes.block(), Block.box(2, 0, 2, 14, 16, 14), BooleanOp.ONLY_FIRST));
        builder.put(Direction.Axis.Z, Shapes.join(Shapes.block(), Block.box(2, 2, 0, 14, 14, 16), BooleanOp.ONLY_FIRST));
        SHAPES = builder.build();
    }
}
