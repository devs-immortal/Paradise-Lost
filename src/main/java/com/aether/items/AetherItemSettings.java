package com.aether.items;

import com.aether.util.item.AetherRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class AetherItemSettings extends Item.Settings {
    private Formatting formatting = null;
    private int enchantmentGlintColor = 0x8e48e1;

    public AetherItemSettings rarity(AetherRarity rarity) {
        return formatting(rarity.formatting());
    }

    public AetherItemSettings formatting(Formatting formatting) {
        this.formatting = formatting;
        return this;
    }

    public AetherItemSettings enchantmentGlintColor(int color) {
        this.enchantmentGlintColor = color;
        return this;
    }

    public AetherItemSettings group(ItemGroup group) {
        super.group(group);
        return this;
    }

    public AetherItemSettings rarity(Rarity rarity) {
        super.rarity(rarity);
        return this;
    }

    public Formatting getRarityFormatting() {
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
