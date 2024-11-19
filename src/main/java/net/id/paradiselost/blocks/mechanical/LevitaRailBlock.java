package net.id.paradiselost.blocks.mechanical;

import net.id.paradiselost.component.ParadiseLostComponents;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class LevitaRailBlock extends PoweredRailBlock {

    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

    public LevitaRailBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(SHAPE, RailShape.NORTH_SOUTH)
                        .with(POWERED, Boolean.valueOf(false))
                        .with(WATERLOGGED, Boolean.valueOf(false))
                        .with(TRIGGERED, Boolean.valueOf(false))
        );
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, TRIGGERED);
    }
}
