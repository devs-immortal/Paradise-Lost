package net.id.paradise_lost.debug;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class MinecartRotationDebug {
    private static final String PREFIX = "[PL-CartRotation]";
    private static final long START_NANOS = System.nanoTime();
    private static final Map<Entity, Map<String, Float>> LAST_VALUES = new WeakHashMap<>();

    private MinecartRotationDebug() {
    }

    public static void log(String source, AbstractMinecart cart, String message) {
        long millis = (System.nanoTime() - START_NANOS) / 1_000_000L;
        ModConstants.LOGGER.info("{} t={}ms {} {} id={} {}", PREFIX, millis, side(cart), source, cart.getId(), message);
    }

    public static boolean moved(AbstractMinecart cart, String channel, float degrees, float threshold) {
        Map<String, Float> channels = LAST_VALUES.computeIfAbsent(cart, c -> new HashMap<>());
        Float last = channels.get(channel);
        if (last != null) {
            boolean unchanged = Float.isNaN(degrees) || Float.isNaN(last)
                    ? Float.isNaN(degrees) && Float.isNaN(last)
                    : Math.abs(Mth.wrapDegrees(degrees - last)) < threshold;
            if (unchanged) {
                return false;
            }
        }
        channels.put(channel, degrees);
        return true;
    }

    public static String side(AbstractMinecart cart) {
        return cart.level().isClientSide() ? "CLIENT" : "SERVER";
    }

    public static String deg(float value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    public static String state(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        Vec3 motion = cart.getDeltaMovement();
        double dx = cart.getX() - cart.xOld;
        double dz = cart.getZ() - cart.zOld;
        double travel = Math.sqrt(dx * dx + dz * dz);
        return "tick=" + cart.tickCount
                + " game=" + cart.level().getGameTime()
                + " yRot=" + deg(cart.getYRot()) + " yRotO=" + deg(cart.yRotO)
                + " xRot=" + deg(cart.getXRot())
                + " sync=" + deg(MinecartFloatingHelper.getSyncedFloatYaw(cart))
                + "/" + deg(MinecartFloatingHelper.getSyncedFloatPitch(cart))
                + " railPose=" + (pose.paradiseLost$hasRailRenderPose()
                ? deg(pose.paradiseLost$getRailRenderYaw()) + "/" + deg(pose.paradiseLost$getRailRenderPitch())
                : "none")
                + " floatT=" + MinecartFloatingHelper.getFloatTime(cart)
                + " offRail=" + MinecartFloatingHelper.isOffRail(cart)
                + " incline=" + MinecartFloatingHelper.getIncline(cart)
                + " storedShape=" + MinecartFloatingHelper.getRailShapeName(cart)
                + " motionYaw=" + deg((float) (Math.atan2(motion.z, motion.x) * 180.0D / Math.PI))
                + " motionH=" + String.format(Locale.ROOT, "%.3f", motion.horizontalDistance())
                + " travelYaw=" + deg((float) (Math.atan2(dz, dx) * 180.0D / Math.PI))
                + " travelH=" + String.format(Locale.ROOT, "%.3f", travel)
                + " pos=" + String.format(Locale.ROOT, "(%.2f,%.2f,%.2f)", cart.getX(), cart.getY(), cart.getZ())
                + " rails=" + rails(cart);
    }

    private static String rails(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        return "at:" + railShape(cart, pos) + " below:" + railShape(cart, pos.below());
    }

    private static String railShape(AbstractMinecart cart, BlockPos pos) {
        BlockState state = cart.level().getBlockState(pos);
        if (state.getBlock() instanceof BaseRailBlock rail) {
            return state.getValue(rail.getShapeProperty()).getSerializedName();
        }
        return "-";
    }
}
