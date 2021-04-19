package com.aether.blocks.natural;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class AetherHangerBlock extends PlantBlock {

    public AetherHangerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.fallDistance = 0;
        }
    }

    @Override
    protected boolean canPlantOnTop(BlockState ceiling, BlockView world, BlockPos pos) {
        return ceiling.getBlock() instanceof AetherHangerBlock || ceiling.getBlock() instanceof LeavesBlock || ceiling.isSideSolidFullSquare(world, pos, Direction.DOWN);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.canPlantOnTop(world.getBlockState(pos.up()), world, pos.up());
    }
}
