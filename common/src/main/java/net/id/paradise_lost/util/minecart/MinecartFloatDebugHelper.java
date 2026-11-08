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

import java.util.Locale;
import java.util.WeakHashMap;

/**
 * Debug logging for floating minecarts.
 */
public final class MinecartFloatDebugHelper {
    private MinecartFloatDebugHelper() {
    }

    private static final String DEBUG_PREFIX = "[PL-MinecartFloat]";

    private static final WeakHashMap<AbstractMinecart, OnRailSample> ON_RAIL = new WeakHashMap<>();

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
                railInfo = blockId(railState) + "@" + railPos + " shape=" + shape
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
                        + " motion=" + fmt(cart.getDeltaMovement())
                        + " rail=" + railInfo);
    }

    public static void debugEvent(AbstractMinecart cart, String event, String detail) {
        debug(cart, event, detail);
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
                                + " shape=" + shape.getSerializedName()
                                + " pos=" + fmt(cart.position()));
            }
            float yawJump = Math.abs(Mth.wrapDegrees(yaw - last.yaw));
            if (!MinecartRailMotionHelper.isCornerRail(shape) && yawJump > 25.0F) {
                debug(cart, "rotJump",
                        "yaw " + String.format(Locale.ROOT, "%.1f", last.yaw)
                                + " -> " + String.format(Locale.ROOT, "%.1f", yaw)
                                + " shape=" + shape.getSerializedName()
                                + " pitch=" + String.format(Locale.ROOT, "%.1f", pitch));
            }
        }
        boolean flat = !shape.isAscending() && !MinecartRailMotionHelper.isCornerRail(shape);
        if (flat && Math.abs(pitch) > 8.0F && (last == null || !last.rail.equals(railPos) || Math.abs(last.pitch) <= 8.0F)) {
            debug(cart, "rotOdd",
                    "flat rail but pitch=" + String.format(Locale.ROOT, "%.1f", pitch)
                            + " shape=" + shape.getSerializedName()
                            + " yaw=" + String.format(Locale.ROOT, "%.1f", yaw));
        }
        if (shape.isAscending() && MinecartFloatStateHelper.getIncline(cart) != 0
                && Math.abs(pitch) < 4.0F
                && (last == null || !last.rail.equals(railPos) || Math.abs(last.pitch) >= 4.0F)) {
            debug(cart, "rotFlat",
                    "slope rail but pitch=" + String.format(Locale.ROOT, "%.1f", pitch)
                            + " incline=" + MinecartFloatStateHelper.getIncline(cart)
                            + " shape=" + shape.getSerializedName());
        }
        if (hasPose && !MinecartRailMotionHelper.isCornerRail(shape)) {
            float disagree = Math.abs(Mth.wrapDegrees(poseYaw - yaw));
            float lastDisagree = last == null || !last.hasPose ? 0.0F : Math.abs(Mth.wrapDegrees(last.poseYaw - last.yaw));
            if (disagree > 25.0F && lastDisagree <= 25.0F) {
                debug(cart, "rotMismatch",
                        "entityYaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                                + " railPoseYaw=" + String.format(Locale.ROOT, "%.1f", poseYaw)
                                + " shape=" + shape.getSerializedName());
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
