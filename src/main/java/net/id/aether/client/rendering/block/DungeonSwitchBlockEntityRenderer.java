package net.id.aether.client.rendering.block;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class DungeonSwitchBlockEntityRenderer implements BlockEntityRenderer<DungeonSwitchBlockEntity> {
    private static final Identifier CUBE_TEXTURE = Aether.locate("textures/block/hellfire_stone.png");
    private final DungeonSwitchModel model;

    public DungeonSwitchBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        model = new DungeonSwitchModel(ctx);
    }

    @Override
    public void render(DungeonSwitchBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float yOffset = getYOffset(entity.getAnimationDelta(), tickDelta);
        float spin = entity.getAnimationDelta() * 4 * 0.017453F;

        matrices.push();
        matrices.translate(0.0D, .5 + yOffset, 0.0D);
        //matrices.scale(.75f, .75f, .75f);
        model.cube.roll = spin;
        model.cube.yaw = spin;
        model.cube.pitch = spin;
        model.cube.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(CUBE_TEXTURE)), light, overlay);
        matrices.pop();
    }

    public float getYOffset(int ticks, float tickDelta) {
        float f = (float) ticks + tickDelta;
        return MathHelper.sin((float) Math.toRadians(f) * 4) / 4;
    }
}
