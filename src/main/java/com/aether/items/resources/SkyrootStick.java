package com.aether.items.resources;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class SkyrootStick extends Item {
    public SkyrootStick() {
        super(new Settings().group(AetherItemGroups.Resources));
    }
}