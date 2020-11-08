package com.aether.mixin.render;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @ModifyVariable(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient$WorldLoadAction;NONE:Lnet/minecraft/client/MinecraftClient$WorldLoadAction;", ordinal = 0), ordinal = 2,index = 11,name = "bl2",require = 1)
    private boolean replaceBl2(boolean bl2) {
        return false;
    }
    @Redirect(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;method_31382(Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;Lnet/minecraft/client/RunArgs;)Lcom/mojang/authlib/minecraft/SocialInteractionsService;"))
    public SocialInteractionsService RemoveAuthenticationWarning(final MinecraftClient client, final YggdrasilAuthenticationService yggdrasilAuthenticationService, final RunArgs runArgs) {
        return new OfflineSocialInteractions();
    }
}