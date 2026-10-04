package net.id.paradise_lost.item.misc;

import net.id.paradise_lost.world.portal.ParadiseLostPortalHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ParadiseLostPortalItem extends Item {
    public ParadiseLostPortalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            Direction.Axis portalAxis = context.getHorizontalDirection().getAxis() == Direction.Axis.X
                    ? Direction.Axis.Z
                    : Direction.Axis.X;
            ParadiseLostPortalHelper.placeForcedPortal(context.getLevel(), context.getClickedPos(), portalAxis);

            if (context.getPlayer() != null && !context.getPlayer().hasInfiniteMaterials()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
