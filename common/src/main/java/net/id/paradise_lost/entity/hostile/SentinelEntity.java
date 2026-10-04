package net.id.paradise_lost.entity.hostile;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class SentinelEntity extends Zombie implements IEnlightenable {

    private static final EntityDataAccessor<Boolean> ENLIGHTENED;

    public SentinelEntity(EntityType<? extends SentinelEntity> entityType, Level world) {
        super(entityType, world);
        Arrays.fill(this.handDropChances, 0.125F);
        this.xpReward = 10;
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

    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (this.level() instanceof ServerLevel serverWorld && this.getEnlightened() && damageSource.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
            EntityRegistry.QUINT.get().spawn(serverWorld, this.blockPosition().above(), EntitySpawnReason.MOB_SUMMONED);
        }
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        return super.finalizeSpawn(world, difficulty, spawnReason, new Zombie.ZombieGroupData(false, false));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance localDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.SOUL_BLADE.get()));
    }

    @Override
    public void playAmbientSound() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_SENTINEL_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_SENTINEL_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return ParadiseLostSoundEvents.ENTITY_SENTINEL_STEP;
    }

    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (!this.getEnlightened()) {
            if (source.isCreativePlayer()) {
                return super.hurtServer(level, source, 1000);
            }
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    public boolean isNoAi() {
        return !this.getEnlightened();
    }

    public static AttributeSupplier.Builder createSentinelAttributes() {
        return createMonsterAttributes().add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.SCALE, 1.15f)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0F);
    }

    protected void randomizeReinforcementsChance() {
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENLIGHTENED, false);
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
        ENLIGHTENED = SynchedEntityData.defineId(SentinelEntity.class, EntityDataSerializers.BOOLEAN);
    }

    public static boolean noSpawn(EntityType<? extends Monster> type, ServerLevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return false;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
