package com.aether.items.food;

import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GummySwet extends Item {

    public GummySwet() {
        super(new Settings().group(AetherItemGroups.FOOD).rarity(AetherItems.AETHER_LOOT));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (playerIn.canFoodHeal()) {
            if (!playerIn.isCreative()) heldItem.setCount(heldItem.getCount() - 1);
            playerIn.heal(playerIn.getMaxHealth());
            return new TypedActionResult<>(ActionResult.SUCCESS, heldItem);
        }

        return new TypedActionResult<>(ActionResult.PASS, heldItem);
    }
}