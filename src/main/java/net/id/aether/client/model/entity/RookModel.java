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
        var cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        var cameraPos = cameraEntity.getEyePos();
        var relativeX = cameraEntity.getX() - entity.getX();
        var relativeZ = cameraEntity.getZ() - entity.getZ();
        var angle = (float) (Math.atan2(relativeZ, relativeX) - Math.PI);
        var cameraFacing = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getYaw();



        while (Math.abs(cameraFacing) > 360) {
            if(cameraFacing < 0) {
                cameraFacing += 360;
            }
            else {
                cameraFacing -= 360;
            }
        }

        var radianFacing = Math.abs(Math.toRadians(cameraFacing));

        if(entity.world.getTime() % 30 == 0) {
            Aether.LOG.error("ROOK: " + angle);
            Aether.LOG.error("PLAYER: " + radianFacing);
        }

        lookAlpha = (float) MathHelper.clamp((0.95 - MathHelper.clamp(Math.abs(angle - radianFacing) / Math.PI, 0, 1)), 0, 1);

        //body.setAngles(0, (float) (angle + (Math.PI / 2 + Math.PI)), 0);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertices, light, overlay, red, green, blue, lookAlpha);
    }
}
