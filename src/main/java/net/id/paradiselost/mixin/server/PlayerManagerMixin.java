package net.id.paradiselost.mixin.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(
            method = "createPlayer",
            at = @At("RETURN")
    )
    private void createPlayer(GameProfile profile, SyncedClientOptions syncedOptions, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            cir.getReturnValue().giveItemStack(new ItemStack(ParadiseLostItems.PARADISE_LOST_PORTAL));
        }
    }
}
