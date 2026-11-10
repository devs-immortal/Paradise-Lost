package net.id.paradise_lost.mixin.server;

import com.mojang.authlib.GameProfile;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.util.GameRuleHelper;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(
            method = "getPlayerForLogin",
            at = @At("RETURN")
    )
    private void createPlayer(GameProfile profile, ClientInformation syncedOptions, CallbackInfoReturnable<ServerPlayer> cir) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            cir.getReturnValue().addItem(new ItemStack(ItemRegistry.PARADISE_LOST_PORTAL.get()));
        }
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void paradiseLost$syncAnchoredRunOnJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        GameRuleHelper.syncAnchoredRun(player);
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    private void paradiseLost$syncAnchoredRunOnRespawn(ServerPlayer player, boolean keepInventory, Entity.RemovalReason reason, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer respawned = cir.getReturnValue();
        if (respawned != null) {
            GameRuleHelper.syncAnchoredRun(respawned);
        }
    }
}
