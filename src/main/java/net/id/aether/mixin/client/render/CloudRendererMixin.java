package net.id.aether.mixin.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static Identifier CLOUDS;
    @Shadow
    @NotNull
    private final ClientWorld world;
    @Shadow
    private final int ticks;
    @Final
    @Shadow
    @NotNull
    private final MinecraftClient client;
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
    @Nullable
    private VertexBuffer cloudsBuffer;

    public CloudRendererMixin() {
        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.world.ClientWorld");
    }

    @Inject(method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V", at = @At("HEAD"), cancellable = true)
    public void renderClouds(MatrixStack matrices, Matrix4f model, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            internalCloudRender(matrices, model, tickDelta, cameraX, cameraY, cameraZ, 96, 1f, 1f);
            internalCloudRender(matrices, model, tickDelta, cameraX, cameraY, cameraZ, 32, 1.25f, -2f);
            internalCloudRender(matrices, model, tickDelta, cameraX, cameraY, cameraZ, -128, 2f, 1.5f);
            ci.cancel();
        }
    }

    // TODO: Replace this mostly copy-pasted code with some redirects or injections
    private void internalCloudRender(MatrixStack matrices, Matrix4f model, float tickDelta, double cameraX, double cameraY, double cameraZ, float cloudOffset, float cloudScale, float speedMod) {
        float cloudHeight = this.world.getDimensionEffects().getCloudsHeight();
        if (!Float.isNaN(cloudHeight)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.depthMask(true);
            double speed = ((this.ticks + tickDelta) * (0.03F * speedMod));
            double posX = (cameraX + speed) / 12.0D / cloudScale;
            double posY = (cloudHeight - cameraY + cloudOffset) / cloudScale + 0.33F;
            double posZ = cameraZ / 12.0D / cloudScale + 0.33000001311302185D;
            posX -= MathHelper.floor(posX / 2048.0D) * 2048;
            posZ -= MathHelper.floor(posZ / 2048.0D) * 2048;
            float adjustedX = (float) (posX - (double) MathHelper.floor(posX));
            float adjustedY = (float) (posY / 4.0D - (double) MathHelper.floor(posY / 4.0D)) * 4.0F;
            float adjustedZ = (float) (posZ - (double) MathHelper.floor(posZ));
            Vec3d cloudColor = this.world.getCloudsColor(tickDelta);
            int floorX = (int) Math.floor(posX);
            int floorY = (int) Math.floor(posY / 4.0D);
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
                BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
                if (this.cloudsBuffer != null) this.cloudsBuffer.close();

                this.cloudsBuffer = new VertexBuffer();
                this.renderClouds(bufferBuilder, posX, posY, posZ, cloudColor);
                bufferBuilder.end();
                this.cloudsBuffer.upload(bufferBuilder);
            }

            RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
            RenderSystem.setShaderTexture(0, CLOUDS);
            BackgroundRenderer.setFogBlack();
            matrices.push();
            matrices.scale(12.0F, 1.0F, 12.0F);
            matrices.scale(cloudScale, cloudScale, cloudScale);
            matrices.translate(-adjustedX, adjustedY, -adjustedZ);
            if (this.cloudsBuffer != null) {
                int cloudMainIndex = this.lastCloudsRenderMode == CloudRenderMode.FANCY ? 0 : 1;

                for (int cloudIndex = cloudMainIndex; cloudIndex < 2; ++cloudIndex) {
                    if (cloudIndex == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }

                    Shader shader = RenderSystem.getShader();
                    this.cloudsBuffer.setShader(matrices.peek().getPositionMatrix(), model, shader);
                }
            }

            matrices.pop();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }
    }

    @Shadow
    private void renderClouds(BufferBuilder builder, double x, double y, double z, Vec3d color) {
    }
}