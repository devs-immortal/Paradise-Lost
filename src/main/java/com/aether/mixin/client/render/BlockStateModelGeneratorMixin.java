package com.aether.mixin.client.render;

import com.aether.blocks.AetherBlocks;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockModelGenerators.class)
public abstract class BlockStateModelGeneratorMixin {
    @Shadow
    protected abstract void registerTorch(Block torch, Block wallTorch);

    @Shadow
    protected abstract void registerGlassPane(Block glass, Block glassPane);

    @Inject(method = "register", at = @At("TAIL"))
    public void onRegister(CallbackInfo ci) {
        registerTorch(AetherBlocks.AMBROSIUM_TORCH, AetherBlocks.AMBROSIUM_TORCH_WALL);
        registerGlassPane(AetherBlocks.QUICKSOIL_GLASS, AetherBlocks.QUICKSOIL_GLASS_PANE);
    }
}