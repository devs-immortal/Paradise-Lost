package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BaseAercloudBlock extends Block {

    public BaseAercloudBlock(Settings settings) {
        super(settings.strength(0.2F, -1.0F).blockVision(AetherBlocks::never).suffocates(AetherBlocks::never).nonOpaque());
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return state.isOf(stateFrom.getBlock()) || super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 0.5F;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if((world.getBlockState(pos.down()).getBlock() instanceof BaseAercloudBlock) || !(world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.DOWN)))
            return Block.createCuboidShape(0, 0, 0, 16, 0.001, 16);
        else
            return VoxelShapes.empty();
    }
}
