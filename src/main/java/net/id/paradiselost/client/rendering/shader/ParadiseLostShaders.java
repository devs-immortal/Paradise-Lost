package net.id.paradiselost.client.rendering.shader;

import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexFormats;

import static net.id.paradiselost.ParadiseLost.locate;

@Environment(EnvType.CLIENT)
public final class ParadiseLostShaders {
    private ParadiseLostShaders(){}
    
    static final ManagedCoreShader AURAL;
    private static final Uniform1f AURAL_TIME;
    
    static final ManagedCoreShader AURAL_CUTOUT;
    private static final Uniform1f AURAL_CUTOUT_TIME;
    
    static {
        var manager = ShaderEffectManager.getInstance();
        AURAL = manager.manageCoreShader(locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        AURAL_TIME = AURAL.findUniform1f("Time");
        
        AURAL_CUTOUT = manager.manageCoreShader(locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        AURAL_CUTOUT_TIME = AURAL.findUniform1f("Time");
    }
    
    public static void init(){
        ParadiseLostRenderLayers.init();
        EntitiesPreRenderCallback.EVENT.register(ParadiseLostShaders::preRender);
    }
    
    private static float auralTime = 0;
    
    public static void preRender(Camera camera, Frustum frustum, float tickDelta){
        auralTime += tickDelta;
        
        if(AURAL_TIME != null){
            AURAL_TIME.set(auralTime);
        }
        if(AURAL_CUTOUT_TIME != null){
            AURAL_CUTOUT_TIME.set(auralTime);
        }
    }
}
