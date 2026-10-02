package net.id.paradiselost.client.rendering.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.api.BlockLikeEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.FallingBlockEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class BlockLikeEntityRenderer extends EntityRenderer<BlockLikeEntity, FallingBlockEntityRenderState> {
    private final BlockRenderManager blockRenderManager;

    public BlockLikeEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        this.shadowRadius = 0.5F;
        this.blockRenderManager = renderManager.getBlockRenderManager();
    }

    @Override
    public boolean shouldRender(BlockLikeEntity entity, Frustum frustum, double x, double y, double z) {
        if (!super.shouldRender(entity, frustum, x, y, z)) {
            return false;
        }
        return entity.getBlockState() != entity.getWorldObj().getBlockState(entity.getBlockPos());
    }

    @Override
    public void render(FallingBlockEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        BlockState blockState = state.blockState;
        if (blockState.getRenderType() != BlockRenderType.MODEL) {
            return;
        }
        matrices.push();
        matrices.translate(-0.5, 0.0, -0.5);
        this.blockRenderManager.getModelRenderer().render(state, this.blockRenderManager.getModel(blockState), blockState, state.currentPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, Random.create(), blockState.getRenderingSeed(state.fallingBlockPos), OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public FallingBlockEntityRenderState createRenderState() {
        return new FallingBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(BlockLikeEntity entity, FallingBlockEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.fallingBlockPos = entity.getOrigin();
        state.currentPos = entity.getBlockPos();
        state.blockState = entity.getBlockState();
        state.biome = entity.getWorldObj().getBiome(entity.getBlockPos());
        state.world = entity.getWorldObj();
    }
}
