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
    protected abstract void createNormalTorch(Block torch, Block wallTorch);

    @Shadow
    protected abstract void createGlassBlocks(Block glass, Block glassPane);

    @Inject(method = "run", at = @At("TAIL"))
    public void onRegister(CallbackInfo ci) {
        createNormalTorch(AetherBlocks.AMBROSIUM_TORCH, AetherBlocks.AMBROSIUM_TORCH_WALL);
        createGlassBlocks(AetherBlocks.QUICKSOIL_GLASS, AetherBlocks.QUICKSOIL_GLASS_PANE);
    }
}