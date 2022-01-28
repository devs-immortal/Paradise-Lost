package net.id.aether.blocks.natural.aercloud;

import net.id.aether.client.rendering.particle.AetherParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlueAercloudBlock extends AercloudBlock {

    protected static VoxelShape SHAPE = VoxelShapes.empty();

    public BlueAercloudBlock(Settings properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3d motion = entity.getVelocity();

        if (entity.isSneaking()) {
            if (motion.y < 0) {
                entity.setVelocity(motion.multiply(1.0, 0.005, 1.0));
            }
            return;
        }

        if (motion.y < 2) {
            if (-1.4 < motion.y) {
                entity.setVelocity(motion.x, 2.0, motion.z);
            } else {
                entity.setVelocity(motion.x, -1.4 * motion.y, motion.z);
            }
        }

        if (world.isClient && !entity.verticalCollision && !(entity instanceof PlayerEntity player && player.isCreative())) {
            for (int count = 0; count < 50; count++) {
                double xOffset = pos.getX() + world.random.nextDouble();
                double yOffset = pos.getY() + world.random.nextDouble();
                double zOffset = pos.getZ() + world.random.nextDouble();

                world.addParticle(AetherParticles.BLUE_SPLASH, xOffset, yOffset, zOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
