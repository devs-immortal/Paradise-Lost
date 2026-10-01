package net.id.paradise_lost.mixin.server;

import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    private GameType gameModeForPlayer;

    @Unique
    private GameType paradiseLost$floatyPreviousGameMode;

    @Inject(method = "changeGameModeForPlayer", at = @At("HEAD"))
    private void paradiseLost$captureGameMode(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        this.paradiseLost$floatyPreviousGameMode = this.gameModeForPlayer;
    }

    @Inject(method = "changeGameModeForPlayer", at = @At("RETURN"))
    private void paradiseLost$floatyOnGameModeChanged(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (!Boolean.TRUE.equals(cir.getReturnValue()) || this.paradiseLost$floatyPreviousGameMode == null) {
            return;
        }
        FloatyLeggingsItem.onGameModeChanged(this.player, this.paradiseLost$floatyPreviousGameMode, gameMode);
    }
}
