package com.aether.mixin.server;

import com.aether.Aether;
import com.aether.entities.block.FloatingBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {

    @Shadow private int emptyTime;

    @Shadow @Final EntityTickList entityTickList;

    @Inject(at = @At(value = "RETURN"), method = "tick")
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (this.emptyTime < 300) {
            entityTickList.forEach(entityObj -> {
                if (entityObj instanceof FloatingBlockEntity entity) {
                    entity.postTickEntities();
                } else if (entityObj == null) {
                    Aether.LOG.error("Started checking null entities in ServerWorldMixin::postEntityTick");
                }
            });
        }
    }
}
