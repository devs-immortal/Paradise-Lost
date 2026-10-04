package net.id.paradise_lost.recipe;

import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.crafting.CraftingInput;

final class OminousCookieCrafting {
    static final int OUTPUT_COUNT = 8;

    static void copyAmplifier(CraftingInput input, ItemStack result) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.OMINOUS_BOTTLE)) {
                OminousBottleAmplifier amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
                if (amplifier != null) {
                    result.set(ParadiseLostDataComponentTypes.OMINOUS_COOKIE_AMPLIFIER, amplifier.value());
                }
                break;
            }
        }
    }
}
