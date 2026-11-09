package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.Nullable;

/**
 * Applies the float pose to the minecart and decides when a slope pose should stick.
 */
public final class MinecartFloatRotationHelper {
    private MinecartFloatRotationHelper() {
    }

    public static void applyFloatingRotation(AbstractMinecart cart) {
        // Over a real flat landing, never publish 45 degrees again from a leftover incline.
        if (MinecartFloatRenderHelper.flattenPoseOverStraightFlat(cart)) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float yaw;
        float pitch = 0.0F;
        if (pose.paradiseLost$hasRailRenderPose()) {
            yaw = pose.paradiseLost$getRailRenderYaw();
            if (shouldKeepSlopePitch(cart)) {
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
        } else {
            yaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
            pitch = MinecartFloatStateHelper.getSyncedFloatPitch(cart);
        }
        // Incline 0 means flat pitch.
        if (MinecartFloatStateHelper.getIncline(cart) == 0) {
            pitch = 0.0F;
        } else if (Math.abs(pitch) <= 1.0F) {
            // Still climbing, invent a 45 degrees if pose got wiped.
            RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
            if (shape != null && shape.isAscending()) {
                MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, shape);
                if (pose.paradiseLost$hasRailRenderPose()) {
                    yaw = pose.paradiseLost$getRailRenderYaw();
                    pitch = pose.paradiseLost$getRailRenderPitch();
                }
            }
            if (Math.abs(pitch) <= 1.0F) {
                pitch = MinecartFloatPoseHelper.synthSlopePitch(cart, yaw);
            }
        }
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    private static boolean shouldKeepSlopePitch(AbstractMinecart cart) {
        // While incline is set, always keep the slope pitch.
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            return true;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = railState.getValue(rail.getShapeProperty());
                if (MinecartRailMotionHelper.isCornerRail(shape)) {
                    return poseHasSlopePitch(cart);
                }
                if (!shape.isAscending()) {
                    // Incline already 0, stay tilted until we're in the adopt band.
                    return poseHasSlopePitch(cart) && !MinecartRailConnectHelper.isNearFlatPoseAdoptHeight(cart, railPos);
                }
            }
        }
        return false;
    }

    static boolean poseHasSlopePitch(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        return pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
    }

    public static void applySyncedFloatingRotation(AbstractMinecart cart) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return;
        }
        float yaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
        float pitch = MinecartFloatStateHelper.getSyncedFloatPitch(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean localSlope = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        boolean settled = railPos != null && MinecartRailLocatorHelper.isSettledOnRail(cart, railPos);
        // The climb incline got synced while the cart is still seated on a flat, so level the pitch.
        if (cart.level().isClientSide() && MinecartFloatStateHelper.getIncline(cart) != 0 && !MinecartFloatRenderHelper.clientShouldHoldSlopePose(cart)) {
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail) {
                    RailShape shape = railState.getValue(rail.getShapeProperty());
                    if (!shape.isAscending() && !MinecartRailMotionHelper.isCornerRail(shape)) {
                        yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
                    }
                }
            }
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            return;
        }
        if (MinecartFloatStateHelper.getIncline(cart) == 0) {
            // Incline already cleared on the server, keep local tilt until we actually seat.
            if (!settled && shouldKeepMidairSlopePose(cart, railPos)) {
                if (localSlope) {
                    yaw = pose.paradiseLost$getRailRenderYaw();
                    pitch = pose.paradiseLost$getRailRenderPitch();
                } else if (Math.abs(pitch) <= 1.0F) {
                    pitch = 0.0F;
                }
            } else {
                pitch = 0.0F;
            }
        } else if (Math.abs(pitch) <= 1.0F) {
            // Sync pitch lagging / missing while incline is live, never write flat over a slope.
            if (localSlope && MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            } else {
                RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
                if (shape != null && shape.isAscending()) {
                    MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, shape);
                    if (pose.paradiseLost$hasRailRenderPose()) {
                        yaw = pose.paradiseLost$getRailRenderYaw();
                        pitch = pose.paradiseLost$getRailRenderPitch();
                    }
                }
                if (Math.abs(pitch) <= 1.0F) {
                    pitch = MinecartFloatPoseHelper.synthSlopePitch(cart, yaw);
                }
                // Sync yaw defaults to 0 (east), realign to motion or we sit 90 degrees off on N/S slopes.
                if (!MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, yaw)) {
                    yaw = MinecartFloatPoseHelper.slopeYawFromMotionOrPose(cart);
                    if (Math.abs(Math.abs(pitch) - 45.0F) < 8.0F) {
                        pitch = MinecartFloatPoseHelper.synthSlopePitch(cart, yaw);
                    }
                }
            }
        } else if (!MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, yaw)) {
            // Sync pitch ok but yaw still the east default, fix axis without flattening.
            yaw = MinecartFloatPoseHelper.slopeYawFromMotionOrPose(cart);
            if (localSlope
                    && Math.abs(Math.abs(pose.paradiseLost$getRailRenderPitch()) - 45.0F) < 5.0F
                    && MinecartFloatPoseHelper.isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
        }
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    /**
     * True when we should keep floating instead of hard-seating on this flat/corner. Deep floors mid-climb get skipped;
     * normal track flats still seat.
     */
    public static boolean shouldDeferSlopeToCornerHardSeat(
            AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        if (shape.isAscending()) {
            return false;
        }
        boolean slopeLive = MinecartFloatStateHelper.getIncline(cart) != 0 || poseHasSlopePitch(cart) || MinecartFloatStateHelper.isClimbMemory(cart);
        if (!slopeLive) {
            return false;
        }
        // Only defer while actively climbing, descents seat on flats normally.
        if (MinecartFloatStateHelper.getIncline(cart) > 0
                && (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart))
                && isInterveningFlatWhileClimbing(cart, pos, state)) {
            return true;
        }
        if (!MinecartRailMotionHelper.isCornerRail(shape)) {
            return false;
        }
        // Corners: defer only while still above the seat band.
        return cart.getY() - pos.getY() > MinecartRailConnectHelper.FLAT_CONNECT_HEIGHT;
    }

    /**
     * Flat we should fly past while climbing (deep below, or through-flat under a higher lip). Same-level track flats
     * are not intervening.
     */
    static boolean isInterveningFlatWhileClimbing(
            AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (MinecartRailLookaheadHelper.isDeepFlatBelowClimb(cart, pos, state)) {
            return true;
        }
        double height = cart.getY() - pos.getY();
        // Close enough to seat, this is real track.
        if (height <= MinecartRailConnectHelper.FLAT_CONNECT_HEIGHT) {
            return false;
        }
        // High above a through-flat with a higher lip beyond it.
        return MinecartRailLocatorHelper.hasHigherClimbTarget(cart, pos);
    }

    /**
     * Keep slope pose/incline while we're deferring a flat/corner seat.
     */
    public static void keepSlopeFloatThroughCorner(AbstractMinecart cart, BlockPos pos, BlockState state) {
        RailShape remembered = MinecartFloatStateHelper.getRailShape(cart);
        if (remembered != null && remembered.isAscending()) {
            if (MinecartFloatStateHelper.getIncline(cart) == 0) {
                MinecartFloatStateHelper.setIncline(cart, cart.getDeltaMovement().y < -MinecartRailMotionHelper.MOTION_EPSILON ? -1 : 1);
            }
            MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, remembered);
            applyFloatingRotation(cart);
            return;
        }
        if (poseHasSlopePitch(cart)) {
            applyFloatingRotation(cart);
        }
    }

    /**
     * Once incline is 0, leftover tilt is only kept while the cart is unsettled over a curve during an active climb
     * (ascending memory or a slope that continues). Settled corners and carts that have already levelled out have to
     * drop the pitch, or junctions stay at 45 degrees.
     */
    static boolean shouldKeepMidairSlopePose(AbstractMinecart cart, @Nullable BlockPos railPos) {
        if (MinecartFloatStateHelper.getIncline(cart) != 0) {
            return true;
        }
        // Already leveled, don't resurrect tilt in empty air.
        if (railPos == null) {
            return false;
        }
        if (MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
            return false;
        }
        // Stored flat means we already adopted a landing, don't keep slope pitch from underfoot ascent.
        RailShape stored = MinecartFloatStateHelper.getRailShape(cart);
        if (stored == RailShape.NORTH_SOUTH || stored == RailShape.EAST_WEST) {
            return false;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Curves only while a climb is still in progress, not after levelFlight or a flat reconnect.
        return MinecartRailMotionHelper.isCornerRail(shape)
                && poseHasSlopePitch(cart)
                && (MinecartFloatStateHelper.isClimbMemory(cart)
                || MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart)
                || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart));
    }

    static void snapEntityRotation(AbstractMinecart cart, float yaw, float pitch) {
        applyEntityRotation(cart, yaw, pitch, true);
    }

    /**
     * Snap entity rotation in MinecartRenderer offs space (0=east). Don't convert to look-yaw , that makes a correct
     * slope look diagonal.
     */
    private static void applyEntityRotation(AbstractMinecart cart, float railYaw, float pitch, boolean hardSnap) {
        float yaw = Mth.wrapDegrees(railYaw);
        cart.setYRot(yaw);
        cart.setXRot(pitch);
        cart.xRotO = pitch;
        if (hardSnap) {
            cart.yRotO = yaw;
        }
    }

    public static void maintainSlopeEntityPitch(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.getIncline(cart) == 0) {
            return;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos == null) {
            return;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || !railState.getValue(rail.getShapeProperty()).isAscending()) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
    }

    /**
     * Push captured slope pose to sync without fighting vanilla on-rail entity rotation.
     */
    public static void publishSlopePoseToSync(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        MinecartFloatStateHelper.setSyncedFloatPose(cart,
                pose.paradiseLost$getRailRenderYaw(),
                pose.paradiseLost$getRailRenderPitch());
    }

    public static void clearFloatingPitch(AbstractMinecart cart) {
        cart.setXRot(0.0F);
        cart.xRotO = 0.0F;
    }
}
