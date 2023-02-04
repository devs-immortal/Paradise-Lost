package net.id.paradiselost.items.tools.base_tools;

import net.id.paradiselost.api.FloatingBlockHelper;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravityTool {
    public static ActionResult flipEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        ((ParadiseLostEntityExtensions) entity).setFlipped();
        return ActionResult.SUCCESS;
    }

    public static ActionResult tryFloatBlock(ItemUsageContext context, ActionResult defaultResult) {
        if (defaultResult != ActionResult.PASS) {
            return defaultResult;
        }
        return createFloatingBlockEntity(context);
    }

    private static ActionResult createFloatingBlockEntity(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);

        if (!world.isClient()) {
            if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.SOUL_FIRE) {
                world.breakBlock(pos, false);
                return ActionResult.SUCCESS;
            }

            if (!FloatingBlockHelper.isToolAdequate(context)) {
                return ActionResult.PASS;
            }

            if (!FloatingBlockHelper.ANY.tryCreate(world, pos)) {
                return ActionResult.PASS;
            }
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getStack().damage(4, context.getPlayer(), (p) -> p.sendToolBreakStatus(context.getHand()));
        }

        return ActionResult.SUCCESS;
    }
}
