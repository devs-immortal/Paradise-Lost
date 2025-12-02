package net.id.paradiselost.entities.hostile;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class KeeperEntity extends ZombieEntity implements IEnlightenable {

    private static final TrackedData<Boolean> ENLIGHTENED;

    public KeeperEntity(EntityType<? extends KeeperEntity> entityType, World world) {
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
        if (this.getWorld().isClient) {
            for (int i = 0; i < 18; i++) {
                this.getWorld().addParticle(ParadiseLostParticles.LIT_CLOUD,
                        this.getParticleX(0.2), (this.getY() + this.random.nextDouble() * 0.6) + 0.85, this.getParticleZ(0.2),
                        (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3
                );
            }
        }
    }

    public void onDeath(DamageSource damageSource) { //TODO
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld && this.getEnlightened() && damageSource.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
            ParadiseLostEntityTypes.QUINT.spawn(serverWorld, this.getBlockPos().up(), SpawnReason.MOB_SUMMONED);
        }
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_STEP;
    }

    public boolean damage(DamageSource source, float amount) {
        if (!this.getEnlightened()) {
            return false;
        }
        return super.damage(source, amount);
    }

    public boolean isAiDisabled() {
        return !this.getEnlightened();
    }

    public static DefaultAttributeContainer.Builder createKeeperAttributes() {
        return createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1D);
    }


    protected void initAttributes() {
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENLIGHTENED, false);
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
        ENLIGHTENED = DataTracker.registerData(KeeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }


    public static boolean noSpawn(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return false;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
