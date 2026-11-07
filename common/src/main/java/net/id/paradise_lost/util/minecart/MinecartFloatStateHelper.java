package net.id.paradise_lost.util.minecart;

import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.datafixer.MinecartFloatDataFix;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.Locale;

/**
 * Float timer, off-rail flag, incline, stored rail shape, and the pose synced to clients.
 */
public final class MinecartFloatStateHelper {
    private MinecartFloatStateHelper() {
    }

    private static final double FLOAT_SECONDS = 4.1;

    private static final String LEGACY_NBT_KEY = "paradiseLostFloating";

    public static boolean isFloating(AbstractMinecart cart) {
        return getFloatTime(cart) > 0;
    }

    public static int getFloatTime(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.getFloatTicks(cart);
    }

    public static void setFloatTime(AbstractMinecart cart, int time) {
        CommonDataAttachments.FLOATY_MINECART.setFloatTicks(cart, Math.max(time, 0));
        if (time <= 0) {
            setIncline(cart, 0);
            setRailShape(cart, "");
            setOffRail(cart, false);
            ((MinecartFloatPoseAccess) cart).paradiseLost$setOpenAirClimbGrace(0);
        }
    }

    public static boolean isOffRail(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.isOffRail(cart);
    }

    public static void setOffRail(AbstractMinecart cart, boolean offRail) {
        CommonDataAttachments.FLOATY_MINECART.setOffRail(cart, offRail);
    }

    public static int getIncline(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.getIncline(cart);
    }

    public static void setIncline(AbstractMinecart cart, int incline) {
        CommonDataAttachments.FLOATY_MINECART.setIncline(cart, Mth.clamp(incline, -1, 1));
    }

    public static String getRailShapeName(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.getRailShapeName(cart);
    }

    public static void setRailShape(AbstractMinecart cart, RailShape shape) {
        setRailShape(cart, shape == null ? "" : shape.getSerializedName());
    }

    public static void setRailShape(AbstractMinecart cart, String shapeName) {
        CommonDataAttachments.FLOATY_MINECART.setRailShapeName(cart, shapeName == null ? "" : shapeName);
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

    /**
     * True when we still remember leaving an ascending rail.
     */
    public static boolean isClimbMemory(AbstractMinecart cart) {
        RailShape shape = getRailShape(cart);
        return shape != null && shape.isAscending();
    }

    public static void setSyncedFloatPose(AbstractMinecart cart, float yaw, float pitch) {
        CommonDataAttachments.FLOATY_MINECART.setPose(cart, yaw, pitch);
    }

    public static float getSyncedFloatYaw(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.getPoseYaw(cart);
    }

    public static float getSyncedFloatPitch(AbstractMinecart cart) {
        return CommonDataAttachments.FLOATY_MINECART.getPosePitch(cart);
    }

    public static void loadLegacyNbt(AbstractMinecart cart, CompoundTag tag) {
        if (tag.contains(LEGACY_NBT_KEY, Tag.TAG_COMPOUND)) {
            CompoundTag floating = tag.getCompound(LEGACY_NBT_KEY);
            int time = floating.getInt("floatTime");
            setFloatTime(cart, floating.getBoolean("floating") ? Math.max(time, 1) : time);
            setIncline(cart, floating.getInt("floatIncline"));
            if (floating.contains("floatShape", Tag.TAG_STRING)) {
                setRailShape(cart, floating.getString("floatShape"));
            }
            tag.remove(LEGACY_NBT_KEY);
        }
        MinecartFloatDataFix.migrateCardinal(cart, tag);
    }
}
