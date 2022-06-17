package net.id.aether.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.util.AetherSoundEvents;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin{
    @Redirect(
        method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/sound/SoundManager$SoundList;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/ResourceManager;getAllResources(Lnet/minecraft/util/Identifier;)Ljava/util/List;"
        )
    )
    private List<Resource> prepare(ResourceManager instance, Identifier identifier){
        var resources = instance.getAllResources(identifier);
        if(identifier.getNamespace().equals(Aether.MOD_ID)){
            // MC uses Stream.toList, we use it here to make sure we still look legitimate
            return Stream.concat(
                resources.stream(),
                Stream.of(AetherSoundEvents.createResource())
            ).toList();
        }
        return resources;
    }
}
