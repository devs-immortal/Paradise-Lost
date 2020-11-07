package com.aether.mixin

import com.aether.world.dimension.AetherDimension
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
abstract class AetherVoidFallMixin: Entity(null, null) {

    @Inject(method = ["damage"], at = [At("HEAD")], cancellable = true)
    fun damage(source: DamageSource, amount: Float, cir: CallbackInfoReturnable<Boolean>) {
        if(world.registryKey == AetherDimension.AETHER_WORLD_KEY) {
            if(!world.isClient())
                (this as ServerPlayerEntity).teleport(server.getWorld(World.OVERWORLD), this.getX(), world.height.toDouble(), this.getZ(), this.yaw, this.pitch);
            cir.returnValue = false
            return
        }
    }
}