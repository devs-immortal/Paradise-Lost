package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.duck.client.ShaderDuck;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Shader.class)
public abstract class ShaderMixin implements ShaderDuck{
    @Shadow public abstract @Nullable GlUniform getUniform(String name);
    
    @Unique private boolean the_aether$isAetherShader;
    @Unique private GlUniform the_aether$time;
    
    @Unique
    public GlUniform the_aether$getTime(){
        return the_aether$time;
    }
    
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void init(ResourceFactory factory, String name, VertexFormat format, CallbackInfo ci){
        if(the_aether$isAetherShader){
            the_aether$time = getUniform("Time");
        }else{
            the_aether$time = null;
        }
    }
    
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"
        ),
        index = 0
    )
    private String init$fixId(String id){
        if(id.contains(Aether.MOD_ID + ':')){
            the_aether$isAetherShader = true;
            if(id.startsWith(Aether.MOD_ID + ':')){
                return id;
            }
            var split = id.split(":", 2);
            return Aether.MOD_ID + ":shaders/core/" + split[1];
        }
        the_aether$isAetherShader = false;
        return id;
    }
    
    @ModifyVariable(
        method = "loadProgram",
        at = @At(
            value = "LOAD"
        ),
        ordinal = 1,
        slice = @Slice(
            from = @At(
                value = "HEAD"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/resource/ResourceFactory;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"
            )
        )
    )
    private static String loadProgram$fixId(String id){
        if(id.contains(Aether.MOD_ID + ':')){
            if(id.startsWith(Aether.MOD_ID + ':')){
                return id;
            }
            var split = id.split(":", 2);
            return Aether.MOD_ID + ":shaders/core/" + split[1];
        }
        return id;
    }
}
