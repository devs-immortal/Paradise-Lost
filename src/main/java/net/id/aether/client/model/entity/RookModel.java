package net.id.aether.client.model.entity;

import dev.emi.trinkets.api.TrinketsApi;
import net.id.aether.Aether;
import net.id.aether.component.LUV;
import net.id.aether.entities.misc.RookEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class RookModel extends EntityModel<RookEntity> {

    private final ModelPart body;

    public float lookAlpha, translation, blinkTicks;

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
    public void setAngles(RookEntity rook, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        var cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        var cameraPos = cameraEntity.getEyePos();
        var rookPos = rook.getPos();

        byte luv = 127;

        if(cameraEntity instanceof PlayerEntity player) {
            luv = LUV.getLUV(player).getValue();
        }


        var difX = rook.getX() - cameraEntity.getX();
        var difZ = rook.getZ() - cameraEntity.getZ();
        var angle = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) + 90);

        var faceValue = MathHelper.angleBetween(angle, MathHelper.wrapDegrees(cameraEntity.getHeadYaw())) / 180;

        lookAlpha = MathHelper.lerp(faceValue, 0.7F, 0);

        lookAlpha = (float) MathHelper.lerp(cameraPos.distanceTo(rookPos) / 12F, 0, lookAlpha);

        if(lookAlpha < 0.075F || cameraPos.distanceTo(rookPos) < 4.5F)
            lookAlpha = 0F;

        if(luv >= 0 && luv < 48) {
            lookAlpha = 0F;
        }
        else if(luv == 127 || luv < 0) {
            lookAlpha = 0.7F;
        }

        var time = rook.age + MinecraftClient.getInstance().getTickDelta();
        translation = (float) (Math.sin(time / 25) / 8 + 0.125);

        this.blinkTicks = rook.blinkTicks;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.translate(0, -translation, 0);
        body.render(matrices, vertices, light, overlay, red, green, blue, lookAlpha);
        matrices.pop();
    }
}
