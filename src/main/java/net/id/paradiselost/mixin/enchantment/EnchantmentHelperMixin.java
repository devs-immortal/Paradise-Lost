package net.id.paradiselost.mixin.enchantment;

import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getFireAspect", at = @At("HEAD"), cancellable = true)
    private static void getFireAspect(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity.getStackInHand(entity.getActiveHand()).isIn(ParadiseLostItemTags.IGNITING_TOOLS)) cir.setReturnValue(2);
    }
}
