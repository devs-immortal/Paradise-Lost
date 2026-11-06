package net.id.paradise_lost.block.mechanical;

import net.id.paradise_lost.util.MinecartFloatingHelper;
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
                        .setValue(POWERED, Boolean.valueOf(false))
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
                        .setValue(TRIGGERED, Boolean.valueOf(false))
        );
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        // A lifted rail calls this too, as a floating block entity, for carts in the cell it left. It is not in the world
        // then, so it must neither charge a cart nor write its state back as a copy of the rail.
        if (!world.isClientSide && world.getBlockState(pos).is(this) && entity instanceof AbstractMinecart cart
                && isOnRail(cart, pos)) {
            apply(state, world, pos, cart);
        }
    }

    private void apply(BlockState state, Level world, BlockPos pos, AbstractMinecart cart) {
        // AABB overlap is enough to charge, but not to adopt this rail. The next levita in a valley used
        // to flip incline to climb while the cart was still on the descent, which drew an early rotation.
        BlockPos riding = MinecartFloatingHelper.findRailPos(cart);
        if (pos.equals(riding) && MinecartFloatingHelper.isSettledOnRail(cart, pos)) {
            MinecartFloatingHelper.updateInclineFromRail(cart, state.getValue(SHAPE), cart.getDeltaMovement());
        }
        if (!state.getValue(POWERED)) {
            MinecartFloatingHelper.stopFloating(cart);
        } else if (!state.getValue(TRIGGERED)) {
            // TRIGGERED is the rail's off state: it gives one charge, then does nothing at all for 80 ticks,
            // no matter which cart is on it or how much float that cart has left.
            MinecartFloatingHelper.addFloating(cart);
            // Clients need TRIGGERED, neighbors must not: UPDATE_ALL reshaped a descending powered
            // rail in a valley into a climb, which looked like an early rotation on the cart.
            world.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
            world.scheduleTick(pos, this, TRIGGER_DELAY_TICKS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(TRIGGERED)) {
            return;
        }
        BlockState reset = state.setValue(TRIGGERED, false);
        world.setBlock(pos, reset, Block.UPDATE_CLIENTS);
        if (!reset.getValue(POWERED)) {
            return;
        }
        for (AbstractMinecart cart : world.getEntitiesOfClass(AbstractMinecart.class, detectionBox(pos))) {
            apply(world.getBlockState(pos), world, pos, cart);
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
