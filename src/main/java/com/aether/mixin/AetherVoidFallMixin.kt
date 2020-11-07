package com.aether.mixin

import com.aether.util.CustomStatusEffectInstance
import com.aether.world.dimension.AetherDimension
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World
import org.apache.logging.log4j.core.jmx.Server
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
abstract class AetherVoidFallMixin : Entity(null, null) {

    @Inject(method = ["damage"], at = [At("HEAD")], cancellable = true)
    fun damage(source: DamageSource, amount: Float, cir: CallbackInfoReturnable<Boolean>) {
        if (world.registryKey == AetherDimension.AETHER_WORLD_KEY) {
            if (!world.isClient()) {
                (this as ServerPlayerEntity).teleport(server.getWorld(World.OVERWORLD), this.getX(), world.height.toDouble(), this.getZ(), this.yaw, this.pitch)
                var ef = CustomStatusEffectInstance(StatusEffect.byRawId(9), 160, 2)
                ef.ShowParticles = false
                (this as ServerPlayerEntity).addStatusEffect(ef);
            }
            cir.returnValue = false
            return
        }
    }
}