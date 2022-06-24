package net.id.paradiselost.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

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
        if(identifier.getNamespace().equals(ParadiseLost.MOD_ID)){
            // MC uses Stream.toList, we use it here to make sure we still look legitimate
            return Stream.concat(
                resources.stream(),
                Stream.of(ParadiseLostSoundEvents.createResource())
            ).toList();
        }
        return resources;
    }
}
