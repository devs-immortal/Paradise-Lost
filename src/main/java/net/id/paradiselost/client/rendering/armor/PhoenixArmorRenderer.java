package net.id.paradiselost.client.rendering.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.armor.PhoenixArmorModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PhoenixArmorRenderer implements ArmorRenderer { //24: Left this in to repurpose it later because I liked it
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/models/armor/paradise_lost_phoenix_layer_1.png");
    private static PhoenixArmorModel phoenixArmorModel;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (phoenixArmorModel == null) {
            phoenixArmorModel = new PhoenixArmorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(ParadiseLostModelLayers.PHOENIX_ARMOR));
        }
        contextModel.copyBipedStateTo(phoenixArmorModel);
        phoenixArmorModel.setVisible(false);
        phoenixArmorModel.head.visible = slot == EquipmentSlot.HEAD;
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, phoenixArmorModel, TEXTURE);
    }
}
