package com.aether.mixin.client;

import com.aether.blocks.aercloud.BaseAercloudBlock;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
@Environment(EnvType.CLIENT)
public class InGameOverlayRendererMixin {

    @Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableAlphaTest()V"))
    private static void renderOverlays(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci){
        PlayerEntity player = minecraftClient.player;
        if (!player.noClip) {
            BlockState blockState = getBlockStateFromEyePos(player);
            if (blockState.getBlock() instanceof BaseAercloudBlock) {
                renderInAercloudOverlay(minecraftClient, blockState.getBlock(), matrixStack);
            }
        }
    }

    // Follows the same procedures as the renderUnderwaterOverlay method in the original class
    private static void renderInAercloudOverlay(MinecraftClient minecraftClient, Block block, MatrixStack matrixStack) {
        minecraftClient.getTextureManager().bindTexture(new Identifier("the_aether:textures/block/aercloud_overlay.png"));
        // color[0] = red, color[1] = green, color[2] = blue
        int[] color = rgbFromMaterialColor(block.getDefaultMaterialColor());
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // m and n move the overlay as the player rotates
        float m = -minecraftClient.player.yaw / 256.0F;
        float n = minecraftClient.player.pitch / 256.0F;
        Matrix4f matrix4f = matrixStack.peek().getModel();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
        // The reason it's 255-color[n] is because this function calculates colors backwards.
        bufferBuilder.vertex(matrix4f, -2.0F, -2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(4.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 2.0F, -2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(0.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 2.0F, 2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(0.0F + m, 0.0F + n).next();
        bufferBuilder.vertex(matrix4f, -2.0F, 2.0F, -0.5F).color(255-color[0], 255-color[1], 255-color[2], 0.4F).texture(4.0F + m, 0.0F + n).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder); // Overlays it on the screen
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
