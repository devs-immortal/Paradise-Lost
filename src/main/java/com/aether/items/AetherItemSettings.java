package com.aether.items;

import com.aether.util.item.AetherRarity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AetherItemSettings extends Item.Properties {
    private ChatFormatting formatting = null;
    private int enchantmentGlintColor = 0x8e48e1;

    public AetherItemSettings rarity(AetherRarity rarity) {
        return formatting(rarity.getCustomRarityFormatting());
    }

    public AetherItemSettings formatting(ChatFormatting formatting) {
        this.rarity((Rarity) null);
        this.formatting = formatting;
        return this;
    }

    public AetherItemSettings enchantmentGlintColor(int color) {
        this.enchantmentGlintColor = color;
        return this;
    }

    public AetherItemSettings tab(CreativeModeTab group) {
        super.tab(group);
        return this;
    }

    public AetherItemSettings rarity(Rarity rarity) {
        super.rarity(rarity);
        return this;
    }

    public ChatFormatting getRarityFormatting() {
//        if (rarity == null && formatting != null){
//            return formatting;
//        } else if (this.rarity != null) {
//            return this.rarity.formatting;
//        } else {
//            return Rarity.COMMON.formatting;
//        }
        return formatting;
    }

    public int getEnchantmentGlintColor() {
        return this.enchantmentGlintColor;
    }
}
