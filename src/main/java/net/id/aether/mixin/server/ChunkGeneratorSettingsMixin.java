package net.id.aether.mixin.server;

import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGeneratorSettings.class)
public abstract class ChunkGeneratorSettingsMixin {
    @Shadow private static void register(RegistryKey<ChunkGeneratorSettings> key, ChunkGeneratorSettings settings){}
    
    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void staticInit(CallbackInfo ci) {
        // private static void register(RegistryKey<ChunkGeneratorSettings> registryKey, ChunkGeneratorSettings settings)
        AetherDimension.createChunkGeneratorSettings(ChunkGeneratorSettingsMixin::register);
    }
}
