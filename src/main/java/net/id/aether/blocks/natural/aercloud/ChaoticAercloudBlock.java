package net.id.aether.blocks.natural.aercloud;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class ChaoticAercloudBlock extends AercloudBlock {

    protected static VoxelShape SHAPE = VoxelShapes.empty();

    public ChaoticAercloudBlock(Settings properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos blockPos, Entity entity) {
        entity.fallDistance = 0.0F;
        Direction facing = DIRECTIONS[new Random().nextInt(DIRECTIONS.length)];
        Vec3d motion = entity.getVelocity();

        if (entity.isSneaking()) {
            if (motion.y < 0) {
                entity.setVelocity(motion.multiply(1.0, 0.005, 1.0));
            }
            return;
        }

        entity.setVelocity(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
