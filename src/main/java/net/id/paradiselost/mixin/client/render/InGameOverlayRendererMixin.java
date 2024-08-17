package net.id.paradiselost.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.natural.cloud.ParadiseLostCloudBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameOverlayRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class InGameOverlayRendererMixin {

    @Shadow
    @Nullable
    private static BlockState getInWallBlockState(PlayerEntity player) {
        return null;
    }

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderCloudOverlay(Sprite sprite, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockState overlayState = getInWallBlockState(client.player);
        if (overlayState != null && overlayState.getBlock() instanceof ParadiseLostCloudBlock) {
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
            RenderSystem.setShaderTexture(0, ParadiseLost.locate("textures/block/" + Registries.BLOCK.getId(overlayState.getBlock()).getPath() + ".png"));
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            float f = client.player.getBrightnessAtEyes();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(f, f, f, 0.775F);
            float yaw = client.player.getYaw() / 192.0F;
            float pitch = client.player.getPitch() / 192.0F;
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(1.0F - yaw, 1.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F - yaw, 1.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F - yaw, 0.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(1.0F - yaw, 0.0F + pitch).next();
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            //FIXME? Should this be removed? The original doesn't have it.
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "getInWallBlockState", at = @At("HEAD"), cancellable = true)
    private static void getInWallBlockState(PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i < 8; ++i) {
            double d = player.getX() + (double) (((float) ((i) % 2) - 0.5F) * player.getWidth() * 0.8F);
            double e = player.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = player.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * player.getWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = player.getWorld().getBlockState(mutable);
            if (blockState.getBlock() instanceof ParadiseLostCloudBlock) {
                cir.setReturnValue(blockState);
                cir.cancel();
            }
        }
    }
}
