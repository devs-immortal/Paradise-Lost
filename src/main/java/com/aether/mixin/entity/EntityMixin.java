package com.aether.mixin.entity;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin {

    // TODO: Uncomment when more of the Api is implemented
    /*@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"), index = 3)
    private float modifyFallDistance(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (entity instanceof PlayerEntity) {
            return AetherAPI.get((PlayerEntity) entity).disableFallDamage() ? 0.0F : fallDistance;
        }

        return fallDistance;
    }*/
}