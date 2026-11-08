package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.debug.DebugFlags;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Debug logging for floating minecarts.
 */
public final class MinecartFloatDebugHelper {
    private MinecartFloatDebugHelper() {
    }

    private static final String DEBUG_PREFIX = "[PL-MinecartFloat]";

    private static final Map<AbstractMinecart, OnRailSample> ON_RAIL = Collections.synchronizedMap(new WeakHashMap<>());

    public static void debugTick(AbstractMinecart cart, boolean wasOffRail, boolean midair, String phase) {
        if (!DebugFlags.MINECART_FLOAT || !MinecartFloatStateHelper.isFloating(cart)) {
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
                RailShape shape = railState.getValue(rail.getShapeProperty());
                railInfo = blockId(railState) + "@" + railPos + shape(shape)
                        + " yAbove=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY());
                if (MinecartRailLocatorHelper.isCartOnRail(cart)) {
                    noteOnRail(cart, railPos, shape);
                }
            }
        }
        debug(cart, "tick",
                "phase=" + phase
                        + " side=" + (cart.level().isClientSide() ? "C" : "S")
                        + " onRail=" + MinecartRailLocatorHelper.isCartOnRail(cart)
                        + " wasOff=" + wasOffRail
                        + " offRailAtt=" + MinecartFloatStateHelper.isOffRail(cart)
                        + " midair=" + midair
                        + " floatT=" + MinecartFloatStateHelper.getFloatTime(cart)
                        + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                        + " storedShape=" + MinecartFloatStateHelper.getRailShapeName(cart)
                        + " skipSnap=" + MinecartFloatRenderHelper.shouldSkipRailRenderSnap(cart)
                        + " yaw/pitch=" + String.format(Locale.ROOT, "%.1f/%.1f", cart.getYRot(), cart.getXRot())
                        + " railPose=" + formatRailPose(cart)
                        + " sync=" + String.format(Locale.ROOT, "%.1f/%.1f",
                        MinecartFloatStateHelper.getSyncedFloatYaw(cart), MinecartFloatStateHelper.getSyncedFloatPitch(cart))
                        + " pos=" + fmt(cart.position())
                        + motion(cart)
                        + " rail=" + railInfo);
    }

    static void debug(AbstractMinecart cart, String event, String detail) {
        if (!DebugFlags.MINECART_FLOAT) {
            return;
        }
        ModConstants.LOGGER.info("{} {} id={} {}", DEBUG_PREFIX, event, cart.getId(), detail);
    }

    static String blockId(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
    }

    static String fmt(Vec3 v) {
        return String.format(Locale.ROOT, "(%.3f,%.3f,%.3f)", v.x, v.y, v.z);
    }

    private static String shape(Object value) {
        return " shape=" + value;
    }

    private static String motion(Vec3 value) {
        return " motion=" + fmt(value);
    }

    private static String motion(AbstractMinecart cart) {
        return motion(cart.getDeltaMovement());
    }

    private static String incline(AbstractMinecart cart) {
        return " incline=" + MinecartFloatStateHelper.getIncline(cart);
    }

    private static String angles(float yaw, float pitch) {
        return String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch);
    }

    private static String one(float value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String thousandths(double value) {
        return String.format(Locale.ROOT, "%.3f", value);
    }

    public static void reconnectDefer(AbstractMinecart cart, MinecartFloatPoseAccess pose, Object ignored, Object keep) {
        debug(cart, "reconnect", "deferFlatClimb yaw/pitch="
                + angles(pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                + " ignored=" + ignored
                + " keep=" + keep
                + incline(cart));
    }

    public static void reconnectSlope(AbstractMinecart cart, MinecartFloatPoseAccess pose, Object railShape) {
        debug(cart, "reconnect", "slope yaw/pitch="
                + angles(pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                + shape(railShape)
                + incline(cart)
                + motion(cart));
    }

    public static void reconnectCorner(AbstractMinecart cart, float yaw, Object railShape, Object from) {
        debug(cart, "reconnect", "cornerFlat yaw=" + one(yaw)
                + shape(railShape)
                + " from=" + from
                + motion(cart));
    }

    public static void reconnectFlat(AbstractMinecart cart, float yaw) {
        debug(cart, "reconnect", "flat yaw=" + one(yaw)
                + shape(MinecartFloatStateHelper.getRailShapeName(cart))
                + motion(cart));
    }

    public static void flatAlign(AbstractMinecart cart, float yaw, Object railShape, double yAbove) {
        debug(cart, "flatAlign", "yaw=" + one(yaw)
                + " pitch=0"
                + shape(railShape)
                + " yAbove=" + thousandths(yAbove)
                + motion(cart));
    }

    public static void ascendPoseFlip(AbstractMinecart cart, Object priorShape, Object railShape,
                                      float priorYaw, float priorPitch, MinecartFloatPoseAccess pose) {
        debug(cart, "ascendAdapt", "poseFlipYaw " + priorShape + "->" + railShape
                + " " + angles(priorYaw, priorPitch)
                + "->" + angles(pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch()));
    }

    public static void ascendAdapt(AbstractMinecart cart, Object railShape, int before, int now, boolean restoreFlatPose,
                                   MinecartFloatPoseAccess pose, BlockPos railPos) {
        debug(cart, "ascendAdapt", "shape=" + railShape
                + " incline " + before + "->" + now
                + (restoreFlatPose ? " restoreFlatPose" : "")
                + motion(cart)
                + " railPose=" + angles(pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                + (railPos == null ? "" : " yAbove=" + thousandths(cart.getY() - railPos.getY())));
    }

    public static void flatAdapt(AbstractMinecart cart, float yaw, RailShape railShape, double yAbove, boolean nearSeat) {
        debug(cart, "flatAdapt", "pitch=0 yaw=" + one(yaw)
                + shape(railShape)
                + " parallel=" + MinecartRailMotionHelper.isParallelToRail(cart, railShape)
                + incline(cart)
                + " yAbove=" + thousandths(yAbove)
                + " nearSeat=" + nearSeat
                + motion(cart));
    }

    public static void ghostSlope(AbstractMinecart cart, BlockPos snapped) {
        debug(cart, "ghostSlope", "kept flight height over " + snapped);
    }

    public static void captureGhost(AbstractMinecart cart, BlockPos railPos) {
        debug(cart, "captureRail", "ghost slope at " + railPos);
    }

    public static void captureRail(AbstractMinecart cart, BlockPos railPos, BlockState state, RailShape railShape,
                                   int beforeIncline, String beforeShape) {
        debug(cart, "captureRail", "railPos=" + railPos
                + " block=" + blockId(state)
                + shape(railShape)
                + " incline " + beforeIncline + "->" + MinecartFloatStateHelper.getIncline(cart)
                + " storedShape " + beforeShape + "->" + MinecartFloatStateHelper.getRailShapeName(cart)
                + " yAboveRail=" + thousandths(cart.getY() - railPos.getY()));
    }

    public static void captureNone(AbstractMinecart cart, BlockPos pos) {
        debug(cart, "captureRail", "no rail at " + pos + " / below");
    }

    public static void slopeCapture(AbstractMinecart cart, RailShape railShape, float offsYaw, float offsPitch,
                                    float motionYaw, float yaw, float pitch, Vec3 dir, boolean usedMotionDir,
                                    boolean motionDisagrees, boolean unsettledForce, BlockPos railPos) {
        debug(cart, "slopeCapture", "shape=" + railShape
                + " offs=" + angles(offsYaw, offsPitch)
                + " motionYaw=" + one(motionYaw)
                + " final=" + angles(yaw, pitch)
                + " dir=" + fmt(dir)
                + " usedMotionDir=" + usedMotionDir
                + " motionDisagree=" + motionDisagrees
                + " unsettledForce=" + unsettledForce
                + incline(cart)
                + motion(cart)
                + " yAbove=" + thousandths(cart.getY() - railPos.getY()));
    }

    public static void slopeSynth(AbstractMinecart cart, Object railShape, float yaw, float pitch) {
        debug(cart, "slopeSynth", "shape=" + railShape
                + " yaw/pitch=" + angles(yaw, pitch)
                + incline(cart));
    }

    public static void cornerClear(AbstractMinecart cart, float yaw, Object railShape) {
        debug(cart, "cornerClear", "drop slope pitch yaw=" + one(yaw) + shape(railShape));
    }

    public static void takeoffSlopeKeep(AbstractMinecart cart) {
        debug(cart, "takeoff", "slopeKeep incline=" + MinecartFloatStateHelper.getIncline(cart)
                + shape(MinecartFloatStateHelper.getRailShapeName(cart))
                + " railPose=" + formatRailPose(cart)
                + " ignoredUnderfootFlat");
    }

    public static void takeoffFlat(AbstractMinecart cart, float yaw) {
        debug(cart, "takeoff", "flat pitch=0 yaw=" + one(yaw)
                + shape(MinecartFloatStateHelper.getRailShapeName(cart)));
    }

    public static void takeoffSlope(AbstractMinecart cart) {
        debug(cart, "takeoff", "slope incline=" + MinecartFloatStateHelper.getIncline(cart)
                + shape(MinecartFloatStateHelper.getRailShapeName(cart))
                + " railPose=" + formatRailPose(cart));
    }

    public static void climbRestore(AbstractMinecart cart, BlockPos flatTarget, double yAbove) {
        debug(cart, "climbRestore", "flatTarget=" + flatTarget
                + " yAbove=" + thousandths(yAbove)
                + " memory=" + MinecartFloatStateHelper.getRailShapeName(cart));
    }

    public static void climbRestoreAhead(AbstractMinecart cart) {
        debug(cart, "climbRestore", "ascendingAhead memory=" + MinecartFloatStateHelper.getRailShapeName(cart));
    }

    public static void levelFlight(AbstractMinecart cart, float yaw, Object railShape) {
        debug(cart, "levelFlight", "pitch=0 dy=0 yaw=" + one(yaw)
                + shape(railShape)
                + motion(cart));
    }

    public static void sideways(AbstractMinecart cart, Object railShape, Vec3 before, Vec3 after) {
        debug(cart, "sideways", "shape=" + railShape + " motion " + fmt(before) + "->" + fmt(after));
    }

    public static void nudgeCorner(AbstractMinecart cart, Object railShape) {
        debug(cart, "nudge", "skip corner shape=" + railShape);
    }

    public static void nudgeParallel(AbstractMinecart cart, Object railShape, int before, int now) {
        debug(cart, "nudge", "parallel skip XZ shape=" + railShape + " incline " + before + "->" + now);
    }

    public static void nudge(AbstractMinecart cart, BlockState railState, Object railShape, Vec3 approach, Vec3 along,
                             double dotA, double dotB, Vec3 before, Vec3 after) {
        debug(cart, "nudge", "block=" + blockId(railState)
                + shape(railShape)
                + " approach=" + fmt(approach)
                + " along=" + fmt(along)
                + " dots=" + String.format(Locale.ROOT, "%.2f/%.2f", dotA, dotB)
                + " motion " + fmt(before) + "->" + fmt(after)
                + incline(cart));
    }

    public static void deferSeat(AbstractMinecart cart, String where, BlockPos pos) {
        debug(cart, "deferSeat", where + " at " + pos + shape(MinecartFloatStateHelper.getRailShapeName(cart)));
    }

    public static void hardSeat(AbstractMinecart cart, BlockPos pos) {
        debug(cart, "reconnect", "hard seat at " + pos + shape(MinecartFloatStateHelper.getRailShapeName(cart)));
    }

    public static void skipTrack(AbstractMinecart cart, BlockPos pos, boolean leftRails, boolean unsettledFlat) {
        debug(cart, "skipTrack", "midair cancel moveAlongTrack at " + pos
                + " leftRails=" + leftRails
                + " unsettledFlat=" + unsettledFlat);
    }

    public static void holdTakeoff(AbstractMinecart cart, String reason) {
        debug(cart, reason, "hold takeoff near-zero motion");
    }

    public static void airTakeoff(AbstractMinecart cart, String reason, int grace, int inclineAtExit) {
        debug(cart, reason, "first exit grace=" + grace
                + " incline=" + inclineAtExit
                + shape(MinecartFloatStateHelper.getRailShapeName(cart))
                + motion(cart));
    }

    public static void solidBarrier(AbstractMinecart cart, boolean onGround, boolean vertical, boolean horizontal) {
        debug(cart, "solidBarrier", "onGround=" + onGround + " vertCol=" + vertical + " horCol=" + horizontal);
    }

    static String formatRailPose(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return "none";
        }
        return String.format(Locale.ROOT, "%.1f/%.1f",
                pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch());
    }

    /**
     * Every tick is too much to read. While the cart is actually on a rail, only write a line when the
     * height pops or the yaw/pitch stops matching that rail.
     */
    private static void noteOnRail(AbstractMinecart cart, BlockPos railPos, RailShape shape) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float yaw = cart.getYRot();
        float pitch = cart.getXRot();
        boolean hasPose = pose.paradiseLost$hasRailRenderPose();
        float poseYaw = hasPose ? pose.paradiseLost$getRailRenderYaw() : yaw;
        OnRailSample last = ON_RAIL.get(cart);
        boolean sameRail = last != null && last.rail.equals(railPos) && last.shape == shape;
        if (sameRail) {
            double dy = cart.getY() - last.y;
            double expected = MinecartFloatStateHelper.getIncline(cart) * cart.getDeltaMovement().horizontalDistance();
            // Rose more than the slope itself accounts for. Flat rails should not rise at all.
            if (dy > 0.03D && dy - expected > 0.05D) {
                debug(cart, "heightUp",
                        "dy=" + String.format(Locale.ROOT, "%.3f", dy)
                                + " expected=" + String.format(Locale.ROOT, "%.3f", expected)
                                + " yAbove=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY())
                                + shape(shape.getSerializedName())
                                + " pos=" + fmt(cart.position()));
            }
            float yawJump = Math.abs(Mth.wrapDegrees(yaw - last.yaw));
            if (!MinecartRailMotionHelper.isCornerRail(shape) && yawJump > 25.0F) {
                debug(cart, "rotJump",
                        "yaw " + String.format(Locale.ROOT, "%.1f", last.yaw)
                                + " -> " + String.format(Locale.ROOT, "%.1f", yaw)
                                + shape(shape.getSerializedName())
                                + " pitch=" + String.format(Locale.ROOT, "%.1f", pitch));
            }
        }
        boolean flat = !shape.isAscending() && !MinecartRailMotionHelper.isCornerRail(shape);
        if (flat && Math.abs(pitch) > 8.0F && (last == null || !last.rail.equals(railPos) || Math.abs(last.pitch) <= 8.0F)) {
            debug(cart, "rotOdd",
                    "flat rail but pitch=" + String.format(Locale.ROOT, "%.1f", pitch)
                            + shape(shape.getSerializedName())
                            + " yaw=" + String.format(Locale.ROOT, "%.1f", yaw));
        }
        if (shape.isAscending() && MinecartFloatStateHelper.getIncline(cart) != 0
                && Math.abs(pitch) < 4.0F
                && (last == null || !last.rail.equals(railPos) || Math.abs(last.pitch) >= 4.0F)) {
            debug(cart, "rotFlat",
                    "slope rail but pitch=" + String.format(Locale.ROOT, "%.1f", pitch)
                            + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                            + shape(shape.getSerializedName()));
        }
        if (hasPose && !MinecartRailMotionHelper.isCornerRail(shape)) {
            float disagree = Math.abs(Mth.wrapDegrees(poseYaw - yaw));
            float lastDisagree = last == null || !last.hasPose ? 0.0F : Math.abs(Mth.wrapDegrees(last.poseYaw - last.yaw));
            if (disagree > 25.0F && lastDisagree <= 25.0F) {
                debug(cart, "rotMismatch",
                        "entityYaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                                + " railPoseYaw=" + String.format(Locale.ROOT, "%.1f", poseYaw)
                                + shape(shape.getSerializedName()));
            }
        }
        OnRailSample sample = new OnRailSample();
        sample.rail = railPos.immutable();
        sample.shape = shape;
        sample.y = cart.getY();
        sample.yaw = yaw;
        sample.pitch = pitch;
        sample.poseYaw = poseYaw;
        sample.hasPose = hasPose;
        ON_RAIL.put(cart, sample);
    }

    private static final class OnRailSample {
        private BlockPos rail;
        private RailShape shape;
        private double y;
        private float yaw;
        private float pitch;
        private float poseYaw;
        private boolean hasPose;
    }
}
