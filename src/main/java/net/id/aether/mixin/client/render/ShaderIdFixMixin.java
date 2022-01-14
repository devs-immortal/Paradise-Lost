package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.minecraft.client.render.Shader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Environment(EnvType.CLIENT)
@Mixin(Shader.class)
public abstract class ShaderIdFixMixin{
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
            if(id.startsWith(Aether.MOD_ID + ':')){
                return id;
            }
            var split = id.split(":", 2);
            return Aether.MOD_ID + ":shaders/core/" + split[1];
        }
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
