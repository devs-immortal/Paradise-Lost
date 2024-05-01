package net.id.paradiselost.entities.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class EnvoyEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> ENLIGHTENED;

    public EnvoyEntity(EntityType<? extends EnvoyEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean getEnlightened() {
        return this.dataTracker.get(ENLIGHTENED);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENLIGHTENED, false);
    }

    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
    }

    public boolean isShaking() {
        return this.getEnlightened() || super.isShaking();
    }


    public void tick() {
        if (this.world.isClient && this.getEnlightened() && this.random.nextInt(3) == 0) {
            this.world.addParticle(ParticleTypes.CLOUD,
                    this.getParticleX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getParticleZ(0.2),
                    (this.random.nextDouble() - 0.5) * 0.05, -this.random.nextDouble() * 0.025, (this.random.nextDouble() - 0.5) * 0.05
            );
        }
        super.tick();
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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D);
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
