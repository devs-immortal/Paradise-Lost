package net.id.paradise_lost.test.server;

import net.id.paradise_lost.recipe.ParadiseLostRecipeTypes;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class PotTests {
    private PotTests() {
    }

    @ServerTest("calcite pot recipe uses its own serializer")
    private static Step calcitePotSerializer() {
        return Step.run(0, () -> {
            var recipe = ServerTestRunner.server().getRecipeManager()
                    .byKey(ResourceLocation.fromNamespaceAndPath("paradise_lost", "calcite_decorated_pot"))
                    .orElseThrow(() -> new AssertionError("recipe paradise_lost:calcite_decorated_pot not found"));
            var serializer = recipe.value().getSerializer();
            check(serializer == ParadiseLostRecipeTypes.CALCITE_DECORATED_POT_RECIPE_SERIALIZER, "serializer is " + BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer));
        });
    }

    @ServerTest("a crafted calcite pot stacks with the creative one")
    private static Step calcitePotStacks() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            ItemStack calcite = new ItemStack(Items.CALCITE);
            CraftingInput input = CraftingInput.of(3, 3, List.of(
                    ItemStack.EMPTY, calcite, ItemStack.EMPTY,
                    calcite, calcite, calcite,
                    ItemStack.EMPTY, calcite, ItemStack.EMPTY));
            var recipes = ServerTestRunner.server().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, world);
            check(!recipes.isEmpty(), "no recipe matched 5 calcite");

            ItemStack creative = new ItemStack(BlockRegistry.CALCITE_DECORATED_POT.get());
            List<String> wrong = new ArrayList<>();
            for (var recipe : recipes) {
                ItemStack crafted = recipe.value().assemble(input, world.registryAccess());
                expect(wrong, ItemStack.isSameItemSameComponents(crafted, creative),
                        recipe.id() + " crafted " + crafted + " with " + crafted.getComponentsPatch() + " creative pot has " + creative.getComponentsPatch());
            }
            checkAll(wrong);
        });
    }
}
