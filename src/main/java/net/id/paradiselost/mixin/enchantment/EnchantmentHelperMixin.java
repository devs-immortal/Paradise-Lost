package net.id.paradiselost.mixin.enchantment;

import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "HEAD"))
    private static void onTargetDamaged(ServerWorld world, Entity target, DamageSource damageSource, @Nullable ItemStack weapon, CallbackInfo ci) {
        if (target instanceof LivingEntity && weapon != null && weapon.isIn(ParadiseLostItemTags.IGNITING_TOOLS)) {
            target.setFireTicks(120);
        }
    }

}
