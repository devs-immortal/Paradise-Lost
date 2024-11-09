package net.id.paradiselost.blocks.mechanical;

import net.id.paradiselost.component.ParadiseLostComponents;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class LevitaRailBlock extends PoweredRailBlock {

    public LevitaRailBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            List<AbstractMinecartEntity> list = this.getCarts(world, pos, AbstractMinecartEntity.class, e -> true);
            for (AbstractMinecartEntity cart : list) {
                var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(cart);
                if (state.get(POWERED)) {
                    floatingComponent.startFloating();
                } else {
                    floatingComponent.stopFloating();
                }
                ParadiseLostComponents.FLOATING_KEY.sync(cart);
            }
        }
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
