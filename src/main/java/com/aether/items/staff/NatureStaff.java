package com.aether.items.staff;

import net.minecraft.item.Item;

import com.aether.items.AetherItemGroups;

public class NatureStaff extends Item {
    public NatureStaff() {
        super(new Settings().maxCount(1).maxDamage(100).group(AetherItemGroups.MISC));
    }
}