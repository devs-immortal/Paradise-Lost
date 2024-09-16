package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.ParadiseLostItems.BoatSet;
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
    public abstract BoatEntity.Type getVariant();

    @Inject(method = "asItem", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;OAK_BOAT:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC), cancellable = true)
    private void checkCustomBoats(CallbackInfoReturnable<Item> cir) {
        BoatEntity.Type type = this.getVariant();
        if (type != BoatEntity.Type.OAK) {
            for (var entry : ParadiseLostItems.BOAT_SETS) {
                if (type == entry.type()) {
                    cir.setReturnValue(entry.boat());
                }
            }
        }
    }
}
