package com.aether.mixin.render;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelGenerator.class)
public abstract class BlockStateModelGeneratorMixin {
    @Shadow
    protected abstract void registerTorch(Block torch, Block wallTorch);

    @Shadow
    protected abstract void registerGlassPane(Block glass, Block glassPane);

    @Inject(method = "register", at = @At("TAIL"))
    public void onRegister(CallbackInfo ci) {
        registerTorch(AetherBlocks.ambrosium_torch, AetherBlocks.ambrosium_torch_wall);
        registerGlassPane(AetherBlocks.quicksoil_glass, AetherBlocks.quicksoil_glass_pane);
    }
}
