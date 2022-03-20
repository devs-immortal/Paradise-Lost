package net.id.aether.items.accessories;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.client.model.item.ParachuteTrinketModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ParachuteTrinketItem extends TrinketItem implements TrinketRenderer {

    private final Identifier texture;
    private BipedEntityModel<LivingEntity> model;

    public ParachuteTrinketItem(Settings settings, String texturePath) {
        super(settings);
        texture = Aether.locate("textures/entity/trinket/"+texturePath+".png");
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.fallDistance > 0 && !entity.isFallFlying()) { // If falling and not elytra-ing
            BipedEntityModel<LivingEntity> model = this.getModel();
            model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
            model.animateModel(entity, limbAngle, limbDistance, tickDelta);
            TrinketRenderer.followBodyRotations(entity, model);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(texture));
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        }
    }

    @Environment(EnvType.CLIENT)
    private BipedEntityModel<LivingEntity> getModel() {
        if (this.model == null) {
            this.model = new ParachuteTrinketModel(ParachuteTrinketModel.getTexturedModelData().createModel());
        }
        return this.model;
    }
}
