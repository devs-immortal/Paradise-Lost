package net.id.paradiselost.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import static net.id.paradiselost.items.ParadiseLostItems.*;
import static net.id.paradiselost.items.ParadiseLostItems.BURNISHED_STONE_WALL;

public class ParadiseLostItemGroups {

    public static final RegistryKey<ItemGroup> PARADISE_BLOCKS = create("building_blocks", FabricItemGroup.builder()
            .icon(() -> new ItemStack(ParadiseLostBlocks.CHISELED_FLOESTONE))
            .entries((context, entries) -> {
                // Woodstuff
                entries.add(AUREL_LOG);
                entries.add(MOTTLED_AUREL_LOG);
                entries.add(MOTTLED_AUREL_WOOD);
                entries.add(AUREL_WOOD);
                entries.add(STRIPPED_AUREL_LOG);
                entries.add(STRIPPED_AUREL_WOOD);
                entries.add(MOTTLED_AUREL_FALLEN_LOG);
                entries.add(AUREL_PLANKS);
                entries.add(AUREL_STAIRS);
                entries.add(AUREL_SLAB);
                entries.add(AUREL_FENCE);
                entries.add(AUREL_FENCE_GATE);
                entries.add(AUREL_DOOR);
                entries.add(AUREL_TRAPDOOR);
                entries.add(AUREL_PRESSURE_PLATE);
                entries.add(AUREL_BUTTON);
                entries.add(MOTHER_AUREL_LOG);
                entries.add(MOTHER_AUREL_WOOD);
                entries.add(STRIPPED_MOTHER_AUREL_LOG);
                entries.add(STRIPPED_MOTHER_AUREL_WOOD);
                entries.add(MOTHER_AUREL_PLANKS);
                entries.add(MOTHER_AUREL_STAIRS);
                entries.add(MOTHER_AUREL_SLAB);
                entries.add(MOTHER_AUREL_FENCE);
                entries.add(MOTHER_AUREL_FENCE_GATE);
                entries.add(MOTHER_AUREL_DOOR);
                entries.add(MOTHER_AUREL_TRAPDOOR);
                entries.add(MOTHER_AUREL_PRESSURE_PLATE);
                entries.add(MOTHER_AUREL_BUTTON);
                entries.add(MENTH_LOG);
                entries.add(MENTH_WOOD);
                entries.add(STRIPPED_MENTH_LOG);
                entries.add(STRIPPED_MENTH_WOOD);
                entries.add(MENTH_PLANKS);
                entries.add(MENTH_STAIRS);
                entries.add(MENTH_SLAB);
                entries.add(MENTH_FENCE);
                entries.add(MENTH_FENCE_GATE);
                entries.add(MENTH_DOOR);
                entries.add(MENTH_TRAPDOOR);
                entries.add(MENTH_PRESSURE_PLATE);
                entries.add(MENTH_BUTTON);
                entries.add(WISTERIA_LOG);
                entries.add(WISTERIA_WOOD);
                entries.add(STRIPPED_WISTERIA_LOG);
                entries.add(STRIPPED_WISTERIA_WOOD);
                entries.add(WISTERIA_PLANKS);
                entries.add(WISTERIA_STAIRS);
                entries.add(WISTERIA_SLAB);
                entries.add(WISTERIA_FENCE);
                entries.add(WISTERIA_FENCE_GATE);
                entries.add(WISTERIA_DOOR);
                entries.add(WISTERIA_TRAPDOOR);
                entries.add(WISTERIA_PRESSURE_PLATE);
                entries.add(WISTERIA_BUTTON);
                entries.add(THATCH_BLOCK);
                entries.add(THATCH_STAIRS);
                entries.add(THATCH_SLAB);
                // Stone / stonelike
                entries.add(FLOESTONE);
                entries.add(FLOESTONE_STAIRS);
                entries.add(FLOESTONE_SLAB);
                entries.add(FLOESTONE_WALL);
                entries.add(FLOESTONE_PRESSURE_PLATE);
                entries.add(FLOESTONE_BUTTON);
                entries.add(COBBLED_FLOESTONE);
                entries.add(COBBLED_FLOESTONE_STAIRS);
                entries.add(COBBLED_FLOESTONE_SLAB);
                entries.add(COBBLED_FLOESTONE_WALL);
                entries.add(MOSSY_FLOESTONE);
                entries.add(MOSSY_FLOESTONE_STAIRS);
                entries.add(MOSSY_FLOESTONE_SLAB);
                entries.add(MOSSY_FLOESTONE_WALL);
                entries.add(GOLDEN_MOSSY_FLOESTONE);
                entries.add(FLOESTONE_BRICK);
                entries.add(FLOESTONE_BRICK_STAIRS);
                entries.add(FLOESTONE_BRICK_SLAB);
                entries.add(FLOESTONE_BRICK_WALL);
                entries.add(SMOOTH_FLOESTONE);
                entries.add(SMOOTH_FLOESTONE_STAIRS);
                entries.add(SMOOTH_FLOESTONE_SLAB);
                entries.add(CHISELED_FLOESTONE);
                entries.add(HELIOLITH);
                entries.add(HELIOLITH_STAIRS);
                entries.add(HELIOLITH_SLAB);
                entries.add(HELIOLITH_WALL);
                entries.add(SMOOTH_HELIOLITH);
                entries.add(SMOOTH_HELIOLITH_STAIRS);
                entries.add(SMOOTH_HELIOLITH_SLAB);
                entries.add(LEVITA_BRICK);
                entries.add(LEVITA_BRICK_STAIRS);
                entries.add(LEVITA_BRICK_SLAB);
                entries.add(CHISELED_LEVITA_BRICK);
                entries.add(BURNISHED_STONE);
                entries.add(BURNISHED_STONE_STAIRS);
                entries.add(BURNISHED_STONE_SLAB);
                entries.add(BURNISHED_STONE_WALL);
                entries.add(BURNISHED_STONE_PLAQUE);
                entries.add(BURNISHED_STONE_SCRIPT);
                entries.add(GOLDEN_AMBER_TILE);
                entries.add(GOLDEN_AMBER_TILE_STAIRS);
                entries.add(GOLDEN_AMBER_TILE_SLAB);
                entries.add(CALCITE_TILES);
                entries.add(CALCITE_TILES_STAIRS);
                entries.add(CALCITE_TILES_SLAB);
                entries.add(CALCITE_TILES_WALL);
                entries.add(BLOOMED_CALCITE_TILES);
                entries.add(BLOOMED_CALCITE_TILES_STAIRS);
                entries.add(BLOOMED_CALCITE_TILES_SLAB);
                entries.add(BLOOMED_CALCITE_TILES_WALL);
                // overworld hybrid
                entries.add(BLOOMED_CALCITE);
                // ores
                entries.add(CHERINE_ORE);
                entries.add(OLVITE_ORE);
                entries.add(FLOESTONE_REDSTONE_ORE);
                entries.add(SURTRUM);
                entries.add(METAMORPHIC_SHELL);
                entries.add(LEVITA_ORE);
                entries.add(CHERINE_BLOCK);
                entries.add(OLVITE_BLOCK);
                entries.add(OLVITE_CHAIN);
                entries.add(REFINED_SURTRUM_BLOCK);

            }));
    public static final RegistryKey<ItemGroup> PARADISE_PLANTS = create("plants", FabricItemGroup.builder()
            .icon(() -> new ItemStack(ParadiseLostBlocks.HIGHLANDS_GRASS))
            .entries((context, entries) -> {
                // soil
                entries.add(HIGHLANDS_GRASS);
                entries.add(DIRT_PATH);
                entries.add(DIRT);
                entries.add(FARMLAND);
                entries.add(COARSE_DIRT);
                entries.add(FROZEN_GRASS);
                entries.add(PERMAFROST);
                entries.add(PERMAFROST_PATH);
                entries.add(LIVERWORT);
                entries.add(LIVERWORT_CARPET);
                entries.add(LEVITA);
                entries.add(THATCH_BLOCK);
                entries.add(THATCH_STAIRS);
                entries.add(THATCH_SLAB);
                // "packed" blocks
                entries.add(PACKED_SWEDROOT);
                entries.add(AMADRYS_BUNDLE);
                entries.add(NITRA_BUNCH);
                // tree foliage
                entries.add(AUREL_SAPLING);
                entries.add(AUREL_LEAVES);
                entries.add(AUREL_LEAF_PILE);
                entries.add(MOTHER_AUREL_SAPLING);
                entries.add(MOTHER_AUREL_LEAVES);
                entries.add(MENTH_SAPLING);
                entries.add(MENTH_LEAVES);
                entries.add(ROSE_WISTERIA_SAPLING);
                entries.add(ROSE_WISTERIA_LEAVES);
                entries.add(ROSE_WISTERIA_HANGER);
                entries.add(ROSE_WISTERIA_LEAF_PILE);
                entries.add(FROST_WISTERIA_SAPLING);
                entries.add(FROST_WISTERIA_LEAVES);
                entries.add(FROST_WISTERIA_HANGER);
                entries.add(FROST_WISTERIA_LEAF_PILE);
                entries.add(LAVENDER_WISTERIA_SAPLING);
                entries.add(LAVENDER_WISTERIA_LEAVES);
                entries.add(LAVENDER_WISTERIA_HANGER);
                entries.add(LAVENDER_WISTERIA_LEAF_PILE);
                // grasses
                entries.add(GRASS);
                entries.add(TALL_GRASS);
                entries.add(SHORT_GRASS);
                entries.add(GRASS_FLOWERING);
                entries.add(FERN);
                entries.add(BUSH);
                entries.add(SHAMROCK);
                entries.add(MALT_SPRIG);
                // flowers
                entries.add(ATARAXIA);
                entries.add(CLOUDSBLUFF);
                entries.add(DRIGEAN);
                entries.add(LUMINAR);
                entries.add(ANCIENT_FLOWER);
                entries.add(WILD_FLAX);
                entries.add(HONEY_NETTLE);
                // fungi
                entries.add(ROOTCAP);
                entries.add(BROWN_SPORECAP);
                entries.add(PINK_SPORECAP);
                entries.add(ROOTCAP_BLOCK);
                entries.add(BROWN_SPORECAP_BLOCK);
                entries.add(PINK_SPORECAP_BLOCK);
                // plants
                entries.add(BLACKCURRANT);
                entries.add(AMADRYS_BUSHEL);
                entries.add(FLAXSEED);
                entries.add(NITRA_SEED);
                entries.add(NITRA_BULB);
                entries.add(SWEDROOT);

            }));
    public static final RegistryKey<ItemGroup> PARADISE_DECO = create("decoration", FabricItemGroup.builder()
            .icon(() -> new ItemStack(ParadiseLostBlocks.CHERINE_LANTERN))
            .entries((context, entries) -> {
                // clouds
                entries.add(COLD_CLOUD);
                entries.add(BLUE_CLOUD);
                entries.add(GOLDEN_CLOUD);
                entries.add(GREEN_CLOUD);
                // misc
                entries.add(CHERINE_TORCH);
                entries.add(CHERINE_LANTERN);
                entries.add(OLVITE_CHAIN);
                entries.add(CALCITE_FLOWER_POT);
                entries.add(CALCITE_DECORATED_POT);
                entries.add(CHERINE_CAMPFIRE);
                entries.add(AUREL_BOOKSHELF);
                entries.add(FLAXWEAVE_CUSHION);
                entries.add(FLAXWEAVE_CUSHION_SLAB);
                entries.add(GOLDEN_AMBER_BARS);
                entries.add(AMADRYS_BUNDLE);
                entries.add(SUSPICIOUS_DIRT);
                entries.add(NITRA_BUNCH);
                entries.add(LEVITATOR);
                entries.add(LEVITA_RAIL);
                entries.add(OLVITE_PRESSURE_PLATE);
                entries.add(INCUBATOR);
                entries.add(NEST);
                entries.add(FOOD_BOWL);
                entries.add(TREE_TAP);
                entries.add(AUREL_SIGN);
                entries.add(AUREL_HANGING_SIGN);
                entries.add(MOTHER_AUREL_SIGN);
                entries.add(MOTHER_AUREL_HANGING_SIGN);
                entries.add(MENTH_SIGN);
                entries.add(MENTH_HANGING_SIGN);
                entries.add(WISTERIA_SIGN);
                entries.add(WISTERIA_HANGING_SIGN);

            }));
    public static final RegistryKey<ItemGroup> PARADISE_EQUIPMENT = create("equipment", FabricItemGroup.builder()
            .icon(() -> new ItemStack(SURTRUM_PICKAXE))
            .entries((context, entries) -> {
                // actual tools
                entries.add(OLVITE_SWORD);
                entries.add(OLVITE_SHOVEL);
                entries.add(OLVITE_PICKAXE);
                entries.add(OLVITE_AXE);
                entries.add(OLVITE_HOE);
                entries.add(SURTRUM_SWORD);
                entries.add(SURTRUM_SHOVEL);
                entries.add(SURTRUM_PICKAXE);
                entries.add(SURTRUM_AXE);
                entries.add(SURTRUM_HOE);
                entries.add(GLAZED_GOLD_SWORD);
                entries.add(GLAZED_GOLD_SHOVEL);
                entries.add(GLAZED_GOLD_PICKAXE);
                entries.add(GLAZED_GOLD_AXE);
                entries.add(GLAZED_GOLD_HOE);
                entries.add(SOUL_BLADE);
                // misc
                entries.add(OLVITE_SPYGLASS);
                entries.add(TOTEM_OF_LEVITATION);
                // wands
                entries.add(LEVITA_WAND);
                entries.add(CHERINE_BLOODSTONE);
                entries.add(OLVITE_BLOODSTONE);
                entries.add(SURTRUM_BLOODSTONE);
                // jars
                entries.add(WARDED_JAR);
                entries.add(WARDED_JAR_ALLAY);
                entries.add(WARDED_JAR_QUINT);
                // misc
                entries.add(AUREL_BUCKET);
                entries.add(AUREL_WATER_BUCKET);
                entries.add(AUREL_POWDER_SNOW_BUCKET);
                entries.add(AUREL_MILK_BUCKET);
                entries.add(NITRA_BULB);
                // armor
                entries.add(OLVITE_HELMET);
                entries.add(OLVITE_HELMET_ORNATE);
                entries.add(OLVITE_CHESTPLATE);
                entries.add(OLVITE_LEGGINGS);
                entries.add(OLVITE_BOOTS);
                entries.add(SURTRUM_HELMET);
                entries.add(SURTRUM_CHESTPLATE);
                entries.add(SURTRUM_LEGGINGS);
                entries.add(SURTRUM_BOOTS);
                entries.add(GLAZED_GOLD_HELMET);
                entries.add(GLAZED_GOLD_CHESTPLATE);
                entries.add(GLAZED_GOLD_LEGGINGS);
                entries.add(GLAZED_GOLD_BOOTS);
                entries.add(XP_CIRCLET);
                // upgrades
                entries.add(GLAZED_GOLD_UPGRADE);
                // travel
                entries.add(AUREL_BOATS.boat());
                entries.add(AUREL_BOATS.chestBoat());
                entries.add(MOTHER_AUREL_BOATS.boat());
                entries.add(MOTHER_AUREL_BOATS.chestBoat());
                entries.add(MENTH_BOATS.boat());
                entries.add(MENTH_BOATS.chestBoat());
                entries.add(WISTERIA_BOATS.boat());
                entries.add(WISTERIA_BOATS.chestBoat());
            }));
    public static final RegistryKey<ItemGroup> PARADISE_RESOURCES = create("resources", FabricItemGroup.builder()
            .icon(() -> new ItemStack(CHERINE))
            .entries((context, entries) -> {
                //
                entries.add(CHERINE);
                entries.add(OLVITE);
                entries.add(OLVITE_NUGGET);
                entries.add(RAW_SURTRUM);
                entries.add(REFINED_SURTRUM);
                entries.add(LEVITA_GEM);
                entries.add(GOLDEN_AMBER);
                entries.add(FLAX_THREAD);
                entries.add(FLAXWEAVE);
                entries.add(SOL_POTTERY_SHERD);
                entries.add(COO_POTTERY_SHERD);
            }));
    public static final RegistryKey<ItemGroup> PARADISE_FOOD = create("food", FabricItemGroup.builder()
            .icon(() -> new ItemStack(AMADRYS_NOODLES))
            .entries((context, entries) -> {
                // all
                entries.add(BLACKCURRANT);
                entries.add(AMADRYS_BUSHEL);
                entries.add(AMADRYS_BREAD);
                entries.add(AMADRYS_BREAD_GLAZED);
                entries.add(AMADRYS_BREAD_GLAZED_FILLED);
                entries.add(POPOM_JELLY);
                entries.add(BLACKCURRANT_PIE);
                entries.add(BLACKCURRANT_COOKIE);
                entries.add(AMADRYS_NOODLES);
                entries.add(ROOT_STEW);
                entries.add(SWEDROOT);
                entries.add(SWEDROOT_PULP);
                // meat
                entries.add(MOA_MEAT);
                entries.add(COOKED_MOA_MEAT);
                // loot
                entries.add(CHEESECAKE);

            }));

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
                .register((itemGroup) -> {
                    itemGroup.add(MOA_SPAWN_EGG);
                    itemGroup.add(POPOM_SPAWN_EGG);
                    itemGroup.add(QUINT_SPAWN_EGG);
                    itemGroup.add(ENVOY_SPAWN_EGG);
                    itemGroup.add(SENTINEL_SPAWN_EGG);
                });
    }

    // item group registry helper
    private static RegistryKey<ItemGroup> create(String identifier, ItemGroup.Builder itemGroup) {
        var key = RegistryKey.of(RegistryKeys.ITEM_GROUP, ParadiseLost.locate(identifier));
        Registry.register(Registries.ITEM_GROUP, key, itemGroup.displayName(Text.translatable("itemGroup.paradise_lost." + identifier)).build());
        return key;
    }
}
