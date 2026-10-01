package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(FloatyBootsItem.class)
public abstract class FloatyBootsItemMixin extends ArmorItem {
    public FloatyBootsItemMixin(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.FEATHER_FALLING)) return false;
        return super.supportsEnchantment(stack, enchantment);
    }
}
