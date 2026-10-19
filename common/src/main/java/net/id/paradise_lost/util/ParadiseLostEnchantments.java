package net.id.paradise_lost.util;

import net.id.paradise_lost.PLConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ParadiseLostEnchantments {
    public static final ResourceKey<Enchantment> RENDING = key("rending");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(PLConstants.MOD_ID, name));
    }
}
