package com.aether.client.rendering.entity;

import com.aether.entities.block.FloatingBlockEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FloatingBlockRenderer extends EntityRenderer<FloatingBlockEntity> {

    public FloatingBlockRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn);

        this.shadowOpacity = 0.5F;
    }

    @Override
    public void render(FloatingBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        BlockState iblockstate = entity.getBlockstate();

        if (iblockstate.getRenderType() == BlockRenderType.MODEL) {
            World world = entity.getWorldObj();

            if (iblockstate != world.getBlockState(new BlockPos(entity.getPos())) && iblockstate.getRenderType() != BlockRenderType.INVISIBLE) {
                GlStateManager.pushMatrix();
                GlStateManager.disableLighting();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();

                /*if (this.renderOutlines)
                {
                    GlStateManager.enableColorMaterial();
                    GlStateManager.setupSolidRenderingTextureCombine(this.getOutlineColor(entity));
                }*/

                bufferbuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT); // TODO: ???
                BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                GlStateManager.translatef((float) (this.dispatcher.camera.getPos().x - (double) blockpos.getX() - 0.5D), (float) (this.dispatcher.camera.getPos().y - (double) blockpos.getY()), (float) (this.dispatcher.camera.getPos().z - (double) blockpos.getZ() - 0.5D));
                BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));
                blockrendererdispatcher.getModelRenderer().render(world, blockrendererdispatcher.getModel(iblockstate), iblockstate, blockpos, matrices, vertexConsumer, false, new Random(), iblockstate.getRenderingSeed(entity.getOrigin()), 0);
                tessellator.draw();

                /*if (this.renderOutlines)
                {
                    GlStateManager.tearDownSolidRenderingTextureCombine();
                    GlStateManager.disableColorMaterial();
                }*/

                GlStateManager.enableLighting();
                GlStateManager.popMatrix();

                super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }

    @Override
    public Identifier getTexture(FloatingBlockEntity entityIn) {
        return TextureManager.MISSING_IDENTIFIER;
    }
}
