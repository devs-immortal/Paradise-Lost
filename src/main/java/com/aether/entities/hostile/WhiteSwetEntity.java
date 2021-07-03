package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;

public class WhiteSwetEntity extends SwetEntity{

    public WhiteSwetEntity(World world){
        super(AetherEntityTypes.WHITE_SWET, world);
        resize(2);
    }

    @Override
    protected void onEntityCollision(Entity entity){
        if (getSize() > 1 && entity instanceof LivingEntity livingEntity) {
            StatusEffectInstance[] effects = livingEntity.getStatusEffects().toArray(new StatusEffectInstance[0]);
            for (StatusEffectInstance effect : effects) {
                this.setStatusEffect(effect, this);
                livingEntity.removeStatusEffect(effect.getEffectType());
            }
        }
        if(entity.getVehicle().equals(this)){
            if (entity instanceof CockatriceEntity || entity instanceof AechorPlantEntity) {
                this.changeType(AetherEntityTypes.PURPLE_SWET);
            }
        }
        super.onEntityCollision(entity);
    }
}
