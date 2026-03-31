package net.id.paradiselost.blocks.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.component.ParadiseLostComponents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class LevitaRailBlock extends AbstractRailBlock {

    public static final MapCodec<LevitaRailBlock> CODEC = createCodec(LevitaRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE;
    public static final BooleanProperty TRIGGERED;
    public static final BooleanProperty POWERED;

    public MapCodec<LevitaRailBlock> getCodec() {
        return CODEC;
    }

    public LevitaRailBlock(AbstractBlock.Settings settings) {
        super(true, settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(SHAPE, RailShape.NORTH_SOUTH)
                        .with(POWERED, Boolean.valueOf(false))
                        .with(WATERLOGGED, Boolean.valueOf(false))
                        .with(TRIGGERED, Boolean.valueOf(false))
        );
    }

    protected boolean isPoweredByOtherRails(World world, BlockPos pos, BlockState state, boolean bl, int distance) {
        if (distance >= 8) {
            return false;
        } else {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean bl2 = true;
            RailShape railShape = (RailShape)state.get(SHAPE);
            switch (railShape) {
                case NORTH_SOUTH:
                    if (bl) {
                        ++k;
                    } else {
                        --k;
                    }
                    break;
                case EAST_WEST:
                    if (bl) {
                        --i;
                    } else {
                        ++i;
                    }
                    break;
                case ASCENDING_EAST:
                    if (bl) {
                        --i;
                    } else {
                        ++i;
                        ++j;
                        bl2 = false;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (bl) {
                        --i;
                        ++j;
                        bl2 = false;
                    } else {
                        ++i;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (bl) {
                        ++k;
                    } else {
                        --k;
                        ++j;
                        bl2 = false;
                    }

                    railShape = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (bl) {
                        ++k;
                        ++j;
                        bl2 = false;
                    } else {
                        --k;
                    }

                    railShape = RailShape.NORTH_SOUTH;
            }

            if (this.isPoweredByOtherRails(world, new BlockPos(i, j, k), bl, distance, railShape)) {
                return true;
            } else {
                return bl2 && this.isPoweredByOtherRails(world, new BlockPos(i, j - 1, k), bl, distance, railShape);
            }
        }
    }

    protected boolean isPoweredByOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(this)) {
            return false;
        } else {
            RailShape railShape = (RailShape)blockState.get(SHAPE);
            if (shape != RailShape.EAST_WEST || railShape != RailShape.NORTH_SOUTH && railShape != RailShape.ASCENDING_NORTH && railShape != RailShape.ASCENDING_SOUTH) {
                if (shape != RailShape.NORTH_SOUTH || railShape != RailShape.EAST_WEST && railShape != RailShape.ASCENDING_EAST && railShape != RailShape.ASCENDING_WEST) {
                    if ((Boolean)blockState.get(POWERED)) {
                        return world.isReceivingRedstonePower(pos) ? true : this.isPoweredByOtherRails(world, pos, blockState, bl, distance + 1);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean bl = (Boolean)state.get(POWERED);
        boolean bl2 = world.isReceivingRedstonePower(pos) || this.isPoweredByOtherRails(world, pos, state, true, 0) || this.isPoweredByOtherRails(world, pos, state, false, 0);
        if (bl2 != bl) {
            world.setBlockState(pos, state.with(POWERED, bl2), 3);
            world.updateNeighborsAlways(pos.down(), this);
            if (((RailShape)state.get(SHAPE)).isAscending()) {
                world.updateNeighborsAlways(pos.up(), this);
            }
        }

    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch ((RailShape)state.get(SHAPE)) {
                    case ASCENDING_EAST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_WEST);
                    }
                    case ASCENDING_WEST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_EAST);
                    }
                    case ASCENDING_NORTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                    }
                    case ASCENDING_SOUTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_NORTH);
                    }
                    case SOUTH_EAST -> {
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                    }
                    case SOUTH_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    }
                    case NORTH_WEST -> {
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    }
                    case NORTH_EAST -> {
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    }
                }
            case COUNTERCLOCKWISE_90:
                switch ((RailShape)state.get(SHAPE)) {
                    case NORTH_SOUTH -> {
                        return state.with(SHAPE, RailShape.EAST_WEST);
                    }
                    case EAST_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_SOUTH);
                    }
                    case ASCENDING_EAST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_NORTH);
                    }
                    case ASCENDING_WEST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                    }
                    case ASCENDING_NORTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_WEST);
                    }
                    case ASCENDING_SOUTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_EAST);
                    }
                    case SOUTH_EAST -> {
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    }
                    case SOUTH_WEST -> {
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    }
                    case NORTH_WEST -> {
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    }
                    case NORTH_EAST -> {
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                    }
                }
            case CLOCKWISE_90:
                switch ((RailShape)state.get(SHAPE)) {
                    case NORTH_SOUTH -> {
                        return state.with(SHAPE, RailShape.EAST_WEST);
                    }
                    case EAST_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_SOUTH);
                    }
                    case ASCENDING_EAST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                    }
                    case ASCENDING_WEST -> {
                        return state.with(SHAPE, RailShape.ASCENDING_NORTH);
                    }
                    case ASCENDING_NORTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_EAST);
                    }
                    case ASCENDING_SOUTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_WEST);
                    }
                    case SOUTH_EAST -> {
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    }
                    case SOUTH_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                    }
                    case NORTH_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    }
                    case NORTH_EAST -> {
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    }
                }
            default:
                return state;
        }
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = (RailShape)state.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (railShape) {
                    case ASCENDING_NORTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                    }
                    case ASCENDING_SOUTH -> {
                        return state.with(SHAPE, RailShape.ASCENDING_NORTH);
                    }
                    case SOUTH_EAST -> {
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    }
                    case SOUTH_WEST -> {
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                    }
                    case NORTH_WEST -> {
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    }
                    case NORTH_EAST -> {
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    }
                    default -> {
                        return super.mirror(state, mirror);
                    }
                }
            case FRONT_BACK:
                switch (railShape) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, TRIGGERED);
    }

    static {
        SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
        POWERED = Properties.POWERED;
        TRIGGERED = Properties.TRIGGERED;
    }
    

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            List<AbstractMinecartEntity> list = this.getCarts(world, pos, AbstractMinecartEntity.class, e -> true);
            for (AbstractMinecartEntity cart : list) {
                var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(cart);
                if (state.get(POWERED)) {
                    if (!state.get(TRIGGERED)) {
                        floatingComponent.addFloating();
                        world.setBlockState(pos, state.with(TRIGGERED, true), 3);
                        world.scheduleBlockTick(pos, this, 80);
                    }
                } else {
                    floatingComponent.stopFloating();
                }
                ParadiseLostComponents.FLOATING_KEY.sync(cart);
            }
        }
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(TRIGGERED, false), 3);
    }

    private <T extends AbstractMinecartEntity> List<T> getCarts(World world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate) {
        return world.getEntitiesByClass(entityClass, this.getCartDetectionBox(pos), entityPredicate);
    }

    private Box getCartDetectionBox(BlockPos pos) {
        double radius = 0.4;
        return new Box(
                pos.getX() + radius,
                pos.getY(),
                pos.getZ() + radius,
                pos.getX() + 1 - radius,
                pos.getY() + 1 - radius,
                pos.getZ() + 1 - radius
        );
    }
}
