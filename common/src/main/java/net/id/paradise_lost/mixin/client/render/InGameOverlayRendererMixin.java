package net.id.paradise_lost.mixin.client.render;

import net.minecraft.client.renderer.CoreShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.natural.cloud.ParadiseLostCloudBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenEffectRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Shadow
    @Nullable
    private static BlockState getViewBlockingState(Player player) {
        return null;
    }

    @Inject(method = "renderTex", at = @At("HEAD"), cancellable = true)
    private static void renderCloudOverlay(TextureAtlasSprite sprite, PoseStack matrices, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        BlockState overlayState = getViewBlockingState(client.player);
        if (overlayState != null && overlayState.getBlock() instanceof ParadiseLostCloudBlock) {
            RenderSystem.setShader(CoreShaders.POSITION_TEX);
            RenderSystem.setShaderTexture(0, ModConstants.id("textures/block/" + BuiltInRegistries.BLOCK.getKey(overlayState.getBlock()).getPath() + ".png"));
            BlockPos blockPos = BlockPos.containing(client.player.getX(), client.player.getEyeY(), client.player.getZ());
            float brightness = LightTexture.getBrightness(client.player.level().dimensionType(), client.player.level().getMaxLocalRawBrightness(blockPos));
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(brightness, brightness, brightness, 0.6F);
            float yaw = client.player.getYRot() / 192.0F;
            float pitch = client.player.getXRot() / 192.0F;
            Matrix4f matrix4f = matrices.last().pose();
            BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(1.0F - yaw, 1.0F + pitch);
            bufferBuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F - yaw, 1.0F + pitch);
            bufferBuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F - yaw, 0.0F + pitch);
            bufferBuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(1.0F - yaw, 0.0F + pitch);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true)
    private static void getInWallBlockState(Player player, CallbackInfoReturnable<BlockState> cir) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 8; ++i) {
            double d = player.getX() + (double) (((float) ((i) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
            double e = player.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = player.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = player.level().getBlockState(mutable);
            if (blockState.getBlock() instanceof ParadiseLostCloudBlock) {
                cir.setReturnValue(blockState);
                cir.cancel();
            }
        }
    }
}
