package net.id.paradise_lost.util.minecart;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Reconnects a floating cart to a rail and eases slope motion onto a landing.
 */
public final class MinecartRailConnectHelper {
    private MinecartRailConnectHelper() {
    }

    public static void prepareReconnect(AbstractMinecart cart) {
        // Redirect onto the landing before sampling yaw (tick-path seats can skip hard-seat).
        // Remember slope before captureRailState overwrites it.
        RailShape priorStoredShape = MinecartFloatStateHelper.getRailShape(cart);
        int priorIncline = MinecartFloatStateHelper.getIncline(cart);
        // Check for a continuing climb before captureRailState clears incline.
        boolean slopeLive = priorIncline > 0
                || (priorStoredShape != null && priorStoredShape.isAscending())
                || MinecartFloatRotationHelper.poseHasSlopePitch(cart);
        boolean ascendingClimbContinues = slopeLive
                && (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart));
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        BlockState railState = null;
        RailShape landedPreview = null;
        if (railPos != null) {
            railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                landedPreview = railState.getValue(rail.getShapeProperty());
            }
        }
        // Skip deep floor flats mid-climb; normal track flats should still seat.
        boolean interveningFloor = ascendingClimbContinues
                && priorIncline > 0
                && (landedPreview == null || !landedPreview.isAscending())
                && railPos != null
                && MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, railState);
        if (interveningFloor) {
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (priorStoredShape != null && priorStoredShape.isAscending()) {
                MinecartFloatStateHelper.setRailShape(cart, priorStoredShape);
            }
            MinecartFloatStateHelper.setIncline(cart, priorIncline);
            RailShape keep = MinecartFloatStateHelper.getRailShape(cart);
            if (keep != null && keep.isAscending()) {
                MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, keep);
                MinecartFloatRotationHelper.applyFloatingRotation(cart);
            } else if (MinecartFloatRotationHelper.poseHasSlopePitch(cart)) {
                MinecartFloatRotationHelper.applyFloatingRotation(cart);
            }
            MinecartFloatStateHelper.setOffRail(cart, true);
            MinecartFloatDebugHelper.debug(cart, "reconnect", "deferFlatClimb yaw/pitch="
                    + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + " ignored=" + landedPreview
                    + " keep=" + keep
                    + " incline=" + MinecartFloatStateHelper.getIncline(cart));
            return;
        }
        if (railState != null && railState.getBlock() instanceof BaseRailBlock) {
            MinecartRailMotionHelper.nudgeVelocityToRail(cart, railState);
        }
        MinecartFloatPoseHelper.captureRailState(cart);
        MinecartFloatStateHelper.setOffRail(cart, false);
        // Snap into vanilla rail seat, float often left Y at *.000 while seat is *.0625
        // (carousel looked like it hovered above the rail).
        MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() + 1);
        try {
            Vec3 snapped = cart.getPos(cart.getX(), cart.getY(), cart.getZ());
            if (snapped != null) {
                cart.setPos(snapped.x, snapped.y, snapped.z);
            }
        } finally {
            MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() - 1);
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        // Prefer whatever is actually under the cart.
        RailShape landed = MinecartFloatStateHelper.getRailShape(cart);
        if (railPos != null && railState.getBlock() instanceof BaseRailBlock rail) {
            landed = railState.getValue(rail.getShapeProperty());
            MinecartFloatStateHelper.setRailShape(cart, landed);
        }
        if (landed != null && landed.isAscending()) {
            // Landing on a slope, restore incline if a perp approach zeroed it.
            if (MinecartFloatStateHelper.getIncline(cart) == 0) {
                MinecartRailMotionHelper.updateInclineFromRail(cart, landed, cart.getDeltaMovement());
            }
            if (MinecartFloatStateHelper.getIncline(cart) == 0) {
                // Motion still cross-axis after nudge flicker, pick climb/descend from vertical speed.
                Vec3 motion = cart.getDeltaMovement();
                int incline = motion.y < -MinecartRailMotionHelper.MOTION_EPSILON ? -1 : 1;
                MinecartFloatStateHelper.setIncline(cart, incline);
                double speed = Math.max(motion.horizontalDistance(), MinecartRailMotionHelper.MOTION_EPSILON);
                Pair<Vec3i, Vec3i> exits = MinecartRailMotionHelper.RAIL_EXITS.get(landed);
                if (exits != null) {
                    Vec3i upper = exits.getFirst().getY() >= exits.getSecond().getY()
                            ? exits.getFirst() : exits.getSecond();
                    Vec3i lower = exits.getFirst().getY() < exits.getSecond().getY()
                            ? exits.getFirst() : exits.getSecond();
                    Vec3i pick = incline < 0 ? lower : upper;
                    Vec3 along = new Vec3(pick.getX(), 0.0D, pick.getZ());
                    if (along.lengthSqr() > 1.0E-8D) {
                        along = along.normalize().scale(speed);
                        cart.setDeltaMovement(along.x, incline * speed, along.z);
                    } else {
                        cart.setDeltaMovement(motion.x, incline * speed, motion.z);
                    }
                } else {
                    cart.setDeltaMovement(motion.x, incline * speed, motion.z);
                }
            } else {
                Vec3 motion = cart.getDeltaMovement();
                int incline = MinecartFloatStateHelper.getIncline(cart);
                cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
            }
            MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, landed);
            MinecartFloatRotationHelper.applyFloatingRotation(cart);
            MinecartFloatDebugHelper.debug(cart, "reconnect", "slope yaw/pitch="
                    + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + " shape=" + landed
                    + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                    + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
        } else if (landed != null && MinecartRailMotionHelper.isCornerRail(landed)) {
            // Seating on a corner always flattens the cart. Corner flyovers in the middle of a climb go through deferSeat
            // (keepSlopeFloatThroughCorner), not reconnect. cornerKeepSlope used to leave the cart at 45 degrees
            // on junctions because syncFlatFloatYawFromMotion wouldn't clear the curve pitch.
            MinecartFloatStateHelper.setIncline(cart, 0);
            pose.paradiseLost$setOpenAirClimbGrace(0);
            float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, landed);
            MinecartFloatRotationHelper.clearFloatingPitch(cart);
            MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
            MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            MinecartFloatDebugHelper.debug(cart, "reconnect", "cornerFlat yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + landed
                    + " from=" + priorStoredShape
                    + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
        } else {
            float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, landed);
            MinecartFloatRotationHelper.clearFloatingPitch(cart);
            MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
            MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            pose.paradiseLost$setOpenAirClimbGrace(0);
            MinecartFloatDebugHelper.debug(cart, "reconnect", "flat yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + MinecartFloatStateHelper.getRailShapeName(cart)
                    + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
        }
    }

    /**
     * Whether float motion should ignore block collision this tick (slope gaps, under-pier climbs). Descending onto a
     * flat landing stays collidable so we seat on top instead of phasing through.
     */
    public static boolean shouldUseSlopeNoclip(AbstractMinecart cart) {
        // Re-arm climb if levelFlight wiped incline mid-gap.
        MinecartFlightHelper.maintainClimbTowardFlatLanding(cart);
        int incline = MinecartFloatStateHelper.getIncline(cart);
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = railState.getValue(rail.getShapeProperty());
                if (shape.isAscending()) {
                    return incline != 0;
                }
                if (MinecartRailMotionHelper.isCornerRail(shape)) {
                    // Ghost corners until we're close enough to seat.
                    return incline != 0 && !MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
                }
                // Flat underfoot: noclip deep floors / under-pier climbs, but collide with normal track.
                if (incline > 0 && !MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
                    if (MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, railState)) {
                        return true;
                    }
                    return cart.getY() < railPos.getY() + 0.0625D;
                }
            }
        }
        // Open air toward more slope, keep noclip. A descent that is about to enter the floor must collide
        // so a disconnected slope ahead cannot pull the cart through the ground.
        if (incline != 0
                && (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart))
                && !MinecartRailLookaheadHelper.descendingIntoFloor(cart)) {
            return true;
        }
        // Still climbing up into a flat landing.
        return incline > 0 && MinecartRailLookaheadHelper.isBelowContinuingFlatLanding(cart);
    }

    /**
     * Brief noclip after leaving a rail so we clear the lip without digging into the pier.
     */
    public static boolean shouldUseExitGraceNoclip(AbstractMinecart cart) {
        if (MinecartRailLookaheadHelper.descendingIntoFloor(cart)) {
            return false;
        }
        if (MinecartFloatStateHelper.getIncline(cart) < 0) {
            return MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart)
                    || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart)
                    || MinecartRailLocatorHelper.findRailPos(cart) != null;
        }
        return MinecartRailLocatorHelper.findRailPos(cart) != null
                || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart)
                || MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart)
                || MinecartRailLookaheadHelper.isBelowContinuingFlatLanding(cart);
    }

    /**
     * Descent-only comeOffTrack noclip ticks to clear the exit pier. Keep short. 12 let carts coast above the next rail
     * (carousel "flies a little above").
     */
    public static int slopeDescentExitGraceTicks() {
        return 5;
    }

    static final double FLAT_CONNECT_HEIGHT = 0.28D;

    /**
     * Match {@link MinecartRailLocatorHelper#isSettledOnRail} flat threshold, flatten only when seating.
     */
    private static final double FLAT_POSE_ADOPT = 0.16D;

    public static boolean isNearFlatConnectHeight(AbstractMinecart cart, BlockPos railPos) {
        return cart.getY() - railPos.getY() <= FLAT_CONNECT_HEIGHT;
    }

    public static boolean isNearFlatPoseAdoptHeight(AbstractMinecart cart, BlockPos railPos) {
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return cart.getY() - railPos.getY() <= FLAT_POSE_ADOPT;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Curves are not flat landings, never adopt flat pose on corners (carousel).
        if (MinecartRailMotionHelper.isCornerRail(shape) || shape.isAscending()) {
            return false;
        }
        // Same moment as rail connection / settle, earlier adopt rotates before seating.
        return MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
    }

    public static boolean isFlatRailConnectTooEarly(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !isNearFlatConnectHeight(cart, railPos);
    }

    /**
     * Ghosting over a rail midair: ascending refreshes climb; flat redirects + adopts landing pose. Corners keep slope
     * tilt while we're still climbing into them.
     */
    public static void adaptFloatWhileGhosting(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (shape.isAscending()) {
            adaptFloatToAscendingRail(cart, railState);
            return;
        }
        if (MinecartRailMotionHelper.isCornerRail(shape)) {
            // Only keep the slope through a corner while the climb is still going; pitch left over
            // after levelling out used to leave junctions at 45 degrees.
            if (MinecartFloatStateHelper.getIncline(cart) != 0
                    && (MinecartFloatStateHelper.isClimbMemory(cart)
                    || MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart)
                    || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart))) {
                MinecartFloatRotationHelper.keepSlopeFloatThroughCorner(cart, MinecartRailLocatorHelper.findRailPos(cart), railState);
            } else if (MinecartFloatStateHelper.getIncline(cart) != 0 || MinecartFloatRotationHelper.poseHasSlopePitch(cart)) {
                MinecartFloatStateHelper.setIncline(cart, 0);
                float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
                MinecartFloatRotationHelper.clearFloatingPitch(cart);
                MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
                MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
                MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
                pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
                pose.paradiseLost$setOpenAirClimbGrace(0);
            }
            return;
        }
        // Line up with the landing before we settle, or we skim past on a cross-axis approach.
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            alignFloatTowardFlatLanding(cart, railPos, railState);
        }
        adaptFloatToFlatRailBelow(cart, railState);
    }

    /**
     * Near the seat: nudge onto a straight flat landing and face along it.
     */
    public static void alignFloatTowardFlatLanding(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        if (railPos == null || !(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (shape.isAscending() || MinecartRailMotionHelper.isCornerRail(shape)) {
            return;
        }
        // Don't yank onto a deep floor flat we're meant to fly past.
        if (MinecartFloatStateHelper.getIncline(cart) > 0 && MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return;
        }
        // Flat crest with more slope after it: don't flatten the pitch or incline yet.
        if (MinecartFloatStateHelper.getIncline(cart) != 0 && MinecartFloatRenderHelper.slopePathStillContinues(cart, MinecartFloatStateHelper.getIncline(cart))) {
            return;
        }
        if (MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
            return;
        }
        double height = cart.getY() - railPos.getY();
        // Only when we're about to seat.
        if (height < 0.0D || height > 0.55D) {
            return;
        }
        // Still over an ascending rail, leave flat landing alone.
        BlockPos underfoot = MinecartRailLocatorHelper.findRailPos(cart);
        if (underfoot != null && !underfoot.equals(railPos)) {
            BlockState underState = cart.level().getBlockState(underfoot);
            if (underState.getBlock() instanceof BaseRailBlock underRail
                    && underState.getValue(underRail.getShapeProperty()).isAscending()) {
                return;
            }
        }
        if (MinecartRailMotionHelper.isParallelToRail(cart, shape)) {
            return;
        }
        // Must be over the landing cell, not still on the approach.
        if (!MinecartRailLocatorHelper.isOverRailCell(cart, railPos, shape)) {
            return;
        }
        MinecartRailMotionHelper.nudgeVelocityToRail(cart, railState);
        // Flat landing: drop the slope pitch. Holding 45 degrees while aligning left carts
        // tilted on powered flats until they reached seat height, or even after.
        MinecartFloatStateHelper.setIncline(cart, 0);
        float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        MinecartFloatRotationHelper.clearFloatingPitch(cart);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
        MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        pose.paradiseLost$setOpenAirClimbGrace(0);
        MinecartFloatDebugHelper.debug(cart, "flatAlign", "yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                + " pitch=0"
                + " shape=" + shape
                + " yAbove=" + String.format(Locale.ROOT, "%.3f", height)
                + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
    }

    /**
     * Midair over ascending: refresh incline/Y (and pose on flip). Leaves XZ alone.
     */
    public static void adaptFloatToAscendingRail(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (!shape.isAscending()) {
            return;
        }
        int before = MinecartFloatStateHelper.getIncline(cart);
        RailShape priorShape = MinecartFloatStateHelper.getRailShape(cart);
        Vec3 motion = cart.getDeltaMovement();
        // Ambiguous horizontal motion: keep climb/descend, do not re-sample pose (carousel glitch).
        if (before != 0 && !hasClearAscendAxis(shape, motion)) {
            cart.setDeltaMovement(motion.x, before * motion.horizontalDistance(), motion.z);
            return;
        }
        MinecartRailMotionHelper.updateInclineFromRail(cart, shape, motion);
        int incline = MinecartFloatStateHelper.getIncline(cart);
        motion = cart.getDeltaMovement();
        if (incline != 0) {
            cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean flatPose = !pose.paradiseLost$hasRailRenderPose()
                || Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 1.0F;
        // Refresh when incline flips, or when we're somehow flat on a slope.
        if (incline != 0 && (before != incline || flatPose)) {
            // Force midair seat-sample: settle-gated capture no-ops high above the rail and can
            // leave the prior descending pose after an incline flip onto the opposite slope.
            float priorYaw = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderYaw() : 0.0F;
            float priorPitch = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderPitch() : 0.0F;
            MinecartFloatPoseHelper.captureRailRenderPose(cart, true);
            if (flatPose && !MinecartFloatPoseHelper.hasCredibleSlopeLaunchPitch(cart, pose)) {
                MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, shape);
            }
            // Seat sample can still fail midair, opposite ascending rails reverse offs yaw (~180 degrees)
            // while keeping slope pitch (south -90/-45 to north +90/-45). Only when the rail
            // reversed; same-shape incline flips correctly keep the prior offs pair.
            if (isOppositeAscending(priorShape, shape)
                    && pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(priorPitch) > 1.0F
                    && Math.abs(Mth.wrapDegrees(pose.paradiseLost$getRailRenderYaw() - priorYaw)) < 5.0F
                    && Math.abs(pose.paradiseLost$getRailRenderPitch() - priorPitch) < 5.0F) {
                float flippedYaw = Mth.wrapDegrees(priorYaw + 180.0F);
                float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                        ? 0.0F
                        : 90.0F;
                pose.paradiseLost$setRailRenderPose(MinecartFloatPoseHelper.nearestRailYaw(flippedYaw, axis), priorPitch);
                MinecartFloatDebugHelper.debug(cart, "ascendAdapt", "poseFlipYaw " + priorShape + "->" + shape
                        + " " + String.format(Locale.ROOT, "%.1f/%.1f", priorYaw, priorPitch)
                        + "->" + String.format(Locale.ROOT, "%.1f/%.1f",
                        pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch()));
            }
            MinecartFloatRotationHelper.applyFloatingRotation(cart);
            BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
            MinecartFloatDebugHelper.debug(cart, "ascendAdapt", "shape=" + shape
                    + " incline " + before + "->" + incline
                    + (flatPose ? " restoreFlatPose" : "")
                    + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement())
                    + " railPose=" + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + (railPos == null ? "" : " yAbove=" + String.format(Locale.ROOT, "%.3f",
                    cart.getY() - railPos.getY())));
        }
    }

    private static boolean isOppositeAscending(@Nullable RailShape a, @Nullable RailShape b) {
        if (a == null || b == null) {
            return false;
        }
        return (a == RailShape.ASCENDING_SOUTH && b == RailShape.ASCENDING_NORTH)
                || (a == RailShape.ASCENDING_NORTH && b == RailShape.ASCENDING_SOUTH)
                || (a == RailShape.ASCENDING_EAST && b == RailShape.ASCENDING_WEST)
                || (a == RailShape.ASCENDING_WEST && b == RailShape.ASCENDING_EAST);
    }

    private static boolean hasClearAscendAxis(RailShape shape, Vec3 motion) {
        return switch (shape) {
            case ASCENDING_EAST, ASCENDING_WEST -> Math.abs(motion.x) > MinecartRailMotionHelper.MOTION_EPSILON;
            case ASCENDING_NORTH, ASCENDING_SOUTH -> Math.abs(motion.z) > MinecartRailMotionHelper.MOTION_EPSILON;
            default -> false;
        };
    }

    public static void adaptFloatToFlatRailBelow(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Ascending + corners: not flat landings (flat adopt on curves breaks carousel yaw).
        if (shape.isAscending() || MinecartRailMotionHelper.isCornerRail(shape)) {
            return;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        // Flying past a deep floor, keep slope. Near a real track flat, adopt it.
        if (railPos != null
                && MinecartFloatStateHelper.getIncline(cart) > 0
                && (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart))
                && MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return;
        }
        if (railPos == null) {
            return;
        }
        // Flat crest while the slope continues (descending onto stairs and the like): don't take it yet.
        if (MinecartFloatStateHelper.getIncline(cart) != 0 && MinecartFloatRenderHelper.slopePathStillContinues(cart, MinecartFloatStateHelper.getIncline(cart))) {
            return;
        }
        double height = cart.getY() - railPos.getY();
        // Only near the seat band. Don't wipe the tilt while still climbing or launching high above.
        boolean nearSeat = MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)
                || isNearFlatPoseAdoptHeight(cart, railPos)
                || (height >= 0.0D && height <= 0.55D);
        if (!nearSeat) {
            return;
        }
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            MinecartFloatStateHelper.setIncline(cart, 0);
        }
        MinecartFloatStateHelper.setRailShape(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        if (!MinecartRailMotionHelper.isParallelToRail(cart, shape)) {
            MinecartRailMotionHelper.nudgeVelocityToRail(cart, railState);
        }
        float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
        boolean alreadyFlat = Math.abs(MinecartFloatStateHelper.getSyncedFloatPitch(cart)) <= 0.01F
                && (!pose.paradiseLost$hasRailRenderPose()
                || Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 0.01F)
                && MinecartFloatPoseHelper.isStraightRailAxisYaw(yaw, shape)
                && pose.paradiseLost$hasRailRenderPose()
                && Math.abs(Mth.wrapDegrees(pose.paradiseLost$getRailRenderYaw() - yaw)) <= 1.0F;
        if (alreadyFlat) {
            return;
        }
        MinecartFloatRotationHelper.clearFloatingPitch(cart);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
        MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
        MinecartFloatDebugHelper.debug(cart, "flatAdapt", "pitch=0 yaw=" + String.format(java.util.Locale.ROOT, "%.1f", yaw)
                + " shape=" + shape
                + " parallel=" + MinecartRailMotionHelper.isParallelToRail(cart, shape)
                + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                + " yAbove=" + String.format(java.util.Locale.ROOT, "%.3f", height)
                + " nearSeat=" + nearSeat
                + " motion=" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
    }

    public static double softenSlopeDescentOntoFlat(AbstractMinecart cart, double yMotion) {
        if (yMotion >= 0.0D || !MinecartFloatStateHelper.isFloating(cart)) {
            return yMotion;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos == null || !isNearFlatConnectHeight(cart, railPos)) {
            return yMotion;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return yMotion;
        }
        double height = cart.getY() - railPos.getY();
        double target = 0.0625D;
        if (height <= target) {
            return 0.0D;
        }
        double maxStep = Math.min(0.35D, height - target);
        return -Math.min(Math.abs(yMotion), maxStep);
    }

    /**
     * Climbing into a flat: rise until seat height, then drop onto it instead of flying level into the pier.
     */
    public static double softenSlopeAscentOntoFlat(AbstractMinecart cart, double yMotion) {
        if (yMotion <= 0.0D || !MinecartFloatStateHelper.isFloating(cart)) {
            return yMotion;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos == null) {
            BlockPos ahead = MinecartRailLookaheadHelper.findContinuingRailAhead(cart, false);
            if (ahead == null) {
                return yMotion;
            }
            BlockState aheadState = cart.level().getBlockState(ahead);
            if (!(aheadState.getBlock() instanceof BaseRailBlock rail)
                    || aheadState.getValue(rail.getShapeProperty()).isAscending()) {
                return yMotion;
            }
            // Deep floor under a later climb, keep going up.
            if (MinecartRailLookaheadHelper.isDeepFlatBelowClimb(cart, ahead, aheadState)
                    && (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart))) {
                return yMotion;
            }
            if (MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, ahead, aheadState)) {
                return yMotion;
            }
            // Flat track way below us isn't the landing.
            if (cart.getY() - ahead.getY() > 1.0D) {
                return yMotion;
            }
            return seatYMotionOntoFlat(cart, ahead, yMotion);
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return yMotion;
        }
        double height = cart.getY() - railPos.getY();
        // Deep floor underfoot, don't kill climb Y.
        if (height > 1.0D) {
            return yMotion;
        }
        if (MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return yMotion;
        }
        return seatYMotionOntoFlat(cart, railPos, yMotion);
    }

    private static double seatYMotionOntoFlat(AbstractMinecart cart, BlockPos railPos, double yMotion) {
        double target = 0.0625D;
        double height = cart.getY() - railPos.getY();
        if (height > target) {
            return -Math.min(0.35D, height - target);
        }
        return Math.min(yMotion, target - height);
    }

    public static double descentSpeedOntoFlatRail(AbstractMinecart cart, BlockPos railPos) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return 0.0D;
        }
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            return 0.0D;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return 0.0D;
        }
        if (MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
            return 0.0D;
        }
        double height = cart.getY() - railPos.getY();
        double target = 0.0625D;
        if (height <= target) {
            return 0.0D;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean slopePose = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
        boolean catchUp = !MinecartFloatStateHelper.isOffRail(cart) || slopePose;
        if (!catchUp && !isNearFlatConnectHeight(cart, railPos)) {
            return 0.0D;
        }
        double cap = catchUp ? 0.50D : 0.35D;
        return -Math.min(cap, Math.max(0.10D, height - target));
    }

    public static boolean shouldCancelTrackWhileFloating(
            AbstractMinecart cart, BlockPos railPos, BlockState railState, boolean leftRails) {
        if (MinecartRailLocatorHelper.isMidairAboveRail(cart, railPos, leftRails)) {
            return true;
        }
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
    }

    /**
     * moveAlongTrack drops a level cart onto a slope one block down. Put the old height and horizontal motion back.
     * Leave corner turns alone once the center is over the cell.
     */
    public static void correctVanillaTrackSnap(AbstractMinecart cart,
                                               double enterX, double enterY, double enterZ, Vec3 enterMotion) {
        if (!MinecartFloatStateHelper.isFloating(cart) || enterMotion == null) {
            return;
        }
        keepFlightHeightOverGhostSlope(cart, enterX, enterY, enterZ, enterMotion);
    }

    private static void keepFlightHeightOverGhostSlope(AbstractMinecart cart,
                                                       double enterX, double enterY, double enterZ, Vec3 enterMotion) {
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            return;
        }
        BlockPos snapped = MinecartRailLocatorHelper.railAtOrBelow(cart.level(), cart.getX(), cart.getY(), cart.getZ());
        if (snapped == null) {
            return;
        }
        BlockState snappedState = cart.level().getBlockState(snapped);
        if (!MinecartRailLocatorHelper.isGhostSlopeBelow(cart, snapped, snappedState, enterX, enterY, enterZ, enterMotion)) {
            return;
        }
        cart.setPos(cart.getX(), enterY, cart.getZ());
        cart.setDeltaMovement(enterMotion.x, 0.0D, enterMotion.z);
        MinecartFloatStateHelper.setIncline(cart, 0);
        MinecartFloatDebugHelper.debug(cart, "ghostSlope", "kept flight height over " + snapped);
    }

    public static boolean isCornerShape(BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        return MinecartRailMotionHelper.isCornerRail(state.getValue(rail.getShapeProperty()));
    }

    /**
     * Touching a corner isn't being on it. Vanilla snaps to the curve at the block edge and the cart turns early.
     * Wait until the center is over the cell.
     */
    public static boolean shouldWaitToEnterRail(AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (!MinecartFloatStateHelper.isFloating(cart) || !(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        if (!MinecartRailMotionHelper.isCornerRail(shape)) {
            return false;
        }
        double dx = cart.getX() - (pos.getX() + 0.5D);
        double dz = cart.getZ() - (pos.getZ() + 0.5D);
        return dx * dx + dz * dz > 0.3D * 0.3D;
    }
}
