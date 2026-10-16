package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.MinecartFloatingHelper;
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

    /**
     * Short noclip window so the cart clears the rail block without a multi-tick physics hitch.
     */
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
    private int paradiseLost$noDescentPathTicks;

    @Unique
    private boolean paradiseLost$wasOffRail;

    @Unique
    private int paradiseLost$openAirClimbGrace;

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

    @Override
    public void paradiseLost$setOpenAirClimbGrace(int ticks) {
        this.paradiseLost$openAirClimbGrace = Math.max(0, ticks);
    }

    @Override
    public int paradiseLost$getOpenAirClimbGrace() {
        return this.paradiseLost$openAirClimbGrace;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadLegacyFloating(CompoundTag tag, CallbackInfo ci) {
        MinecartFloatingHelper.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    /**
     * Off-rail float only: force the vanilla off-track render path (entity yaw/pitch). Don't rewrite seated
     * {@code getPos}/{@code getPosOffs}, that broke curve posing (offs collapsed to renderer fell back to stuck entity
     * yaw on corners).
     */
    @Inject(method = "getPos(DDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailSnap(double x, double y, double z, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloatingHelper.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getPosOffs(DDDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailOffs(
            double x, double y, double z, double offset, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloatingHelper.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "moveAlongTrack", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipMidairRailResnap(BlockPos pos, BlockState state, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        boolean leftRails = this.level().isClientSide()
                ? MinecartFloatingHelper.isOffRail(cart)
                : this.paradiseLost$wasOffRail;
        if (!this.level().isClientSide()
                && this.paradiseLost$wasOffRail
                && MinecartFloatingHelper.isFloating(cart)
                && !MinecartFloatingHelper.isMidairAboveRail(cart, pos, true)) {
            // Still climbing past a floor/corner, don't seat yet or the tilt drops out.
            if (MinecartFloatingHelper.shouldDeferSlopeToCornerHardSeat(cart, pos, state)) {
                MinecartFloatingHelper.keepSlopeFloatThroughCorner(cart, pos, state);
                MinecartFloatingHelper.debugEvent(cart, "deferSeat",
                        "keep slope over corner/flat at " + pos
                                + " shape=" + MinecartFloatingHelper.getRailShapeName(cart));
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            // Close enough: snap onto the rail and hand off to vanilla.
            this.paradiseLost$exitGrace = 0;
            MinecartFloatingHelper.prepareReconnect(cart);
            if (MinecartFloatingHelper.isOffRail(cart)) {
                // prepareReconnect decided this isn't a real landing yet.
                this.paradiseLost$wasOffRail = true;
                MinecartFloatingHelper.debugEvent(cart, "deferSeat",
                        "reconnect kept float at " + pos
                                + " shape=" + MinecartFloatingHelper.getRailShapeName(cart));
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            this.paradiseLost$wasOffRail = false;
            MinecartFloatingHelper.debugEvent(cart, "reconnect",
                    "hard seat at " + pos + " shape=" + MinecartFloatingHelper.getRailShapeName(cart));
            return;
        }
        if (MinecartFloatingHelper.shouldCancelTrackWhileFloating(cart, pos, state, leftRails)) {
            // Too high to seat, keep floating. Also catches client desync where offRail is already false.
            if (MinecartFloatingHelper.isFlatRailConnectTooEarly(cart, pos, state)) {
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            if (!this.level().isClientSide()) {
                MinecartFloatingHelper.adaptFloatWhileGhosting(cart, state);
            }
            MinecartFloatingHelper.debugEvent(cart, "skipTrack",
                    "midair cancel moveAlongTrack at " + pos + " leftRails=" + leftRails
                            + " unsettledFlat=" + MinecartFloatingHelper.isUnsettledAboveFlatRail(cart));
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        }
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloatingHelper.isFloating(cart) || MinecartFloatingHelper.getFloatTime(cart) <= 0) {
            return;
        }
        if (!this.level().isClientSide()) {
            boolean alreadyOff = this.paradiseLost$wasOffRail;
            this.paradiseLost$beginFloatTakeoff(cart, "comeOff");
            this.paradiseLost$moveFloating(cart, true);
            if (!alreadyOff && !this.paradiseLost$wasOffRail) {
                // Takeoff was held; don't leave a stale offRail flag.
                MinecartFloatingHelper.setOffRail(cart, false);
            }
            ci.cancel();
        } else if (MinecartFloatingHelper.isOffRail(cart) || MinecartFloatingHelper.isFloating(cart)) {
            // Client: kill vanilla come-off even if offRail hasn't synced yet.
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        }
    }

    /**
     * Start a float gap: freeze pose, set grace, mark off-rail. Called from comeOffTrack and the
     * air tick branch (leaving during moveAlongTrack can skip comeOff for a tick).
     */
    @Unique
    private void paradiseLost$beginFloatTakeoff(AbstractMinecart cart, String reason) {
        if (this.paradiseLost$wasOffRail) {
            return;
        }
        // A cart that has basically stopped isn't taking off, so don't lock in a flat takeoff.
        Vec3 exitMotion = this.getDeltaMovement();
        if (exitMotion.horizontalDistanceSqr() <= 0.0025D) {
            MinecartFloatingHelper.debugEvent(cart, reason, "hold takeoff near-zero motion");
            return;
        }
        // Capture incline before prepareTakeoff; flat takeoff clears it.
        int inclineAtExit = MinecartFloatingHelper.getIncline(cart);
        MinecartFloatingHelper.prepareTakeoff(cart);
        // Slightly longer grace when leaving a descent lip.
        int grace = inclineAtExit < 0
                ? MinecartFloatingHelper.slopeDescentExitGraceTicks()
                : EXIT_GRACE_TICKS;
        this.paradiseLost$exitGrace = grace;
        this.paradiseLost$wasOffRail = true;
        MinecartFloatingHelper.setOffRail(cart, true);
        MinecartFloatingHelper.debugEvent(cart, reason,
                "first exit grace=" + grace
                        + " incline=" + inclineAtExit
                        + " shape=" + MinecartFloatingHelper.getRailShapeName(cart)
                        + " motion=" + this.getDeltaMovement());
    }

    @Unique
    private void paradiseLost$moveFloating(AbstractMinecart cart, boolean tickFloatTime) {
        if (this.paradiseLost$openAirClimbGrace > 0) {
            this.paradiseLost$openAirClimbGrace--;
        }
        // Stair collision can zero xz; nudge enough to keep the diagonal.
        MinecartFloatingHelper.assistOpenAirSlopeTravel(cart);
        MinecartFloatingHelper.maintainClimbTowardFlatLanding(cart);
        double d = this.getMaxSpeed();
        Vec3 vec3d = this.getDeltaMovement();
        double x = Mth.clamp(vec3d.x, -d, d);
        double z = Mth.clamp(vec3d.z, -d, d);
        double y = 0.0D;
        int incline = MinecartFloatingHelper.getIncline(cart);
        if (incline != 0) {
            boolean openAir = MinecartFloatingHelper.findRailPos(cart) == null;
            // Descending into empty air, hold height a bit, then level if nothing shows up.
            if (incline < 0 && openAir && !MinecartFloatingHelper.shouldKeepOpenAirDescent(cart)) {
                y = 0.0D;
                if (++this.paradiseLost$noDescentPathTicks >= 4) {
                    MinecartFloatingHelper.levelOpenAirFlight(cart);
                    incline = 0;
                    this.paradiseLost$noDescentPathTicks = 0;
                }
            } else if (incline > 0 && openAir && !MinecartFloatingHelper.shouldKeepOpenAirClimb(cart)) {
                // Past the last slope/solid stair: the climb is over, so let the cart drop
                // instead of coasting level for the rest of the float.
                y = 0.0D;
                if (++this.paradiseLost$noDescentPathTicks >= 2) {
                    MinecartFloatingHelper.levelOpenAirFlight(cart);
                    MinecartFloatingHelper.stopFloating(cart);
                    incline = 0;
                    this.paradiseLost$noDescentPathTicks = 0;
                }
            } else {
                this.paradiseLost$noDescentPathTicks = 0;
                y = incline * Math.sqrt(x * x + z * z);
                y = MinecartFloatingHelper.softenSlopeDescentOntoFlat(cart, y);
                y = MinecartFloatingHelper.softenSlopeAscentOntoFlat(cart, y);
            }
        } else {
            this.paradiseLost$noDescentPathTicks = 0;
            BlockPos railPos = MinecartFloatingHelper.findRailPos(cart);
            if (railPos != null) {
                y = MinecartFloatingHelper.descentSpeedOntoFlatRail(cart, railPos);
            }
        }
        this.setDeltaMovement(x, y, z);

        boolean noclip = MinecartFloatingHelper.shouldUseSlopeNoclip(cart)
                || (this.paradiseLost$exitGrace > 0 && MinecartFloatingHelper.shouldUseExitGraceNoclip(cart));
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

        // Hit solid with nothing to land on, level out and coast.
        if (!this.level().isClientSide()
                && MinecartFloatingHelper.shouldLevelAgainstSolidBarrier(cart, this.onGround(), this.verticalCollision)) {
            MinecartFloatingHelper.levelOpenAirFlight(cart);
            MinecartFloatingHelper.debugEvent(cart, "solidBarrier",
                    "onGround=" + this.onGround() + " vertCol=" + this.verticalCollision);
        }

        if (tickFloatTime) {
            MinecartFloatingHelper.tick(cart);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloatingHelper.isFloating(cart)) {
            this.paradiseLost$exitGrace = 0;
            this.paradiseLost$noDescentPathTicks = 0;
            this.paradiseLost$wasOffRail = false;
            MinecartFloatingHelper.setOffRail(cart, false);
            this.paradiseLost$clearRailRenderPose();
            return;
        }

        // comeOffTrack owns takeoff, don't flip offRail from a one-tick !onRail blip.

        boolean offRailNow = MinecartFloatingHelper.isOffRail(cart);

        if (MinecartFloatingHelper.isCartOnRail(cart)) {
            boolean leftRails = this.level().isClientSide()
                    ? offRailNow
                    : (this.paradiseLost$wasOffRail || offRailNow);
            BlockPos railPos = MinecartFloatingHelper.findRailPos(cart);
            if (!this.level().isClientSide() && railPos != null) {
                MinecartFloatingHelper.assistFloatingSlopeTravel(cart, this.level().getBlockState(railPos));
            }
            boolean midair = railPos != null && MinecartFloatingHelper.isMidairAboveRail(cart, railPos, leftRails);
            // Client can clear offRail while still a block or two above the pier.
            boolean unsettledFlat = MinecartFloatingHelper.isUnsettledAboveFlatRail(cart);
            boolean keepFloat = midair || unsettledFlat;

            if (offRailNow) {
                boolean settled = railPos != null && MinecartFloatingHelper.isSettledOnRail(cart, railPos);
                if (settled && !this.level().isClientSide()) {
                    BlockState seatState = this.level().getBlockState(railPos);
                    if (MinecartFloatingHelper.shouldDeferSlopeToCornerHardSeat(cart, railPos, seatState)) {
                        MinecartFloatingHelper.keepSlopeFloatThroughCorner(cart, railPos, seatState);
                        MinecartFloatingHelper.applyFloatingRotation(cart);
                        MinecartFloatingHelper.debugTick(cart, leftRails, midair, "deferSeatTick");
                    } else {
                        this.paradiseLost$exitGrace = 0;
                        MinecartFloatingHelper.prepareReconnect(cart);
                        if (MinecartFloatingHelper.isOffRail(cart)) {
                            this.paradiseLost$wasOffRail = true;
                            MinecartFloatingHelper.applyFloatingRotation(cart);
                            MinecartFloatingHelper.debugTick(cart, leftRails, midair, "deferSeatTick");
                        } else {
                            this.paradiseLost$wasOffRail = false;
                            MinecartFloatingHelper.debugTick(cart, false, false, "reconnect");
                        }
                    }
                } else {
                    if (!this.level().isClientSide()) {
                        if (railPos != null) {
                            MinecartFloatingHelper.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                        }
                        MinecartFloatingHelper.applyFloatingRotation(cart);
                    } else {
                        paradiseLost$applyClientFloatPose(cart);
                    }
                    MinecartFloatingHelper.debugTick(cart, leftRails, midair,
                            settled ? "ghostRailLerp" : (midair ? "ghostRail" : "ghostRailLerp"));
                }
            } else if (keepFloat) {
                // Still high even though offRail is already false, keep the float pose.
                if (!this.level().isClientSide()) {
                    if (railPos != null) {
                        MinecartFloatingHelper.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                    }
                    MinecartFloatingHelper.applyFloatingRotation(cart);
                } else {
                    paradiseLost$applyClientFloatPose(cart);
                }
                MinecartFloatingHelper.debugTick(cart, leftRails, true,
                        unsettledFlat ? "ghostRailDesync" : "ghostRailPending");
            } else {
                this.paradiseLost$exitGrace = 0;
                this.paradiseLost$wasOffRail = false;
                if (!this.level().isClientSide()) {
                    MinecartFloatingHelper.captureRailState(cart);
                    if (MinecartFloatingHelper.getIncline(cart) != 0) {
                        MinecartFloatingHelper.captureRailRenderPose(cart);
                        // Offs can spit out garbage near mixed geometry, clamp back to 45 degrees.
                        MinecartFloatingHelper.maintainSlopeFloatPose(cart);
                        if (MinecartFloatingHelper.shouldSkipRailRenderSnap(cart)) {
                            MinecartFloatingHelper.maintainSlopeEntityPitch(cart);
                        } else {
                            MinecartFloatingHelper.publishSlopePoseToSync(cart);
                        }
                    } else {
                        MinecartFloatingHelper.syncFlatFloatYawFromMotion(cart);
                    }
                } else {
                    paradiseLost$applyClientFloatPose(cart);
                }
                MinecartFloatingHelper.debugTick(cart, false, false, "onRail");
            }
            if (!this.level().isClientSide()) {
                MinecartFloatingHelper.tick(cart);
            }
            return;
        }

        // Open air. Client still applies float pose when offRail sync is late.
        if (!this.level().isClientSide()) {
            boolean firstExit = !this.paradiseLost$wasOffRail;
            this.paradiseLost$beginFloatTakeoff(cart, "airTakeoff");
            if (firstExit && this.paradiseLost$wasOffRail) {
                Vec3 motion = this.getDeltaMovement();
                int incline = MinecartFloatingHelper.getIncline(cart);
                if (incline > 0
                        || (incline < 0 && (MinecartFloatingHelper.findRailPos(cart) != null
                        || MinecartFloatingHelper.shouldKeepOpenAirDescent(cart)))) {
                    this.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
                } else if (incline < 0) {
                    // No landing in sight, hold height, keep incline for the next look-ahead tick.
                    this.setDeltaMovement(motion.x, 0.0D, motion.z);
                }
            }
            if (this.paradiseLost$wasOffRail) {
                MinecartFloatingHelper.applyFloatingRotation(cart);
                if (firstExit) {
                    // comeOffTrack was skipped this tick (left during moveAlongTrack).
                    // Move now with slope noclip so we don't stick on the next stair pier.
                    this.paradiseLost$moveFloating(cart, true);
                }
            }
        } else if (MinecartFloatingHelper.isOffRail(cart) || MinecartFloatingHelper.isFloating(cart)) {
            paradiseLost$applyClientFloatPose(cart);
        }
        MinecartFloatingHelper.debugTick(cart, this.paradiseLost$wasOffRail, false, "air");
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

    /** 45 degrees only if the rail under this client cart really is a slope, since the incline sync runs ahead. */
    @Unique
    private void paradiseLost$applyClientFloatPose(AbstractMinecart cart) {
        if (MinecartFloatingHelper.clientShouldHoldSlopePose(cart)) {
            MinecartFloatingHelper.maintainSlopeFloatPose(cart);
        } else {
            MinecartFloatingHelper.applySyncedFloatingRotation(cart);
        }
    }
}
