package com.aether.entities.hostile.swet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

public class PurpleSwetEntity extends SwetEntity {
    public PurpleSwetEntity(EntityType<? extends PurpleSwetEntity> entityType, World world){
        super(entityType, world);
    }

    protected void onEntityCollision(Entity entity){
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 4, 1));
        }
        super.onEntityCollision(entity);
    }
}
