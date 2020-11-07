package com.aether.items.utils;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;

public enum AetherTiers {
    SKYROOT(new AetherItemTier(0, 59, 2.0F, 0.0F, 15, AetherBlocks.skyroot_planks)),
    HOLYSTONE(new AetherItemTier(1, 131, 4.0F, 1.0F, 5, AetherBlocks.holystone)),
    ZANITE(new AetherItemTier(2, 250, 6.0F, 2.0F, 14, AetherItems.zanite_gem)),
    CANDY(new AetherItemTier(2, 520, 7.0F, 2.5F, 12, AetherItems.candy_cane)),
    GRAVITITE(new AetherItemTier(3, 1561, 8.0F, 3.0F, 10, null)),
    VALKYRIE(new AetherItemTier(4, 2164, 10.0F, 4.0F, 8, null)),
    LEGENDARY(new AetherItemTier(4, 2164, 10.0F, 4.0F, 8, null));

    private final AetherItemTier defaultTier;

    AetherTiers(AetherItemTier defaultTier) {
        this.defaultTier = defaultTier;
    }

    public AetherItemTier getDefaultTier() {
        return this.defaultTier;
    }
}