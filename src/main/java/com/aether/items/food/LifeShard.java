package com.aether.items.food;

import com.aether.api.AetherAPI;
import com.aether.api.player.IPlayerAether;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LifeShard extends Item {

    public LifeShard() {
        super(new Settings().maxCount(1).group(AetherItemGroups.Misc).rarity(AetherItems.AETHER_LOOT));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        IPlayerAether playerAether = AetherAPI.get(playerIn);
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (worldIn.isClient) return new TypedActionResult<>(ActionResult.PASS, heldItem);

        if (playerAether.getShardsUsed() < 10) {
            playerAether.increaseHealth(1);
            heldItem.setCount(heldItem.getCount() - 1);
            return new TypedActionResult<>(ActionResult.SUCCESS, heldItem);
        } else {
            playerIn.sendMessage(new LiteralText("You can only use a total of 10 life shards"), true);
        }

        return new TypedActionResult<>(ActionResult.PASS, heldItem);
    }
}