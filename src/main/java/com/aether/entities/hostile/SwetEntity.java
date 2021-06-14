package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class SwetEntity extends SlimeEntity {
    public int stuckCooldown = 0;

    public SwetEntity(World world){
        super(AetherEntityTypes.BLUE_SWET, world);
        init();
    }

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world){
        super(entityType, world);
        init();
    }

    protected void init(){
        if (this instanceof GoldenSwetEntity) {
            super.setSize(4, false);
        } else {
            super.setSize(2, false);
        }
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(25);
        setHealth(getMaxHealth());
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28000000417232513D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D);
    }

    @Override
    public void tick(){
        if(stuckCooldown >= 0){
            --stuckCooldown;
        }
        super.tick();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player){
        if (player.getVehicle() == null && stuckCooldown <= 0) {
            player.startRiding(this, true);
        } else {
            super.onPlayerCollision(player);
        }
    }

    protected void removePassenger(Entity passenger) {
        if (passenger instanceof PlayerEntity) {
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
    protected ParticleEffect getParticles() {
        return ParticleTypes.SPLASH;
    }
}
