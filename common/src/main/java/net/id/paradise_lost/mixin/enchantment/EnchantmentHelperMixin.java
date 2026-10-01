package net.id.paradise_lost.mixin.enchantment;

import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "doPostAttackEffectsWithItemSource(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"))
    private static void onTargetDamaged(ServerLevel world, Entity target, DamageSource damageSource, @Nullable ItemStack weapon, CallbackInfo ci) {
        if (target instanceof LivingEntity && weapon != null && weapon.is(ParadiseLostItemTags.IGNITING_TOOLS)) {
            target.setRemainingFireTicks(120);
        }
    }

}
