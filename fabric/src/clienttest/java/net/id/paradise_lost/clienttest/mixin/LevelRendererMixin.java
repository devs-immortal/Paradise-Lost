package net.id.paradise_lost.clienttest.mixin;

import net.id.paradise_lost.clienttest.Recorder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = "levelEvent(ILnet/minecraft/core/BlockPos;I)V", at = @At("HEAD"))
    private void clienttest$recordLevelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        Recorder.levelEvents.add(type + "@" + pos.toShortString());
    }
}
