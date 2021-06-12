package com.aether.items.food;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GummySwet extends Item {

    public GummySwet(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (playerIn.isHurt()) {
            if (!playerIn.isCreative()) heldItem.setCount(heldItem.getCount() - 1);
            playerIn.heal(playerIn.getMaxHealth());
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
    }
}