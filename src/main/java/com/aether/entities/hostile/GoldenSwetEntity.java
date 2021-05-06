package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class GoldenSwetEntity extends SwetEntity{
    public GoldenSwetEntity(World world){
        super(AetherEntityTypes.GOLDEN_SWET, world);
    }

    protected void init(){
        super.init();
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(40);
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        setHealth(getMaxHealth());
    }
}
