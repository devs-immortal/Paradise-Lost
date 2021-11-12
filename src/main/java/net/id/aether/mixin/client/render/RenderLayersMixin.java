package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.client.rendering.block.RenderLayerOverride;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(RenderLayers.class)
public class RenderLayersMixin{
    @Shadow private static boolean fancyGraphicsOrBetter;
    
    @Inject(
        method = "getBlockLayer",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void getBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> cir, Block block){
        if(block instanceof RenderLayerOverride override){
            RenderLayer layer = override.getRenderLayerOverride(fancyGraphicsOrBetter);
            if(layer != null){
                cir.setReturnValue(layer);
            }
        }
    }
    
    @Inject(
        method = "getMovingBlockLayer",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void getMovingBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> cir, Block block){
        if(block instanceof RenderLayerOverride override){
            RenderLayer layer = override.getRenderLayerOverride(fancyGraphicsOrBetter);
            if(layer != null){
                cir.setReturnValue(layer);
            }
        }
    }
}
