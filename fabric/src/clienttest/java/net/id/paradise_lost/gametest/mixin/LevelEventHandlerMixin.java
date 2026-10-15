package net.id.paradise_lost.gametest.mixin;

import net.id.paradise_lost.gametest.Recorder;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {
    @Inject(method = "levelEvent(ILnet/minecraft/core/BlockPos;I)V", at = @At("HEAD"))
    private void clienttest$recordLevelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        Recorder.levelEvents.add(type + "@" + pos.toShortString());
    }
}
