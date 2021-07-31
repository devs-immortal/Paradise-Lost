package com.aether.util.item;

import net.minecraft.util.Formatting;

public record AetherRarity(Formatting formatting) {
    public static final AetherRarity AETHER_LOOT = new AetherRarity(Formatting.GREEN);
}
