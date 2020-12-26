package com.aether.mixin.client;

import com.aether.entities.block.FloatingBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
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
    private Int2ObjectMap<Entity> regularEntities;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"), method = "tickEntities")
    void postEntityTick(CallbackInfo ci) {
        for (Int2ObjectMap.Entry<Entity> entry : regularEntities.int2ObjectEntrySet()) {
            if (entry.getValue() instanceof FloatingBlockEntity) {
                FloatingBlockEntity entity = (FloatingBlockEntity) entry.getValue();
                entity.postTickEntities();
            }
            if (entry.getValue() instanceof FloatingBlockEntity.ICPEM) {
                FloatingBlockEntity.ICPEM entity = (FloatingBlockEntity.ICPEM) entry.getValue();
                entity.postTick();
            }
        }
    }
}
