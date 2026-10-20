package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.Recorder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "displayItemActivation", at = @At("HEAD"))
    private void testmod$recordActivation(ItemStack stack, CallbackInfo ci) {
        Recorder.itemActivations.add(stack.copy());
    }
}
