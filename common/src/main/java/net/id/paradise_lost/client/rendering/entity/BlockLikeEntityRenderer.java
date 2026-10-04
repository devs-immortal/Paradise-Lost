package net.id.paradise_lost.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.api.BlockLikeEntity;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockLikeEntityRenderer extends EntityRenderer<BlockLikeEntity, FallingBlockRenderState> {
    private final BlockRenderDispatcher dispatcher;

    public BlockLikeEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.shadowRadius = 0.5F;
        this.dispatcher = renderManager.getBlockRenderDispatcher();
    }

    @Override
    public boolean shouldRender(BlockLikeEntity entity, Frustum frustum, double x, double y, double z) {
        if (!super.shouldRender(entity, frustum, x, y, z)) {
            return false;
        }
        return entity.getBlockState() != entity.getWorldObj().getBlockState(entity.blockPosition());
    }

    @Override
    public void render(FallingBlockRenderState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        BlockState blockState = state.blockState;
        if (blockState.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        matrices.pushPose();
        matrices.translate(-0.5, 0.0, -0.5);
        this.dispatcher.getModelRenderer().tesselateBlock(state, this.dispatcher.getBlockModel(blockState), blockState, state.blockPos, matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, RandomSource.create(), blockState.getSeed(state.startBlockPos), OverlayTexture.NO_OVERLAY);
        matrices.popPose();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public FallingBlockRenderState createRenderState() {
        return new FallingBlockRenderState();
    }

    @Override
    public void extractRenderState(BlockLikeEntity entity, FallingBlockRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.startBlockPos = entity.getOrigin();
        state.blockPos = entity.blockPosition();
        state.blockState = entity.getBlockState();
        state.biome = entity.getWorldObj().getBiome(entity.blockPosition());
        state.level = entity.getWorldObj();
    }
}
