package net.id.paradise_lost.block.mechanical;

import net.id.paradise_lost.component.MinecartFloating;
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
import java.util.List;
import java.util.function.Predicate;

public class LevitaRailBlock extends PoweredRailBlock {

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

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
        if (!world.isClientSide) {
            List<AbstractMinecart> list = this.getCarts(world, pos, AbstractMinecart.class, e -> true);
            for (AbstractMinecart cart : list) {
                var floatingComponent = MinecartFloating.get(cart);
                if (state.getValue(POWERED)) {
                    if (!state.getValue(TRIGGERED)) {
                        floatingComponent.addFloating();
                        world.setBlock(pos, state.setValue(TRIGGERED, true), 3);
                        world.scheduleTick(pos, this, 80);
                    }
                } else {
                    floatingComponent.stopFloating();
                }
                MinecartFloating.sync(cart);
            }
        }
    }

    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlock(pos, state.setValue(TRIGGERED, false), 3);
    }

    private <T extends AbstractMinecart> List<T> getCarts(Level world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate) {
        return world.getEntitiesOfClass(entityClass, this.getCartDetectionBox(pos), entityPredicate);
    }

    private AABB getCartDetectionBox(BlockPos pos) {
        double radius = 0.4;
        return new AABB(
                pos.getX() + radius,
                pos.getY(),
                pos.getZ() + radius,
                pos.getX() + 1 - radius,
                pos.getY() + 1 - radius,
                pos.getZ() + 1 - radius
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, TRIGGERED);
    }
}
