package net.id.paradise_lost.client.rendering.armor;

import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.armor.OrnateOlviteArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class OrnateOlviteArmorClientExtensions implements IClientItemExtensions {
    private OrnateOlviteArmorModel armorModel;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (armorModel == null) {
            armorModel = new OrnateOlviteArmorModel(
                    Minecraft.getInstance().getEntityModels().bakeLayer(ParadiseLostModelLayers.ORNATE_OLVITE_ARMOR));
        }
        HumanoidModel rawOriginal = original;
        rawOriginal.copyPropertiesTo(armorModel);
        armorModel.setAllVisible(false);
        armorModel.head.visible = equipmentSlot == EquipmentSlot.HEAD;
        armorModel.hat.visible = equipmentSlot == EquipmentSlot.HEAD;
        return armorModel;
    }
}
