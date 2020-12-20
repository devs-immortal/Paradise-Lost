package com.aether.mixin.entity;

import com.aether.Aether;
import com.aether.items.AetherItems;
import com.google.common.collect.Sets;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

		if ((Object)this instanceof PlayerEntity) {
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
}
