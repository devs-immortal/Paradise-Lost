package com.aether.items.resources;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class GoldAerdust extends Item {
    public GoldAerdust() {
        super(new Settings().group(AetherItemGroups.Resources));
    }
}
