package net.id.aether.client.rendering.shader;

import ladysnake.satin.api.util.RenderLayerHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.util.Config;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public final class AetherRenderLayers{
    private AetherRenderLayers(){}
    
    public static final RenderLayer AURAL;
    public static final RenderLayer AURAL_CUTOUT_MIPPED;
    
    static{
        if(Config.SODIUM_WORKAROUND){
            AURAL = RenderLayer.getSolid();
            AURAL_CUTOUT_MIPPED = RenderLayer.getCutoutMipped();
        }else{
            AURAL = AetherShaders.AURAL.getRenderLayer(RenderLayer.getSolid());
            AURAL_CUTOUT_MIPPED = AetherShaders.AURAL_CUTOUT.getRenderLayer(RenderLayer.getCutoutMipped());
            
            // This is marked as deprecated for silly reasons...
            //noinspection deprecation
            RenderLayerHelper.registerBlockRenderLayer(AURAL);
            //noinspection deprecation
            RenderLayerHelper.registerBlockRenderLayer(AURAL_CUTOUT_MIPPED);
        }
    }
    
    static void init(){}
}
