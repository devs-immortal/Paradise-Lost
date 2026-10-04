package net.id.paradise_lost.entity.util;

import net.id.paradise_lost.entity.passive.ParadiseLostAnimalEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class MountableEntity extends ParadiseLostAnimalEntity {

    private static final EntityDataAccessor<Boolean> RIDER_SNEAKING = SynchedEntityData.defineId(MountableEntity.class, EntityDataSerializers.BOOLEAN);
    protected float jumpPower;
    protected boolean mountJumping;
    protected boolean playStepSound = false;
    protected boolean canJumpMidAir = false;

    public MountableEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    public MountableEntity(Level world) {
        this(null, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RIDER_SNEAKING, false);
    }

    protected boolean canBeControlledByRider() {
        return false;
    }

    @Override
    public float getSpeed() {
        return this.getMountedMoveSpeed();
    }

    public float getMountedMoveSpeed() {
        return 0.15F;
    }

    protected double getMountJumpStrength() {
        return 1.0D;
    }

    protected boolean isMountJumping() {
        return this.mountJumping;
    }

    protected void setMountJumping(boolean mountJumping) {
        this.mountJumping = mountJumping;
    }

    public void onMountedJump(Vec3 motion) {
        this.jumpPower = 0.4F;
    }

}
