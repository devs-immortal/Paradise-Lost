package com.aether.mixin.client.render;

import com.aether.util.DynamicBlockColorProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final private BlockColors blockColors;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void registerDynamicColorProvs(RunArgs args, CallbackInfo ci) {
        Registry.BLOCK.stream()
                .filter(block -> block instanceof DynamicBlockColorProvider)
                .forEach(block -> blockColors.registerColorProvider(((DynamicBlockColorProvider) block).getProvider(), block));
    }

    @ModifyVariable(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient$WorldLoadAction;NONE:Lnet/minecraft/client/MinecraftClient$WorldLoadAction;", ordinal = 0), ordinal = 2, index = 11, name = "bl2", require = 1)
    private boolean replaceBl2(boolean bl2) {
        return false;
    }

}