package com.aether.mixin.entity;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.RabbitEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RabbitEntity.EatCarrotCropGoal.class)
public abstract class EatCarrotCropGoalMixin {
    @Redirect(method = "isTargetPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean isThisOrAetherFarmland(BlockState self, Block block) {
        return self.isOf(block) || self.isOf(AetherBlocks.AETHER_FARMLAND);
    }
}