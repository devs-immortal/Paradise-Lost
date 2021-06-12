package com.aether.mixin.server;

import com.aether.items.AetherItems;
import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
	@Inject(method = "createPlayer", at = @At("RETURN"))
	private void givePlayerAetherPortal(GameProfile profile, CallbackInfoReturnable<ServerPlayer> cir) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			cir.getReturnValue().addItem(new ItemStack(AetherItems.AETHER_PORTAL));
		}
	}
}
