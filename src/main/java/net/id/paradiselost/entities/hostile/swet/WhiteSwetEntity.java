package net.id.paradiselost.entities.hostile.swet;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class WhiteSwetEntity extends TransformableSwetEntity {
    public WhiteSwetEntity(EntityType<? extends WhiteSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityCollision(Entity entity) {
        if (getSize() > 1 && entity instanceof LivingEntity livingEntity) {
            StatusEffectInstance[] effects = livingEntity.getStatusEffects().toArray(new StatusEffectInstance[0]);
            for (StatusEffectInstance effect : effects) {
                this.setStatusEffect(effect, livingEntity);
                livingEntity.removeStatusEffect(effect.getEffectType());
            }
        }
        super.onEntityCollision(entity);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        return ParadiseLostParticles.coloredSplash(0x87_87_87);
    }
}
