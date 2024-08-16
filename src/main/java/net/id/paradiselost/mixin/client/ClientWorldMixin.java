package net.id.paradiselost.mixin.client;

import net.id.paradiselost.api.BlockLikeSet;
import net.id.paradiselost.entities.util.PostTickEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Shadow
    @Final
    EntityList entityList;

    @Inject(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"))
    void postEntityTick(CallbackInfo ci) {
        entityList.forEach(entity -> {
            if (entity instanceof PostTickEntity postTickEntity) {
                postTickEntity.incubus_Concern$postTick();
            }
        });
        BlockLikeSet.getAllSets().forEachRemaining(BlockLikeSet::postTick);
    }
}
