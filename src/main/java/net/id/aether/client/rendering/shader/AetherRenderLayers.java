package net.id.aether.client.rendering.shader;

import java.util.List;
import net.id.aether.mixin.client.render.RenderLayerAccessor;
import net.id.aether.mixin.client.render.RenderPhaseAccessor;
import net.minecraft.client.render.*;

import static net.id.aether.client.rendering.shader.AetherShaders.locate;

public final class AetherRenderLayers{
    private AetherRenderLayers(){}
    
    public static final RenderLayer AURAL = RenderLayerAccessor.invokeOf(locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
    public static final RenderLayer AURAL_CUTOUT_MIPPED = RenderLayerAccessor.invokeOf(locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL_CUTOUT_MIPPED).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
    
    static void init(){}
    
    public static List<RenderLayer> getBlockLayers(){
        return List.of(AURAL, AURAL_CUTOUT_MIPPED);
    }
}
