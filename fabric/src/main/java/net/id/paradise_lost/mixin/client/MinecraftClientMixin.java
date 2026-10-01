package net.id.paradise_lost.mixin.client;

import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    LocalPlayer player;

    @Inject(method = "getSituationalMusic", at = @At(value = "RETURN"), cancellable = true)
    void getMusicType(CallbackInfoReturnable<Music> cir) {
        if (this.player != null && this.player.level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            cir.setReturnValue(ParadiseLostSoundEvents.PARADISE_MUSIC_SOUND);
        }
    }
}
