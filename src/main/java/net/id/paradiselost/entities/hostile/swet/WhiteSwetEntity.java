package net.id.paradiselost.entities.hostile.swet;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
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
                setStatusEffect(effect, livingEntity);
                livingEntity.removeStatusEffect(effect.getEffectType());
            }
        }
        super.onEntityCollision(entity);
    }
    
    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return SwetEntity.canSpawn(type, world, spawnReason, pos, random) && (world.getBiome(pos).value().getTemperature() <= 0.4 || world.getRandom().nextFloat() < 0.3);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        return ParadiseLostParticles.coloredSplash(0x87_87_87);
    }
}
