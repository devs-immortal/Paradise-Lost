package com.aether.items.staff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CloudStaffItem extends Item {

    public CloudStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (worldIn.isClient) return super.use(worldIn, playerIn, handIn);

        //TODO: Implement logic

        return super.use(worldIn, playerIn, handIn);
    }
}