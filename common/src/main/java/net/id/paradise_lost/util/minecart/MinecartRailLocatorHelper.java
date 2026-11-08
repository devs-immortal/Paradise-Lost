package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Finds the rail under a floating cart and whether the cart is actually seated on it.
 */
public final class MinecartRailLocatorHelper {
    private MinecartRailLocatorHelper() {
    }

    /**
     * Max Y above an ascending rail block while still on its seat curve (~0 at low end, ~1 at high).
     */
    static final double ASCENDING_SEAT_HEIGHT = 1.05D;

    /**
     * Above the band where an ascending rail can still be the one under the cart.
     */
    public static boolean isAboveAscendingSeat(AbstractMinecart cart, BlockPos railPos) {
        return cart.getY() - railPos.getY() > ASCENDING_SEAT_HEIGHT;
    }

    /**
     * Pitch from before moveAlongTrack rewrites the pose. A level cart is not on the slope under its rail.
     */
    public static void noteApproachPitch(AbstractMinecart cart) {
        APPROACH_PITCH.set(livePitch(cart));
    }

    public static void clearApproachPitch() {
        APPROACH_PITCH.remove();
    }

    public static float approachPitch(AbstractMinecart cart) {
        Float saved = APPROACH_PITCH.get();
        return saved != null ? saved : livePitch(cart);
    }

    public static float livePitch(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()) {
            return pose.paradiseLost$getRailRenderPitch();
        }
        return MinecartFloatStateHelper.getSyncedFloatPitch(cart);
    }

    /**
     * Flat enough that the cart is not riding a slope.
     */
    public static final float LEVEL_PITCH = 12.0F;

    private static final ThreadLocal<Float> APPROACH_PITCH = new ThreadLocal<>();

    /**
     * Roughly over the rail cell (within half a block sideways).
     */
    static boolean isOverRailCell(AbstractMinecart cart, BlockPos railPos, RailShape shape) {
        double cx = railPos.getX() + 0.5D;
        double cz = railPos.getZ() + 0.5D;
        if (shape == RailShape.EAST_WEST) {
            return Math.abs(cart.getZ() - cz) <= 0.45D
                    && Math.abs(cart.getX() - cx) <= 0.75D;
        }
        if (shape == RailShape.NORTH_SOUTH) {
            return Math.abs(cart.getX() - cx) <= 0.45D
                    && Math.abs(cart.getZ() - cz) <= 0.75D;
        }
        return Math.abs(cart.getX() - cx) <= 0.55D && Math.abs(cart.getZ() - cz) <= 0.55D;
    }

    public static boolean isUnsettledAboveFlatRail(AbstractMinecart cart) {
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !isSettledOnRail(cart, railPos);
    }

    /**
     * Slope one block under a cart flying level, not hooked up to the cart's height. The seat fills the whole
     * block, so this used to look occupied and the cart climbed or turned on a rail it never touched. Already on
     * a slope, or diving onto one, is real. The high end of a slope we're riding stays in that block. A rail up
     * there only counts once we're at it and heading down.
     */

    public static boolean isGhostSlopeBelow(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        return isGhostSlopeBelow(cart, railPos, railState, cart.getX(), cart.getY(), cart.getZ(), cart.getDeltaMovement());
    }

    /**
     * Same check, using the position from before vanilla snapped us down onto the slope.
     */
    public static boolean isGhostSlopeBelow(AbstractMinecart cart, BlockPos railPos, BlockState railState,
                                            double x, double y, double z, Vec3 motion) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // High end of a slope we're on is still inside that block. Ghost means the block above it.
        if (!shape.isAscending() || Mth.floor(y) <= railPos.getY()) {
            return false;
        }
        // Creeping along this slope, still inside its column. A fast cart one block up is flying over a different rail.
        if (Mth.floor(x) == railPos.getX() && Mth.floor(z) == railPos.getZ()
                && y - railPos.getY() <= 1.0D
                && motion.horizontalDistanceSqr() < MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return false;
        }
        // Level pose over a slope. Down a joined exit is the track. Up that slope is the yank.
        if (Math.abs(approachPitch(cart)) < LEVEL_PITCH) {
            return !headingDownJoinedSlope(cart, railPos, shape, x, y, z, motion);
        }
        // High end of the slope we're pitched on sits in the block above it. Still this rail.
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline > 0 || (incline < 0 && MinecartRailMotionHelper.headingDownSlope(shape, motion))) {
            return false;
        }
        BlockPos above = BlockPos.containing(x, y, z);
        BlockState aboveState = cart.level().getBlockState(above);
        if (aboveState.getBlock() instanceof BaseRailBlock aboveRail
                && !MinecartRailMotionHelper.railsConnect(above, aboveState.getValue(aboveRail.getShapeProperty()), railPos, shape)) {
            return true;
        }
        // Falling is not enough. The cart in the neoforge log was a block and a half above an
        // unconnected slope, already dropping, and this treated that slope as the one it was on.
        if (motion.y < -0.05D && (y - railPos.getY() <= ASCENDING_SEAT_HEIGHT
                || MinecartRailMotionHelper.headingDownSlope(shape, motion))) {
            return false;
        }
        Direction high = switch (shape) {
            case ASCENDING_EAST -> Direction.EAST;
            case ASCENDING_WEST -> Direction.WEST;
            case ASCENDING_NORTH -> Direction.NORTH;
            default -> Direction.SOUTH;
        };
        BlockPos junction = railPos.relative(high).above();
        if (!BaseRailBlock.isRail(cart.level().getBlockState(junction))) {
            return true;
        }
        // Rail at the high end. Ours only if we're actually there and already heading down.
        // Otherwise it's just a slope sitting under the flight path.
        double dx = x - (junction.getX() + 0.5D);
        double dz = z - (junction.getZ() + 0.5D);
        boolean atJunction = dx * dx + dz * dz <= 0.8D * 0.8D;
        if (!atJunction) {
            return true;
        }
        boolean headingDown = switch (shape) {
            case ASCENDING_EAST -> motion.x < -MinecartRailMotionHelper.MOTION_EPSILON;
            case ASCENDING_WEST -> motion.x > MinecartRailMotionHelper.MOTION_EPSILON;
            case ASCENDING_NORTH -> motion.z > MinecartRailMotionHelper.MOTION_EPSILON;
            case ASCENDING_SOUTH -> motion.z < -MinecartRailMotionHelper.MOTION_EPSILON;
            default -> false;
        };
        return !headingDown;
    }

    private static boolean headingDownJoinedSlope(AbstractMinecart cart, BlockPos railPos, RailShape shape,
                                                  double x, double y, double z, Vec3 motion) {
        if (!MinecartRailMotionHelper.headingDownSlope(shape, motion) && motion.y >= -0.05D) {
            return false;
        }
        BlockPos above = BlockPos.containing(x, y, z);
        BlockState aboveState = cart.level().getBlockState(above);
        if (!(aboveState.getBlock() instanceof BaseRailBlock aboveRail)) {
            // Air over the slope. Ours if we're already dropping, or its high end meets a rail at this height.
            return motion.y < -0.05D || joinedToRailAtHeight(cart.level(), railPos, shape, above.getY());
        }
        return MinecartRailMotionHelper.railsConnect(
                above, aboveState.getValue(aboveRail.getShapeProperty()), railPos, shape);
    }

    private static boolean joinedToRailAtHeight(Level level, BlockPos slopePos, RailShape shape, int y) {
        BlockPos up = slopePos.above();
        if (up.getY() == y && railConnectsAt(level, slopePos, shape, up)) {
            return true;
        }
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos beside = up.relative(dir);
            if (beside.getY() == y && railConnectsAt(level, slopePos, shape, beside)) {
                return true;
            }
        }
        return false;
    }

    private static boolean railConnectsAt(Level level, BlockPos slopePos, RailShape shape, BlockPos other) {
        BlockState state = level.getBlockState(other);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        return MinecartRailMotionHelper.railsConnect(slopePos, shape, other, state.getValue(rail.getShapeProperty()));
    }

    /**
     * Rail here, or the one directly below. Same pick as getPos.
     */
    @Nullable
    public static BlockPos railAtOrBelow(Level level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        if (BaseRailBlock.isRail(level.getBlockState(pos))) {
            return pos;
        }
        BlockPos below = pos.below();
        if (BaseRailBlock.isRail(level.getBlockState(below))) {
            return below;
        }
        return null;
    }

    public static boolean isSettledOnRail(AbstractMinecart cart, BlockPos railPos) {
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        if (isGhostSlopeBelow(cart, railPos, railState)) {
            return false;
        }
        double height = cart.getY() - railPos.getY();
        // Under the rail is never seated.
        if (height < 0.0D) {
            return false;
        }
        if (railState.getValue(rail.getShapeProperty()).isAscending()) {
            // Ascending seat Y spans ~0..1 within the rail cell. 0.45 only covers the low end.
            return height <= ASCENDING_SEAT_HEIGHT;
        }
        // A diving cart drops this far in its next tick. If it had to be within 0.16 to count as seated, it could step right
        // over a one-block rail across its path (a perpendicular landing) without ever being counted as seated.
        double limit = 0.16D;
        Vec3 motion = cart.getDeltaMovement();
        if (MinecartFloatStateHelper.isFloating(cart) && motion.y < -0.05D) {
            limit = Math.max(limit, -motion.y + 0.02D);
        }
        return height <= limit;
    }

    @Nullable
    public static BlockPos findRailPos(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        boolean climbFloat = MinecartFloatStateHelper.isFloating(cart) && MinecartFloatStateHelper.getIncline(cart) > 0;
        BlockState at = cart.level().getBlockState(pos);
        if (BaseRailBlock.isRail(at) && !isGhostSlopeBelow(cart, pos, at)) {
            // High above a through-flat with a higher target, ignore it. Otherwise it's the landing.
            if (!(climbFloat && isFlatStraightRail(at) && cart.getY() - pos.getY() > 0.45D
                    && hasHigherClimbTarget(cart, pos))) {
                return pos;
            }
        }
        BlockPos below = pos.below();
        BlockState belowState = cart.level().getBlockState(below);
        if (BaseRailBlock.isRail(belowState) && !isGhostSlopeBelow(cart, below, belowState)) {
            // Ignore rails far below so a floor track can't kill climb Y.
            // Ascending underfoot is different: seat Y spans ~0..1 in the same cell.
            if (climbFloat) {
                double height = cart.getY() - below.getY();
                boolean ascendingBelow = belowState.getBlock() instanceof BaseRailBlock belowRail
                        && belowState.getValue(belowRail.getShapeProperty()).isAscending();
                if (ascendingBelow ? height <= ASCENDING_SEAT_HEIGHT : height <= 0.45D) {
                    return below;
                }
            } else {
                return below;
            }
        }
        // Might be under a pier, look up a couple blocks.
        if (climbFloat) {
            for (int dy = 1; dy <= 2; dy++) {
                BlockPos above = pos.above(dy);
                if (BaseRailBlock.isRail(cart.level().getBlockState(above))) {
                    return above;
                }
            }
        }
        return null;
    }

    /**
     * Higher ascending beyond this flat. Don't call findRailPos from here.
     */
    static boolean hasHigherClimbTarget(AbstractMinecart cart, BlockPos flatPos) {
        var level = cart.level();
        if (BaseRailBlock.isRail(level.getBlockState(flatPos.above()))) {
            return true;
        }
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() <= MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            return false;
        }
        int dx;
        int dz;
        if (Math.abs(motion.x) >= Math.abs(motion.z)) {
            dx = motion.x > 0.0D ? 1 : -1;
            dz = 0;
        } else {
            dx = 0;
            dz = motion.z > 0.0D ? 1 : -1;
        }
        for (int step = 1; step <= 4; step++) {
            // dy from 1, same-level ascending later isn't a "higher" target.
            for (int dy = 1; dy <= 3; dy++) {
                BlockState state = level.getBlockState(flatPos.offset(dx * step, dy, dz * step));
                if (state.getBlock() instanceof BaseRailBlock rail
                        && state.getValue(rail.getShapeProperty()).isAscending()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFlatStraightRail(BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        return !shape.isAscending() && !MinecartRailMotionHelper.isCornerRail(shape);
    }

    public static boolean isMidairAboveRail(AbstractMinecart cart, BlockPos railPos, boolean alreadyLeftRails) {
        if (!MinecartFloatStateHelper.isFloating(cart)) {
            return false;
        }
        if (isGhostSlopeBelow(cart, railPos, cart.level().getBlockState(railPos))) {
            return true;
        }
        double height = cart.getY() - railPos.getY();
        // Still under the rail.
        if (height < 0.0D) {
            return alreadyLeftRails || MinecartFloatStateHelper.getIncline(cart) != 0 || MinecartFloatStateHelper.isClimbMemory(cart);
        }
        BlockState railState = cart.level().getBlockState(railPos);
        boolean ascendingUnder = railState.getBlock() instanceof BaseRailBlock rail
                && railState.getValue(rail.getShapeProperty()).isAscending();
        // High above underfoot rail, treat as midair even if offRail hasn't synced.
        boolean slopeFloat = MinecartFloatStateHelper.getIncline(cart) != 0 || MinecartFloatStateHelper.isClimbMemory(cart) || MinecartFloatRotationHelper.poseHasSlopePitch(cart);
        if (!alreadyLeftRails && !slopeFloat) {
            return false;
        }
        if (ascendingUnder) {
            // On-rail climb uses the full ~0..1 seat curve; only treat as midair above the crest.
            return height > ASCENDING_SEAT_HEIGHT;
        }
        return !isSettledOnRail(cart, railPos);
    }

    public static boolean isCartOnRail(AbstractMinecart cart) {
        int i = Mth.floor(cart.getX());
        int j = Mth.floor(cart.getY());
        int k = Mth.floor(cart.getZ());
        BlockState blockState = cart.level().getBlockState(new BlockPos(i, j, k));
        if (BaseRailBlock.isRail(blockState)) {
            return true;
        }
        return BaseRailBlock.isRail(cart.level().getBlockState(new BlockPos(i, j - 1, k)));
    }
}
