package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity implements MinecartFloatPoseAccess {

    /** Short noclip window so the cart clears the rail block without a multi-tick physics hitch. */
    @Unique
    private static final int EXIT_GRACE_TICKS = 2;

    @Unique
    private float paradiseLost$railRenderYaw;

    @Unique
    private float paradiseLost$railRenderPitch;

    @Unique
    private boolean paradiseLost$hasRailRenderPose;

    @Unique
    private int paradiseLost$exitGrace;

    @Unique
    private boolean paradiseLost$wasOffRail;

    public AbstractMinecartEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract double getMaxSpeed();

    @Override
    public void paradiseLost$setRailRenderPose(float yaw, float pitch) {
        this.paradiseLost$railRenderYaw = yaw;
        this.paradiseLost$railRenderPitch = pitch;
        this.paradiseLost$hasRailRenderPose = true;
    }

    @Override
    public float paradiseLost$getRailRenderYaw() {
        return this.paradiseLost$railRenderYaw;
    }

    @Override
    public float paradiseLost$getRailRenderPitch() {
        return this.paradiseLost$railRenderPitch;
    }

    @Override
    public boolean paradiseLost$hasRailRenderPose() {
        return this.paradiseLost$hasRailRenderPose;
    }

    @Override
    public void paradiseLost$clearRailRenderPose() {
        this.paradiseLost$hasRailRenderPose = false;
        this.paradiseLost$railRenderYaw = 0.0F;
        this.paradiseLost$railRenderPitch = 0.0F;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadLegacyFloating(CompoundTag tag, CallbackInfo ci) {
        MinecartFloating.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    /**
     * Off-rail float only: force the vanilla off-track render path (entity yaw/pitch).
     * Do <em>not</em> rewrite seated {@code getPos}/{@code getPosOffs} — that broke curve posing
     * (offs collapsed → renderer fell back to stuck entity yaw on corners).
     */
    @Inject(method = "getPos(DDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailSnap(double x, double y, double z, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloating.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getPosOffs(DDDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailOffs(
            double x, double y, double z, double offset, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloating.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "moveAlongTrack", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipMidairRailResnap(BlockPos pos, BlockState state, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        boolean leftRails = this.level().isClientSide()
                ? MinecartFloating.isOffRail(cart)
                : this.paradiseLost$wasOffRail;
        if (!this.level().isClientSide()
                && this.paradiseLost$wasOffRail
                && MinecartFloating.isFloating(cart)
                && !MinecartFloating.isMidairAboveRail(cart, pos, true)) {
            MinecartFloating.debugEvent(cart, "reconnect",
                    "pos=" + pos + " beforeNudge wasOff=" + this.paradiseLost$wasOffRail);
            MinecartFloating.nudgeVelocityToRail(cart, state);
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
            return;
        }
        if (MinecartFloating.shouldCancelTrackWhileFloating(cart, pos, state, leftRails)) {
            // Flat far above: keep airborne float (no flatten/latch). Near seat: adapt like perpendicular.
            // Also covers client offRail=false while still high — vanilla moveAlongTrack would teleport.
            if (MinecartFloating.isFlatRailConnectTooEarly(cart, pos, state)) {
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            if (!this.level().isClientSide()) {
                // Ascending: refresh incline/Y for carousel climb (p.1). Flat: landing pose (p.2).
                MinecartFloating.adaptFloatWhileGhosting(cart, state);
            }
            MinecartFloating.debugEvent(cart, "skipTrack",
                    "midair cancel moveAlongTrack at " + pos + " leftRails=" + leftRails
                            + " unsettledFlat=" + MinecartFloating.isUnsettledAboveFlatRail(cart));
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        }
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloating.isFloating(cart) || MinecartFloating.getFloatTime(cart) <= 0) {
            return;
        }
        if (!this.level().isClientSide()) {
            if (!this.paradiseLost$wasOffRail) {
                MinecartFloating.prepareTakeoff(cart);
                this.paradiseLost$exitGrace = EXIT_GRACE_TICKS;
                this.paradiseLost$wasOffRail = true;
                MinecartFloating.setOffRail(cart, true);
                MinecartFloating.debugEvent(cart, "comeOff",
                        "first exit grace=" + EXIT_GRACE_TICKS
                                + " incline=" + MinecartFloating.getIncline(cart)
                                + " shape=" + MinecartFloating.getRailShapeName(cart)
                                + " motion=" + this.getDeltaMovement());
            }
            this.paradiseLost$moveFloating(cart, true);
            ci.cancel();
        } else if (MinecartFloating.isOffRail(cart) || MinecartFloating.isFloating(cart)) {
            // offRail sync can lag; still cancel vanilla comeOffTrack so network lerp
            // does not rotate toward landing while we are mid-air.
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        }
    }

    @Unique
    private void paradiseLost$moveFloating(AbstractMinecart cart, boolean tickFloatTime) {
        double d = this.getMaxSpeed();
        Vec3 vec3d = this.getDeltaMovement();
        double x = Mth.clamp(vec3d.x, -d, d);
        double z = Mth.clamp(vec3d.z, -d, d);
        double y = 0.0D;
        int incline = MinecartFloating.getIncline(cart);
        if (incline != 0) {
            y = incline * Math.sqrt(x * x + z * z);
            // Soft-seat onto flat without clearing incline (keeps slope pitch until settle).
            y = MinecartFloating.softenSlopeDescentOntoFlat(cart, y);
        } else {
            BlockPos railPos = MinecartFloating.findRailPos(cart);
            if (railPos != null) {
                y = MinecartFloating.descentSpeedOntoFlatRail(cart, railPos);
            }
        }
        this.setDeltaMovement(x, y, z);

        // Slope noclip only over ascending rails / open air — not over flat piers (phases through).
        boolean noclip = MinecartFloating.shouldUseSlopeNoclip(cart) || this.paradiseLost$exitGrace > 0;
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
            MinecartFloating.setOffRail(cart, false);
            this.paradiseLost$clearRailRenderPose();
            return;
        }

        // Do not mark off-rail from a brief !onRail at tick end — that froze look-ahead curve
        // poses (early corner rotate) and triggered reconnect nudges. comeOffTrack owns takeoff.

        boolean offRailNow = MinecartFloating.isOffRail(cart);

        if (MinecartFloating.isCartOnRail(cart)) {
            boolean leftRails = this.level().isClientSide()
                    ? offRailNow
                    : (this.paradiseLost$wasOffRail || offRailNow);
            BlockPos railPos = MinecartFloating.findRailPos(cart);
            boolean midair = railPos != null && MinecartFloating.isMidairAboveRail(cart, railPos, leftRails);
            // Client often receives offRail=false while still ~1–2 blocks above a flat pier.
            boolean unsettledFlat = MinecartFloating.isUnsettledAboveFlatRail(cart);
            boolean keepFloat = midair || unsettledFlat;

            // Synced off-rail: keep float pose until settled on the rail.
            if (offRailNow) {
                boolean settled = railPos != null && MinecartFloating.isSettledOnRail(cart, railPos);
                if (settled && !this.level().isClientSide()) {
                    this.paradiseLost$exitGrace = 0;
                    this.paradiseLost$wasOffRail = false;
                    MinecartFloating.prepareReconnect(cart);
                    MinecartFloating.debugTick(cart, false, false, "reconnect");
                } else {
                    if (!this.level().isClientSide()) {
                        if (railPos != null) {
                            // Pose adopt (~0.50) is earlier than soft-seat (~0.28); do not gate on connect.
                            MinecartFloating.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                        }
                        MinecartFloating.applyFloatingRotation(cart);
                    } else {
                        MinecartFloating.applySyncedFloatingRotation(cart);
                    }
                    MinecartFloating.debugTick(cart, leftRails, midair,
                            settled ? "ghostRailLerp" : (midair ? "ghostRail" : "ghostRailLerp"));
                }
            } else if (keepFloat) {
                // offRail cleared (or never set locally) but still high — do not snap to onRail pose.
                if (!this.level().isClientSide()) {
                    if (railPos != null) {
                        MinecartFloating.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                    }
                    MinecartFloating.applyFloatingRotation(cart);
                } else {
                    MinecartFloating.applySyncedFloatingRotation(cart);
                }
                MinecartFloating.debugTick(cart, leftRails, true,
                        unsettledFlat ? "ghostRailDesync" : "ghostRailPending");
            } else {
                this.paradiseLost$exitGrace = 0;
                this.paradiseLost$wasOffRail = false;
                if (!this.level().isClientSide()) {
                    MinecartFloating.captureRailState(cart);
                    if (MinecartFloating.getIncline(cart) != 0) {
                        // Slope: always refresh offs pose (vanilla MinecartRenderer pair). Only force
                        // entity rot while skipSnap — settled on-rail uses getPos for render.
                        MinecartFloating.captureRailRenderPose(cart);
                        if (MinecartFloating.shouldSkipRailRenderSnap(cart)) {
                            MinecartFloating.maintainSlopeEntityPitch(cart);
                        } else {
                            MinecartFloating.publishSlopePoseToSync(cart);
                        }
                    } else {
                        // Straight flat only: refresh float yaw attachments for takeoff/reconnect.
                        MinecartFloating.syncFlatFloatYawFromMotion(cart);
                    }
                }
                MinecartFloating.debugTick(cart, false, false, "onRail");
            }
            if (!this.level().isClientSide()) {
                MinecartFloating.tick(cart);
            }
            return;
        }

        // True airborne (no rail under cart). comeOffTrack usually set offRail already; keep pose frozen.
        // Client must keep applying float pose even after offRail=false sync — otherwise network
        // lerps toward landing yaw, then ghostRailDesync snaps back (perpendicular double-rotate).
        if (!this.level().isClientSide()) {
            this.paradiseLost$wasOffRail = true;
            MinecartFloating.setOffRail(cart, true);
            MinecartFloating.applyFloatingRotation(cart);
        } else if (MinecartFloating.isOffRail(cart) || MinecartFloating.isFloating(cart)) {
            MinecartFloating.applySyncedFloatingRotation(cart);
        }
        MinecartFloating.debugTick(cart, this.paradiseLost$wasOffRail, false, "air");
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
