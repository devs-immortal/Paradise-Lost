package com.aether.mixin.entity;

import com.aether.blocks.AetherBlocks;
import com.aether.util.AetherEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements AetherEntity {
    @Shadow
    public World world;
    @Shadow
    private BlockPos blockPos;
    private int aetherTickTime;
    private int aetherCooldown;
    private boolean aetherReady;

    @Inject(method = {"tick"}, at = {@At("TAIL")})
    public void tick(CallbackInfo ci) {
        boolean inPortal = world.getBlockState(blockPos).isOf(AetherBlocks.BLUE_PORTAL);
        if (inPortal) {
            if (aetherCooldown == 0) {
                if (aetherTickTime < 20) {
                    aetherTickTime++;
                } else {
                    aetherCooldown = 40;
                    aetherReady = true;
                }
            }
        } else if (aetherTickTime != 0) {
            aetherTickTime = 0;
        }
        if (!inPortal && aetherCooldown > 0) {
            aetherCooldown--;
            aetherReady = false;
        }
    }

    @Override
    public boolean canUseAetherPortal() {
        return aetherReady;
    }

    @Override
    public void setPortalReady(boolean ready) {
        aetherReady = ready;
    }

    // TODO: Uncomment when more of the Api is implemented
    /*@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"), index = 3)
    private float modifyFallDistance(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (entity instanceof PlayerEntity) {
            return AetherAPI.get((PlayerEntity) entity).disableFallDamage() ? 0.0F : fallDistance;
        }

        return fallDistance;
    }*/
}