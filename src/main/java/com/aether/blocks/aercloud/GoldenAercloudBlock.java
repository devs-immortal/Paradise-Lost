package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GoldenAercloudBlock extends BaseAercloudBlock {

    public GoldenAercloudBlock() {
        super(FabricBlockSettings.of(Material.SNOW_BLOCK, MaterialColor.GOLD).sounds(BlockSoundGroup.SNOW));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if ((Math.abs(entity.getVelocity().getY()) > 0.1f) && canFallThrough(world.getBlockState(entity.getBlockPos().down())) && canFallThrough(world.getBlockState(entity.getBlockPos().down().down()))) {
            entity.fallDistance = 23F;
            entity.setVelocity(entity.getVelocity().x, -3.9, entity.getVelocity().z);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable() || state == AetherBlocks.GOLDEN_AERCLOUD.getDefaultState();
    }
}
