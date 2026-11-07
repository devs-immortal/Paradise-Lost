package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Captures and computes the yaw and pitch a floating cart should hold.
 */
public final class MinecartFloatPoseHelper {
    private MinecartFloatPoseHelper() {
    }

    static final ThreadLocal<Integer> POS_CAPTURE_BYPASS = ThreadLocal.withInitial(() -> 0);

    public static void captureRailState(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        BlockState state = cart.level().getBlockState(pos);
        BlockPos railPos = pos;
        if (!BaseRailBlock.isRail(state)) {
            railPos = pos.below();
            state = cart.level().getBlockState(railPos);
        }
        if (state.getBlock() instanceof BaseRailBlock rail) {
            if (MinecartRailLocatorHelper.isGhostSlopeBelow(cart, railPos, state)) {
                MinecartFloatDebugHelper.debug(cart, "captureRail", "ghost slope at " + railPos);
                return;
            }
            RailShape shape = state.getValue(rail.getShapeProperty());
            int beforeIncline = MinecartFloatStateHelper.getIncline(cart);
            String beforeShape = MinecartFloatStateHelper.getRailShapeName(cart);
            MinecartRailMotionHelper.updateInclineFromRail(cart, shape, cart.getDeltaMovement());
            MinecartFloatDebugHelper.debug(cart, "captureRail",
                    "railPos=" + railPos
                            + " block=" + MinecartFloatDebugHelper.blockId(state)
                            + " shape=" + shape
                            + " incline " + beforeIncline + "->" + MinecartFloatStateHelper.getIncline(cart)
                            + " storedShape " + beforeShape + "->" + MinecartFloatStateHelper.getRailShapeName(cart)
                            + " yAboveRail=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY()));
        } else {
            MinecartFloatDebugHelper.debug(cart, "captureRail", "no rail at " + pos + " / below");
        }
    }

    public static void captureRailRenderPose(AbstractMinecart cart) {
        captureRailRenderPose(cart, false);
    }

    /**
     * @param allowUnsettledAscending when true, midair over ascending still samples pose at the projected rail seat.
     *                                Used when incline flips (descend to climb) before the cart seats, cart.y offs are
     *                                unreliable high above the rail.
     */
    public static void captureRailRenderPose(AbstractMinecart cart, boolean allowUnsettledAscending) {
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            double x = cart.getX();
            double y = cart.getY();
            double z = cart.getZ();
            Vec3 mid = cart.getPos(x, y, z);
            if (mid == null) {
                return;
            }
            Vec3 posOff = cart.getPosOffs(x, y, z, 0.30000001192092896D);
            Vec3 negOff = cart.getPosOffs(x, y, z, -0.30000001192092896D);
            if (posOff == null) {
                posOff = mid;
            }
            if (negOff == null) {
                negOff = mid;
            }
            // Match MinecartRenderer exactly: dir = negOff - posOff, yaw=atan2(dz,dx), pitch=atan(dy)*73.
            Vec3 dir = negOff.subtract(posOff);
            boolean usedMotionDir = false;
            if (dir.lengthSqr() < 1.0E-6D) {
                Vec3 motion = cart.getDeltaMovement();
                if (motion.horizontalDistanceSqr() < 1.0E-6D) {
                    return;
                }
                dir = new Vec3(motion.x, 0.0D, motion.z).normalize();
                usedMotionDir = true;
            } else {
                dir = dir.normalize();
            }
            float yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI));
            // Flat curves can sample a bogus dy from neighboring slopes, only keep pitch on ascents.
            float pitch = 0.0F;
            BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
            RailShape shape = null;
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail) {
                    shape = railState.getValue(rail.getShapeProperty());
                    if (shape.isAscending()) {
                        // High above an ascending (e.g. transition onto a corner) samples garbage
                        // offs and can leave stale slope pitch on the next rail. Only capture when
                        // seated, unless incline-flip needs a midair refresh, in which case sample
                        // at the projected rail seat instead.
                        if (!MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
                            if (!allowUnsettledAscending) {
                                return;
                            }
                            Vec3 seatPosOff = cart.getPosOffs(mid.x, mid.y, mid.z, 0.30000001192092896D);
                            Vec3 seatNegOff = cart.getPosOffs(mid.x, mid.y, mid.z, -0.30000001192092896D);
                            if (seatPosOff == null) {
                                seatPosOff = mid;
                            }
                            if (seatNegOff == null) {
                                seatNegOff = mid;
                            }
                            Vec3 seatDir = seatNegOff.subtract(seatPosOff);
                            if (seatDir.lengthSqr() < 1.0E-6D) {
                                return;
                            }
                            dir = seatDir.normalize();
                            usedMotionDir = false;
                            yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI));
                            float trialPitch = (float) (Math.atan(dir.y) * 73.0D);
                            // Seat sample still unusable, keep prior pose.
                            if (Math.abs(trialPitch) < 25.0F || Math.abs(trialPitch) > 60.0F) {
                                return;
                            }
                        }
                        // Match on-rail offs. If motion faces the other way, flip yaw and pitch together.
                        float offsYaw = yaw;
                        pitch = (float) (Math.atan(dir.y) * 73.0D);
                        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                                ? 0.0F
                                : 90.0F;
                        // Bad offs sample while floating, keep the last good 45 degrees instead of publishing a shallow tilt.
                        boolean keptPriorSlope = false;
                        if (MinecartFloatStateHelper.isFloating(cart) && MinecartFloatStateHelper.getIncline(cart) != 0
                                && (Math.abs(pitch) < 40.0F || Math.abs(pitch) > 50.0F)) {
                            MinecartFloatPoseAccess priorPose = (MinecartFloatPoseAccess) cart;
                            if (hasCredibleSlopeLaunchPitch(cart, priorPose)) {
                                pitch = priorPose.paradiseLost$getRailRenderPitch();
                                yaw = priorPose.paradiseLost$getRailRenderYaw();
                                keptPriorSlope = true;
                            } else {
                                pitch = synthSlopePitch(cart, yawAlongMotion(cart, axis));
                            }
                        }
                        float motionYaw = yawAlongMotion(cart, axis);
                        boolean motionDisagrees = false;
                        if (!keptPriorSlope) {
                            motionDisagrees = Math.abs(Mth.wrapDegrees(motionYaw - offsYaw)) > 90.0F;
                            if (motionDisagrees) {
                                yaw = motionYaw;
                                pitch = -pitch;
                            } else {
                                yaw = nearestRailYaw(offsYaw, axis);
                            }
                        }
                        // Log when pose jumps or motion disagrees with offs (throttled vs every tick).
                        MinecartFloatPoseAccess prior = (MinecartFloatPoseAccess) cart;
                        boolean poseJump = !prior.paradiseLost$hasRailRenderPose()
                                || Math.abs(Mth.wrapDegrees(yaw - prior.paradiseLost$getRailRenderYaw())) > 5.0F
                                || Math.abs(pitch - prior.paradiseLost$getRailRenderPitch()) > 5.0F;
                        if (poseJump || usedMotionDir || allowUnsettledAscending) {
                            MinecartFloatDebugHelper.debug(cart, "slopeCapture",
                                    "shape=" + shape
                                            + " offs=" + String.format(Locale.ROOT, "%.1f/%.1f", offsYaw, pitch)
                                            + " motionYaw=" + String.format(Locale.ROOT, "%.1f", motionYaw)
                                            + " final=" + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                                            + " dir=" + MinecartFloatDebugHelper.fmt(dir)
                                            + " usedMotionDir=" + usedMotionDir
                                            + " motionDisagree=" + motionDisagrees
                                            + " unsettledForce=" + allowUnsettledAscending
                                            + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                                            + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement())
                                            + " yAbove=" + String.format(Locale.ROOT, "%.3f",
                                            cart.getY() - railPos.getY()));
                        }
                    } else if (shape == RailShape.EAST_WEST || shape == RailShape.NORTH_SOUTH) {
                        // Straight flat: offs near corners sampled yaw=-16.5 (carousel reconnect).
                        yaw = yawAlongMotion(cart, shape == RailShape.EAST_WEST ? 0.0F : 90.0F);
                    }
                }
            }
            // Stabilize against per-tick getPosOffs noise (carousel yaw/pitch flicker).
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                // Never pull ascending yaw toward a prior (possibly motion-locked) pose.
                if (shape == null || MinecartRailMotionHelper.isCornerRail(shape)) {
                    yaw = nearestRailYaw(pose.paradiseLost$getRailRenderYaw(), yaw);
                }
                float oldPitch = pose.paradiseLost$getRailRenderPitch();
                // Micro-jitter only (<5 degrees). Wider magnitude freeze can stick takeoff below the
                // true slope pitch from offs.
                if (Math.abs(pitch) > 1.0F && Math.abs(oldPitch) > 1.0F
                        && Math.signum(pitch) == Math.signum(oldPitch)
                        && Math.abs(pitch - oldPitch) < 5.0F) {
                    pitch = oldPitch;
                }
            }
            // Don't publish pitch=0 while we're still marked as on a slope.
            if (MinecartFloatStateHelper.getIncline(cart) != 0 && Math.abs(pitch) <= 1.0F) {
                if (hasCredibleSlopeLaunchPitch(cart, pose)) {
                    return;
                }
                // Synthesize here, do not call ensureAscendingFloatPose (it re-enters capture).
                pitch = synthSlopePitch(cart, yaw);
            }
            pose.paradiseLost$setRailRenderPose(yaw, pitch);
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    /**
     * Ensure slope yaw/pitch is stored for ascending takeoff / short rails. Prefer vanilla offs (including midair
     * seat-sample); if findRailPos is already gone, synthesize from shape+motion.
     */
    public static void ensureAscendingFloatPose(AbstractMinecart cart, RailShape shape) {
        if (shape == null || !shape.isAscending()) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                ? 0.0F
                : 90.0F;
        // A pitch of about 45 isn't enough, the wrong sign reads as a 90 degree tilt on NeoForge.
        if (hasCredibleSlopeLaunchPitch(cart, pose)) {
            return;
        }
        captureRailRenderPose(cart, true);
        if (hasCredibleSlopeLaunchPitch(cart, pose)) {
            return;
        }
        float yaw = yawAlongMotion(cart, axis);
        float pitch = pitchFromAscendingShape(shape, yaw);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
        MinecartFloatDebugHelper.debug(cart, "slopeSynth", "shape=" + shape
                + " yaw/pitch=" + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                + " incline=" + MinecartFloatStateHelper.getIncline(cart));
    }

    /**
     * Near 45 degrees and the right sign for this shape+yaw.
     */
    static boolean hasCredibleSlopeLaunchPitch(AbstractMinecart cart, MinecartFloatPoseAccess pose) {
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return false;
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        if (Math.abs(Math.abs(pitch) - 45.0F) >= 5.0F) {
            return false;
        }
        if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
            return false;
        }
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return Math.abs(pitch - pitchFromAscendingShape(shape, yaw)) <= 10.0F;
        }
        return true;
    }

    /**
     * Pitch of 45 degrees from the rail shape and yaw. Climbing is stored as +44.9, like the flipped E/W offsets.
     */
    static float synthSlopePitch(AbstractMinecart cart, float yaw) {
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return pitchFromAscendingShape(shape, yaw);
        }
        return MinecartFloatStateHelper.getIncline(cart) >= 0 ? 44.9F : -44.9F;
    }

    /**
     * Horizontal travel for atan2 yaw (0=east). NeoForge clients often have zero mid-air deltaMovement, so fall back to
     * xo/zo delta.
     */
    private static Vec3 horizontalTravelVector(AbstractMinecart cart) {
        Vec3 motion = cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);
        if (motion.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return motion;
        }
        double dx = cart.getX() - cart.xo;
        double dz = cart.getZ() - cart.zo;
        if (dx * dx + dz * dz > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return new Vec3(dx, 0.0D, dz);
        }
        return motion;
    }

    /**
     * Pose yaw on the travel axis (atan2, 0=east). Sync defaults to 0, don't treat "no motion" as aligned when the
     * stored shape is N/S.
     */
    static boolean isSlopeYawAlignedWithMotion(AbstractMinecart cart, float poseYaw) {
        Vec3 travel = horizontalTravelVector(cart);
        if (travel.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            float motionYaw = (float) (Math.atan2(travel.z, travel.x) * (180.0D / Math.PI));
            float delta = Math.abs(Mth.wrapDegrees(poseYaw - motionYaw));
            // Allow opposite travel on the same rail axis (pitch sign handles climb vs descend).
            return delta <= 35.0F || delta >= 145.0F;
        }
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            float expected = yawFromAscendingShape(shape, MinecartFloatStateHelper.getIncline(cart));
            float delta = Math.abs(Mth.wrapDegrees(poseYaw - expected));
            return delta <= 35.0F || delta >= 145.0F;
        }
        // No travel and no shape, can't prove misalignment.
        return true;
    }

    /**
     * Climb faces the high end of an ascending rail; descend faces the low end. Values are MinecartRenderer atan2 yaw
     * (0=east, 90=south).
     */
    static float yawFromAscendingShape(RailShape shape, int incline) {
        boolean climb = incline >= 0;
        return switch (shape) {
            case ASCENDING_EAST -> climb ? 0.0F : 180.0F;
            case ASCENDING_WEST -> climb ? 180.0F : 0.0F;
            case ASCENDING_NORTH -> climb ? -90.0F : 90.0F;
            default -> climb ? 90.0F : -90.0F;
        };
    }

    /**
     * atan2 yaw from travel, else shape+incline, else pose/sync.
     */
    static float slopeYawFromMotionOrPose(AbstractMinecart cart) {
        Vec3 travel = horizontalTravelVector(cart);
        if (travel.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return (float) (Math.atan2(travel.z, travel.x) * (180.0D / Math.PI));
        }
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return yawFromAscendingShape(shape, MinecartFloatStateHelper.getIncline(cart));
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()
                && isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
            return pose.paradiseLost$getRailRenderYaw();
        }
        float syncYaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
        if (Math.abs(syncYaw) > 0.01F || Math.abs(Math.abs(syncYaw) - 180.0F) <= 0.01F) {
            return syncYaw;
        }
        return entityYawToRailYaw(cart.getYRot());
    }

    /**
     * Keep float yaw/sync in MinecartRenderer (atan2) space on straight flats. On corners, only clear stale slope pitch
     * , do not overwrite curve yaw (vanilla getPos poses the curve).
     */
    public static void syncFlatFloatYawFromMotion(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        // Still seated on an ascending rail with slope pitch left over, so maintainSlope handles it.
        // Corners used to return early here as well, which left junctions stuck at 45 degrees.
        if (MinecartFloatStateHelper.isFloating(cart) && MinecartFloatRotationHelper.poseHasSlopePitch(cart)) {
            BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail
                        && railState.getValue(rail.getShapeProperty()).isAscending()) {
                    return;
                }
            } else if (MinecartFloatStateHelper.isOffRail(cart)) {
                return;
            }
        }
        MinecartFloatRotationHelper.clearFloatingPitch(cart);
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape != RailShape.EAST_WEST && shape != RailShape.NORTH_SOUTH) {
            // Corners and unknown shapes: only zero the pitch, keep the curve yaw.
            float yaw = pose.paradiseLost$hasRailRenderPose()
                    ? pose.paradiseLost$getRailRenderYaw()
                    : MinecartFloatStateHelper.getSyncedFloatYaw(cart);
            if (Math.abs(MinecartFloatStateHelper.getSyncedFloatPitch(cart)) > 0.01F
                    || (pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 0.01F)) {
                MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
                pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
                MinecartFloatDebugHelper.debug(cart, "cornerClear", "drop slope pitch yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                        + " shape=" + shape);
            }
            return;
        }
        float yaw = resolveFlatFloatYaw(cart, shape);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
    }

    /**
     * Prefer motion along the rail axis; if motion is cross-axis (perp approach), keep an existing on-axis float yaw
     * from flatAdapt/sync so reconnect does not flip between 0 and 180.
     */
    static float resolveFlatFloatYaw(AbstractMinecart cart, @Nullable RailShape shape) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float syncYaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
        float poseYaw = pose.paradiseLost$hasRailRenderPose()
                ? pose.paradiseLost$getRailRenderYaw()
                : syncYaw;
        Vec3 motion = cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);

        if (shape == RailShape.EAST_WEST || shape == RailShape.NORTH_SOUTH) {
            float axis = shape == RailShape.EAST_WEST ? 0.0F : 90.0F;
            boolean alongAxis = motion.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON
                    && (shape == RailShape.EAST_WEST
                    ? Math.abs(motion.x) >= Math.abs(motion.z)
                    : Math.abs(motion.z) >= Math.abs(motion.x));
            if (alongAxis) {
                return yawAlongMotion(cart, axis);
            }
            // Perp / weak motion: preserve flatAdapt yaw if it already sits on this axis.
            if (Math.abs(MinecartFloatStateHelper.getSyncedFloatPitch(cart)) <= 0.01F && isStraightRailAxisYaw(syncYaw, shape)) {
                return Mth.wrapDegrees(syncYaw);
            }
            if (pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 0.01F
                    && isStraightRailAxisYaw(poseYaw, shape)) {
                return Mth.wrapDegrees(poseYaw);
            }
            return yawAlongMotion(cart, axis);
        }

        if (motion.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            motion = motion.normalize();
            return (float) (Math.atan2(motion.z, motion.x) * (180.0D / Math.PI));
        }
        if (pose.paradiseLost$hasRailRenderPose()) {
            return poseYaw;
        }
        return entityYawToRailYaw(cart.getYRot());
    }

    /**
     * Force a credible 45 degrees pose whenever incline says we're on a slope.
     */
    public static void maintainSlopeFloatPose(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.getIncline(cart) == 0) {
            return;
        }
        // No slope tilt over a straight flat landing.
        if (MinecartFloatRenderHelper.flattenPoseOverStraightFlat(cart)) {
            return;
        }
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        if (shape == null || !shape.isAscending()) {
            // Incline is set but shape was lost, still show a slope tilt along motion.
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            float yaw = slopeYawFromMotionOrPose(cart);
            float pitch = synthSlopePitch(cart, yaw);
            if (hasCredibleSlopeLaunchPitch(cart, pose)) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
            MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, pitch);
            MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, pitch);
            pose.paradiseLost$setRailRenderPose(yaw, pitch);
            return;
        }
        ensureAscendingFloatPose(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        // Reject shallow offs samples, east-default yaw, or wrong-sign 45 degrees.
        if (!hasCredibleSlopeLaunchPitch(cart, pose)) {
            ensureAscendingFloatPose(cart, shape);
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
            yaw = slopeYawFromMotionOrPose(cart);
        }
        if (Math.abs(pitch) <= 1.0F
                || Math.abs(pitch - pitchFromAscendingShape(shape, yaw)) > 10.0F) {
            pitch = pitchFromAscendingShape(shape, yaw);
        }
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, pitch);
        MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, pitch);
    }

    /**
     * Stored float pitch for an ascending shape and the travel yaw (atan2, 0 = east). Climbing (toward the high end) is
     * always +44.9, which matches the flipped E/W getPosOffs. The old per-shape offsYaw table gave N/S climbs the
     * opposite sign from E/W.
     */
    static float pitchFromAscendingShape(RailShape shape, float yaw) {
        float climbYaw = yawFromAscendingShape(shape, 1);
        boolean towardHighEnd = Math.abs(Mth.wrapDegrees(yaw - climbYaw)) <= 90.0F;
        return towardHighEnd ? 44.9F : -44.9F;
    }

    /**
     * Float / MinecartRenderer yaw uses atan2(dz, dx): 0=east, 90=south, 180=west, -90=north. Do NOT use
     * {@link Vec3#directionFromRotation} here, that is entity look (0=south).
     */
    private static float yawAlongMotion(AbstractMinecart cart, float railYaw) {
        Vec3 motion = horizontalTravelVector(cart);
        float a = Mth.wrapDegrees(railYaw);
        float b = Mth.wrapDegrees(railYaw + 180.0F);
        if (motion.lengthSqr() < 1.0E-6D) {
            RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
            if (shape != null && shape.isAscending()) {
                return nearestRailYaw(yawFromAscendingShape(shape, MinecartFloatStateHelper.getIncline(cart)), railYaw);
            }
            return nearestRailYaw(entityYawToRailYaw(cart.getYRot()), railYaw);
        }
        motion = motion.normalize();
        Vec3 dirA = railYawDirection(a);
        Vec3 dirB = railYawDirection(b);
        double dotA = dirA.x * motion.x + dirA.z * motion.z;
        double dotB = dirB.x * motion.x + dirB.z * motion.z;
        return dotA >= dotB ? a : b;
    }

    /**
     * Unit vector for MinecartRenderer/atan2 yaw (0=east).
     */
    static Vec3 railYawDirection(float railYawDegrees) {
        double rad = Math.toRadians(railYawDegrees);
        return new Vec3(Math.cos(rad), 0.0D, Math.sin(rad));
    }

    /**
     * yRot is already renderer/atan2 space (0=east). Don't add +90 (that's look-yaw space).
     */
    private static float entityYawToRailYaw(float entityYaw) {
        return Mth.wrapDegrees(entityYaw);
    }

    static float nearestRailYaw(float currentYaw, float railYaw) {
        float a = Mth.wrapDegrees(railYaw);
        float b = Mth.wrapDegrees(railYaw + 180.0F);
        return Math.abs(Mth.wrapDegrees(a - currentYaw)) <= Math.abs(Mth.wrapDegrees(b - currentYaw))
                ? a
                : b;
    }

    static boolean isStraightRailAxisYaw(float yaw, RailShape shape) {
        float y = Mth.wrapDegrees(yaw);
        if (shape == RailShape.EAST_WEST) {
            return Math.abs(y) <= 1.0F || Math.abs(Math.abs(y) - 180.0F) <= 1.0F;
        }
        if (shape == RailShape.NORTH_SOUTH) {
            return Math.abs(Math.abs(y) - 90.0F) <= 1.0F;
        }
        return false;
    }
}
