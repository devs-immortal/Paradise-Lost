package net.id.paradise_lost.mixin.client.sound;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerPortalSoundMixin {

    @WrapOperation(
            method = "handleConfusionTransitionEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/sounds/SimpleSoundInstance;"
                            + "forLocalAmbience(Lnet/minecraft/sounds/SoundEvent;FF)"
                            + "Lnet/minecraft/client/resources/sounds/SimpleSoundInstance;"
            )
    )
    private SimpleSoundInstance paradiseLost$useParadisePortalTrigger(
            SoundEvent sound,
            float pitch,
            float volume,
            Operation<SimpleSoundInstance> original
    ) {
        LocalPlayer self = (LocalPlayer) (Object) this;

        if (paradiseLost$isBluePortal(self)) {
            sound = ParadiseLostSoundEvents.BLOCK_PORTAL_TRIGGER;
        }
        return original.call(sound, pitch, volume);
    }

    @Unique
    private static boolean paradiseLost$isBluePortal(LocalPlayer player) {
        if (player.portalProcess != null) {
            return player.portalProcess.isSamePortal(BlockRegistry.BLUE_PORTAL.get());
        }
        return player.level()
                .getBlockState(player.blockPosition())
                .is(BlockRegistry.BLUE_PORTAL.get());
    }
}
