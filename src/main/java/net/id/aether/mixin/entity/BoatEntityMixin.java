package net.id.aether.mixin.entity;

import net.id.aether.entities.vehicle.AetherBoatTypes;
import net.id.aether.items.AetherItems;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Shadow
    public abstract BoatEntity.Type getBoatType();

    @Inject(method = "asItem", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;OAK_BOAT:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC), cancellable = true)
    private void checkAetherBoats(CallbackInfoReturnable<Item> cir) {
        BoatEntity.Type type = this.getBoatType();
        if (type != BoatEntity.Type.OAK) {
            if (type == AetherBoatTypes.SKYROOT) cir.setReturnValue(AetherItems.SKYROOT_BOAT);
            else if (type == AetherBoatTypes.GOLDEN_OAK) cir.setReturnValue(AetherItems.GOLDEN_OAK_BOAT);
            else if (type == AetherBoatTypes.ORANGE) cir.setReturnValue(AetherItems.ORANGE_BOAT);
            else if (type == AetherBoatTypes.CRYSTAL) cir.setReturnValue(AetherItems.CRYSTAL_BOAT);
            else if (type == AetherBoatTypes.WISTERIA) cir.setReturnValue(AetherItems.WISTERIA_BOAT);
        }
    }
}
