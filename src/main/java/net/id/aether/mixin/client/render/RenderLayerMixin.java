package net.id.aether.mixin.client.render;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.client.rendering.shader.AetherRenderLayers;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(RenderLayer.class)
public class RenderLayerMixin{
    @Inject(
        method = "getBlockLayers",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void getBlockLayers(CallbackInfoReturnable<List<RenderLayer>> cir){
        cir.setReturnValue(Stream.of(
            cir.getReturnValue(),
            AetherRenderLayers.getBlockLayers()
        ).flatMap(List::stream).collect(ImmutableList.toImmutableList()));
    }
}
