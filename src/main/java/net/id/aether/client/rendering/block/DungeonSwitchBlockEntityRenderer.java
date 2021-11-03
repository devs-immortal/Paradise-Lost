package net.id.aether.client.rendering.block;


import net.id.aether.Aether;
import net.id.aether.blocks.blockentity.dungeon.DungeonSwitchBlockEntity;
import net.id.aether.client.model.block.DungeonSwitchModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DungeonSwitchBlockEntityRenderer implements BlockEntityRenderer<DungeonSwitchBlockEntity> {
    private static final Identifier CUBE_TEXTURE = Aether.locate("textures/block/hellfire_stone.png");
    private final DungeonSwitchModel model;

    public DungeonSwitchBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        model = new DungeonSwitchModel(ctx);
    }

    @Override
    public void render(DungeonSwitchBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float yOffset = getYOffset(entity.getAnimationDelta(), tickDelta);
        matrices.push();
        matrices.translate(0.0D, 1 + yOffset / 2.0F, 0.0D);

        model.cube.roll = entity.getAnimationDelta() / 10f;
        model.cube.yaw = entity.getAnimationDelta() / 10f;
        model.cube.pitch = entity.getAnimationDelta() / 10f;
        model.cube.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(CUBE_TEXTURE)), light, overlay);

        matrices.pop();
    }

    public float getYOffset(int ticks, float tickDelta) {
        float f = (float) ticks + tickDelta;
        float g = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        g = (g * g + g) * 0.4F;
        return g - 1.4F;
    }
}
