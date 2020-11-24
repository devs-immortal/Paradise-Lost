package com.aether.items.resources;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class GoldenAmber extends Item {
    public GoldenAmber() {
        super(new Settings().group(AetherItemGroups.Resources));
    }
}