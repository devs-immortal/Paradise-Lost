package net.id.paradiselost.mixin.client;

import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.MusicSound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    
    @Shadow @Nullable public ClientPlayerEntity player;
    
    @Shadow public abstract CompletableFuture<Void> reloadResources();
    
    /*FIXME
    @ModifyVariable(
        method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/MinecraftClient$WorldLoadAction;NONE:Lnet/minecraft/client/MinecraftClient$WorldLoadAction;",
            ordinal = 0
        ),
        ordinal = 2,
        index = 11,
        name = "bl2",
        require = 1
    )
    private boolean replaceBl2(boolean bl2) {
        return false;
    }
     */

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
        if(world.getRegistryKey().equals(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY)){
            cir.setReturnValue(world.getBiomeAccess().getBiomeForNoiseGen(this.player.getBlockPos()).value().getMusic().orElse(ParadiseLostSoundEvents.Music.PARADISE_LOST));
        }
    }
}
