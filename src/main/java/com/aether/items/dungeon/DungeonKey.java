package com.aether.items.dungeon;

import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.item.Item;

public class DungeonKey extends Item {
    public DungeonKey() {
        super(new Settings().maxCount(1).group(AetherItemGroups.MISC).rarity(AetherItems.AETHER_LOOT));
    }
}