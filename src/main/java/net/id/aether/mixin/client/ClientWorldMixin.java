package net.id.aether.mixin.client;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.entities.util.floatingblock.FloatingBlockStructure;
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

    @Inject(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"))
    void postEntityTick(CallbackInfo ci) {
        entityList.forEach(entityObj -> {
            if (entityObj instanceof FloatingBlockEntity entity) {
                entity.postTickEntities();
            } else if (entityObj instanceof FloatingBlockEntity.PostTickEntity entity) {
                entity.postTick();
            }
        });
        FloatingBlockStructure[] structures = FloatingBlockStructure.getAllStructures().toArray(new FloatingBlockStructure[0]);
        for(FloatingBlockStructure structure : structures){
            structure.postTick();
        }
    }
}
