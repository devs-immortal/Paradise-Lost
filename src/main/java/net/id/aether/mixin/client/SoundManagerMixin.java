package net.id.aether.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.util.AetherSoundEvents;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin{
    @Inject(
        method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/sound/SoundManager$SoundList;",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/resource/ResourceManager;getAllResources(Lnet/minecraft/util/Identifier;)Ljava/util/List;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void prepare(
        ResourceManager resourceManager, Profiler profiler,
        CallbackInfoReturnable<Object> cir,
        @Coerce Object soundList, Iterator<String> iterator, String namespace, List<Resource> resources
    ){
        if(namespace.equals(Aether.MOD_ID)){
            resources.add(AetherSoundEvents.createResource());
        }
    }
}
