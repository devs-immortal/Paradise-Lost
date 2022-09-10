package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.entities.misc.RookEntity;
import net.id.incubus_core.devel.Devel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(
        method = "renderHitbox",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if(!Devel.isDevel() && entity instanceof RookEntity) {
            ci.cancel();
        }
    }
}
