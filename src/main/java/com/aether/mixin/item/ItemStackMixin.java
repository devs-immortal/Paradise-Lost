package com.aether.mixin.item;

import com.aether.util.item.AetherItemExtensions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Redirect(method = "toHoverableText", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Rarity;formatting:Lnet/minecraft/util/Formatting;", opcode = Opcodes.GETFIELD))
    private Formatting getCustomRarityFormattingForHovering(Rarity rarity) {
        if (rarity != null) return rarity.formatting;
        return ((AetherItemExtensions) this.getItem()).getCustomRarityFormatting();
    }

    @Inject(method = "canCombine", at = @At("TAIL"), cancellable = true)
    private static void combineStackableVariants(ItemStack stack, ItemStack otherStack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && stack.isOf(otherStack.getItem())) {
            ItemStack stackComparable = stack.copy();
            ItemStack otherStackComparable = otherStack.copy();
            stackComparable.getOrCreateNbt().remove("stackableVariant");
            otherStackComparable.getOrCreateNbt().remove("stackableVariant");
            cir.setReturnValue(ItemStack.areNbtEqual(stackComparable, otherStackComparable));
        }
    }
}