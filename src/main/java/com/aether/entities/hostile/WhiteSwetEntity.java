package com.aether.entities.hostile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aether.entities.AetherEntityTypes;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class WhiteSwetEntity extends SwetEntity{
    public WhiteSwetEntity(World world){
        super(AetherEntityTypes.WHITE_SWET, world);
    }

    public void onPlayerCollision(PlayerEntity player){
    	List<StatusEffectInstance> parseableEffects = new ArrayList<StatusEffectInstance>();
        Collection<StatusEffectInstance> effects = player.getStatusEffects();
        parseableEffects.addAll(effects);
        
        for (StatusEffectInstance effect : parseableEffects) {
            this.applyStatusEffect(effect);
            player.removeStatusEffect(effect.getEffectType());
        }
        super.onPlayerCollision(player);
    }
}
