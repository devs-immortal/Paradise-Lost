package com.aether.mixin.server;

import com.aether.Aether;
import com.aether.entities.block.FloatingBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.Logger;
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

    @Shadow @Final private Int2ObjectMap<Entity> entitiesById;

    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At(value = "RETURN"), method = "tick")
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (this.idleTimeout < 300) {
            for (Int2ObjectMap.Entry<Entity> entry : entitiesById.int2ObjectEntrySet()) {
                if (entry.getValue() instanceof FloatingBlockEntity) {
                    FloatingBlockEntity entity = (FloatingBlockEntity) entry.getValue();
                    entity.postTickEntities();
                } else if (entry.getValue() == null) {
                    Aether.LOG.error("Started checking null entities in ServerWorldMixin::postEntityTick");
                    return;
                }
            }
        }
    }
}
