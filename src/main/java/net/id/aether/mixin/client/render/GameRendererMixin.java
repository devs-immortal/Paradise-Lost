package net.id.aether.mixin.client.render;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.client.rendering.shader.AetherShaders;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin{
    @Inject(
        method = "loadShaders",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void loadShaders(ResourceManager manager, CallbackInfo ci, List<Program> programs, List<Pair<Shader, Consumer<Shader>>> shaders) throws IOException{
        shaders.addAll(AetherShaders.getShaders(manager));
    }
}
