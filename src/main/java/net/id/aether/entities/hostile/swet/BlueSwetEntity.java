package net.id.aether.entities.hostile.swet;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class BlueSwetEntity extends SwetEntity {
    public BlueSwetEntity(EntityType<? extends BlueSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleTypes.SPLASH;
    }
}
