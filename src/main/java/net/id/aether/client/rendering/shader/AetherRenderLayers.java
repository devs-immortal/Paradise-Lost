package net.id.aether.client.rendering.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.mixin.client.render.RenderLayerAccessor;
import net.id.aether.mixin.client.render.RenderPhaseAccessor;
import net.id.aether.util.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.id.aether.client.rendering.shader.AetherShaders.locate;

@Environment(EnvType.CLIENT)
public final class AetherRenderLayers{
    private AetherRenderLayers(){}
    
    public static final RenderLayer AURAL;
    public static final RenderLayer AURAL_CUTOUT_MIPPED;
    private static final Map<Identifier, RenderLayer> CUBEMAPS = new HashMap<>();
    private static final Map<Identifier, RenderPhase.TextureBase> CUBEMAP_TEXTURES = new HashMap<>();
    
    static{
        if(Config.SODIUM_WORKAROUND){
            AURAL = RenderLayer.getSolid();
            AURAL_CUTOUT_MIPPED = RenderLayer.getCutoutMipped();
        }else{
            AURAL = RenderLayerAccessor.invokeOf(locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
            AURAL_CUTOUT_MIPPED = RenderLayerAccessor.invokeOf(locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL_CUTOUT_MIPPED).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
        }
    }
    
    static void init(){}
    
    public static RenderLayer getCubemapLayer(Identifier identifier){
        synchronized(CUBEMAPS){
            return CUBEMAPS.computeIfAbsent(identifier, AetherRenderLayers::createCubeMap);
        }
    }
    
    private static RenderLayer createCubeMap(Identifier identifier){
        return RenderLayerAccessor.invokeOf(
            identifier.getNamespace() + ":cubemap/" + identifier.getPath(),
            AetherVertexFormats.POSITION_COLOR_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS,
            0x20000, true, false,
            RenderLayer.MultiPhaseParameters.builder()
                .lightmap(RenderPhaseAccessor.getEnableLightmap())
                .shader(AetherRenderPhases.CUBEMAP)
                .texture(CUBEMAP_TEXTURES.computeIfAbsent(identifier, (key)->new CubemapTexture(identifier)))
                .build(true)
        );
    }
    
    public static List<RenderLayer> getBlockLayers(){
        if(Config.SODIUM_WORKAROUND){
            return List.of();
        }
        return Stream.of(
            Stream.of(AURAL, AURAL_CUTOUT_MIPPED),
            CUBEMAPS.values().stream()
        ).flatMap(Function.identity()).toList();
    }
    
    private static final class CubemapTexture extends RenderPhase.TextureBase{
        private final Optional<Identifier> id;
    
        public CubemapTexture(Identifier id){
            super(()->{
                RenderSystem.enableTexture();
                TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                textureManager.getTexture(id);
                RenderSystem.setShaderTexture(0, id);
            }, ()->{});
            this.id = Optional.of(id);
        }
    
        public String toString(){
            return name + '[' + id + ']';
        }
    
        protected Optional<Identifier> getId(){
            return id;
        }
    }
}
