package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.Recorder;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderStateShard.class)
public abstract class RenderStateShardMixin {
    @Inject(method = "setupRenderState", at = @At("HEAD"))
    private void testmod$countClouds(CallbackInfo ci) {
        if ((Object) this == RenderType.clouds()) {
            Recorder.cloudCalls.incrementAndGet();
        }
    }
}
