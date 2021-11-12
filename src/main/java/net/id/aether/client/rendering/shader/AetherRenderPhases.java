package net.id.aether.client.rendering.shader;

import net.minecraft.client.render.RenderPhase;

public final class AetherRenderPhases{
    private AetherRenderPhases(){}
    
    public static final RenderPhase.Shader AURAL = new RenderPhase.Shader(AetherShaders::getAural);
    public static final RenderPhase.Shader AURAL_CUTOUT_MIPPED = new RenderPhase.Shader(AetherShaders::getAuralCutoutMipped);
    public static final RenderPhase.Shader CUBEMAP = new RenderPhase.Shader(AetherShaders::getCubemap);
    
    static void init(){}
}
