package com.aether.mixin.client;

import com.aether.client.rendering.map.AetherMap;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.block.MapColor;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public class MapRendererMixin {

    @Shadow
    @Final
    private MapState state;

    @Shadow
    @Final
    private NativeImageBackedTexture texture;

    @Inject(method = "updateTexture()V", at = @At(value = "RETURN", target = "net/minecraft/client/render/MapRenderer$MapTexture.updateTexture()V"))
    private void updateAetherTexture(CallbackInfo ci) {
        boolean isAether = this.state.dimension == AetherDimension.AETHER_WORLD_KEY;
        for (int int_1 = 0; int_1 < 128; ++int_1) {
            for (int int_2 = 0; int_2 < 128; ++int_2) {
                int int_3 = int_2 + int_1 * 128;
                int int_4 = this.state.colors[int_3] & 255;

                if (int_4 / 4 == 0) {
                    this.texture.getImage().setPixelColor(int_2, int_1, (int_3 + int_3 / 128 & 1) * 8 + 16 << 24);
                } else {
                    int color = MapColor.COLORS[int_4 / 4].getRenderColor(int_4 & 3);

                    if (isAether) color = AetherMap.getColor(MapColor.COLORS[int_4 / 4], int_4 & 3);

                    this.texture.getImage().setPixelColor(int_2, int_1, color);
                }
            }
        }
        this.texture.upload();
    }
}