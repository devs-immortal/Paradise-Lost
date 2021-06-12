package com.aether.util.item;

import net.minecraft.ChatFormatting;

public class AetherRarity {
    private final ChatFormatting formatting;

    public AetherRarity(ChatFormatting formatting) {
        this.formatting = formatting;
    }

    public ChatFormatting getCustomRarityFormatting() {
        return formatting;
    }
}
