package net.id.paradiselost.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.VertexBuffer;
import org.spongepowered.asm.mixin.Mixin;

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
        var uniform = accessibleShader.paradise_lost$getTime();
        if(shader.getName().contains("paradise_lost")){
            ParadiseLostDevel.breakpointSpotForBrokenMixinDebugging();
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
