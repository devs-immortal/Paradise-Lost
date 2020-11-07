package com.aether.items.materials;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class GoldenAmber extends Item {
    public GoldenAmber() {
        super(new Settings().group(AetherItemGroups.Materials));
    }
}