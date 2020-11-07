package com.aether.items.food;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class HealingStone extends Item {
    public HealingStone() {
        super(new Settings().group(AetherItemGroups.Food).rarity(Rarity.RARE).food(AetherFood.HEALING_STONE));
    }

    @Override
    public boolean hasGlint(ItemStack stackIn) {
        return true;
    }
}