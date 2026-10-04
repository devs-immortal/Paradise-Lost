package net.id.paradise_lost.mixin.server;

import com.mojang.authlib.GameProfile;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.id.paradise_lost.platform.Services;

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
}
