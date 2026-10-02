package net.id.paradiselost.recipe;

import net.id.paradiselost.blocks.blockentity.CalciteDecoratedPotBlockEntity;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.block.entity.Sherds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CraftingCalciteDecoratedPotRecipe extends SpecialCraftingRecipe {
    public CraftingCalciteDecoratedPotRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (craftingRecipeInput.getWidth() != 3 || craftingRecipeInput.getHeight() != 3) {
            return false;
        } else {
            for (int i = 0; i < craftingRecipeInput.size(); i++) {
                ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
                switch (i) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        if (!itemStack.isIn(ParadiseLostItemTags.CALCITE_DECORATED_POT_INGREDIENTS)) {
                            return false;
                        }
                        break;
                    case 4:
                        if (!itemStack.isOf(Items.CALCITE)) {
                            return false;
                        }
                        break;
                    default:
                        if (!itemStack.isOf(Items.AIR)) {
                            return false;
                        }
                }
            }

            return true;
        }
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        Sherds sherds = new Sherds(
                craftingRecipeInput.getStackInSlot(1).getItem(),
                craftingRecipeInput.getStackInSlot(3).getItem(),
                craftingRecipeInput.getStackInSlot(5).getItem(),
                craftingRecipeInput.getStackInSlot(7).getItem()
        );
        return CalciteDecoratedPotBlockEntity.getStackWith(sherds);
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return RecipeSerializer.CRAFTING_DECORATED_POT;
    }
}
