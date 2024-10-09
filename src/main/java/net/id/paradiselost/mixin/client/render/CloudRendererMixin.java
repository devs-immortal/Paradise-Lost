package net.id.paradiselost.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
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
    private CloudRenderMode lastCloudRenderMode;
    @Shadow
    private boolean cloudsDirty;
    @Shadow
    @Nullable
    private VertexBuffer cloudsBuffer;
    
    @Shadow private BufferBuilder.BuiltBuffer renderClouds(BufferBuilder builder, double x, double y, double z, Vec3d color) {
        throw new AssertionError();
    }

    public CloudRendererMixin() {
        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.world.ClientWorld");
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void renderClouds(MatrixStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (world.getRegistryKey() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, 160, 1f, 1f);
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, 64, 1.25f, -2f);
            internalCloudRender(matrices, matrix4f, matrix4f2, tickDelta, cameraX, cameraY, cameraZ, -196, 2f, 1.5f);
            ci.cancel();
        }
    }

    // TODO: Replace this mostly copy-pasted code with some redirects or injections (PL-1.7)
    private void internalCloudRender(MatrixStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, float cloudOffset, float cloudScale, float speedMod) {
        float cloudHeight = this.world.getDimensionEffects().getCloudsHeight();
        if (!Float.isNaN(cloudHeight)) {
            double speed = ((this.ticks + tickDelta) * (0.03F * speedMod));
            double posX = (cameraX + speed) / 12.0D / cloudScale;
            double posY = (cloudHeight - cameraY + cloudOffset) / cloudScale + 0.33F;
            double posZ = cameraZ / 12.0D / cloudScale + 0.33000001311302185D;
            posX -= (MathHelper.floor(posX / 2048.0) * 2048);
            posZ -= (MathHelper.floor(posZ / 2048.0) * 2048);
            float l = (float)(posX - (double)MathHelper.floor(posX));
            float m = (float)(posY / 4.0 - (double)MathHelper.floor(posY / 4.0)) * 4.0F;
            float n = (float)(posZ - (double)MathHelper.floor(posZ));
            Vec3d vec3d = this.world.getCloudsColor(tickDelta);
            int o = (int)Math.floor(posX);
            int p = (int)Math.floor(posY / 4.0);
            int q = (int)Math.floor(posZ);
            if (o != this.lastCloudsBlockX || p != this.lastCloudsBlockY || q != this.lastCloudsBlockZ || this.client.options.getCloudRenderModeValue() != this.lastCloudRenderMode || this.lastCloudsColor.squaredDistanceTo(vec3d) > 2.0E-4) {
                this.lastCloudsBlockX = o;
                this.lastCloudsBlockY = p;
                this.lastCloudsBlockZ = q;
                this.lastCloudsColor = vec3d;
                this.lastCloudRenderMode = this.client.options.getCloudRenderModeValue();
                this.cloudsDirty = true;
            }

            if (this.cloudsDirty) {
                this.cloudsDirty = false;
                BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
                if (this.cloudsBuffer != null) {
                    this.cloudsBuffer.close();
                }

                this.cloudsBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                BufferBuilder.BuiltBuffer builtBuffer = this.renderClouds(bufferBuilder, posX, posY, posZ, vec3d);
                this.cloudsBuffer.bind();
                this.cloudsBuffer.upload(builtBuffer);
                VertexBuffer.unbind();
            }

            BackgroundRenderer.applyFogColor();
            matrices.push();
            matrices.multiplyPositionMatrix(matrix4f);
            matrices.scale(12.0F, 1.0F, 12.0F);
            matrices.scale(cloudScale, cloudScale, cloudScale);
            matrices.translate(-l, m, -n);
            if (this.cloudsBuffer != null) {
                this.cloudsBuffer.bind();
                int r = this.lastCloudRenderMode == CloudRenderMode.FANCY ? 0 : 1;

                for(int s = r; s < 2; ++s) {
                    RenderLayer renderLayer = s == 0 ? RenderLayer.getFancyClouds() : RenderLayer.getFastClouds();
                    renderLayer.startDrawing();
                    ShaderProgram shaderProgram = RenderSystem.getShader();
                    this.cloudsBuffer.draw(matrices.peek().getPositionMatrix(), matrix4f2, shaderProgram);
                    renderLayer.endDrawing();
                }

                VertexBuffer.unbind();
            }

            matrices.pop();
        }
    }
}
