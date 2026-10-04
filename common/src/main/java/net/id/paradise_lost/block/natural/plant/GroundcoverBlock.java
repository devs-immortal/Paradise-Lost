package net.id.paradise_lost.block.natural.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GroundcoverBlock extends ParadiseLostBrushBlock {

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public GroundcoverBlock(Properties settings) {
        super(settings.offsetType(OffsetType.NONE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
