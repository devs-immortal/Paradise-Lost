package com.aether.items.resources;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class FrozenAerdust extends Item {
    public FrozenAerdust() {
        super(new Settings().group(AetherItemGroups.Materials));
    }
}
