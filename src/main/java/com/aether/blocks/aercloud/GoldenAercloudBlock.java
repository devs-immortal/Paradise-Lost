package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GoldenAercloudBlock extends BaseAercloudBlock {

    public GoldenAercloudBlock() {
        super(FabricBlockSettings.of(Material.SNOW, MaterialColor.GOLD).sound(SoundType.SNOW));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if ((Math.abs(entity.getDeltaMovement().y()) > 0.1f) && canFallThrough(world.getBlockState(entity.blockPosition().below())) && canFallThrough(world.getBlockState(entity.blockPosition().below().below()))) {
            entity.fallDistance = 23F;
            entity.setDeltaMovement(entity.getDeltaMovement().x, -3.9, entity.getDeltaMovement().z);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable() || state == AetherBlocks.GOLDEN_AERCLOUD.defaultBlockState();
    }
}
