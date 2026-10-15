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
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public final class MinecartFloating {
    public static final boolean DEBUG_LOG = true;

    private static final double FLOAT_SECONDS = 4.1;
    private static final double MOTION_EPSILON = 0.05D;
    /**
     * Horizontal alignment to treat float approach as parallel to the rail (carousel).
     */
    private static final double PARALLEL_DOT = 0.92D;
    private static final String LEGACY_NBT_KEY = "paradiseLostFloating";
    private static final String DEBUG_PREFIX = "[PL-MinecartFloat]";

    private static final ThreadLocal<Integer> POS_CAPTURE_BYPASS = ThreadLocal.withInitial(() -> 0);

    private static final Map<RailShape, Pair<Vec3i, Vec3i>> RAIL_EXITS = Map.ofEntries(
            Map.entry(RailShape.NORTH_SOUTH, Pair.of(Direction.NORTH.getUnitVec3i(), Direction.SOUTH.getUnitVec3i())),
            Map.entry(RailShape.EAST_WEST, Pair.of(Direction.WEST.getUnitVec3i(), Direction.EAST.getUnitVec3i())),
            Map.entry(RailShape.ASCENDING_EAST, Pair.of(Direction.WEST.getUnitVec3i().below(), Direction.EAST.getUnitVec3i())),
            Map.entry(RailShape.ASCENDING_WEST, Pair.of(Direction.WEST.getUnitVec3i(), Direction.EAST.getUnitVec3i().below())),
            Map.entry(RailShape.ASCENDING_NORTH, Pair.of(Direction.NORTH.getUnitVec3i(), Direction.SOUTH.getUnitVec3i().below())),
            Map.entry(RailShape.ASCENDING_SOUTH, Pair.of(Direction.NORTH.getUnitVec3i().below(), Direction.SOUTH.getUnitVec3i())),
            Map.entry(RailShape.SOUTH_EAST, Pair.of(Direction.SOUTH.getUnitVec3i(), Direction.EAST.getUnitVec3i())),
            Map.entry(RailShape.SOUTH_WEST, Pair.of(Direction.SOUTH.getUnitVec3i(), Direction.WEST.getUnitVec3i())),
            Map.entry(RailShape.NORTH_WEST, Pair.of(Direction.NORTH.getUnitVec3i(), Direction.WEST.getUnitVec3i())),
            Map.entry(RailShape.NORTH_EAST, Pair.of(Direction.NORTH.getUnitVec3i(), Direction.EAST.getUnitVec3i()))
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
        if (isOffRail(cart) == offRail) {
            return;
        }
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_OFF_RAIL, offRail);
    }

    public static int getIncline(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE);
    }

    public static void setIncline(AbstractMinecart cart, int incline) {
        int clamped = Mth.clamp(incline, -1, 1);
        if (getIncline(cart) == clamped) {
            return;
        }
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_INCLINE, clamped);
    }

    public static String getRailShapeName(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_SHAPE);
    }

    public static void setRailShape(AbstractMinecart cart, RailShape shape) {
        setRailShape(cart, shape == null ? "" : shape.getSerializedName());
    }

    public static void setRailShape(AbstractMinecart cart, String shapeName) {
        String next = shapeName == null ? "" : shapeName;
        String current = getRailShapeName(cart);
        if (next.equals(current == null ? "" : current)) {
            return;
        }
        Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_SHAPE, next);
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
        if (!shape.isSlope()) {
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
        // Wider hysteresis — tiny exit-dot margins flipped incline every tick on carousel slopes.
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

    @Nullable
    private static Vec3 railSeatPos(AbstractMinecart cart, double x, double y, double z) {
        return cart.getBehavior() instanceof OldMinecartBehavior behavior ? behavior.getPos(x, y, z) : null;
    }

    @Nullable
    private static Vec3 railSeatPosOffs(AbstractMinecart cart, double x, double y, double z, double offset) {
        return cart.getBehavior() instanceof OldMinecartBehavior behavior ? behavior.getPosOffs(x, y, z, offset) : null;
    }

    public static void captureRailRenderPose(AbstractMinecart cart) {
        captureRailRenderPose(cart, false);
    }

    /**
     * @param allowUnsettledAscending when true, midair over ascending still samples pose at the projected rail seat.
     *                                Used when incline flips (descend→climb) before the cart seats — cart.y offs are
     *                                unreliable high above the rail.
     */
    public static void captureRailRenderPose(AbstractMinecart cart, boolean allowUnsettledAscending) {
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            double x = cart.getX();
            double y = cart.getY();
            double z = cart.getZ();
            Vec3 mid = railSeatPos(cart, x, y, z);
            if (mid == null) {
                return;
            }
            Vec3 posOff = railSeatPosOffs(cart, x, y, z, 0.30000001192092896D);
            Vec3 negOff = railSeatPosOffs(cart, x, y, z, -0.30000001192092896D);
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
            // Flat curves can sample a bogus dy from neighboring slopes — only keep pitch on ascents.
            float pitch = 0.0F;
            BlockPos railPos = findRailPos(cart);
            RailShape shape = null;
            if (railPos != null) {
                BlockState railState = cart.level().getBlockState(railPos);
                if (railState.getBlock() instanceof BaseRailBlock rail) {
                    shape = railState.getValue(rail.getShapeProperty());
                    if (shape.isSlope()) {
                        // High above an ascending (e.g. transition onto a corner) samples garbage
                        // offs and can leave stale slope pitch on the next rail. Only capture when
                        // seated — unless incline-flip needs a midair refresh, in which case sample
                        // at the projected rail seat instead.
                        if (!isSettledOnRail(cart, railPos)) {
                            if (!allowUnsettledAscending) {
                                return;
                            }
                            Vec3 seatPosOff = railSeatPosOffs(cart, mid.x, mid.y, mid.z, 0.30000001192092896D);
                            Vec3 seatNegOff = railSeatPosOffs(cart, mid.x, mid.y, mid.z, -0.30000001192092896D);
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
                            // Seat sample still unusable — keep prior pose.
                            if (Math.abs(trialPitch) < 25.0F || Math.abs(trialPitch) > 60.0F) {
                                return;
                            }
                        }
                        // Keep the vanilla offs pair. Do NOT yawAlongMotion — motion-locking yaw
                        // without flipping pitch can invert the air gap vs the on-rail look.
                        float offsYaw = yaw;
                        pitch = (float) (Math.atan(dir.y) * 73.0D);
                        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                                ? 0.0F
                                : 90.0F;
                        float motionYaw = yawAlongMotion(cart, axis);
                        // Quantize only within the offs hemisphere (never cross 90° from offs).
                        float locked = nearestRailYaw(offsYaw, axis);
                        if (Math.abs(Mth.wrapDegrees(locked - offsYaw)) <= 90.0F) {
                            yaw = locked;
                        }
                        // Log when pose jumps or motion disagrees with offs (throttled vs every tick).
                        MinecartFloatPoseAccess prior = (MinecartFloatPoseAccess) cart;
                        boolean motionDisagrees = Math.abs(Mth.wrapDegrees(motionYaw - offsYaw)) > 90.0F;
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
                // Micro-jitter only (<5°). Wider magnitude freeze can stick takeoff below the
                // true slope pitch from offs.
                if (Math.abs(pitch) > 1.0F && Math.abs(oldPitch) > 1.0F
                        && Math.signum(pitch) == Math.signum(oldPitch)
                        && Math.abs(pitch - oldPitch) < 5.0F) {
                    pitch = oldPitch;
                }
            }
            pose.paradiseLost$setRailRenderPose(yaw, pitch);
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
    }

    public static void prepareTakeoff(AbstractMinecart cart) {
        // Refresh incline/shape from the rail we are leaving (flat after a slope must clear incline).
        captureRailState(cart);
        RailShape shape = getRailShape(cart);
        // Flat takeoff (or unknown): never carry slope pitch into the gap — photo regression.
        if (getIncline(cart) == 0 || shape == null || !shape.isSlope()) {
            captureRailRenderPose(cart);
            setIncline(cart, 0);
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            float yaw = resolveFlatFloatYaw(cart, shape);
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, yaw, 0.0F);
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
            debug(cart, "takeoff", "flat pitch=0 yaw=" + String.format(Locale.ROOT, "%.1f", yaw)
                    + " shape=" + getRailShapeName(cart));
        } else {
            // Short ascending rails often never settle long enough for gated capture — force
            // seat-sample / synthesize before freezing the air pose.
            ensureAscendingFloatPose(cart, shape);
            applyFloatingRotation(cart);
            debug(cart, "takeoff", "slope incline=" + getIncline(cart)
                    + " shape=" + getRailShapeName(cart)
                    + " railPose=" + formatRailPose(cart));
        }
    }

    /**
     * Ensure slope yaw/pitch is stored for ascending takeoff / short rails. Prefer vanilla offs (including midair
     * seat-sample); if findRailPos is already gone, synthesize from shape+motion.
     */
    public static void ensureAscendingFloatPose(AbstractMinecart cart, RailShape shape) {
        if (shape == null || !shape.isSlope()) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F) {
            return;
        }
        captureRailRenderPose(cart, true);
        if (pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F) {
            return;
        }
        float axis = (shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)
                ? 0.0F
                : 90.0F;
        float yaw = yawAlongMotion(cart, axis);
        // Approximate vanilla ascending offs pitch when seat-sample is unavailable.
        float pitch = switch (shape) {
            case ASCENDING_WEST -> 44.9F;
            case ASCENDING_EAST, ASCENDING_SOUTH, ASCENDING_NORTH -> -44.9F;
            default -> -44.9F;
        };
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
        debug(cart, "slopeSynth", "shape=" + shape
                + " yaw/pitch=" + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                + " incline=" + getIncline(cart));
    }

    /**
     * Keep float yaw/sync in MinecartRenderer (atan2) space on straight flats. On corners, only clear stale slope pitch
     * — do not overwrite curve yaw (vanilla getPos poses the corner).
     */
    public static void syncFlatFloatYawFromMotion(AbstractMinecart cart) {
        clearFloatingPitch(cart);
        RailShape shape = getRailShape(cart);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (shape != RailShape.EAST_WEST && shape != RailShape.NORTH_SOUTH) {
            // Post-slope corners: zero pitch only — do not overwrite curve yaw.
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
     * from flatAdapt/sync so reconnect does not flip 0↔180.
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
        captureRailState(cart);
        setOffRail(cart, false);
        // Snap into vanilla rail seat — float often left Y at *.000 while seat is *.0625
        // (carousel looked like it hovered above the rail).
        POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() + 1);
        try {
            Vec3 snapped = railSeatPos(cart, cart.getX(), cart.getY(), cart.getZ());
            if (snapped != null) {
                cart.setPos(snapped.x, snapped.y, snapped.z);
            }
        } finally {
            POS_CAPTURE_BYPASS.set(POS_CAPTURE_BYPASS.get() - 1);
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        RailShape landed = getRailShape(cart);
        if (getIncline(cart) == 0 || landed == null || !landed.isSlope()) {
            float yaw = resolveFlatFloatYaw(cart, landed);
            clearFloatingPitch(cart);
            setSyncedFloatPose(cart, yaw, 0.0F);
            snapEntityRotation(cart, yaw, 0.0F);
            pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        } else {
            captureRailRenderPose(cart);
            applyFloatingRotation(cart);
        }
    }

    /**
     * Slope noclip: climb open-air + either direction over ascending rails. Never open-air descent noclip — that phased
     * perp landings through rails/blocks. Carousel descent pier skid is handled by a longer {@code EXIT_GRACE} on slope
     * takeoff instead.
     */
    public static boolean shouldUseSlopeNoclip(AbstractMinecart cart) {
        int incline = getIncline(cart);
        if (incline == 0) {
            return false;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            return incline > 0;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return incline > 0;
        }
        return railState.getValue(rail.getShapeProperty()).isSlope();
    }

    /**
     * Descent-only comeOffTrack noclip ticks to clear the exit pier. Keep short — 12 let carts coast above the next
     * rail (carousel "flies a little above"). Climb already open-air noclip.
     */
    public static int slopeDescentExitGraceTicks() {
        return 5;
    }

    private static final double FLAT_CONNECT_HEIGHT = 0.28D;
    /**
     * Match {@link #isSettledOnRail} flat threshold — flatten only when seating.
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
        // Curves are not flat landings — never adopt flat pose on corners (carousel).
        if (isCornerRail(shape) || shape.isSlope()) {
            return false;
        }
        // Same moment as rail connection / settle — earlier adopt rotates before seating.
        return isSettledOnRail(cart, railPos);
    }

    public static boolean isFlatRailConnectTooEarly(AbstractMinecart cart, BlockPos railPos, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
            return false;
        }
        return !isNearFlatConnectHeight(cart, railPos);
    }

    /**
     * Ghosting over a rail midair: ascending refreshes climb; flat adopts landing pose.
     */
    public static void adaptFloatWhileGhosting(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        if (railState.getValue(rail.getShapeProperty()).isSlope()) {
            adaptFloatToAscendingRail(cart, railState);
        } else {
            adaptFloatToFlatRailBelow(cart, railState);
        }
    }

    /**
     * Midair over ascending rails: refresh incline/Y (and slope pose when direction flips) so a carousel can climb
     * after a descent gap. Does not redirect XZ — parallel keep from p.1.
     */
    public static void adaptFloatToAscendingRail(AbstractMinecart cart, BlockState railState) {
        if (!(railState.getBlock() instanceof BaseRailBlock rail)) {
            return;
        }
        RailShape shape = railState.getValue(rail.getShapeProperty());
        if (!shape.isSlope()) {
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
        if (before != incline && incline != 0) {
            // Force midair seat-sample: settle-gated capture no-ops high above the rail and can
            // leave the prior descending pose after an incline flip onto the opposite slope.
            MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
            float priorYaw = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderYaw() : 0.0F;
            float priorPitch = pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderPitch() : 0.0F;
            captureRailRenderPose(cart, true);
            // Seat sample can still fail midair — opposite ascending rails reverse offs yaw (~180°)
            // while keeping slope pitch (south -90/-45 → north +90/-45). Only when the rail
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
        if (shape.isSlope() || isCornerRail(shape)) {
            return;
        }
        BlockPos railPos = findRailPos(cart);
        // Flatten only when settled on the rail (same gate as reconnect).
        if (railPos == null || !isSettledOnRail(cart, railPos) || !isNearFlatPoseAdoptHeight(cart, railPos)) {
            return;
        }
        boolean changed = false;
        if (getIncline(cart) != 0) {
            setIncline(cart, 0);
            changed = true;
        }
        setRailShape(cart, shape);
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean alreadyFlat = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) <= 0.01F
                && Math.abs(getSyncedFloatPitch(cart)) <= 0.01F;
        if (alreadyFlat) {
            if (changed) {
                debug(cart, "flatAdapt", "incline only shape=" + shape
                        + " yAbove=" + String.format(java.util.Locale.ROOT, "%.3f", cart.getY() - railPos.getY()));
            }
            return;
        }
        // One-shot: sample landing yaw from motion, hard-snap yaw+pitch together.
        captureRailRenderPose(cart);
        float yaw = pose.paradiseLost$hasRailRenderPose()
                ? yawAlongMotion(cart, pose.paradiseLost$getRailRenderYaw())
                : getSyncedFloatYaw(cart);
        pose.paradiseLost$setRailRenderPose(yaw, 0.0F);
        setSyncedFloatPose(cart, yaw, 0.0F);
        snapEntityRotation(cart, yaw, 0.0F);
        debug(cart, "flatAdapt", "pitch=0 yaw=" + String.format(java.util.Locale.ROOT, "%.1f", yaw)
                + " shape=" + shape
                + " parallel=" + isParallelToRail(cart, shape)
                + " incline=" + getIncline(cart)
                + " yAbove=" + String.format(java.util.Locale.ROOT, "%.3f", cart.getY() - railPos.getY())
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
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
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


    public static double descentSpeedOntoFlatRail(AbstractMinecart cart, BlockPos railPos) {
        if (!isFloating(cart)) {
            return 0.0D;
        }
        if (getIncline(cart) != 0) {
            return 0.0D;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
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
        BlockPos railPos = findRailPos(cart);
        if (railPos != null) {
            BlockState railState = cart.level().getBlockState(railPos);
            if (railState.getBlock() instanceof BaseRailBlock rail) {
                RailShape shape = railState.getValue(rail.getShapeProperty());
                if (isCornerRail(shape)) {
                    // Curves: never force flat pitch from "flat below" logic.
                    return getIncline(cart) != 0
                            || (poseHasSlopePitch(cart));
                }
                if (!shape.isSlope()) {
                    // Straight flat: stay tilted until adopt band.
                    return !isNearFlatPoseAdoptHeight(cart, railPos);
                }
            }
        }
        if (getIncline(cart) == 0) {
            return false;
        }
        return true;
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
        if (localSlope
                && Math.abs(pitch) <= 0.01F
                && shouldHoldSlopePitchAgainstSync(cart)) {
            yaw = pose.paradiseLost$getRailRenderYaw();
            pitch = pose.paradiseLost$getRailRenderPitch();
        } else {
            if (localSlope && Math.abs(pitch) <= 0.01F
                    && Math.abs(pose.paradiseLost$getRailRenderPitch() - pitch) > 1.0F) {
                debug(cart, "syncAdopt",
                        "drop local slope " + formatRailPose(cart)
                                + " -> sync "
                                + String.format(Locale.ROOT, "%.1f/%.1f", yaw, pitch)
                                + " incline=" + getIncline(cart));
            }
            if (Math.abs(pitch) <= 0.01F) {
                yaw = getSyncedFloatYaw(cart);
            }
        }
        snapEntityRotation(cart, yaw, pitch);
        pose.paradiseLost$setRailRenderPose(yaw, pitch);
    }

    private static boolean shouldHoldSlopePitchAgainstSync(AbstractMinecart cart) {
        if (Math.abs(getSyncedFloatPitch(cart)) > 0.01F) {
            return false;
        }
        // Flat takeoff publishes incline=0 + sync pitch=0. Never keep a stale local slope pose
        // after that — open-air hold can leave the client tilted across a flat gap.
        if (getIncline(cart) == 0) {
            return false;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean localSlope = pose.paradiseLost$hasRailRenderPose()
                && Math.abs(pose.paradiseLost$getRailRenderPitch()) > 1.0F;
        if (!localSlope) {
            return false;
        }
        BlockPos railPos = findRailPos(cart);
        if (railPos == null) {
            // Slope float with pitch-sync lag only: keep local tilt until flat sync arrives.
            return true;
        }
        BlockState railState = cart.level().getBlockState(railPos);
        if (!(railState.getBlock() instanceof BaseRailBlock rail)
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
            return false;
        }
        // Parallel adopts sooner; perpendicular waits until seat (see adopt heights).
        return !isNearFlatPoseAdoptHeight(cart, railPos);
    }

    /**
     * Float / MinecartRenderer yaw uses atan2(dz, dx): 0=east, 90=south, 180=west, -90=north. Do NOT use
     * {@link Vec3#directionFromRotation} here — that is entity look (0=south).
     */
    private static float yawAlongMotion(AbstractMinecart cart, float railYaw) {
        Vec3 motion = cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);
        float a = Mth.wrapDegrees(railYaw);
        float b = Mth.wrapDegrees(railYaw + 180.0F);
        if (motion.lengthSqr() < 1.0E-6D) {
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
     * Vanilla entity yRot (0=south) → float rail yaw (0=east).
     */
    private static float entityYawToRailYaw(float entityYaw) {
        return Mth.wrapDegrees(entityYaw + 90.0F);
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
        if (Float.floatToIntBits(getSyncedFloatYaw(cart)) != Float.floatToIntBits(yaw)) {
            Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_YAW, yaw);
        }
        if (Float.floatToIntBits(getSyncedFloatPitch(cart)) != Float.floatToIntBits(pitch)) {
            Services.ATTACHMENTS.setAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_PITCH, pitch);
        }
    }

    public static float getSyncedFloatYaw(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_YAW);
    }

    public static float getSyncedFloatPitch(AbstractMinecart cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, CommonDataAttachments.MINE_CART_FLOAT_PITCH);
    }

    private static void snapEntityRotation(AbstractMinecart cart, float yaw, float pitch) {
        applyEntityRotation(cart, yaw, pitch, true);
    }

    private static void applyEntityRotation(AbstractMinecart cart, float yaw, float pitch, boolean hardSnap) {
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
        BlockPos railPos = findRailPos(cart);
        if (railPos != null && isSettledOnRail(cart, railPos)) {
            return false;
        }
        if (isOffRail(cart)) {
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
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
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
                || railState.getValue(rail.getShapeProperty()).isSlope()) {
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
                || !railState.getValue(rail.getShapeProperty()).isSlope()) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        if (!pose.paradiseLost$hasRailRenderPose()) {
            return;
        }
        float yaw = pose.paradiseLost$getRailRenderYaw();
        float pitch = pose.paradiseLost$getRailRenderPitch();
        cart.setYRot(yaw);
        cart.yRotO = yaw;
        cart.setXRot(pitch);
        cart.xRotO = pitch;
        setSyncedFloatPose(cart, yaw, pitch);
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
        if (railState.getValue(rail.getShapeProperty()).isSlope()) {
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
        BlockState railState = cart.level().getBlockState(railPos);
        if (railState.getBlock() instanceof BaseRailBlock rail
                && railState.getValue(rail.getShapeProperty()).isSlope()) {
            // Full ascending block envelope: let vanilla seat after a stair/gap. Only treat as
            // midair when clearly above the slope.
            double height = cart.getY() - railPos.getY();
            return height > 1.0D;
        }
        return !isSettledOnRail(cart, railPos);
    }

    /**
     * True when a rail continues ahead (ascending stair step / short gap). Used to defer air-branch takeoff so brief
     * !isCartOnRail between slope blocks does not float past the rest of the path.
     */
    public static boolean hasContinuingRailAhead(AbstractMinecart cart) {
        if (findRailPos(cart) != null) {
            return true;
        }
        Vec3 motion = cart.getDeltaMovement();
        int dx = 0;
        int dz = 0;
        if (motion.horizontalDistanceSqr() > MOTION_EPSILON * MOTION_EPSILON) {
            if (Math.abs(motion.x) >= Math.abs(motion.z)) {
                dx = motion.x > 0.0D ? 1 : -1;
            } else {
                dz = motion.z > 0.0D ? 1 : -1;
            }
        } else {
            float yaw = cart.getYRot();
            Direction dir = Direction.fromYRot(yaw);
            dx = dir.getStepX();
            dz = dir.getStepZ();
        }
        BlockPos base = cart.blockPosition();
        var level = cart.level();
        for (int step = 1; step <= 2; step++) {
            for (int dy = -1; dy <= 1; dy++) {
                BlockPos ahead = base.offset(dx * step, dy, dz * step);
                if (BaseRailBlock.isRail(level.getBlockState(ahead))) {
                    return true;
                }
            }
        }
        for (int dy = -1; dy <= 1; dy++) {
            if (dy == 0) {
                continue;
            }
            if (BaseRailBlock.isRail(level.getBlockState(base.offset(0, dy, 0)))) {
                return true;
            }
        }
        return false;
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
            approach = Vec3.directionFromRotation(0.0F, cart.getYRot()).multiply(1.0D, 0.0D, 1.0D);
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
        // Parallel (p.1): keep XZ, only refresh incline/Y — required for carousel ascend.
        if (isParallelToRail(cart, shape)) {
            setRailShape(cart, shape);
            Vec3 motion = cart.getDeltaMovement();
            int before = getIncline(cart);
            updateInclineFromRail(cart, shape, motion);
            int incline = getIncline(cart);
            if (shape.isSlope() && incline != 0) {
                cart.setDeltaMovement(motion.x, incline * motion.horizontalDistance(), motion.z);
            }
            if (before != incline && incline != 0 && shape.isSlope()) {
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
            approach = Vec3.directionFromRotation(0.0F, cart.getYRot()).multiply(1.0D, 0.0D, 1.0D);
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
        // Keep nudge horizontal — Y comes from incline, not from ascending exit normals.
        Vec3 flatAlong = new Vec3(along.x, 0.0D, along.z);
        if (flatAlong.lengthSqr() > 1.0E-8D) {
            flatAlong = flatAlong.normalize();
        } else {
            flatAlong = approach;
        }
        Vec3 newMotion = flatAlong.scale(speed);
        if (shape.isSlope()) {
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
