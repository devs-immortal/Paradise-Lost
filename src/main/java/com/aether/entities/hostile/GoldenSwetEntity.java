package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.world.World;

public class GoldenSwetEntity extends SwetEntity{
    public GoldenSwetEntity(World world){
        super(AetherEntityTypes.GOLDEN_SWET, world, 4);
    }
    public GoldenSwetEntity(World world, int size){
        super(AetherEntityTypes.GOLDEN_SWET, world, size);
    }

    @Override
    protected void init(int size){
        super.init(4);
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(40);
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        setHealth(getMaxHealth());
    }
}
