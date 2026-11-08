package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.minecart.MinecartFloatDebugHelper;
import net.id.paradise_lost.util.minecart.MinecartFlightHelper;
import net.id.paradise_lost.util.minecart.MinecartFloatPoseHelper;
import net.id.paradise_lost.util.minecart.MinecartFloatRenderHelper;
import net.id.paradise_lost.util.minecart.MinecartFloatRotationHelper;
import net.id.paradise_lost.util.minecart.MinecartFloatStateHelper;
import net.id.paradise_lost.util.minecart.MinecartRailConnectHelper;
import net.id.paradise_lost.util.minecart.MinecartRailLocatorHelper;
import net.id.paradise_lost.util.minecart.MinecartRailLookaheadHelper;
import net.id.paradise_lost.util.minecart.MinecartRailMotionHelper;
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
import net.minecraft.world.level.block.BaseRailBlock;
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

    @Unique
    private boolean paradiseLost$vanillaTrack;

    @Unique
    private boolean paradiseLost$ghostRedirect;

    @Shadow
    protected abstract void moveAlongTrack(BlockPos pos, BlockState state);

    @Unique
    private boolean paradiseLost$cornerNoclip;

    @Unique
    private double paradiseLost$enterX;

    @Unique
    private double paradiseLost$enterY;

    @Unique
    private double paradiseLost$enterZ;

    @Unique
    private Vec3 paradiseLost$enterMotion = Vec3.ZERO;

    public AbstractMinecartEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract double getMaxSpeed();

    /**
     * A floating cart is not driving over the block below it, so honey and soul sand must not drag it to a halt.
     */
    @Override
    public float getBlockSpeedFactor() {
        if (MinecartFloatStateHelper.isFloating((AbstractMinecart) (Object) this)) {
            return 1.0F;
        }
        return super.getBlockSpeedFactor();
    }

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
        MinecartFloatStateHelper.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    /**
     * Off-rail float only: force the vanilla off-track render path (entity yaw/pitch). Don't rewrite seated
     * {@code getPos}/{@code getPosOffs}, that broke curve posing (offs collapsed to renderer fell back to stuck entity
     * yaw on corners).
     */
    @Inject(method = "getPos(DDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailSnap(double x, double y, double z, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloatRenderHelper.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getPosOffs(DDDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailOffs(
            double x, double y, double z, double offset, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloatRenderHelper.shouldSkipRailRenderSnap((AbstractMinecart) (Object) this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "moveAlongTrack", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipMidairRailResnap(BlockPos pos, BlockState state, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        MinecartRailLocatorHelper.noteApproachPitch(cart);
        this.paradiseLost$vanillaTrack = false;
        this.paradiseLost$enterX = this.getX();
        this.paradiseLost$enterY = this.getY();
        this.paradiseLost$enterZ = this.getZ();
        this.paradiseLost$enterMotion = this.getDeltaMovement();
        boolean leftRails = this.level().isClientSide()
                ? MinecartFloatStateHelper.isOffRail(cart)
                : this.paradiseLost$wasOffRail;
        if (!this.level().isClientSide()
                && this.paradiseLost$wasOffRail
                && MinecartFloatStateHelper.isFloating(cart)
                && !MinecartRailLocatorHelper.isMidairAboveRail(cart, pos, true)) {
            // Still climbing past a floor/corner, don't seat yet or the tilt drops out.
            if (MinecartFloatRotationHelper.shouldDeferSlopeToCornerHardSeat(cart, pos, state)) {
                MinecartFloatRotationHelper.keepSlopeFloatThroughCorner(cart, pos, state);
                MinecartFloatDebugHelper.debugEvent(cart, "deferSeat",
                        "keep slope over corner/flat at " + pos
                                + " shape=" + MinecartFloatStateHelper.getRailShapeName(cart));
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            if (MinecartRailConnectHelper.shouldWaitToEnterRail(cart, pos, state)) {
                // Just clipping the corner. Keep the heading we had.
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            // Close enough: snap onto the rail and hand off to vanilla.
            this.paradiseLost$exitGrace = 0;
            MinecartRailConnectHelper.prepareReconnect(cart);
            if (MinecartFloatStateHelper.isOffRail(cart)) {
                // prepareReconnect decided this isn't a real landing yet.
                this.paradiseLost$wasOffRail = true;
                MinecartFloatDebugHelper.debugEvent(cart, "deferSeat",
                        "reconnect kept float at " + pos
                                + " shape=" + MinecartFloatStateHelper.getRailShapeName(cart));
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            this.paradiseLost$wasOffRail = false;
            this.paradiseLost$beginCornerNoclip(state);
            MinecartFloatDebugHelper.debugEvent(cart, "reconnect",
                    "hard seat at " + pos + " shape=" + MinecartFloatStateHelper.getRailShapeName(cart));
            return;
        }
        if (MinecartRailConnectHelper.shouldCancelTrackWhileFloating(cart, pos, state, leftRails)) {
            // Too high to seat, keep floating. Also catches client desync where offRail is already false.
            if (MinecartRailConnectHelper.isFlatRailConnectTooEarly(cart, pos, state)) {
                this.paradiseLost$moveFloating(cart, false);
                ci.cancel();
                return;
            }
            if (!this.level().isClientSide() && !MinecartRailLocatorHelper.isGhostSlopeBelow(cart, pos, state)) {
                MinecartRailConnectHelper.adaptFloatWhileGhosting(cart, state);
            }
            MinecartFloatDebugHelper.debugEvent(cart, "skipTrack",
                    "midair cancel moveAlongTrack at " + pos + " leftRails=" + leftRails
                            + " unsettledFlat=" + MinecartRailLocatorHelper.isUnsettledAboveFlatRail(cart));
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        } else if (MinecartRailConnectHelper.shouldWaitToEnterRail(cart, pos, state)) {
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        } else if (MinecartRailLocatorHelper.isGhostSlopeBelow(cart, pos, state)) {
            // Slope under the rail we're on. Ride that rail instead of dropping, or keep floating if there isn't one.
            BlockPos above = pos.above();
            BlockState aboveState = this.level().getBlockState(above);
            if (!this.paradiseLost$ghostRedirect
                    && aboveState.getBlock() instanceof BaseRailBlock
                    && !MinecartRailLocatorHelper.isGhostSlopeBelow(cart, above, aboveState)) {
                this.paradiseLost$ghostRedirect = true;
                // Same inject runs again for the rail above. Clear the flag if that throws,
                // or every later slope skips the redirect and the cart floats off.
                try {
                    this.moveAlongTrack(above, aboveState);
                } finally {
                    this.paradiseLost$ghostRedirect = false;
                }
            } else {
                this.paradiseLost$moveFloating(cart, false);
            }
            ci.cancel();
        } else if (MinecartRailLookaheadHelper.shouldRefuseTrackPhase(cart, pos, state)) {
            // Descent pointed at a block. Don't let the track move set us inside it.
            MinecartRailLookaheadHelper.pullBackFromFloor(cart);
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        } else {
            this.paradiseLost$vanillaTrack = true;
            this.paradiseLost$beginCornerNoclip(state);
            MinecartRailMotionHelper.resolveSidewaysSlopeEntry(cart, state);
        }
    }

    /**
     * Corner curve clips the inside fence post, and that collision kills the new heading.
     * Ignore blocks for this one move.
     */
    @Unique
    private void paradiseLost$beginCornerNoclip(BlockState state) {
        if (!MinecartRailConnectHelper.isCornerShape(state)) {
            return;
        }
        this.noPhysics = true;
        this.paradiseLost$cornerNoclip = true;
    }

    @Inject(method = "moveAlongTrack", at = @At("RETURN"))
    private void paradiseLost$correctVanillaTrackSnap(BlockPos pos, BlockState state, CallbackInfo ci) {
        // Pitch was saved for this thread at the start of the move. The return below still has to clear it,
        // or the next cart on this thread keeps the old pose and the ghost check reads the wrong tilt.
        try {
            if (this.paradiseLost$cornerNoclip) {
                this.noPhysics = false;
                this.paradiseLost$cornerNoclip = false;
            }
            // Normal carts never get a float incline, so the descent stop has to run after vanilla places them.
            if (!this.level().isClientSide()) {
                AbstractMinecart cart = (AbstractMinecart) (Object) this;
                if (MinecartRailLookaheadHelper.stopDeadEndDescent(cart)
                        && MinecartFloatStateHelper.isFloating(cart)) {
                    MinecartFlightHelper.levelOpenAirFlight(cart);
                }
            }
            if (!this.paradiseLost$vanillaTrack) {
                return;
            }
            this.paradiseLost$vanillaTrack = false;
            MinecartRailConnectHelper.correctVanillaTrackSnap((AbstractMinecart) (Object) this,
                    this.paradiseLost$enterX, this.paradiseLost$enterY, this.paradiseLost$enterZ,
                    this.paradiseLost$enterMotion);
        } finally {
            MinecartRailLocatorHelper.clearApproachPitch();
        }
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloatStateHelper.isFloating(cart) || MinecartFloatStateHelper.getFloatTime(cart) <= 0) {
            return;
        }
        if (!this.level().isClientSide()) {
            boolean alreadyOff = this.paradiseLost$wasOffRail;
            this.paradiseLost$beginFloatTakeoff(cart, "comeOff");
            this.paradiseLost$moveFloating(cart, true);
            if (!alreadyOff && !this.paradiseLost$wasOffRail) {
                // Takeoff was held; don't leave a stale offRail flag.
                MinecartFloatStateHelper.setOffRail(cart, false);
            }
            ci.cancel();
        } else if (MinecartFloatStateHelper.isOffRail(cart) || MinecartFloatStateHelper.isFloating(cart)) {
            // Client: kill vanilla come-off even if offRail hasn't synced yet.
            this.paradiseLost$moveFloating(cart, false);
            ci.cancel();
        }
    }

    /**
     * Start a float gap: freeze pose, set grace, mark off-rail. Called from comeOffTrack and the air tick branch
     * (leaving during moveAlongTrack can skip comeOff for a tick).
     */
    @Unique
    private void paradiseLost$beginFloatTakeoff(AbstractMinecart cart, String reason) {
        if (this.paradiseLost$wasOffRail) {
            return;
        }
        // A cart that has basically stopped isn't taking off, so don't lock in a flat takeoff.
        Vec3 exitMotion = this.getDeltaMovement();
        if (exitMotion.horizontalDistanceSqr() <= 0.0025D) {
            MinecartFloatDebugHelper.debugEvent(cart, reason, "hold takeoff near-zero motion");
            return;
        }
        // Capture incline before prepareTakeoff; flat takeoff clears it.
        int inclineAtExit = MinecartFloatStateHelper.getIncline(cart);
        MinecartFlightHelper.prepareTakeoff(cart);
        // Slightly longer grace when leaving a descent lip.
        int grace = inclineAtExit < 0
                ? MinecartRailConnectHelper.slopeDescentExitGraceTicks()
                : EXIT_GRACE_TICKS;
        this.paradiseLost$exitGrace = grace;
        this.paradiseLost$wasOffRail = true;
        MinecartFloatStateHelper.setOffRail(cart, true);
        MinecartFloatDebugHelper.debugEvent(cart, reason,
                "first exit grace=" + grace
                        + " incline=" + inclineAtExit
                        + " shape=" + MinecartFloatStateHelper.getRailShapeName(cart)
                        + " motion=" + this.getDeltaMovement());
    }

    @Unique
    private void paradiseLost$moveFloating(AbstractMinecart cart, boolean tickFloatTime) {
        if (this.paradiseLost$openAirClimbGrace > 0) {
            this.paradiseLost$openAirClimbGrace--;
        }
        // Stair collision can zero xz; nudge enough to keep the diagonal.
        MinecartFlightHelper.assistOpenAirSlopeTravel(cart);
        MinecartFlightHelper.maintainClimbTowardFlatLanding(cart);
        double d = this.getMaxSpeed();
        Vec3 vec3d = this.getDeltaMovement();
        double x = Mth.clamp(vec3d.x, -d, d);
        double z = Mth.clamp(vec3d.z, -d, d);
        double y = 0.0D;
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline != 0) {
            boolean openAir = MinecartRailLocatorHelper.findRailPos(cart) == null;
            // Descending into empty air, hold height a bit, then level if nothing shows up.
            if (incline < 0 && openAir && !MinecartRailLookaheadHelper.shouldKeepOpenAirDescent(cart)) {
                y = 0.0D;
                if (++this.paradiseLost$noDescentPathTicks >= 4) {
                    MinecartFlightHelper.levelOpenAirFlight(cart);
                    incline = 0;
                    this.paradiseLost$noDescentPathTicks = 0;
                }
            } else if (incline > 0 && openAir && !MinecartRailLookaheadHelper.shouldKeepOpenAirClimb(cart)) {
                // Past the last slope/solid stair: the climb is over, so let the cart drop
                // instead of coasting level for the rest of the float.
                y = 0.0D;
                if (++this.paradiseLost$noDescentPathTicks >= 2) {
                    MinecartFlightHelper.levelOpenAirFlight(cart);
                    MinecartFloatStateHelper.stopFloating(cart);
                    incline = 0;
                    this.paradiseLost$noDescentPathTicks = 0;
                }
            } else {
                this.paradiseLost$noDescentPathTicks = 0;
                y = incline * Math.sqrt(x * x + z * z);
                y = MinecartRailConnectHelper.softenSlopeDescentOntoFlat(cart, y);
                y = MinecartRailConnectHelper.softenSlopeAscentOntoFlat(cart, y);
            }
        } else {
            this.paradiseLost$noDescentPathTicks = 0;
            BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
            if (railPos != null) {
                y = MinecartRailConnectHelper.descentSpeedOntoFlatRail(cart, railPos);
            }
        }
        this.setDeltaMovement(x, y, z);
        // Stopped against a dead-end block. Don't lift onto its top, that bounce carries the cart over it.
        if (MinecartFloatStateHelper.getIncline(cart) < 0
                && MinecartRailLookaheadHelper.stopDeadEndDescent(cart)) {
            MinecartFlightHelper.levelOpenAirFlight(cart);
            this.setDeltaMovement(0.0D, 0.0D, 0.0D);
        } else if (MinecartFloatStateHelper.getIncline(cart) < 0 && MinecartRailLookaheadHelper.liftOutOfSolid(cart)) {
            MinecartFlightHelper.levelOpenAirFlight(cart);
            Vec3 lifted = this.getDeltaMovement();
            this.setDeltaMovement(lifted.x, 0.0D, lifted.z);
        }

        Vec3 intended = this.getDeltaMovement();
        boolean noclip = MinecartRailConnectHelper.shouldUseSlopeNoclip(cart)
                || (this.paradiseLost$exitGrace > 0 && MinecartRailConnectHelper.shouldUseExitGraceNoclip(cart));
        if (this.paradiseLost$exitGrace > 0) {
            this.paradiseLost$exitGrace--;
        }
        if (noclip) {
            boolean previousNoPhysics = this.noPhysics;
            this.noPhysics = true;
            // One move with collision off. Put noPhysics back if move throws, or the cart keeps phasing.
            try {
                this.move(MoverType.SELF, this.getDeltaMovement());
            } finally {
                this.noPhysics = previousNoPhysics;
            }
        } else {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        // Hit a floor, ceiling or wall with nothing to land on, level out and coast.
        if (!this.level().isClientSide()
                && MinecartFlightHelper.shouldLevelAgainstSolidBarrier(cart, this.onGround(), this.verticalCollision,
                this.horizontalCollision, intended)) {
            MinecartFlightHelper.levelOpenAirFlight(cart);
            MinecartFloatDebugHelper.debugEvent(cart, "solidBarrier",
                    "onGround=" + this.onGround() + " vertCol=" + this.verticalCollision
                            + " horCol=" + this.horizontalCollision);
        }

        if (tickFloatTime) {
            MinecartFloatStateHelper.tick(cart);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            this.paradiseLost$exitGrace = 0;
            this.paradiseLost$noDescentPathTicks = 0;
            this.paradiseLost$wasOffRail = false;
            MinecartFloatStateHelper.setOffRail(cart, false);
            this.paradiseLost$clearRailRenderPose();
            return;
        }

        // comeOffTrack owns takeoff, don't flip offRail from a one-tick !onRail blip.

        boolean offRailNow = MinecartFloatStateHelper.isOffRail(cart);

        if (MinecartRailLocatorHelper.isCartOnRail(cart)) {
            boolean leftRails = this.level().isClientSide()
                    ? offRailNow
                    : (this.paradiseLost$wasOffRail || offRailNow);
            BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
            if (!this.level().isClientSide() && railPos != null) {
                MinecartRailMotionHelper.assistFloatingSlopeTravel(cart, this.level().getBlockState(railPos));
            }
            boolean midair = railPos != null && MinecartRailLocatorHelper.isMidairAboveRail(cart, railPos, leftRails);
            // Client can clear offRail while still a block or two above the pier.
            boolean unsettledFlat = MinecartRailLocatorHelper.isUnsettledAboveFlatRail(cart);
            boolean keepFloat = midair || unsettledFlat;

            if (offRailNow) {
                boolean settled = railPos != null && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
                if (settled && !this.level().isClientSide()) {
                    BlockState seatState = this.level().getBlockState(railPos);
                    if (MinecartFloatRotationHelper.shouldDeferSlopeToCornerHardSeat(cart, railPos, seatState)
                            || MinecartRailConnectHelper.shouldWaitToEnterRail(cart, railPos, seatState)) {
                        if (MinecartFloatRotationHelper.shouldDeferSlopeToCornerHardSeat(cart, railPos, seatState)) {
                            MinecartFloatRotationHelper.keepSlopeFloatThroughCorner(cart, railPos, seatState);
                        }
                        MinecartFloatRotationHelper.applyFloatingRotation(cart);
                        MinecartFloatDebugHelper.debugTick(cart, leftRails, midair, "deferSeatTick");
                    } else {
                        this.paradiseLost$exitGrace = 0;
                        MinecartRailConnectHelper.prepareReconnect(cart);
                        if (MinecartFloatStateHelper.isOffRail(cart)) {
                            this.paradiseLost$wasOffRail = true;
                            MinecartFloatRotationHelper.applyFloatingRotation(cart);
                            MinecartFloatDebugHelper.debugTick(cart, leftRails, midair, "deferSeatTick");
                        } else {
                            this.paradiseLost$wasOffRail = false;
                            MinecartFloatDebugHelper.debugTick(cart, false, false, "reconnect");
                        }
                    }
                } else {
                    if (!this.level().isClientSide()) {
                        if (railPos != null) {
                            MinecartRailConnectHelper.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                        }
                        MinecartFloatRotationHelper.applyFloatingRotation(cart);
                    } else {
                        paradiseLost$applyClientFloatPose(cart);
                    }
                    MinecartFloatDebugHelper.debugTick(cart, leftRails, midair,
                            settled ? "ghostRailLerp" : (midair ? "ghostRail" : "ghostRailLerp"));
                }
            } else if (keepFloat) {
                // Still high even though offRail is already false, keep the float pose.
                if (!this.level().isClientSide()) {
                    if (railPos != null) {
                        MinecartRailConnectHelper.adaptFloatToFlatRailBelow(cart, this.level().getBlockState(railPos));
                    }
                    MinecartFloatRotationHelper.applyFloatingRotation(cart);
                } else {
                    paradiseLost$applyClientFloatPose(cart);
                }
                MinecartFloatDebugHelper.debugTick(cart, leftRails, true,
                        unsettledFlat ? "ghostRailDesync" : "ghostRailPending");
            } else {
                this.paradiseLost$exitGrace = 0;
                this.paradiseLost$wasOffRail = false;
                if (!this.level().isClientSide()) {
                    MinecartFloatPoseHelper.captureRailState(cart);
                    if (MinecartFloatStateHelper.getIncline(cart) != 0) {
                        MinecartFloatPoseHelper.captureRailRenderPose(cart);
                        // Offs can spit out garbage near mixed geometry, clamp back to 45 degrees.
                        MinecartFloatPoseHelper.maintainSlopeFloatPose(cart);
                        if (MinecartFloatRenderHelper.shouldSkipRailRenderSnap(cart)) {
                            MinecartFloatRotationHelper.maintainSlopeEntityPitch(cart);
                        } else {
                            MinecartFloatRotationHelper.publishSlopePoseToSync(cart);
                        }
                    } else {
                        MinecartFloatPoseHelper.syncFlatFloatYawFromMotion(cart);
                    }
                } else {
                    paradiseLost$applyClientFloatPose(cart);
                }
                MinecartFloatDebugHelper.debugTick(cart, false, false, "onRail");
            }
            if (!this.level().isClientSide()) {
                MinecartFloatStateHelper.tick(cart);
            }
            return;
        }

        // Open air. Client still applies float pose when offRail sync is late.
        if (!this.level().isClientSide()) {
            boolean firstExit = !this.paradiseLost$wasOffRail;
            this.paradiseLost$beginFloatTakeoff(cart, "airTakeoff");
            if (firstExit && this.paradiseLost$wasOffRail) {
                Vec3 motion = this.getDeltaMovement();
                int incline = MinecartFloatStateHelper.getIncline(cart);
                if (incline > 0
                        || (incline < 0 && (MinecartRailLocatorHelper.findRailPos(cart) != null
                        || MinecartRailLookaheadHelper.shouldKeepOpenAirDescent(cart)))) {
                    this.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
                } else if (incline < 0) {
                    // No landing in sight, hold height, keep incline for the next look-ahead tick.
                    this.setDeltaMovement(motion.x, 0.0D, motion.z);
                }
            }
            if (this.paradiseLost$wasOffRail) {
                MinecartFloatRotationHelper.applyFloatingRotation(cart);
                if (firstExit) {
                    // comeOffTrack was skipped this tick (left during moveAlongTrack).
                    // Move now with slope noclip so we don't stick on the next stair pier.
                    this.paradiseLost$moveFloating(cart, true);
                }
            }
        } else if (MinecartFloatStateHelper.isOffRail(cart) || MinecartFloatStateHelper.isFloating(cart)) {
            paradiseLost$applyClientFloatPose(cart);
        }
        MinecartFloatDebugHelper.debugTick(cart, this.paradiseLost$wasOffRail, false, "air");
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

    /**
     * 45 degrees only if the rail under this client cart really is a slope, since the incline sync runs ahead.
     */
    @Unique
    private void paradiseLost$applyClientFloatPose(AbstractMinecart cart) {
        if (MinecartFloatRenderHelper.clientShouldHoldSlopePose(cart)) {
            MinecartFloatPoseHelper.maintainSlopeFloatPose(cart);
        } else {
            MinecartFloatRotationHelper.applySyncedFloatingRotation(cart);
        }
    }
}
