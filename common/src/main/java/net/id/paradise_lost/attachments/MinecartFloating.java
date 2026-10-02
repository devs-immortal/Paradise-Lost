package net.id.paradise_lost.attachments;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
    private static final double FLOAT_SECONDS = 4.1;
    private static final double MOTION_EPSILON = 0.05D;
    private static final String LEGACY_NBT_KEY = "paradiseLostFloating";

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
        }
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
        } else if (goingDown) {
            setIncline(cart, -1);
        }
    }

    public static void captureRailState(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        BlockState state = cart.level().getBlockState(pos);
        if (!BaseRailBlock.isRail(state)) {
            state = cart.level().getBlockState(pos.below());
        }
        if (state.getBlock() instanceof BaseRailBlock rail) {
            updateInclineFromRail(cart, state.getValue(rail.getShapeProperty()), cart.getDeltaMovement());
        }
    }

    public static void applyFloatingRotation(AbstractMinecart cart) {
        int incline = getIncline(cart);
        if (incline == 0 || getMotionAlignedTangent(cart) == null) {
            cart.setXRot(0.0F);
            cart.xRotO = 0.0F;
            return;
        }
        float pitch = incline * 45.0F;
        cart.setXRot(pitch);
        cart.xRotO = pitch;
    }

    public static boolean shouldUseFloatingRenderRotation(AbstractMinecart cart) {
        return isFloating(cart) && getIncline(cart) != 0;
    }


    public static boolean isMidairAboveRail(AbstractMinecart cart, BlockPos railPos, boolean alreadyLeftRails) {
        return alreadyLeftRails
                && isFloating(cart)
                && getIncline(cart) != 0
                && cart.getY() - railPos.getY() > 0.4D;
    }

    @Nullable
    public static Float getFloatingRenderYaw(AbstractMinecart cart) {
        Vec3 tangent = getMotionAlignedTangent(cart);
        if (tangent == null) {
            return null;
        }
        return (float) (Math.atan2(tangent.z, tangent.x) * (180.0D / Math.PI));
    }

    @Nullable
    public static Vec3 getMotionAlignedTangent(AbstractMinecart cart) {
        RailShape shape = getRailShape(cart);
        Pair<Vec3i, Vec3i> exits = shape == null ? null : RAIL_EXITS.get(shape);
        if (exits == null || !shape.isAscending()) {
            return null;
        }
        Vec3 first = Vec3.atLowerCornerOf(exits.getFirst());
        Vec3 second = Vec3.atLowerCornerOf(exits.getSecond());
        Vec3 tangent = first.subtract(second);
        if (tangent.lengthSqr() == 0.0D) {
            return null;
        }
        tangent = tangent.normalize();
        Vec3 motion = cart.getDeltaMovement();
        if (tangent.x * motion.x + tangent.z * motion.z < 0.0D) {
            tangent = tangent.scale(-1.0D);
        }
        return tangent;
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
