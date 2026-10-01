package net.id.paradise_lost.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.api.BlockLikeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public class BlockLikeEntityRenderer extends EntityRenderer<BlockLikeEntity> {
    private final RandomSource random = RandomSource.create();

    public BlockLikeEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(BlockLikeEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        BlockState blockState = entity.getBlockState();

        if (blockState.getRenderShape() == RenderShape.MODEL) {
            Level world = entity.getWorldObj();

            if (blockState != world.getBlockState(new BlockPos(entity.blockPosition())) && blockState.getRenderShape() != RenderShape.INVISIBLE) {
                matrices.pushPose();

                BlockPos blockpos = new BlockPos(new BlockPos(entity.blockPosition()));
                matrices.translate(-0.5, 0.0, -0.5);
                BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
                blockRenderManager.getModelRenderer().tesselateBlock(world, blockRenderManager.getBlockModel(blockState), blockState, blockpos, matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, random, blockState.getSeed(entity.getOrigin()), OverlayTexture.NO_OVERLAY);
                matrices.popPose();
                super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ResourceLocation getTextureLocation(BlockLikeEntity entityIn) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
