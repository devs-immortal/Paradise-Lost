package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FloatyLeggingsItem.class)
public abstract class FloatyLeggingsItemMixin extends ArmorItem {
    private static final ResourceLocation FLOATY_LEGGINGS_ANCHORED =
            ModConstants.id("textures/models/armor/floaty_layer_2_on.png");

    public FloatyLeggingsItemMixin(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(
            ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel
    ) {
        if (innerModel && entity instanceof ParadiseLostEntityExtensions extensions && extensions.isFloatyAnchored()) {
            return FLOATY_LEGGINGS_ANCHORED;
        }
        return null;
    }
}
