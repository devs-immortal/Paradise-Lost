package com.aether.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    /*@Shadow public abstract Item getItem();

    @Redirect(method = "getTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Rarity;formatting:Lnet/minecraft/util/Formatting;", opcode = Opcodes.GETFIELD))
    private Formatting getCustomRarityFormattingForTooltip(Rarity rarity) {
        if (rarity != null) return rarity.formatting;
        return ((AetherItemExtensions) this.getItem()).getCustomRarityFormatting();
    }*/
}
