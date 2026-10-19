package net.id.paradise_lost.util;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.datafixer.MinecartFloatDataFix;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class MinecartFloatingHelper {
    public static final boolean DEBUG_LOG = Services.PLATFORM.isDevelopmentEnvironment() && System.getenv("GITHUB_ACTIONS") == null;

    private static final double FLOAT_SECONDS = 4.1;
    private static final double MOTION_EPSILON = 0.05D;
    /**
     * Max Y above an ascending rail block while still on its seat curve (~0 at low end, ~1 at high).
     */
    private static final double ASCENDING_SEAT_HEIGHT = 1.05D;
    /**
     * Horizontal alignment to treat float approach as parallel to the rail (carousel).
     */
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

    private MinecartFloatingHelper() {
    }

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
        // Curve to slope transitions often lack a clear axis component (CW vs CCW). Pick by
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
        // Wider hysteresis, tiny exit-dot margins flipped incline every tick on carousel slopes.
        int current = getIncline(cart);
        if (dotUpper > dotLower + 0.20D) {
            setIncline(cart, 1);
        } else if (dotLower > dotUpper + 0.20D) {
            setIncline(cart, -1);
        } else if (current == 0) {
            setIncline(cart, dotUpper >= dotLower ? 1 : -1);
        }
        // else keep current incline (ambiguous motion)
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
        captureRailRenderPose(cart, false);
    }

    /**
     * @param allowUnsettledAscending when true, midair over ascending still samples pose at the projected rail seat.
     *                                Used when incline flips (descend to climb) before the cart seats, cart.y offs are
     *                                unreliable high above the rail.
     */
    public static void captureRailRenderPose(AbstractMinecart cart, boolean allowUnsettledAscending) {
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
            // Match MinecartRenderer exactly: dir = negOff - posOff, yaw=atan2(dz,dx), pitch=atan(dy)*73.
            Vec3 dir = negOff.subtract(posOff);
            boolean usedMotionDir = false;
            if (dir.lengthSqr() < 1.0E-6D) {
                Vec3 motion = cart.getDeltaMovement();
                if (motion.horizontalDistanceSqr() < 1.0E-6D) {
                    return;
                }
                dir = new Vec3(motion.x, 0.0D, motion.z).normalize();
                usedMotionDir = true;
            } else {
                dir = dir.normalize();
            }
            float yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI));
            // Flat curves can sample a bogus dy from neighboring slopes, only keep pitch on ascents.
            float pitch = 0.0F;
            BlockPos railPos = findRailPos(cart);
            RailShape shape = null;
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail) {
                    shape = railState.getValue(rail.getShapeProperty());
                    if (shape.isAscending()) {
                        // High above an ascending (e.g. transition onto a corner) samples garbage
                        // offs and can leave stale slope pitch on the next rail. Only capture when
                        // seated, unless incline-flip needs a midair refresh, in which case sample
                        // at the projected rail seat instead.
                        if (!isSettledOnRail(cart, railPos)) {
                            if (!allowUnsettledAscending) {
                                return;
                            }
                            Vec3 seatPosOff = cart.getPosOffs(mid.x, mid.y, mid.z, 0.30000001192092896D);
                            Vec3 seatNegOff = cart.getPosOffs(mid.x, mid.y, mid.z, -0.30000001192092896D);
                            if (seatPosOff == null) {
                                seatPosOff = mid;
                            }
                            if (seatNegOff == null) {
                                seatNegOff = mid;
                            }
                            Vec3 seatDir = seatNegOff.subtract(seatPosOff);
                            if (seatDir.lengthSqr() < 1.0E-6D) {
                                return;
                            }
                            dir = seatDir.normalize();
                            usedMotionDir = false;
                            yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI));
                            float trialPitch = (float) (Math.atan(dir.y) * 73.0D);
                            // Seat sample still unusable, keep prior pose.
                            if (Math.abs(trialPitch) < 25.0F || Math.abs(trialPitch) > 60.0F) {
                                return;
                            }
                        }
                        // Match on-rail offs. If motion faces the other way, flip yaw and pitch together.
                        float offsYaw = yaw;
                        pitch = (float) (Math.atan(dir.y) * 73.0D);
                        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                                ? 0.0F
                                : 90.0F;
                        // Bad offs sample while floating, keep the last good 45 degrees instead of publishing a shallow tilt.
                        boolean keptPriorSlope = false;
                        if (isFloating(cart) && getIncline(cart) != 0
                                && (Math.abs(pitch) < 40.0F || Math.abs(pitch) > 50.0F)) {
                            MinecartFloatPoseAccess priorPose = (MinecartFloatPoseAccess) cart;
                            if (hasCredibleSlopeLaunchPitch(cart, priorPose)) {
                                pitch = priorPose.paradiseLost$getRailRenderPitch();
                                yaw = priorPose.paradiseLost$getRailRenderYaw();
                                keptPriorSlope = true;
                            } else {
                                pitch = synthSlopePitch(cart, yawAlongMotion(cart, axis));
                            }
                        }
                        float motionYaw = yawAlongMotion(cart, axis);
                        boolean motionDisagrees = false;
                        if (!keptPriorSlope) {
                            motionDisagrees = Math.abs(Mth.wrapDegrees(motionYaw - offsYaw)) > 90.0F;
                            if (motionDisagrees) {
                                yaw = motionYaw;
                                pitch = -pitch;
                            } else {
                                yaw = nearestRailYaw(offsYaw, axis);
                            }
                        }
                        // Log when pose jumps or motion disagrees with offs (throttled vs every tick).
                        MinecartFloatPoseAccess prior = (MinecartFloatPoseAccess) cart;
                        boolean poseJump = !prior.paradiseLost$hasRailRenderPose()
                                || Math.abs(Mth.wrapDegrees(yaw - prior.paradiseLost$getRailRenderYaw())) > 5.0F
                                || Math.abs(pitch - prior.paradiseLost$getRailRenderPitch()) > 5.0F;
                        if (poseJump || usedMotionDir || allowUnsettledAscending) {
                            debug(cart, "slopeCapture",
                                    "shape=" + shape
                                            + " offs=" + String.format(Locale.ROOT, "%.1f/%.1f", offsYaw, pitch)
                                            + " motionYaw=" + String.format(Locale.ROOT, "%.1f", motionYaw)
                                            + " final=" + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                                            + " dir=" + fmt(dir)
                                            + " usedMotionDir=" + usedMotionDir
                                            + " motionDisagree=" + motionDisagrees
                                            + " unsettledForce=" + allowUnsettledAscending
                                            + " incline=" + getIncline(cart)
                                            + " motion=" + fmt(cart.getDeltaMovement())
                                            + " yAbove=" + String.format(Locale.ROOT, "%.3f",
                                            cart.getY() - railPos.getY()));
                        }
                    } else if (shape == RailShape.EAST_WEST || shape == RailShape.NORTH_SOUTH) {
                        // Straight flat: offs near corners sampled yaw=-16.5 (carousel reconnect).
                        yaw = yawAlongMotion(cart, shape == RailShape.EAST_WEST ? 0.0F : 90.0F);
                    }
                }
            }
            // Stabilize against per-tick getPosOffs noise (carousel yaw/pitch flicker).
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                // Never pull ascending yaw toward a prior (possibly motion-locked) pose.
                if (shape == null || isCornerRail(shape)) {
                    yaw = nearestRailYaw(pose.paradiseLost$getRailRenderYaw(), yaw);
                }
                float oldPitch = pose.paradiseLost$getRailRenderPitch();
                // Micro-jitter only (<5 degrees). Wider magnitude freeze can stick takeoff below the
                // true slope pitch from offs.
                if (Math.abs(pitch) > 1.0F && Math.abs(oldPitch) > 1.0F
                        && Math.signum(pitch) == Math.signum(oldPitch)
                        && Math.abs(pitch - oldPitch) < 5.0F) {
                    pitch = oldPitch;
                }
            }
            // Don't publish pitch=0 while we're still marked as on a slope.
            if (getIncline(cart) != 0 && Math.abs(pitch) <= 1.0F) {
                if (hasCredibleSlopeLaunchPitch(cart, pose)) {
                    return;
                }
                // Synthesize here, do not call ensureAscendingFloatPose (it re-enters capture).
                pitch = synthSlopePitch(cart, yaw);
            }
            pose.paradiseLost$setRailRenderPose(yaw, pitch);
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    public static void prepareTakeoff(AbstractMinecart cart) {
        // Remember slope state first, captureRailState can overwrite it with a floor flat underfoot.
        RailShape priorShape = getRailShape(cart);
        int priorIncline = getIncline(cart);
        captureRailState(cart);
        RailShape shape = getRailShape(cart);
        // Only keep the slope if the cart was really climbing or descending. An old ascending shape left
        // behind by levelFlight used to bring incline=1 back on the next flat takeoff.
        if (priorIncline != 0 && (shape == null || !shape.isAscending())) {
            // Underfoot flat is not the exit rail, keep the slope we left with.
            if (priorShape != null && priorShape.isAscending()) {
                setRailShape(cart, priorShape);
                shape = priorShape;
            } else {
                shape = priorShape;
            }
            setIncline(cart, priorIncline);
            if (shape != null && shape.isAscending()) {
                ensureAscendingFloatPose(cart, shape);
                applyFloatingRotation(cart);
                armOpenAirClimbGrace(cart, priorIncline);
                debug(cart, "takeoff", "slopeKeep incline=" + getIncline(cart)
                        + " shape=" + getRailShapeName(cart)
                        + " railPose=" + formatRailPose(cart)
                        + " ignoredUnderfootFlat");
                return;
            }
        }
        // Flat takeoff (or unknown): never carry slope pitch into the gap, photo regression.
        if (getIncline(cart) == 0 || shape == null || !shape.isAscending()) {
            captureRailRenderPose(cart);
            setIncline(cart, 0);
            // Forget the climb so the next gap doesn't start with a slope that isn't there.
            if (shape == null || shape.isAscending()) {
                setRailShape(cart, "");
                shape = null;
            }
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            pose.paradiseLost$setOpenAirClimbGrace(0);
            float yaw = resolveFlatFloatYaw(cart, shape);
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, yaw, 0.0F);
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            debug(cart, "takeoff", "flat pitch=0 yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + getRailShapeName(cart));
        } else {
            // Short ascending rails often never settle long enough for gated capture, force
            // seat-sample / synthesize before freezing the air pose.
            ensureAscendingFloatPose(cart, shape);
            applyFloatingRotation(cart);
            armOpenAirClimbGrace(cart, getIncline(cart));
            debug(cart, "takeoff", "slope incline=" + getIncline(cart)
                    + " shape=" + getRailShapeName(cart)
                    + " railPose=" + formatRailPose(cart));
        }
    }

    /**
     * About 2.5 seconds of climbing in the open after leaving an ascending rail (slopeLaunchesUpward).
     */
    private static final int OPEN_AIR_CLIMB_GRACE_TICKS = 50;
    /**
     * Once the grace is over, only a ceiling this close keeps the cart rising. Far-off trees used to tilt it forever.
     */
    private static final int IMMINENT_CEILING_BLOCKS = 3;
    private static final int CEILING_LOOKAHEAD_BLOCKS = 12;

    private static void armOpenAirClimbGrace(AbstractMinecart cart, int incline) {
        if (incline <= 0) {
            return;
        }
        int grace = OPEN_AIR_CLIMB_GRACE_TICKS;
        int solidDist = distanceToSolidAbove(cart, CEILING_LOOKAHEAD_BLOCKS);
        // A high ceiling (the test puts stone about 10 up) stretches the grace until it gets close.
        if (solidDist > IMMINENT_CEILING_BLOCKS) {
            grace = Math.max(grace, (solidDist - IMMINENT_CEILING_BLOCKS) * 10 + 20);
        }
        ((MinecartFloatPoseAccess) cart).paradiseLost$setOpenAirClimbGrace(grace);
    }

    /**
     * Ensure slope yaw/pitch is stored for ascending takeoff / short rails. Prefer vanilla offs (including midair
     * seat-sample); if findRailPos is already gone, synthesize from shape+motion.
     */
    public static void ensureAscendingFloatPose(AbstractMinecart cart, RailShape shape) {
        if (shape == null || !shape.isAscending()) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                ? 0.0F
                : 90.0F;
        // A pitch of about 45 isn't enough, the wrong sign reads as a 90 degree tilt on NeoForge.
        if (hasCredibleSlopeLaunchPitch(cart, pose)) {
            return;
        }
        captureRailRenderPose(cart, true);
        if (hasCredibleSlopeLaunchPitch(cart, pose)) {
            return;
        }
        float yaw = yawAlongMotion(cart, axis);
        float pitch = pitchFromAscendingShape(shape, yaw);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
        debug(cart, "slopeSynth", "shape=" + shape
                + " yaw/pitch=" + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                + " incline=" + getIncline(cart));
    }

    /**
     * Near 45 degrees and the right sign for this shape+yaw.
     */
    private static boolean hasCredibleSlopeLaunchPitch(AbstractMinecart cart, MinecartFloatPoseAccess pose) {
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return false;
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        if (Math.abs(Math.abs(pitch) - 45.0F) >= 5.0F) {
            return false;
        }
        if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
            return false;
        }
        RailShape shape = getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return Math.abs(pitch - pitchFromAscendingShape(shape, yaw)) <= 10.0F;
        }
        return true;
    }

    /**
     * Pitch of 45 degrees from the rail shape and yaw. Climbing is stored as +44.9, like the flipped E/W offsets.
     */
    private static float synthSlopePitch(AbstractMinecart cart, float yaw) {
        RailShape shape = getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return pitchFromAscendingShape(shape, yaw);
        }
        return getIncline(cart) >= 0 ? 44.9F : -44.9F;
    }

    /**
     * Horizontal travel for atan2 yaw (0=east). NeoForge clients often have zero mid-air deltaMovement, so fall back to
     * xo/zo delta.
     */
    private static Vec3 horizontalTravelVector(AbstractMinecart cart) {
        Vec3 motion = cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);
        if (motion.lengthSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            return motion;
        }
        double dx = cart.getX() - cart.xo;
        double dz = cart.getZ() - cart.zo;
        if (dx * dx + dz * dz > MOTION_EPSILON * MOTION_EPSILON) {
            return new Vec3(dx, 0.0D, dz);
        }
        return motion;
    }

    /**
     * Pose yaw on the travel axis (atan2, 0=east). Sync defaults to 0, don't treat "no motion" as aligned when the
     * stored shape is N/S.
     */
    private static boolean isSlopeYawAlignedWithMotion(AbstractMinecart cart, float poseYaw) {
        Vec3 travel = horizontalTravelVector(cart);
        if (travel.lengthSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            float motionYaw = (float) (Math.atan2(travel.z, travel.x) * (180.0D / Math.PI));
            float delta = Math.abs(Mth.wrapDegrees(poseYaw - motionYaw));
            // Allow opposite travel on the same rail axis (pitch sign handles climb vs descend).
            return delta <= 35.0F || delta >= 145.0F;
        }
        RailShape shape = getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            float expected = yawFromAscendingShape(shape, getIncline(cart));
            float delta = Math.abs(Mth.wrapDegrees(poseYaw - expected));
            return delta <= 35.0F || delta >= 145.0F;
        }
        // No travel and no shape, can't prove misalignment.
        return true;
    }

    /**
     * Climb faces the high end of an ascending rail; descend faces the low end. Values are MinecartRenderer atan2 yaw
     * (0=east, 90=south).
     */
    private static float yawFromAscendingShape(RailShape shape, int incline) {
        boolean climb = incline >= 0;
        return switch (shape) {
            case ASCENDING_EAST -> climb ? 0.0F : 180.0F;
            case ASCENDING_WEST -> climb ? 180.0F : 0.0F;
            case ASCENDING_NORTH -> climb ? -90.0F : 90.0F;
            default -> climb ? 90.0F : -90.0F;
        };
    }

    /**
     * atan2 yaw from travel, else shape+incline, else pose/sync.
     */
    private static float slopeYawFromMotionOrPose(AbstractMinecart cart) {
        Vec3 travel = horizontalTravelVector(cart);
        if (travel.lengthSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            return (float) (Math.atan2(travel.z, travel.x) * (180.0D / Math.PI));
        }
        RailShape shape = getRailShape(cart);
        if (shape != null && shape.isAscending()) {
            return yawFromAscendingShape(shape, getIncline(cart));
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()
                && isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
            return pose.paradiseLost$getRailRenderYaw();
        }
        float syncYaw = getSyncedFloatYaw(cart);
        if (Math.abs(syncYaw) > 0.01F || Math.abs(Math.abs(syncYaw) - 180.0F) <= 0.01F) {
            return syncYaw;
        }
        return entityYawToRailYaw(cart.getYRot());
    }

    /**
     * Keep float yaw/sync in MinecartRenderer (atan2) space on straight flats. On corners, only clear stale slope pitch
     * , do not overwrite curve yaw (vanilla getPos poses the curve).
     */
    public static void syncFlatFloatYawFromMotion(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        // Still seated on an ascending rail with slope pitch left over, so maintainSlope handles it.
        // Corners used to return early here as well, which left junctions stuck at 45 degrees.
        if (isFloating(cart) && poseHasSlopePitch(cart)) {
            BlockPos railPos = findRailPos(cart);
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail
                        && railState.getValue(rail.getShapeProperty()).isAscending()) {
                    return;
                }
            } else if (isOffRail(cart)) {
                return;
            }
        }
        clearFloatingPitch(cart);
        RailShape shape = getRailShape(cart);
        if (shape != RailShape.EAST_WEST && shape != RailShape.NORTH_SOUTH) {
            // Corners and unknown shapes: only zero the pitch, keep the curve yaw.
            float yaw = pose.paradiseLost$hasRailRenderPose()
                    ? pose.paradiseLost$getRailRenderYaw()
                    : getSyncedFloatYaw(cart);
            if (Math.abs(getSyncedFloatPitch(cart)) > 0.01F
                    || (pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 0.01F)) {
                setSyncedFloatPose(cart, yaw, 0.0F);
                pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
                debug(cart, "cornerClear", "drop slope pitch yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                        + " shape=" + shape);
            }
            return;
        }
        float yaw = resolveFlatFloatYaw(cart, shape);
        setSyncedFloatPose(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
    }

    /**
     * Prefer motion along the rail axis; if motion is cross-axis (perp approach), keep an existing on-axis float yaw
     * from flatAdapt/sync so reconnect does not flip between 0 and 180.
     */
    private static float resolveFlatFloatYaw(AbstractMinecart cart, @Nullable RailShape shape) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float syncYaw = getSyncedFloatYaw(cart);
        float poseYaw = pose.paradiseLost$hasRailRenderPose()
                ? pose.paradiseLost$getRailRenderYaw()
                : syncYaw;
        Vec3 motion = cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);

        if (shape == RailShape.EAST_WEST || shape == RailShape.NORTH_SOUTH) {
            float axis = shape == RailShape.EAST_WEST ? 0.0F : 90.0F;
            boolean alongAxis = motion.lengthSqr() > MOTION_EPSILON * MOTION_EPSILON
                    && (shape == RailShape.EAST_WEST
                    ? Math.abs(motion.x) >= Math.abs(motion.z)
                    : Math.abs(motion.z) >= Math.abs(motion.x));
            if (alongAxis) {
                return yawAlongMotion(cart, axis);
            }
            // Perp / weak motion: preserve flatAdapt yaw if it already sits on this axis.
            if (Math.abs(getSyncedFloatPitch(cart)) <= 0.01F && isStraightRailAxisYaw(syncYaw, shape)) {
                return Mth.wrapDegrees(syncYaw);
            }
            if (pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 0.01F
                    && isStraightRailAxisYaw(poseYaw, shape)) {
                return Mth.wrapDegrees(poseYaw);
            }
            return yawAlongMotion(cart, axis);
        }

        if (motion.lengthSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            motion = motion.normalize();
            return (float) (Math.atan2(motion.z, motion.x) * (180.0D / Math.PI));
        }
        if (pose.paradiseLost$hasRailRenderPose()) {
            return poseYaw;
        }
        return entityYawToRailYaw(cart.getYRot());
    }

    public static void prepareReconnect(AbstractMinecart cart) {
        // Redirect onto the landing before sampling yaw (tick-path seats can skip hard-seat).
        // Remember slope before captureRailState overwrites it.
        RailShape priorStoredShape = getRailShape(cart);
        int priorIncline = getIncline(cart);
        // Check for a continuing climb before captureRailState clears incline.
        boolean slopeLive = priorIncline > 0
                || (priorStoredShape != null && priorStoredShape.isAscending())
                || poseHasSlopePitch(cart);
        boolean ascendingClimbContinues = slopeLive
                && (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart));
        BlockPos railPos = findRailPos(cart);
        BlockState railState = null;
        RailShape landedPreview = null;
        if (railPos != null) {
            railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                landedPreview = railState.getValue(rail.getShapeProperty());
            }
        }
        // Skip deep floor flats mid-climb; normal track flats should still seat.
        boolean interveningFloor = ascendingClimbContinues
                && priorIncline > 0
                && (landedPreview == null || !landedPreview.isAscending())
                && railPos != null
                && isInterveningFlatWhileClimbing(cart, railPos, railState);
        if (interveningFloor) {
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (priorStoredShape != null && priorStoredShape.isAscending()) {
                setRailShape(cart, priorStoredShape);
            }
            setIncline(cart, priorIncline);
            RailShape keep = getRailShape(cart);
            if (keep != null && keep.isAscending()) {
                ensureAscendingFloatPose(cart, keep);
                applyFloatingRotation(cart);
            } else if (poseHasSlopePitch(cart)) {
                applyFloatingRotation(cart);
            }
            setOffRail(cart, true);
            debug(cart, "reconnect", "deferFlatClimb yaw/pitch="
                    + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + " ignored=" + landedPreview
                    + " keep=" + keep
                    + " incline=" + getIncline(cart));
            return;
        }
        if (railState != null && railState.getBlock() instanceof BaseRailBlock) {
            nudgeVelocityToRail(cart, railState);
        }
        captureRailState(cart);
        setOffRail(cart, false);
        // Snap into vanilla rail seat, float often left Y at *.000 while seat is *.0625
        // (carousel looked like it hovered above the rail).
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            Vec3 snapped = cart.getPos(cart.getX(), cart.getY(), cart.getZ());
            if (snapped != null) {
                cart.setPos(snapped.x, snapped.y, snapped.z);
            }
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        // Prefer whatever is actually under the cart.
        RailShape landed = getRailShape(cart);
        if (railPos != null && railState.getBlock() instanceof BaseRailBlock rail) {
            landed = railState.getValue(rail.getShapeProperty());
            setRailShape(cart, landed);
        }
        if (landed != null && landed.isAscending()) {
            // Landing on a slope, restore incline if a perp approach zeroed it.
            if (getIncline(cart) == 0) {
                updateInclineFromRail(cart, landed, cart.getDeltaMovement());
            }
            if (getIncline(cart) == 0) {
                // Motion still cross-axis after nudge flicker, pick climb/descend from vertical speed.
                Vec3 motion = cart.getDeltaMovement();
                int incline = motion.y < -MOTION_EPSILON ? -1 : 1;
                setIncline(cart, incline);
                double speed = Math.max(motion.horizontalDistance(), MOTION_EPSILON);
                Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(landed);
                if (exits != null) {
                    Vec3i upper = exits.getFirst().getY() >= exits.getSecond().getY()
                            ? exits.getFirst() : exits.getSecond();
                    Vec3i lower = exits.getFirst().getY() < exits.getSecond().getY()
                            ? exits.getFirst() : exits.getSecond();
                    Vec3i pick = incline < 0 ? lower : upper;
                    Vec3 along = new Vec3(pick.getX(), 0.0D, pick.getZ());
                    if (along.lengthSqr() > 1.0E-8D) {
                        along = along.normalize().scale(speed);
                        cart.setDeltaMovement(along.x, incline * speed, along.z);
                    } else {
                        cart.setDeltaMovement(motion.x, incline * speed, motion.z);
                    }
                } else {
                    cart.setDeltaMovement(motion.x, incline * speed, motion.z);
                }
            } else {
                Vec3 motion = cart.getDeltaMovement();
                int incline = getIncline(cart);
                cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
            }
            ensureAscendingFloatPose(cart, landed);
            applyFloatingRotation(cart);
            debug(cart, "reconnect", "slope yaw/pitch="
                    + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + " shape=" + landed
                    + " incline=" + getIncline(cart)
                    + " motion=" + fmt(cart.getDeltaMovement()));
        } else if (landed != null && isCornerRail(landed)) {
            // Seating on a corner always flattens the cart. Corner flyovers in the middle of a climb go through deferSeat
            // (keepSlopeFloatThroughCorner), not reconnect. cornerKeepSlope used to leave the cart at 45 degrees
            // on junctions because syncFlatFloatYawFromMotion wouldn't clear the curve pitch.
            setIncline(cart, 0);
            pose.paradiseLost$setOpenAirClimbGrace(0);
            float yaw = resolveFlatFloatYaw(cart, landed);
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, yaw, 0.0F);
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            debug(cart, "reconnect", "cornerFlat yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + landed
                    + " from=" + priorStoredShape
                    + " motion=" + fmt(cart.getDeltaMovement()));
        } else {
            float yaw = resolveFlatFloatYaw(cart, landed);
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, yaw, 0.0F);
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            pose.paradiseLost$setOpenAirClimbGrace(0);
            debug(cart, "reconnect", "flat yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + getRailShapeName(cart)
                    + " motion=" + fmt(cart.getDeltaMovement()));
        }
    }

    /**
     * Whether float motion should ignore block collision this tick (slope gaps, under-pier climbs). Descending onto a
     * flat landing stays collidable so we seat on top instead of phasing through.
     */
    public static boolean shouldUseSlopeNoclip(AbstractMinecart cart) {
        // Re-arm climb if levelFlight wiped incline mid-gap.
        maintainClimbTowardFlatLanding(cart);
        int incline = getIncline(cart);
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = railState.getValue(rail.getShapeProperty());
                if (shape.isAscending()) {
                    return incline != 0;
                }
                if (isCornerRail(shape)) {
                    // Ghost corners until we're close enough to seat.
                    return incline != 0 && !isSettledOnRail(cart, railPos);
                }
                // Flat underfoot: noclip deep floors / under-pier climbs, but collide with normal track.
                if (incline > 0 && !isSettledOnRail(cart, railPos)) {
                    if (isInterveningFlatWhileClimbing(cart, railPos, railState)) {
                        return true;
                    }
                    return cart.getY() < railPos.getY() + 0.0625D;
                }
            }
        }
        // Open air toward more slope, keep noclip.
        if (incline != 0
                && (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart))) {
            return true;
        }
        // Still climbing up into a flat landing.
        return incline > 0 && isBelowContinuingFlatLanding(cart);
    }

    /**
     * True when we still remember leaving an ascending rail.
     */
    private static boolean isClimbMemory(AbstractMinecart cart) {
        RailShape shape = getRailShape(cart);
        return shape != null && shape.isAscending();
    }

    /**
     * If incline got cleared mid-gap but we're still climbing toward a target, put it back.
     */
    public static void maintainClimbTowardFlatLanding(AbstractMinecart cart) {
        if (!isFloating(cart)) {
            return;
        }
        int incline = getIncline(cart);
        if (incline > 0) {
            maintainSlopeFloatPose(cart);
            return;
        }
        // Don't turn a descent into a climb.
        if (incline < 0) {
            return;
        }
        BlockPos flatTarget = findUnsettledFlatClimbTarget(cart);
        boolean climbMemory = isClimbMemory(cart);
        if (flatTarget == null && !climbMemory) {
            return;
        }
        if (flatTarget != null) {
            double height = cart.getY() - flatTarget.getY();
            // Only restore while still under the pier, at/above is a descend landing.
            if (height >= 0.0D) {
                return;
            }
            setIncline(cart, 1);
            maintainSlopeFloatPose(cart);
            debug(cart, "climbRestore", "flatTarget=" + flatTarget
                    + " yAbove=" + String.format(Locale.ROOT, "%.3f", height)
                    + " memory=" + getRailShapeName(cart));
            return;
        }
        // Still aiming at an ascending lip in open air.
        if (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart)) {
            setIncline(cart, 1);
            maintainSlopeFloatPose(cart);
            debug(cart, "climbRestore", "ascendingAhead memory=" + getRailShapeName(cart));
        }
    }

    /**
     * Force a credible 45 degrees pose whenever incline says we're on a slope.
     */
    public static void maintainSlopeFloatPose(AbstractMinecart cart) {
        if (getIncline(cart) == 0) {
            return;
        }
        // No slope tilt over a straight flat landing.
        if (flattenPoseOverStraightFlat(cart)) {
            return;
        }
        RailShape shape = getRailShape(cart);
        if (shape == null || !shape.isAscending()) {
            // Incline is set but shape was lost, still show a slope tilt along motion.
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            float yaw = slopeYawFromMotionOrPose(cart);
            float pitch = synthSlopePitch(cart, yaw);
            if (hasCredibleSlopeLaunchPitch(cart, pose)) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
            setSyncedFloatPose(cart, yaw, pitch);
            snapEntityRotation(cart, yaw, pitch);
            pose.paradiseLost$setRailRenderPose(yaw, pitch);
            return;
        }
        ensureAscendingFloatPose(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        // Reject shallow offs samples, east-default yaw, or wrong-sign 45 degrees.
        if (!hasCredibleSlopeLaunchPitch(cart, pose)) {
            ensureAscendingFloatPose(cart, shape);
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
            yaw = slopeYawFromMotionOrPose(cart);
        }
        if (Math.abs(pitch) <= 1.0F
                || Math.abs(pitch - pitchFromAscendingShape(shape, yaw)) > 10.0F) {
            pitch = pitchFromAscendingShape(shape, yaw);
        }
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
        setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
    }

    @Nullable
    private static BlockPos findUnsettledFlatClimbTarget(AbstractMinecart cart) {
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState state = cart.level().getBlockState(railPos);
            if (state.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = state.getValue(rail.getShapeProperty());
                if (!shape.isAscending() && !isCornerRail(shape) && !isSettledOnRail(cart, railPos)) {
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
        if (shape.isAscending() || isCornerRail(shape)) {
            return null;
        }
        if (isSettledOnRail(cart, ahead)) {
            return null;
        }
        // Must be at/above the cart, a floor flat below is not an ascend-flat target.
        if (ahead.getY() + 1 < cart.blockPosition().getY()) {
            return null;
        }
        return ahead;
    }

    /**
     * Brief noclip after leaving a rail so we clear the lip without digging into the pier.
     */
    public static boolean shouldUseExitGraceNoclip(AbstractMinecart cart) {
        if (getIncline(cart) < 0) {
            return hasContinuingAscendingRailAhead(cart)
                    || hasContinuingSlopeSolidAhead(cart)
                    || findRailPos(cart) != null;
        }
        return findRailPos(cart) != null
                || hasContinuingSlopeSolidAhead(cart)
                || hasContinuingAscendingRailAhead(cart)
                || isBelowContinuingFlatLanding(cart);
    }

    /**
     * Flat rail ahead/above that we haven't seated on yet.
     */
    private static boolean isBelowContinuingFlatLanding(AbstractMinecart cart) {
        BlockPos ahead = findContinuingRailAhead(cart, false);
        if (ahead == null) {
            return false;
        }
        BlockState state = cart.level().getBlockState(ahead);
        if (!(state.getBlock() instanceof BaseRailBlock rail)
                || state.getValue(rail.getShapeProperty()).isAscending()
                || isCornerRail(state.getValue(rail.getShapeProperty()))) {
            return false;
        }
        // Under the pier or not seated yet, both need noclip.
        return !isSettledOnRail(cart, ahead);
    }

    /**
     * Descent-only comeOffTrack noclip ticks to clear the exit pier. Keep short. 12 let carts coast above the next rail
     * (carousel "flies a little above").
     */
    public static int slopeDescentExitGraceTicks() {
        return 5;
    }

    private static final double FLAT_CONNECT_HEIGHT = 0.28D;
    /**
     * Match {@link #isSettledOnRail} flat threshold, flatten only when seating.
     */
    private static final double FLAT_POSE_ADOPT = 0.16D;

    public static boolean isNearFlatConnectHeight(AbstractMinecart cart, BlockPos railPos) {
        return cart.getY() - railPos.getY() <= FLAT_CONNECT_HEIGHT;
    }

    public static boolean isNearFlatPoseAdoptHeight(AbstractMinecart cart, BlockPos railPos) {
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return cart.getY() - railPos.getY() <= FLAT_POSE_ADOPT;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Curves are not flat landings, never adopt flat pose on corners (carousel).
        if (isCornerRail(shape) || shape.isAscending()) {
            return false;
        }
        // Same moment as rail connection / settle, earlier adopt rotates before seating.
        return isSettledOnRail(cart, railPos);
    }

    public static boolean isFlatRailConnectTooEarly(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !isNearFlatConnectHeight(cart, railPos);
    }

    /**
     * Ghosting over a rail midair: ascending refreshes climb; flat redirects + adopts landing pose. Corners keep slope
     * tilt while we're still climbing into them.
     */
    public static void adaptFloatWhileGhosting(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (shape.isAscending()) {
            adaptFloatToAscendingRail(cart, railState);
            return;
        }
        if (isCornerRail(shape)) {
            // Only keep the slope through a corner while the climb is still going; pitch left over
            // after levelling out used to leave junctions at 45 degrees.
            if (getIncline(cart) != 0
                    && (isClimbMemory(cart)
                    || hasContinuingAscendingRailAhead(cart)
                    || hasContinuingSlopeSolidAhead(cart))) {
                keepSlopeFloatThroughCorner(cart, findRailPos(cart), railState);
            } else if (getIncline(cart) != 0 || poseHasSlopePitch(cart)) {
                setIncline(cart, 0);
                float yaw = resolveFlatFloatYaw(cart, shape);
                clearFloatingPitch(cart);
                setSyncedFloatPose(cart, yaw, 0.0F);
                snapEntityRotation(cart, yaw, 0.0F);
                MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
                pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
                pose.paradiseLost$setOpenAirClimbGrace(0);
            }
            return;
        }
        // Line up with the landing before we settle, or we skim past on a cross-axis approach.
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            alignFloatTowardFlatLanding(cart, railPos, railState);
        }
        adaptFloatToFlatRailBelow(cart, railState);
    }

    /**
     * Near the seat: nudge onto a straight flat landing and face along it.
     */
    public static void alignFloatTowardFlatLanding(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        if (railPos == null || !(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (shape.isAscending() || isCornerRail(shape)) {
            return;
        }
        // Don't yank onto a deep floor flat we're meant to fly past.
        if (getIncline(cart) > 0 && isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return;
        }
        // Flat crest with more slope after it: don't flatten the pitch or incline yet.
        if (getIncline(cart) != 0 && slopePathStillContinues(cart, getIncline(cart))) {
            return;
        }
        if (isSettledOnRail(cart, railPos)) {
            return;
        }
        double height = cart.getY() - railPos.getY();
        // Only when we're about to seat.
        if (height < 0.0D || height > 0.55D) {
            return;
        }
        // Still over an ascending rail, leave flat landing alone.
        BlockPos underfoot = findRailPos(cart);
        if (underfoot != null && !underfoot.equals(railPos)) {
            BlockState underState = cart.level().getBlockState(underfoot);
            if (underState.getBlock() instanceof BaseRailBlock underRail
                    && underState.getValue(underRail.getShapeProperty()).isAscending()) {
                return;
            }
        }
        if (isParallelToRail(cart, shape)) {
            return;
        }
        // Must be over the landing cell, not still on the approach.
        if (!isOverRailCell(cart, railPos, shape)) {
            return;
        }
        nudgeVelocityToRail(cart, railState);
        // Flat landing: drop the slope pitch. Holding 45 degrees while aligning left carts
        // tilted on powered flats until they reached seat height, or even after.
        setIncline(cart, 0);
        float yaw = resolveFlatFloatYaw(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        clearFloatingPitch(cart);
        setSyncedFloatPose(cart, yaw, 0.0F);
        snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        pose.paradiseLost$setOpenAirClimbGrace(0);
        debug(cart, "flatAlign", "yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                + " pitch=0"
                + " shape=" + shape
                + " yAbove=" + String.format(Locale.ROOT, "%.3f", height)
                + " motion=" + fmt(cart.getDeltaMovement()));
    }

    /**
     * Roughly over the rail cell (within half a block sideways).
     */
    private static boolean isOverRailCell(AbstractMinecart cart, BlockPos railPos, RailShape shape) {
        double cx = railPos.getX() + 0.5D;
        double cz = railPos.getZ() + 0.5D;
        if (shape == RailShape.EAST_WEST) {
            return Math.abs(cart.getZ() - cz) <= 0.45D
                    && Math.abs(cart.getX() - cx) <= 0.75D;
        }
        if (shape == RailShape.NORTH_SOUTH) {
            return Math.abs(cart.getX() - cx) <= 0.45D
                    && Math.abs(cart.getZ() - cz) <= 0.75D;
        }
        return Math.abs(cart.getX() - cx) <= 0.55D && Math.abs(cart.getZ() - cz) <= 0.55D;
    }

    /**
     * Midair over ascending: refresh incline/Y (and pose on flip). Leaves XZ alone.
     */
    public static void adaptFloatToAscendingRail(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (!shape.isAscending()) {
            return;
        }
        int before = getIncline(cart);
        RailShape priorShape = getRailShape(cart);
        Vec3 motion = cart.getDeltaMovement();
        // Ambiguous horizontal motion: keep climb/descend, do not re-sample pose (carousel glitch).
        if (before != 0 && !hasClearAscendAxis(shape, motion)) {
            cart.setDeltaMovement(motion.x, before * motion.horizontalDistance(), motion.z);
            return;
        }
        updateInclineFromRail(cart, shape, motion);
        int incline = getIncline(cart);
        motion = cart.getDeltaMovement();
        if (incline != 0) {
            cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean flatPose = !pose.paradiseLost$hasRailRenderPose()
                || Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 1.0F;
        // Refresh when incline flips, or when we're somehow flat on a slope.
        if (incline != 0 && (before != incline || flatPose)) {
            // Force midair seat-sample: settle-gated capture no-ops high above the rail and can
            // leave the prior descending pose after an incline flip onto the opposite slope.
            float priorYaw = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderYaw() : 0.0F;
            float priorPitch = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderPitch() : 0.0F;
            captureRailRenderPose(cart, true);
            if (flatPose && !hasCredibleSlopeLaunchPitch(cart, pose)) {
                ensureAscendingFloatPose(cart, shape);
            }
            // Seat sample can still fail midair, opposite ascending rails reverse offs yaw (~180 degrees)
            // while keeping slope pitch (south -90/-45 to north +90/-45). Only when the rail
            // reversed; same-shape incline flips correctly keep the prior offs pair.
            if (isOppositeAscending(priorShape, shape)
                    && pose.paradiseLost$hasRailRenderPose()
                    && Math.abs(priorPitch) > 1.0F
                    && Math.abs(Mth.wrapDegrees(pose.paradiseLost$getRailRenderYaw() - priorYaw)) < 5.0F
                    && Math.abs(pose.paradiseLost$getRailRenderPitch() - priorPitch) < 5.0F) {
                float flippedYaw = Mth.wrapDegrees(priorYaw + 180.0F);
                float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                        ? 0.0F
                        : 90.0F;
                pose.paradiseLost$setRailRenderPose(nearestRailYaw(flippedYaw, axis), priorPitch);
                debug(cart, "ascendAdapt", "poseFlipYaw " + priorShape + "->" + shape
                        + " " + String.format(Locale.ROOT, "%.1f/%.1f", priorYaw, priorPitch)
                        + "->" + String.format(Locale.ROOT, "%.1f/%.1f",
                        pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch()));
            }
            applyFloatingRotation(cart);
            BlockPos railPos = findRailPos(cart);
            debug(cart, "ascendAdapt", "shape=" + shape
                    + " incline " + before + "->" + incline
                    + (flatPose ? " restoreFlatPose" : "")
                    + " motion=" + fmt(cart.getDeltaMovement())
                    + " railPose=" + String.format(Locale.ROOT, "%.1f/%.1f",
                    pose.paradiseLost$getRailRenderYaw(), pose.paradiseLost$getRailRenderPitch())
                    + (railPos == null ? "" : " yAbove=" + String.format(Locale.ROOT, "%.3f",
                    cart.getY() - railPos.getY())));
        }
    }

    private static boolean isOppositeAscending(@Nullable RailShape a, @Nullable RailShape b) {
        if (a == null || b == null) {
            return false;
        }
        return (a == RailShape.ASCENDING_SOUTH && b == RailShape.ASCENDING_NORTH)
                || (a == RailShape.ASCENDING_NORTH && b == RailShape.ASCENDING_SOUTH)
                || (a == RailShape.ASCENDING_EAST && b == RailShape.ASCENDING_WEST)
                || (a == RailShape.ASCENDING_WEST && b == RailShape.ASCENDING_EAST);
    }

    private static boolean hasClearAscendAxis(RailShape shape, Vec3 motion) {
        return switch (shape) {
            case ASCENDING_EAST, ASCENDING_WEST -> Math.abs(motion.x) > MOTION_EPSILON;
            case ASCENDING_NORTH, ASCENDING_SOUTH -> Math.abs(motion.z) > MOTION_EPSILON;
            default -> false;
        };
    }

    public static void adaptFloatToFlatRailBelow(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Ascending + corners: not flat landings (flat adopt on curves breaks carousel yaw).
        if (shape.isAscending() || isCornerRail(shape)) {
            return;
        }
        BlockPos railPos = findRailPos(cart);
        // Flying past a deep floor, keep slope. Near a real track flat, adopt it.
        if (railPos != null
                && getIncline(cart) > 0
                && (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart))
                && isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return;
        }
        if (railPos == null) {
            return;
        }
        // Flat crest while the slope continues (descending onto stairs and the like): don't take it yet.
        if (getIncline(cart) != 0 && slopePathStillContinues(cart, getIncline(cart))) {
            return;
        }
        double height = cart.getY() - railPos.getY();
        // Only near the seat band. Don't wipe the tilt while still climbing or launching high above.
        boolean nearSeat = isSettledOnRail(cart, railPos)
                || isNearFlatPoseAdoptHeight(cart, railPos)
                || (height >= 0.0D && height <= 0.55D);
        if (!nearSeat) {
            return;
        }
        if (getIncline(cart) != 0) {
            setIncline(cart, 0);
        }
        setRailShape(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        if (!isParallelToRail(cart, shape)) {
            nudgeVelocityToRail(cart, railState);
        }
        float yaw = resolveFlatFloatYaw(cart, shape);
        boolean alreadyFlat = Math.abs(getSyncedFloatPitch(cart)) <= 0.01F
                && (!pose.paradiseLost$hasRailRenderPose()
                || Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 0.01F)
                && isStraightRailAxisYaw(yaw, shape)
                && pose.paradiseLost$hasRailRenderPose()
                && Math.abs(Mth.wrapDegrees(pose.paradiseLost$getRailRenderYaw() - yaw)) <= 1.0F;
        if (alreadyFlat) {
            return;
        }
        clearFloatingPitch(cart);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        setSyncedFloatPose(cart, yaw, 0.0F);
        snapEntityRotation(cart, yaw, 0.0F);
        debug(cart, "flatAdapt", "pitch=0 yaw=" + String.format(java.util.Locale.ROOT, "%.1f", yaw)
                + " shape=" + shape
                + " parallel=" + isParallelToRail(cart, shape)
                + " incline=" + getIncline(cart)
                + " yAbove=" + String.format(java.util.Locale.ROOT, "%.3f", height)
                + " nearSeat=" + nearSeat
                + " motion=" + fmt(cart.getDeltaMovement()));
    }

    public static double softenSlopeDescentOntoFlat(AbstractMinecart cart, double yMotion) {
        if (yMotion >= 0.0D || !isFloating(cart)) {
            return yMotion;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos == null || !isNearFlatConnectHeight(cart, railPos)) {
            return yMotion;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return yMotion;
        }
        double height = cart.getY() - railPos.getY();
        double target = 0.0625D;
        if (height <= target) {
            return 0.0D;
        }
        double maxStep = Math.min(0.35D, height - target);
        return -Math.min(Math.abs(yMotion), maxStep);
    }

    /**
     * Climbing into a flat: rise until seat height, then stop so we don't punch through.
     */
    public static double softenSlopeAscentOntoFlat(AbstractMinecart cart, double yMotion) {
        if (yMotion <= 0.0D || !isFloating(cart)) {
            return yMotion;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            BlockPos ahead = findContinuingRailAhead(cart, false);
            if (ahead == null) {
                return yMotion;
            }
            BlockState aheadState = cart.level().getBlockState(ahead);
            if (!(aheadState.getBlock() instanceof BaseRailBlock rail)
                    || aheadState.getValue(rail.getShapeProperty()).isAscending()) {
                return yMotion;
            }
            // Deep floor under a later climb, keep going up.
            if (isDeepFlatBelowClimb(cart, ahead, aheadState)
                    && (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart))) {
                return yMotion;
            }
            // Flat track way below us isn't the landing.
            if (cart.getY() - ahead.getY() > 1.0D) {
                return yMotion;
            }
            double targetY = ahead.getY() + 0.0625D;
            if (cart.getY() >= targetY) {
                return 0.0D;
            }
            return Math.min(yMotion, targetY - cart.getY());
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return yMotion;
        }
        double target = 0.0625D;
        double height = cart.getY() - railPos.getY();
        // Deep floor underfoot, don't kill climb Y.
        if (height > 1.0D) {
            return yMotion;
        }
        if (isInterveningFlatWhileClimbing(cart, railPos, railState)) {
            return yMotion;
        }
        // Normal track flat, drop onto the seat instead of hovering above it.
        if (height > target) {
            return -Math.min(0.35D, height - target);
        }
        return Math.min(yMotion, target - height);
    }

    public static double descentSpeedOntoFlatRail(AbstractMinecart cart, BlockPos railPos) {
        if (!isFloating(cart)) {
            return 0.0D;
        }
        if (getIncline(cart) != 0) {
            return 0.0D;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return 0.0D;
        }
        if (isSettledOnRail(cart, railPos)) {
            return 0.0D;
        }
        double height = cart.getY() - railPos.getY();
        double target = 0.0625D;
        if (height <= target) {
            return 0.0D;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean slopePose = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
        boolean catchUp = !isOffRail(cart) || slopePose;
        if (!catchUp && !isNearFlatConnectHeight(cart, railPos)) {
            return 0.0D;
        }
        double cap = catchUp ? 0.50D : 0.35D;
        return -Math.min(cap, Math.max(0.10D, height - target));
    }

    public static void applyFloatingRotation(AbstractMinecart cart) {
        // Over a real flat landing, never publish 45 degrees again from a leftover incline.
        if (flattenPoseOverStraightFlat(cart)) {
            return;
        }
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
        // Incline 0 means flat pitch.
        if (getIncline(cart) == 0) {
            pitch = 0.0F;
        } else if (Math.abs(pitch) <= 1.0F) {
            // Still climbing, invent a 45 degrees if pose got wiped.
            RailShape shape = getRailShape(cart);
            if (shape != null && shape.isAscending()) {
                ensureAscendingFloatPose(cart, shape);
                if (pose.paradiseLost$hasRailRenderPose()) {
                    yaw = pose.paradiseLost$getRailRenderYaw();
                    pitch = pose.paradiseLost$getRailRenderPitch();
                }
            }
            if (Math.abs(pitch) <= 1.0F) {
                pitch = synthSlopePitch(cart, yaw);
            }
        }
        setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    private static boolean shouldKeepSlopePitch(AbstractMinecart cart) {
        // While incline is set, always keep the slope pitch.
        if (getIncline(cart) != 0) {
            return true;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = railState.getValue(rail.getShapeProperty());
                if (isCornerRail(shape)) {
                    return poseHasSlopePitch(cart);
                }
                if (!shape.isAscending()) {
                    // Incline already 0, stay tilted until we're in the adopt band.
                    return poseHasSlopePitch(cart) && !isNearFlatPoseAdoptHeight(cart, railPos);
                }
            }
        }
        return false;
    }

    private static boolean poseHasSlopePitch(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        return pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
    }

    public static void applySyncedFloatingRotation(AbstractMinecart cart) {
        if (!isFloating(cart)) {
            return;
        }
        float yaw = getSyncedFloatYaw(cart);
        float pitch = getSyncedFloatPitch(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean localSlope = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
        BlockPos railPos = findRailPos(cart);
        boolean settled = railPos != null && isSettledOnRail(cart, railPos);
        // The climb incline got synced while the cart is still seated on a flat, so level the pitch.
        if (cart.level().isClientSide() && getIncline(cart) != 0 && !clientShouldHoldSlopePose(cart)) {
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail) {
                    RailShape shape = railState.getValue(rail.getShapeProperty());
                    if (!shape.isAscending() && !isCornerRail(shape)) {
                        yaw = resolveFlatFloatYaw(cart, shape);
                    }
                }
            }
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            return;
        }
        if (getIncline(cart) == 0) {
            // Incline already cleared on the server, keep local tilt until we actually seat.
            if (!settled && shouldKeepMidairSlopePose(cart, railPos)) {
                if (localSlope) {
                    yaw = pose.paradiseLost$getRailRenderYaw();
                    pitch = pose.paradiseLost$getRailRenderPitch();
                } else if (Math.abs(pitch) <= 1.0F) {
                    pitch = 0.0F;
                }
            } else {
                pitch = 0.0F;
            }
        } else if (Math.abs(pitch) <= 1.0F) {
            // Sync pitch lagging / missing while incline is live, never write flat over a slope.
            if (localSlope && isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            } else {
                RailShape shape = getRailShape(cart);
                if (shape != null && shape.isAscending()) {
                    ensureAscendingFloatPose(cart, shape);
                    if (pose.paradiseLost$hasRailRenderPose()) {
                        yaw = pose.paradiseLost$getRailRenderYaw();
                        pitch = pose.paradiseLost$getRailRenderPitch();
                    }
                }
                if (Math.abs(pitch) <= 1.0F) {
                    pitch = synthSlopePitch(cart, yaw);
                }
                // Sync yaw defaults to 0 (east), realign to motion or we sit 90 degrees off on N/S slopes.
                if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
                    yaw = slopeYawFromMotionOrPose(cart);
                    if (Math.abs(Math.abs(pitch) - 45.0F) < 8.0F) {
                        pitch = synthSlopePitch(cart, yaw);
                    }
                }
            }
        } else if (!isSlopeYawAlignedWithMotion(cart, yaw)) {
            // Sync pitch ok but yaw still the east default, fix axis without flattening.
            yaw = slopeYawFromMotionOrPose(cart);
            if (localSlope
                    && Math.abs(Math.abs(pose.paradiseLost$getRailRenderPitch()) - 45.0F) < 5.0F
                    && isSlopeYawAlignedWithMotion(cart, pose.paradiseLost$getRailRenderYaw())) {
                yaw = pose.paradiseLost$getRailRenderYaw();
                pitch = pose.paradiseLost$getRailRenderPitch();
            }
        }
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    /**
     * True when we should keep floating instead of hard-seating on this flat/corner. Deep floors mid-climb get skipped;
     * normal track flats still seat.
     */
    public static boolean shouldDeferSlopeToCornerHardSeat(
            AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        if (shape.isAscending()) {
            return false;
        }
        boolean slopeLive = getIncline(cart) != 0 || poseHasSlopePitch(cart) || isClimbMemory(cart);
        if (!slopeLive) {
            return false;
        }
        // Only defer while actively climbing, descents seat on flats normally.
        if (getIncline(cart) > 0
                && (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart))
                && isInterveningFlatWhileClimbing(cart, pos, state)) {
            return true;
        }
        if (!isCornerRail(shape)) {
            return false;
        }
        // Corners: defer only while still above the seat band.
        return cart.getY() - pos.getY() > FLAT_CONNECT_HEIGHT;
    }

    /**
     * Flat we should fly past while climbing (deep below, or through-flat under a higher lip). Same-level track flats
     * are not intervening.
     */
    private static boolean isInterveningFlatWhileClimbing(
            AbstractMinecart cart, BlockPos pos, BlockState state) {
        if (isDeepFlatBelowClimb(cart, pos, state)) {
            return true;
        }
        double height = cart.getY() - pos.getY();
        // Close enough to seat, this is real track.
        if (height <= FLAT_CONNECT_HEIGHT) {
            return false;
        }
        // High above a through-flat with a higher lip beyond it.
        return hasHigherClimbTarget(cart, pos);
    }

    /**
     * Keep slope pose/incline while we're deferring a flat/corner seat.
     */
    public static void keepSlopeFloatThroughCorner(AbstractMinecart cart, BlockPos pos, BlockState state) {
        RailShape remembered = getRailShape(cart);
        if (remembered != null && remembered.isAscending()) {
            if (getIncline(cart) == 0) {
                setIncline(cart, cart.getDeltaMovement().y < -MOTION_EPSILON ? -1 : 1);
            }
            ensureAscendingFloatPose(cart, remembered);
            applyFloatingRotation(cart);
            return;
        }
        if (poseHasSlopePitch(cart)) {
            applyFloatingRotation(cart);
        }
    }

    /**
     * Once incline is 0, leftover tilt is only kept while the cart is unsettled over a curve during an active climb
     * (ascending memory or a slope that continues). Settled corners and carts that have already levelled out have to
     * drop the pitch, or junctions stay at 45 degrees.
     */
    private static boolean shouldKeepMidairSlopePose(AbstractMinecart cart, @Nullable BlockPos railPos) {
        if (getIncline(cart) != 0) {
            return true;
        }
        // Already leveled, don't resurrect tilt in empty air.
        if (railPos == null) {
            return false;
        }
        if (isSettledOnRail(cart, railPos)) {
            return false;
        }
        // Stored flat means we already adopted a landing, don't keep slope pitch from underfoot ascent.
        RailShape stored = getRailShape(cart);
        if (stored == RailShape.NORTH_SOUTH || stored == RailShape.EAST_WEST) {
            return false;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        // Curves only while a climb is still in progress, not after levelFlight or a flat reconnect.
        return isCornerRail(shape)
                && poseHasSlopePitch(cart)
                && (isClimbMemory(cart)
                || hasContinuingAscendingRailAhead(cart)
                || hasContinuingSlopeSolidAhead(cart));
    }

    private static final Map<AbstractMinecart, float[]> CLIENT_MOTION_POSE = new WeakHashMap<>();

    /**
     * Client only. The server pose (incline, synced yaw/pitch) arrives several ticks before the lerped client cart gets
     * there, so drawing it makes the cart turn or tilt early. This works from what the client cart is doing instead:
     * close to a rail it follows that rail, otherwise it follows its own movement. Null while the cart is standing
     * still, then the normal pose logic applies.
     */
    @Nullable
    private static float[] clientMotionPose(AbstractMinecart cart) {
        if (!cart.level().isClientSide()) {
            return null;
        }
        double dx = cart.getX() - cart.xOld;
        double dy = cart.getY() - cart.yOld;
        double dz = cart.getZ() - cart.zOld;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        // xOld is stale while the client isn't ticking entities, so velocity has to agree too.
        if (horizontal < 0.04D || cart.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4D) {
            CLIENT_MOTION_POSE.remove(cart);
            return null;
        }
        float travelYaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI);
        float yaw = snapYawToAxis(travelYaw);
        float[] previous = CLIENT_MOTION_POSE.get(cart);
        if (previous != null && !isNearAxis(travelYaw)) {
            // Lerping across a turn looks diagonal for a few ticks, so keep the facing it had.
            yaw = previous[0];
        }
        float pitch;
        RailShape near = clientRailShapeNearUnderfoot(cart);
        if (near == null) {
            double ratio = dy / horizontal;
            pitch = ratio > 0.3D ? 44.9F : (ratio < -0.3D ? -44.9F : 0.0F);
        } else if (near.isAscending()) {
            float climbYaw = yawFromAscendingShape(near, 1);
            boolean towardHighEnd = Math.abs(Mth.wrapDegrees(travelYaw - climbYaw)) <= 90.0F;
            yaw = towardHighEnd ? climbYaw : Mth.wrapDegrees(climbYaw + 180.0F);
            pitch = towardHighEnd ? 44.9F : -44.9F;
        } else if (isCornerRail(near)) {
            pitch = 0.0F;
        } else {
            // The lerp keeps sinking the cart onto a flat rail for a few ticks after it connects. That isn't a dive.
            boolean eastWest = near == RailShape.EAST_WEST;
            double along = eastWest ? dx : dz;
            yaw = eastWest ? (along >= 0.0D ? 0.0F : 180.0F) : (along >= 0.0D ? 90.0F : -90.0F);
            pitch = 0.0F;
        }
        float[] last = CLIENT_MOTION_POSE.computeIfAbsent(cart, c -> new float[2]);
        last[0] = yaw;
        last[1] = pitch;
        return last;
    }

    private static boolean isNearAxis(float yaw) {
        return Math.abs(Mth.wrapDegrees(yaw - Math.round(yaw / 90.0F) * 90.0F)) <= 30.0F;
    }

    private static float snapYawToAxis(float yaw) {
        float nearestAxis = Math.round(yaw / 90.0F) * 90.0F;
        return Math.abs(Mth.wrapDegrees(yaw - nearestAxis)) <= 30.0F ? Mth.wrapDegrees(nearestAxis) : yaw;
    }

    /**
     * Shape of a rail the client cart is sitting on or just above, or null when it is clear of rails.
     */
    @Nullable
    private static RailShape clientRailShapeNearUnderfoot(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        for (BlockPos at : new BlockPos[]{pos, pos.below()}) {
            BlockState state = cart.level().getBlockState(at);
            if (!(state.getBlock() instanceof BaseRailBlock rail)) {
                continue;
            }
            RailShape shape = state.getValue(rail.getShapeProperty());
            double height = cart.getY() - at.getY();
            double limit = shape.isAscending() ? ASCENDING_SEAT_HEIGHT : 0.8D;
            if (height >= -0.1D && height <= limit) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Yaw/pitch for MinecartRenderer when getPos is skipped (atan2 space, 0=east).
     */
    public static float getFloatRenderYaw(AbstractMinecart cart) {
        float[] seen = clientMotionPose(cart);
        if (seen != null) {
            return seen[0];
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float yaw = pose.paradiseLost$hasRailRenderPose()
                ? pose.paradiseLost$getRailRenderYaw()
                : getSyncedFloatYaw(cart);
        // Sync/default yaw is 0=east; N/S slopes need +/-90 or mid-air looks 90 degrees off.
        if ((getIncline(cart) != 0 || isClimbMemory(cart) || isOffRail(cart))
                && !isSlopeYawAlignedWithMotion(cart, yaw)) {
            return slopeYawFromMotionOrPose(cart);
        }
        return yaw;
    }

    /**
     * If the cart is in the seat band over a straight flat that isn't a floor in the middle of a climb, clear the
     * incline and pitch. Returns true if it dealt with the pose.
     */
    private static boolean flattenPoseOverStraightFlat(AbstractMinecart cart) {
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState state = cart.level().getBlockState(railPos);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape under = state.getValue(rail.getShapeProperty());
        if (under.isAscending() || isCornerRail(under)) {
            return false;
        }
        double height = cart.getY() - railPos.getY();
        boolean nearSeat = isSettledOnRail(cart, railPos)
                || isNearFlatPoseAdoptHeight(cart, railPos)
                || (height >= 0.0D && height <= 0.55D);
        if (!nearSeat) {
            return false;
        }
        int incline = getIncline(cart);
        if (incline > 0 && isInterveningFlatWhileClimbing(cart, railPos, state)) {
            return false;
        }
        // Flat crest before a descending run, or a climb with more slope ahead: keep the tilt.
        if (incline != 0 && slopePathStillContinues(cart, incline)) {
            return false;
        }
        if (incline == 0
                && Math.abs(getSyncedFloatPitch(cart)) <= 0.01F
                && !poseHasSlopePitch(cart)) {
            return false;
        }
        setIncline(cart, 0);
        setRailShape(cart, under);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        float yaw = resolveFlatFloatYaw(cart, under);
        clearFloatingPitch(cart);
        setSyncedFloatPose(cart, yaw, 0.0F);
        snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        return true;
    }

    public static float getFloatRenderPitch(AbstractMinecart cart) {
        float[] seen = clientMotionPose(cart);
        if (seen != null) {
            return seen[1];
        }
        BlockPos railPos = findRailPos(cart);
        boolean settled = railPos != null && isSettledOnRail(cart, railPos);
        // The climb incline is synced while the cart is still on the flat, so don't draw 45 degrees yet.
        if (cart.level().isClientSide() && getIncline(cart) != 0 && !clientShouldHoldSlopePose(cart)) {
            return 0.0F;
        }
        float pitch = effectiveFloatPitch(cart);
        float yaw = getFloatRenderYaw(cart);
        RailShape shape = getRailShape(cart);
        // Also reject wrong-sign 45 degrees for this shape.
        boolean pitchOk = Math.abs(Math.abs(pitch) - 45.0F) < 8.0F
                && isSlopeYawAlignedWithMotion(cart, yaw)
                && (shape == null || !shape.isAscending()
                || Math.abs(pitch - pitchFromAscendingShape(shape, yaw)) <= 10.0F);
        // Keep the slope in mid-air even if the incline already synced to 0 (curves only).
        if (!settled && shouldKeepMidairSlopePose(cart, railPos) && pitchOk) {
            return pitch;
        }
        // Only while the incline is live. Climb memory alone tilted ceiling and flat takeoffs again.
        if (getIncline(cart) != 0) {
            if (pitchOk) {
                return pitch;
            }
            if (shape != null && shape.isAscending()) {
                return pitchFromAscendingShape(shape, yaw);
            }
            return synthSlopePitch(cart, yaw);
        }
        // The incline is already flat, so never keep a leftover 45 degrees (it used to stick to one direction in open air).
        return 0.0F;
    }

    /**
     * Client: hold 45 degrees while the incline is live on a slope path. A straight flat underfoot drops the tilt only
     * if it's a real landing (seat band, no slope continuing), not the flat crest before a descending run and not when
     * the cart is still high above a flat during a launch or gap.
     */
    public static boolean clientShouldHoldSlopePose(AbstractMinecart cart) {
        int incline = getIncline(cart);
        if (incline == 0) {
            return false;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState state = cart.level().getBlockState(railPos);
            if (state.getBlock() instanceof BaseRailBlock rail) {
                RailShape under = state.getValue(rail.getShapeProperty());
                if (under.isAscending() || isCornerRail(under)) {
                    return true;
                }
                // Straight flat underfoot.
                if (incline > 0 && isInterveningFlatWhileClimbing(cart, railPos, state)) {
                    return true;
                }
                // There is still slope ahead on the way down or up, so keep the tilt on the flat crest.
                if (slopePathStillContinues(cart, incline)) {
                    return true;
                }
                double height = cart.getY() - railPos.getY();
                boolean nearSeat = isSettledOnRail(cart, railPos)
                        || isNearFlatPoseAdoptHeight(cart, railPos)
                        || (height >= 0.0D && height <= 0.55D);
                // In the seat band of a real flat landing there is no slope tilt, still high over a flat cell
                // (launch or gap) the slope is kept.
                return !nearSeat;
            }
        }
        // Open air with no rail: keep the slope for the climb or descent.
        return true;
    }

    /**
     * True while an ascending rail, a solid slope or an open-air target is still ahead of the incline.
     */
    private static boolean slopePathStillContinues(AbstractMinecart cart, int incline) {
        if (hasContinuingAscendingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart)) {
            return true;
        }
        if (incline < 0) {
            return shouldKeepOpenAirDescent(cart) || hasOpenAirSlopeTarget(cart);
        }
        if (incline > 0) {
            return isBelowContinuingFlatLanding(cart)
                    || ((MinecartFloatPoseAccess) cart).paradiseLost$getOpenAirClimbGrace() > 0;
        }
        return false;
    }

    /**
     * Stored float pitch for an ascending shape and the travel yaw (atan2, 0 = east). Climbing (toward the high end) is
     * always +44.9, which matches the flipped E/W getPosOffs. The old per-shape offsYaw table gave N/S climbs the
     * opposite sign from E/W.
     */
    private static float pitchFromAscendingShape(RailShape shape, float yaw) {
        float climbYaw = yawFromAscendingShape(shape, 1);
        boolean towardHighEnd = Math.abs(Mth.wrapDegrees(yaw - climbYaw)) <= 90.0F;
        return towardHighEnd ? 44.9F : -44.9F;
    }

    private static final float MAX_RENDER_YAW_STEP_PER_TICK = 40.0F;
    private static final Map<AbstractMinecart, RenderYawFilter> RENDER_YAW_FILTERS = new WeakHashMap<>();

    /**
     * Limits how fast the drawn yaw can change. The pose source flips between vanilla and ours around curves and
     * slope ends, and each source can disagree by tens of degrees for a tick, which shows up as the cart snapping
     * round. Following the wanted yaw at a bounded rate turns those snaps into a short smooth turn.
     * <p>
     * The cart looks the same from both ends, so a pose can also be drawn as yaw + 180 with the pitch negated. The
     * filter takes whichever of the two is closer to what is on screen, and {@link #pitchSign()} says which it took.
     * Without that, a climb would be drawn tilted down the slope whenever the two sources disagree about direction.
     */
    public static final class RenderYawFilter {
        private float yaw;
        private float time;
        private boolean set;
        private float pitchSign = 1.0F;

        public float apply(float wanted, float time) {
            if (!set || time < this.time - 0.001F || time - this.time > 2.0F) {
                return reset(wanted, time);
            }
            float dt = time - this.time;
            if (dt <= 0.0001F) {
                return yaw;
            }
            float direct = Mth.wrapDegrees(wanted - yaw);
            float flipped = Mth.wrapDegrees(wanted + 180.0F - yaw);
            float diff;
            if (Math.abs(flipped) < Math.abs(direct)) {
                diff = flipped;
                pitchSign = -1.0F;
            } else {
                diff = direct;
                pitchSign = 1.0F;
            }
            float limit = MAX_RENDER_YAW_STEP_PER_TICK * dt;
            yaw = Mth.wrapDegrees(yaw + Mth.clamp(diff, -limit, limit));
            this.time = time;
            return yaw;
        }

        public float pitchSign() {
            return pitchSign;
        }

        private float reset(float wanted, float time) {
            yaw = wanted;
            this.time = time;
            set = true;
            pitchSign = 1.0F;
            return yaw;
        }
    }

    /**
     * Client only. How much of the full slope tilt vanilla would draw at this spot, 0 for flat to 1 for a full slope,
     * or null when no rail is there. Vanilla looks 0.3 ahead and behind on the rail, so it eases into and out of a
     * slope over about half a block. The float pose is either flat or a full 45 degrees, so it was drawn up to
     * a few ticks late at the end of a slope. The client cart also trails the rail height a little while it
     * catches up, so the lookup is retried a bit higher when nothing is found.
     */
    @Nullable
    public static Float railSlopeScale(AbstractMinecart cart, double x, double y, double z) {
        if (!cart.level().isClientSide()) {
            return null;
        }
        // Float carts have getPos switched off while they are off the rail, this needs the real lookup.
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            for (double lift = 0.0D; lift <= 0.61D; lift += 0.3D) {
                double yy = y + lift;
                BlockPos railAt = BlockPos.containing(x, yy, z);
                if (!BaseRailBlock.isRail(cart.level().getBlockState(railAt))
                        || y - railAt.getY() < -0.7D || y - railAt.getY() > 0.9D) {
                    continue;
                }
                Vec3 at = cart.getPos(x, yy, z);
                if (at == null) {
                    continue;
                }
                Vec3 a = cart.getPosOffs(x, yy, z, 0.3F);
                Vec3 b = cart.getPosOffs(x, yy, z, -0.3F);
                if (a == null) {
                    a = at;
                }
                if (b == null) {
                    b = at;
                }
                double run = Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.z - a.z) * (b.z - a.z));
                double rise = Math.abs(b.y - a.y);
                if (run < 1.0E-4D && rise < 1.0E-4D) {
                    return null;
                }
                return (float) Math.min(1.0D, rise / Math.max(run, 1.0E-4D));
            }
            return null;
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    /**
     * -1 while the smoothed yaw is drawn the other way round (see {@link RenderYawFilter}), so the pitch has to flip.
     */
    public static float smoothRenderPitchSign(AbstractMinecart cart) {
        if (!cart.level().isClientSide() || !isFloating(cart)) {
            return 1.0F;
        }
        RenderYawFilter filter = RENDER_YAW_FILTERS.get(cart);
        return filter == null ? 1.0F : filter.pitchSign();
    }

    /**
     * Render yaw in degrees as given to the renderer (180 minus the atan2 yaw), smoothed per cart.
     */
    public static float smoothRenderYawDegrees(AbstractMinecart cart, float wantedDegrees, float partialTicks) {
        if (!cart.level().isClientSide() || !isFloating(cart)) {
            RENDER_YAW_FILTERS.remove(cart);
            return wantedDegrees;
        }
        return RENDER_YAW_FILTERS.computeIfAbsent(cart, c -> new RenderYawFilter())
                .apply(wantedDegrees, cart.tickCount + partialTicks);
    }

    /**
     * Client only. True while the cart sits on a curve that vanilla would pick up by itself. The server incline for the
     * slope next to the curve arrives early, and our axis-snapped yaw would turn the cart before the curve does.
     */
    private static boolean isRidingCurveLikeVanilla(AbstractMinecart cart) {
        if (!cart.level().isClientSide() || isOffRail(cart)) {
            return false;
        }
        double x = cart.getX();
        double y = cart.getY();
        double z = cart.getZ();
        if (isCurveCell(cart, x, y, z)) {
            return true;
        }
        // Vanilla samples the rail 0.3 ahead and behind, so it starts turning before the curve cell.
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            Vec3 ahead = cart.getPosOffs(x, y, z, 0.3F);
            Vec3 behind = cart.getPosOffs(x, y, z, -0.3F);
            return (ahead != null && isCurveCell(cart, ahead.x, ahead.y, ahead.z))
                    || (behind != null && isCurveCell(cart, behind.x, behind.y, behind.z));
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    private static boolean isCurveCell(AbstractMinecart cart, double x, double y, double z) {
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        for (int dy = 0; dy >= -1; dy--) {
            BlockState state = cart.level().getBlockState(new BlockPos(i, j + dy, k));
            if (state.getBlock() instanceof BaseRailBlock rail
                    && isCornerRail(state.getValue(rail.getShapeProperty()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldUseFloatRenderPose(AbstractMinecart cart) {
        if (!isFloating(cart)) {
            return false;
        }
        if (isRidingCurveLikeVanilla(cart)) {
            return false;
        }
        // The client cart is visibly climbing or diving, whatever the synced incline says right now.
        float[] seen = clientMotionPose(cart);
        if (seen != null && (Math.abs(seen[1]) > 1.0F || isOffRail(cart))) {
            return true;
        }
        BlockPos railPos = findRailPos(cart);
        // Settled on a flat or corner with the incline cleared: vanilla pose, even if the pitch sync is behind.
        if (getIncline(cart) == 0 && railPos != null && isSettledOnRail(cart, railPos)) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail
                    && !railState.getValue(rail.getShapeProperty()).isAscending()) {
                return false;
            }
        }
        // Slope float always uses our pose, vanilla getPos flashes flat on reconnect.
        if (getIncline(cart) != 0
                || isClimbMemory(cart)
                || Math.abs(effectiveFloatPitch(cart)) > 1.0F) {
            return true;
        }
        return railPos == null || !isSettledOnRail(cart, railPos);
    }

    private static float effectiveFloatPitch(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        float sync = getSyncedFloatPitch(cart);
        if (pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F) {
            float local = pose.paradiseLost$getRailRenderPitch();
            RailShape shape = getRailShape(cart);
            if (shape == null || !shape.isAscending()) {
                return local;
            }
            float yaw = pose.paradiseLost$getRailRenderYaw();
            float expected = pitchFromAscendingShape(shape, yaw);
            if (Math.abs(local - expected) <= 10.0F) {
                return local;
            }
            // Local flipped vs shape, use sync if it matches.
            if (Math.abs(sync) > 1.0F && Math.abs(sync - expected) <= 10.0F) {
                return sync;
            }
            return expected;
        }
        return sync;
    }

    /**
     * Float / MinecartRenderer yaw uses atan2(dz, dx): 0=east, 90=south, 180=west, -90=north. Do NOT use
     * {@link Vec3#directionFromRotation} here, that is entity look (0=south).
     */
    private static float yawAlongMotion(AbstractMinecart cart, float railYaw) {
        Vec3 motion = horizontalTravelVector(cart);
        float a = Mth.wrapDegrees(railYaw);
        float b = Mth.wrapDegrees(railYaw + 180.0F);
        if (motion.lengthSqr() < 1.0E-6D) {
            RailShape shape = getRailShape(cart);
            if (shape != null && shape.isAscending()) {
                return nearestRailYaw(yawFromAscendingShape(shape, getIncline(cart)), railYaw);
            }
            return nearestRailYaw(entityYawToRailYaw(cart.getYRot()), railYaw);
        }
        motion = motion.normalize();
        Vec3 dirA = railYawDirection(a);
        Vec3 dirB = railYawDirection(b);
        double dotA = dirA.x * motion.x + dirA.z * motion.z;
        double dotB = dirB.x * motion.x + dirB.z * motion.z;
        return dotA >= dotB ? a : b;
    }

    /**
     * Unit vector for MinecartRenderer/atan2 yaw (0=east).
     */
    private static Vec3 railYawDirection(float railYawDegrees) {
        double rad = Math.toRadians(railYawDegrees);
        return new Vec3(Math.cos(rad), 0.0D, Math.sin(rad));
    }

    /**
     * yRot is already renderer/atan2 space (0=east). Don't add +90 (that's look-yaw space).
     */
    private static float entityYawToRailYaw(float entityYaw) {
        return Mth.wrapDegrees(entityYaw);
    }

    private static float nearestRailYaw(float currentYaw, float railYaw) {
        float a = Mth.wrapDegrees(railYaw);
        float b = Mth.wrapDegrees(railYaw + 180.0F);
        return Math.abs(Mth.wrapDegrees(a - currentYaw)) <= Math.abs(Mth.wrapDegrees(b - currentYaw))
                ? a
                : b;
    }

    private static boolean isStraightRailAxisYaw(float yaw, RailShape shape) {
        float y = Mth.wrapDegrees(yaw);
        if (shape == RailShape.EAST_WEST) {
            return Math.abs(y) <= 1.0F || Math.abs(Math.abs(y) - 180.0F) <= 1.0F;
        }
        if (shape == RailShape.NORTH_SOUTH) {
            return Math.abs(Math.abs(y) - 90.0F) <= 1.0F;
        }
        return false;
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

    private static void snapEntityRotation(AbstractMinecart cart, float yaw, float pitch) {
        applyEntityRotation(cart, yaw, pitch, true);
    }

    /**
     * Snap entity rotation in MinecartRenderer offs space (0=east). Don't convert to look-yaw , that makes a correct
     * slope look diagonal.
     */
    private static void applyEntityRotation(AbstractMinecart cart, float railYaw, float pitch, boolean hardSnap) {
        float yaw = Mth.wrapDegrees(railYaw);
        cart.setYRot(yaw);
        cart.setXRot(pitch);
        cart.xRotO = pitch;
        if (hardSnap) {
            cart.yRotO = yaw;
        }
    }

    public static boolean shouldSkipRailRenderSnap(AbstractMinecart cart) {
        if (POS_CAPTURE_BYPASS.get() > 0) {
            return false;
        }
        if (!isFloating(cart)) {
            return false;
        }
        // Off-rail: never let getPos sample a nearby rail, crest/gap offs are often ~90 degrees wrong.
        if (isOffRail(cart)) {
            return true;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos != null && isMidairAboveRail(cart, railPos, false)) {
            return true;
        }
        if (railPos != null && isSettledOnRail(cart, railPos)) {
            return false;
        }
        // Slope float, don't let getPos sample a floor rail mid-gap.
        if (getIncline(cart) != 0 || isClimbMemory(cart) || Math.abs(effectiveFloatPitch(cart)) > 1.0F) {
            return true;
        }
        return isUnsettledAboveFlatRail(cart);
    }

    public static boolean isUnsettledAboveFlatRail(AbstractMinecart cart) {
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            return false;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !isSettledOnRail(cart, railPos);
    }

    public static boolean shouldCancelTrackWhileFloating(
            AbstractMinecart cart, BlockPos railPos, BlockState railState, boolean leftRails) {
        if (isMidairAboveRail(cart, railPos, leftRails)) {
            return true;
        }
        if (!isFloating(cart)) {
            return false;
        }
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isAscending()) {
            return false;
        }
        return !isSettledOnRail(cart, railPos);
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
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        setSyncedFloatPose(cart, yaw, pitch);
        snapEntityRotation(cart, yaw, pitch);
    }

    /**
     * Push captured slope pose to sync without fighting vanilla on-rail entity rotation.
     */
    public static void publishSlopePoseToSync(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        setSyncedFloatPose(cart,
                pose.paradiseLost$getRailRenderYaw(),
                pose.paradiseLost$getRailRenderPitch());
    }

    public static void clearFloatingPitch(AbstractMinecart cart) {
        cart.setXRot(0.0F);
        cart.xRotO = 0.0F;
    }

    /**
     * Level out: keep xz, zero dy, clear slope. Used against solid with no rail to seat on.
     */
    public static void levelOpenAirFlight(AbstractMinecart cart) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x, 0.0D, motion.z);
        setIncline(cart, 0);
        clearFloatingPitch(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        pose.paradiseLost$setOpenAirClimbGrace(0);
        // Prefer the flat underfoot over a remembered approach shape for yaw.
        RailShape shape = getRailShape(cart);
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape under = railState.getValue(rail.getShapeProperty());
                if (!under.isAscending() && !isCornerRail(under)) {
                    shape = under;
                    setRailShape(cart, under);
                    if (!isParallelToRail(cart, under)) {
                        nudgeVelocityToRail(cart, railState);
                    }
                }
            }
        }
        // Clear the ascending climb memory, or the next flat takeoff brings the 45 degrees back.
        if (shape != null && shape.isAscending()) {
            setRailShape(cart, "");
            shape = null;
        }
        float yaw = resolveFlatFloatYaw(cart, shape);
        setSyncedFloatPose(cart, yaw, 0.0F);
        snapEntityRotation(cart, yaw, 0.0F);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        debug(cart, "levelFlight", "pitch=0 dy=0 yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                + " shape=" + shape
                + " motion=" + fmt(cart.getDeltaMovement()));
    }

    /**
     * Pressed against solid with no rail to seat on. Skipped while a landing/stair is still ahead.
     */
    public static boolean shouldLevelAgainstSolidBarrier(AbstractMinecart cart, boolean onGround, boolean verticalCollision) {
        if (!isFloating(cart)) {
            return false;
        }
        if (!onGround && !verticalCollision) {
            return false;
        }
        // A cart that has stopped has nothing to flatten out of.
        if (cart.getDeltaMovement().horizontalDistanceSqr() < MOTION_EPSILON * MOTION_EPSILON) {
            return false;
        }
        if (findRailPos(cart) != null) {
            return false;
        }
        // Still climbing into a real landing, don't level out.
        // Do not key off climb-memory alone: remembered ascending shape after the rails end
        // (ceiling / floor skim / top of a solid stair run) must still flatten.
        if (isBelowContinuingFlatLanding(cart)) {
            return false;
        }
        // Only level into dead solid, not a stair skim.
        return !hasContinuingRailAhead(cart) && !hasContinuingSlopeSolidAhead(cart);
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
        // Under the rail is never seated.
        if (height < 0.0D) {
            return false;
        }
        if (railState.getValue(rail.getShapeProperty()).isAscending()) {
            // Ascending seat Y spans ~0..1 within the rail cell. 0.45 only covers the low end.
            return height <= ASCENDING_SEAT_HEIGHT;
        }
        // A diving cart drops this far in its next tick. If it had to be within 0.16 to count as seated, it could step right
        // over a one-block rail across its path (a perpendicular landing) without ever being counted as seated.
        double limit = 0.16D;
        Vec3 motion = cart.getDeltaMovement();
        if (isFloating(cart) && motion.y < -0.05D) {
            limit = Math.max(limit, -motion.y + 0.02D);
        }
        return height <= limit;
    }

    @Nullable
    public static BlockPos findRailPos(AbstractMinecart cart) {
        BlockPos pos = cart.blockPosition();
        boolean climbFloat = isFloating(cart) && getIncline(cart) > 0;
        BlockState at = cart.level().getBlockState(pos);
        if (BaseRailBlock.isRail(at)) {
            // High above a through-flat with a higher target, ignore it. Otherwise it's the landing.
            if (!(climbFloat && isFlatStraightRail(at) && cart.getY() - pos.getY() > 0.45D
                    && hasHigherClimbTarget(cart, pos))) {
                return pos;
            }
        }
        BlockPos below = pos.below();
        BlockState belowState = cart.level().getBlockState(below);
        if (BaseRailBlock.isRail(belowState)) {
            // Ignore rails far below so a floor track can't kill climb Y.
            // Ascending underfoot is different: seat Y spans ~0..1 in the same cell.
            if (climbFloat) {
                double height = cart.getY() - below.getY();
                boolean ascendingBelow = belowState.getBlock() instanceof BaseRailBlock belowRail
                        && belowState.getValue(belowRail.getShapeProperty()).isAscending();
                if (ascendingBelow ? height <= ASCENDING_SEAT_HEIGHT : height <= 0.45D) {
                    return below;
                }
            } else {
                return below;
            }
        }
        // Might be under a pier, look up a couple blocks.
        if (climbFloat) {
            for (int dy = 1; dy <= 2; dy++) {
                BlockPos above = pos.above(dy);
                if (BaseRailBlock.isRail(cart.level().getBlockState(above))) {
                    return above;
                }
            }
        }
        return null;
    }

    /**
     * Higher ascending beyond this flat. Don't call findRailPos from here.
     */
    private static boolean hasHigherClimbTarget(AbstractMinecart cart, BlockPos flatPos) {
        var level = cart.level();
        if (BaseRailBlock.isRail(level.getBlockState(flatPos.above()))) {
            return true;
        }
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            return false;
        }
        int dx;
        int dz;
        if (Math.abs(motion.x) >= Math.abs(motion.z)) {
            dx = motion.x > 0.0D ? 1 : -1;
            dz = 0;
        } else {
            dx = 0;
            dz = motion.z > 0.0D ? 1 : -1;
        }
        for (int step = 1; step <= 4; step++) {
            // dy from 1, same-level ascending later isn't a "higher" target.
            for (int dy = 1; dy <= 3; dy++) {
                BlockState state = level.getBlockState(flatPos.offset(dx * step, dy, dz * step));
                if (state.getBlock() instanceof BaseRailBlock rail
                        && state.getValue(rail.getShapeProperty()).isAscending()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFlatStraightRail(BlockState state) {
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return false;
        }
        RailShape shape = state.getValue(rail.getShapeProperty());
        return !shape.isAscending() && !isCornerRail(shape);
    }

    public static boolean isMidairAboveRail(AbstractMinecart cart, BlockPos railPos, boolean alreadyLeftRails) {
        if (!isFloating(cart)) {
            return false;
        }
        double height = cart.getY() - railPos.getY();
        // Still under the rail.
        if (height < 0.0D) {
            return alreadyLeftRails || getIncline(cart) != 0 || isClimbMemory(cart);
        }
        BlockState railState = cart.level().getBlockState(railPos);
        boolean ascendingUnder = railState.getBlock() instanceof BaseRailBlock rail
                && railState.getValue(rail.getShapeProperty()).isAscending();
        // High above underfoot rail, treat as midair even if offRail hasn't synced.
        boolean slopeFloat = getIncline(cart) != 0 || isClimbMemory(cart) || poseHasSlopePitch(cart);
        if (!alreadyLeftRails && !slopeFloat) {
            return false;
        }
        if (ascendingUnder) {
            // On-rail climb uses the full ~0..1 seat curve; only treat as midair above the crest.
            return height > ASCENDING_SEAT_HEIGHT;
        }
        return !isSettledOnRail(cart, railPos);
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
    private static BlockPos findContinuingRailAhead(AbstractMinecart cart, boolean ascendingOnly) {
        BlockPos underfoot = findRailPos(cart);
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
        if (motion.horizontalDistanceSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            if (Math.abs(motion.x) >= Math.abs(motion.z)) {
                dx = motion.x > 0.0D ? 1 : -1;
            } else {
                dz = motion.z > 0.0D ? 1 : -1;
            }
        } else {
            // Float yaw is 0=east, not look-yaw (Direction.fromYRot would face the wrong way).
            float railYaw = getSyncedFloatYaw(cart);
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                railYaw = pose.paradiseLost$getRailRenderYaw();
            }
            Vec3 along = railYawDirection(railYaw);
            if (along.lengthSqr() > 1.0E-8D) {
                if (Math.abs(along.x) >= Math.abs(along.z)) {
                    dx = along.x > 0.0D ? 1 : -1;
                } else {
                    dz = along.z > 0.0D ? 1 : -1;
                }
            } else {
                RailShape shape = getRailShape(cart);
                if (shape != null) {
                    Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
                    if (exits != null) {
                        Vec3i upper = exits.getFirst().getY() >= exits.getSecond().getY()
                                ? exits.getFirst() : exits.getSecond();
                        Vec3i lower = exits.getFirst().getY() < exits.getSecond().getY()
                                ? exits.getFirst() : exits.getSecond();
                        Vec3i pick = getIncline(cart) < 0 ? lower : upper;
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
        // Look far enough for stair gaps; descend searches deeper, ascend mostly looks up.
        RailShape currentShape = getRailShape(cart);
        boolean slopePath = getIncline(cart) != 0 || (currentShape != null && currentShape.isAscending());
        boolean descending = getIncline(cart) < 0;
        boolean ascending = getIncline(cart) > 0;
        int maxStep = slopePath ? 8 : 3;
        int maxDyUp = slopePath ? 4 : 1;
        int maxDyDown = descending ? 8 : (ascending ? 1 : (slopePath ? 4 : 1));
        for (int step = 1; step <= maxStep; step++) {
            for (int dy = -maxDyDown; dy <= maxDyUp; dy++) {
                BlockPos ahead = base.offset(dx * step, dy, dz * step);
                BlockState state = level.getBlockState(ahead);
                if (!BaseRailBlock.isRail(state)) {
                    continue;
                }
                if (ascending && isDeepFlatBelowClimb(cart, ahead, state)) {
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

    /**
     * Flat more than a block below, not a climb target.
     */
    private static boolean isDeepFlatBelowClimb(AbstractMinecart cart, BlockPos ahead, BlockState state) {
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
        return getIncline(cart) < 0 && hasOpenAirSlopeTarget(cart);
    }

    /**
     * Open-air climb while incline is live and we still have a target: continuing rail/stair, a short grace after
     * takeoff (sky launch), or a ceiling that is about to be hit. Climb memory alone or far-away solids (trees) used to
     * keep one-block launches tilted forever.
     */
    public static boolean shouldKeepOpenAirClimb(AbstractMinecart cart) {
        if (getIncline(cart) <= 0) {
            return false;
        }
        if (hasContinuingAscendingRailAhead(cart)
                || hasContinuingSlopeSolidAhead(cart)
                || isBelowContinuingFlatLanding(cart)) {
            return true;
        }
        if (((MinecartFloatPoseAccess) cart).paradiseLost$getOpenAirClimbGrace() > 0) {
            return true;
        }
        // After the grace only a close ceiling keeps the cart rising, not foliage 8 or more blocks up.
        return distanceToSolidAbove(cart, IMMINENT_CEILING_BLOCKS) > 0;
    }

    /**
     * Number of blocks up to the first collidable block above, or 0 if there is none within maxBlocks.
     */
    private static int distanceToSolidAbove(AbstractMinecart cart, int maxBlocks) {
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
    private static boolean hasOpenAirSlopeTarget(AbstractMinecart cart) {
        if (hasContinuingRailAhead(cart) || hasContinuingSlopeSolidAhead(cart)) {
            return true;
        }
        if (getIncline(cart) >= 0) {
            return false;
        }
        RailShape shape = getRailShape(cart);
        return shape != null && shape.isAscending();
    }

    /**
     * Walking-stair diagonal ahead (two piers at different Y). Flat floors don't count.
     */
    public static boolean hasContinuingSlopeSolidAhead(AbstractMinecart cart) {
        int incline = getIncline(cart);
        if (incline == 0) {
            return false;
        }
        int dx = 0;
        int dz = 0;
        Vec3 motion = cart.getDeltaMovement();
        if (motion.horizontalDistanceSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            if (Math.abs(motion.x) >= Math.abs(motion.z)) {
                dx = motion.x > 0.0D ? 1 : -1;
            } else {
                dz = motion.z > 0.0D ? 1 : -1;
            }
        } else {
            float railYaw = getSyncedFloatYaw(cart);
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            if (pose.paradiseLost$hasRailRenderPose()) {
                railYaw = pose.paradiseLost$getRailRenderYaw();
            }
            Vec3 along = railYawDirection(railYaw);
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
     * Solid underfoot with air above, a tread, not a wall.
     */
    private static boolean isWalkableStairPier(Level level, BlockPos pier) {
        if (!isBlockingSolid(level, pier)) {
            return false;
        }
        return !isBlockingSolid(level, pier.above());
    }

    private static boolean isBlockingSolid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || BaseRailBlock.isRail(state)) {
            return false;
        }
        return !state.getCollisionShape(level, pos).isEmpty();
    }

    /**
     * If collision killed xz mid-slope but more slope/stair is ahead, nudge forward. Flat landings ahead don't get this
     * assist.
     */
    public static void assistOpenAirSlopeTravel(AbstractMinecart cart) {
        int incline = getIncline(cart);
        if (incline == 0 || findRailPos(cart) != null) {
            return;
        }
        if (!hasContinuingAscendingRailAhead(cart) && !hasContinuingSlopeSolidAhead(cart)) {
            return;
        }
        Vec3 motion = cart.getDeltaMovement();
        double minSpeed = 0.32D;
        if (motion.horizontalDistance() >= minSpeed) {
            return;
        }
        float railYaw = getSyncedFloatYaw(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()) {
            railYaw = pose.paradiseLost$getRailRenderYaw();
        }
        Vec3 along = railYawDirection(railYaw);
        if (along.lengthSqr() <= 1.0E-8D) {
            return;
        }
        // railYaw points up-rail; descend flips it.
        if (incline < 0) {
            along = along.scale(-1.0D);
        }
        along = along.normalize();
        // Restore climb/descend Y too, pier friction often zeros the whole vector.
        cart.setDeltaMovement(along.x * minSpeed, incline * minSpeed, along.z * minSpeed);
    }

    /**
     * Keep enough along-track speed on ascending rails. Levita flats don't boost ({@code railNeitherBoostsNorBrakes});
     * slope travel still needs the float charge meant to provide when the cart stays seated between stair steps.
     */
    public static void assistFloatingSlopeTravel(AbstractMinecart cart, BlockState railState) {
        if (!isFloating(cart) || !(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (!shape.isAscending()) {
            return;
        }
        updateInclineFromRail(cart, shape, cart.getDeltaMovement());
        int incline = getIncline(cart);
        if (incline == 0) {
            return;
        }
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return;
        }
        Vec3i a = exits.getFirst();
        Vec3i b = exits.getSecond();
        Vec3i upper = a.getY() >= b.getY() ? a : b;
        Vec3i lower = a.getY() < b.getY() ? a : b;
        Vec3i target = incline > 0 ? upper : lower;
        Vec3 dir = new Vec3(target.getX(), 0.0D, target.getZ());
        if (dir.lengthSqr() < 1.0E-8D) {
            return;
        }
        dir = dir.normalize();
        Vec3 motion = cart.getDeltaMovement();
        double along = motion.x * dir.x + motion.z * dir.z;
        double minSpeed = 0.42D;
        if (along < minSpeed) {
            double add = minSpeed - along;
            cart.setDeltaMovement(motion.x + dir.x * add, motion.y, motion.z + dir.z * add);
        }
    }

    /**
     * True when horizontal motion already follows the rail axis (carousel parallel reconnect).
     */
    public static boolean isParallelToRail(AbstractMinecart cart, RailShape shape) {
        Pair<Vec3i, Vec3i> exits = RAIL_EXITS.get(shape);
        if (exits == null) {
            return false;
        }
        Vec3 approach = new Vec3(cart.getDeltaMovement().x, 0.0D, cart.getDeltaMovement().z);
        if (approach.lengthSqr() <= MOTION_EPSILON * MOTION_EPSILON) {
            // cart.yRot is MinecartRenderer/atan2 space (0=east), not living-entity look yaw.
            approach = railYawDirection(cart.getYRot());
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
        // Already parallel, keep XZ, just refresh incline/Y.
        if (isParallelToRail(cart, shape)) {
            setRailShape(cart, shape);
            Vec3 motion = cart.getDeltaMovement();
            int before = getIncline(cart);
            updateInclineFromRail(cart, shape, motion);
            int incline = getIncline(cart);
            if (shape.isAscending() && incline != 0) {
                cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
            }
            if (before != incline && incline != 0 && shape.isAscending()) {
                captureRailRenderPose(cart, true);
                applyFloatingRotation(cart);
            }
            debug(cart, "nudge", "parallel skip XZ shape=" + shape
                    + " incline " + before + "->" + incline);
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
            approach = railYawDirection(cart.getYRot());
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
        // Keep nudge horizontal. Y comes from incline, not from ascending exit normals.
        Vec3 flatAlong = new Vec3(along.x, 0.0D, along.z);
        if (flatAlong.lengthSqr() > 1.0E-8D) {
            flatAlong = flatAlong.normalize();
        } else {
            flatAlong = approach;
        }
        Vec3 newMotion = flatAlong.scale(speed);
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
                        + " along=" + fmt(flatAlong)
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
                        + " sync=" + String.format(Locale.ROOT, "%.1f/%.1f",
                        getSyncedFloatYaw(cart), getSyncedFloatPitch(cart))
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
