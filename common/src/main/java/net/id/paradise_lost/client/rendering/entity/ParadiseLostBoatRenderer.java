package net.id.paradise_lost.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatEntity;
import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatType;
import net.id.paradise_lost.entity.vehicle.ParadiseLostChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

import java.util.Map;

public class ParadiseLostBoatRenderer extends EntityRenderer<Boat> {
    private final Map<ParadiseLostBoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ParadiseLostBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context);
        this.shadowRadius = 0.8F;
        ImmutableMap.Builder<ParadiseLostBoatType, Pair<ResourceLocation, ListModel<Boat>>> resources =
                ImmutableMap.builder();
        for (ParadiseLostBoatType type : ParadiseLostBoatType.values()) {
            resources.put(type, Pair.of(
                    chestBoat ? type.chestBoatTexture() : type.boatTexture(),
                    createBoatModel(context, type, chestBoat)));
        }
        this.boatResources = resources.buildOrThrow();
    }

    private static ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, ParadiseLostBoatType type, boolean chestBoat) {
        ModelPart root = context.bakeLayer(modelLayer(chestBoat));
        return chestBoat ? new ChestBoatModel(root) : new BoatModel(root);
    }

    private static ModelLayerLocation modelLayer(boolean chestBoat) {
        return chestBoat
                ? ModelLayers.createChestBoatModelName(Boat.Type.OAK)
                : ModelLayers.createBoatModelName(Boat.Type.OAK);
    }

    private static ParadiseLostBoatType woodOf(Boat boat) {
        if (boat instanceof ParadiseLostBoatEntity modBoat) {
            return modBoat.getWood();
        }
        if (boat instanceof ParadiseLostChestBoatEntity modChestBoat) {
            return modChestBoat.getWood();
        }
        return null;
    }

    @Override
    public void render(Boat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float hurtTime = (float) entity.getHurtTime() - partialTicks;
        float damage = entity.getDamage() - partialTicks;
        if (damage < 0.0F) {
            damage = 0.0F;
        }
        if (hurtTime > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * damage / 10.0F * (float) entity.getHurtDir()));
        }
        float bubbleAngle = entity.getBubbleAngle(partialTicks);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(bubbleAngle * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        ParadiseLostBoatType wood = woodOf(entity);

        Pair<ResourceLocation, ListModel<Boat>> model = boatResources.get(wood == null ? ParadiseLostBoatType.AUREL : wood);
        ResourceLocation texture = model.getFirst();
        ListModel<Boat> listModel = model.getSecond();

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listModel.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(listModel.renderType(texture));
        listModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (!entity.isUnderWater()) {
            VertexConsumer waterMask = buffer.getBuffer(RenderType.waterMask());
            if (listModel instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(poseStack, waterMask, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Boat entity) {
        ParadiseLostBoatType wood = woodOf(entity);
        return boatResources.get(wood == null ? ParadiseLostBoatType.AUREL : wood).getFirst();
    }
}
