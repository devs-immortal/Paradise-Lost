package com.aether.mixin.server;

import com.aether.items.AetherItems;
import com.aether.world.AetherGameRules;
import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(method = "createPlayer", at = @At("RETURN"))
	private void givePlayerAetherPortal(GameProfile profile, CallbackInfoReturnable<ServerPlayerEntity> cir) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			cir.getReturnValue().giveItemStack(new ItemStack(AetherItems.AETHER_PORTAL));
		}
	}

	@Inject(method = "sendWorldInfo", at = @At("HEAD"))
	private void syncMaxQuicksoilVelocity(ServerPlayerEntity player, ServerWorld world, CallbackInfo ci) {
		player.networkHandler.sendPacket(AetherGameRules.getMaxQuicksoilSpeedSyncPacket(world.getGameRules().get(AetherGameRules.MAX_QUICKSOIL_SPEED).get()));
	}
}
