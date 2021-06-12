package com.aether.items.staff;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CloudStaff extends Item {

    public CloudStaff(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (worldIn.isClientSide) return super.use(worldIn, playerIn, handIn);

        //TODO: Implement logic

        return super.use(worldIn, playerIn, handIn);
    }
}