package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ColdAercloudBlock extends BaseAercloudBlock {

    public ColdAercloudBlock() {
        super(BlockBehaviour.Properties.of(Material.SNOW).sound(SoundType.SNOW));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (entity.getDeltaMovement().y <= 0.0F) entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.005D, 1.0D));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.LAND;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context){
        if (world.getBlockState(pos.above()).is(AetherBlocks.COLD_AERCLOUD))
            return Shapes.block();
        else if ((world.getBlockState(pos.below()).getBlock() instanceof BaseAercloudBlock) || !(world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.DOWN)))
            return Block.box(0, 0, 0, 16, 0.001, 16);
        else
            return Shapes.empty();
    }

}
