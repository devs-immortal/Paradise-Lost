package net.id.paradise_lost.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class FloatyBootsItem extends ArmorItem {
    /** Feather Falling IV equivalent: 3 blocks of fall protection per level. */
    public static final float FEATHER_FALLING_BLOCKS = 12.0F;

    public FloatyBootsItem(ArmorMaterial material, Properties settings) {
        super(material, ArmorType.BOOTS, settings);
    }

    public static boolean isWearing(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof FloatyBootsItem;
    }
}
