package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyVariable(
            method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private int paradiseLost$capFloatyLeggingsWearDamage(int amount) {
        ItemStack stack = (ItemStack) (Object) this;
        if (!(stack.getItem() instanceof FloatyLeggingsItem) || !stack.isDamageableItem()) {
            return amount;
        }
        int room = Math.max(0, stack.getMaxDamage() - 1 - stack.getDamageValue());
        return Math.min(amount, room);
    }
}
