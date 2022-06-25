package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.util.ParadiseLostMapColorUtil;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
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
    private MapState state;

    @Shadow
    @Final
    private NativeImageBackedTexture texture;

    /**
     * Recolors map colors in the paradise_lost dimension to look nicer.
     * Could be a redirect, but that would actually probably be less compatible with other mods.
     * However, if the game updates this won't break, which is a bad thing
     */
    @Inject(
            method = "updateTexture()V",
            at = @At(
                    value = "HEAD",
                    target = "net/minecraft/client/render/MapRenderer$MapTexture.updateTexture()V"
            ),
            cancellable = true)
    private void updateTexture(CallbackInfo ci) {
        if (state.dimension == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            for (int i = 0; i < 128; ++i) {
                for (int j = 0; j < 128; ++j) {
                    int k = j + i * 128;
                    int l = state.colors[k] & 255;

                    if (l >> 2 == 0) { // MapColor.CLEAR
                        // Who knows what this does? Nothing, it seems. But just in case I'll let it stick around.
                        // Comment your code please!
                        texture.getImage().setColor(j, i, (k + k / 128 & 1) * 8 + 16 << 24);
                    } else {
                        texture.getImage().setColor(j, i, ParadiseLostMapColorUtil.getColor(MapColor.COLORS[l >> 2], l & 3));
                    }
                }
            }
            texture.upload();
            ci.cancel();
        }
    }
}
