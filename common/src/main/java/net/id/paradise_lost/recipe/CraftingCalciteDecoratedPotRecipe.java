package net.id.paradise_lost.recipe;

import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.PotDecorations;

public class CraftingCalciteDecoratedPotRecipe extends CustomRecipe {
    public CraftingCalciteDecoratedPotRecipe(CraftingBookCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingInput craftingRecipeInput, Level world) {
        if (craftingRecipeInput.width() != 3 || craftingRecipeInput.height() != 3) {
            return false;
        } else {
            for (int i = 0; i < craftingRecipeInput.size(); i++) {
                ItemStack itemStack = craftingRecipeInput.getItem(i);
                switch (i) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        if (!itemStack.is(ParadiseLostItemTags.CALCITE_DECORATED_POT_INGREDIENTS)) {
                            return false;
                        }
                        break;
                    case 4:
                        if (!itemStack.is(Items.CALCITE)) {
                            return false;
                        }
                        break;
                    default:
                        if (!itemStack.is(Items.AIR)) {
                            return false;
                        }
                }
            }

            return true;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput craftingRecipeInput, HolderLookup.Provider wrapperLookup) {
        PotDecorations sherds = new PotDecorations(
                craftingRecipeInput.getItem(1).getItem(),
                craftingRecipeInput.getItem(3).getItem(),
                craftingRecipeInput.getItem(5).getItem(),
                craftingRecipeInput.getItem(7).getItem()
        );
        return CalciteDecoratedPotBlockEntity.getStackWith(sherds);
    }

    @Override
    public RecipeSerializer<CraftingCalciteDecoratedPotRecipe> getSerializer() {
        return ParadiseLostRecipeTypes.CALCITE_DECORATED_POT_RECIPE_SERIALIZER;
    }
}
