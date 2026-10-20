package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.Recorder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "die", at = @At("TAIL"))
    private void testmod$recordDeath(DamageSource source, CallbackInfo ci) {
        Recorder.playerDeaths.add(source);
    }
}
