package net.id.paradise_lost.client.rendering.armor;

import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.client.model.Model;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.armor.OrnateOlviteArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class OrnateOlviteArmorClientExtensions implements IClientItemExtensions {
    private OrnateOlviteArmorModel armorModel;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentModel.LayerType layerType, Model original) {
        if (armorModel == null) {
            armorModel = new OrnateOlviteArmorModel(
                    Minecraft.getInstance().getEntityModels().bakeLayer(ParadiseLostModelLayers.ORNATE_OLVITE_ARMOR));
        }
        // NeoForge 21.3 does not copy the pose into replacement models yet
        if (original instanceof HumanoidModel humanoid) {
            humanoid.copyPropertiesTo(armorModel);
        }
        armorModel.setAllVisible(false);
        armorModel.head.visible = true;
        armorModel.hat.visible = true;
        return armorModel;
    }
}
