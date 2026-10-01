package net.id.paradise_lost.entity.hostile;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class EnvoyEntity extends Skeleton implements IEnlightenable {

    private static final EntityDataAccessor<Boolean> ENLIGHTENED;

    public EnvoyEntity(EntityType<? extends EnvoyEntity> entityType, Level world) {
        super(entityType, world);
    }

    public boolean getEnlightened() {
        return this.entityData.get(ENLIGHTENED);
    }

    public void setEnlightened(boolean value) {
        if (value) playEnlighteningEffects();
        this.entityData.set(ENLIGHTENED, value);
    }

    private void playEnlighteningEffects() {
        this.makeSound(ParadiseLostSoundEvents.ENTITY_ENVOY_GETS_ENLIGHTENED);
        this.level().broadcastEntityEvent(this, EntityEvent.WITCH_HAT_MAGIC);
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.WITCH_HAT_MAGIC) {
            for (int i = 0; i < 18; i++) {
                this.level().addParticle(ParadiseLostParticleTypes.LIT_CLOUD,
                        this.getRandomX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getRandomZ(0.2),
                        (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3
                );
            }
        } else {
            super.handleEntityEvent(status);
        }
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        if (spawnReason == MobSpawnType.NATURAL && world.getRandom().nextFloat() < 0.05F) {
            this.setEnlightened(true);
        }

        return entityData;
    }

    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (this.level() instanceof ServerLevel serverWorld && this.getEnlightened() && damageSource.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
            EntityRegistry.QUINT.get().spawn(serverWorld, this.blockPosition().above(), MobSpawnType.MOB_SUMMONED);
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENLIGHTENED, false);
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance localDifficulty) {
    }

    public boolean isShaking() {
        return this.getEnlightened() || super.isShaking();
    }

    public void tick() {
        if (this.level().isClientSide && this.getEnlightened() && this.random.nextInt(3) == 0) {
            this.level().addParticle(ParadiseLostParticleTypes.LIT_CLOUD,
                    this.getRandomX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getRandomZ(0.2),
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

    public boolean hurt(DamageSource source, float amount) {
        float dmg = amount;
        if (this.getEnlightened()) {
            dmg /= 2;
        }
        if (this.isSunBurnTick()) {
            dmg *= 3f;
        }
        return super.hurt(source, dmg);
    }

    public boolean doHurtTarget(Entity target) {
        if (!super.doHurtTarget(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200), this);
            }
            return true;
        }
    }
    public static AttributeSupplier.Builder createEnvoyAttributes() {
        return createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, -1)
                .add(Attributes.SCALE, 1.05f);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Enlightened", this.entityData.get(ENLIGHTENED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ENLIGHTENED, compound.getBoolean("Enlightened"));
    }

    static {
        ENLIGHTENED = SynchedEntityData.defineId(EnvoyEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
