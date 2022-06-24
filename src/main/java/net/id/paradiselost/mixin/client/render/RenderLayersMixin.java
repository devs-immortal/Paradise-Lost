package net.id.paradiselost.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.block.RenderLayerOverride;
import net.id.paradiselost.util.Holiday;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(RenderLayers.class)
public class RenderLayersMixin{
    @Shadow private static boolean fancyGraphicsOrBetter;
    
    @Unique private static final boolean paradise_lost$overrideLeavesLayer = Holiday.get() == Holiday.CHRISTMAS;
    
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
        
        //TODO Try to find a better way of doing this, this forces fancy leaves on people that don't want them...
        if(paradise_lost$overrideLeavesLayer && block instanceof LeavesBlock){
            cir.setReturnValue(RenderLayer.getCutoutMipped());
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
