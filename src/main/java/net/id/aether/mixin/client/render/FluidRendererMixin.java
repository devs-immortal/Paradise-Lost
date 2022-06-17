package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
@Environment(EnvType.CLIENT)
public class FluidRendererMixin {

    @Unique
    private float fadeAlpha;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        fadeAlpha = 1F;
        if (MinecraftClient.getInstance().world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            if (fluidState.getFluid().matchesType(Fluids.WATER)) {
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
