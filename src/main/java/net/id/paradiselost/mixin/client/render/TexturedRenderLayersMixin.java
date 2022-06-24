package net.id.paradiselost.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.texture.ParadiseLostTextures;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin{
    @Inject(
        method = "addDefaultTextures",
        at = @At("TAIL")
    )
    private static void addDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo ci){
        ParadiseLostTextures.addDefaultTextures(adder);
    }
}
