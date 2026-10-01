package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public final class CloudRendererMixin {
    @Final
    @Shadow
    private static ResourceLocation CLOUDS_LOCATION;
    @Shadow
    @NotNull
    private final ClientLevel level;
    @Shadow
    private final int ticks;
    @Final
    @Shadow
    @NotNull
    private final Minecraft minecraft;
    @Shadow
    private int prevCloudX;
    @Shadow
    private int prevCloudY;
    @Shadow
    private int prevCloudZ;
    @Shadow
    @NotNull
    private Vec3 prevCloudColor;
    @Shadow
    @NotNull
    private CloudStatus prevCloudsType;
    @Shadow
    private boolean generateClouds;
    @Shadow
    @Nullable
    private VertexBuffer cloudBuffer;

    @Shadow private MeshData buildClouds(Tesselator tessellator, double x, double y, double z, Vec3 color) {
        throw new AssertionError();
    }

    public CloudRendererMixin() {
        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.multiplayer.ClientLevel");
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void renderClouds(PoseStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (level.dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, 160, 1f, 1f);
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, 64, 1.25f, -2f);
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, -196, 2f, 1.5f);
            ci.cancel();
        }
    }

    private void internalCloudRender(PoseStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, float cloudOffset, float cloudScale, float speedMod) {
        float cloudHeight = this.level.effects().getCloudHeight();
        if (!Float.isNaN(cloudHeight)) {
            double speed = ((this.ticks + tickDelta) * (0.03F * speedMod));
            double posX = (cameraX + speed) / 12.0D / cloudScale;
            double posY = (cloudHeight - cameraY + cloudOffset) / cloudScale + 0.33F;
            double posZ = cameraZ / 12.0D / cloudScale + 0.33000001311302185D;
            posX -= (Mth.floor(posX / 2048.0) * 2048);
            posZ -= (Mth.floor(posZ / 2048.0) * 2048);
            float l = (float) (posX - (double) Mth.floor(posX));
            float m = (float) (posY / 4.0 - (double) Mth.floor(posY / 4.0)) * 4.0F;
            float n = (float) (posZ - (double) Mth.floor(posZ));
            Vec3 vec3d = this.level.getCloudColor(tickDelta);
            int o = (int) Math.floor(posX);
            int p = (int) Math.floor(posY / 4.0);
            int q = (int) Math.floor(posZ);
            if (o != this.prevCloudX || p != this.prevCloudY || q != this.prevCloudZ || this.minecraft.options.getCloudsType() != this.prevCloudsType || this.prevCloudColor.distanceToSqr(vec3d) > 2.0E-4) {
                this.prevCloudX = o;
                this.prevCloudY = p;
                this.prevCloudZ = q;
                this.prevCloudColor = vec3d;
                this.prevCloudsType = this.minecraft.options.getCloudsType();
                this.generateClouds = true;
            }

            if (this.generateClouds) {
                this.generateClouds = false;
                if (this.cloudBuffer != null) {
                    this.cloudBuffer.close();
                }

                this.cloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                this.cloudBuffer.bind();
                this.cloudBuffer.upload(this.buildClouds(Tesselator.getInstance(), posX, posY, posZ, vec3d));
                VertexBuffer.unbind();
            }

            FogRenderer.levelFogColor();
            matrices.pushPose();
            matrices.mulPose(matrix4f);
            matrices.scale(12.0F, 1.0F, 12.0F);
            matrices.scale(cloudScale, cloudScale, cloudScale);
            matrices.translate(-l, m, -n);
            if (this.cloudBuffer != null) {
                this.cloudBuffer.bind();
                int r = this.prevCloudsType == CloudStatus.FANCY ? 0 : 1;

                for (int s = r; s < 2; ++s) {
                    RenderType renderLayer = s == 0 ? RenderType.cloudsDepthOnly() : RenderType.clouds();
                    renderLayer.setupRenderState();
                    ShaderInstance shaderProgram = RenderSystem.getShader();
                    this.cloudBuffer.drawWithShader(matrices.last().pose(), matrix4f2, shaderProgram);
                    renderLayer.clearRenderState();
                }

                VertexBuffer.unbind();
            }

            matrices.popPose();
        }
    }
}
