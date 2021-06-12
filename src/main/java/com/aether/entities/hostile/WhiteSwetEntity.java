package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WhiteSwetEntity extends SwetEntity{
    public WhiteSwetEntity(Level world){
        super(AetherEntityTypes.WHITE_SWET, world);
    }

    public void playerTouch(Player player){
        Collection<MobEffectInstance> effects = player.getActiveEffects();
        for (MobEffectInstance effect : effects) {
            this.forceAddEffect(effect, this);
            player.removeEffect(effect.getEffect());
        }
        super.playerTouch(player);
    }
}
