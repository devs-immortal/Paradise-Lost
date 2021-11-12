package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.devel.AetherDevel;
import net.id.aether.duck.client.ShaderDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.Shader;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(VertexBuffer.class)
public abstract class VertexBufferMixin{
    /*
    @Inject(
        method = "innerSetShader",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setupShaderLights(Lnet/minecraft/client/render/Shader;)V"
        )
    )
    private void innerSetShader(Matrix4f viewMatrix, Matrix4f projectionMatrix, Shader shader, CallbackInfo ci){
        var accessibleShader = (Shader & ShaderDuck)shader;
        var uniform = accessibleShader.the_aether$getTime();
        if(shader.getName().contains("aether")){
            AetherDevel.breakpointSpotForBrokenMixinDebugging();
        }
        if(uniform != null){
            var world = MinecraftClient.getInstance().world;
            if(world != null){
                uniform.set(world.getTime());
            }else{
                uniform.set(0);
            }
        }
    }
    */
}
