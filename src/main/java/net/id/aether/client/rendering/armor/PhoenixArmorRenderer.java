package net.id.aether.client.rendering.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.aether.client.model.armor.PhoenixArmorModel;
import net.id.aether.client.model.AetherModelLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PhoenixArmorRenderer implements ArmorRenderer {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/models/armor/aether_phoenix_layer_1.png");
    private static PhoenixArmorModel phoenixArmorModel;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (phoenixArmorModel == null) {
            phoenixArmorModel = new PhoenixArmorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(AetherModelLayers.PHOENIX_ARMOR));
        }
        contextModel.setAttributes(phoenixArmorModel);
        phoenixArmorModel.setVisible(false);
        phoenixArmorModel.head.visible = slot == EquipmentSlot.HEAD;
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, phoenixArmorModel, TEXTURE);
    }
}
