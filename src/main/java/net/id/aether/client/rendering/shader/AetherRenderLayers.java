package net.id.aether.client.rendering.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.csl.api.v0.CustomRenderLayers;
import net.gudenau.minecraft.csl.api.v0.CustomShaderTexture;
import net.gudenau.minecraft.csl.api.v0.RenderLayerBuilder;
import net.id.aether.util.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL33;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.id.aether.Aether.locate;

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
    
    public static RenderLayer getCubemapLayer(Identifier identifier){
        synchronized(CUBEMAPS){
            return CUBEMAPS.computeIfAbsent(identifier, AetherRenderLayers::createCubeMap);
        }
    }
    
    private static RenderLayer createCubeMap(Identifier identifier){
        var layer = RenderLayerBuilder.createBuilder()
            .vertexFormat(AetherVertexFormats.POSITION_COLOR_LIGHT_NORMAL)
            .drawMode(VertexFormat.DrawMode.QUADS)
            .expectedBufferSize(0x20000)
            .hasCrumbling(true)
            .translucent(false)
            .outlineMode(RenderLayerBuilder.OutlineMode.AFFECTS_OUTLINE)
            .lightmap(true)
            .shader(AetherShaders::getCubemap)
            .texture(CUBEMAP_TEXTURES.computeIfAbsent(identifier, (key)->new CubemapTexture(identifier)))
            .build(new Identifier(identifier.getNamespace(), "cubemap/" + identifier.getPath()));
        CustomRenderLayers.getInstance().registerBlockRenderLayer(layer);
        return layer;
    }
    
    private static final class CubemapTexture extends RenderPhase.TextureBase implements CustomShaderTexture {
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
    
        @Override
        public void bindTexture(int texture) {
            GL33.glBindTexture(GL33.GL_TEXTURE_CUBE_MAP, texture);
        }
    }
}
