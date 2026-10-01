package net.id.paradise_lost.attachments;

import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

/**
 * Minecart levitation state stored as data attachments ({@link CommonDataAttachments#MINE_CART_FLOATING},
 * {@link CommonDataAttachments#MINE_CART_FLOAT_INCLINE}).
 */
public final class MinecartFloating {
    private static final double FLOAT_SECONDS = 4.1;
    private static final double MOTION_EPSILON = 0.05D;
    private static final String LEGACY_NBT_KEY = "paradiseLostFloating";

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
        }
    }

    public static int getIncline(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE);
    }

    public static void setIncline(AbstractMinecart cart, int incline) {
        Services.ATTACHMENTS.setAttachedValue(
                cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE, Mth.clamp(incline, -1, 1));
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
     * Remember whether the cart is climbing or descending a non-flat rail so off-rail
     * levitation can continue on that diagonal until float time runs out.
     */
    public static void updateInclineFromRail(AbstractMinecart cart, RailShape shape, Vec3 motion) {
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
        } else if (goingDown) {
            setIncline(cart, -1);
        }
    }

    /**
     * Best-effort refresh when leaving rails: if a rail block is still at the cart's
     * position, sync incline from that shape so non-flat exits keep climbing/descending.
     * Does not look below the cart — that would wipe incline while flying over other rails.
     */
    public static void refreshInclineFromCurrentRail(AbstractMinecart cart) {
        BlockState state = cart.level().getBlockState(cart.blockPosition());
        if (state.getBlock() instanceof BaseRailBlock rail) {
            updateInclineFromRail(cart, state.getValue(rail.getShapeProperty()), cart.getDeltaMovement());
        }
    }

    public static boolean isCartOnRail(AbstractMinecart cart) {
        int i = Mth.floor(cart.getX());
        int j = Mth.floor(cart.getY());
        int k = Mth.floor(cart.getZ());
        BlockState blockState = cart.level().getBlockState(new BlockPos(i, j, k));
        return BaseRailBlock.isRail(blockState);
    }

    /**
     * One-time migration from the old manual NBT blob into attachments.
     */
    public static void loadLegacyNbt(AbstractMinecart cart, CompoundTag tag) {
        if (!tag.contains(LEGACY_NBT_KEY, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag floating = tag.getCompound(LEGACY_NBT_KEY);
        int time = floating.getInt("floatTime");
        setFloatTime(cart, floating.getBoolean("floating") ? Math.max(time, 1) : time);
        setIncline(cart, floating.getInt("floatIncline"));
        tag.remove(LEGACY_NBT_KEY);
    }
}
