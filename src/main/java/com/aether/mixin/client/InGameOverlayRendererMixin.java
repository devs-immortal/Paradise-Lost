package com.aether.mixin.client;

import com.aether.blocks.aercloud.BaseAercloudBlock;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameOverlayRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class InGameOverlayRendererMixin {

    @Shadow @Nullable protected static BlockState getInWallBlockState(PlayerEntity player) { return null; }

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderAercloudOverlay(Sprite sprite, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockState overlayState = getInWallBlockState(client.player);
        if(overlayState != null && overlayState.getBlock() instanceof BaseAercloudBlock) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            float f = client.player.getBrightnessAtEyes();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(f, f, f, 0.775F);
            float uMin = sprite.getMinU();
            float uMax = sprite.getMaxU();
            float vMin = sprite.getMinV();
            float vMax = sprite.getMaxV();
            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(uMax, vMax).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(uMin, vMax).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(uMin, vMin).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(uMax, vMin).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "getInWallBlockState", at = @At("HEAD"), cancellable = true)
    private static void getInWallBlockState(PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int i = 0; i < 8; ++i) {
            double d = player.getX() + (double)(((float)((i) % 2) - 0.5F) * player.getWidth() * 0.8F);
            double e = player.getEyeY() + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = player.getZ() + (double)(((float)((i >> 2) % 2) - 0.5F) * player.getWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = player.world.getBlockState(mutable);
            if (blockState.getBlock() instanceof BaseAercloudBlock) {
                cir.setReturnValue(blockState);
                cir.cancel();
            }
        }
    }
}
