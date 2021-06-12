package com.aether.items.resources;

import com.aether.blocks.AetherBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class AmbrosiumShard extends Item {

    public AmbrosiumShard(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK) {
            if (!context.getPlayer().isCreative())
                context.getItemInHand().setCount(context.getItemInHand().getCount() - 1);
            context.getLevel().setBlockAndUpdate(context.getClickedPos(), AetherBlocks.AETHER_ENCHANTED_GRASS.defaultBlockState());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (playerIn.isHurt()) {
            if (!playerIn.isCreative())
                heldItem.setCount(heldItem.getCount() - 1);
            playerIn.heal(2.0F);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
    }
}