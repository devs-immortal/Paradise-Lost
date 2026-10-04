package net.id.paradise_lost.block;

import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.LevelReader;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.id.paradise_lost.world.portal.ParadiseLostPortalForcer;
import net.id.paradise_lost.world.portal.ParadiseLostPortalShape;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ParadiseLostPortalBlock extends Block implements Portal {

    public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    protected static final VoxelShape Y_AXIS_AABB = Block.box(0.0, 6.0, 0.0, 16.0, 10.0, 16.0);

    public ParadiseLostPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public static Direction.Axis axisOf(BlockState state) {
        if (state.hasProperty(AXIS)) {
            return state.getValue(AXIS);
        }
        return state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)
                ? state.getValue(BlockStateProperties.HORIZONTAL_AXIS)
                : Direction.Axis.X;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case Z -> Z_AXIS_AABB;
            case Y -> Y_AXIS_AABB;
            default -> X_AXIS_AABB;
        };
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        Direction.Axis directionAxis = direction.getAxis();
        Direction.Axis blockAxis = state.getValue(AXIS);

        if (!blockAxis.isHorizontal()) {
            return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
        }
        boolean flag = blockAxis != directionAxis && directionAxis.isHorizontal();
        if (!flag && !neighborState.is(this) && level instanceof LevelAccessor accessor && !(new ParadiseLostPortalShape(accessor, pos, blockAxis).isComplete())) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED)) {
            return;
        }
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return entity instanceof Player player
                ? Math.max(
                        1,
                        level.getGameRules().getInt(
                                player.getAbilities().invulnerable
                                        ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
                                        : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY
                        )
                )
                : 0;
    }

    @Override
    public Portal.Transition getLocalTransition() {
        return Portal.Transition.CONFUSION;
    }

    @Nullable
    @Override
    public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> targetKey = level.dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY
                ? Level.OVERWORLD
                : ParadiseLostDimension.PARADISE_LOST_WORLD_KEY;
        ServerLevel destination = level.getServer().getLevel(targetKey);
        if (destination == null) {
            return null;
        }

        WorldBorder border = destination.getWorldBorder();
        double scale = DimensionType.getTeleportationScale(level.dimensionType(), destination.dimensionType());
        BlockPos scaled = border.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);

        return getExitPortal(destination, entity, pos, scaled, border);
    }

    @Nullable
    private TeleportTransition getExitPortal(
            ServerLevel destination,
            Entity entity,
            BlockPos enteredPos,
            BlockPos exitSearchPos,
            WorldBorder border
    ) {
        ParadiseLostPortalForcer forcer = new ParadiseLostPortalForcer(destination);
        Optional<BlockPos> existing = forcer.findClosestPortalPosition(exitSearchPos, border);

        BlockUtil.FoundRectangle rectangle;
        TeleportTransition.PostTeleportTransition post;
        if (existing.isPresent()) {
            BlockPos portalPos = existing.get();
            BlockState portalState = destination.getBlockState(portalPos);
            rectangle = BlockUtil.getLargestRectangleAround(
                    portalPos,
                    axisOf(portalState),
                    21,
                    Direction.Axis.Y,
                    21,
                    testPos -> destination.getBlockState(testPos) == portalState
            );
            post = ParadiseLostPortalForcer.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(portalPos));
        } else {
            Direction.Axis axis = entity.level().getBlockState(enteredPos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> created = forcer.createPortal(exitSearchPos, axis);
            if (created.isEmpty()) {
                ParadiseLost.LOG.error("Unable to create a Paradise Lost portal (likely outside world border)");
                return null;
            }
            rectangle = created.get();
            post = ParadiseLostPortalForcer.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
        }

        return getDimensionTransitionFromExit(entity, enteredPos, rectangle, destination, post);
    }

    private static TeleportTransition getDimensionTransitionFromExit(
            Entity entity,
            BlockPos enteredPos,
            BlockUtil.FoundRectangle rectangle,
            ServerLevel destination,
            TeleportTransition.PostTeleportTransition post
    ) {
        BlockState enteredState = entity.level().getBlockState(enteredPos);
        Direction.Axis axis;
        Vec3 relative;
        if (enteredState.hasProperty(AXIS) || enteredState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {

            axis = axisOf(enteredState);
            BlockUtil.FoundRectangle entryRect = BlockUtil.getLargestRectangleAround(
                    enteredPos,
                    axis,
                    21,
                    Direction.Axis.Y,
                    21,
                    testPos -> entity.level().getBlockState(testPos) == enteredState
            );
            relative = entity.getRelativePortalPosition(axis, entryRect);
        } else {
            axis = Direction.Axis.X;
            relative = new Vec3(0.5, 0.0, 0.0);
        }

        return createDimensionTransition(
                destination,
                rectangle,
                axis,
                relative,
                entity,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot(),
                post
        );
    }

    private static TeleportTransition createDimensionTransition(
            ServerLevel destination,
            BlockUtil.FoundRectangle rectangle,
            Direction.Axis entryAxis,
            Vec3 relative,
            Entity entity,
            Vec3 speed,
            float yRot,
            float xRot,
            TeleportTransition.PostTeleportTransition post
    ) {
        BlockPos minCorner = rectangle.minCorner;
        BlockState cornerState = destination.getBlockState(minCorner);
        Direction.Axis exitAxis = axisOf(cornerState);
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        int yawOffset = entryAxis == exitAxis ? 0 : 90;
        Vec3 exitSpeed = entryAxis == exitAxis ? speed : new Vec3(speed.z, speed.y, -speed.x);
        double d2 = (double) dimensions.width() / 2.0 + (rectangle.axis1Size - (double) dimensions.width()) * relative.x();
        double d3 = (rectangle.axis2Size - (double) dimensions.height()) * relative.y();
        double d4 = 0.5 + relative.z();
        boolean alongX = exitAxis == Direction.Axis.X;
        Vec3 raw = new Vec3(
                (double) minCorner.getX() + (alongX ? d2 : d4),
                (double) minCorner.getY() + d3,
                (double) minCorner.getZ() + (alongX ? d4 : d2)
        );
        Vec3 dest = ParadiseLostPortalShape.findCollisionFreePosition(raw, destination, entity, dimensions);
        return new TeleportTransition(destination, dest, exitSpeed, yRot + (float) yawOffset, xRot, post);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!Services.PLATFORM.isPhysicalClient()) {
            return;
        }
        if (random.nextInt(200) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    ParadiseLostSoundEvents.BLOCK_PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        double d = pos.getX() + random.nextDouble();
        double e = pos.getY() + random.nextDouble();
        double f = pos.getZ() + random.nextDouble();
        double g = (random.nextFloat() - 0.5D) * 0.5D;
        double h = (random.nextFloat() - 0.5D) * 0.5D;
        double j = (random.nextFloat() - 0.5D) * 0.5D;
        int k = random.nextInt(2) * 2 - 1;
        if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
            d = pos.getX() + 0.5D + 0.25D * k;
            g = random.nextFloat() * 2.0F * k;
        } else {
            f = pos.getZ() + 0.5D + 0.25D * k;
            j = random.nextFloat() * 2.0F * k;
        }
        if (level.getRandom().nextInt(6) != 0) {
            level.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, g, h, j);
        } else {
            level.addParticle(ParticleTypes.CLOUD, d, e, f, 0, 0, 0);
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
