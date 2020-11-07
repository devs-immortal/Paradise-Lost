package com.aether.mixin.entity;

import com.aether.util.CustomStatusEffectInstance;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            if (!world.isClient()) {
                ((ServerPlayerEntity)(Object)this).teleport(getServer().getWorld(World.OVERWORLD), this.getX(), world.getHeight(), this.getZ(), this.yaw, this.pitch);
                CustomStatusEffectInstance ef = new CustomStatusEffectInstance(StatusEffect.byRawId(9), 160, 2);
                ef.ShowParticles = false;
                ((ServerPlayerEntity)(Object)this).addStatusEffect(ef);
            }
            cir.setReturnValue(false);
        }
    }
}