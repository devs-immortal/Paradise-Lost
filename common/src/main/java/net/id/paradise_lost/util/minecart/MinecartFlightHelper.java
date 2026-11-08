package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

/**
 * Takeoff, open-air climb and descent, noclip, and leveling into a solid barrier.
 */
public final class MinecartFlightHelper {
    private MinecartFlightHelper() {
    }

    public static void prepareTakeoff(AbstractMinecart cart) {
        // Remember slope state first, captureRailState can overwrite it with a floor flat underfoot.
        RailShape priorShape = MinecartFloatStateHelper.getRailShape(cart);
        int priorIncline = MinecartFloatStateHelper.getIncline(cart);
        MinecartFloatPoseHelper.captureRailState(cart);
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        // Only keep the slope if the cart was really climbing or descending. An old ascending shape left
        // behind by levelFlight used to bring incline=1 back on the next flat takeoff.
        if (priorIncline != 0 && (shape == null || !shape.isAscending())) {
            // Underfoot flat is not the exit rail, keep the slope we left with.
            if (priorShape != null && priorShape.isAscending()) {
                MinecartFloatStateHelper.setRailShape(cart, priorShape);
                shape = priorShape;
            } else {
                shape = priorShape;
            }
            MinecartFloatStateHelper.setIncline(cart, priorIncline);
            if (shape != null && shape.isAscending()) {
                MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, shape);
                MinecartFloatRotationHelper.applyFloatingRotation(cart);
                armOpenAirClimbGrace(cart, priorIncline);
                MinecartFloatDebugHelper.takeoffSlopeKeep(cart);
                return;
            }
        }
        // Flat takeoff (or unknown): never carry slope pitch into the gap, photo regression.
        if (MinecartFloatStateHelper.getIncline(cart) == 0 || shape == null || !shape.isAscending()) {
            MinecartFloatPoseHelper.captureRailRenderPose(cart);
            MinecartFloatStateHelper.setIncline(cart, 0);
            // Forget the climb so the next gap doesn't start with a slope that isn't there.
            if (shape == null || shape.isAscending()) {
                MinecartFloatStateHelper.setRailShape(cart, "");
                shape = null;
            }
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            pose.paradiseLost$setOpenAirClimbGrace(0);
            float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
            MinecartFloatRotationHelper.clearFloatingPitch(cart);
            MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
            MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            MinecartFloatDebugHelper.takeoffFlat(cart, yaw);
        } else {
            // Short ascending rails often never settle long enough for gated capture, force
            // seat-sample / synthesize before freezing the air pose.
            MinecartFloatPoseHelper.ensureAscendingFloatPose(cart, shape);
            MinecartFloatRotationHelper.applyFloatingRotation(cart);
            armOpenAirClimbGrace(cart, MinecartFloatStateHelper.getIncline(cart));
            MinecartFloatDebugHelper.takeoffSlope(cart);
        }
    }

    /**
     * About 2.5 seconds of climbing in the open after leaving an ascending rail (slopeLaunchesUpward).
     */
    private static final int OPEN_AIR_CLIMB_GRACE_TICKS = 50;

    /**
     * Once the grace is over, only a ceiling this close keeps the cart rising. Far-off trees used to tilt it forever.
     */
    static final int IMMINENT_CEILING_BLOCKS = 3;

    private static final int CEILING_LOOKAHEAD_BLOCKS = 12;

    /**
     * Leaving a slope into open air keeps the slope (up or down) for at least the base grace, and for as long as the
     * cart still has float time left, so the distance flown grows with the number of powered rails it passed. Running
     * out of float, or hitting the ground or a ceiling, is what ends it.
     */
    private static void armOpenAirClimbGrace(AbstractMinecart cart, int incline) {
        if (incline == 0) {
            return;
        }
        int grace = Math.max(OPEN_AIR_CLIMB_GRACE_TICKS, MinecartFloatStateHelper.getFloatTime(cart));
        if (incline > 0) {
            int solidDist = MinecartRailLookaheadHelper.distanceToSolidAbove(cart, CEILING_LOOKAHEAD_BLOCKS);
            if (solidDist > IMMINENT_CEILING_BLOCKS) {
                grace = Math.max(grace, (solidDist - IMMINENT_CEILING_BLOCKS) * 10 + 20);
            }
        }
        ((MinecartFloatPoseAccess) cart).paradiseLost$setOpenAirClimbGrace(grace);
    }

    /**
     * If incline got cleared mid-gap but we're still climbing toward a target, put it back.
     */
    public static void maintainClimbTowardFlatLanding(AbstractMinecart cart) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return;
        }
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline > 0) {
            MinecartFloatPoseHelper.maintainSlopeFloatPose(cart);
            return;
        }
        // Don't turn a descent into a climb.
        if (incline < 0) {
            return;
        }
        BlockPos flatTarget = MinecartRailLookaheadHelper.findUnsettledFlatClimbTarget(cart);
        boolean climbMemory = MinecartFloatStateHelper.isClimbMemory(cart);
        if (flatTarget == null && !climbMemory) {
            return;
        }
        if (flatTarget != null) {
            double height = cart.getY() - flatTarget.getY();
            // Only restore while still under the pier, at/above is a descend landing.
            if (height >= 0.0D) {
                return;
            }
            if (!climbMemory) {
                return;
            }
            MinecartFloatStateHelper.setIncline(cart, 1);
            MinecartFloatPoseHelper.maintainSlopeFloatPose(cart);
            MinecartFloatDebugHelper.climbRestore(cart, flatTarget, height);
            return;
        }
        // Still aiming at an ascending lip in open air.
        if (MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) || MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart)) {
            MinecartFloatStateHelper.setIncline(cart, 1);
            MinecartFloatPoseHelper.maintainSlopeFloatPose(cart);
            MinecartFloatDebugHelper.climbRestoreAhead(cart);
        }
    }

    /**
     * Level out: keep xz, zero dy, clear slope. Used against solid with no rail to seat on.
     */
    public static void levelOpenAirFlight(AbstractMinecart cart) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x, 0.0D, motion.z);
        MinecartFloatStateHelper.setIncline(cart, 0);
        MinecartFloatRotationHelper.clearFloatingPitch(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        // Prefer the flat underfoot over a remembered approach shape for yaw.
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape under = railState.getValue(rail.getShapeProperty());
                if (!under.isAscending() && !MinecartRailMotionHelper.isCornerRail(under)) {
                    shape = under;
                    MinecartFloatStateHelper.setRailShape(cart, under);
                    if (!MinecartRailMotionHelper.isParallelToRail(cart, under)) {
                        MinecartRailMotionHelper.nudgeVelocityToRail(cart, railState);
                    }
                }
            }
        }
        // Clear the ascending climb memory, or the next flat takeoff brings the 45 degrees back.
        if (shape != null && shape.isAscending()) {
            MinecartFloatStateHelper.setRailShape(cart, "");
            shape = null;
        }
        float yaw = MinecartFloatPoseHelper.resolveFlatFloatYaw(cart, shape);
        MinecartFloatStateHelper.setSyncedFloatPose(cart, yaw, 0.0F);
        MinecartFloatRotationHelper.snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        MinecartFloatDebugHelper.levelFlight(cart, yaw, shape);
    }

    /**
     * Pressed against solid with no rail to seat on: a floor, a ceiling, or (while climbing or diving) a wall. Skipped
     * while a landing/stair is still ahead.
     *
     * @param intended the motion the cart tried to make this tick. A wall zeroes the motion that is left on the cart
     *                 after the move, so only this tells whether the cart was going anywhere.
     */
    public static boolean shouldLevelAgainstSolidBarrier(AbstractMinecart cart, boolean onGround, boolean verticalCollision,
                                                         boolean horizontalCollision, Vec3 intended) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        // A level cart that meets a wall just stops, it has no slope to flatten.
        boolean wall = horizontalCollision && MinecartFloatStateHelper.getIncline(cart) != 0;
        if (!onGround && !verticalCollision && !wall) {
            return false;
        }
        // A cart that has stopped has nothing to flatten out of.
        Vec3 moving = wall ? intended : cart.getDeltaMovement();
        if (moving.horizontalDistanceSqr() < MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return false;
        }
        // A wall in the way ends the slope even when the cart is on a slope rail that runs into it.
        if (wall && MinecartRailLookaheadHelper.isWallAhead(cart, moving)) {
            return true;
        }
        if (MinecartRailLocatorHelper.findRailPos(cart) != null) {
            return false;
        }
        // Touching something sideways that is not a wall in the way (a grazed edge, a stair tread) is no reason to level.
        if (!onGround && !verticalCollision) {
            return false;
        }
        // A cart diving off the end of a slope clips the support block under the slope's low end with the edge of its
        // hitbox. That is not the ground: only a floor under the middle of the cart ends the dive.
        if (MinecartFloatStateHelper.getIncline(cart) < 0) {
            BlockPos under = BlockPos.containing(cart.getX(), cart.getY() - 0.05D, cart.getZ());
            if (cart.level().getBlockState(under).getCollisionShape(cart.level(), under).isEmpty()) {
                return false;
            }
        }
        // Still climbing into a real landing, don't level out.
        // Do not key off climb-memory alone: remembered ascending shape after the rails end
        // (ceiling / floor skim / top of a solid stair run) must still flatten.
        if (MinecartRailLookaheadHelper.hasNearUnsettledFlatLanding(cart) || MinecartRailLookaheadHelper.isBelowContinuingFlatLanding(cart)) {
            return false;
        }
        // Only level into dead solid, not a stair skim.
        return !MinecartRailLookaheadHelper.hasContinuingRailAhead(cart) && !MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart);
    }

    /**
     * If collision killed xz mid-slope but more slope/stair is ahead, nudge forward. Flat landings ahead don't get this
     * assist.
     */
    public static void assistOpenAirSlopeTravel(AbstractMinecart cart) {
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline == 0 || MinecartRailLocatorHelper.findRailPos(cart) != null) {
            return;
        }
        if (!MinecartRailLookaheadHelper.hasContinuingAscendingRailAhead(cart) && !MinecartRailLookaheadHelper.hasContinuingSlopeSolidAhead(cart)) {
            return;
        }
        Vec3 motion = cart.getDeltaMovement();
        double minSpeed = 0.32D;
        if (motion.horizontalDistance() >= minSpeed) {
            return;
        }
        float railYaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()) {
            railYaw = pose.paradiseLost$getRailRenderYaw();
        }
        Vec3 along = MinecartFloatPoseHelper.railYawDirection(railYaw);
        if (along.lengthSqr() <= 1.0E-8D) {
            return;
        }
        // railYaw points up-rail; descend flips it.
        if (incline < 0) {
            along = along.scale(-1.0D);
        }
        along = along.normalize();
        // A cart that is still crossing the slope (a flat run ending beside the foot of a perpendicular stair) must
        // not be turned onto it before it is over it: the rail is not under the cart yet, so the turn only drives it
        // sideways into the stair's pier and stops it dead.
        Vec3 heading = new Vec3(motion.x, 0.0D, motion.z);
        if (heading.lengthSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON && MinecartRailMotionHelper.horizontalDot(heading.normalize(), along) < 0.5D) {
            return;
        }
        // Restore climb/descend Y too, pier friction often zeros the whole vector.
        cart.setDeltaMovement(along.x * minSpeed, incline * minSpeed, along.z * minSpeed);
    }
}
