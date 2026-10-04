package net.id.paradise_lost.mixin.server;

import net.id.paradise_lost.api.BlockLikeSet;
import net.id.paradise_lost.entity.util.PostTickEntity;
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
public abstract class ServerWorldMixin {

    @Shadow
    @Final
    EntityTickList entityTickList;
    @Shadow
    private int emptyTime;

    @Inject(method = "tick", at = @At(value = "RETURN"))
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (this.emptyTime < 300) {
            entityTickList.forEach(entityObj -> {
                if (entityObj instanceof PostTickEntity entity) {
                    entity.incubus_Concern$postTick();
                }
            });
            BlockLikeSet.getAllSets().forEachRemaining(BlockLikeSet::postTick);
        }
    }
}
