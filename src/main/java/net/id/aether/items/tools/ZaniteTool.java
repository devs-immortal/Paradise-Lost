package net.id.aether.items.tools;

import net.minecraft.item.ItemStack;

public class ZaniteTool {
    // Creates a parabola that smoothly matches the old durability scaling
    public static float calculateSpeedBoost(ItemStack tool) {
        int damage = tool.getDamage();
        int max = tool.getMaxDamage();
        return (float) (-((damage * damage) / (0.29 * max * max)) + 7.15);
    }
}
