package net.id.paradiselost.entities.hostile;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnvoyEntity extends SkeletonEntity implements IEnlightenable {

    private static final TrackedData<Boolean> ENLIGHTENED;

    public EnvoyEntity(EntityType<? extends EnvoyEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean getEnlightened() {
        return this.dataTracker.get(ENLIGHTENED);
    }

    public void setEnlightened(boolean value) {
        if (value) playEnlighteningEffects();
        this.dataTracker.set(ENLIGHTENED, value);
    }

    private void playEnlighteningEffects() {
        this.playSound(ParadiseLostSoundEvents.ENTITY_ENVOY_GETS_ENLIGHTENED);
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_WITCH_PARTICLES);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_WITCH_PARTICLES) {
            for (int i = 0; i < 18; i++) {
                this.getWorld().addParticle(ParadiseLostParticles.LIT_CLOUD,
                        this.getParticleX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getParticleZ(0.2),
                        (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3
                );
            }
        } else {
            super.handleStatus(status);
        }
    }

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        if (spawnReason == SpawnReason.NATURAL && world.getRandom().nextFloat() < 0.05F) {
            this.setEnlightened(true);
        }

        return entityData;
    }

    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld && this.getEnlightened() && damageSource.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
            ParadiseLostEntityTypes.QUINT.spawn(serverWorld, this.getBlockPos().up(), SpawnReason.MOB_SUMMONED);
        }
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENLIGHTENED, false);
    }

    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
    }

    public boolean isShaking() {
        return this.getEnlightened() || super.isShaking();
    }


    public void tick() {
        if (this.getWorld().isClient && this.getEnlightened() && this.random.nextInt(3) == 0) {
            this.getWorld().addParticle(ParadiseLostParticles.LIT_CLOUD,
                    this.getParticleX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getParticleZ(0.2),
                    (this.random.nextDouble() - 0.5) * 0.05, -this.random.nextDouble() * 0.025, (this.random.nextDouble() - 0.5) * 0.05
            );
        }
        super.tick();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return this.getEnlightened() ? ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_HURT : ParadiseLostSoundEvents.ENTITY_ENVOY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.getEnlightened() ? ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_DEATH : ParadiseLostSoundEvents.ENTITY_ENVOY_DEATH;
    }

    protected SoundEvent getStepSound() {
        return this.getEnlightened() ? ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_STEP : ParadiseLostSoundEvents.ENTITY_ENVOY_STEP;
    }

    protected SoundEvent getAmbientSound() {
        return this.getEnlightened() ? ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_AMBIENT : ParadiseLostSoundEvents.ENTITY_ENVOY_AMBIENT;
    }

    public boolean damage(DamageSource source, float amount) {
        float dmg = amount;
        if (this.getEnlightened()) {
            dmg /= 2;
        }
        if (this.isAffectedByDaylight()) {
            dmg *= 3f;
        }
        return super.damage(source, dmg);
    }

    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200), this);
            }
            return true;
        }
    }
    public static DefaultAttributeContainer.Builder createEnvoyAttributes() {
        return createHostileAttributes()
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, -1)
                .add(EntityAttributes.SCALE, 1.05f);

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("Enlightened", this.dataTracker.get(ENLIGHTENED));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.dataTracker.set(ENLIGHTENED, compound.getBoolean("Enlightened"));
    }

    static {
        ENLIGHTENED = DataTracker.registerData(EnvoyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
