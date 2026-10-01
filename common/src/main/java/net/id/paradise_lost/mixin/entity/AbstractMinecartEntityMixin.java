package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {

    /** Ticks of noclip after leaving rails so the AABB clears the support without killing incline. */
    private static final int EXIT_GRACE_TICKS = 8;

    @Unique
    private int paradiseLost$exitGrace;

    @Unique
    private boolean paradiseLost$wasOffRail;

    public AbstractMinecartEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract double getMaxSpeed();

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadLegacyFloating(CompoundTag tag, CallbackInfo ci) {
        MinecartFloating.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (MinecartFloating.isFloating(cart) && MinecartFloating.getFloatTime(cart) > 0) {
            if (!this.paradiseLost$wasOffRail) {
                this.paradiseLost$exitGrace = EXIT_GRACE_TICKS;
                this.paradiseLost$wasOffRail = true;
            }

            double d = this.getMaxSpeed();
            Vec3 vec3d = this.getDeltaMovement();
            double x = Mth.clamp(vec3d.x, -d, d);
            double z = Mth.clamp(vec3d.z, -d, d);
            double y = 0.0D;
            int incline = MinecartFloating.getIncline(cart);
            if (incline != 0) {
                y = incline * Math.sqrt(x * x + z * z);
            }
            this.setDeltaMovement(x, y, z);

            if (this.paradiseLost$exitGrace > 0) {
                this.paradiseLost$exitGrace--;
                boolean previousNoPhysics = this.noPhysics;
                this.noPhysics = true;
                try {
                    this.move(MoverType.SELF, this.getDeltaMovement());
                } finally {
                    this.noPhysics = previousNoPhysics;
                }
            } else {
                this.move(MoverType.SELF, this.getDeltaMovement());
                // Real walls only — never clear incline during exit grace.
                if (this.horizontalCollision || this.verticalCollision) {
                    MinecartFloating.setIncline(cart, 0);
                }
            }

            MinecartFloating.tick(cart);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloating.isFloating(cart)) {
            this.paradiseLost$exitGrace = 0;
            this.paradiseLost$wasOffRail = false;
            return;
        }

        if (MinecartFloating.isCartOnRail(cart)) {
            this.paradiseLost$exitGrace = 0;
            this.paradiseLost$wasOffRail = false;
            MinecartFloating.captureRailState(cart);
            if (!this.level().isClientSide()) {
                MinecartFloating.tick(cart);
            }
            return;
        }

        MinecartFloating.applyFloatingRotation(cart);
        if (this.level().isClientSide()) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.lengthSqr() > 1.0E-4) {
                var pos = this.position();
                var rightParticlePos = pos.add(motion.normalize().scale(0.35F).yRot(1.57F));
                var leftParticlePos = pos.add(motion.normalize().scale(0.35F).yRot(-1.57F));
                this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, rightParticlePos.x(), this.getY(), rightParticlePos.z(), 0, 0, 0);
                this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, leftParticlePos.x(), this.getY(), leftParticlePos.z(), 0, 0, 0);
            }
        }
    }
}
