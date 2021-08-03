package com.aether.mixin.entity;

import com.aether.Aether;
import com.aether.entities.vehicle.AetherBoatTypes;
import com.aether.items.AetherItems;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.aether.entities.vehicle.AetherBoatTypes.GOLDEN_OAK;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Shadow public abstract BoatEntity.Type getBoatType();

    @Inject(method = "asItem", at = @At("HEAD"), cancellable = true)
    private void checkAetherBoats(CallbackInfoReturnable<Item> cir) {
        BoatEntity.Type type = this.getBoatType();
        if (type == AetherBoatTypes.SKYROOT) cir.setReturnValue(AetherItems.SKYROOT_BOAT);
        else if (type == GOLDEN_OAK) cir.setReturnValue(AetherItems.GOLDEN_OAK_BOAT);
        else if (type == AetherBoatTypes.ORANGE) cir.setReturnValue(AetherItems.ORANGE_BOAT);
        else if (type == AetherBoatTypes.CRYSTAL) cir.setReturnValue(AetherItems.CRYSTAL_BOAT);
        else if (type == AetherBoatTypes.WISTERIA) cir.setReturnValue(AetherItems.WISTERIA_BOAT);
    }
}
