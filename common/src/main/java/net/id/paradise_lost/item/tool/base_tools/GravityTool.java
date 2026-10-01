package net.id.paradise_lost.item.tool.base_tools;

import net.id.paradise_lost.api.FloatingBlockHelper;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.util.ParadiseLostEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GravityTool {
    public static InteractionResult flipEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        ((ParadiseLostEntityExtensions) entity).setFlipped();
        if (!entity.level().isClientSide()) {

            entity.level().broadcastEntityEvent(entity, ParadiseLostEvents.GRAVITY_FLIP);
        }
        return InteractionResult.SUCCESS;
    }

    public static InteractionResult tryFloatBlock(UseOnContext context, InteractionResult defaultResult) {
        if (defaultResult != InteractionResult.PASS) {
            return defaultResult;
        }
        return createFloatingBlockEntity(context);
    }

    private static InteractionResult createFloatingBlockEntity(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);

        if (!world.isClientSide()) {
            if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.SOUL_FIRE) {
                world.destroyBlock(pos, false);
                return InteractionResult.SUCCESS;
            }

            if (!FloatingBlockHelper.isToolAdequate(context)) {
                return InteractionResult.PASS;
            }

            if (!FloatingBlockHelper.ANY.tryCreate(world, pos)) {
                return InteractionResult.PASS;
            }
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getItemInHand().hurtAndBreak(4, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
        }

        return InteractionResult.SUCCESS;
    }
}
