package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseAercloudBlock extends Block {

    public BaseAercloudBlock(Properties settings) {
        super(settings.strength(0.2F, -1.0F).isViewBlocking(AetherBlocks::never).isSuffocating(AetherBlocks::never).noOcclusion());
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return state.is(stateFrom.getBlock()) || super.skipRendering(state, stateFrom, direction);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.5F;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if ((world.getBlockState(pos.below()).getBlock() instanceof BaseAercloudBlock) || !(world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.DOWN)))
            return Block.box(0, 0, 0, 16, 0.001, 16);
        else
            return Shapes.empty();
    }
}
