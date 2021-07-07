package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class SwetEntity extends SlimeEntity {

    public int stuckCooldown = 0;

    public SwetEntity(World world) {
        this(AetherEntityTypes.WHITE_SWET, world);
    }

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
        init();
    }

    protected void init() {
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(25);
        setHealth(getMaxHealth());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
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
            if (!(entity instanceof PlayerEntity player && player.isCreative())) {
                if (getSize() > 1 && entity.getVehicle() == null && stuckCooldown <= 0) {
                    entity.startRiding(this, true);
                } else if (entity instanceof LivingEntity)
                    this.damage((LivingEntity) entity);
            }
        } else if (this.getSize() >= swet.getSize()) {
            this.setSize(MathHelper.ceil(MathHelper.sqrt(this.getSize()*this.getSize() + swet.getSize()*swet.getSize())), true);
            swet.discard();
        }
    }

    protected void removePassenger(Entity passenger) {
        stuckCooldown = 30;
        super.removePassenger(passenger);
    }

    public void setSize(int size, boolean heal){
        super.setSize(size, heal);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt){
        setSize(getInitialSize(), true);
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        if (this.random.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }

        return entityData;
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
            SwetEntity swet = (this.convertTo(type, true));
            if (swet != null)
                swet.setSize(this.getSize(),false);
            world.spawnEntity(swet);
        }
    }

    protected int getInitialSize() { return 2; }
}
