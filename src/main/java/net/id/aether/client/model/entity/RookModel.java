package net.id.aether.client.model.entity;

import net.id.aether.Aether;
import net.id.aether.entities.misc.RookEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RookModel extends EntityModel<RookEntity> {

    private final ModelPart body;

    public float lookAlpha;

    public RookModel(ModelPart root) {
        body = root.getChild("body");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        var mainData = modelData.getRoot();
        mainData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-16, -8, 0, 32, 32, 0), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(RookEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        var cameraPos = MinecraftClient.getInstance().getCameraEntity().getEyePos();
        var relativeX = cameraPos.getX() - entity.getX();
        var relativeZ = cameraPos.getZ() - entity.getZ();
        var angle = (float) (Math.atan(relativeZ / relativeX) + 1.5708);
        var cameraFacing = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getYaw();
        while (Math.abs(cameraFacing) > 360) {
            if(cameraFacing < 0) {
                cameraFacing += 360;
            }
            else {
                cameraFacing -= 360;
            }
        }

        var radianFacing = Math.toRadians(cameraFacing);

        lookAlpha = (float) (1 - MathHelper.clamp(Math.abs(angle - radianFacing) / Math.PI, 0, 1));

        body.setAngles(0, angle, 0);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertices, light, overlay, red, green, blue, lookAlpha);
    }
}
