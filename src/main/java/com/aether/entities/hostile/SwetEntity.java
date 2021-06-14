package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SwetEntity extends Slime {
    public int stuckCooldown = 0;

    public SwetEntity(Level world){
        super(AetherEntityTypes.BLUE_SWET, world);
        init();
    }

    public SwetEntity(EntityType<? extends SwetEntity> entityType, Level world){
        super(entityType, world);
        init();
    }

    protected void init(){
        if (this instanceof GoldenSwetEntity) {
            super.setSize(4, false);
        } else {
            super.setSize(2, false);
        }
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(25);
        setHealth(getMaxHealth());
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28000000417232513D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 25.0D);
    }

    @Override
    public void tick(){
        if(stuckCooldown >= 0){
            --stuckCooldown;
        }
        super.tick();
    }

    @Override
    public void playerTouch(Player player){
        if (player.getVehicle() == null && stuckCooldown <= 0) {
            player.startRiding(this, true);
        } else {
            super.playerTouch(player);
        }
    }

    protected void removePassenger(Entity passenger) {
        if (passenger instanceof Player) {
            stuckCooldown = 30;
        }
        super.removePassenger(passenger);
    }

    // Prevents the size from being changed
    @Override
    protected void setSize(int size, boolean heal){
        if (heal) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.SPLASH;
    }
}
