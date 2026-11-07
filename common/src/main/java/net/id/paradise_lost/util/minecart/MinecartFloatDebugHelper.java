package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.debug.DebugFlags;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

/**
 * Debug logging for floating minecarts.
 */
public final class MinecartFloatDebugHelper {
    private MinecartFloatDebugHelper() {
    }

    private static final String DEBUG_PREFIX = "[PL-MinecartFloat]";

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
                railInfo = blockId(railState) + "@" + railPos + " shape=" + railState.getValue(rail.getShapeProperty())
                        + " yAbove=" + String.format(Locale.ROOT, "%.3f", cart.getY() - railPos.getY());
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
}
