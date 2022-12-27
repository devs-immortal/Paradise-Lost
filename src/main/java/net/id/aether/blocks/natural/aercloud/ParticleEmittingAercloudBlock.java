package net.id.aether.blocks.natural.aercloud;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleEmittingAercloudBlock extends AercloudBlock {

    private final ParticleEffect effect;
    private final boolean heals;

    public ParticleEmittingAercloudBlock(Settings properties, ParticleEffect effect, boolean heals) {
        super(properties);
        this.effect = effect;
        this.heals = heals;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (!world.isClient() && entity instanceof LivingEntity) {
            if (world.getTime() % 20 == 0) {
                if (heals) ((LivingEntity) entity).heal(1F);
                for (int i = world.getRandom().nextInt(3); i <= 5; i++) {
                    double offX = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
                    double offZ = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
                    double offY = world.getRandom().nextDouble() * entity.getHeight();
                    ((ServerWorld) world).spawnParticles(effect, entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ, 3, 0, 0, 0, 1);
                }
            }
            double offX = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
            double offZ = (world.getRandom().nextDouble() * entity.getWidth()) - (entity.getWidth() / 2);
            double offY = world.getRandom().nextDouble() * entity.getHeight();
            ((ServerWorld) world).spawnParticles(effect, entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ, 1, 0, 0, 0, 0.1);
        }
    }
}
