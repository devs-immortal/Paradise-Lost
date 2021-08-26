package com.aether.mixin.server;

import com.aether.Aether;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.entities.util.floatingblock.FloatingBlockStructure;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Shadow private int idleTimeout;

    @Shadow @Final EntityList entityList;

    @Inject(method = "tick", at = @At(value = "RETURN"))
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (this.idleTimeout < 300) {
            entityList.forEach(entityObj -> {
                if (entityObj instanceof FloatingBlockEntity entity) {
                    entity.postTickEntities();
                } else if (entityObj == null) {
                    Aether.LOG.error("Started checking null entities in ServerWorldMixin::postEntityTick");
                }
            });
            FloatingBlockStructure[] structures = FloatingBlockStructure.getAllStructures().toArray(new FloatingBlockStructure[0]);
            for(FloatingBlockStructure structure : structures){
                structure.postTick();
            }
        }
    }
}
