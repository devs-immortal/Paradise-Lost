package net.id.paradiselost.mixin.server;

import net.id.paradiselost.api.BlockLikeSet;
import net.id.paradiselost.entities.util.PostTickEntity;
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
public abstract class ServerWorldMixin {

    @Shadow
    @Final
    EntityList entityList;
    @Shadow
    private int idleTimeout;

    @Inject(method = "tick", at = @At(value = "RETURN"))
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (this.idleTimeout < 300) {
            entityList.forEach(entityObj -> {
                if (entityObj instanceof PostTickEntity entity) {
                    entity.incubus_Concern$postTick();
                }
            });
            BlockLikeSet.getAllSets().forEachRemaining(BlockLikeSet::postTick);
        }
    }
}