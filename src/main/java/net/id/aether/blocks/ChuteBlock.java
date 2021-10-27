package net.id.aether.blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.Map;

public class ChuteBlock extends PillarBlock {

    protected static final Map<Direction.Axis, VoxelShape> SHAPES;

    public ChuteBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(AXIS));
    }

    static {
        var builder = ImmutableMap.<Direction.Axis, VoxelShape>builder();
        builder.put(Direction.Axis.X, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(0, 2, 2, 16, 14, 14), BooleanBiFunction.ONLY_FIRST));
        builder.put(Direction.Axis.Y, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(2, 0, 2, 14, 16, 14), BooleanBiFunction.ONLY_FIRST));
        builder.put(Direction.Axis.Z, VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(2, 2, 0, 14, 14, 16), BooleanBiFunction.ONLY_FIRST));
        SHAPES = builder.build();
    }
}
