package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

public class PurpleSwetEntity extends SwetEntity{
    public PurpleSwetEntity(World world){
        super(AetherEntityTypes.PURPLE_SWET, world);
        resize(2);
    }

    protected void onEntityCollision(Entity entity){
        if (entity instanceof LivingEntity livingEntity)
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 4, 1));
        super.onEntityCollision(entity);
    }
}
