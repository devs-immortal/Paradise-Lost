package net.id.paradiselost.mixin.item;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin {
    /*@Shadow @Final private Rarity rarity;
    private Formatting customRarity = null;
    private int enchantmentGlintColor = 0x8e48e1;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setCustomRarity(Item.Settings settings, CallbackInfo ci) {
        if (settings instanceof ParadiseLostItemSettings) {
            customRarity = ((ParadiseLostItemSettings) settings).getRarityFormatting();
            enchantmentGlintColor = ((ParadiseLostItemSettings) settings).getEnchantmentGlintColor();
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
    }*/
}
