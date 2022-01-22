package net.id.aether.mixin.client.render;

import net.id.aether.client.rendering.shader.AetherRenderLayers;
import net.id.aether.client.rendering.shader.AetherShaders;
import net.id.aether.util.Config;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.HeightLimitView;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow protected abstract void renderLayer(RenderLayer renderLayer, MatrixStack matrices, double d, double e, double f, Matrix4f matrix4f);
    
    @Shadow public abstract void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f);
    
    @Shadow @Nullable private ClientWorld world;
    
    /**
     * Disables the void fog in the Aether. Used to be a pair of redirects.
     *
     * @author gudenau 
     */
    @ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight(Lnet/minecraft/world/HeightLimitView;)D",
            shift = At.Shift.BY,
            by = 3
        )
    )
    private double dontRenderVoid(double original){
        return world != null && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY ? 0 : original;
    }
    
    /**
     * This injection is critical for the proper rendering of our custom render layers. Without this some of our blocks
     * will never render.
     *
     * @author gudenau
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLnet/minecraft/util/math/Matrix4f;)V",
            shift = At.Shift.AFTER,
            ordinal = 2
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(
        MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f,
        CallbackInfo ci,
        Profiler profiler, boolean hasNoUpdaters, Vec3d camPos, double camX, double camY, double camZ
    ){
        // Sodium doesn't support custom render layers, if we try to render ours Sodium crashes.
        if(Config.SODIUM_WORKAROUND){
            return;
        }
        AetherShaders.preRender(tickDelta);
        for(RenderLayer layer : AetherRenderLayers.getBlockLayers()){
            renderLayer(layer, matrices, camX, camY, camZ, matrix4f);
        }
    }
}
