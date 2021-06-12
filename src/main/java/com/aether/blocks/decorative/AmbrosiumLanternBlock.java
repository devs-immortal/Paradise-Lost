package com.aether.blocks.decorative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmbrosiumLanternBlock extends LanternBlock {

    protected static final VoxelShape STANDING_SHAPE;
    protected static final VoxelShape HANGING_SHAPE;

    public AmbrosiumLanternBlock(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }


    static {
        STANDING_SHAPE = Shapes.or(Block.box(6.0D, 8.0D, 6.0D, 10.0D, 9.0D, 10.0D), Block.box(5.0D, 1.0D, 5.0D, 11.0D, 7.0D, 11.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D), Block.box(4.0D, 7.0D, 4.0D, 12.0D, 8.0D, 12.0D));
        HANGING_SHAPE = Shapes.or(Block.box(6.0D, 9.0D, 6.0D, 10.0D, 10.0D, 10.0D), Block.box(5.0D, 2.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0D, 12.0D), Block.box(4.0D, 8.0D, 4.0D, 12.0D, 9.0D, 12.0D));
    }
}