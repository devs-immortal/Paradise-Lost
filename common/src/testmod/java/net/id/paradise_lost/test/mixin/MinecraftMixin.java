package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.client.ClientTestRunner;
import net.id.paradise_lost.test.TickEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void testmod$showTest(CallbackInfoReturnable<String> cir) {
        if (ClientTestRunner.title != null) cir.setReturnValue(ClientTestRunner.title);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void testmod$endTick(CallbackInfo ci) {
        TickEvents.CLIENT_END.forEach(Runnable::run);
    }
}
