package net.id.paradiselost.mixin.item;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
        method = "canCombine",
        at = @At("TAIL"),
        cancellable = true
    )
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
