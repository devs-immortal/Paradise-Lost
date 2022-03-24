package net.id.aether.client.rendering.entity.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.block.BlockLikeEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class BlockLikeEntityRenderer extends EntityRenderer<BlockLikeEntity> {

    public BlockLikeEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(BlockLikeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        BlockState blockState = entity.getBlockState();

        if (blockState.getRenderType() == BlockRenderType.MODEL) {
            World world = entity.getWorldObj();

            if (blockState != world.getBlockState(new BlockPos(entity.getPos())) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                matrices.push();

                BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                matrices.translate(-0.5, 0.0, -0.5);
                BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
                blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockpos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(entity.getOrigin()), OverlayTexture.DEFAULT_UV);
                matrices.pop();
                super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public Identifier getTexture(BlockLikeEntity entityIn) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
