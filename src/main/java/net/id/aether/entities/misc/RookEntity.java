package net.id.aether.entities.misc;

import net.id.aether.util.AetherSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static net.id.aether.tag.AetherItemTags.RIGHTEOUS_WEAPONS;
import static net.id.aether.tag.AetherItemTags.SACRED_WEAPONS;

public class RookEntity extends MobEntity {

    public static final TrackedData<Byte> ASCENSION = DataTracker.registerData(RookEntity.class, TrackedDataHandlerRegistry.BYTE);
    public int blinkTicks = 0;

    public RookEntity(EntityType<? extends RookEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createRookAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(ASCENSION, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();

        if(blinkTicks > 0) {
            blinkTicks--;
        }

        if(world.getTimeOfDay() < 13000) {
            this.remove(RemovalReason.DISCARDED);
        }

        world.getPlayers()
                .stream()
                .min(Comparator.comparing(player -> player.getPos().distanceTo(getPos())))
                .ifPresent(player -> getLookControl().lookAt(player, 15, 15));
        bodyYaw = headYaw;

        if(world.getTime() % 20 == 0 && random.nextBoolean()) {
            blinkTicks = random.nextInt(5);
        }
    }

    public int getAscencion() {
        return MathHelper.clamp(dataTracker.get(ASCENSION), 0, 3);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (!(entityData instanceof RookData)) {
            entityData = new RookData((byte) random.nextInt(4));
        }
        dataTracker.set(ASCENSION, ((RookData) entityData).ascension);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if(nbt.contains("ascension"))
            dataTracker.set(ASCENSION, nbt.getByte("ascension"));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putByte("ascension", dataTracker.get(ASCENSION));
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        var attacker = source.getAttacker();
        var success = false;

        if(attacker instanceof LivingEntity entity) {
            var weapon = entity.getStackInHand(Hand.MAIN_HAND);
            if(weapon.isIn(RIGHTEOUS_WEAPONS)) {
                amount /= 4;
                success = true;
            }
            else if(weapon.isIn(SACRED_WEAPONS)) {
                success = true;
            }
        }

        if(success) {
            return super.damage(source, amount);
        }

        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_NIGHTMARE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return AetherSoundEvents.ENTITY_NIGHTMARE_AMBIENT;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 200;
    }

    @Override
    protected void pushAway(Entity entity) {}

    @Override
    public void pushAwayFrom(Entity entity) {}

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static record RookData(byte ascension) implements EntityData {}
}
