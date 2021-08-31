package net.id.aether.mixin.client;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    /*@Shadow public abstract Item getItem();

    @Redirect(method = "getTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Rarity;formatting:Lnet/minecraft/util/Formatting;", opcode = Opcodes.GETFIELD))
    private Formatting getCustomRarityFormattingForTooltip(Rarity rarity) {
        if (rarity != null) return rarity.formatting;
        return ((AetherItemExtensions) this.getItem()).getCustomRarityFormatting();
    }*/
}
