package net.id.paradiselost.entities.util;

import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MountableEntity extends ParadiseLostAnimalEntity {

    private static final TrackedData<Boolean> RIDER_SNEAKING = DataTracker.registerData(MountableEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected float jumpPower;
    protected boolean mountJumping;
    protected boolean playStepSound = false;
    protected boolean canJumpMidAir = false;

    public MountableEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    public MountableEntity(World world) {
        this(null, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(RIDER_SNEAKING, false);
    }
    
    //TODO Should this do more than just `return false;`?
    protected boolean canBeControlledByRider() {
        return false;
    }

    // TODO: Patch this or remove it, pick one. (PL-1.7)
    //@Override
    //public void travel(Vec3d motion) {
    //    Entity entity = this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);

    //    if (entity instanceof PlayerEntity) {
    //        PlayerEntity player = (PlayerEntity) entity;

    //        this.prevYaw = this.yaw = player.yaw;
    //        this.prevPitch = this.pitch = player.pitch;

    //        this.headYaw = player.headYaw;

    //        motion = new Vec3d(player.horizontalSpeed, player.upwardSpeed, player.forwardSpeed);

    //        if (motion.z <= 0.0F) motion.multiply(1.0F, 1.0F, 0.25F);
    //        else motion.multiply(1.0F, 1.0F, 0.75F);

    //        if (this.jumpPower > 0.0F && !this.isMountJumping() && (this.onGround || this.canJumpMidAir)) {
    //            this.setVelocity(new Vec3d(this.getVelocity().x, this.getMountJumpStrength() * this.jumpPower, this.getVelocity().z));

    //            if (this.hasStatusEffect(StatusEffects.JUMP_BOOST))
    //                this.setVelocity(this.getVelocity().add(0.0D, (this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F, 0.0D));

    //            this.setMountJumping(true);
    //            this.velocityDirty = true;

    //            if (motion.z > 0.0F) this.setVelocity(this.getVelocity().multiply(0.35F, 1.0F, 0.35F));

    //            this.jumpPower = 0.0F;
    //        }

    //        this.stepHeight = 1.0F;

    //        this.flyingSpeed = this.getMovementSpeed() * 0.1F;

    //        if (this.isLogicalSideForUpdatingMovement()) {
    //            this.setMovementSpeed((float) this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue());
    //            super.travel(motion);
    //        } else {
    //            super.travel(Vec3d.ZERO);
    //        }

    //        if (this.onGround) {
    //            this.jumpPower = 0.0F;
    //            this.setMountJumping(false);
    //        }

    //        this.lastLimbDistance = this.limbDistance;
    //        double d0 = this.getX() - this.prevX;
    //        double d1 = this.getZ() - this.prevZ;
    //        float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

    //        if (f4 > 1.0F) f4 = 1.0F;

    //        this.limbDistance += (f4 - this.limbDistance) * 0.4F;
    //        this.limbAngle += this.limbDistance;
    //    } else {
    //        this.stepHeight = 0.5F;
    //        this.flyingSpeed = 0.02F;

    //        super.travel(motion);
    //    }
    //}

    @Override
    public float getMovementSpeed() {
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

    public void onMountedJump(Vec3d motion) {
        this.jumpPower = 0.4F;
    }

}
