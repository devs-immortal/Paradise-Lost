package com.aether.mixin.entity;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.Sets;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect effect);

    @ModifyVariable(method = "travel", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private double changeGravity(double gravity) {
        boolean isFalling = this.getDeltaMovement().y <= 0.0D;

        if ((Object) this instanceof Player) {
            Player playerEntity = (Player) (Object) this;
            Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(playerEntity);

            if (componentOptional.isPresent()) {
                // Get parachutes from trinket slots
                final Set<Item> validItems = Sets.newHashSet(AetherItems.CLOUD_PARACHUTE, AetherItems.GOLDEN_CLOUD_PARACHUTE);
                for (Item item : validItems) {
                    if (componentOptional.get().isEquipped(item)) {
                        if (isFalling && !this.hasEffect(MobEffects.SLOW_FALLING) && !isInWater() && !playerEntity.isShiftKeyDown()) {
                            gravity -= 0.07;
                            this.fallDistance = 0;
                        }
                        break;
                    }
                }
            }
        }

        return gravity;
    }

    @Inject(at = @At("RETURN"), method = "hurt")
    void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getEntity();
        if (cir.getReturnValue() && attacker instanceof LivingEntity) {
            Item item = ((LivingEntity) attacker).getMainHandItem().getItem();
            if (item instanceof TieredItem && ((TieredItem) item).getTier() == AetherTiers.Gravitite.getDefaultTier()) {
                this.push(0, amount / 20 + 0.1, 0);
            }
        }
    }
}
