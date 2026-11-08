package net.id.paradise_lost.util.minecart;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.Map;

/**
 * Rail exit geometry, incline from motion, and steering velocity onto a rail.
 */
public final class MinecartRailMotionHelper {
    private MinecartRailMotionHelper() {
    }

    static final double MOTION_EPSILON = 0.05D;

    private static final double SLOPE_ASSIST_MIN_ALONG = MOTION_EPSILON;

    private static final double SIDEWAYS_DOT = 0.1D;

    /**
     * Horizontal alignment to treat float approach as parallel to the rail (carousel).
     */
    private static final double PARALLEL_DOT = 0.92D;

    static final Map<RailShape, Pair<Vec3i, Vec3i>> RAIL_EXITS = Map.ofEntries(
            Map.entry(RailShape.NORTH_SOUTH, Pair.of(Direction.NORTH.getNormal(), Direction.SOUTH.getNormal())),
            Map.entry(RailShape.EAST_WEST, Pair.of(Direction.WEST.getNormal(), Direction.EAST.getNormal())),
            Map.entry(RailShape.ASCENDING_EAST, Pair.of(Direction.WEST.getNormal().below(), Direction.EAST.getNormal())),
            Map.entry(RailShape.ASCENDING_WEST, Pair.of(Direction.WEST.getNormal(), Direction.EAST.getNormal().below())),
            Map.entry(RailShape.ASCENDING_NORTH, Pair.of(Direction.NORTH.getNormal(), Direction.SOUTH.getNormal().below())),
            Map.entry(RailShape.ASCENDING_SOUTH, Pair.of(Direction.NORTH.getNormal().below(), Direction.SOUTH.getNormal())),
            Map.entry(RailShape.SOUTH_EAST, Pair.of(Direction.SOUTH.getNormal(), Direction.EAST.getNormal())),
            Map.entry(RailShape.SOUTH_WEST, Pair.of(Direction.SOUTH.getNormal(), Direction.WEST.getNormal())),
            Map.entry(RailShape.NORTH_WEST, Pair.of(Direction.NORTH.getNormal(), Direction.WEST.getNormal())),
            Map.entry(RailShape.NORTH_EAST, Pair.of(Direction.NORTH.getNormal(), Direction.EAST.getNormal()))
    );

    public static void updateInclineFromRail(AbstractMinecart cart, RailShape shape, Vec3 motion) {
        MinecartFloatStateHelper.setRailShape(cart, shape);
        if (!shape.isAscending()) {
            MinecartFloatStateHelper.setIncline(cart, 0);
            return;
        }
        boolean goingUp = switch (shape) {
            case ASCENDING_EAST -> motion.x > MOTION_EPSILON;
            case ASCENDING_WEST -> motion.x < -MOTION_EPSILON;
            case ASCENDING_NORTH -> motion.z < -MOTION_EPSILON;
            case ASCENDING_SOUTH -> motion.z > MOTION_EPSILON;
            default -> false;
        };
        boolean goingDown = switch (shape) {
            case ASCENDING_EAST -> motion.x < -MOTION_EPSILON;
            case ASCENDING_WEST -> motion.x > MOTION_EPSILON;
            case ASCENDING_NORTH -> motion.z > MOTION_EPSILON;
            case ASCENDING_SOUTH -> motion.z < -MOTION_EPSILON;
            default -> false;
        };
        if (goingUp) {
            MinecartFloatStateHelper.setIncline(cart, 1);
            return;
        }
        if (goingDown) {
            MinecartFloatStateHelper.setIncline(cart, -1);
            return;
        }
        // Curve to slope transitions often lack a clear axis component (CW vs CCW). Pick by
        // which rail exit the motion aligns with (upper vs lower).
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return;
        }
        Vec3 horizontal = new Vec3(motion.x, 0.0D, motion.z);
        if (horizontal.lengthSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            return;
        }
        horizontal = horizontal.normalize();
        Vec3i a = exits.getFirst();
        Vec3i b = exits.getSecond();
        Vec3i upper = a.getY() >= b.getY() ? a : b;
        Vec3i lower = a.getY() < b.getY() ? a : b;
        double dotUpper = horizontalDot(horizontal, Vec3.atLowerCornerOf(upper));
        double dotLower = horizontalDot(horizontal, Vec3.atLowerCornerOf(lower));
        // Wider hysteresis, tiny exit-dot margins flipped incline every tick on carousel slopes.
        int current = MinecartFloatStateHelper.getIncline(cart);
        if (dotUpper > dotLower + 0.20D) {
            MinecartFloatStateHelper.setIncline(cart, 1);
        } else if (dotLower > dotUpper + 0.20D) {
            MinecartFloatStateHelper.setIncline(cart, -1);
        } else if (current == 0) {
            MinecartFloatStateHelper.setIncline(cart, dotUpper >= dotLower ? 1 : -1);
        }
        // else keep current incline (ambiguous motion)
    }

    /**
     * The two rails share an end. A slope's high end meets the rail one block up on that side, the low end meets the
     * rail straight ahead at the same height.
     */
    public static boolean railsConnect(Level level, BlockPos aPos, BlockPos bPos) {
        BlockState aState = level.getBlockState(aPos);
        BlockState bState = level.getBlockState(bPos);
        if (!(aState.getBlock() instanceof BaseRailBlock aRail) || !(bState.getBlock() instanceof BaseRailBlock bRail)) {
            return false;
        }
        return railsConnect(aPos, aState.getValue(aRail.getShapeProperty()), bPos, bState.getValue(bRail.getShapeProperty()));
    }

    public static boolean railsConnect(BlockPos aPos, RailShape aShape, BlockPos bPos, RailShape bShape) {
        Face[] aFaces = exitFaces(aPos, aShape);
        Face[] bFaces = exitFaces(bPos, bShape);
        for (Face a : aFaces) {
            if (a == null) {
                continue;
            }
            for (Face b : bFaces) {
                if (a.matches(b)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Face[] exitFaces(BlockPos pos, RailShape shape) {
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return new Face[0];
        }
        Vec3i first = exits.getFirst();
        Vec3i second = exits.getSecond();
        boolean ascending = shape.isAscending();
        boolean firstHigh = ascending && first.getY() >= second.getY();
        boolean secondHigh = ascending && second.getY() >= first.getY();
        return new Face[] { exitFace(pos, first, firstHigh), exitFace(pos, second, secondHigh) };
    }

    private static Face exitFace(BlockPos pos, Vec3i exit, boolean highEnd) {
        int dx = Integer.signum(exit.getX());
        int dz = Integer.signum(exit.getZ());
        if (dx == 0 && dz == 0) {
            return null;
        }
        BlockPos here = highEnd ? pos.above() : pos;
        return new Face(here, here.offset(dx, 0, dz));
    }

    private static final class Face {
        private final BlockPos a;
        private final BlockPos b;

        private Face(BlockPos a, BlockPos b) {
            this.a = a;
            this.b = b;
        }

        private boolean matches(Face other) {
            if (other == null) {
                return false;
            }
            return this.a.equals(other.a) && this.b.equals(other.b)
                    || this.a.equals(other.b) && this.b.equals(other.a);
        }
    }

    public static boolean headingDownSlope(RailShape shape, Vec3 motion) {
        return switch (shape) {
            case ASCENDING_EAST -> motion.x < -MOTION_EPSILON;
            case ASCENDING_WEST -> motion.x > MOTION_EPSILON;
            case ASCENDING_NORTH -> motion.z > MOTION_EPSILON;
            case ASCENDING_SOUTH -> motion.z < -MOTION_EPSILON;
            default -> false;
        };
    }

    static boolean isCornerRail(RailShape shape) {
        return shape == RailShape.NORTH_EAST
                || shape == RailShape.NORTH_WEST
                || shape == RailShape.SOUTH_EAST
                || shape == RailShape.SOUTH_WEST;
    }

    /**
     * Keep enough along-track speed on ascending rails. Levita flats don't boost ({@code railNeitherBoostsNorBrakes});
     * slope travel still needs the float charge meant to provide when the cart stays seated between stair steps.
     */
    public static void assistFloatingSlopeTravel(AbstractMinecart cart, BlockState railState) {
        if (!MinecartFloatStateHelper.isFloating(cart) || !(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (!shape.isAscending()) {
            return;
        }
        updateInclineFromRail(cart, shape, cart.getDeltaMovement());
        int incline = MinecartFloatStateHelper.getIncline(cart);
        // Only helps a cart that is climbing. A cart rolling down a slope (or drifting back after a stall) has gravity
        // for that and must not be flung off the bottom.
        if (incline <= 0) {
            return;
        }
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return;
        }
        Vec3i a = exits.getFirst();
        Vec3i b = exits.getSecond();
        Vec3i target = a.getY() >= b.getY() ? a : b;
        Vec3 dir = new Vec3(target.getX(), 0.0D, target.getZ());
        if (dir.lengthSqr() < 1.0E-8D) {
            return;
        }
        dir = dir.normalize();
        Vec3 motion = cart.getDeltaMovement();
        double along = motion.x * dir.x + motion.z * dir.z;
        double minSpeed = 0.42D;
        // Only keeps a cart that is already travelling up/down the slope from bleeding speed. A cart that is almost
        // stopped (or drifting with gravity) must not be flung to full speed by a single slope rail, that would make
        // the rail act like a powered one.
        if (along >= SLOPE_ASSIST_MIN_ALONG && along < minSpeed) {
            double add = minSpeed - along;
            cart.setDeltaMovement(motion.x + dir.x * add, motion.y, motion.z + dir.z * add);
        }
    }

    /**
     * Vanilla leaves a slope rail that the cart meets exactly sideways towards whichever side the last rounding error
     * of its motion points to, so up or down was random. Pin it to the rail's second exit (east/south), which is what
     * vanilla picks when the motion is exactly sideways.
     */
    public static void resolveSidewaysSlopeEntry(AbstractMinecart cart, BlockState railState) {
        if (!MinecartFloatStateHelper.isFloating(cart) || !(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        Pair<Vec3i, Vec3i> exits = shape.isAscending() ? RAIL_EXITS.get(shape) : null;
        if (exits == null) {
            return;
        }
        Vec3 motion = cart.getDeltaMovement();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        if (speed < 1.0E-4D) {
            return;
        }
        Vec3 first = new Vec3(exits.getFirst().getX(), 0.0D, exits.getFirst().getZ()).normalize();
        Vec3 second = new Vec3(exits.getSecond().getX(), 0.0D, exits.getSecond().getZ()).normalize();
        double dotFirst = (motion.x * first.x + motion.z * first.z) / speed;
        double dotSecond = (motion.x * second.x + motion.z * second.z) / speed;
        if (Math.abs(dotFirst) > SIDEWAYS_DOT || Math.abs(dotSecond) > SIDEWAYS_DOT) {
            return;
        }
        cart.setDeltaMovement(second.x * speed, motion.y, second.z * speed);
        MinecartFloatDebugHelper.debug(cart, "sideways", "shape=" + shape + " motion " + MinecartFloatDebugHelper.fmt(motion) + "->" + MinecartFloatDebugHelper.fmt(cart.getDeltaMovement()));
    }

    /**
     * True when horizontal motion already follows the rail axis (carousel parallel reconnect).
     */
    public static boolean isParallelToRail(AbstractMinecart cart, RailShape shape) {
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return false;
        }
        Vec3 approach = new Vec3(cart.getDeltaMovement().x, 0.0D, cart.getDeltaMovement().z);
        if (approach.lengthSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            // cart.yRot is MinecartRenderer/atan2 space (0=east), not living-entity look yaw.
            approach = MinecartFloatPoseHelper.railYawDirection(cart.getYRot());
        }
        if (approach.lengthSqr() <= 1.0E-8D) {
            return false;
        }
        approach = approach.normalize();
        Vec3 exitA = Vec3.atLowerCornerOf(exits.getFirst());
        Vec3 exitB = Vec3.atLowerCornerOf(exits.getSecond());
        Vec3 flatA = new Vec3(exitA.x, 0.0D, exitA.z);
        Vec3 flatB = new Vec3(exitB.x, 0.0D, exitB.z);
        if (flatA.lengthSqr() > 1.0E-8D) {
            flatA = flatA.normalize();
        }
        if (flatB.lengthSqr() > 1.0E-8D) {
            flatB = flatB.normalize();
        }
        return horizontalDot(approach, flatA) >= PARALLEL_DOT
                || horizontalDot(approach, flatB) >= PARALLEL_DOT;
    }

    public static void nudgeVelocityToRail(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (isCornerRail(shape)) {
            MinecartFloatStateHelper.setRailShape(cart, shape);
            MinecartFloatDebugHelper.debug(cart, "nudge", "skip corner shape=" + shape);
            return;
        }
        // Already parallel, keep XZ, just refresh incline/Y.
        if (isParallelToRail(cart, shape)) {
            MinecartFloatStateHelper.setRailShape(cart, shape);
            Vec3 motion = cart.getDeltaMovement();
            int before = MinecartFloatStateHelper.getIncline(cart);
            updateInclineFromRail(cart, shape, motion);
            int incline = MinecartFloatStateHelper.getIncline(cart);
            if (shape.isAscending() && incline != 0) {
                cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
            }
            if (before != incline && incline != 0 && shape.isAscending()) {
                MinecartFloatPoseHelper.captureRailRenderPose(cart, true);
                MinecartFloatRotationHelper.applyFloatingRotation(cart);
            }
            MinecartFloatDebugHelper.debug(cart, "nudge", "parallel skip XZ shape=" + shape
                    + " incline " + before + "->" + incline);
            return;
        }
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return;
        }
        Vec3 exitA = Vec3.atLowerCornerOf(exits.getFirst());
        Vec3 exitB = Vec3.atLowerCornerOf(exits.getSecond());
        if (exitA.lengthSqr() == 0.0D || exitB.lengthSqr() == 0.0D) {
            return;
        }
        exitA = exitA.normalize();
        exitB = exitB.normalize();

        Vec3 motion = cart.getDeltaMovement();
        Vec3 approach = new Vec3(motion.x, 0.0D, motion.z);
        if (approach.lengthSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            approach = MinecartFloatPoseHelper.railYawDirection(cart.getYRot());
        }
        approach = approach.normalize();

        double dotA = horizontalDot(approach, exitA);
        double dotB = horizontalDot(approach, exitB);
        Vec3 along;
        // Same pick as vanilla's moveAlongTrack: the exit the cart is heading towards, and for a cart that is exactly
        // sideways on to the rail, the rail's second exit (east/south), so uphill or downhill depends only on the
        // rail's orientation.
        along = dotA > dotB + 1.0E-4D ? exitA : exitB;

        MinecartFloatStateHelper.setRailShape(cart, shape);
        double speed = Math.max(motion.horizontalDistance(), MOTION_EPSILON);
        // Keep nudge horizontal. Y comes from incline, not from ascending exit normals.
        Vec3 flatAlong = new Vec3(along.x, 0.0D, along.z);
        if (flatAlong.lengthSqr() > 1.0E-8D) {
            flatAlong = flatAlong.normalize();
        } else {
            flatAlong = approach;
        }
        Vec3 newMotion = flatAlong.scale(speed);
        if (shape.isAscending()) {
            updateInclineFromRail(cart, shape, newMotion);
            int incline = MinecartFloatStateHelper.getIncline(cart);
            if (incline != 0) {
                newMotion = new Vec3(newMotion.x, incline * newMotion.horizontalDistance(), newMotion.z);
            }
        } else {
            updateInclineFromRail(cart, shape, newMotion);
        }
        cart.setDeltaMovement(newMotion);
        MinecartFloatDebugHelper.debug(cart, "nudge",
                "block=" + MinecartFloatDebugHelper.blockId(railState)
                        + " shape=" + shape
                        + " approach=" + MinecartFloatDebugHelper.fmt(approach)
                        + " along=" + MinecartFloatDebugHelper.fmt(flatAlong)
                        + " dots=" + String.format(Locale.ROOT, "%.2f/%.2f", dotA, dotB)
                        + " motion " + MinecartFloatDebugHelper.fmt(motion) + "->" + MinecartFloatDebugHelper.fmt(newMotion)
                        + " incline=" + MinecartFloatStateHelper.getIncline(cart));
    }

    static double horizontalDot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.z * b.z;
    }
}
