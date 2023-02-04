package net.id.paradiselost.blocks.natural.cloud;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GoldenParadiseLostCloudBlock extends ParadiseLostCloudBlock {

    protected static VoxelShape SHAPE = VoxelShapes.empty();

    public GoldenParadiseLostCloudBlock(Settings properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3d motion = entity.getVelocity();
        BlockPos position = entity.getBlockPos();

        if ((Math.abs(motion.getY()) > 0.1f) && canFallThrough(world.getBlockState(position.down())) && canFallThrough(world.getBlockState(position.down().down()))) {
            entity.fallDistance = 23F;
            entity.setVelocity(motion.x, -3.9, motion.z);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    private boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable() || state == ParadiseLostBlocks.GOLDEN_CLOUD.getDefaultState();
    }
}
