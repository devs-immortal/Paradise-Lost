package net.id.aether.client.rendering.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.csl.api.v0.CustomRenderLayers;
import net.gudenau.minecraft.csl.api.v0.RenderLayerBuilder;
import net.id.aether.util.Config;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.screen.PlayerScreenHandler;

import static net.id.aether.Aether.locate;

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
            var builder = RenderLayerBuilder.createBuilder();
            builder.vertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)
                .drawMode(VertexFormat.DrawMode.QUADS)
                .expectedBufferSize(0x20000)
                .hasCrumbling(true)
                .translucent(false)
                .outlineMode(RenderLayerBuilder.OutlineMode.AFFECTS_OUTLINE)
                .lightmap(true)
                .texture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, false, true);
            
            AURAL = builder.shader(AetherShaders::getAural)
                .build(locate("aural"));
            AURAL_CUTOUT_MIPPED = builder.shader(AetherShaders::getAuralCutoutMipped)
                .build(locate("aural_cutout_mipped"));
    
            CustomRenderLayers.getInstance().registerBlockRenderLayers(
                AURAL,
                AURAL_CUTOUT_MIPPED
            );
        }
    }
    
    static void init(){}
}
