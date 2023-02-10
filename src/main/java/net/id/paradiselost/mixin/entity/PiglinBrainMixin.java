package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.items.armor.ParadiseLostArmorMaterials;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(method = "wearsGoldArmor", at = @At("TAIL"), cancellable = true)
    private static void wearsGoldArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        Iterable<ItemStack> iterable = entity.getArmorItems();
        for (ItemStack itemStack : iterable) {
            Item item = itemStack.getItem();
            if (!(item instanceof ArmorItem) || ((ArmorItem)item).getMaterial() != ParadiseLostArmorMaterials.GLAZED_GOLD) continue;
            cir.setReturnValue(true);
        }
    }

}
