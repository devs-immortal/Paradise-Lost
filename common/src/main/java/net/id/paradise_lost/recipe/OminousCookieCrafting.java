package net.id.paradise_lost.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;

final class OminousCookieCrafting {
    static final int OUTPUT_COUNT = 8;

    static void copyAmplifier(CraftingInput input, ItemStack result) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.OMINOUS_BOTTLE)) {
                Integer amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
                if (amplifier != null) {
                    result.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier);
                }
                break;
            }
        }
    }
}
