package net.id.paradise_lost.block.mechanical;

import net.id.paradise_lost.util.minecart.MinecartFloatStateHelper;
import net.id.paradise_lost.util.minecart.MinecartRailLocatorHelper;
import net.id.paradise_lost.util.minecart.MinecartRailMotionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;

public class LevitaRailBlock extends PoweredRailBlock {

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final int TRIGGER_DELAY_TICKS = 80;

    private static final double DETECTION_INSET = 0.4D;

    public LevitaRailBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(SHAPE, RailShape.NORTH_SOUTH)
                        .setValue(POWERED, Boolean.FALSE)
                        .setValue(WATERLOGGED, Boolean.FALSE)
                        .setValue(TRIGGERED, Boolean.FALSE)
        );
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide() || (oldState.is(this) && !movedByPiston)) {
            return;
        }
        // Piston copies TRIGGERED over, but the reset tick stays at the old spot.
        // A rail placed already triggered has the same problem.
        if (state.getValue(TRIGGERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.scheduleTick(pos, this, TRIGGER_DELAY_TICKS);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        // A lifted rail calls this too, for carts in the cell it left. It is not in the world then.
        if (world.isClientSide() || !world.getBlockState(pos).is(this) || !(entity instanceof AbstractMinecart cart)
                || !isOnRail(cart, pos)) {
            return;
        }
        // Like detector rail, bails once it is already on. Triggered is that latch: the tick releases it.
        if (state.getValue(TRIGGERED)) {
            trackIncline(state, pos, cart);
            if (!state.getValue(POWERED)) {
                MinecartFloatStateHelper.stopFloating(cart);
            }
            return;
        }
        trigger(state, world, pos, cart);
    }

    private void trigger(BlockState state, Level world, BlockPos pos, AbstractMinecart cart) {
        trackIncline(state, pos, cart);
        if (!state.getValue(POWERED)) {
            MinecartFloatStateHelper.stopFloating(cart);
            return;
        }
        if (state.getValue(TRIGGERED)) {
            return;
        }
        // One charge, then nothing for 80 ticks, whatever float the cart already has.
        MinecartFloatStateHelper.addFloating(cart);
        // Clients need TRIGGERED, neighbors must not: UPDATE_ALL reshaped a descending powered
        // rail in a valley into a climb, which looked like an early rotation on the cart.
        world.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
        world.scheduleTick(pos, this, TRIGGER_DELAY_TICKS);
    }

    private static void trackIncline(BlockState state, BlockPos pos, AbstractMinecart cart) {
        // Overlap is enough to notice the rail, not to adopt it. The next levita in a valley used
        // to flip incline to climb while the cart was still on the descent.
        BlockPos riding = MinecartRailLocatorHelper.findRailPos(cart);
        if (pos.equals(riding) && MinecartRailLocatorHelper.isSettledOnRail(cart, pos)) {
            MinecartRailMotionHelper.updateInclineFromRail(cart, state.getValue(SHAPE), cart.getDeltaMovement());
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(TRIGGERED)) {
            return;
        }
        // Delay is over. Release with the state this tick already has, then one press if someone is still on it.
        BlockState open = state.setValue(TRIGGERED, false);
        world.setBlock(pos, open, Block.UPDATE_CLIENTS);
        if (!open.getValue(POWERED)) {
            return;
        }
        BlockState latched = open.setValue(TRIGGERED, true);
        boolean charged = false;
        for (AbstractMinecart cart : world.getEntitiesOfClass(AbstractMinecart.class, detectionBox(pos))) {
            if (!isOnRail(cart, pos)) {
                continue;
            }
            if (!charged) {
                trigger(open, world, pos, cart);
                charged = true;
            } else {
                trackIncline(latched, pos, cart);
            }
        }
    }

    private static boolean isOnRail(AbstractMinecart cart, BlockPos pos) {
        return cart.getBoundingBox().intersects(detectionBox(pos));
    }

    private static AABB detectionBox(BlockPos pos) {
        return new AABB(
                pos.getX() + DETECTION_INSET,
                pos.getY(),
                pos.getZ() + DETECTION_INSET,
                pos.getX() + 1 - DETECTION_INSET,
                pos.getY() + 1 - DETECTION_INSET,
                pos.getZ() + 1 - DETECTION_INSET);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, TRIGGERED);
    }
}
