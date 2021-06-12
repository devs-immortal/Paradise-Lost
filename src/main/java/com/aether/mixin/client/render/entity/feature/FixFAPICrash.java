package com.aether.mixin.client.render.entity.feature;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: [1.17] Fabric API does not contain this for the time being, uncomment when it's back or rewrite this.
//@Mixin(ArmorRenderingRegistryImpl.class)
public class FixFAPICrash {
//    @Inject(method = "getArmorTexture", at = @At("RETURN"), remap = false, cancellable = true)
//    private static void getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, Identifier defaultTexture, CallbackInfoReturnable<Identifier> cir) {
//        if (cir.getReturnValue() == null) {
//            cir.setReturnValue(defaultTexture);
//        }
//    }
}
