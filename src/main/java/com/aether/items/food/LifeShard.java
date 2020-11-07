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

public class LifeShard extends Item {

    public LifeShard() {
        super(new Settings().maxCount(1).group(AetherItemGroups.Misc).rarity(AetherItems.aether_loot));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (worldIn.isClient) return new TypedActionResult<>(ActionResult.PASS, heldItem);

        //TODO: Limit shards use (max 10 uses)

        return new TypedActionResult<>(ActionResult.PASS, heldItem);
    }
}