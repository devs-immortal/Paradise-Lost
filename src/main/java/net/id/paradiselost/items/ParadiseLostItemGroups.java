package net.id.paradiselost.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;

import static net.id.paradiselost.items.ParadiseLostItems.*;

public class ParadiseLostItemGroups {

    private static final ItemGroup PARADISE_BLOCKS = FabricItemGroup.builder(ParadiseLost.locate("building_blocks"))
            .icon(() -> new ItemStack(ParadiseLostBlocks.CHISELED_FLOESTONE))
            .entries((context, entries) -> {
                // Woodstuff
                entries.add(AUREL_LOG);
                entries.add(MOTTLED_AUREL_LOG);
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
                entries.add(ORANGE_LOG);
                entries.add(ORANGE_WOOD);
                entries.add(STRIPPED_ORANGE_LOG);
                entries.add(STRIPPED_ORANGE_WOOD);
                entries.add(ORANGE_PLANKS);
                entries.add(ORANGE_STAIRS);
                entries.add(ORANGE_SLAB);
                entries.add(ORANGE_FENCE);
                entries.add(ORANGE_FENCE_GATE);
                entries.add(ORANGE_DOOR);
                entries.add(ORANGE_TRAPDOOR);
                entries.add(ORANGE_PRESSURE_PLATE);
                entries.add(ORANGE_BUTTON);
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
                entries.add(GOLDEN_AMBER_TILE);
                entries.add(GOLDEN_AMBER_TILE_STAIRS);
                entries.add(GOLDEN_AMBER_TILE_SLAB);
                // overworld hybrid
                entries.add(BLOOMED_CALCITE);
                // ores
                entries.add(CHERINE_ORE);
                entries.add(OLVITE_ORE);
                entries.add(SURTRUM);
                entries.add(METAMORPHIC_SHELL);
                entries.add(LEVITA_ORE);
                entries.add(CHERINE_BLOCK);
                entries.add(OLVITE_BLOCK);
                entries.add(OLVITE_CHAIN);
                entries.add(REFINED_SURTRUM_BLOCK);

            }).build();

    private static final ItemGroup PARADISE_PLANTS = FabricItemGroup.builder(ParadiseLost.locate("plants"))
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
                entries.add(ORANGE_SAPLING);
                entries.add(ORANGE_LEAVES);
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
                // plants
                entries.add(BLACKCURRANT);
                entries.add(AMADRYS_BUSHEL);
                entries.add(FLAXSEED);
                entries.add(NITRA_SEED);
                entries.add(NITRA_BULB);
                entries.add(SWEDROOT);
                entries.add(SWEDROOT_SPREAD);

            }).build();

    private static final ItemGroup PARADISE_DECO = FabricItemGroup.builder(ParadiseLost.locate("decoration"))
            .icon(() -> new ItemStack(ParadiseLostBlocks.CHERINE_LANTERN))
            .entries((context, entries) -> {
                // clouds
                entries.add(COLD_CLOUD);
                entries.add(BLUE_CLOUD);
                entries.add(GOLDEN_CLOUD);
                // misc
                entries.add(CHERINE_TORCH);
                entries.add(CHERINE_LANTERN);
                entries.add(OLVITE_CHAIN);
                entries.add(CHERINE_CAMPFIRE);
                entries.add(AUREL_BOOKSHELF);
                entries.add(FLAXWEAVE_CUSHION);
                entries.add(AMADRYS_BUNDLE);
                entries.add(NITRA_BUNCH);
                entries.add(LEVITATOR);
                entries.add(INCUBATOR);
                entries.add(FOOD_BOWL);
                entries.add(TREE_TAP);
                entries.add(AUREL_SIGN);
                entries.add(MOTHER_AUREL_SIGN);
                entries.add(ORANGE_SIGN);
                entries.add(WISTERIA_SIGN);

            }).build();

    private static final ItemGroup PARADISE_EQUIPMENT = FabricItemGroup.builder(ParadiseLost.locate("equipment"))
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
                // wands
                entries.add(LEVITA_WAND);
                entries.add(CHERINE_BLOODSTONE);
                entries.add(OLVITE_BLOODSTONE);
                entries.add(SURTRUM_BLOODSTONE);
                entries.add(ABSTENTINE_BLOODSTONE);
                // misc
                entries.add(AUREL_BUCKET);
                entries.add(AUREL_WATER_BUCKET);
                entries.add(AUREL_MILK_BUCKET);
                entries.add(NITRA_BULB);
                // armor
                entries.add(OLVITE_HELMET);
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
                // upgrades
                entries.add(GLAZED_GOLD_UPGRADE);
                // travel
                entries.add(AUREL_BOATS.boat());
                entries.add(AUREL_BOATS.chestBoat());
                entries.add(MOTHER_AUREL_BOATS.boat());
                entries.add(MOTHER_AUREL_BOATS.chestBoat());
                entries.add(ORANGE_BOATS.boat());
                entries.add(ORANGE_BOATS.chestBoat());
                entries.add(WISTERIA_BOATS.boat());
                entries.add(WISTERIA_BOATS.chestBoat());
                entries.add(CLOUD_PARACHUTE);
                entries.add(GOLDEN_CLOUD_PARACHUTE);
            }).build();

    private static final ItemGroup PARADISE_RESOURCES = FabricItemGroup.builder(ParadiseLost.locate("resources"))
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
            }).build();

    private static final ItemGroup PARADISE_FOOD = FabricItemGroup.builder(ParadiseLost.locate("food"))
            .icon(() -> new ItemStack(AMADRYS_NOODLES))
            .entries((context, entries) -> {
                // all
                entries.add(BLACKCURRANT);
                entries.add(ORANGE);
                entries.add(AMADRYS_BUSHEL);
                entries.add(AMADRYS_BREAD);
                entries.add(AMADRYS_BREAD_GLAZED);
                entries.add(AMADRYS_NOODLES);
                entries.add(SWEDROOT);
                entries.add(SWEDROOT_PULP);
                // meat
                entries.add(MOA_MEAT);
                entries.add(COOKED_MOA_MEAT);
                // loot
                //entries.add(CHEESECAKE); // TODO

            }).build();

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
            .register((itemGroup) -> {
                itemGroup.add(MOA_SPAWN_EGG);
                itemGroup.add(ENVOY_SPAWN_EGG);
            });
    }
}
