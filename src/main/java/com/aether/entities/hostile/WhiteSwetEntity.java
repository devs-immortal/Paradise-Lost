package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Collection;

public class WhiteSwetEntity extends SwetEntity{
    public WhiteSwetEntity(World world){
        super(AetherEntityTypes.WHITE_SWET, world);
    }

    public void onPlayerCollision(PlayerEntity player){
        Collection<StatusEffectInstance> effects = player.getStatusEffects();
        for (StatusEffectInstance effect : effects) {
            this.setStatusEffect(effect, this);
            player.removeStatusEffect(effect.getEffectType());
        }
        super.onPlayerCollision(player);
    }
}
