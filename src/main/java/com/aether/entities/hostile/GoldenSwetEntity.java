package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class GoldenSwetEntity extends SwetEntity{
    public GoldenSwetEntity(Level world){
        super(AetherEntityTypes.GOLDEN_SWET, world);
    }

    protected void init(){
        super.init();
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(40);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2);
        setHealth(getMaxHealth());
    }
}
