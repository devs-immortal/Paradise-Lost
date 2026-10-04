package net.id.paradise_lost.client.rendering.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.armor.OrnateOlviteArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class OrnateOlviteArmorRenderer implements ArmorRenderer {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/equipment/humanoid/ornate_olvite.png");
    private static OrnateOlviteArmorModel armorModel;

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (armorModel == null) {
            armorModel = new OrnateOlviteArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(ParadiseLostModelLayers.ORNATE_OLVITE_ARMOR));
        }
        contextModel.copyPropertiesTo(armorModel);
        armorModel.setAllVisible(false);
        armorModel.head.visible = slot == EquipmentSlot.HEAD;
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, TEXTURE);
    }
}
