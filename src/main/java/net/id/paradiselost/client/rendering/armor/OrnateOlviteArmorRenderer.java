package net.id.paradiselost.client.rendering.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.armor.OrnateOlviteArmorModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class OrnateOlviteArmorRenderer implements ArmorRenderer {
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/models/armor/paradise_lost_ornate_olvite.png");
    private static OrnateOlviteArmorModel armorModel;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, BipedEntityRenderState state, EquipmentSlot slot, int light, BipedEntityModel<BipedEntityRenderState> contextModel) {
        if (armorModel == null) {
            armorModel = new OrnateOlviteArmorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(ParadiseLostModelLayers.ORNATE_OLVITE_ARMOR));
        }
        contextModel.copyTransforms(armorModel);
        armorModel.setVisible(false);
        armorModel.head.visible = slot == EquipmentSlot.HEAD;
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, TEXTURE);
    }
}
