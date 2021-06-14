package com.aether.mixin.item;

import com.aether.items.AetherItemSettings;
import com.aether.util.item.AetherItemExtensions;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin implements AetherItemExtensions {
    @Shadow @Final private Rarity rarity;
    private Formatting customRarity = null;
    private int enchantmentGlintColor = 0x8e48e1;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setCustomRarity(Item.Settings settings, CallbackInfo ci) {
        if (settings instanceof AetherItemSettings) {
            customRarity = ((AetherItemSettings) settings).getRarityFormatting();
            enchantmentGlintColor = ((AetherItemSettings) settings).getEnchantmentGlintColor();
        }
    }

    @Override
    public Formatting getCustomRarityFormatting() {
        if (customRarity != null) {
            return customRarity;
        } else if (rarity != null) {
            return rarity.formatting;
        } else {
            return Rarity.COMMON.formatting;
        }
    }

    @Override
    public int getCustomEnchantmentGlintColor() {
        return enchantmentGlintColor;
    }
}
