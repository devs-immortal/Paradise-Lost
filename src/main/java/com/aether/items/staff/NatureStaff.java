package com.aether.items.staff;

import com.aether.items.AetherItemGroups;
import net.minecraft.item.Item;

public class NatureStaff extends Item {
    public NatureStaff() {
        super(new Settings().maxCount(1).maxDamage(100).group(AetherItemGroups.Misc));
    }
}