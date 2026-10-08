package net.id.paradise_lost.clienttest.mixin;

import net.id.paradise_lost.clienttest.ClientTestRunner;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void clienttest$showTest(CallbackInfoReturnable<String> cir) {
        if (ClientTestRunner.title != null) cir.setReturnValue(ClientTestRunner.title);
    }
}