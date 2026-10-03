package net.id.paradise_lost.recipe;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Shaped-like cookie recipe:
 * <pre>
 * A C A
 *   O
 * </pre>
 * where A is amadrys bushel, C is cocoa beans, and O is an ominous bottle.
 * Yields 8 cookies that inherit {@link DataComponents#OMINOUS_BOTTLE_AMPLIFIER}.
 */
public class CraftingOminousCookieRecipe extends CustomRecipe {
    public static final int OUTPUT_COUNT = 8;

    public CraftingOminousCookieRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        if (input.width() != 3 || input.height() != 2) {
            return false;
        }
        return input.getItem(0).isEmpty() &&
                input.getItem(1).is(Items.OMINOUS_BOTTLE) &&
                input.getItem(2).isEmpty() &&
                input.getItem(3).is(ItemRegistry.AMADRYS_BUSHEL.get()) &&
                input.getItem(4).is(Items.COCOA_BEANS) &&
                input.getItem(5).is(ItemRegistry.AMADRYS_BUSHEL.get());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack bottle = input.getItem(1);
        ItemStack result = new ItemStack(ItemRegistry.OMINOUS_COOKIE.get(), OUTPUT_COUNT);
        Integer amplifier = bottle.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
        if (amplifier != null) {
            result.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(ItemRegistry.OMINOUS_COOKIE.get(), OUTPUT_COUNT);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ParadiseLostRecipeTypes.OMINOUS_COOKIE_RECIPE_SERIALIZER;
    }
}
