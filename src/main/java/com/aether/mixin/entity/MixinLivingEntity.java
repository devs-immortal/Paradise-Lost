package com.aether.mixin.entity;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.Sets;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @ModifyVariable(method = "travel", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
    private double changeGravity(double gravity) {
        boolean isFalling = this.getVelocity().y <= 0.0D;

        //noinspection ConstantConditions
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) (Object) this;

            // Get parachutes from trinket slots
            boolean hasParachute = TrinketsApi.getTrinketsInventory(playerEntity).containsAny(Sets.newHashSet(AetherItems.CLOUD_PARACHUTE, AetherItems.GOLDEN_CLOUD_PARACHUTE));

            if (hasParachute && isFalling && !this.hasStatusEffect(StatusEffects.SLOW_FALLING) && !isTouchingWater() && !playerEntity.isSneaking()) {
                gravity -= 0.07;
                this.fallDistance = 0;
            }
        }

        return gravity;
    }

    @Inject(at = @At("RETURN"), method = "damage")
    void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (cir.getReturnValue() && attacker instanceof LivingEntity) {
            Item item = ((LivingEntity) attacker).getMainHandStack().getItem();
            if (item instanceof ToolItem && ((ToolItem) item).getMaterial() == AetherTiers.Gravitite.getDefaultTier()) {
                this.addVelocity(0, amount / 20 + 0.1, 0);
            }
        }
    }
}
