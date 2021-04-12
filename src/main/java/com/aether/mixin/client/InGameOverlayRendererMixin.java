package com.aether.mixin.client;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.aercloud.BaseAercloudBlock;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.awt.*;

@Mixin(InGameOverlayRenderer.class)
@Environment(EnvType.CLIENT)
public class InGameOverlayRendererMixin {

    @Overwrite
    public static void renderOverlays(MinecraftClient minecraftClient, MatrixStack matrixStack){
        RenderSystem.disableAlphaTest();
        PlayerEntity playerEntity = minecraftClient.player;
        if (!playerEntity.noClip) {
            BlockState blockState = getInWallBlockState(playerEntity);
            if (blockState != null) {
                renderInWallOverlay(minecraftClient, minecraftClient.getBlockRenderManager().getModels().getSprite(blockState), matrixStack);
            }
        }

        if (!minecraftClient.player.isSpectator()) {
            if (minecraftClient.player.isSubmergedIn(FluidTags.WATER)) {
                renderUnderwaterOverlay(minecraftClient, matrixStack);
            }

            if (minecraftClient.player.isOnFire()) {
                renderFireOverlay(minecraftClient, matrixStack);
            }
        }

        PlayerEntity player = minecraftClient.player;
        if (!player.noClip) {
            BlockState blockState = getBlockStateFromEyePos(player);
            if (blockState.getBlock() instanceof BaseAercloudBlock) {
                renderInAercloudOverlay(minecraftClient, blockState.getBlock(), matrixStack);
            }
        }
        RenderSystem.enableAlphaTest();
    }

    @Shadow
    private static void renderFireOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack) {
    }

    @Shadow
    private static void renderUnderwaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack) {
    }

    @Shadow
    private static void renderInWallOverlay(MinecraftClient minecraftClient, Sprite sprite, MatrixStack matrixStack) {
    }

    @Shadow
    private static BlockState getInWallBlockState(PlayerEntity playerEntity) {
        return null;
    }

    private static void renderInAercloudOverlay(MinecraftClient minecraftClient, Block block, MatrixStack matrixStack) {
        minecraftClient.getTextureManager().bindTexture(new Identifier("the_aether:textures/block/aercloud_overlay.png"));
        int[] color = rgbFromMaterialColor(block.getDefaultMaterialColor());
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        float f = minecraftClient.player.getBrightnessAtEyes();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float m = -minecraftClient.player.yaw / 256.0F;
        float n = minecraftClient.player.pitch / 256.0F;
        Matrix4f matrix4f = matrixStack.peek().getModel();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f, -2.0F, -2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(4.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 2.0F, -2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(0.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 2.0F, 2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(0.0F + m, 0.0F + n).next();
        bufferBuilder.vertex(matrix4f, -2.0F, 2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(4.0F + m, 0.0F + n).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
    }

    private static BlockState getBlockStateFromEyePos(PlayerEntity playerEntity) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        mutable.set(playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ());
        return playerEntity.world.getBlockState(mutable);
    }

    private static int[] rgbFromMaterialColor(MaterialColor materialColor){
        int color = materialColor.color;
        int r = color/256/256;
        int g = (color/256)%256;
        int b = (color)%256;
        int[] rgb = new int[3];
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        return rgb;
    }
}
