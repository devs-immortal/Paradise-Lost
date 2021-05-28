package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PurpleSwetEntity extends SwetEntity{
    public PurpleSwetEntity(World world){
        super(AetherEntityTypes.PURPLE_SWET, world);
    }

    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
    }

    public void onPlayerCollision(PlayerEntity player){
        if (player.getVehicle() == null){
            if (stuckCooldown <= 0) {
                player.startRiding(this, true);
            } else {
                this.damage(player);
            }
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 4, 1));
    }
}
