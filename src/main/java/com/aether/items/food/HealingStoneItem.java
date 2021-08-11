package com.aether.items.food;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HealingStoneItem extends Item {
    public HealingStoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stackIn) {
        return true;
    }
}