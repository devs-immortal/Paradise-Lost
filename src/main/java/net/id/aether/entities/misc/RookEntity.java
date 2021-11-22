package net.id.aether.entities.misc;

import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RookEntity extends MobEntity {

    public static final TrackedData<Byte> ASCENSION = DataTracker.registerData(RookEntity.class, TrackedDataHandlerRegistry.BYTE);

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
        if(world.getTimeOfDay() < 13000) {
            this.remove(RemovalReason.DISCARDED);
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

    public static record RookData(byte ascension) implements EntityData {}
}
