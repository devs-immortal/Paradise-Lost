package net.id.aether.client.rendering.shader;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.csl.api.v0.duck.ShaderDuck;
import net.gudenau.minecraft.csl.api.v0.event.ShaderEvents;
import net.id.aether.Aether;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;

import java.io.IOException;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class AetherShaders{
    private AetherShaders(){}
    
    public static void init(){
        AetherRenderLayers.init();
    
        ShaderEvents.CREATE.register((manager)->{
            try{
                return List.of(
                    Pair.of(new Shader(manager, locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader)->{
                        aural = shader;
                        ShaderDuck duck = (ShaderDuck)shader;
                        auralTimeUniform = duck.getCustomUniform("Time");
                    }),
                    Pair.of(new Shader(manager, locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader)->{
                        auralCutoutMipped = shader;
                        ShaderDuck duck = (ShaderDuck)shader;
                        auralCutoutTimeUniform = duck.getCustomUniform("Time");
                    }),
                    Pair.of(new Shader(manager, locate("cubemap"), AetherVertexFormats.POSITION_COLOR_LIGHT_NORMAL), (shader)->cubemap = shader)
                );
            }catch(IOException e){
                System.err.print("Failed to load Aether shaders\n");
                e.printStackTrace();
                System.exit(1);
                return List.of();
            }
        });
        ShaderEvents.PRE_RENDER_BLOCKS.register(AetherShaders::preRender);
    }
    
    private static Shader aural;
    private static GlUniform auralTimeUniform;
    
    private static Shader auralCutoutMipped;
    private static GlUniform auralCutoutTimeUniform;
    
    private static Shader cubemap;
    
    static String locate(String name){
        return Aether.MOD_ID + ':' + name;
    }
    
    public static Shader getAural(){
        return aural;
    }
    
    public static Shader getAuralCutoutMipped(){
        return auralCutoutMipped;
    }
    
    public static Shader getCubemap(){
        return cubemap;
    }
    
    private static float auralTime = 0;
    
    public static void preRender(float tickDelta){
        auralTime += tickDelta;
        
        if(auralTimeUniform != null){
            auralTimeUniform.set(auralTime);
        }
        if(auralCutoutTimeUniform != null){
            auralCutoutTimeUniform.set(auralTime);
        }
    }
}
