package net.id.aether.items.tools;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.entities.util.floatingblock.FloatingBlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravititeTool {
    public static ActionResult flipEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        ((AetherEntityExtensions) entity).setFlipped();
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

            boolean success;
            if (state.isOf(AetherBlocks.GRAVITITE_LEVITATOR)) {
                success = FloatingBlockHelper.tryCreatePusher(world, pos);
            } else if (state.getProperties().contains(Properties.DOUBLE_BLOCK_HALF)) {
                success = FloatingBlockHelper.tryCreateDouble(world, pos);
            } else {
                success = FloatingBlockHelper.tryCreateGeneric(world, pos);
            }

            if (!success) {
                return ActionResult.PASS;
            }
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getStack().damage(4, context.getPlayer(), (p) -> p.sendToolBreakStatus(context.getHand()));
        }

        return ActionResult.SUCCESS;
    }
}
