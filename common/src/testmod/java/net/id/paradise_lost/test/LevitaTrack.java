package net.id.paradise_lost.test;

import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

import static net.id.paradise_lost.test.TestHelpers.check;

public final class LevitaTrack {
    public static final int CHARGE = 82;
    public static final int STAIRS = 6;

    public static BlockPos start;
    public static Direction dir;
    public static int activeStairs = STAIRS;
    public static int activeGapAt = -1;
    public static int activeGapLen = 0;
    public static double launchPhase;

    private LevitaTrack() {
    }

    public static void track(ServerLevel world, BlockPos from, String layout) {
        for (int i = 0; i < layout.length(); i++) {
            BlockPos at = from.relative(dir, i);

            char c = layout.charAt(i);
            if (c == '.')
                continue;
            if (c == 'L')
                world.setBlockAndUpdate(at.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());

            BlockState block = c == 'r'
                    ? Blocks.RAIL.defaultBlockState()
                    : BlockRegistry.LEVITA_RAIL.get().defaultBlockState();
            world.setBlockAndUpdate(at, block);
        }
    }

    public static void slope(ServerLevel world, BlockPos at) {
        RailShape shape = switch (dir) {
            case NORTH -> RailShape.ASCENDING_NORTH;
            case SOUTH -> RailShape.ASCENDING_SOUTH;
            case EAST -> RailShape.ASCENDING_EAST;
            default -> RailShape.ASCENDING_WEST;
        };
        BlockState old = world.getBlockState(at);
        Property<RailShape> property = ((BaseRailBlock) old.getBlock()).getShapeProperty();
        world.setBlock(at, old.setValue(property, shape), Block.UPDATE_ALL);

        BlockState now = world.getBlockState(at);
        check(now.is(old.getBlock()) && now.getValue(property) == shape, "setup: slope at " + at.toShortString() + " is " + now);
    }

    public static void singleSlope(ServerLevel world, boolean down) {
        world.setBlockAndUpdate(start.relative(dir), Blocks.STONE.defaultBlockState());
        track(world, start, "L");
        if (down) {
            track(world, start.relative(dir).above(), "r");
        } else {
            track(world, start.relative(dir, -2), "rr");
        }
        slope(world, start);
    }

    public static void staircase(ServerLevel world, BlockPos from, boolean levita) {
        staircase(world, from, levita, activeStairs);
    }

    public static void staircase(ServerLevel world, BlockPos from, boolean levita, int stairs) {
        for (int i = 0; i < stairs; i++) {
            BlockPos at = from.relative(dir, i).above(i);
            if (levita) {
                placePoweredLevitaRail(world, at);
            } else {
                world.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
                track(world, at, "r");
            }
        }
        BlockPos top = from.relative(dir, stairs).above(stairs);
        fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
        track(world, top, "rr");
        track(world, from.relative(dir, -3), "rrr");
        for (int i = 0; i < stairs; i++) {
            slope(world, from.relative(dir, i).above(i));
        }
    }

    public static void placePoweredLevitaRail(ServerLevel world, BlockPos at) {
        world.setBlockAndUpdate(at.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(at, BlockRegistry.LEVITA_RAIL.get().defaultBlockState());
    }

    public static void trench(ServerLevel world, int from, int to) {
        Direction side = dir.getClockWise();
        fill(world, start.relative(dir, from).relative(side).below(3), start.relative(dir, to).relative(side.getOpposite()).below(), Blocks.AIR.defaultBlockState());
    }

    public static void fill(ServerLevel world, BlockPos a, BlockPos b, BlockState state) {
        for (BlockPos at : BlockPos.betweenClosed(a, b)) world.setBlockAndUpdate(at, state);
    }

    public static int launch(ServerLevel world, EntityType<? extends AbstractMinecart> type, BlockPos at, double speed) {
        AbstractMinecart cart = type.create(world);
        cart.moveTo(at.getX() + 0.5 + dir.getStepX() * launchPhase, at.getY(), at.getZ() + 0.5 + dir.getStepZ() * launchPhase, 0, 0);
        cart.setDeltaMovement(Vec3.atLowerCornerOf(dir.getNormal()).scale(speed));
        world.addFreshEntity(cart);
        return cart.getId();
    }

    public static void checkFloatRotation(AbstractMinecart cart, String where) {
        if (!MinecartFloatingHelper.isFloating(cart)) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        int incline = MinecartFloatingHelper.getIncline(cart);
        boolean offRail = MinecartFloatingHelper.isOffRail(cart) || MinecartFloatingHelper.shouldSkipRailRenderSnap(cart);
        boolean hasPose = pose.paradiseLost$hasRailRenderPose();

        if (incline != 0) {
            if (offRail) {
                float pitch = hasPose
                        ? pose.paradiseLost$getRailRenderPitch()
                        : MinecartFloatingHelper.getSyncedFloatPitch(cart);
                float yaw = hasPose
                        ? pose.paradiseLost$getRailRenderYaw()
                        : MinecartFloatingHelper.getSyncedFloatYaw(cart);
                check(Math.abs(pitch) > 1 || hasPose, where + " floating slope has no pitch pose/sync"
                        + describe(cart) + floatState(cart));
                check(Math.abs(Math.abs(pitch) - 45) < 8, where + " floating slope pitch is " + fmt(pitch)
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                check(Math.abs(Math.abs(cart.getXRot()) - 45) < 8, where + " entity pitch is " + fmt(cart.getXRot())
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                check(Math.abs(cart.getXRot() - pitch) < 5, where + " entity pitch " + fmt(cart.getXRot())
                        + " != float pitch " + fmt(pitch) + describe(cart) + floatState(cart));
                if (hasPose || Math.abs(MinecartFloatingHelper.getSyncedFloatPitch(cart)) > 1) {
                    check(Math.abs(Mth.wrapDegrees(cart.getYRot() - yaw)) < 8, where + " entity yaw " + fmt(cart.getYRot())
                            + " != float yaw " + fmt(yaw) + describe(cart) + floatState(cart));
                }
                checkSlopeFacing(cart, where, yaw);
            } else if (hasPose) {
                float pitch = pose.paradiseLost$getRailRenderPitch();
                check(Math.abs(Math.abs(pitch) - 45) < 8, where + " on-rail slope pose pitch is " + fmt(pitch)
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                checkSlopeFacing(cart, where, pose.paradiseLost$getRailRenderYaw());
            }
        } else if (offRail) {
            check(Math.abs(cart.getXRot()) < 2, where + " flat float entity pitch is " + fmt(cart.getXRot())
                    + describe(cart) + floatState(cart));
            if (hasPose) {
                check(Math.abs(pose.paradiseLost$getRailRenderPitch()) < 2, where + " flat float pose pitch is "
                        + fmt(pose.paradiseLost$getRailRenderPitch()) + describe(cart) + floatState(cart));
            }
            check(Math.abs(MinecartFloatingHelper.getSyncedFloatPitch(cart)) < 2, where + " flat float sync pitch is "
                    + fmt(MinecartFloatingHelper.getSyncedFloatPitch(cart)) + describe(cart) + floatState(cart));
        }
    }

    public static void checkSlopeFacing(AbstractMinecart cart, String where, float railYaw) {
        double yawRad = Math.toRadians(railYaw);
        Vec3 facing = new Vec3(Math.cos(yawRad), 0, Math.sin(yawRad));
        Vec3 alongTrack = Vec3.atLowerCornerOf(dir.getNormal());
        Vec3 sideways = Vec3.atLowerCornerOf(dir.getClockWise().getNormal());
        check(Math.abs(facing.dot(alongTrack)) > 0.85, where + " cart yaw is not along the track (diagonal float), poseYaw="
                + fmt(railYaw) + describe(cart) + floatState(cart));
        check(Math.abs(facing.dot(sideways)) < 0.15, where + " cart renders across the track, shape "
                + MinecartFloatingHelper.getRailShapeName(cart) + describe(cart) + floatState(cart));

        Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
        if (motion.lengthSqr() > 0.01) {
            motion = motion.normalize();
            check(Math.abs(facing.dot(motion)) > 0.85, where + " cart yaw is not on the motion axis (diagonal float pose), poseYaw="
                    + fmt(railYaw) + describe(cart) + floatState(cart));
        }
    }

    public static double along(Entity entity) {
        return entity.position().subtract(Vec3.atBottomCenterOf(start)).dot(Vec3.atLowerCornerOf(dir.getNormal()));
    }

    public static double side(Entity entity) {
        return entity.position().subtract(Vec3.atBottomCenterOf(start)).dot(Vec3.atLowerCornerOf(dir.getClockWise().getNormal()));
    }

    public static String describe(Entity entity) {
        return " (along=" + fmt(along(entity)) + ", side=" + fmt(side(entity)) + ", y=" + fmt(entity.getY())
                + ", motion=" + fmt(entity.getDeltaMovement().x) + "/" + fmt(entity.getDeltaMovement().y) + "/" + fmt(entity.getDeltaMovement().z) + ")";
    }

    public static String floatState(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        String railPose = pose.paradiseLost$hasRailRenderPose()
                ? fmt(pose.paradiseLost$getRailRenderYaw()) + "/" + fmt(pose.paradiseLost$getRailRenderPitch())
                : "none";
        return " [float=" + MinecartFloatingHelper.getFloatTime(cart) + ", incline=" + MinecartFloatingHelper.getIncline(cart)
                + ", shape=" + MinecartFloatingHelper.getRailShapeName(cart)
                + ", offRail=" + MinecartFloatingHelper.isOffRail(cart)
                + ", yaw/pitch=" + fmt(cart.getYRot()) + "/" + fmt(cart.getXRot())
                + ", railPose=" + railPose + "]";
    }

    public static String fmt(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
