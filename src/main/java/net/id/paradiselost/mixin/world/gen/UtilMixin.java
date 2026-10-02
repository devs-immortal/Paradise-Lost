package net.id.paradiselost.mixin.world.gen;

import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Util.class)
public class UtilMixin {

    @Inject(
            method = "logErrorOrPause(Ljava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void shutUpSetblockError(String message, CallbackInfo cir) {
        if (message.contains("paradise_lost:")) {
            cir.cancel();
        }
    }

}
