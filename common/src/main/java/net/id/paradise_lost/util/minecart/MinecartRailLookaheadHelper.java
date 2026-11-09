package net.id.paradise_lost.util.minecart;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Looks along the cart's travel for rails, landings, stairs, walls, and ceilings.
 */
public final class MinecartRailLookaheadHelper {
    private MinecartRailLookaheadHelper() {
    }

    @Nullable
    static BlockPos findUnsettledFlatClimbTarget(AbstractMinecart cart) {
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos != null) {
            BlockState state = cart.level().getBlockState(railPos);
            if (state.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = state.getValue(rail.getShapeProperty());
                if (!shape.isAscending() && !MinecartRailMotionHelper.isCornerRail(shape) && !MinecartRailLocatorHelper.isSettledOnRail(cart, railPos)) {
                    return railPos;
                }
            }
        }
        BlockPos ahead = findContinuingRailAhead(cart, false);
        if (ahead == null) {
            return null;
        }
        BlockState state = cart.level().getBlockState(ahead);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return null;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        if (shape.isAscending() || MinecartRailMotionHelper.isCornerRail(shape)) {
            return null;
        }
        if (MinecartRailLocatorHelper.isSettledOnRail(cart, ahead)) {
            return null;
        }
        // Must be at/above the cart, a floor flat below is not an ascend-flat target.
        if (ahead.getY() + 1 < cart.blockPosition().getY()) {
            return null;
        }
        return ahead;
    }

    @Nullable
    private static BlockPos findUnsettledFlatLandingAhead(AbstractMinecart cart) {
        BlockPos ahead = findContinuingRailAhead(cart, false);
        if (ahead == null) {
            return null;
        }
        BlockState state = cart.level().getBlockState(ahead);
        if (!(state.getBlock() instanceof BaseRailBlock rail)
                || state.getValue(rail.getShapeProperty()).isAscending()
                || MinecartRailMotionHelper.isCornerRail(state.getValue(rail.getShapeProperty()))) {
            return null;
        }
        if (MinecartRailLocatorHelper.isSettledOnRail(cart, ahead)) {
            return null;
        }
        return ahead;
    }

    /**
     * Unseated straight flat within a block of the cart: the landing we are climbing onto, whether we are still under
     * the pier or already clipping it from above.
     */
    static boolean hasNearUnsettledFlatLanding(AbstractMinecart cart) {
        BlockPos ahead = findUnsettledFlatLandingAhead(cart);
        if (ahead == null) {
            return false;
        }
        double height = cart.getY() - ahead.getY();
        return height > -1.0D && height <= 1.0D;
    }

    /**
     * Still under the pier of a flat landing. Noclip belongs here; clipping the rail from above must collide so we
     * seat.
     */
    static boolean isBelowContinuingFlatLanding(AbstractMinecart cart) {
        BlockPos ahead = findUnsettledFlatLandingAhead(cart);
        return ahead != null && cart.getY() < ahead.getY() + 0.0625D;
    }

    /**
     * Solid in front of the cart at its own height that it cannot get over or past.
     * <ul>
     *     <li>Two blocks high or more: always a wall. Rails or stairs seen beyond it are no way forward.</li>
     *     <li>One block high: may be the tread of a stair path, so a stair or a rail still leading on (or a landing
     *     the climb is heading for) means it is not a wall. A diving cart cannot step up a tread, so for it the stair
     *     path is the only exception.</li>
     * </ul>
     */
    static boolean isWallAhead(AbstractMinecart cart, Vec3 moving) {
        double reach = cart.getBbWidth() / 2.0D + 0.1D;
        double dx = 0.0D;
        double dz = 0.0D;
        if (Math.abs(moving.x) >= Math.abs(moving.z)) {
            dx = Math.signum(moving.x) * reach;
        } else {
            dz = Math.signum(moving.z) * reach;
        }
        BlockPos ahead = BlockPos.containing(cart.getX() + dx, cart.getY() + cart.getBbHeight() / 2.0D, cart.getZ() + dz);
        if (!isBlockingSolid(cart.level(), ahead)) {
            BlockPos top = BlockPos.containing(cart.getX() + dx, cart.getY() + cart.getBbHeight() - 0.05D, cart.getZ() + dz);
            return !top.equals(ahead) && isBlockingSolid(cart.level(), top) && !hasContinuingRailAhead(cart);
        }
        if (isBlockingSolid(cart.level(), ahead.above())) {
            return true;
        }
        if (hasContinuingSlopeSolidAhead(cart)) {
            return false;
        }
        return MinecartFloatStateHelper.getIncline(cart) < 0
                || (!hasContinuingRailAhead(cart) && !hasNearUnsettledFlatLanding(cart)
                && !isBelowContinuingFlatLanding(cart));
    }

    /**
     * Any rail continues ahead (slope, flat landing, or short gap).
     */
    public static boolean hasContinuingRailAhead(AbstractMinecart cart) {
        return findContinuingRailAhead(cart, false) != null;
    }

    /**
     * Ascending rail continues ahead. Flats don't count, those need a real takeoff + seat.
     */
    public static boolean hasContinuingAscendingRailAhead(AbstractMinecart cart) {
        return findContinuingRailAhead(cart, true) != null;
    }

    @Nullable
    static BlockPos findContinuingRailAhead(AbstractMinecart cart, boolean ascendingOnly) {
        BlockPos underfoot = MinecartRailLocatorHelper.findRailPos(cart);
        if (underfoot != null) {
            BlockState underState = cart.level().getBlockState(underfoot);
            if (underState.getBlock() instanceof BaseRailBlock underRail) {
                RailShape underShape = underState.getValue(underRail.getShapeProperty());
                // Underfoot ascending counts. Flat underfoot isn't "ascending ahead".
                if (!ascendingOnly || underShape.isAscending()) {
                    return underfoot;
                }
            } else if (!ascendingOnly) {
                return underfoot;
            }
        }
        int dx = 0;
        int dz = 0;
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            if (Math.abs(motion.x) >= Math.abs(motion.z)) {
                dx = motion.x > 0.0D ? 1 : -1;
            } else {
                dz = motion.z > 0.0D ? 1 : -1;
            }
        } else {
            // Float yaw is 0=east, not look-yaw (Direction.fromYRot would face the wrong way).
            float railYaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                railYaw = pose.paradiseLost$getRailRenderYaw();
            }
            Vec3 along = MinecartFloatPoseHelper.railYawDirection(railYaw);
            if (along.lengthSqr() > 1.0E-8D) {
                if (Math.abs(along.x) >= Math.abs(along.z)) {
                    dx = along.x > 0.0D ? 1 : -1;
                } else {
                    dz = along.z > 0.0D ? 1 : -1;
                }
            } else {
                RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
                if (shape != null) {
                    Pair<Vec3i, Vec3i> exits = MinecartRailMotionHelper.RAIL_EXITS.get(shape);
                    if (exits != null) {
                        Vec3i upper = exits.getFirst().getY() >= exits.getSecond().getY()
                                ? exits.getFirst() : exits.getSecond();
                        Vec3i lower = exits.getFirst().getY() < exits.getSecond().getY()
                                ? exits.getFirst() : exits.getSecond();
                        Vec3i pick = MinecartFloatStateHelper.getIncline(cart) < 0 ? lower : upper;
                        dx = Integer.signum(pick.getX());
                        dz = Integer.signum(pick.getZ());
                    }
                }
            }
        }
        if (dx == 0 && dz == 0) {
            return null;
        }
        BlockPos base = cart.blockPosition();
        var level = cart.level();
        // Look far enough for stair gaps; a wall on the path still stops the search.
        RailShape currentShape = MinecartFloatStateHelper.getRailShape(cart);
        boolean slopePath = MinecartFloatStateHelper.getIncline(cart) != 0 || (currentShape != null && currentShape.isAscending());
        boolean ascending = MinecartFloatStateHelper.getIncline(cart) > 0;
        int maxStep = slopePath ? 8 : 3;
        int maxDyUp = slopePath ? 4 : 1;
        int maxDyDown = maxDropDepth(MinecartFloatStateHelper.getIncline(cart), slopePath);
        for (int step = 1; step <= maxStep; step++) {
            if (isFullWallOnPath(cart, dx, dz, step)) {
                break;
            }
            for (int dy = -maxDyDown; dy <= maxDyUp; dy++) {
                BlockPos ahead = base.offset(dx * step, dy, dz * step);
                BlockState state = level.getBlockState(ahead);
                if (!BaseRailBlock.isRail(state)) {
                    continue;
                }
                if (ascending && isDeepFlatBelowClimb(cart, ahead, state)) {
                    continue;
                }
                if (!countsAsContinuingRail(cart, ahead, state, step, dx, dz)) {
                    continue;
                }
                if (!ascendingOnly) {
                    return ahead;
                }
                if (state.getBlock() instanceof BaseRailBlock rail
                        && state.getValue(rail.getShapeProperty()).isAscending()) {
                    return ahead;
                }
            }
        }
        for (int dy = -maxDyDown; dy <= maxDyUp; dy++) {
            if (dy == 0) {
                continue;
            }
            BlockPos ahead = base.offset(0, dy, 0);
            BlockState state = level.getBlockState(ahead);
            if (!BaseRailBlock.isRail(state)) {
                continue;
            }
            if (ascending && isDeepFlatBelowClimb(cart, ahead, state)) {
                continue;
            }
            if (!countsAsContinuingRail(cart, ahead, state, Math.abs(dy), 0, 0)) {
                continue;
            }
            if (!ascendingOnly) {
                return ahead;
            }
            if (state.getBlock() instanceof BaseRailBlock rail
                    && state.getValue(rail.getShapeProperty()).isAscending()) {
                return ahead;
            }
        }
        return null;
    }

    private static int maxDropDepth(int incline, boolean slopePath) {
        if (incline < 0) {
            return 8;
        }
        if (incline == 0 && slopePath) {
            return 4;
        }
        return 1;
    }

    /**
     * Ahead rail only counts if we can actually ride it. A slope buried in the ground or ceiling, or one we'd have
     * to noclip through a solid to reach, isn't a continuation.
     */
    private static boolean countsAsContinuingRail(AbstractMinecart cart, BlockPos railPos, BlockState state,
                                                  int step, int dx, int dz) {
        if (buriedSlopeTarget(cart.level(), railPos, state, dx, dz)) {
            return false;
        }
        // Level cart. A slope in the block below is not the rail it's on.
        if (state.getBlock() instanceof BaseRailBlock railBlock) {
            RailShape shape = state.getValue(railBlock.getShapeProperty());
            if (shape.isAscending()
                    && railPos.getY() < cart.blockPosition().getY()
                    && Math.abs(MinecartRailLocatorHelper.approachPitch(cart)) < MinecartRailLocatorHelper.LEVEL_PITCH) {
                BlockPos here = cart.blockPosition();
                BlockState hereState = cart.level().getBlockState(here);
                boolean joinedDown = hereState.getBlock() instanceof BaseRailBlock hereRail
                        && MinecartRailMotionHelper.headingDownSlope(shape, cart.getDeltaMovement())
                        && MinecartRailMotionHelper.railsConnect(
                        here, hereState.getValue(hereRail.getShapeProperty()), railPos, shape);
                if (!joinedDown) {
                    return false;
                }
            }
        }
        // Climbs noclip stair piers and the stone under a landing, so the straight line hits those on purpose.
        // Only a descent treats ground in the way as unreachable.
        if (MinecartFloatStateHelper.getIncline(cart) < 0 && !pathToRailOpen(cart, railPos, step, dx, dz)) {
            return false;
        }
        return true;
    }

    /**
     * Downhill end is solid (runs into the ground), or the block above an uphill rail is solid (ceiling).
     * A real stair has the next rail on that exit, so that one isn't buried.
     */
    private static boolean buriedSlopeTarget(Level level, BlockPos railPos, BlockState state, int dx, int dz) {
        if (!(state.getBlock() instanceof BaseRailBlock rail) || !state.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        Pair<Vec3i, Vec3i> exits = MinecartRailMotionHelper.RAIL_EXITS.get(state.getValue(rail.getShapeProperty()));
        if (exits == null) {
            return false;
        }
        Vec3i first = exits.getFirst();
        Vec3i second = exits.getSecond();
        Vec3i low = first.getY() <= second.getY() ? first : second;
        Vec3i high = first.getY() >= second.getY() ? first : second;
        if (sameHorizontal(high, dx, dz)) {
            BlockPos into = railPos.offset(high);
            // Next stair step is the rail above the high side. Solid there, and no rail, is a ceiling.
            if (BaseRailBlock.isRail(level.getBlockState(into)) || BaseRailBlock.isRail(level.getBlockState(into.above()))) {
                return false;
            }
            return isBlockingSolid(level, into) || isBlockingSolid(level, railPos.above());
        }
        if (!sameHorizontal(low, dx, dz)) {
            return false;
        }
        BlockPos into = railPos.offset(low);
        return !BaseRailBlock.isRail(level.getBlockState(into)) && isBlockingSolid(level, into);
    }

    private static boolean sameHorizontal(Vec3i exit, int dx, int dz) {
        return (dx != 0 || dz != 0) && Integer.signum(exit.getX()) == dx && Integer.signum(exit.getZ()) == dz;
    }

    /**
     * Solid on the straight line to the rail. Stair treads sit above their piers, so a real staircase still counts.
     */
    private static boolean pathToRailOpen(AbstractMinecart cart, BlockPos railPos, int step, int dx, int dz) {
        if (step <= 1) {
            return columnToRailOpen(cart, railPos);
        }
        Level level = cart.level();
        double y0 = cart.getY() + 0.2D;
        double y1 = railPos.getY() + 0.5D;
        for (int s = 1; s < step; s++) {
            double y = y0 + (y1 - y0) * (s / (double) step);
            double x = dx == 0 && dz == 0 ? railPos.getX() + 0.5D : cart.getX() + dx * s;
            double z = dx == 0 && dz == 0 ? railPos.getZ() + 0.5D : cart.getZ() + dz * s;
            if (isBlockingSolid(level, BlockPos.containing(x, y, z))) {
                return false;
            }
        }
        return columnToRailOpen(cart, railPos);
    }

    private static boolean columnToRailOpen(AbstractMinecart cart, BlockPos railPos) {
        int from = Math.min(cart.blockPosition().getY(), railPos.getY()) + 1;
        int to = Math.max(cart.blockPosition().getY(), railPos.getY()) - 1;
        for (int y = from; y <= to; y++) {
            if (isBlockingSolid(cart.level(), new BlockPos(railPos.getX(), y, railPos.getZ()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Low end is about to enter a block that isn't the next rail. Vanilla sets the position straight in.
     * Same for a snap onto a rail that doesn't share an end, when a full block sits under that rail.
     */
    public static boolean shouldRefuseTrackPhase(AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (MinecartFloatStateHelper.getIncline(cart) >= 0) {
            return false;
        }
        Level level = cart.level();
        BlockPos current = MinecartRailLocatorHelper.findRailPos(cart);
        if (current != null && !current.equals(pos)
                && !MinecartRailMotionHelper.railsConnect(level, current, pos)
                && isFullCube(level, pos.below())) {
            return true;
        }
        if (!(state.getBlock() instanceof BaseRailBlock rail) || !state.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return descendingIntoFloor(cart);
    }

    /**
     * Descent has run into a solid with no rail through it. Sit the cart back on the face and stop.
     * A stair tread with a rail on top is the track, so that one stays.
     * Lifting onto the top is what bounced carts over the block.
     */
    public static boolean stopDeadEndDescent(AbstractMinecart cart) {
        if (!headingDownIntoDeadEnd(cart)) {
            return false;
        }
        // A clipped corner is a stair or a gap pier. Only a center buried in the block is a dead end.
        if (!ejectCenterFromDeadEnd(cart)) {
            return false;
        }
        cart.setDeltaMovement(0.0D, 0.0D, 0.0D);
        return true;
    }

    private static boolean ejectCenterFromDeadEnd(AbstractMinecart cart) {
        Level level = cart.level();
        double x = cart.getX();
        double z = cart.getZ();
        double half = cart.getBbWidth() * 0.5D;
        Vec3 motion = cart.getDeltaMovement();
        int minY = (int) Math.floor(cart.getY() + 0.05D);
        int maxY = (int) Math.floor(cart.getY() + cart.getBbHeight() * 0.5D);
        for (int by = minY; by <= maxY; by++) {
            BlockPos solid = BlockPos.containing(x, by, z);
            if (!isFullCube(level, solid) || solidContinuesTrack(cart, solid)) {
                continue;
            }
            double mx = motion.x;
            double mz = motion.z;
            if (Math.abs(mx) >= Math.abs(mz) && Math.abs(mx) > 1.0E-4D) {
                x = mx > 0.0D ? solid.getX() - half - 0.01D : solid.getX() + 1.0D + half + 0.01D;
            } else if (Math.abs(mz) > 1.0E-4D) {
                z = mz > 0.0D ? solid.getZ() - half - 0.01D : solid.getZ() + 1.0D + half + 0.01D;
            } else if ((x - solid.getX()) <= (solid.getX() + 1.0D - x)) {
                x = solid.getX() - half - 0.01D;
            } else {
                x = solid.getX() + 1.0D + half + 0.01D;
            }
            cart.setPos(x, cart.getY(), z);
            return true;
        }
        return false;
    }

    /**
     * Next cell along the move is a full block that does not continue the rail. Exit-grace noclip
     * was carrying a leveled descent through that block (#988).
     */
    public static boolean deadEndCubeAhead(AbstractMinecart cart) {
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() < 1.0E-8D) {
            return false;
        }
        Level level = cart.level();
        double x = cart.getX() + motion.x;
        double z = cart.getZ() + motion.z;
        int minY = (int) Math.floor(cart.getY() + 0.05D);
        int maxY = (int) Math.floor(cart.getY() + cart.getBbHeight() * 0.5D);
        int bx = (int) Math.floor(x);
        int bz = (int) Math.floor(z);
        for (int by = minY; by <= maxY; by++) {
            BlockPos solid = new BlockPos(bx, by, bz);
            if (isFullCube(level, solid) && !solidContinuesTrack(cart, solid)) {
                return true;
            }
        }
        return false;
    }

    private static boolean headingDownIntoDeadEnd(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.getIncline(cart) < 0) {
            return true;
        }
        BlockPos railPos = MinecartRailLocatorHelper.findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState state = cart.level().getBlockState(railPos);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        return shape.isAscending() && MinecartRailMotionHelper.headingDownSlope(shape, cart.getDeltaMovement());
    }

    /**
     * Cart is already overlapping the block its descent runs into. Sit it back on the face.
     * Returns whether it had to move the cart.
     */
    public static boolean pullBackFromFloor(AbstractMinecart cart) {
        Level level = cart.level();
        AABB box = cart.getBoundingBox();
        double half = cart.getBbWidth() * 0.5D;
        double x = cart.getX();
        double z = cart.getZ();
        Vec3 motion = cart.getDeltaMovement();
        double mx = motion.x;
        double mz = motion.z;
        boolean moved = false;
        int minX = (int) Math.floor(box.minX + 1.0E-4D);
        int maxX = (int) Math.floor(box.maxX - 1.0E-4D);
        int minY = (int) Math.floor(cart.getY() + 0.05D);
        int maxY = (int) Math.floor(cart.getY() + cart.getBbHeight() * 0.5D);
        int minZ = (int) Math.floor(box.minZ + 1.0E-4D);
        int maxZ = (int) Math.floor(box.maxZ - 1.0E-4D);
        for (int by = minY; by <= maxY; by++) {
            for (int bx = minX; bx <= maxX; bx++) {
                for (int bz = minZ; bz <= maxZ; bz++) {
                    BlockPos solid = new BlockPos(bx, by, bz);
                    if (!isFullCube(level, solid) || solidContinuesTrack(cart, solid)) {
                        continue;
                    }
                    boolean insideX = x >= bx && x <= bx + 1.0D;
                    boolean insideZ = z >= bz && z <= bz + 1.0D;
                    if (insideX && insideZ) {
                        // Vanilla already put the center in the block. Step back out the way we came.
                        boolean alongX = Math.abs(mx) >= Math.abs(mz);
                        if (alongX && Math.abs(mx) > 1.0E-4D) {
                            x = mx > 0.0D ? bx - half - 0.01D : bx + 1.0D + half + 0.01D;
                            mx = 0.0D;
                        } else if (!alongX && Math.abs(mz) > 1.0E-4D) {
                            z = mz > 0.0D ? bz - half - 0.01D : bz + 1.0D + half + 0.01D;
                            mz = 0.0D;
                        } else if ((x - bx) <= (bx + 1.0D - x)) {
                            x = bx - half - 0.01D;
                        } else {
                            x = bx + 1.0D + half + 0.01D;
                        }
                        moved = true;
                        continue;
                    }
                    // Only the axis the center hasn't entered. A cart aimed at the face is centered on the other one.
                    if (x < bx && box.maxX > bx) {
                        x = bx - half - 0.01D;
                        mx = Math.min(mx, 0.0D);
                        moved = true;
                    } else if (x > bx + 1.0D && box.minX < bx + 1.0D) {
                        x = bx + 1.0D + half + 0.01D;
                        mx = Math.max(mx, 0.0D);
                        moved = true;
                    }
                    if (z < bz && box.maxZ > bz) {
                        z = bz - half - 0.01D;
                        mz = Math.min(mz, 0.0D);
                        moved = true;
                    } else if (z > bz + 1.0D && box.minZ < bz + 1.0D) {
                        z = bz + 1.0D + half + 0.01D;
                        mz = Math.max(mz, 0.0D);
                        moved = true;
                    }
                }
            }
        }
        if (!moved) {
            return false;
        }
        cart.setPos(x, cart.getY(), z);
        cart.setDeltaMovement(mx, motion.y, mz);
        return true;
    }

    private static boolean solidContinuesTrack(AbstractMinecart cart, BlockPos solid) {
        BlockPos upper = solid.above();
        Level level = cart.level();
        BlockState upperState = level.getBlockState(upper);
        if (!(upperState.getBlock() instanceof BaseRailBlock upperRail)) {
            return false;
        }
        BlockPos current = MinecartRailLocatorHelper.findRailPos(cart);
        if (current == null) {
            return false;
        }
        return MinecartRailMotionHelper.railsConnect(level, current, upper);
    }

    /**
     * Next move would put us inside a floor. A tread with air above is a stair lip and doesn't count.
     * Ground under an open descent does, or noclip carries the cart through it.
     */
    static boolean descendingIntoFloor(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.getIncline(cart) >= 0) {
            return false;
        }
        Vec3 motion = cart.getDeltaMovement();
        BlockPos next = BlockPos.containing(cart.getX() + motion.x, cart.getY() + motion.y, cart.getZ() + motion.z);
        Level level = cart.level();
        if (!isBlockingSolid(level, next)) {
            return false;
        }
        BlockPos upper = next.above();
        // Rail on top of this block only counts when it actually continues the rail we're on.
        // A descent aimed at the underside is a floor.
        if (BaseRailBlock.isRail(level.getBlockState(upper))) {
            BlockPos current = MinecartRailLocatorHelper.findRailPos(cart);
            return current != null && !MinecartRailMotionHelper.railsConnect(level, current, upper);
        }
        BlockPos chest = BlockPos.containing(
                cart.getX() + motion.x, cart.getY() + cart.getBbHeight() * 0.5D, cart.getZ() + motion.z);
        if (!next.equals(chest) && !isBlockingSolid(level, chest)) {
            return true;
        }
        return isBlockingSolid(level, next.above());
    }

    /**
     * Flat more than a block below, not a climb target.
     */
    static boolean isDeepFlatBelowClimb(AbstractMinecart cart, BlockPos ahead, BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        if (state.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return cart.getY() - ahead.getY() > 1.0D;
    }

    /**
     * Keep diving on open-air descent while a slope/stair is ahead. Brief look-ahead misses shouldn't immediately level
     * us out.
     */
    public static boolean shouldKeepOpenAirDescent(AbstractMinecart cart) {
        return MinecartFloatStateHelper.getIncline(cart) < 0
                && (hasOpenAirSlopeTarget(cart) || ((MinecartFloatPoseAccess) cart).paradiseLost$getOpenAirClimbGrace() > 0);
    }

    /**
     * Open-air climb while incline is live and we still have a target: continuing rail/stair, a short grace after
     * takeoff (sky launch), or a ceiling that is about to be hit. Climb memory alone or far-away solids (trees) used to
     * keep one-block launches tilted forever.
     */
    public static boolean shouldKeepOpenAirClimb(AbstractMinecart cart) {
        if (MinecartFloatStateHelper.getIncline(cart) <= 0) {
            return false;
        }
        if (hasContinuingAscendingRailAhead(cart)
                || hasContinuingSlopeSolidAhead(cart)
                || hasNearUnsettledFlatLanding(cart)
                || isBelowContinuingFlatLanding(cart)) {
            return true;
        }
        if (((MinecartFloatPoseAccess) cart).paradiseLost$getOpenAirClimbGrace() > 0) {
            return true;
        }
        // After the grace only a close ceiling keeps the cart rising, not foliage 8 or more blocks up.
        return distanceToSolidAbove(cart, MinecartFlightHelper.IMMINENT_CEILING_BLOCKS) > 0;
    }

    /**
     * Number of blocks up to the first collidable block above, or 0 if there is none within maxBlocks.
     */
    static int distanceToSolidAbove(AbstractMinecart cart, int maxBlocks) {
        BlockPos.MutableBlockPos cursor = cart.blockPosition().mutable();
        for (int i = 1; i <= maxBlocks; i++) {
            cursor.move(0, 1, 0);
            BlockState state = cart.level().getBlockState(cursor);
            if (!state.getCollisionShape(cart.level(), cursor).isEmpty()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Rail, solid stair, or remembered slope still worth aiming at.
     */
    static boolean hasOpenAirSlopeTarget(AbstractMinecart cart) {
        if (hasContinuingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart)) {
            return true;
        }
        if (MinecartFloatStateHelper.getIncline(cart) >= 0) {
            return false;
        }
        RailShape shape = MinecartFloatStateHelper.getRailShape(cart);
        return shape != null && shape.isAscending();
    }

    /**
     * Walking-stair diagonal ahead (two piers at different Y). Flat floors don't count.
     */
    public static boolean hasContinuingSlopeSolidAhead(AbstractMinecart cart) {
        int incline = MinecartFloatStateHelper.getIncline(cart);
        if (incline == 0) {
            return false;
        }
        int dx = 0;
        int dz = 0;
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() > MinecartRailMotionHelper.MOTION_EPSILON * MinecartRailMotionHelper.MOTION_EPSILON) {
            if (Math.abs(motion.x) >= Math.abs(motion.z)) {
                dx = motion.x > 0.0D ? 1 : -1;
            } else {
                dz = motion.z > 0.0D ? 1 : -1;
            }
        } else {
            float railYaw = MinecartFloatStateHelper.getSyncedFloatYaw(cart);
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                railYaw = pose.paradiseLost$getRailRenderYaw();
            }
            Vec3 along = MinecartFloatPoseHelper.railYawDirection(railYaw);
            if (along.lengthSqr() <= 1.0E-8D) {
                return false;
            }
            // Climb looks up-rail, descend looks down-rail.
            if (incline < 0) {
                along = along.scale(-1.0D);
            }
            if (Math.abs(along.x) >= Math.abs(along.z)) {
                dx = along.x > 0.0D ? 1 : -1;
            } else {
                dz = along.z > 0.0D ? 1 : -1;
            }
        }
        if (dx == 0 && dz == 0) {
            return false;
        }
        Level level = cart.level();
        int firstPierY = Integer.MIN_VALUE;
        int distinctY = 0;
        int sign = incline > 0 ? 1 : -1;
        // Use feet, carts often sit inside the pier cell.
        // Pier sits one below the path cell ahead.
        // +/-1 Y covers skimming a bit high/low off a lip.
        // Do not skip "head-height" piers: on a climb the next tread is often at headY, and
        // isWalkableStairPier already rejects face-walls (solid with solid above).
        double feetY = cart.getY();
        for (int step = 1; step <= 6; step++) {
            if (isFullWallOnPath(cart, dx, dz, step)) {
                break;
            }
            boolean found = false;
            int foundY = 0;
            for (int yOff = -1; yOff <= 1; yOff++) {
                BlockPos pier = BlockPos.containing(
                        cart.getX() + dx * step,
                        feetY + sign * step - 1.0D + yOff,
                        cart.getZ() + dz * step);
                if (!isWalkableStairPier(level, pier)) {
                    continue;
                }
                found = true;
                foundY = pier.getY();
                break;
            }
            if (!found) {
                continue;
            }
            if (firstPierY == Integer.MIN_VALUE) {
                firstPierY = foundY;
                distinctY = 1;
            } else if (foundY != firstPierY) {
                distinctY = 2;
                break;
            }
        }
        return distinctY >= 2;
    }

    /**
     * Solid underfoot with air above, a tread, not a wall. Pistons that shove the cart are not stairs.
     */
    private static boolean isWalkableStairPier(Level level, BlockPos pier) {
        if (!isBlockingSolid(level, pier)) {
            return false;
        }
        BlockState state = level.getBlockState(pier);
        if (state.getBlock() instanceof PistonBaseBlock
                || state.getBlock() instanceof PistonHeadBlock
                || state.getBlock() instanceof MovingPistonBlock) {
            return false;
        }
        return !isBlockingSolid(level, pier.above());
    }

    private static boolean isFullWallOnPath(AbstractMinecart cart, int dx, int dz, int step) {
        Level level = cart.level();
        double midY = cart.getY() + cart.getBbHeight() / 2.0D;
        for (int s = 1; s < step; s++) {
            BlockPos at = BlockPos.containing(cart.getX() + dx * s, midY, cart.getZ() + dz * s);
            if (!isBlockingSolid(level, at) || !isBlockingSolid(level, at.above())) {
                continue;
            }
            // A rail on top of this column is the landing we are climbing onto, not a wall in front of it.
            if (BaseRailBlock.isRail(level.getBlockState(at.above(2)))) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Chest is inside a full block, or the next move would put it there. Fences and stair treads don't count.
     */
    public static boolean wouldPhaseThroughSolid(AbstractMinecart cart) {
        if (cartInsideSolid(cart, cart.getX(), cart.getY(), cart.getZ())) {
            return true;
        }
        Vec3 motion = cart.getDeltaMovement();
        return cartInsideSolid(cart, cart.getX() + motion.x, cart.getY() + motion.y, cart.getZ() + motion.z);
    }

    /**
     * Sit the cart on top of the solid it's buried in. A couple of passes covers a short dive into the ground.
     */
    public static boolean liftOutOfSolid(AbstractMinecart cart) {
        boolean lifted = false;
        Level level = cart.level();
        for (int i = 0; i < 4; i++) {
            double chestY = cart.getY() + cart.getBbHeight() * 0.5D;
            BlockPos pos = BlockPos.containing(cart.getX(), chestY, cart.getZ());
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || BaseRailBlock.isRail(state)) {
                break;
            }
            VoxelShape shape = state.getCollisionShape(level, pos);
            if (shape.isEmpty()) {
                break;
            }
            double top = pos.getY() + shape.max(Direction.Axis.Y);
            if (cart.getY() >= top - 1.0E-4D) {
                break;
            }
            if (!pointInsideSolid(level, cart.getX(), chestY, cart.getZ())) {
                break;
            }
            cart.setPos(cart.getX(), top, cart.getZ());
            lifted = true;
        }
        if (lifted) {
            Vec3 motion = cart.getDeltaMovement();
            if (motion.y < 0.0D) {
                cart.setDeltaMovement(motion.x, 0.0D, motion.z);
            }
        }
        return lifted;
    }

    private static boolean cartInsideSolid(AbstractMinecart cart, double x, double y, double z) {
        return pointInsideSolid(cart.level(), x, y + cart.getBbHeight() * 0.5D, z);
    }

    private static boolean pointInsideSolid(Level level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || BaseRailBlock.isRail(state) || !state.isCollisionShapeFullBlock(level, pos)) {
            return false;
        }
        VoxelShape shape = state.getCollisionShape(level, pos);
        if (shape.isEmpty()) {
            return false;
        }
        double localY = y - pos.getY();
        if (localY >= shape.max(Direction.Axis.Y) - 0.05D) {
            return false;
        }
        double localX = x - pos.getX();
        double localZ = z - pos.getZ();
        AABB point = new AABB(localX, localY, localZ, localX, localY, localZ);
        for (AABB box : shape.toAabbs()) {
            if (box.intersects(point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFullCube(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() && !BaseRailBlock.isRail(state) && state.isCollisionShapeFullBlock(level, pos);
    }

    private static boolean isBlockingSolid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || BaseRailBlock.isRail(state)) {
            return false;
        }
        return !state.getCollisionShape(level, pos).isEmpty();
    }
}
