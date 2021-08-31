package net.id.aether.items.resources;

import net.id.aether.blocks.AetherBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AmbrosiumShardItem extends Item {

    public AmbrosiumShardItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK) {
            if (!context.getPlayer().isCreative())
                context.getStack().setCount(context.getStack().getCount() - 1);
            context.getWorld().setBlockState(context.getBlockPos(), AetherBlocks.AETHER_ENCHANTED_GRASS.getDefaultState());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (playerIn.canFoodHeal()) {
            if (!playerIn.isCreative())
                heldItem.setCount(heldItem.getCount() - 1);
            playerIn.heal(2.0F);
            return new TypedActionResult<>(ActionResult.SUCCESS, heldItem);
        }

        return new TypedActionResult<>(ActionResult.PASS, heldItem);
    }
}