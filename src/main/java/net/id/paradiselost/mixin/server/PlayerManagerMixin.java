package net.id.paradiselost.mixin.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.world.ParadiseLostGameRules;
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
    @Inject(
            method = "createPlayer",
            at = @At("RETURN")
    )
    private void createPlayer(GameProfile profile, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            cir.getReturnValue().giveItemStack(new ItemStack(ParadiseLostItems.PARADISE_LOST_PORTAL));
        }
    }
}
