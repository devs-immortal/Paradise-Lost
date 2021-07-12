package com.aether.mixin.client.render;

import com.aether.world.dimension.AetherDimension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
@Environment(EnvType.CLIENT)
public class FluidRendererMixin {

    @Unique
    private float fadeAlpha;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(BlockRenderView world, BlockPos pos, VertexConsumer builder, FluidState state, CallbackInfoReturnable<Boolean> info) {
        fadeAlpha = 1F;
        if (state.getFluid().matchesType(Fluids.WATER)) {
            if (MinecraftClient.getInstance().world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
                fadeAlpha = Math.min((pos.getY() - world.getBottomY()) / 32F, 1);
            }

        }
    }

    @ModifyArg(
            method = "vertex",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"
            ),
            index = 3
    )
    private float adjustAlphaForUplandsFadeOut(float a) {
        return fadeAlpha;
    }
}
