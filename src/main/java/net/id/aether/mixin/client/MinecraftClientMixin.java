package net.id.aether.mixin.client;

import java.util.concurrent.CompletableFuture;
import net.id.aether.util.AetherSoundEvents;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    
    @Shadow @Nullable public ClientPlayerEntity player;
    
    @Shadow public abstract CompletableFuture<Void> reloadResources();
    
    @ModifyVariable(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient$WorldLoadAction;NONE:Lnet/minecraft/client/MinecraftClient$WorldLoadAction;", ordinal = 0), ordinal = 2, index = 11, name = "bl2", require = 1)
    private boolean replaceBl2(boolean bl2) {
        return false;
    }

    @Inject(
        method = "getMusicType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;",
            ordinal = 0
        ),
        cancellable = true
    )
    private void getMusicType(CallbackInfoReturnable<MusicSound> cir){
        var world = player.world;
        if(world.getRegistryKey().equals(AetherDimension.AETHER_WORLD_KEY)){
            cir.setReturnValue(world.getBiomeAccess().getBiomeForNoiseGen(this.player.getBlockPos()).getMusic().orElse(AetherSoundEvents.Music.AETHER));
        }
    }
}