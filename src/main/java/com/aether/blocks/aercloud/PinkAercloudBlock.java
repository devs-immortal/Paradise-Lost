package com.aether.blocks.aercloud;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class PinkAercloudBlock extends BaseAercloudBlock {

    private static final ParticleEffect pinkFluff = new DustParticleEffect(new Vec3f(0.89F, 0.65F, 0.9F), 1F);

    public PinkAercloudBlock(AbstractBlock.Settings properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (!world.isClient() && entity instanceof LivingEntity) {
            if (world.getTime() % 20 == 0) {
                ((LivingEntity) entity).heal(1F);
                for (int i = world.getRandom().nextInt(3); i <= 5; i++) {
                    double offX = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
                    double offZ = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
                    double offY = world.getRandom().nextDouble() * entity.getHeight();
                    ((ServerWorld) world).spawnParticles(pinkFluff, entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ, 3, 0, 0, 0, 1);
                }
            }
            double offX = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
            double offZ = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
            double offY = world.getRandom().nextDouble() * entity.getHeight();
            ((ServerWorld) world).spawnParticles(pinkFluff, entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ, 1, 0, 0, 0, 0.1);
        }
    }
}
