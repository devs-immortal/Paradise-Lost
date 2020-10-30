package com.aether.items.staff;

import com.aether.items.AetherItemGroups;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CloudStaff extends Item {

    public CloudStaff() {
        super(new Settings().maxCount(1).maxDamage(60).group(AetherItemGroups.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (worldIn.isClient) return super.use(worldIn, playerIn, handIn);

        //TODO: Implement logic

        return super.use(worldIn, playerIn, handIn);
    }
}