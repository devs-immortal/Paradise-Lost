package com.aether.mixin.entity;

import com.aether.util.CustomStatusEffectInstance;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends Entity {

    public PlayerEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isBypassInvul() && getY() < -1 && level.dimension() == AetherDimension.AETHER_WORLD_KEY) {
            if (!level.isClientSide()) {
                ((ServerPlayer) (Object) this).teleportTo(getServer().getLevel(Level.OVERWORLD), this.getX() * 16, level.getHeight(), this.getZ() * 16, this.getYRot(), this.getXRot());
                CustomStatusEffectInstance ef = new CustomStatusEffectInstance(MobEffect.byId(9), 160, 2);
                ef.ShowParticles = false;
                ((ServerPlayer) (Object) this).addEffect(ef);
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
        //TODO: Custom death message on fall from aether death.
    }
}