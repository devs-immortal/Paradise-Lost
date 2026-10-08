package net.id.paradise_lost.attachments;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public final class MinecartFloating {
    public static final boolean DEBUG_LOG = false;

    private static final double FLOAT_SECONDS = 4.1;
    private static final double MOTION_EPSILON = 0.05D;
    private static final double PARALLEL_DOT = 0.92D;
    private static final String LEGACY_NBT_KEY = "paradiseLostFloating";
    private static final String DEBUG_PREFIX = "[PL-MinecartFloat]";

    private static final ThreadLocal<Integer> POS_CAPTURE_BYPASS = ThreadLocal.withInitial(() -> 0);

    private static final Map<RailShape, Pair<Vec3i, Vec3i>> RAIL_EXITS = Map.ofEntries(
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

    private MinecartFloating() {
    }

    public static boolean isFloating(AbstractMinecart cart) {
        return getFloatTime(cart) > 0;
    }

    public static int getFloatTime(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOATING);
    }

    public static void setFloatTime(AbstractMinecart cart, int time) {
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOATING, Math.max(time, 0));
        if (time <= 0) {
            setIncline(cart, 0);
            setRailShape(cart, "");
            setOffRail(cart, false);
        }
    }

    public static boolean isOffRail(AbstractMinecart cart) {
        return Boolean.TRUE.equals(
                Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_OFF_RAIL));
    }

    public static void setOffRail(AbstractMinecart cart, boolean offRail) {
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_OFF_RAIL, offRail);
    }

    public static int getIncline(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE);
    }

    public static void setIncline(AbstractMinecart cart, int incline) {
        Services.ATTACHMENTS.setAttachedValue(
                cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE, Mth.clamp(incline, -1, 1));
    }

    public static String getRailShapeName(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_SHAPE);
    }

    public static void setRailShape(AbstractMinecart cart, RailShape shape) {
        setRailShape(cart, shape == null ? "" : shape.getSerializedName());
    }

    public static void setRailShape(AbstractMinecart cart, String shapeName) {
        Services.ATTACHMENTS.setAttachedValue(
                cart, CommonDataAttachments.MINE_CART_FLOAT_SHAPE, shapeName == null ? "" : shapeName);
    }

    public static RailShape getRailShape(AbstractMinecart cart) {
        String name = getRailShapeName(cart);
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return RailShape.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static void addFloating(AbstractMinecart cart) {
        setFloatTime(cart, getFloatTime(cart) + (int) (20 * FLOAT_SECONDS));
    }

    public static void stopFloating(AbstractMinecart cart) {
        setFloatTime(cart, 0);
    }

    public static void tick(AbstractMinecart cart) {
        setFloatTime(cart, getFloatTime(cart) - 1);
    }

    public static void updateInclineFromRail(AbstractMinecart cart, RailShape shape, Vec3 motion) {
        setRailShape(cart, shape);
        if (!shape.isAscending()) {
            setIncline(cart, 0);
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
            setIncline(cart, 1);
            return;
        }
        if (goingDown) {
            setIncline(cart, -1);
            return;
        }
        // Curve → slope transitions often lack a clear axis component (CW vs CCW). Pick by
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
        if (dotUpper > dotLower + 1.0E-3D) {
            setIncline(cart, 1);
        } else if (dotLower > dotUpper + 1.0E-3D) {
            setIncline(cart, -1);
        }
    }

    public static void captureRailState(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        BlockState state = cart.level().getBlockState(pos);
        BlockPos railPos = pos;
        if (!BaseRailBlock.isRail(state)) {
            railPos = pos.below();
            state = cart.level().getBlockState(railPos);
        }
        if (state.getBlock() instanceof BaseRailBlock rail) {
            RailShape shape = state.getValue(rail.getShapeProperty());
            int beforeIncline = getIncline(cart);
            String beforeShape = getRailShapeName(cart);
            updateInclineFromRail(cart, shape, cart.getDeltaMovement());
            debug(cart, "captureRail",
                    "railPos=" + railPos
                            + " block=" + blockId(state)
                            + " shape=" + shape
                            + " incline " + beforeIncline + "->" + getIncline(cart)
                            + " storedShape " + beforeShape + "->" + getRailShapeName(cart)
                            + " yAboveRail=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY()));
        } else {
            debug(cart, "captureRail", "no rail at " + pos + " / below");
        }
    }

    public static void captureRailRenderPose(AbstractMinecart cart) {
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
            Vec3 dir = negOff.subtract(posOff);
            if (dir.lengthSqr() < 1.0E-6D) {
                Vec3 motion = cart.getDeltaMovement();
                if (motion.horizontalDistanceSqr() < 1.0E-6D) {
                    return;
                }
                dir = new Vec3(motion.x, 0.0D, motion.z).normalize();
            } else {
                dir = dir.normalize();
            }
            float yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI));
            // Flat curves can sample a bogus dy from neighboring slopes — only keep pitch on ascents.
            float pitch = 0.0F;
            BlockPos railPos = findRailPos(cart);
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail
                        && railState.getValue(rail.getShapeProperty()).isAscending()) {
                    // Same factor MinecartRenderer uses: atan(dy) * 73 on a normalized offs delta.
                    pitch = (float) (Math.atan(dir.y) * 73.0D);
                }
            }
            ((MinecartFloatPoseAccess) cart).paradiseLost$setRailRenderPose(yaw, pitch);
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    public static void prepareTakeoff(AbstractMinecart cart) {
        captureRailRenderPose(cart);
        applyFloatingRotation(cart);
    }

    public static void prepareReconnect(AbstractMinecart cart) {
        captureRailState(cart);
        setOffRail(cart, false);
        captureRailRenderPose(cart);
        RailShape landed = getRailShape(cart);
        if (getIncline(cart) == 0 || landed == null || !landed.isAscending()) {
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, getSyncedFloatYaw(cart), 0.0F);
        } else {
            applyFloatingRotation(cart);
        }
    }

    public static boolean isParallelToRail(AbstractMinecart cart, RailShape shape) {
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return false;
        }
        Vec3 approach = new Vec3(cart.getDeltaMovement().x, 0.0D, cart.getDeltaMovement().z);
        if (approach.lengthSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            approach = Vec3.directionFromRotation(cart.getYRot(), 0.0F).multiply(1.0D, 0.0D, 1.0D);
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


    public static void applyFloatingRotation(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float yaw;
        float pitch = 0.0F;
        if (pose.paradiseLost$hasRailRenderPose()) {
            yaw = pose.paradiseLost$getRailRenderYaw();
            if (shouldKeepSlopePitch(cart)) {
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
        } else {
            yaw = getSyncedFloatYaw(cart);
            pitch = getSyncedFloatPitch(cart);
        }
        setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    private static boolean shouldKeepSlopePitch(AbstractMinecart cart) {
        if (getIncline(cart) != 0) {
            return true;
        }
        RailShape shape = getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return true;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        return railState.getBlock() instanceof BaseRailBlock rail
                && railState.getValue(rail.getShapeProperty()).isAscending();
    }


    public static void applySyncedFloatingRotation(AbstractMinecart cart) {
        if (!isFloating(cart)) {
            return;
        }
        float yaw = getSyncedFloatYaw(cart);
        float pitch = getSyncedFloatPitch(cart);
        snapEntityRotation(cart, yaw, pitch);
        ((MinecartFloatPoseAccess) cart).paradiseLost$setRailRenderPose(yaw, pitch);
    }

    public static void setSyncedFloatPose(AbstractMinecart cart, float yaw, float pitch) {
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_YAW, yaw);
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_PITCH, pitch);
    }

    public static float getSyncedFloatYaw(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_YAW);
    }

    public static float getSyncedFloatPitch(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_PITCH);
    }

    private static void snapEntityRotation(AbstractMinecart cart, float yaw, float pitch) {
        cart.setYRot(yaw);
        cart.yRotO = yaw;
        cart.setXRot(pitch);
        cart.xRotO = pitch;
    }

    public static boolean shouldSkipRailRenderSnap(AbstractMinecart cart) {
        if (POS_CAPTURE_BYPASS.get() > 0) {
            return false;
        }
        return isFloating(cart) && isOffRail(cart);
    }

    public static void maintainSlopeEntityPitch(AbstractMinecart cart) {
        if (getIncline(cart) == 0) {
            return;
        }
        BlockPos railPos = findRailPos(cart);
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
        float pitch = pose.paradiseLost$getRailRenderPitch();
        cart.setXRot(pitch);
        cart.xRotO = pitch;
        setSyncedFloatPose(cart, getSyncedFloatYaw(cart), pitch);
    }

    public static void clearFloatingPitch(AbstractMinecart cart) {
        cart.setXRot(0.0F);
        cart.xRotO = 0.0F;
    }

    private static boolean isCornerRail(RailShape shape) {
        return shape == RailShape.NORTH_EAST
                || shape == RailShape.NORTH_WEST
                || shape == RailShape.SOUTH_EAST
                || shape == RailShape.SOUTH_WEST;
    }

    public static boolean isSettledOnRail(AbstractMinecart cart, BlockPos railPos) {
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        double height = cart.getY() - railPos.getY();
        if (railState.getValue(rail.getShapeProperty()).isAscending()) {
            return height <= 0.45D;
        }
        return height <= 0.16D;
    }

    @Nullable
    public static BlockPos findRailPos(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        if (BaseRailBlock.isRail(cart.level().getBlockState(pos))) {
            return pos;
        }
        BlockPos below = pos.below();
        if (BaseRailBlock.isRail(cart.level().getBlockState(below))) {
            return below;
        }
        return null;
    }

    public static boolean isMidairAboveRail(AbstractMinecart cart, BlockPos railPos, boolean alreadyLeftRails) {
        if (!alreadyLeftRails || !isFloating(cart)) {
            return false;
        }
        return !isSettledOnRail(cart, railPos);
    }

    public static void nudgeVelocityToRail(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (isCornerRail(shape)) {
            setRailShape(cart, shape);
            debug(cart, "nudge", "skip corner shape=" + shape);
            return;
        }
        if (isParallelToRail(cart, shape)) {
            setRailShape(cart, shape);
            Vec3 motion = cart.getDeltaMovement();
            if (shape.isAscending()) {
                updateInclineFromRail(cart, shape, motion);
                int incline = getIncline(cart);
                if (incline != 0) {
                    cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
                }
            } else {
                updateInclineFromRail(cart, shape, motion);
            }
            debug(cart, "nudge", "parallel skip XZ shape=" + shape + " incline=" + getIncline(cart));
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
            approach = Vec3.directionFromRotation(cart.getYRot(), 0.0F).multiply(1.0D, 0.0D, 1.0D);
        }
        approach = approach.normalize();

        double dotA = horizontalDot(approach, exitA);
        double dotB = horizontalDot(approach, exitB);
        Vec3 along;
        if (Math.max(dotA, dotB) >= 0.35D) {
            along = dotA >= dotB ? exitA : exitB;
        } else {
            Vec3 right = new Vec3(approach.z, 0.0D, -approach.x);
            along = horizontalDot(right, exitA) >= horizontalDot(right, exitB) ? exitA : exitB;
        }

        setRailShape(cart, shape);
        double speed = Math.max(motion.horizontalDistance(), MOTION_EPSILON);
        Vec3 newMotion = along.scale(speed);
        if (shape.isAscending()) {
            updateInclineFromRail(cart, shape, newMotion);
            int incline = getIncline(cart);
            if (incline != 0) {
                newMotion = new Vec3(newMotion.x, incline * newMotion.horizontalDistance(), newMotion.z);
            }
        } else {
            updateInclineFromRail(cart, shape, newMotion);
        }
        cart.setDeltaMovement(newMotion);
        debug(cart, "nudge",
                "block=" + blockId(railState)
                        + " shape=" + shape
                        + " approach=" + fmt(approach)
                        + " along=" + fmt(along)
                        + " dots=" + String.format(Locale.ROOT, "%.2f/%.2f", dotA, dotB)
                        + " motion " + fmt(motion) + "->" + fmt(newMotion)
                        + " incline=" + getIncline(cart));
    }

    public static void debugTick(AbstractMinecart cart, boolean wasOffRail, boolean midair, String phase) {
        if (!DEBUG_LOG || !isFloating(cart)) {
            return;
        }
        BlockPos pos = cart.blockPosition();
        BlockState at = cart.level().getBlockState(pos);
        BlockState below = cart.level().getBlockState(pos.below());
        BlockPos railPos = BaseRailBlock.isRail(at) ? pos : (BaseRailBlock.isRail(below) ? pos.below() : null);
        String railInfo = "none";
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                railInfo = blockId(railState) + "@" + railPos + " shape=" + railState.getValue(rail.getShapeProperty())
                        + " yAbove=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY());
            }
        }
        debug(cart, "tick",
                "phase=" + phase
                        + " side=" + (cart.level().isClientSide() ? "C" : "S")
                        + " onRail=" + isCartOnRail(cart)
                        + " wasOff=" + wasOffRail
                        + " offRailAtt=" + isOffRail(cart)
                        + " midair=" + midair
                        + " floatT=" + getFloatTime(cart)
                        + " incline=" + getIncline(cart)
                        + " storedShape=" + getRailShapeName(cart)
                        + " skipSnap=" + shouldSkipRailRenderSnap(cart)
                        + " yaw/pitch=" + String.format(Locale.ROOT, "%.1f/%.1f", cart.getYRot(), cart.getXRot())
                        + " railPose=" + formatRailPose(cart)
                        + " pos=" + fmt(cart.position())
                        + " motion=" + fmt(cart.getDeltaMovement())
                        + " rail=" + railInfo);
    }

    public static void debugEvent(AbstractMinecart cart, String event, String detail) {
        debug(cart, event, detail);
    }

    private static void debug(AbstractMinecart cart, String event, String detail) {
        if (!DEBUG_LOG) {
            return;
        }
        ModConstants.LOGGER.info("{} {} id={} {}", DEBUG_PREFIX, event, cart.getId(), detail);
    }

    private static String blockId(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
    }

    private static String fmt(Vec3 v) {
        return String.format(Locale.ROOT, "(%.3f,%.3f,%.3f)", v.x, v.y, v.z);
    }

    private static String formatRailPose(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return "none";
        }
        return String.format(Locale.ROOT, "%.1f/%.1f",
                pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch());
    }

    private static double horizontalDot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.z * b.z;
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

    public static void loadLegacyNbt(AbstractMinecart cart, CompoundTag tag) {
        if (!tag.contains(LEGACY_NBT_KEY, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag floating = tag.getCompound(LEGACY_NBT_KEY);
        int time = floating.getInt("floatTime");
        setFloatTime(cart, floating.getBoolean("floating") ? Math.max(time, 1) : time);
        setIncline(cart, floating.getInt("floatIncline"));
        if (floating.contains("floatShape", Tag.TAG_STRING)) {
            setRailShape(cart, floating.getString("floatShape"));
        }
        tag.remove(LEGACY_NBT_KEY);
    }
}
