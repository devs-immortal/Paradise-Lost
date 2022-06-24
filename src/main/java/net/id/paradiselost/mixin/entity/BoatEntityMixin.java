package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.entities.vehicle.ParadiseLostBoatTypes;
import net.id.paradiselost.items.ParadiseLostItems;
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

    @Inject(
        method = "asItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;OAK_BOAT:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        ),
        cancellable = true
    )
    private void asItem(CallbackInfoReturnable<Item> cir) {
        BoatEntity.Type type = getBoatType();
        if (type != BoatEntity.Type.OAK) {
            if (type == ParadiseLostBoatTypes.SKYROOT) {
                cir.setReturnValue(ParadiseLostItems.SKYROOT_BOAT);
            } else if (type == ParadiseLostBoatTypes.GOLDEN_OAK) {
                cir.setReturnValue(ParadiseLostItems.GOLDEN_OAK_BOAT);
            } else if (type == ParadiseLostBoatTypes.ORANGE) {
                cir.setReturnValue(ParadiseLostItems.ORANGE_BOAT);
            } else if (type == ParadiseLostBoatTypes.CRYSTAL) {
                cir.setReturnValue(ParadiseLostItems.CRYSTAL_BOAT);
            } else if (type == ParadiseLostBoatTypes.WISTERIA) {
                cir.setReturnValue(ParadiseLostItems.WISTERIA_BOAT);
            }
        }
    }
}
