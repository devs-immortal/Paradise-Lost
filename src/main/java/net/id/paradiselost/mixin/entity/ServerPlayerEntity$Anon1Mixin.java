package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.lore.ParadiseLostLore;
import net.id.paradiselost.lore.LoreTriggerType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayerEntity$2")
public abstract class ServerPlayerEntity$Anon1Mixin{
    @Inject(
        method = "onSlotUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/criterion/InventoryChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private void trigger(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci){
        ParadiseLostLore.trigger(LoreTriggerType.ITEM, (ServerPlayerEntity)(Object)this, stack);
    }
}
