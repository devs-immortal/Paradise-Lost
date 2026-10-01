package net.id.paradise_lost.mixin.client;

import net.id.paradise_lost.api.BlockLikeSet;
import net.id.paradise_lost.entity.util.PostTickEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin {

    @Shadow
    @Final
    EntityTickList tickingEntities;

    @Inject(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;tickBlockEntities()V"))
    void postEntityTick(CallbackInfo ci) {
        tickingEntities.forEach(entity -> {
            if (entity instanceof PostTickEntity postTickEntity) {
                postTickEntity.incubus_Concern$postTick();
            }
        });
        BlockLikeSet.getAllSets().forEachRemaining(BlockLikeSet::postTick);
    }
}
