package com.aether.mixin.client;

import com.aether.world.dimension.AetherDimension;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @ModifyArg(method = "renderDarkSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSkyHalf(Lnet/minecraft/client/render/BufferBuilder;FZ)V"), index = 2)
    private boolean modifyBottom(boolean bottom) {
        return world != null && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY || bottom;
    }
}
