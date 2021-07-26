package com.aether.mixin.entity;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Inject(method = "asItem", at = @At("HEAD"), cancellable = true)
    private void checkAetherBoats(CallbackInfoReturnable<Item> cir) {
        switch (((BoatEntity) (Object) this).getBoatType().getName()) {
            case "aether_skyroot" -> cir.setReturnValue(AetherItems.SKYROOT_BOAT);
            case "aether_golden_oak" -> cir.setReturnValue(AetherItems.GOLDEN_OAK_BOAT);
            case "aether_orange" -> cir.setReturnValue(AetherItems.ORANGE_BOAT);
            case "aether_crystal" -> cir.setReturnValue(AetherItems.CRYSTAL_BOAT);
            case "aether_wisteria" -> cir.setReturnValue(AetherItems.WISTERIA_BOAT);
        }
    }
}
