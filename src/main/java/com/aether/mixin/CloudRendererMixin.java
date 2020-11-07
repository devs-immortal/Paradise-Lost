package com.aether.mixin;

import com.aether.world.dimension.AetherDimension;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
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
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Metadata(
        mv = {1, 4, 0},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020A2\u0006\u0010C\u001a\u00020A2\u0006\u0010D\u001a\u00020$H\u0003J0\u0010<\u001a\u00020=2\u0006\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020A2\u0006\u0010J\u001a\u00020A2\u0006\u0010K\u001a\u00020AH\u0017R\u0010\u0010\u0003\u001a\u00020\u00048\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001e\u0010\u001d\u001a\u00020\u00188\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001a\"\u0004\b\u001f\u0010\u001cR\u001e\u0010 \u001a\u00020\u00188\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001a\"\u0004\b\"\u0010\u001cR\u001e\u0010#\u001a\u00020$8\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001e\u0010)\u001a\u00020*8\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001e\u0010/\u001a\u0002008\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001e\u00105\u001a\u00020\u00188\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u001a\"\u0004\b7\u0010\u001cR\u0016\u00108\u001a\u0002098\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b:\u0010;¨\u0006L"},
        d2 = {"Lcom/aether/mixin/CloudRendererMixin;", "", "()V", "CLOUDS", "Lnet/minecraft/util/Identifier;", "client", "Lnet/minecraft/client/MinecraftClient;", "getClient", "()Lnet/minecraft/client/MinecraftClient;", "setClient", "(Lnet/minecraft/client/MinecraftClient;)V", "cloudsBuffer", "Lnet/minecraft/client/gl/VertexBuffer;", "getCloudsBuffer", "()Lnet/minecraft/client/gl/VertexBuffer;", "setCloudsBuffer", "(Lnet/minecraft/client/gl/VertexBuffer;)V", "cloudsDirty", "", "getCloudsDirty", "()Z", "setCloudsDirty", "(Z)V", "lastCloudsBlockX", "", "getLastCloudsBlockX", "()I", "setLastCloudsBlockX", "(I)V", "lastCloudsBlockY", "getLastCloudsBlockY", "setLastCloudsBlockY", "lastCloudsBlockZ", "getLastCloudsBlockZ", "setLastCloudsBlockZ", "lastCloudsColor", "Lnet/minecraft/util/math/Vec3d;", "getLastCloudsColor", "()Lnet/minecraft/util/math/Vec3d;", "setLastCloudsColor", "(Lnet/minecraft/util/math/Vec3d;)V", "lastCloudsRenderMode", "Lnet/minecraft/client/options/CloudRenderMode;", "getLastCloudsRenderMode", "()Lnet/minecraft/client/options/CloudRenderMode;", "setLastCloudsRenderMode", "(Lnet/minecraft/client/options/CloudRenderMode;)V", "textureManager", "Lnet/minecraft/client/texture/TextureManager;", "getTextureManager", "()Lnet/minecraft/client/texture/TextureManager;", "setTextureManager", "(Lnet/minecraft/client/texture/TextureManager;)V", "ticks", "getTicks", "setTicks", "world", "Lnet/minecraft/client/world/ClientWorld;", "getWorld", "()Lnet/minecraft/client/world/ClientWorld;", "renderClouds", "", "builder", "Lnet/minecraft/client/render/BufferBuilder;", "x", "", "y", "z", "color", "matrices", "Lnet/minecraft/client/util/math/MatrixStack;", "tickDelta", "", "cameraX", "cameraY", "cameraZ", "Aether.main"}
)
@Mixin({WorldRenderer.class})
public final class CloudRendererMixin {
    @Shadow
    @NotNull
    private final ClientWorld world;
    @Shadow
    private int ticks;
    @Shadow
    private int lastCloudsBlockX;
    @Shadow
    private int lastCloudsBlockY;
    @Shadow
    private int lastCloudsBlockZ;
    @Shadow
    @NotNull
    private MinecraftClient client;
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
    @Shadow
    @NotNull
    private TextureManager textureManager;
    @Final
    @Shadow
    private static final Identifier CLOUDS = new Identifier("textures/environment/clouds.png");

    @NotNull
    public final ClientWorld getWorld() {
        return this.world;
    }

    public final int getTicks() {
        return this.ticks;
    }

    public final void setTicks(int var1) {
        this.ticks = var1;
    }

    public final int getLastCloudsBlockX() {
        return this.lastCloudsBlockX;
    }

    public final void setLastCloudsBlockX(int var1) {
        this.lastCloudsBlockX = var1;
    }

    public final int getLastCloudsBlockY() {
        return this.lastCloudsBlockY;
    }

    public final void setLastCloudsBlockY(int var1) {
        this.lastCloudsBlockY = var1;
    }

    public final int getLastCloudsBlockZ() {
        return this.lastCloudsBlockZ;
    }

    public final void setLastCloudsBlockZ(int var1) {
        this.lastCloudsBlockZ = var1;
    }

    @NotNull
    public final MinecraftClient getClient() {
        return this.client;
    }

    public final void setClient(@NotNull MinecraftClient var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.client = var1;
    }

    @NotNull
    public final Vec3d getLastCloudsColor() {
        return this.lastCloudsColor;
    }

    public final void setLastCloudsColor(@NotNull Vec3d var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.lastCloudsColor = var1;
    }

    @NotNull
    public final CloudRenderMode getLastCloudsRenderMode() {
        return this.lastCloudsRenderMode;
    }

    public final void setLastCloudsRenderMode(@NotNull CloudRenderMode var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.lastCloudsRenderMode = var1;
    }

    public final boolean getCloudsDirty() {
        return this.cloudsDirty;
    }

    public final void setCloudsDirty(boolean var1) {
        this.cloudsDirty = var1;
    }

    @NotNull
    public final VertexBuffer getCloudsBuffer() {
        return this.cloudsBuffer;
    }

    public final void setCloudsBuffer(@NotNull VertexBuffer var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.cloudsBuffer = var1;
    }

    @NotNull
    public final TextureManager getTextureManager() {
        return this.textureManager;
    }

    public final void setTextureManager(@NotNull TextureManager var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.textureManager = var1;
    }

    @Inject(method = {"renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V"}, at = {@At("HEAD")}, cancellable = true)
    public void renderClouds(MatrixStack matrices, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if(world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, 96, 1f, 1f);
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, 32, 1.25f, -2f);
            internalCloudRender(matrices, tickDelta, cameraX, cameraY, cameraZ, -128, 2f, 1.5f);
            ci.cancel();
        }
    }

    private void internalCloudRender(MatrixStack matrices, float tickDelta, double cameraX, double cameraY, double cameraZ, float cloudOffset, float cloudScale, float speedMod) {
        Intrinsics.checkNotNullParameter(matrices, "matrices");
        SkyProperties var10000 = this.world.getSkyProperties();
        Intrinsics.checkNotNullExpressionValue(var10000, "world.skyProperties");
        float f = var10000.getCloudsHeight();
        if (!Float.isNaN(f)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableFog();
            RenderSystem.depthMask(true);
            float g = 12.0F;
            float h = 4.0F;
            double d = 2.0E-4D;
            double e = ((this.ticks + tickDelta) * (0.03F * speedMod));
            double i = (cameraX + e) / 12.0D;
            // --- THIS FUCKER HERE IS THE JACKPOT ---
            double renderHeight = (double)(f - (float)cameraY + cloudOffset);
            double k = cameraZ / 12.0D + 0.33000001311302185D;
            i -= (double)(MathHelper.floor(i / 2048.0D) * 2048);
            k -= (double)(MathHelper.floor(k / 2048.0D) * 2048);
            float l = (float)(i - (double)MathHelper.floor(i));
            float m = (float)(renderHeight / 4.0D - (double)MathHelper.floor(renderHeight / 4.0D)) * 4.0F;
            float n = (float)(k - (double)MathHelper.floor(k));
            Vec3d var32 = this.world.getCloudsColor(tickDelta);
            Intrinsics.checkNotNullExpressionValue(var32, "this.world.getCloudsColor(tickDelta)");
            Vec3d vec3d = var32;
            int o = (int)Math.floor(i);
            int p = (int)Math.floor(renderHeight / 4.0D);
            int q = (int)Math.floor(k);
            if (o != this.lastCloudsBlockX || p != this.lastCloudsBlockY || q != this.lastCloudsBlockZ || this.client.options.getCloudRenderMode() != this.lastCloudsRenderMode || this.lastCloudsColor.squaredDistanceTo(vec3d) > 2.0E-4D) {
                this.lastCloudsBlockX = o;
                this.lastCloudsBlockY = p;
                this.lastCloudsBlockZ = q;
                this.lastCloudsColor = vec3d;
                CloudRenderMode var10001 = this.client.options.getCloudRenderMode();
                Intrinsics.checkNotNullExpressionValue(var10001, "client.options.getCloudRenderMode()");
                this.lastCloudsRenderMode = var10001;
                this.cloudsDirty = true;
            }

            if (this.cloudsDirty) {
                this.cloudsDirty = false;
                Tessellator var33 = Tessellator.getInstance();
                Intrinsics.checkNotNullExpressionValue(var33, "Tessellator.getInstance()");
                BufferBuilder bufferBuilder = var33.getBuffer();
                if (this.cloudsBuffer != null) {
                    this.cloudsBuffer.close();
                }

                this.cloudsBuffer = new VertexBuffer(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
                Intrinsics.checkNotNullExpressionValue(bufferBuilder, "bufferBuilder");
                this.renderClouds(bufferBuilder, i, renderHeight, k, vec3d);
                bufferBuilder.end();
                this.cloudsBuffer.upload(bufferBuilder);
            }

            this.textureManager.bindTexture(this.CLOUDS);
            matrices.push();
            matrices.scale(12.0F, 1.0F, 12.0F);
            matrices.scale(cloudScale, cloudScale, cloudScale);
            matrices.translate((double)(-l), (double)m, (double)(-n));
            if (this.cloudsBuffer != null) {
                this.cloudsBuffer.bind();
                VertexFormats.POSITION_TEXTURE_COLOR_NORMAL.startDrawing(0L);
                int r = this.lastCloudsRenderMode == CloudRenderMode.FANCY ? 0 : 1;
                int s = r;

                for(byte var31 = 1; s <= var31; ++s) {
                    if (s == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }

                    VertexBuffer var34 = this.cloudsBuffer;
                    Entry var35 = matrices.peek();
                    Intrinsics.checkNotNullExpressionValue(var35, "matrices.peek()");
                    var34.draw(var35.getModel(), 7);
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
    private final void renderClouds(BufferBuilder builder, double x, double y, double z, Vec3d color) {
    }

    public CloudRendererMixin() {
        Object var10001 = null;
        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.world.ClientWorld");
    }
}
