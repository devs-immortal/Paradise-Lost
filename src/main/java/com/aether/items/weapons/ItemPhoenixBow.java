package com.aether.items.weapons;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;

public class ItemPhoenixBow extends BowItem {
    public ItemPhoenixBow() {
        super(new Item.Settings().maxDamage(384).group(AetherItemGroups.Weapons));
    }
}