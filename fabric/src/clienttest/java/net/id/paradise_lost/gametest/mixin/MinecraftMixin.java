package net.id.paradise_lost.gametest.mixin;

import net.id.paradise_lost.gametest.ClientTestRunner;
import net.id.paradise_lost.gametest.TickEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void clienttest$showTest(CallbackInfoReturnable<String> cir) {
        if (ClientTestRunner.title != null) cir.setReturnValue(ClientTestRunner.title);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void clienttest$endTick(CallbackInfo ci) {
        TickEvents.CLIENT_END.forEach(Runnable::run);
    }
}
