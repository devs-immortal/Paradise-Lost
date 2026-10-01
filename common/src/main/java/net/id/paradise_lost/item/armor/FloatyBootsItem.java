package net.id.paradise_lost.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class FloatyBootsItem extends ArmorItem {
    /** Feather Falling IV equivalent: 3 blocks of fall protection per level. */
    public static final float FEATHER_FALLING_BLOCKS = 12.0F;

    public FloatyBootsItem(Holder<ArmorMaterial> material, Properties settings) {
        super(material, Type.BOOTS, settings);
    }

    public static boolean isWearing(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof FloatyBootsItem;
    }
}
