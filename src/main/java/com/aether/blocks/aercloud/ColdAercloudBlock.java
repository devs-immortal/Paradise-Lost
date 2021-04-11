package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ColdAercloudBlock extends BaseAercloudBlock {

    public ColdAercloudBlock() {
        super(FabricBlockSettings.of(Material.SNOW_BLOCK).sounds(BlockSoundGroup.SNOW));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (entity.getVelocity().y <= 0.0F) entity.setVelocity(entity.getVelocity().multiply(1.0D, 0.005D, 1.0D));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.LAND;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        if (world.getBlockState(pos.up()).getBlock().equals(AetherBlocks.COLD_AERCLOUD))
            return VoxelShapes.fullCube();
        else if ((world.getBlockState(pos.down()).getBlock() instanceof BaseAercloudBlock) || !(world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.DOWN)))
            return Block.createCuboidShape(0, 0, 0, 16, 0.001, 16);
        else
            return VoxelShapes.empty();
    }

}
