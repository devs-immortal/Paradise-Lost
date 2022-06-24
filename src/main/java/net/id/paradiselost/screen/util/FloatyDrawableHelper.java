package net.id.paradiselost.screen.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public interface FloatyDrawableHelper{
    default void drawHorizontalLine(MatrixStack matrices, float x1, float x2, float y, int color){
        if(x2 < x1){
            float i = x1;
            x1 = x2;
            x2 = i;
        }
        
        fill(matrices, x1, y, x2 + 1, y + 1, color);
    }
    
    default void drawVerticalLine(MatrixStack matrices, float x, float y1, float y2, int color){
        if(y2 < y1){
            float i = y1;
            y1 = y2;
            y2 = i;
        }
        
        fill(matrices, x, y1 + 1, x + 1, y2, color);
    }
    
    default void fill(MatrixStack matrices, float x1, float y1, float x2, float y2, int color){
        fill(matrices.peek().getPositionMatrix(), x1, y1, x2, y2, color);
    }
    
    default void fill(Matrix4f matrix, float x1, float y1, float x2, float y2, int color){
        float j;
        if(x1 < x2){
            j = x1;
            x1 = x2;
            x2 = j;
        }
        
        if(y1 < y2){
            j = y1;
            y1 = y2;
            y2 = j;
        }
        
        float f = (float)(color >> 24 & 255) / 255.0F;
        float g = (float)(color >> 16 & 255) / 255.0F;
        float h = (float)(color >> 8 & 255) / 255.0F;
        float k = (float)(color & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y2, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, x2, y1, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(g, h, k, f).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    
    default void fillGradient(MatrixStack matrices, float startX, float startY, float endX, float endY, int colorStart, int colorEnd){
        fillGradient(matrices, startX, startY, endX, endY, colorStart, colorEnd, getZOffset());
    }
    
    default void fillGradient(MatrixStack matrices, float startX, float startY, float endX, float endY, int colorStart, int colorEnd, float z){
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getPositionMatrix(), bufferBuilder, startX, startY, endX, endY, z, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
    
    default void fillGradient(Matrix4f matrix, BufferBuilder bufferBuilder, float startX, float startY, float endX, float endY, float z, int colorStart, int colorEnd){
        float f = (float)(colorStart >> 24 & 255) / 255.0F;
        float g = (float)(colorStart >> 16 & 255) / 255.0F;
        float h = (float)(colorStart >> 8 & 255) / 255.0F;
        float i = (float)(colorStart & 255) / 255.0F;
        float j = (float)(colorEnd >> 24 & 255) / 255.0F;
        float k = (float)(colorEnd >> 16 & 255) / 255.0F;
        float l = (float)(colorEnd >> 8 & 255) / 255.0F;
        float m = (float)(colorEnd & 255) / 255.0F;
        bufferBuilder.vertex(matrix, endX, startY, z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, startX, startY, z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, startX, endY, z).color(k, l, m, j).next();
        bufferBuilder.vertex(matrix, endX, endY, z).color(k, l, m, j).next();
    }
    
    default void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, float centerX, float y, int color){
        textRenderer.drawWithShadow(matrices, text, centerX - textRenderer.getWidth(text) / 2, y, color);
    }
    
    default void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, float centerX, float y, int color){
        OrderedText orderedText = text.asOrderedText();
        textRenderer.drawWithShadow(matrices, orderedText, centerX - textRenderer.getWidth(orderedText) / 2, y, color);
    }
    
    default void drawCenteredTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, OrderedText text, float centerX, float y, int color){
        textRenderer.drawWithShadow(matrices, text, centerX - textRenderer.getWidth(text) / 2, y, color);
    }
    
    default void drawStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, float x, float y, int color){
        textRenderer.drawWithShadow(matrices, text, x, y, color);
    }
    
    default void drawWithShadow(MatrixStack matrices, TextRenderer textHandler, OrderedText text, float x, float y, int color){
        textHandler.drawWithShadow(matrices, text, x, y, color);
    }
    
    default void drawTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, Text text, float x, float y, int color){
        textRenderer.drawWithShadow(matrices, text, x, y, color);
    }
    
    /**
     * @param renderAction the action to render both the content and the outline, taking x and y positions as input
     */
    default void drawWithOutline(float x, float y, BiConsumer<Float, Float> renderAction){
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        renderAction.accept(x + 1, y);
        renderAction.accept(x - 1, y);
        renderAction.accept(x, y + 1);
        renderAction.accept(x, y - 1);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        renderAction.accept(x, y);
    }
    
    default void drawSprite(MatrixStack matrices, float x, float y, float z, float width, float height, Sprite sprite){
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x, x + width, y, y + height, z, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }
    
    /**
     * Draws a textured rectangle from a region in a 256x256 texture.
     *
     * <p>The Z coordinate of the rectangle is {@link #getZOffset()}.
     *
     * <p>The width and height of the region are the same as
     * the dimensions of the rectangle.
     *
     * @param matrices the matrix stack used for rendering
     * @param x        the X coordinate of the rectangle
     * @param y        the Y coordinate of the rectangle
     * @param u        the left-most coordinate of the texture region
     * @param v        the top-most coordinate of the texture region
     * @param width    the width
     * @param height   the height
     */
    default void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height){
        drawTexture(matrices, x, y, getZOffset(), u, v, width, height, 256, 256);
    }
    
    /**
     * Draws a textured rectangle from a region in a texture.
     *
     * <p>The width and height of the region are the same as
     * the dimensions of the rectangle.
     *
     * @param matrices      the matrix stack used for rendering
     * @param x             the X coordinate of the rectangle
     * @param y             the Y coordinate of the rectangle
     * @param z             the Z coordinate of the rectangle
     * @param u             the left-most coordinate of the texture region
     * @param v             the top-most coordinate of the texture region
     * @param width         the width of the rectangle
     * @param height        the height of the rectangle
     * @param textureHeight the height of the entire texture
     * @param textureWidth  the width of the entire texture
     */
    default void drawTexture(MatrixStack matrices, float x, float y, float z, float u, float v, float width, float height, float textureHeight, float textureWidth){
        drawTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }
    
    /**
     * Draws a textured rectangle from a region in a texture.
     *
     * @param matrices      the matrix stack used for rendering
     * @param x             the X coordinate of the rectangle
     * @param y             the Y coordinate of the rectangle
     * @param width         the width of the rectangle
     * @param height        the height of the rectangle
     * @param u             the left-most coordinate of the texture region
     * @param v             the top-most coordinate of the texture region
     * @param regionWidth   the width of the texture region
     * @param regionHeight  the height of the texture region
     * @param textureWidth  the width of the entire texture
     * @param textureHeight the height of the entire texture
     */
    default void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight){
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }
    
    /**
     * Draws a textured rectangle from a region in a texture.
     *
     * <p>The width and height of the region are the same as
     * the dimensions of the rectangle.
     *
     * @param matrices      the matrix stack used for rendering
     * @param x             the X coordinate of the rectangle
     * @param y             the Y coordinate of the rectangle
     * @param u             the left-most coordinate of the texture region
     * @param v             the top-most coordinate of the texture region
     * @param width         the width of the rectangle
     * @param height        the height of the rectangle
     * @param textureWidth  the width of the entire texture
     * @param textureHeight the height of the entire texture
     */
    default void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight){
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }
    
    default void drawTexture(MatrixStack matrices, float x0, float y0, float x1, float y1, float z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight){
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, y0, x1, y1, z, (u + 0.0F) / textureWidth, (u + regionWidth) / textureWidth, (v + 0.0F) / textureHeight, (v + regionHeight) / textureHeight);
    }
    
    default void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrices, x0, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrices, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrices, x1, y0, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrices, x0, y0, z).texture(u0, v0).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }
    
    default int getZOffset(){
        return 0;
    }
}
