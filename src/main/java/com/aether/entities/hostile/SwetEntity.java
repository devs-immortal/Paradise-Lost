package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class SwetEntity extends SlimeEntity {
    public int stuckCooldown = 0;

    public SwetEntity(World world) {
        this(AetherEntityTypes.WHITE_SWET, world);
    }

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
        init(2);
    }

    protected void init(int size) {
        super.setSize(size, false);
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
    public void tick() {
        if (stuckCooldown >= 0) { --stuckCooldown; }
        // entities don't have onEntityCollision, so this does that
        world.getOtherEntities(this, this.getBoundingBox()).forEach(this::onEntityCollision);
        super.tick();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        // Already taken care of in tick()
    }

    protected void onEntityCollision(Entity entity){
        if (!(entity instanceof SwetEntity swet)) {
            if (getSize() > 1 && entity.getVehicle() == null && stuckCooldown <= 0) {
                entity.startRiding(this, true);
            } else if (entity instanceof LivingEntity)
                this.damage((LivingEntity) entity);
        }
        // optional fun idea: swets of the same size combine and grow
        /*else {
            if (swet.getSize() == this.getSize()){
                this.resize(this.getSize() + 1);
                swet.discard();
            }
        }*/
    }

    protected void removePassenger(Entity passenger) {
        stuckCooldown = 30;
        super.removePassenger(passenger);
    }

    public void resize(int size) {
        super.setSize(size, false);
    }

    // prevents the game from setting a random size when spawned
    @Override
    protected void setSize(int size, boolean heal){
        if (heal) {
            this.setHealth(this.getMaxHealth());
        }
    }

    // Prevents duplicate entities
    @Override
    public void remove(RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.emitGameEvent(GameEvent.ENTITY_KILLED);
        }
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleTypes.SPLASH;
    }

    @Override
    protected Identifier getLootTableId() {
        return this.getType().getLootTableId();
    }

    protected void changeType(EntityType<? extends SwetEntity> type){
        if(!this.getType().equals(type) && !this.isRemoved()) {
            SwetEntity entity = type.create(world);
            entity.resize(this.getSize());
            entity.setHealth(this.getHealth());
            entity.setPosition(this.getPos());
            this.getStatusEffects().forEach(entity::addStatusEffect);
            entity.lookDirection = this.lookDirection;
            entity.setVelocity(this.getVelocity());
            List<Entity> passengers = this.getPassengerList();
            passengers.forEach((passenger) -> {
                this.removePassenger(passenger);
                entity.addPassenger(passenger);
            });
            this.removeAllPassengers();
            this.discard();
            world.spawnEntity(entity);
        }
    }
}
