package com.aether.mixin;

import com.aether.world.dimension.AetherDimension;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public final class CloudRendererMixin {
    @Final
    @Shadow
    private static final Identifier CLOUDS = new Identifier("textures/environment/clouds.png");
    @Shadow
    @NotNull
    private final ClientWorld world;
    @Shadow
    private final int ticks;
    @Final
    @Shadow
    @NotNull
    private final MinecraftClient client;
    @Final
    @Shadow
    @NotNull
    private final TextureManager textureManager;
    @Shadow
    private int lastCloudsBlockX;
    @Shadow
    private int lastCloudsBlockY;
    @Shadow
    private int lastCloudsBlockZ;
    @Shadow
    @NotNull
    private Vec3d lastCloudsColor;
    @Shadow
    @NotNull
    private CloudRenderMode lastCloudsRenderMode;
    @Shadow
    private boolean cloudsDirty;
    @Shadow
    @NotNull
    private VertexBuffer cloudsBuffer;

    public CloudRendererMixin() {
        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.world.ClientWorld");
    }

    @Inject(method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V", at = @At("HEAD"), cancellable = true)
    public void renderClouds(MatrixStack matrices, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, 96, 1f, 1f);
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, 32, 1.25f, -2f);
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, -128, 2f, 1.5f);
            ci.cancel();
        }
    }

    private void internalCloudRender(MatrixStack matrices, float tickDelta, double cameraX, double cameraY, double cameraZ, float cloudOffset, float cloudScale, float speedMod) {
        SkyProperties properties = this.world.getSkyProperties();
        float cloudHeight = properties.getCloudsHeight();
        if (!Float.isNaN(cloudHeight)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableFog();
            RenderSystem.depthMask(true);
            double e = ((this.ticks + tickDelta) * (0.03F * speedMod));
            double posX = (cameraX + e) / 12.0D;
            // --- THIS RIGHT HERE IS THE JACKPOT ---
            double renderHeight = cloudHeight - (float) cameraY + cloudOffset;
            double posZ = cameraZ / 12.0D + 0.33000001311302185D;
            posX -= MathHelper.floor(posX / 2048.0D) * 2048;
            posZ -= MathHelper.floor(posZ / 2048.0D) * 2048;
            float adjustedX = (float) (posX - (double) MathHelper.floor(posX));
            float adjustedY = (float) (renderHeight / 4.0D - (double) MathHelper.floor(renderHeight / 4.0D)) * 4.0F;
            float adjustedZ = (float) (posZ - (double) MathHelper.floor(posZ));
            Vec3d cloudColor = this.world.getCloudsColor(tickDelta);
            int floorX = (int) Math.floor(posX);
            int floorY = (int) Math.floor(renderHeight / 4.0D);
            int floorZ = (int) Math.floor(posZ);
            if (floorX != this.lastCloudsBlockX || floorY != this.lastCloudsBlockY || floorZ != this.lastCloudsBlockZ || this.client.options.getCloudRenderMode() != this.lastCloudsRenderMode || this.lastCloudsColor.squaredDistanceTo(cloudColor) > 2.0E-4D) {
                this.lastCloudsBlockX = floorX;
                this.lastCloudsBlockY = floorY;
                this.lastCloudsBlockZ = floorZ;
                this.lastCloudsColor = cloudColor;
                this.lastCloudsRenderMode = this.client.options.getCloudRenderMode();
                this.cloudsDirty = true;
            }

            if (this.cloudsDirty) {
                this.cloudsDirty = false;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                if (this.cloudsBuffer != null) {
                    this.cloudsBuffer.close();
                }

                this.cloudsBuffer = new VertexBuffer(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
                this.renderClouds(bufferBuilder, posX, renderHeight, posZ, cloudColor);
                bufferBuilder.end();
                this.cloudsBuffer.upload(bufferBuilder);
            }

            this.textureManager.bindTexture(CLOUDS);
            matrices.push();
            matrices.scale(12.0F, 1.0F, 12.0F);
            matrices.scale(cloudScale, cloudScale, cloudScale);
            matrices.translate(-adjustedX, adjustedY, -adjustedZ);
            if (this.cloudsBuffer != null) {
                this.cloudsBuffer.bind();
                VertexFormats.POSITION_TEXTURE_COLOR_NORMAL.startDrawing(0L);
                int cloudMainIndex = this.lastCloudsRenderMode == CloudRenderMode.FANCY ? 0 : 1;

                for (byte cloudIndex = 1; cloudMainIndex <= cloudIndex; ++cloudMainIndex) {
                    if (cloudMainIndex == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }

                    VertexBuffer cloudBuffer = this.cloudsBuffer;
                    Entry entry = matrices.peek();
                    cloudBuffer.draw(entry.getModel(), 7);
                }

                VertexBuffer.unbind();
                VertexFormats.POSITION_TEXTURE_COLOR_NORMAL.endDrawing();
            }

            matrices.pop();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.disableFog();
        }
    }

    @Shadow
    private void renderClouds(BufferBuilder builder, double x, double y, double z, Vec3d color) {
    }
}
