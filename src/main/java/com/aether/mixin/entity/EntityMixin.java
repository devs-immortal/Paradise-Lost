package com.aether.mixin.entity;

import com.aether.api.AetherAPI;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"), index = 3)
    private float modifyFallDistance(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (entity instanceof PlayerEntity) {
            return AetherAPI.get((PlayerEntity) entity).disableFallDamage() ? 0.0F : fallDistance;
        }

        return fallDistance;
    }
}
