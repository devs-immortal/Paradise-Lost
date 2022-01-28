package net.id.aether.entities.hostile.swet;

import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.client.rendering.particle.ColoredSplashParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

// Not yet implemented, not planned for 1.6.0
public class VermilionSwetEntity extends SwetEntity {
    public VermilionSwetEntity(EntityType<? extends VermilionSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityCollision(Entity entity) {
        if (entity instanceof TntMinecartEntity tnt) {
            if (!tnt.isPrimed() && this.getSize() >= 4) {
                tnt.prime();
            }
        }
        super.onEntityCollision(entity);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        // Missing texture magenta
        return AetherParticles.coloredSplash(0xFF_00_FF);
    }
}
