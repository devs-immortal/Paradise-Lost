package com.aether.util.item;

import net.minecraft.util.Formatting;

public class AetherRarity {
    private final Formatting formatting;

    public AetherRarity(Formatting formatting) {
        this.formatting = formatting;
    }

    public Formatting getCustomRarityFormatting() {
        return formatting;
    }
}
