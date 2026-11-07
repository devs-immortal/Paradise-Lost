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

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Chooses the yaw and pitch drawn on the client, and when to leave that to vanilla.
 */
public final class MinecartFloatRenderHelper {
    private MinecartFloatRenderHelper() {
    }

    private static final Map<AbstractMinecart, float[]> CLIENT_MOTION_POSE = new WeakHashMap<>();

    // Client only: the last slope (incline, game time) the server told us about. The server levels the cart a few ticks
    // before the lerped client cart has finished the climb, so a climb that ended just now is still drawn as one.
    private static final Map<AbstractMinecart, long[]> CLIENT_LAST_INCLINE = new WeakHashMap<>();

    private static final long CLIENT_INCLINE_MEMORY_TICKS = 12L;

    /**
     * Client only. The server pose (incline, synced yaw/pitch) arrives several ticks before the lerped client cart gets
     * there, so drawing it makes the cart turn or tilt early. This works from what the client cart is doing instead:
     * close to a rail it follows that rail, otherwise it follows its own movement. Null while the cart is standing
     * still, then the normal pose logic applies.
     */
    @Nullable
    private static float[] clientMotionPose(AbstractMinecart cart) {
        if (!cart.level().isClientSide()) {
            return null;
        }
        long now = cart.level().getGameTime();
        int liveIncline = MinecartFloatStateHelper.getIncline(cart);
        long[] lastIncline = CLIENT_LAST_INCLINE.computeIfAbsent(cart, c -> new long[]{0L, Long.MIN_VALUE / 2});
        if (liveIncline != 0) {
            lastIncline[0] = liveIncline;
            lastIncline[1] = now;
        }
        int recentIncline = liveIncline != 0 ? liveIncline
                : (now - lastIncline[1] <= CLIENT_INCLINE_MEMORY_TICKS ? (int) lastIncline[0] : 0);
        double dx = cart.getX() - cart.xOld;
        double dy = cart.getY() - cart.yOld;
        double dz = cart.getZ() - cart.zOld;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        // xOld is stale while the client isn't ticking entities, so velocity has to agree too.
        if (horizontal < 0.04D || cart.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4D) {
            CLIENT_MOTION_POSE.remove(cart);
            return null;
        }
        float travelYaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI);
        float yaw = snapYawToAxis(travelYaw);
        float[] previous = CLIENT_MOTION_POSE.get(cart);
        if (previous != null && !isNearAxis(travelYaw)) {
            // Lerping across a turn looks diagonal for a few ticks, so keep the facing it had.
            yaw = previous[0];
        }
        float pitch;
        RailShape near = clientRailShapeNearUnderfoot(cart);
        if (near == null) {
            // Only a cart that is on a slope by its own doing tilts. A piston that pushes it up or down is a shove, not a
            // climb, and the cart keeps lying flat.
            pitch = slopePitchFromVertical(dy, horizontal, recentIncline);
        } else if (near.isAscending()) {
            float climbYaw = MinecartFloatPoseHelper.yawFromAscendingShape(near, 1);
            boolean towardHighEnd = Math.abs(Mth.wrapDegrees(travelYaw - climbYaw)) <= 90.0F;
            yaw = towardHighEnd ? climbYaw : Mth.wrapDegrees(climbYaw + 180.0F);
            pitch = towardHighEnd ? 44.9F : -44.9F;
        } else if (MinecartRailMotionHelper.isCornerRail(near)) {
            pitch = 0.0F;
        } else {
            // Seated on a flat landing, including the last lerp onto it. Leftover dy from the climb is not a slope.
            boolean eastWest = near == RailShape.EAST_WEST;
            double along = eastWest ? dx : dz;
            yaw = eastWest ? (along >= 0.0D ? 0.0F : 180.0F) : (along >= 0.0D ? 90.0F : -90.0F);
            pitch = 0.0F;
        }
        float[] last = CLIENT_MOTION_POSE.computeIfAbsent(cart, c -> new float[2]);
        last[0] = yaw;
        last[1] = pitch;
        return last;
    }

    /**
     * 45 degrees only while the cart is actually on a slope (or just was) and still moving that way. A shove that only
     * changes Y, such as a piston, has incline 0 and stays flat.
     */
    private static float slopePitchFromVertical(double dy, double horizontal, int incline) {
        if (incline == 0 || horizontal < MinecartRailMotionHelper.MOTION_EPSILON) {
            return 0.0F;
        }
        double ratio = dy / horizontal;
        if (incline > 0 && ratio > 0.3D) {
            return 44.9F;
        }
        if (incline < 0 && ratio < -0.3D) {
            return -44.9F;
        }
        return 0.0F;
    }

    private static boolean isNearAxis(float yaw) {
        return Math.abs(Mth.wrapDegrees(yaw - Math.round(yaw / 90.0F) * 90.0F)) <= 30.0F;
    }

    private static float snapYawToAxis(float yaw) {
        float nearestAxis = Math.round(yaw / 90.0F) * 90.0F;
        return Math.abs(Mth.wrapDegrees(yaw - nearestAxis)) <= 30.0F ? Mth.wrapDegrees(nearestAxis) : yaw;
    }

    /**
     * Shape of a rail the client cart is sitting on or just above, or null when it is clear of rails.
     */
    @Nullable
    private static RailShape clientRailShapeNearUnderfoot(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        for (BlockPos at : new BlockPos[]{pos, pos.below()}) {
            BlockState state = cart.level().getBlockState(at);
            if (!(state.getBlock() instanceof BaseRailBlock rail)) {
                continue;
            }
            if (MinecartRailLocatorHelper.isGhostSlopeBelow(cart, at, state)) {
                continue;
            }
            RailShape shape = state.getValue(rail.getShapeProperty());
            double height = cart.getY() - at.getY();
            double limit = shape.isAscending() ? MinecartRailLocatorHelper.ASCENDING_SEAT_HEIGHT : 0.8D;
            if (height >= -0.1D && height <= limit) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Yaw/pitch for MinecartRenderer when getPos is skipped (atan2 space, 0=east).
     */
    public static float getFloatRenderYaw(AbstractMinecart cart) {
        float[] seen = clientMotionPose(cart);
        if (seen != null) {
            return seen[0];
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float yaw = pose.paradiseLost$hasRailRenderPose()
                ? pose.paradiseLost$getRailRenderYaw()
                : MinecartFloatStateHelper.getSyncedFloatYaw(cart);
        // Sync/default yaw is 0=east; N/S slopes need +/-90 or mid-air looks 90 degrees off.
        if ((MinecartFloatStateHelper.getIncline(cart) != 0 || MinecartFloatStateHelper.isClimbMemory(cart) || MinecartFloatStateHelper.isOffRail(cart))
                && !MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, yaw)) {
            return MinecartFloatPoseHelper.slopeYawFromMotionOrPose(cart);
        }
        return yaw;
    }

    /**
     * If the cart is in the seat band over a straight flat that isn't a floor in the middle of a climb, clear the
     * incline and pitch. Returns true if it dealt with the pose.
     */
    static boolean flattenPoseOverStraightFlat(AbstractMinecart cart) {
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState state = cart.level().getBlockState(railPos);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape under = state.getValue(rail.getShapeProperty());
        if (under.isAscending() || MinecartRailMotionHelper.isCornerRail(under)) {
            return false;
        }
        double height = cart.getY() - railPos.getY();
        boolean nearSeat = MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)
                || MinecartRailConnectHelper.isNearFlatPoseAdoptHeight(cart, railPos)
                || (height >= 0.0D && height <= 0.55D);
        if (!nearSeat) {
            return false;
        }
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline > 0 && MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, state)) {
            return false;
        }
        // Flat crest before a descending run, or a climb with more slope ahead: keep the tilt.
        if (incline != 0 && slopePathStillContinues(cart, incline)) {
            return false;
        }
        if (incline == 0
                && Math.abs(MinecartFloatStateHelper.getSyncedFloatPitch(cart)) <= 0.01F
                && !MinecartFloatRotationHelper.poseHasSlopePitch(cart)) {
            return false;
        }
        MinecartFloatStateHelper.setIncline(cart, 0);
        MinecartFloatStateHelper.setRailShape(cart, under);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, under);
        MinecartFloatRotationHelper.clearFloatingPitch(cart);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
        MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        return true;
    }

    public static float getFloatRenderPitch(AbstractMinecart cart) {
        float[] seen = clientMotionPose(cart);
        if (seen != null) {
            return seen[1];
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        boolean settled = railPos != null && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
        // The climb incline is synced while the cart is still on the flat, so don't draw 45 degrees yet.
        if (cart.level().isClientSide() && MinecartFloatStateHelper.getIncline(cart) != 0 && !clientShouldHoldSlopePose(cart)) {
            return 0.0F;
        }
        float pitch = effectiveFloatPitch(cart);
        float yaw = getFloatRenderYaw(cart);
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        // Also reject wrong-sign 45 degrees for this shape.
        boolean pitchOk = Math.abs(Math.abs(pitch) - 45.0F) < 8.0F
                && MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, yaw)
                && (shape == null || !shape.isAscending()
                || Math.abs(pitch - MinecartFloatPoseHelper.pitchFromAscendingShape(shape, yaw)) <= 10.0F);
        // Keep the slope in mid-air even if the incline already synced to 0 (curves only).
        if (!settled && MinecartFloatRotationHelper.shouldKeepMidairSlopePose(cart, railPos) && pitchOk) {
            return pitch;
        }
        // Only while the incline is live. Climb memory alone tilted ceiling and flat takeoffs again.
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            if (pitchOk) {
                return pitch;
            }
            if (shape != null && shape.isAscending()) {
                return MinecartFloatPoseHelper.pitchFromAscendingShape(shape, yaw);
            }
            return MinecartFloatPoseHelper.synthSlopePitch(cart, yaw);
        }
        // The incline is already flat, so never keep a leftover 45 degrees (it used to stick to one direction in open air).
        return 0.0F;
    }

    /**
     * Client: hold 45 degrees while the incline is live on a slope path. A straight flat underfoot drops the tilt only
     * if it's a real landing (seat band, no slope continuing), not the flat crest before a descending run and not when
     * the cart is still high above a flat during a launch or gap.
     */
    public static boolean clientShouldHoldSlopePose(AbstractMinecart cart) {
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline == 0) {
            return false;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            BlockState state = cart.level().getBlockState(railPos);
            if (state.getBlock() instanceof BaseRailBlock rail) {
                RailShape under = state.getValue(rail.getShapeProperty());
                if (under.isAscending() || MinecartRailMotionHelper.isCornerRail(under)) {
                    return true;
                }
                // Straight flat underfoot.
                if (incline > 0 && MinecartFloatRotationHelper.isInterveningFlatWhileClimbing(cart, railPos, state)) {
                    return true;
                }
                // There is still slope ahead on the way down or up, so keep the tilt on the flat crest.
                if (slopePathStillContinues(cart, incline)) {
                    return true;
                }
                double height = cart.getY() - railPos.getY();
                boolean nearSeat = MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)
                        || MinecartRailConnectHelper.isNearFlatPoseAdoptHeight(cart, railPos)
                        || (height >= 0.0D && height <= 0.55D);
                // In the seat band of a real flat landing there is no slope tilt, still high over a flat cell
                // (launch or gap) the slope is kept.
                return !nearSeat;
            }
        }
        // Open air with no rail: keep the slope for the climb or descent.
        return true;
    }

    /**
     * True while an ascending rail, a solid slope or an open-air target is still ahead of the incline.
     */
    static boolean slopePathStillContinues(AbstractMinecart cart, int incline) {
        if (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart)) {
            return true;
        }
        if (incline < 0) {
            return MinecartRailLookaheadHelper.shouldKeepOpenAirDescent(cart) || MinecartRailLookaheadHelper.hasOpenAirSlopeTarget(cart);
        }
        if (incline > 0) {
            return MinecartRailLookaheadHelper.hasNearUnsettledFlatLanding(cart)
                    || MinecartRailLookaheadHelper.isBelowContinuingFlatLanding(cart)
                    || ((MinecartFloatPoseAccess) cart).paradiseLost$getOpenAirClimbGrace() > 0;
        }
        return false;
    }

    private static final float MAX_RENDER_YAW_STEP_PER_TICK = 40.0F;

    private static final Map<AbstractMinecart, RenderYawFilter> RENDER_YAW_FILTERS = new WeakHashMap<>();

    /**
     * Limits how fast the drawn yaw can change. The pose source flips between vanilla and ours around curves and slope
     * ends, and each source can disagree by tens of degrees for a tick, which shows up as the cart snapping round.
     * Following the wanted yaw at a bounded rate turns those snaps into a short smooth turn.
     * <p>
     * The cart looks the same from both ends, so a pose can also be drawn as yaw + 180 with the pitch negated. The
     * filter takes whichever of the two is closer to what is on screen, and {@link #pitchSign()} says which it took.
     * Without that, a climb would be drawn tilted down the slope whenever the two sources disagree about direction.
     */
    public static final class RenderYawFilter {
        private float yaw;
        private float time;
        private boolean set;
        private float pitchSign = 1.0F;
        /**
         * True from the moment the float pose was drawn until the vanilla pose has been caught up with.
         */
        private boolean easing;

        /**
         * A cart that is just riding rails with the vanilla pose is drawn exactly like vanilla draws it, however fast
         * that turns. The bounded rate only applies to the float pose and to the hand-over from it to the vanilla pose
         * (landing on a perpendicular rail), until the drawn yaw has caught up with the vanilla one.
         */
        public float apply(float wanted, float time, boolean floatPose) {
            if (!floatPose && !easing) {
                return reset(wanted, time);
            }
            float drawn = apply(wanted, time);
            if (floatPose) {
                easing = true;
                return drawn;
            }
            if (Math.min(
                    Math.abs(Mth.wrapDegrees(wanted - drawn)),
                    Math.abs(Mth.wrapDegrees(wanted + 180.0F - drawn))) < 1.0F) {
                return reset(wanted, time);
            }
            return drawn;
        }

        public float apply(float wanted, float time) {
            if (!set || time < this.time - 0.001F || time - this.time > 2.0F) {
                return reset(wanted, time);
            }
            float dt = time - this.time;
            if (dt <= 0.0001F) {
                return yaw;
            }
            float direct = Mth.wrapDegrees(wanted - yaw);
            float flipped = Mth.wrapDegrees(wanted + 180.0F - yaw);
            float diff;
            if (Math.abs(flipped) < Math.abs(direct)) {
                diff = flipped;
                pitchSign = -1.0F;
            } else {
                diff = direct;
                pitchSign = 1.0F;
            }
            float limit = MAX_RENDER_YAW_STEP_PER_TICK * dt;
            yaw = Mth.wrapDegrees(yaw + Mth.clamp(diff, -limit, limit));
            this.time = time;
            return yaw;
        }

        public float pitchSign() {
            return pitchSign;
        }

        private float reset(float wanted, float time) {
            yaw = wanted;
            this.time = time;
            set = true;
            easing = false;
            pitchSign = 1.0F;
            return yaw;
        }
    }

    /**
     * Client only. How much of the full slope tilt vanilla would draw at this spot, 0 for flat to 1 for a full slope,
     * or null when no rail is there. Vanilla looks 0.3 ahead and behind on the rail, so it eases into and out of a
     * slope over about half a block. The float pose is either flat or a full 45 degrees, so it was drawn up to a few
     * ticks late at the end of a slope. The client cart also trails the rail height a little while it catches up, so
     * the lookup is retried a bit higher when nothing is found.
     */
    @Nullable
    public static Float railSlopeScale(AbstractMinecart cart, double x, double y, double z) {
        if (!cart.level().isClientSide()) {
            return null;
        }
        // Float carts have getPos switched off while they are off the rail, this needs the real lookup.
        MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() + 1);
        try {
            for (double lift = 0.0D; lift <= 0.61D; lift += 0.3D) {
                double yy = y + lift;
                BlockPos railAt = BlockPos.containing(x, yy, z);
                if (!BaseRailBlock.isRail(cart.level().getBlockState(railAt))
                        || y - railAt.getY() < -0.7D || y - railAt.getY() > 0.9D) {
                    continue;
                }
                Vec3 at = cart.getPos(x, yy, z);
                if (at == null) {
                    continue;
                }
                Vec3 a = cart.getPosOffs(x, yy, z, 0.3F);
                Vec3 b = cart.getPosOffs(x, yy, z, -0.3F);
                if (a == null) {
                    a = at;
                }
                if (b == null) {
                    b = at;
                }
                double run = Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.z - a.z) * (b.z - a.z));
                double rise = Math.abs(b.y - a.y);
                if (run < 1.0E-4D && rise < 1.0E-4D) {
                    return null;
                }
                return (float) Math.min(1.0D, rise / Math.max(run, 1.0E-4D));
            }
            return null;
        } finally {
            MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    /**
     * Render yaw in degrees the smoothing filter last drew for this client cart, or NaN when there is none. Only the
     * rotation debug log reads this.
     */
    public static float renderYawFilterValue(AbstractMinecart cart) {
        RenderYawFilter filter = RENDER_YAW_FILTERS.get(cart);
        return filter == null || !filter.set ? Float.NaN : filter.yaw;
    }

    /**
     * -1 while the smoothed yaw is drawn the other way round (see {@link RenderYawFilter}), so the pitch has to flip.
     */
    public static float smoothRenderPitchSign(AbstractMinecart cart) {
        if (!cart.level().isClientSide() || !MinecartFloatStateHelper.isFloating(cart)) {
            return 1.0F;
        }
        RenderYawFilter filter = RENDER_YAW_FILTERS.get(cart);
        return filter == null ? 1.0F : filter.pitchSign();
    }

    /**
     * Render yaw in degrees as given to the renderer (180 minus the atan2 yaw), smoothed per cart.
     */
    public static float smoothRenderYawDegrees(AbstractMinecart cart, float wantedDegrees, float partialTicks, boolean floatPose) {
        if (!cart.level().isClientSide() || !MinecartFloatStateHelper.isFloating(cart)) {
            RENDER_YAW_FILTERS.remove(cart);
            return wantedDegrees;
        }
        return RENDER_YAW_FILTERS.computeIfAbsent(cart, c -> new RenderYawFilter())
                .apply(wantedDegrees, cart.tickCount + partialTicks, floatPose);
    }

    /**
     * Seated on a rail and still riding it. Vanilla offs already have the slope and the valley blend; our 45-degree
     * pose snaps as soon as levita charges or incline flips.
     */
    private static boolean isRidingRailLikeVanilla(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.isOffRail(cart)) {
            return false;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        return railPos != null
                && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)
                && !MinecartRailLocatorHelper.isMidairAboveRail(cart, railPos, false);
    }

    /**
     * Client only. True while the cart sits on a curve that vanilla would pick up by itself. The server incline for the
     * slope next to the curve arrives early, and our axis-snapped yaw would turn the cart before the curve does.
     */
    private static boolean isRidingCurveLikeVanilla(AbstractMinecart cart) {
        if (!cart.level().isClientSide() || MinecartFloatStateHelper.isOffRail(cart)) {
            return false;
        }
        double x = cart.getX();
        double y = cart.getY();
        double z = cart.getZ();
        if (isCurveCell(cart, x, y, z)) {
            return true;
        }
        // Vanilla samples the rail 0.3 ahead and behind, so it starts turning before the curve cell.
        MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() + 1);
        try {
            Vec3 ahead = cart.getPosOffs(x, y, z, 0.3F);
            Vec3 behind = cart.getPosOffs(x, y, z, -0.3F);
            return (ahead != null && isCurveCell(cart, ahead.x, ahead.y, ahead.z))
                    || (behind != null && isCurveCell(cart, behind.x, behind.y, behind.z));
        } finally {
            MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.set(MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    private static boolean isCurveCell(AbstractMinecart cart, double x, double y, double z) {
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        for (int dy = 0; dy >= -1; dy--) {
            BlockState state = cart.level().getBlockState(new BlockPos(i, j + dy, k));
            if (state.getBlock() instanceof BaseRailBlock rail
                    && MinecartRailMotionHelper.isCornerRail(state.getValue(rail.getShapeProperty()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldUseFloatRenderPose(AbstractMinecart cart) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        if (isRidingCurveLikeVanilla(cart) || isRidingRailLikeVanilla(cart)) {
            return false;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        // Settled on a flat or corner with the incline cleared: vanilla pose. Do this before the client motion
        // pose, or leftover climb dy after a piston-style shove or a landing lerp keeps 45 degrees on the flat.
        // Off-rail skips getPos, so a cart floating through a corner would otherwise fall back to a stale yaw.
        if (!MinecartFloatStateHelper.isOffRail(cart) && MinecartFloatStateHelper.getIncline(cart) == 0 && railPos != null && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail
                    && !railState.getValue(rail.getShapeProperty()).isAscending()) {
                return false;
            }
        }
        // The client cart is visibly climbing or diving, whatever the synced incline says right now.
        float[] seen = clientMotionPose(cart);
        if (seen != null && (Math.abs(seen[1]) > 1.0F || MinecartFloatStateHelper.isOffRail(cart))) {
            return true;
        }
        // A cart that has only just been put on a slope rail has no incline or tilt yet. Our pose would draw it flat
        // across the slope for a moment, vanilla tilts it along the rail it is sitting on.
        if (railPos != null && MinecartFloatStateHelper.getIncline(cart) == 0 && !MinecartFloatStateHelper.isOffRail(cart)
                && Math.abs(effectiveFloatPitch(cart)) <= 1.0F && !shouldSkipRailRenderSnap(cart)
                && cart.level().getBlockState(railPos).getBlock() instanceof BaseRailBlock seatRail
                && cart.level().getBlockState(railPos).getValue(seatRail.getShapeProperty()).isAscending()) {
            return false;
        }
        // Slope float always uses our pose, vanilla getPos flashes flat on reconnect.
        if (MinecartFloatStateHelper.getIncline(cart) != 0
                || MinecartFloatStateHelper.isClimbMemory(cart)
                || Math.abs(effectiveFloatPitch(cart)) > 1.0F) {
            return true;
        }
        return railPos == null || !MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
    }

    private static float effectiveFloatPitch(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float sync = MinecartFloatStateHelper.getSyncedFloatPitch(cart);
        if (pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F) {
            float local = pose.paradiseLost$getRailRenderPitch();
            RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
            if (shape == null || !shape.isAscending()) {
                return local;
            }
            float yaw = pose.paradiseLost$getRailRenderYaw();
            float expected = MinecartFloatPoseHelper.pitchFromAscendingShape(shape, yaw);
            if (Math.abs(local - expected) <= 10.0F) {
                return local;
            }
            // Local flipped vs shape, use sync if it matches.
            if (Math.abs(sync) > 1.0F && Math.abs(sync - expected) <= 10.0F) {
                return sync;
            }
            return expected;
        }
        return sync;
    }

    public static boolean shouldSkipRailRenderSnap(AbstractMinecart cart) {
        if (MinecartFloatPoseHelper.POS_CAPTURE_BYPASS.get() > 0) {
            return false;
        }
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        // Off-rail: never let getPos sample a nearby rail, crest/gap offs are often ~90 degrees wrong.
        if (MinecartFloatStateHelper.isOffRail(cart)) {
            return true;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null && MinecartRailLocatorHelper.isMidairAboveRail(cart, railPos, false)) {
            return true;
        }
        if (railPos != null && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
            return false;
        }
        // Slope float, don't let getPos sample a floor rail mid-gap.
        if (MinecartFloatStateHelper.getIncline(cart) != 0 || MinecartFloatStateHelper.isClimbMemory(cart) || Math.abs(effectiveFloatPitch(cart)) > 1.0F) {
            return true;
        }
        return MinecartRailLocatorHelper.isUnsettledAboveFlatRail(cart);
    }
}
