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

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    ClientPlayerEntity player;

    @Inject(method = "getMusicType", at = @At(value = "RETURN"), cancellable = true)
    void getMusicType(CallbackInfoReturnable<MusicSound> cir) {
        if (this.player != null && this.player.getWorld().getRegistryKey() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            cir.setReturnValue(ParadiseLostSoundEvents.PARADISE_MUSIC_SOUND);
        }
    }
}
