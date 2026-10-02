package net.id.paradise_lost.enchantment;

import net.id.paradise_lost.util.ParadiseLostEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public final class ParadiseLostEnchantmentHelper {
    private static final float EXTRA_SOUL_CHANCE_PER_LEVEL = 0.30F;

    private ParadiseLostEnchantmentHelper() {
    }

    public static int getRendingLevel(ItemStack stack, Level level) {
        return level.registryAccess()
                .lookup(Registries.ENCHANTMENT)
                .flatMap(registry -> registry.get(ParadiseLostEnchantments.RENDING))
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack))
                .orElse(0);
    }

    public static float getExtraSoulChance(int rendingLevel) {
        if (rendingLevel <= 0) {
            return 0.0F;
        }
        return Math.min(EXTRA_SOUL_CHANCE_PER_LEVEL * rendingLevel, 1f);
    }

    public static boolean rollExtraSoul(ItemStack stack, Level level, RandomSource random) {
        return random.nextFloat() < getExtraSoulChance(getRendingLevel(stack, level));
    }
}
