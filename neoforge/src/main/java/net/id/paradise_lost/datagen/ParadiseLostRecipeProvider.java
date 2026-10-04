package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.recipe.CraftingCalciteDecoratedPotRecipe;
import net.id.paradise_lost.recipe.CraftingOminousCookieRecipe;
import net.id.paradise_lost.recipe.CraftingOminousCookieShapelessRecipe;
import net.id.paradise_lost.recipe.TreeTapRecipe;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ParadiseLostRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final String HAS_INGREDIENT = "has_ingredient";

    public ParadiseLostRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        woodSet(output, BlockRegistry.AUREL_WOODSTUFF, ParadiseLostItemTags.AUREL_LOGS,
                ItemRegistry.AUREL_SIGN.get(), ItemRegistry.AUREL_HANGING_SIGN.get(),
                ItemRegistry.AUREL_BOATS.boat().get(), ItemRegistry.AUREL_BOATS.chestBoat().get());
        woodSet(output, BlockRegistry.MOTHER_AUREL_WOODSTUFF, ParadiseLostItemTags.MOTHER_AUREL_LOGS,
                ItemRegistry.MOTHER_AUREL_SIGN.get(), ItemRegistry.MOTHER_AUREL_HANGING_SIGN.get(),
                ItemRegistry.MOTHER_AUREL_BOATS.boat().get(), ItemRegistry.MOTHER_AUREL_BOATS.chestBoat().get());
        woodSet(output, BlockRegistry.MENTH_WOODSTUFF, ParadiseLostItemTags.MENTH_LOGS,
                ItemRegistry.MENTH_SIGN.get(), ItemRegistry.MENTH_HANGING_SIGN.get(),
                ItemRegistry.MENTH_BOATS.boat().get(), ItemRegistry.MENTH_BOATS.chestBoat().get());
        woodSet(output, BlockRegistry.WISTERIA_WOODSTUFF, ParadiseLostItemTags.WISTERIA_LOGS,
                ItemRegistry.WISTERIA_SIGN.get(), ItemRegistry.WISTERIA_HANGING_SIGN.get(),
                ItemRegistry.WISTERIA_BOATS.boat().get(), ItemRegistry.WISTERIA_BOATS.chestBoat().get());

        buildExtraRecipes(output);
    }

    private void ominousCookieShaped(RecipeOutput output) {
        ResourceLocation id = ModConstants.id("ominous_cookie");
        CraftingOminousCookieRecipe recipe = new CraftingOminousCookieRecipe(
                "",
                CraftingBookCategory.MISC,
                ShapedRecipePattern.of(
                        Map.of(
                                'O', Ingredient.of(Items.OMINOUS_BOTTLE),
                                'A', Ingredient.of(ItemRegistry.AMADRYS_BUSHEL.get()),
                                'B', Ingredient.of(ItemRegistry.BLACKCURRANT.get())
                        ),
                        " O ",
                        "ABA"
                ),
                new ItemStack(ItemRegistry.OMINOUS_COOKIE.get(), CraftingOminousCookieRecipe.OUTPUT_COUNT),
                true
        );
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .addCriterion(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BUSHEL.get()))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/misc/")));
    }

    private void ominousCookieShapeless(RecipeOutput output) {
        ResourceLocation id = ModConstants.id("ominous_cookie_from_cookies");
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < CraftingOminousCookieShapelessRecipe.OUTPUT_COUNT; i++) {
            ingredients.add(Ingredient.of(ItemRegistry.BLACKCURRANT_COOKIE.get()));
        }
        ingredients.add(Ingredient.of(Items.OMINOUS_BOTTLE));
        CraftingOminousCookieShapelessRecipe recipe = new CraftingOminousCookieShapelessRecipe(
                "",
                CraftingBookCategory.MISC,
                new ItemStack(ItemRegistry.OMINOUS_COOKIE.get(), CraftingOminousCookieShapelessRecipe.OUTPUT_COUNT),
                ingredients
        );
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .addCriterion(HAS_INGREDIENT, has(ItemRegistry.BLACKCURRANT_COOKIE.get()))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/misc/")));
    }

    private void woodSet(
            RecipeOutput output,
            BlockRegistry.WoodBlockSet wood,
            TagKey<Item> logs,
            ItemLike sign,
            ItemLike hangingSign,
            ItemLike boat,
            ItemLike chestBoat
    ) {
        ItemLike planks = wood.plank().get();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(logs)
                .group("planks")
                .unlockedBy("has_logs", has(logs))
                .save(output);

        stairBuilder(wood.plankStairs().get(), Ingredient.of(planks))
                .group("wooden_stairs")
                .unlockedBy("has_planks", has(planks))
                .save(output);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, wood.plankSlab().get(), Ingredient.of(planks))
                .group("wooden_slab")
                .unlockedBy("has_planks", has(planks))
                .save(output);
        fenceBuilder(wood.fence().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);
        fenceGateBuilder(wood.fenceGate().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);
        doorBuilder(wood.door().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);
        trapdoorBuilder(wood.trapdoor().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);
        buttonBuilder(wood.button().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);
        pressurePlateBuilder(RecipeCategory.REDSTONE, wood.pressurePlate().get(), Ingredient.of(planks))
                .unlockedBy("has_planks", has(planks))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3)
                .define('#', planks)
                .define('X', Items.STICK)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .group("wooden_sign")
                .unlockedBy("has_planks", has(planks))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
                .define('#', planks)
                .define('X', Items.CHAIN)
                .pattern("X X")
                .pattern("###")
                .pattern("###")
                .group("hanging_sign")
                .unlockedBy("has_planks", has(planks))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat)
                .define('#', planks)
                .pattern("# #")
                .pattern("###")
                .group("boat")
                .unlockedBy("has_planks", has(planks))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
                .requires(boat)
                .requires(Items.CHEST)
                .group("chest_boat")
                .unlockedBy("has_boat", has(boat))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.wood().get(), 3)
                .define('#', wood.log().get())
                .pattern("##")
                .pattern("##")
                .group("bark")
                .unlockedBy("has_log", has(wood.log().get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.strippedWood().get(), 3)
                .define('#', wood.strippedLog().get())
                .pattern("##")
                .pattern("##")
                .group("bark")
                .unlockedBy("has_log", has(wood.strippedLog().get()))
                .save(output);
    }

    private void buildExtraRecipes(RecipeOutput output) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ACTIVATOR_RAIL, 6)
                .define('#', Items.REDSTONE_TORCH)
                .define('S', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XSX")
                .pattern("X#X")
                .pattern("XSX")
                .unlockedBy(HAS_INGREDIENT, has(Items.REDSTONE_TORCH))
                .save(output, ModConstants.id("activator_rail_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.AMADRYS_BREAD.get(), 1)
                .define('B', ItemRegistry.AMADRYS_BUSHEL.get())
                .pattern("BBB")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BUSHEL.get()))
                .save(output, ModConstants.id("amadrys_bread"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.AMADRYS_BREAD_GLAZED.get(), 1)
                .requires(ItemRegistry.AMADRYS_BREAD.get())
                .requires(Items.SUGAR)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BREAD.get()))
                .save(output, ModConstants.id("amadrys_bread_glazed"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.AMADRYS_BREAD_GLAZED_FILLED.get(), 1)
                .requires(ItemRegistry.AMADRYS_BREAD.get())
                .requires(Items.SUGAR)
                .requires(ItemRegistry.POPOM_JELLY.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BREAD.get()))
                .save(output, ModConstants.id("amadrys_bread_glazed_filled"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.AMADRYS_BREAD_GLAZED_FILLED.get(), 1)
                .requires(ItemRegistry.AMADRYS_BREAD_GLAZED.get())
                .requires(ItemRegistry.POPOM_JELLY.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BREAD_GLAZED.get()))
                .save(output, ModConstants.id("amadrys_bread_glazed_filled_from_amadrys_bread_glazed"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.AMADRYS_BUNDLE.get(), 1)
                .define('#', ItemRegistry.AMADRYS_BUSHEL.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BUSHEL.get()))
                .save(output, ModConstants.id("amadrys_bundle"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.AMADRYS_BUSHEL.get(), 9)
                .requires(BlockRegistry.AMADRYS_BUNDLE.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.AMADRYS_BUNDLE.get()))
                .save(output, ModConstants.id("amadrys_bushel"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.AMADRYS_NOODLES.get(), 1)
                .requires(Items.BOWL)
                .requires(ItemRegistry.AMADRYS_BUSHEL.get())
                .requires(ItemRegistry.BLACKCURRANT.get())
                .requires(ParadiseLostItemTags.MUSHROOMS)
                .unlockedBy(HAS_INGREDIENT, has(Items.BOWL))
                .save(output, ModConstants.id("amadrys_noodles"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.AUREL_BOOKSHELF.get(), 1)
                .define('#', BlockRegistry.AUREL_WOODSTUFF.plank().get())
                .define('X', Items.BOOK)
                .pattern("###")
                .pattern("XXX")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.AUREL_WOODSTUFF.plank().get()))
                .save(output, ModConstants.id("aurel_bookshelf"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.AUREL_BUCKET.get(), 1)
                .define('#', ParadiseLostItemTags.PARADISE_PLANKS)
                .pattern("# #")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(ParadiseLostItemTags.PARADISE_PLANKS))
                .save(output, ModConstants.id("aurel_bucket"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.AUREL_LEAF_PILE.get(), 3)
                .define('#', BlockRegistry.AUREL_WOODSTUFF.leaves().get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.AUREL_WOODSTUFF.leaves().get()))
                .save(output, ModConstants.id("aurel_leaf_pile"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.BLACKCURRANT_COOKIE.get(), 8)
                .define('B', ItemRegistry.AMADRYS_BUSHEL.get())
                .define('C', ItemRegistry.BLACKCURRANT.get())
                .pattern("BCB")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.AMADRYS_BUSHEL.get()))
                .save(output, ModConstants.id("blackcurrant_cookie"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.BLACKCURRANT_PIE.get(), 1)
                .requires(ItemRegistry.BLACKCURRANT.get())
                .requires(ItemRegistry.BLACKCURRANT.get())
                .requires(Items.EGG)
                .requires(Items.SUGAR)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.BLACKCURRANT.get()))
                .save(output, ModConstants.id("blackcurrant_pie"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE.get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BLOOMED_CALCITE_TILES_SET.slab().get(), 6)
                .define('#', BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE.get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_slab_from_bloomed_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_slab_from_bloomed_calcite_tiles_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BLOOMED_CALCITE_TILES_SET.stairs().get(), 4)
                .define('#', BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE.get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_stairs_from_bloomed_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_stairs_from_bloomed_calcite_tiles_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BLOOMED_CALCITE_TILES_WALL.get(), 6)
                .define('#', BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE.get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_wall_from_bloomed_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BLOOMED_CALCITE_TILES_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("bloomed_calcite_tiles_wall_from_bloomed_calcite_tiles_stonecutting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.MISC, BlockRegistry.BURNISHED_STONE_SET.block().get(), 0.1F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("burnished_stone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ARMOR_STAND, 1)
                .define('/', Items.STICK)
                .define('_', BlockRegistry.BURNISHED_STONE_SET.slab().get())
                .pattern("///")
                .pattern(" / ")
                .pattern("/_/")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("burnished_stone_armor_stand"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BURNISHED_STONE_SET.slab().get(), 6)
                .define('#', BlockRegistry.BURNISHED_STONE_SET.block().get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BURNISHED_STONE_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BURNISHED_STONE_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_slab_from_burnished_stone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BURNISHED_STONE_SET.stairs().get(), 4)
                .define('#', BlockRegistry.BURNISHED_STONE_SET.block().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BURNISHED_STONE_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BURNISHED_STONE_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_stairs_from_burnished_stone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BURNISHED_STONE_WALL.get(), 6)
                .define('#', BlockRegistry.BURNISHED_STONE_SET.block().get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.BURNISHED_STONE_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BURNISHED_STONE_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("burnished_stone_wall_from_burnished_stone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CAKE, 1)
                .define('A', TagKey.create(Registries.ITEM, ResourceLocation.parse("c:buckets/milk")))
                .define('B', Items.SUGAR)
                .define('C', Items.WHEAT)
                .define('E', Items.EGG)
                .pattern("AAA")
                .pattern("BEB")
                .pattern("CCC")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:buckets/milk"))))
                .save(output, ModConstants.id("cake_with_aurel_milk_buckets"));

        SpecialRecipeBuilder.special(CraftingCalciteDecoratedPotRecipe::new)
                .save(output, ModConstants.id("calcite_decorated_pot"));

        ominousCookieShaped(output);
        ominousCookieShapeless(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CALCITE_DECORATED_POT.get(), 1)
                .define('#', Items.CALCITE)
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_decorated_pot_simple"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CALCITE_FLOWER_POT.get(), 1)
                .define('#', Items.CALCITE)
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_flower_pot"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.CALCITE), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_SET.block().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_tiles"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CALCITE_TILES_SET.slab().get(), 6)
                .define('#', BlockRegistry.CALCITE_TILES_SET.block().get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.CALCITE), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_tiles_slab_from_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_slab_from_calcite_tiles_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CALCITE_TILES_SET.stairs().get(), 4)
                .define('#', BlockRegistry.CALCITE_TILES_SET.block().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.CALCITE), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_tiles_stairs_from_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_stairs_from_calcite_tiles_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CALCITE_TILES_WALL.get(), 6)
                .define('#', BlockRegistry.CALCITE_TILES_SET.block().get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.CALCITE), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(Items.CALCITE))
                .save(output, ModConstants.id("calcite_tiles_wall_from_calcite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.CALCITE_TILES_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CALCITE_TILES_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CALCITE_TILES_SET.block().get()))
                .save(output, ModConstants.id("calcite_tiles_wall_from_calcite_tiles_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHERINE_BLOCK.get(), 1)
                .define('#', ItemRegistry.CHERINE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.CHERINE.get()))
                .save(output, ModConstants.id("cherine_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CHERINE_BLOODSTONE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.CHERINE.get())
                .pattern(" X")
                .pattern("# ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("cherine_bloodstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHERINE_CAMPFIRE.get(), 1)
                .define('C', ItemRegistry.CHERINE.get())
                .define('L', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:logs")))
                .define('S', Items.STICK)
                .pattern(" S ")
                .pattern("SCS")
                .pattern("LLL")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.CHERINE.get()))
                .save(output, ModConstants.id("cherine_campfire"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BlockRegistry.CHERINE_ORE.get()), RecipeCategory.MISC, ItemRegistry.CHERINE.get(), 0.1F, 100)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CHERINE_ORE.get()))
                .save(output, ModConstants.id("cherine_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.CHERINE_ORE.get()), RecipeCategory.MISC, ItemRegistry.CHERINE.get(), 0.1F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CHERINE_ORE.get()))
                .save(output, ModConstants.id("cherine_from_smelting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHERINE_LANTERN.get(), 1)
                .define('#', ItemRegistry.CHERINE_TORCH.get())
                .define('X', ItemRegistry.OLVITE_NUGGET.get())
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.CHERINE_TORCH.get()))
                .save(output, ModConstants.id("cherine_lantern"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.CHERINE.get(), 9)
                .requires(BlockRegistry.CHERINE_BLOCK.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CHERINE_BLOCK.get()))
                .save(output, ModConstants.id("cherine_shard"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CHERINE_TORCH.get(), 4)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.CHERINE.get())
                .pattern("X")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("cherine_torch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHISELED_FLOESTONE.get(), 1)
                .define('#', BlockRegistry.FLOESTONE_BRICK_SLAB.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK_SLAB.get()))
                .save(output, ModConstants.id("chiseled_floestone"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CHISELED_FLOESTONE.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("chiseled_floestone_from_floestone_brick_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CHISELED_FLOESTONE.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("chiseled_floestone_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHISELED_LEVITA_BRICK.get(), 1)
                .define('#', BlockRegistry.LEVITA_BRICK_SET.slab().get())
                .pattern("#")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.slab().get()))
                .save(output, ModConstants.id("chiseled_levita_brick"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.LEVITA_BRICK_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CHISELED_LEVITA_BRICK.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.block().get()))
                .save(output, ModConstants.id("chiseled_levita_brick_from_levita_brick_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.COBBLED_FLOESTONE_SLAB.get(), 6)
                .define('#', BlockRegistry.COBBLED_FLOESTONE.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.COBBLED_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.COBBLED_FLOESTONE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_slab_from_cobbled_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.COBBLED_FLOESTONE_STAIRS.get(), 4)
                .define('#', BlockRegistry.COBBLED_FLOESTONE.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.COBBLED_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.COBBLED_FLOESTONE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_stairs_from_cobbled_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.COBBLED_FLOESTONE_WALL.get(), 6)
                .define('#', BlockRegistry.COBBLED_FLOESTONE.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.COBBLED_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.COBBLED_FLOESTONE_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("cobbled_floestone_wall_from_cobbled_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COMPARATOR, 1)
                .define('#', Items.REDSTONE_TORCH)
                .define('I', BlockRegistry.FLOESTONE.get())
                .define('X', Items.QUARTZ)
                .pattern(" # ")
                .pattern("#X#")
                .pattern("III")
                .unlockedBy(HAS_INGREDIENT, has(Items.REDSTONE_TORCH))
                .save(output, ModConstants.id("comparator_floestone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COMPASS, 1)
                .define('I', ItemRegistry.OLVITE.get())
                .define('R', Items.REDSTONE)
                .pattern(" I ")
                .pattern("IRI")
                .pattern(" I ")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("compass_olvite"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ItemRegistry.MOA_MEAT.get()), RecipeCategory.MISC, ItemRegistry.COOKED_MOA_MEAT.get(), 0.3F, 600)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.MOA_MEAT.get()))
                .save(output, ModConstants.id("cooked_moa_meat_from_campfire_cooking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemRegistry.MOA_MEAT.get()), RecipeCategory.MISC, ItemRegistry.COOKED_MOA_MEAT.get(), 0.3F, 200)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.MOA_MEAT.get()))
                .save(output, ModConstants.id("cooked_moa_meat_from_smelting"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ItemRegistry.MOA_MEAT.get()), RecipeCategory.MISC, ItemRegistry.COOKED_MOA_MEAT.get(), 0.3F, 100)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.MOA_MEAT.get()))
                .save(output, ModConstants.id("cooked_moa_meat_from_smoking"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CRAFTER, 1)
                .define('#', ItemRegistry.OLVITE.get())
                .define('C', Items.CRAFTING_TABLE)
                .define('D', Items.DROPPER)
                .define('R', Items.REDSTONE)
                .pattern("###")
                .pattern("#C#")
                .pattern("RDR")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("crafter_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CROSSBOW, 1)
                .define('#', Items.STICK)
                .define('$', Items.TRIPWIRE_HOOK)
                .define('&', ItemRegistry.OLVITE.get())
                .define('~', Items.STRING)
                .pattern("#&#")
                .pattern("~$~")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("crossbow_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DETECTOR_RAIL, 6)
                .define('#', BlockRegistry.FLOESTONE_PRESSURE_PLATE.get())
                .define('R', Items.REDSTONE)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X X")
                .pattern("X#X")
                .pattern("XRX")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_PRESSURE_PLATE.get()))
                .save(output, ModConstants.id("detector_rail_olvite"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 2)
                .requires(ItemRegistry.FLAX_THREAD.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAX_THREAD.get()))
                .save(output, ModConstants.id("flax_thread_to_string"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.FLAXWEAVE.get(), 1)
                .define('#', ItemRegistry.FLAX_THREAD.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAX_THREAD.get()))
                .save(output, ModConstants.id("flaxweave"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.FLOATY_BOOTS.get(), 1)
                .define('X', ItemRegistry.FLAXWEAVE.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAXWEAVE.get()))
                .save(output, ModConstants.id("floaty_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.FLOATY_LEGGINGS.get(), 1)
                .define('X', ItemRegistry.FLAXWEAVE.get())
                .define('G', ItemRegistry.LEVITA_GEM.get())
                .pattern("XXX")
                .pattern("G G")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAXWEAVE.get()))
                .save(output, ModConstants.id("floaty_leggings"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BOOK, 1)
                .requires(Items.PAPER)
                .requires(Items.PAPER)
                .requires(Items.PAPER)
                .requires(ItemRegistry.FLAXWEAVE.get())
                .requires(ItemRegistry.FLAXWEAVE.get())
                .unlockedBy(HAS_INGREDIENT, has(Items.PAPER))
                .save(output, ModConstants.id("flaxweave_book"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLAXWEAVE_CUSHION.get(), 1)
                .define('#', ItemRegistry.FLAXWEAVE.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAXWEAVE.get()))
                .save(output, ModConstants.id("flaxweave_cushion"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLAXWEAVE_CUSHION.get(), 1)
                .define('#', BlockRegistry.FLAXWEAVE_CUSHION_SLAB.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLAXWEAVE_CUSHION_SLAB.get()))
                .save(output, ModConstants.id("flaxweave_cushion_from_slabs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLAXWEAVE_CUSHION_SLAB.get(), 1)
                .define('#', ItemRegistry.FLAXWEAVE.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAXWEAVE.get()))
                .save(output, ModConstants.id("flaxweave_cushion_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLAXWEAVE_CUSHION_SLAB.get(), 6)
                .define('#', BlockRegistry.FLAXWEAVE_CUSHION.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLAXWEAVE_CUSHION.get()))
                .save(output, ModConstants.id("flaxweave_cushion_slab_from_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.FLAXWEAVE.get(), 4)
                .requires(BlockRegistry.FLAXWEAVE_CUSHION.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLAXWEAVE_CUSHION.get()))
                .save(output, ModConstants.id("flaxweave_from_cushion"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ITEM_FRAME, 1)
                .define('F', ItemRegistry.FLAXWEAVE.get())
                .define('S', Items.STICK)
                .pattern("SSS")
                .pattern("SFS")
                .pattern("SSS")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.FLAXWEAVE.get()))
                .save(output, ModConstants.id("flaxweave_item_frame"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.COBBLED_FLOESTONE.get()), RecipeCategory.MISC, BlockRegistry.FLOESTONE.get(), 0.1F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("floestone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_BRICK.get(), 4)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_brick"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_brick_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_BRICK_SLAB.get(), 6)
                .define('#', BlockRegistry.FLOESTONE_BRICK.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_slab_from_floestone_brick_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_brick_slab_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_BRICK_STAIRS.get(), 4)
                .define('#', BlockRegistry.FLOESTONE_BRICK.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_stairs_from_floestone_brick_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_brick_stairs_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_BRICK_WALL.get(), 6)
                .define('#', BlockRegistry.FLOESTONE_BRICK.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("floestone_brick_wall_from_floestone_brick_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_BRICK_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_brick_wall_from_floestone_stonecutting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_BRICK.get(), 1)
                .requires(BlockRegistry.FLOESTONE_BRICK.get())
                .requires(Items.MOSS_BLOCK)
                .group("mossy_floestone_brick")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_from_moss_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_BRICK.get(), 1)
                .requires(BlockRegistry.FLOESTONE_BRICK.get())
                .requires(Items.VINE)
                .group("mossy_floestone_brick")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_from_vine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_BRICK_SLAB.get(), 6)
                .define('#', BlockRegistry.MOSSY_FLOESTONE_BRICK.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_BRICK_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_slab_from_mossy_floestone_brick_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_BRICK_STAIRS.get(), 4)
                .define('#', BlockRegistry.MOSSY_FLOESTONE_BRICK.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_BRICK_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_stairs_from_mossy_floestone_brick_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_BRICK_WALL.get(), 6)
                .define('#', BlockRegistry.MOSSY_FLOESTONE_BRICK.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_BRICK_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE_BRICK.get()))
                .save(output, ModConstants.id("mossy_floestone_brick_wall_from_mossy_floestone_brick_stonecutting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.FLOESTONE_BUTTON.get(), 1)
                .requires(BlockRegistry.FLOESTONE.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_button"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_PRESSURE_PLATE.get(), 1)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_SLAB.get(), 6)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_slab_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_STAIRS.get(), 4)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_stairs_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FLOESTONE_WALL.get(), 6)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.FLOESTONE_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("floestone_wall_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FOOD_BOWL.get(), 1)
                .define('#', BlockRegistry.FLOESTONE.get())
                .define('X', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .pattern("X X")
                .pattern("X X")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("food_bowl"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.FROST_WISTERIA_LEAF_PILE.get(), 3)
                .define('#', BlockRegistry.FROST_WISTERIA_LEAVES.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FROST_WISTERIA_LEAVES.get()))
                .save(output, ModConstants.id("frost_wisteria_leaf_pile"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_AXE),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_AXE.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_axe"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_BOOTS),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_BOOTS.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_boots"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_CHESTPLATE),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_CHESTPLATE.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_chestplate"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_HELMET),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_HELMET.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_helmet"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_HOE),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_HOE.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_hoe"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_LEGGINGS),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_LEGGINGS.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_leggings"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_PICKAXE),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_PICKAXE.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_pickaxe"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_SHOVEL),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_SHOVEL.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_shovel"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.GLAZED_GOLD_UPGRADE.get()),
                        Ingredient.of(Items.GOLDEN_SWORD),
                        Ingredient.of(Items.GOLD_BLOCK),
                        RecipeCategory.MISC,
                        ItemRegistry.GLAZED_GOLD_SWORD.get().asItem())
                .unlocks("has_template", has(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                .save(output, ModConstants.id("glazed_gold_sword"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.GLAZED_GOLD_UPGRADE.get(), 1)
                .define('A', ItemRegistry.GOLDEN_AMBER.get())
                .define('G', Items.GOLD_BLOCK)
                .define('T', ItemRegistry.GLAZED_GOLD_UPGRADE.get())
                .pattern("ATA")
                .pattern("AGA")
                .pattern("AAA")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.GOLDEN_AMBER.get()))
                .save(output, ModConstants.id("glazed_gold_upgrade_smithing_template"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.GOLDEN_AMBER_BARS.get(), 16)
                .define('#', ItemRegistry.GOLDEN_AMBER.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.GOLDEN_AMBER.get()))
                .save(output, ModConstants.id("golden_amber_bars"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.GOLDEN_AMBER_TILE.get(), 4)
                .define('#', ItemRegistry.GOLDEN_AMBER.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.GOLDEN_AMBER.get()))
                .save(output, ModConstants.id("golden_amber_tile"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.GOLDEN_AMBER_TILE_SLAB.get(), 6)
                .define('#', BlockRegistry.GOLDEN_AMBER_TILE.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.GOLDEN_AMBER_TILE.get()))
                .save(output, ModConstants.id("golden_amber_tile_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.GOLDEN_AMBER_TILE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.GOLDEN_AMBER_TILE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.GOLDEN_AMBER_TILE.get()))
                .save(output, ModConstants.id("golden_amber_tile_slab_from_golden_amber_tile_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.GOLDEN_AMBER_TILE_STAIRS.get(), 4)
                .define('#', BlockRegistry.GOLDEN_AMBER_TILE.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.GOLDEN_AMBER_TILE.get()))
                .save(output, ModConstants.id("golden_amber_tile_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.GOLDEN_AMBER_TILE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.GOLDEN_AMBER_TILE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.GOLDEN_AMBER_TILE.get()))
                .save(output, ModConstants.id("golden_amber_tile_stairs_from_golden_amber_tile_stonecutting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.GOLDEN_MOSSY_FLOESTONE.get(), 1)
                .requires(BlockRegistry.MOSSY_FLOESTONE.get())
                .requires(Items.GOLD_NUGGET)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("golden_mossy_floestone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.GREEN_CLOUD.get(), 1)
                .requires(BlockRegistry.GOLDEN_CLOUD.get())
                .requires(BlockRegistry.BLUE_CLOUD.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.GOLDEN_CLOUD.get()))
                .save(output, ModConstants.id("green_cloud"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GRINDSTONE, 1)
                .define('#', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .define('-', BlockRegistry.FLOESTONE_SLAB.get())
                .define('I', Items.STICK)
                .pattern("I-I")
                .pattern("# #")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks"))))
                .save(output, ModConstants.id("grindstone_floestone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHEESECAKE.get(), 1)
                .define('A', TagKey.create(Registries.ITEM, ResourceLocation.parse("c:buckets/milk")))
                .define('C', ItemRegistry.AMADRYS_BUSHEL.get())
                .define('P', ItemRegistry.POPOM_JELLY.get())
                .pattern("PPP")
                .pattern("AAA")
                .pattern("CCC")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:buckets/milk"))))
                .save(output, ModConstants.id("halflight_cheesecake"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.MISC, Items.GLASS, 0.2F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_glass"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HELIOLITH_SLAB.get(), 6)
                .define('#', BlockRegistry.HELIOLITH.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.HELIOLITH_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_slab_from_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HELIOLITH_STAIRS.get(), 4)
                .define('#', BlockRegistry.HELIOLITH.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.HELIOLITH_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_stairs_from_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HELIOLITH_WALL.get(), 6)
                .define('#', BlockRegistry.HELIOLITH.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.HELIOLITH_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("heliolith_wall_from_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.HOPPER, 1)
                .define('C', Items.CHEST)
                .define('I', ItemRegistry.OLVITE.get())
                .pattern("I I")
                .pattern("ICI")
                .pattern(" I ")
                .unlockedBy(HAS_INGREDIENT, has(Items.CHEST))
                .save(output, ModConstants.id("hopper_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.INCUBATOR.get(), 1)
                .define('#', BlockRegistry.THATCH_SET.block().get())
                .define('X', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .pattern(" # ")
                .pattern("X#X")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.THATCH_SET.block().get()))
                .save(output, ModConstants.id("incubator"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LAVENDER_WISTERIA_LEAF_PILE.get(), 3)
                .define('#', BlockRegistry.LAVENDER_WISTERIA_LEAVES.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get()))
                .save(output, ModConstants.id("lavender_wisteria_leaf_pile"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEVER, 1)
                .define('#', BlockRegistry.COBBLED_FLOESTONE.get())
                .define('X', Items.STICK)
                .pattern("X")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("lever_from_floestone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.LEVITA_ARROW.get(), 4)
                .define('A', ItemRegistry.LEVITA_SHARD.get())
                .define('I', Items.STICK)
                .define('F', Items.FEATHER)
                .pattern("A")
                .pattern("I")
                .pattern("F")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.LEVITA_SHARD.get()))
                .save(output, ModConstants.id("levita_arrow"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LEVITA_BRICK_SET.block().get(), 4)
                .define('#', BlockRegistry.LEVITA.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA.get()))
                .save(output, ModConstants.id("levita_brick"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LEVITA_BRICK_SET.slab().get(), 6)
                .define('#', BlockRegistry.LEVITA_BRICK_SET.block().get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.block().get()))
                .save(output, ModConstants.id("levita_brick_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.LEVITA_BRICK_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.LEVITA_BRICK_SET.slab().get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.block().get()))
                .save(output, ModConstants.id("levita_brick_slab_from_levita_brick_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LEVITA_BRICK_SET.stairs().get(), 4)
                .define('#', BlockRegistry.LEVITA_BRICK_SET.block().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.block().get()))
                .save(output, ModConstants.id("levita_brick_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.LEVITA_BRICK_SET.block().get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.LEVITA_BRICK_SET.stairs().get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_BRICK_SET.block().get()))
                .save(output, ModConstants.id("levita_brick_stairs_from_levita_brick_stonecutting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BlockRegistry.LEVITA_ORE.get()), RecipeCategory.MISC, ItemRegistry.LEVITA_GEM.get(), 1.2F, 100)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_ORE.get()))
                .save(output, ModConstants.id("levita_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.LEVITA_ORE.get()), RecipeCategory.MISC, ItemRegistry.LEVITA_GEM.get(), 1.2F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LEVITA_ORE.get()))
                .save(output, ModConstants.id("levita_from_smelting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.LEVITA_SHARD.get(), 4)
                .requires(ItemRegistry.LEVITA_GEM.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.LEVITA_GEM.get()))
                .save(output, ModConstants.id("levita_shard"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LEVITA_RAIL.get(), 1)
                .define('L', ItemRegistry.LEVITA_SHARD.get())
                .define('S', Items.ACTIVATOR_RAIL)
                .pattern("L")
                .pattern("S")
                .pattern("L")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.LEVITA_SHARD.get()))
                .save(output, ModConstants.id("levita_rail"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.LEVITA_WAND.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.LEVITA_GEM.get())
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("levita_wand"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LEVITATOR.get(), 1)
                .define('G', ItemRegistry.LEVITA_SHARD.get())
                .define('R', Items.REDSTONE)
                .define('S', BlockRegistry.FLOESTONE_BRICK.get())
                .pattern("SRS")
                .pattern("GGG")
                .pattern("SRS")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.LEVITA_GEM.get()))
                .save(output, ModConstants.id("levitator"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.LIVERWORT_CARPET.get(), 3)
                .define('B', BlockRegistry.LIVERWORT.get())
                .pattern("BB")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LIVERWORT.get()))
                .save(output, ModConstants.id("liverwort_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.MINECART, 1)
                .define('#', ItemRegistry.OLVITE.get())
                .pattern("# #")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("minecart_olvite"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE.get(), 1)
                .requires(BlockRegistry.COBBLED_FLOESTONE.get())
                .requires(Items.MOSS_BLOCK)
                .group("mossy_cobbled_floestone")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_from_moss_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE.get(), 1)
                .requires(BlockRegistry.COBBLED_FLOESTONE.get())
                .requires(Items.VINE)
                .group("mossy_cobbled_floestone")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.COBBLED_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_from_vine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_SLAB.get(), 6)
                .define('#', BlockRegistry.MOSSY_FLOESTONE.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_slab_from_mossy_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_STAIRS.get(), 4)
                .define('#', BlockRegistry.MOSSY_FLOESTONE.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_stairs_from_mossy_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOSSY_FLOESTONE_WALL.get(), 6)
                .define('#', BlockRegistry.MOSSY_FLOESTONE.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.MOSSY_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOSSY_FLOESTONE_WALL.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOSSY_FLOESTONE.get()))
                .save(output, ModConstants.id("mossy_floestone_wall_from_mossy_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.MOTTLED_AUREL_WOOD.get(), 3)
                .define('#', BlockRegistry.MOTTLED_AUREL_LOG.get())
                .pattern("##")
                .pattern("##")
                .group("bark")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.MOTTLED_AUREL_LOG.get()))
                .save(output, ModConstants.id("mottled_aurel_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.NEST.get(), 1)
                .define('#', BlockRegistry.THATCH_SET.block().get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.THATCH_SET.block().get()))
                .save(output, ModConstants.id("nest"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.NITRA_BULB.get(), 4)
                .requires(BlockRegistry.NITRA_BUNCH.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.NITRA_BUNCH.get()))
                .save(output, ModConstants.id("nitra"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.NITRA_BUNCH.get(), 1)
                .define('#', ItemRegistry.NITRA_BULB.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.NITRA_BULB.get()))
                .save(output, ModConstants.id("nitra_bunch"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.OLVITE.get(), 9)
                .requires(BlockRegistry.OLVITE_BLOCK.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.OLVITE_BLOCK.get()))
                .save(output, ModConstants.id("olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_AXE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XX")
                .pattern("X#")
                .pattern(" #")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_axe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BLAST_FURNACE, 1)
                .define('#', BlockRegistry.BURNISHED_STONE_SET.block().get())
                .define('I', ItemRegistry.OLVITE.get())
                .define('X', Items.FURNACE)
                .pattern("III")
                .pattern("IXI")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.BURNISHED_STONE_SET.block().get()))
                .save(output, ModConstants.id("olvite_blast_furnace"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.OLVITE_BLOCK.get(), 1)
                .define('#', ItemRegistry.OLVITE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_BLOODSTONE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern(" X")
                .pattern("# ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_bloodstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_BOOTS.get(), 1)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BUCKET, 1)
                .define('#', ItemRegistry.OLVITE.get())
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_bucket"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.OLVITE_CHAIN.get(), 1)
                .define('I', ItemRegistry.OLVITE.get())
                .define('N', ItemRegistry.OLVITE_NUGGET.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_chain"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_CHESTPLATE.get(), 1)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_chestplate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.OLVITE_NUGGET.get(), 9)
                .requires(ItemRegistry.OLVITE.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_fragment"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ItemRegistry.OLVITE_PICKAXE.get(), ItemRegistry.OLVITE_SHOVEL.get(), ItemRegistry.OLVITE_AXE.get(), ItemRegistry.OLVITE_HOE.get(), ItemRegistry.OLVITE_SWORD.get(), ItemRegistry.OLVITE_HELMET.get(), ItemRegistry.OLVITE_CHESTPLATE.get(), ItemRegistry.OLVITE_LEGGINGS.get(), ItemRegistry.OLVITE_BOOTS.get()), RecipeCategory.MISC, ItemRegistry.OLVITE_NUGGET.get(), 0.1F, 100)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE_PICKAXE.get()))
                .save(output, ModConstants.id("olvite_fragment_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemRegistry.OLVITE_PICKAXE.get(), ItemRegistry.OLVITE_SHOVEL.get(), ItemRegistry.OLVITE_AXE.get(), ItemRegistry.OLVITE_HOE.get(), ItemRegistry.OLVITE_SWORD.get(), ItemRegistry.OLVITE_HELMET.get(), ItemRegistry.OLVITE_CHESTPLATE.get(), ItemRegistry.OLVITE_LEGGINGS.get(), ItemRegistry.OLVITE_BOOTS.get()), RecipeCategory.MISC, ItemRegistry.OLVITE_NUGGET.get(), 0.1F, 200)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE_PICKAXE.get()))
                .save(output, ModConstants.id("olvite_fragment_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BlockRegistry.OLVITE_ORE.get()), RecipeCategory.MISC, ItemRegistry.OLVITE.get(), 0.6F, 100)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.OLVITE_ORE.get()))
                .save(output, ModConstants.id("olvite_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.OLVITE_ORE.get()), RecipeCategory.MISC, ItemRegistry.OLVITE.get(), 0.6F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.OLVITE_ORE.get()))
                .save(output, ModConstants.id("olvite_from_smelting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE.get(), 1)
                .define('#', ItemRegistry.OLVITE_NUGGET.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE_NUGGET.get()))
                .save(output, ModConstants.id("olvite_gemstone_from_fragments"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_HELMET.get(), 1)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_HOE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_hoe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_LEGGINGS.get(), 1)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_PICKAXE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_pickaxe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.OLVITE_PRESSURE_PLATE.get(), 1)
                .define('#', ItemRegistry.OLVITE.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("olvite_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_SHOVEL.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_shovel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_SPYGLASS.get(), 1)
                .define('A', Items.GLASS)
                .define('O', ItemRegistry.OLVITE.get())
                .pattern("A")
                .pattern("O")
                .pattern("O")
                .unlockedBy(HAS_INGREDIENT, has(Items.GLASS))
                .save(output, ModConstants.id("olvite_spyglass"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.OLVITE_SWORD.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("olvite_sword"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ORANGE_DYE, 1)
                .requires(BlockRegistry.DRIGEAN.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.DRIGEAN.get()))
                .save(output, ModConstants.id("orange_dye_from_drigean"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.PACKED_SWEDROOT.get(), 1)
                .define('#', ItemRegistry.SWEDROOT.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.SWEDROOT.get()))
                .save(output, ModConstants.id("packed_swedroot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PISTON, 1)
                .define('I', ItemRegistry.OLVITE.get())
                .define('O', BlockRegistry.COBBLED_FLOESTONE.get())
                .define('P', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .define('R', Items.REDSTONE)
                .pattern("PPP")
                .pattern("OIO")
                .pattern("ORO")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("piston_olvite"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE, 1)
                .requires(BlockRegistry.ATARAXIA.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.ATARAXIA.get()))
                .save(output, ModConstants.id("purple_dye_from_ataraxia"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.RAIL, 16)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.OLVITE.get())
                .pattern("X X")
                .pattern("X#X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("rail_olvite"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RAW_GOLD, 1)
                .requires(ItemRegistry.GOLDEN_AMBER.get())
                .requires(ItemRegistry.GOLDEN_AMBER.get())
                .requires(ItemRegistry.GOLDEN_AMBER.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.GOLDEN_AMBER.get()))
                .save(output, ModConstants.id("raw_gold_from_amber"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.REFINED_SURTRUM.get(), 9)
                .requires(BlockRegistry.REFINED_SURTRUM_BLOCK.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.REFINED_SURTRUM_BLOCK.get()))
                .save(output, ModConstants.id("refined_surtrum"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.REFINED_SURTRUM_BLOCK.get(), 1)
                .define('#', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.REFINED_SURTRUM.get()))
                .save(output, ModConstants.id("refined_surtrum_block"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlockRegistry.SURTRUM.get()), RecipeCategory.MISC, ItemRegistry.REFINED_SURTRUM.get(), 0.8F, 200)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SURTRUM.get()))
                .save(output, ModConstants.id("refined_surtrum_from_raw"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.REPEATER, 1)
                .define('#', Items.REDSTONE_TORCH)
                .define('I', BlockRegistry.FLOESTONE.get())
                .define('X', Items.REDSTONE)
                .pattern("#X#")
                .pattern("III")
                .unlockedBy(HAS_INGREDIENT, has(Items.REDSTONE_TORCH))
                .save(output, ModConstants.id("repeater_floestone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.ROOT_STEW.get(), 1)
                .requires(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/raw_meat")))
                .requires(ItemRegistry.SWEDROOT_PULP.get())
                .requires(Items.CARROT)
                .requires(Items.POTATO)
                .requires(Items.BOWL)
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/raw_meat"))))
                .save(output, ModConstants.id("root_stew"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.ROSE_WISTERIA_LEAF_PILE.get(), 3)
                .define('#', BlockRegistry.ROSE_WISTERIA_LEAVES.get())
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.ROSE_WISTERIA_LEAVES.get()))
                .save(output, ModConstants.id("rose_wisteria_leaf_pile"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SHEARS, 1)
                .define('#', ItemRegistry.OLVITE.get())
                .pattern(" #")
                .pattern("# ")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("shears_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SHIELD, 1)
                .define('W', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .define('o', ItemRegistry.OLVITE.get())
                .pattern("WoW")
                .pattern("WWW")
                .pattern(" W ")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks"))))
                .save(output, ModConstants.id("shield_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SMITHING_TABLE, 1)
                .define('#', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .define('@', ItemRegistry.OLVITE.get())
                .pattern("@@")
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks"))))
                .save(output, ModConstants.id("smithing_table_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_FLOESTONE.get(), 9)
                .define('#', BlockRegistry.FLOESTONE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_FLOESTONE.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_from_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_FLOESTONE_SLAB.get(), 6)
                .define('#', BlockRegistry.SMOOTH_FLOESTONE.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_FLOESTONE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_slab_from_floestone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.SMOOTH_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_FLOESTONE_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_slab_from_smooth_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_FLOESTONE_STAIRS.get(), 4)
                .define('#', BlockRegistry.SMOOTH_FLOESTONE.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_FLOESTONE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_stairs_from_floestone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.SMOOTH_FLOESTONE.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_FLOESTONE_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_FLOESTONE.get()))
                .save(output, ModConstants.id("smooth_floestone_stairs_from_smooth_floestone_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HELIOLITH.get(), 4)
                .define('#', BlockRegistry.HELIOLITH.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_HELIOLITH.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_from_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HELIOLITH_SLAB.get(), 6)
                .define('#', BlockRegistry.SMOOTH_HELIOLITH.get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_slab"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_HELIOLITH_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_slab_from_heliolith_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.SMOOTH_HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_HELIOLITH_SLAB.get(), 2)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_slab_from_smooth_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HELIOLITH_STAIRS.get(), 4)
                .define('#', BlockRegistry.SMOOTH_HELIOLITH.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_stairs"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_HELIOLITH_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_stairs_from_heliolith_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BlockRegistry.SMOOTH_HELIOLITH.get()), RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SMOOTH_HELIOLITH_STAIRS.get(), 1)
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.SMOOTH_HELIOLITH.get()))
                .save(output, ModConstants.id("smooth_heliolith_stairs_from_smooth_heliolith_stonecutting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICKY_PISTON, 1)
                .define('#', ItemRegistry.POPOM_JELLY.get())
                .define('P', Items.PISTON)
                .pattern("#")
                .pattern("P")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.POPOM_JELLY.get()))
                .save(output, ModConstants.id("sticky_piston_from_popom_jelly"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STONECUTTER, 1)
                .define('#', BlockRegistry.FLOESTONE.get())
                .define('I', ItemRegistry.OLVITE.get())
                .pattern(" I ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.FLOESTONE.get()))
                .save(output, ModConstants.id("stonecutter_olvite"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemRegistry.SWEDROOT_PULP.get()), RecipeCategory.MISC, Items.SUGAR, 0.1F, 200)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.SWEDROOT_PULP.get()))
                .save(output, ModConstants.id("sugar_from_swedroot_smelting"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ItemRegistry.SWEDROOT_PULP.get()), RecipeCategory.MISC, Items.SUGAR, 0.1F, 100)
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.SWEDROOT_PULP.get()))
                .save(output, ModConstants.id("sugar_from_swedroot_smoking"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SURTRUM.get(), 1)
                .define('#', ItemRegistry.RAW_SURTRUM.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.RAW_SURTRUM.get()))
                .save(output, ModConstants.id("surtrum"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_AXE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("XX")
                .pattern("X#")
                .pattern(" #")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_axe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_BLOODSTONE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern(" X")
                .pattern("# ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_bloodstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_BOOTS.get(), 1)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.REFINED_SURTRUM.get()))
                .save(output, ModConstants.id("surtrum_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_CHESTPLATE.get(), 1)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.REFINED_SURTRUM.get()))
                .save(output, ModConstants.id("surtrum_chestplate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_HELMET.get(), 1)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.REFINED_SURTRUM.get()))
                .save(output, ModConstants.id("surtrum_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_HOE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_hoe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_LEGGINGS.get(), 1)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.REFINED_SURTRUM.get()))
                .save(output, ModConstants.id("surtrum_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_PICKAXE.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_pickaxe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_SHOVEL.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_shovel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.SURTRUM_SWORD.get(), 1)
                .define('#', Items.STICK)
                .define('X', ItemRegistry.REFINED_SURTRUM.get())
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("surtrum_sword"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.SWEDROOT_PULP.get(), 2)
                .requires(ItemRegistry.SWEDROOT.get())
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.SWEDROOT.get()))
                .save(output, ModConstants.id("swedroot_pulp"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.THATCH_SET.block().get(), 4)
                .define('#', Items.STICK)
                .define('X', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:leaves")))
                .pattern("#X")
                .pattern("X#")
                .group("boat")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("thatch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.THATCH_SET.block().get(), 4)
                .define('#', Items.STICK)
                .define('X', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:leaves")))
                .pattern("X#")
                .pattern("#X")
                .group("boat")
                .unlockedBy(HAS_INGREDIENT, has(Items.STICK))
                .save(output, ModConstants.id("thatch_mirrored"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.THATCH_SET.slab().get(), 6)
                .define('#', BlockRegistry.THATCH_SET.block().get())
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.THATCH_SET.block().get()))
                .save(output, ModConstants.id("thatch_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.THATCH_SET.stairs().get(), 4)
                .define('#', BlockRegistry.THATCH_SET.block().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.THATCH_SET.block().get()))
                .save(output, ModConstants.id("thatch_stairs"));

        output.accept(
                ModConstants.id("tree_tap/dragon_breath"),
                new TreeTapRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Blocks.DRAGON_HEAD,
                        Blocks.DRAGON_HEAD,
                        new ItemStack(Items.DRAGON_BREATH, 1),
                        Optional.empty(),
                        8),
                null);

        output.accept(
                ModConstants.id("tree_tap/honey"),
                new TreeTapRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Blocks.BEE_NEST,
                        Blocks.BEE_NEST,
                        new ItemStack(Items.HONEY_BOTTLE, 1),
                        Optional.empty(),
                        4),
                null);

        output.accept(
                ModConstants.id("tree_tap/regeneration_potion"),
                new TreeTapRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Blocks.CRYING_OBSIDIAN,
                        Blocks.OBSIDIAN,
                        new ItemStack(Items.POTION, 1),
                        Optional.empty(),
                        2),
                null);

        output.accept(
                ModConstants.id("tree_tap/stew"),
                new TreeTapRecipe(
                        Ingredient.of(Items.BOWL),
                        Blocks.MUSHROOM_STEM,
                        Blocks.MUSHROOM_STEM,
                        new ItemStack(Items.MUSHROOM_STEW, 1),
                        Optional.empty(),
                        3),
                null);

        var wisteriaLog = BlockRegistry.WISTERIA_WOODSTUFF.log().get();
        output.accept(
                ModConstants.id("tree_tap/water"),
                new TreeTapRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        wisteriaLog,
                        wisteriaLog,
                        new ItemStack(Items.POTION, 1),
                        Optional.empty(),
                        1),
                null);

        output.accept(
                ModConstants.id("tree_tap/water_aurel_bucket"),
                new TreeTapRecipe(
                        Ingredient.of(ItemRegistry.AUREL_BUCKET.get()),
                        wisteriaLog,
                        wisteriaLog,
                        new ItemStack(ItemRegistry.AUREL_WATER_BUCKET.get(), 1),
                        Optional.empty(),
                        2),
                null);

        output.accept(
                ModConstants.id("tree_tap/water_bucket"),
                new TreeTapRecipe(
                        Ingredient.of(Items.BUCKET),
                        wisteriaLog,
                        wisteriaLog,
                        new ItemStack(Items.WATER_BUCKET, 1),
                        Optional.empty(),
                        2),
                null);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.TREE_TAP.get(), 1)
                .define('P', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .pattern("PPP")
                .pattern("  P")
                .unlockedBy(HAS_INGREDIENT, has(TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks"))))
                .save(output, ModConstants.id("tree_tap"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TRIPWIRE_HOOK, 2)
                .define('O', ItemRegistry.OLVITE.get())
                .define('P', TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:planks")))
                .define('S', Items.STICK)
                .pattern("O")
                .pattern("S")
                .pattern("P")
                .unlockedBy(HAS_INGREDIENT, has(ItemRegistry.OLVITE.get()))
                .save(output, ModConstants.id("tripwire_hook_olvite"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.WARDED_JAR.get(), 1)
                .define('#', Items.TINTED_GLASS)
                .pattern(" # ")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(HAS_INGREDIENT, has(Items.TINTED_GLASS))
                .save(output, ModConstants.id("warded_jar"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_DYE, 1)
                .requires(BlockRegistry.ANCIENT_FLOWER.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.ANCIENT_FLOWER.get()))
                .save(output, ModConstants.id("white_dye_from_ancient_flower"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_DYE, 1)
                .requires(BlockRegistry.CLOUDSBLUFF.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.CLOUDSBLUFF.get()))
                .save(output, ModConstants.id("white_dye_from_cloudsbluff"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.YELLOW_DYE, 1)
                .requires(BlockRegistry.LUMINAR.get())
                .unlockedBy(HAS_INGREDIENT, has(BlockRegistry.LUMINAR.get()))
                .save(output, ModConstants.id("yellow_dye_from_luminar"));

    }
}
