package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PurpleSwetEntity extends SwetEntity{
    public PurpleSwetEntity(Level world){
        super(AetherEntityTypes.PURPLE_SWET, world);
    }

    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
    }

    public void playerTouch(Player player){
        if (player.getVehicle() == null){
            if (stuckCooldown <= 0) {
                player.startRiding(this, true);
            } else {
                this.dealDamage(player);
            }
        }
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 4, 1));
    }
}
