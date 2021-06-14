package com.aether.mixin.client;

import com.aether.entities.block.FloatingBlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Shadow
    @Final
    EntityList entityList;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"), method = "tickEntities")
    void postEntityTick(CallbackInfo ci) {
        entityList.forEach(entityObj -> {
            if (entityObj instanceof FloatingBlockEntity entity) {
                entity.postTickEntities();
            }
            if (entityObj instanceof FloatingBlockEntity.ICPEM entity) {
                entity.postTick();
            }
        });
    }
}
