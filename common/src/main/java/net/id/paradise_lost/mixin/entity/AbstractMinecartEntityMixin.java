package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.entity.ParadiseLostMinecartExtensions;
import net.minecraft.server.level.ServerLevel;
import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.core.BlockPos;
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
public abstract class AbstractMinecartEntityMixin extends VehicleEntity implements ParadiseLostMinecartExtensions {

    @Unique
    private static final int EXIT_GRACE_TICKS = 8;

    @Unique
    private int paradiseLost$exitGrace;

    @Unique
    private boolean paradiseLost$wasOffRail;

    public AbstractMinecartEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract double getMaxSpeed(ServerLevel level);

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadLegacyFloating(CompoundTag tag, CallbackInfo ci) {
        MinecartFloating.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    @Override
    public boolean paradiseLost$moveMidairAboveRail(ServerLevel level, BlockPos railPos) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (MinecartFloating.isMidairAboveRail(cart, railPos, this.paradiseLost$wasOffRail)) {
            this.paradiseLost$moveFloating(level, cart, false);
            return true;
        }
        return false;
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(ServerLevel level, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (MinecartFloating.isFloating(cart) && MinecartFloating.getFloatTime(cart) > 0) {
            if (!this.paradiseLost$wasOffRail) {
                this.paradiseLost$exitGrace = EXIT_GRACE_TICKS;
                this.paradiseLost$wasOffRail = true;
            }
            this.paradiseLost$moveFloating(level, cart, true);
            ci.cancel();
        }
    }

    @Unique
    private void paradiseLost$moveFloating(ServerLevel level, AbstractMinecart cart, boolean tickFloatTime) {
        double d = this.getMaxSpeed(level);
        Vec3 vec3d = this.getDeltaMovement();
        double x = Mth.clamp(vec3d.x, -d, d);
        double z = Mth.clamp(vec3d.z, -d, d);
        double y = 0.0D;
        int incline = MinecartFloating.getIncline(cart);
        if (incline != 0) {
            y = incline * Math.sqrt(x * x + z * z);
        }
        this.setDeltaMovement(x, y, z);

        boolean noclip = incline != 0 || this.paradiseLost$exitGrace > 0;
        if (this.paradiseLost$exitGrace > 0) {
            this.paradiseLost$exitGrace--;
        }
        if (noclip) {
            boolean previousNoPhysics = this.noPhysics;
            this.noPhysics = true;
            try {
                this.move(MoverType.SELF, this.getDeltaMovement());
            } finally {
                this.noPhysics = previousNoPhysics;
            }
        } else {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (tickFloatTime) {
            MinecartFloating.tick(cart);
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

        if (!MinecartFloating.isCartOnRail(cart) && MinecartFloating.getIncline(cart) != 0) {
            this.paradiseLost$wasOffRail = true;
        }

        if (MinecartFloating.isCartOnRail(cart)) {
            boolean midair = MinecartFloating.isMidairAboveRail(cart, cart.blockPosition(), this.paradiseLost$wasOffRail)
                    || MinecartFloating.isMidairAboveRail(cart, cart.blockPosition().below(), this.paradiseLost$wasOffRail);
            if (!midair) {
                this.paradiseLost$exitGrace = 0;
                this.paradiseLost$wasOffRail = false;
                MinecartFloating.captureRailState(cart);
            }
            if (!this.level().isClientSide()) {
                MinecartFloating.tick(cart);
            }
            // Keep pitch even on "ghost" on-rail ticks so descend doesn't flash flat.
            if (MinecartFloating.getIncline(cart) != 0) {
                MinecartFloating.applyFloatingRotation(cart);
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
