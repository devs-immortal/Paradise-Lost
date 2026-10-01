package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public class FluidRendererMixin {

    @Unique
    private float fadeAlpha;

    @Inject(method = "tesselate", at = @At("HEAD"))
    private void render(BlockAndTintGetter world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        fadeAlpha = 1F;
        if (Minecraft.getInstance().level.dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            if (fluidState.getType().isSame(Fluids.WATER)) {
                fadeAlpha = Math.min((pos.getY() - world.getMinBuildHeight()) / 32F, 1);
            }
        }
    }

    @ModifyArg(
            method = "vertex",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            ),
            index = 3
    )
    private float adjustAlphaForUplandsFadeOut(float a) {
        return fadeAlpha;
    }
}
