package net.id.aether.entities.hostile.swet;

import net.id.aether.client.rendering.particle.AetherParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlueSwetEntity extends SwetEntity {
    public BlueSwetEntity(EntityType<? extends BlueSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY());
            int k = MathHelper.floor(this.getZ());
            BlockPos blockPos = new BlockPos(i, j, k);
            if (this.world.getBiome(blockPos).getTemperature() > 1.0f) {
                this.damage(DamageSource.ON_FIRE, 1.0f);
            }
        }
    }

    @Override
    protected ParticleEffect getParticles() {
        return AetherParticles.BLUE_SPLASH;
    }
}
