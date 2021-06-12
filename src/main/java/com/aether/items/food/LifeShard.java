package com.aether.items.food;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LifeShard extends Item {

    public LifeShard(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (worldIn.isClientSide) return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);

        //if (playerAether.getShardsUsed() < 10) {
        //    playerAether.increaseHealth(1);
        //    heldItem.setCount(heldItem.getCount() - 1);
        //    return new TypedActionResult<>(ActionResult.SUCCESS, heldItem);
        //} else {
        //    playerIn.sendMessage(new LiteralText("You can only use a total of 10 life shards"), true);
        //}

        return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
    }
}