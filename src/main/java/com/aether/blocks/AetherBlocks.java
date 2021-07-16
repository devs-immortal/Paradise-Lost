package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.aercloud.*;
import com.aether.blocks.decorative.*;
import com.aether.blocks.natural.*;
import com.aether.client.rendering.block.FluidRenderSetup;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.RenderUtils;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import com.aether.world.feature.tree.*;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AetherBlocks {
    public static final Block AEROGEL;
    public static final Block AETHER_DIRT;
    public static final Block AETHER_ENCHANTED_GRASS;
    public static final Block AETHER_FARMLAND;
    public static final Block AETHER_GRASS_BLOCK;
    public static final Block AETHER_DIRT_PATH;
    public static final Block AMBROSIUM_ORE;
    public static final Block AMBROSIUM_TORCH;
    public static final Block AMBROSIUM_TORCH_WALL;
    public static final Block ANGELIC_SLAB;
    public static final Block ANGELIC_STAIRS;
    public static final Block ANGELIC_STONE;
    public static final Block ANGELIC_CRACKED_STONE;
//    public static final Block ANGELIC_STONE_TRAP;
    public static final Block ANGELIC_WALL;
//    public static final Block BLACK_DYED_AERCLOUD;
    public static final Block BLUE_AERCLOUD;
//    public static final Block BLUE_DYED_AERCLOUD;
    public static final Block BLUE_PORTAL;
//    public static final Block BROWN_DYED_AERCLOUD;
    public static final Block CARVED_SLAB;
    public static final Block CARVED_STAIRS;
    public static final Block CARVED_STONE;
    public static final Block CARVED_STONE_TRAP;
    public static final Block CARVED_WALL;
//    public static final Block CHEST_MIMIC;
    public static final Block COLD_AERCLOUD;
//    public static final Block CYAN_DYED_AERCLOUD;
//    public static final Block DECORATED_HOLIDAY_LEAVES;
//    public static final Block ENCHANTED_GRAVITITE;
//    public static final Block ENCHANTER;
//    public static final Block FREEZER;
    public static final Block FLUTEGRASS;
    public static final Block GOLDEN_AERCLOUD;
    public static final Block GRAVITITE_ORE;
    public static final Block GRAVITITE_LEVITATOR;
    public static final Block BLOCK_OF_GRAVITITE;
//    public static final Block GREEN_DYED_AERCLOUD;
//    public static final Block GREY_DYED_AERCLOUD;
    public static final Block HELLFIRE_SLAB;
    public static final Block HELLFIRE_STAIRS;
    public static final Block HELLFIRE_STONE;
    public static final Block HELLFIRE_CRACKED_STONE;
    public static final Block HELLFIRE_STONE_TRAP;
    public static final Block HELLFIRE_WALL;
//    public static final Block HOLIDAY_LEAVES;
    public static final Block HOLYSTONE;
    public static final Block HOLYSTONE_BRICK;
    public static final Block HOLYSTONE_BRICK_SLAB;
    public static final Block HOLYSTONE_BRICK_STAIRS;
    public static final Block HOLYSTONE_BRICK_WALL;
    public static final Block HOLYSTONE_SLAB;
    public static final Block HOLYSTONE_STAIRS;
    public static final Block HOLYSTONE_WALL;
    public static final Block ICESTONE;
//    public static final Block ICESTONE_SLAB;
//    public static final Block ICESTONE_STAIRS;
//    public static final Block ICESTONE_WALL;
//    public static final Block INCUBATOR;
    public static final Block LIGHT_ANGELIC_SLAB;
    public static final Block LIGHT_ANGELIC_STAIRS;
    public static final Block LIGHT_ANGELIC_STONE;
    public static final Block LIGHT_ANGELIC_STONE_TRAP;
    public static final Block LIGHT_ANGELIC_WALL;
    public static final Block LIGHT_CARVED_SLAB;
    public static final Block LIGHT_CARVED_STAIRS;
    public static final Block LIGHT_CARVED_STONE;
    public static final Block LIGHT_CARVED_STONE_TRAP;
    public static final Block LIGHT_CARVED_WALL;
//    public static final Block LIGHT_BLUE_DYED_AERCLOUD;
//    public static final Block LIGHT_GREY_DYED_AERCLOUD;
    public static final Block LIGHT_HELLFIRE_SLAB;
    public static final Block LIGHT_HELLFIRE_STAIRS;
    public static final Block LIGHT_HELLFIRE_STONE;
    public static final Block LIGHT_HELLFIRE_STONE_TRAP;
    public static final Block LIGHT_HELLFIRE_WALL;
//    public static final Block LIME_DYED_AERCLOUD;
//    public static final Block LOCKED_ANGELIC_STONE;
//    public static final Block LOCKED_CARVED_STONE;
//    public static final Block LOCKED_HELLFIRE_STONE;
//    public static final Block LOCKED_LIGHT_ANGELIC_STONE;
//    public static final Block LOCKED_LIGHT_HELLFIRE_STONE;
//    public static final Block LOCKED_SENTRY_STONE;
//    public static final Block MAGENTA_DYED_AERCLOUD;
    public static final Block COBBLED_HOLYSTONE;
    public static final Block COBBLED_HOLYSTONE_SLAB;
    public static final Block COBBLED_HOLYSTONE_STAIRS;
    public static final Block COBBLED_HOLYSTONE_WALL;
    public static final Block MOSSY_HOLYSTONE;
    public static final Block MOSSY_HOLYSTONE_SLAB;
    public static final Block MOSSY_HOLYSTONE_STAIRS;
    public static final Block MOSSY_HOLYSTONE_WALL;
    public static final Block GOLDEN_MOSSY_HOLYSTONE;
//    public static final Block ORANGE_DYED_AERCLOUD;
//    public static final Block PILLAR;
//    public static final Block PILLAR_TOP;
    public static final Block PINK_AERCLOUD;
//    public static final Block PINK_DYED_AERCLOUD;
//    public static final Block POTTED_PURPLE_FLOWER;
//    public static final Block POTTED_WHITE_FLOWER;
//    public static final Block PRESENT;
//    public static final Block PURPLE_DYED_AERCLOUD;
    public static final Block QUICKSOIL;
    public static final Block QUICKSOIL_GLASS;
    public static final Block QUICKSOIL_GLASS_PANE;
//    public static final Block RED_DYED_AERCLOUD;
    public static final Block SENTRY_SLAB;
    public static final Block SENTRY_STAIRS;
    public static final Block SENTRY_STONE;
    public static final Block SENTRY_CRACKED_STONE;
    public static final Block SENTRY_STONE_TRAP;
    public static final Block SENTRY_WALL;
    public static final Block LIGHT_SENTRY_STONE;
    public static final Block LIGHT_SENTRY_STAIRS;
    public static final Block LIGHT_SENTRY_SLAB;
    public static final Block LIGHT_SENTRY_WALL;
    public static final Block SKYROOT_BOOKSHELF;
    public static final Block SKYROOT_DOOR;
    public static final Block SKYROOT_FENCE;
    public static final Block SKYROOT_FENCE_GATE;
    public static final Block SKYROOT_LOG;
    public static final Block SKYROOT_WOOD;
    public static final Block SKYROOT_PLANKS;
    public static final Block SKYROOT_SLAB;
    public static final Block SKYROOT_STAIRS;
    public static final Block SKYROOT_SAPLING;
    public static final Block SKYROOT_TRAPDOOR;
    public static final Block POTTED_SKYROOT_SAPLING;
    public static final Block SKYROOT_LEAVES;
    public static final Block SKYROOT_LEAF_PILE;
    public static final Block STRIPPED_SKYROOT_LOG;
    public static final Block STRIPPED_SKYROOT_WOOD;
    public static final Block SKYROOT_BUTTON;
    public static final Block SKYROOT_PRESSURE_PLATE;
    public static final Block GOLDEN_OAK_SAPLING;
    public static final Block POTTED_GOLDEN_OAK_SAPLING;
    public static final Block GOLDEN_OAK_LOG;
    public static final Block GOLDEN_OAK_WOOD;
    public static final Block STRIPPED_GOLDEN_OAK_LOG;
    public static final Block STRIPPED_GOLDEN_OAK_WOOD;
    public static final Block GOLDEN_OAK_LEAVES;
    public static final Block GOLDEN_OAK_PLANKS;
    public static final Block GOLDEN_OAK_FENCE;
    public static final Block GOLDEN_OAK_FENCE_GATE;
    public static final Block GOLDEN_OAK_SLAB;
    public static final Block GOLDEN_OAK_STAIRS;
    public static final Block GOLDEN_OAK_DOOR;
    public static final Block GOLDEN_OAK_TRAPDOOR;
    public static final Block GOLDEN_OAK_BUTTON;
    public static final Block GOLDEN_OAK_PRESSURE_PLATE;
    public static final Block ORANGE_SAPLING;
    public static final Block POTTED_ORANGE_SAPLING;
    public static final Block ORANGE_LOG;
    public static final Block ORANGE_WOOD;
    public static final Block STRIPPED_ORANGE_LOG;
    public static final Block STRIPPED_ORANGE_WOOD;
    public static final Block ORANGE_LEAVES;
    public static final Block ORANGE_PLANKS;
    public static final Block ORANGE_FENCE;
    public static final Block ORANGE_FENCE_GATE;
    public static final Block ORANGE_SLAB;
    public static final Block ORANGE_STAIRS;
    public static final Block ORANGE_DOOR;
    public static final Block ORANGE_TRAPDOOR;
    public static final Block ORANGE_BUTTON;
    public static final Block ORANGE_PRESSURE_PLATE;
    public static final Block CRYSTAL_SAPLING;
    public static final Block POTTED_CRYSTAL_SAPLING;
    public static final Block CRYSTAL_LOG;
    public static final Block CRYSTAL_WOOD;
    public static final Block STRIPPED_CRYSTAL_LOG;
    public static final Block STRIPPED_CRYSTAL_WOOD;
    public static final Block CRYSTAL_LEAVES;
    public static final Block CRYSTAL_PLANKS;
    public static final Block CRYSTAL_FENCE;
    public static final Block CRYSTAL_FENCE_GATE;
    public static final Block CRYSTAL_SLAB;
    public static final Block CRYSTAL_STAIRS;
    public static final Block CRYSTAL_DOOR;
    public static final Block CRYSTAL_TRAPDOOR;
    public static final Block CRYSTAL_BUTTON;
    public static final Block CRYSTAL_PRESSURE_PLATE;
//    public static final Block SUN_ALTAR;
//    public static final Block TREASURE_CHEST;
//    public static final Block WHITE_DYED_AERCLOUD;
    public static final Block ROSE_WISTERIA_LEAVES;
    public static final Block ROSE_WISTERIA_LEAF_PILE;
    public static final Block ROSE_WISTERIA_SAPLING;
    public static final Block POTTED_ROSE_WISTERIA_SAPLING;
    public static final Block ROSE_WISTERIA_HANGER;
    public static final Block FROST_WISTERIA_LEAVES;
    public static final Block FROST_WISTERIA_LEAF_PILE;
    public static final Block FROST_WISTERIA_SAPLING;
    public static final Block POTTED_FROST_WISTERIA_SAPLING;
    public static final Block FROST_WISTERIA_HANGER;
    public static final Block LAVENDER_WISTERIA_LEAVES;
    public static final Block LAVENDER_WISTERIA_LEAF_PILE;
    public static final Block LAVENDER_WISTERIA_SAPLING;
    public static final Block POTTED_LAVENDER_WISTERIA_SAPLING;
    public static final Block LAVENDER_WISTERIA_HANGER;
    public static final Block BOREAL_WISTERIA_LEAVES;
    public static final Block BOREAL_WISTERIA_HANGER;
    public static final Block BOREAL_WISTERIA_SAPLING;
    public static final Block POTTED_BOREAL_WISTERIA_SAPLING;
    public static final Block WISTERIA_LOG;
    public static final Block WISTERIA_WOOD;
    public static final Block STRIPPED_WISTERIA_LOG;
    public static final Block STRIPPED_WISTERIA_WOOD;
    public static final Block WISTERIA_PLANKS;
    public static final Block WISTERIA_SLAB;
    public static final Block WISTERIA_STAIRS;
    public static final Block WISTERIA_FENCE;
    public static final Block WISTERIA_FENCE_GATE;
    public static final Block WISTERIA_DOOR;
    public static final Block WISTERIA_TRAPDOOR;
    public static final Block WISTERIA_BUTTON;
    public static final Block WISTERIA_PRESSURE_PLATE;
//    public static final Block YELLOW_DYED_AERCLOUD;
    public static final Block ZANITE_BLOCK;
    public static final Block ZANITE_ORE;
    public static final Block BLUEBERRY_BUSH;
    public static final FlowableFluid DENSE_AERCLOUD_STILL;
    public static final Block DENSE_AERCLOUD;
    public static final Block AETHER_GRASS;
    public static final Block AETHER_TALL_GRASS;
    public static final Block AETHER_FERN;
    public static final Block POTTED_AETHER_FERN;
    public static final Block AETHER_BUSH;
    public static final Block DRIGEAN;
    public static final Block CLOUDSBLUFF;
    public static final Block ATARAXIA;
    public static final Block ANCIENT_FLOWER;
    public static final Block LUMINAR;
//    public static final Block SPORECAP_PINK;
//    public static final Block SPORECAP_BROWN;
//    public static final Block ROOTCAP;
//    public static final Block CINNABAR_ROSE;
    public static final Block ZANITE_CHAIN;
    public static final Block AMBROSIUM_LANTERN;


    private static final Block SWET_DROP;
    private static final Block BLUE_SWET_DROP;
    private static final Block GOLDEN_SWET_DROP;
    private static final Block PURPLE_SWET_DROP;

    static {
        AETHER_DIRT = register("aether_dirt", new Block(Settings.copy(Blocks.DIRT).strength(0.3f).sounds(BlockSoundGroup.GRAVEL)), buildingBlock());

        // We're going to be using this more than once, so we might as well store it in a variable.
        final Settings GRASS_BLOCKS = Settings.of(Material.SOLID_ORGANIC).mapColor(MapColor.CYAN).strength(0.4f).ticksRandomly().sounds(BlockSoundGroup.GRASS);
        AETHER_GRASS_BLOCK = register("aether_grass", new AetherGrassBlock(GRASS_BLOCKS), buildingBlock());

        // Note that because we're changing the material color (which mutates the `BlockSettings`), we want to
        // make a copy of the settings first.

        // Copying block settings is okay in this case, but copying the settings of another block is *not* something
        // you should do unless the new block is *directly related* to the block you're copying from. If the blocks just
        // happen to have several things in common, you should make a new settings object from scratch instead.
        AETHER_ENCHANTED_GRASS = register("enchanted_aether_grass", new EnchantedAetherGrassBlock(GRASS_BLOCKS.mapColor(MapColor.GOLD)), buildingBlock());

        final Settings GRASS = Settings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS);
        AETHER_GRASS = register("aether_grass_plant", new AetherBrushBlock(GRASS), buildingBlock());
        AETHER_TALL_GRASS = register("aether_tall_grass", new TallPlantBlock(GRASS), buildingBlock());
        AETHER_FERN = register("aether_fern", new AetherBrushBlock(GRASS), buildingBlock());
        POTTED_AETHER_FERN = register("potted_aether_fern", createPottedBlock(AETHER_FERN));
        AETHER_BUSH = register("aether_bush", new AetherBrushBlock(GRASS), buildingBlock());
        AMBROSIUM_ORE = register("ambrosium_ore", new OreBlock(Settings.of(Material.STONE).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(0, 2)), buildingBlock());
        BLUE_PORTAL = register("blue_portal", new AetherPortalBlock(Settings.of(Material.PORTAL, MapColor.BLUE).noCollision().ticksRandomly().nonOpaque().blockVision(AetherBlocks::never).strength(-1.0f).sounds(BlockSoundGroup.GLASS).luminance((state) -> 11)));
        HOLYSTONE = register("holystone", new Block(Settings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(0.5f, 10.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        COBBLED_HOLYSTONE = register("cobbled_holystone", new Block(Settings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(0.4f, 8.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        HOLYSTONE_BRICK = register("holystone_brick", new Block(Settings.of(Material.STONE).requiresTool().strength(1.5F, 6.0F).mapColor(MapColor.WHITE)), buildingBlock());
        MOSSY_HOLYSTONE = register("mossy_holystone", new Block(Settings.of(Material.STONE).requiresTool().strength(0.4F, 8.0F).mapColor(MapColor.WHITE)), buildingBlock());
        GOLDEN_MOSSY_HOLYSTONE = register("golden_mossy_holystone", new Block(Settings.of(Material.STONE).requiresTool().strength(2.0F, 6.0F).mapColor(MapColor.WHITE)), buildingBlock());
        ZANITE_ORE = register("zanite_ore", new OreBlock(Settings.of(Material.STONE).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(0, 2)), buildingBlock());
        AEROGEL = register("aerogel", new Block(Settings.of(Material.SOIL).strength(1.0f, 2000.0f).sounds(BlockSoundGroup.GLASS).solidBlock(AetherBlocks::never).nonOpaque()), buildingBlock());
        AETHER_FARMLAND = register("aether_farmland", new AetherFarmlandBlock(Settings.of(Material.SOIL).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRAVEL).blockVision(AetherBlocks::always).suffocates(AetherBlocks::always)), buildingBlock());
        AETHER_DIRT_PATH = register("aether_grass_path", new AetherDirtPathBlock(), buildingBlock());
        AMBROSIUM_TORCH = register("ambrosium_torch",  new AmbrosiumTorchBlock(), false, buildingBlock());
        AMBROSIUM_TORCH_WALL = register("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(), false, buildingBlock());
        Registry.register(Registry.ITEM, Aether.locate("ambrosium_torch"), new WallStandingBlockItem(AMBROSIUM_TORCH, AMBROSIUM_TORCH_WALL, buildingBlock()));

        final Settings ANGELIC_STONES = Settings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE);
        ANGELIC_STONE = register("angelic_stone", new Block(ANGELIC_STONES), buildingBlock());
        ANGELIC_CRACKED_STONE = register("angelic_stone_cracked", new Block(ANGELIC_STONES), buildingBlock());
        ANGELIC_SLAB = register("angelic_slab", new SlabBlock(Settings.copy(ANGELIC_STONE)), buildingBlock());
        ANGELIC_STAIRS = register("angelic_stairs", new StairsBlock(ANGELIC_STONE.getDefaultState(), Settings.copy(ANGELIC_STONE)), buildingBlock());
//        ANGELIC_STONE_TRAP = register("angelic_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        ANGELIC_WALL = register("angelic_wall", new WallBlock(Settings.copy(ANGELIC_STONE)), buildingBlock());
        BLUEBERRY_BUSH = register("blueberry_bush", new BlueberryBushBlock(Settings.of(Material.PLANT).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(AetherBlocks::canSpawnOnLeaves).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never).noCollision()));
//        BLACK_DYED_AERCLOUD = register("black_dyed_aercloud", null);
//        BLUE_DYED_AERCLOUD = register("blue_dyed_aercloud", null);
//        BROWN_DYED_AERCLOUD = register("brown_dyed_aercloud", null);
        CARVED_STONE = register("carved_stone", new Block(Settings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        CARVED_STONE_TRAP = register("carved_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        CARVED_SLAB = register("carved_slab", new SlabBlock(Settings.copy(CARVED_STONE)), buildingBlock());
        CARVED_STAIRS = register("carved_stairs", new StairsBlock(CARVED_STONE.getDefaultState(), Settings.copy(CARVED_STONE)), buildingBlock());
        CARVED_WALL = register("carved_wall", new WallBlock(Settings.copy(CARVED_STONE)), buildingBlock());
//        CHEST_MIMIC = register("chest_mimic", null);
        COLD_AERCLOUD = register("cold_aercloud", new BaseAercloudBlock(Settings.of(Material.ICE, MapColor.WHITE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque()), buildingBlock());
        BLUE_AERCLOUD = register("blue_aercloud", new BlueAercloudBlock(Settings.of(Material.ICE, MapColor.LIGHT_BLUE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque()), buildingBlock());
        PINK_AERCLOUD = register("pink_aercloud", new PinkAercloudBlock(Settings.of(Material.ICE, MapColor.PINK).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque()), buildingBlock());
//        CYAN_DYED_AERCLOUD = register("cyan_dyed_aercloud", null);
//        DECORATED_HOLIDAY_LEAVES = register("decorated_holiday_leaves", null);
//        ENCHANTED_GRAVITITE = register("enchanted_gravitite", null);
//        ENCHANTER = register("enchanter", null);
//        FREEZER = register("freezer", null);
        GOLDEN_AERCLOUD = register("golden_aercloud", new GoldenAercloudBlock(Settings.of(Material.ICE, MapColor.YELLOW).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque()), buildingBlock());
        DENSE_AERCLOUD_STILL = Registry.register(Registry.FLUID, Aether.locate("dense_aercloud"), new DenseAercloudFluid());
        DENSE_AERCLOUD = register("dense_aercloud", new FluidBlock(DENSE_AERCLOUD_STILL, Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()) {
        });
        GRAVITITE_ORE = register("gravitite_ore", new FloatingBlock(false, Settings.of(Material.STONE).requiresTool().strength(5.0F).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)), buildingBlock());
        GRAVITITE_LEVITATOR = register("gravitite_levitator", new FloatingBlock(true, Settings.of(Material.WOOD).strength(3.0F, 3.0F).sounds(BlockSoundGroup.WOOD)), buildingBlock());
//        GREEN_DYED_AERCLOUD = register("green_dyed_aercloud", null);
//        GREY_DYED_AERCLOUD = register("grey_dyed_aercloud", null);

        final Settings HELLFIRE_STONES = Settings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE);
        HELLFIRE_STONE = register("hellfire_stone", new Block(HELLFIRE_STONES), buildingBlock());
        HELLFIRE_CRACKED_STONE = register("hellfire_stone_cracked", new Block(HELLFIRE_STONES), buildingBlock());
        HELLFIRE_STONE_TRAP = register("hellfire_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        HELLFIRE_WALL = register("hellfire_wall", new WallBlock(Settings.copy(HELLFIRE_STONE)), buildingBlock());
        HELLFIRE_SLAB = register("hellfire_slab", new SlabBlock(Settings.copy(HELLFIRE_STONE)), buildingBlock());
        HELLFIRE_STAIRS = register("hellfire_stairs", new StairsBlock(HELLFIRE_STONE.getDefaultState(), Settings.copy(HELLFIRE_STONE)), buildingBlock());
//        HOLIDAY_LEAVES = register("holiday_leaves", null);
        HOLYSTONE_BRICK_SLAB = register("holystone_brick_slab", new SlabBlock(Settings.copy(HOLYSTONE_BRICK)), buildingBlock());
        HOLYSTONE_BRICK_STAIRS = register("holystone_brick_stairs", new StairsBlock(HOLYSTONE_BRICK.getDefaultState(), Settings.copy(HOLYSTONE_BRICK)), buildingBlock());
        HOLYSTONE_BRICK_WALL = register("holystone_brick_wall", new WallBlock(Settings.copy(HOLYSTONE_BRICK)), buildingBlock());
        HOLYSTONE_SLAB = register("holystone_slab", new SlabBlock(Settings.copy(HOLYSTONE)), buildingBlock());
        HOLYSTONE_STAIRS = register("holystone_stairs", new StairsBlock(HOLYSTONE.getDefaultState(), Settings.copy(HOLYSTONE)), buildingBlock());
        HOLYSTONE_WALL = register("holystone_wall", new WallBlock(Settings.copy(HOLYSTONE)), buildingBlock());
        ICESTONE = register("icestone", new Block(Settings.of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)), buildingBlock());
//        ICESTONE_SLAB = register("icestone_slab", null);
//        ICESTONE_STAIRS = register("icestone_stairs", null);
//        ICESTONE_WALL = register("icestone_wall", null);
//        INCUBATOR = register("incubator", null);
        LIGHT_ANGELIC_STONE = register("light_angelic_stone", new Block(Settings.of(Material.STONE).hardness(0.5f).luminance(state -> 11).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_ANGELIC_STONE_TRAP = register("light_angelic_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).luminance(state -> 11).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        LIGHT_ANGELIC_SLAB = register("light_angelic_slab", new SlabBlock(Settings.copy(LIGHT_ANGELIC_STONE)), buildingBlock());
        LIGHT_ANGELIC_STAIRS = register("light_angelic_stairs", new StairsBlock(LIGHT_ANGELIC_STONE.getDefaultState(), Settings.copy(LIGHT_ANGELIC_STONE)), buildingBlock());
        LIGHT_ANGELIC_WALL = register("light_angelic_wall", new WallBlock(Settings.copy(LIGHT_ANGELIC_STONE)), buildingBlock());
        LIGHT_CARVED_STONE = register("light_carved_stone", new Block(Settings.of(Material.STONE).hardness(0.5f).luminance(state -> 11).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_CARVED_STONE_TRAP = register("light_carved_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).luminance(state -> 11).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        LIGHT_CARVED_SLAB = register("light_carved_slab", new SlabBlock(Settings.copy(LIGHT_CARVED_STONE)), buildingBlock());
        LIGHT_CARVED_STAIRS = register("light_carved_stairs", new StairsBlock(LIGHT_CARVED_STONE.getDefaultState(), Settings.copy(LIGHT_CARVED_STONE)), buildingBlock());
        LIGHT_CARVED_WALL = register("light_carved_wall", new WallBlock(Settings.copy(LIGHT_CARVED_STONE)), buildingBlock());
//        LIGHT_BLUE_DYED_AERCLOUD = register("light_blue_dyed_aercloud", null);
//        LIGHT_GREY_DYED_AERCLOUD = register("light_grey_dyed_aercloud", null);
        LIGHT_HELLFIRE_STONE = register("light_hellfire_stone", new Block(Settings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_HELLFIRE_STONE_TRAP = register("light_hellfire_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        LIGHT_HELLFIRE_SLAB = register("light_hellfire_slab", new SlabBlock(Settings.copy(LIGHT_HELLFIRE_STONE)), buildingBlock());
        LIGHT_HELLFIRE_STAIRS = register("light_hellfire_stairs", new StairsBlock(LIGHT_HELLFIRE_STONE.getDefaultState(), Settings.copy(LIGHT_HELLFIRE_STONE)), buildingBlock());
        LIGHT_HELLFIRE_WALL = register("light_hellfire_wall", new WallBlock(Settings.copy(LIGHT_HELLFIRE_STONE)), buildingBlock());
//        LIME_DYED_AERCLOUD = register("lime_dyed_aercloud", null);
//        LOCKED_ANGELIC_STONE = register("locked_angelic_stone", null);
//        LOCKED_CARVED_STONE = register("locked_carved_stone", null);
//        LOCKED_HELLFIRE_STONE = register("locked_hellfire_stone", null);
//        LOCKED_LIGHT_ANGELIC_STONE = register("locked_light_angelic_stone", null);
//        LOCKED_LIGHT_HELLFIRE_STONE = register("locked_light_hellfire_stone", null);
//        LOCKED_SENTRY_STONE = register("locked_sentry_stone", null);
//        MAGENTA_DYED_AERCLOUD = register("magenta_dyed_aercloud", null);
        COBBLED_HOLYSTONE_SLAB = register("cobbled_holystone_slab", new SlabBlock(Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
        COBBLED_HOLYSTONE_STAIRS = register("cobbled_holystone_stairs", new StairsBlock(MOSSY_HOLYSTONE.getDefaultState(), Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
        COBBLED_HOLYSTONE_WALL = register("cobbled_holystone_wall", new WallBlock(Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
        MOSSY_HOLYSTONE_SLAB = register("mossy_holystone_slab", new SlabBlock(Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
        MOSSY_HOLYSTONE_STAIRS = register("mossy_holystone_stairs", new StairsBlock(MOSSY_HOLYSTONE.getDefaultState(), Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
        MOSSY_HOLYSTONE_WALL = register("mossy_holystone_wall", new WallBlock(Settings.copy(MOSSY_HOLYSTONE)), buildingBlock());
//        ORANGE_DYED_AERCLOUD = register("orange_dyed_aercloud", null);
//        PILLAR = register("pillar", null);
//        PILLAR_TOP = register("pillar_top", null);
//        PINK_AERCLOUD = register("pink_aercloud", null);
//        PINK_DYED_AERCLOUD = register("pink_dyed_aercloud", null);
//        POTTED_PURPLE_FLOWER = register("potted_purple_flower", null);
//        POTTED_WHITE_FLOWER = register("potted_white_flower", null);
//        PRESENT = register("present", null);
//        PURPLE_DYED_AERCLOUD = register("purple_dyed_aercloud", null);
//        PURPLE_FLOWER = register("purple_flower", null);
        QUICKSOIL = register("quicksoil", new Block(Settings.of(Material.AGGREGATE).strength(0.5f, -1.0f).slipperiness(1.0F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)), buildingBlock());
        QUICKSOIL_GLASS = register("quicksoil_glass", new GlassBlock(Settings.of(Material.GLASS).luminance(state -> 14).strength(0.2f, -1.0f).slipperiness(1.0F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(AetherBlocks::never)), buildingBlock());
        QUICKSOIL_GLASS_PANE = register("quicksoil_glass_pane", new PaneBlock(Settings.of(Material.GLASS).luminance(state -> 14).strength(0.2F, -1.0F).slipperiness(1.0F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(AetherBlocks::never)), buildingBlock());
//        RED_DYED_AERCLOUD = register("red_dyed_aercloud", null);
        FLUTEGRASS = register("flutegrass", new AetherBrushBlock(GRASS.mapColor(MapColor.GOLD), ImmutableSet.of(QUICKSOIL), true), buildingBlock());
        final Settings SENTRY_STONES = Settings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE);
        SENTRY_STONE = register("sentry_stone", new Block(SENTRY_STONES), buildingBlock());
        SENTRY_CRACKED_STONE = register("sentry_stone_cracked", new Block(SENTRY_STONES), buildingBlock());
        LIGHT_SENTRY_STONE = register("light_sentry_stone", new Block(SENTRY_STONES.luminance(ignored -> 10)), buildingBlock());
        SENTRY_STONE_TRAP = register("sentry_stone_trap", new Block(Settings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        SENTRY_SLAB = register("sentry_slab", new SlabBlock(Settings.copy(SENTRY_STONE)), buildingBlock());
        SENTRY_STAIRS = register("sentry_stairs", new StairsBlock(SENTRY_STONE.getDefaultState(), Settings.copy(SENTRY_STONE)), buildingBlock());
        SENTRY_WALL = register("sentry_wall", new WallBlock(Settings.copy(SENTRY_STONE)), buildingBlock());
        LIGHT_SENTRY_SLAB = register("light_sentry_slab", new SlabBlock(Settings.copy(LIGHT_SENTRY_STONE)), buildingBlock());
        LIGHT_SENTRY_STAIRS = register("light_sentry_stairs", new StairsBlock(LIGHT_SENTRY_STONE.getDefaultState(), Settings.copy(LIGHT_SENTRY_STONE)), buildingBlock());
        LIGHT_SENTRY_WALL = register("light_sentry_wall", new WallBlock(Settings.copy(LIGHT_SENTRY_STONE)), buildingBlock());

        SKYROOT_SAPLING = register("skyroot_sapling", new AetherSaplingBlock(new SkyrootSaplingGenerator(), createSaplingProperties()), buildingBlock());
        POTTED_SKYROOT_SAPLING = register("potted_skyroot_sapling", createPottedBlock(SKYROOT_SAPLING));
        SKYROOT_LOG = register("skyroot_log", createLogBlock(MapColor.GREEN, MapColor.OAK_TAN), buildingBlock());
        SKYROOT_WOOD = register("skyroot_wood", createLogBlock(MapColor.GREEN, MapColor.OAK_TAN), buildingBlock());
        STRIPPED_SKYROOT_LOG = register("stripped_skyroot_log", createLogBlock(MapColor.OAK_TAN, MapColor.OAK_TAN), buildingBlock());
        STRIPPED_SKYROOT_WOOD = register("stripped_skyroot_wood", createLogBlock(MapColor.OAK_TAN, MapColor.OAK_TAN), buildingBlock());
        SKYROOT_LEAVES = register("skyroot_leaves", createLeavesBlock(), buildingBlock());
        SKYROOT_LEAF_PILE = register("skyroot_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(BlockSoundGroup.VINE)), buildingBlock());

        final Settings SKYROOT_WOOD = Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
        SKYROOT_PLANKS = register("skyroot_planks", new Block(SKYROOT_WOOD), buildingBlock());
        SKYROOT_BOOKSHELF = register("skyroot_bookshelf", new Block(Settings.of(Material.WOOD).strength(1.5f).sounds(BlockSoundGroup.WOOD)), buildingBlock());
        SKYROOT_FENCE = register("skyroot_fence", new FenceBlock(SKYROOT_WOOD), buildingBlock());
        SKYROOT_FENCE_GATE = register("skyroot_fence_gate", new FenceGateBlock(SKYROOT_WOOD), buildingBlock());
        SKYROOT_SLAB = register("skyroot_slab", new SlabBlock(Settings.copy(SKYROOT_PLANKS)), buildingBlock());
        SKYROOT_STAIRS = register("skyroot_stairs", new StairsBlock(SKYROOT_PLANKS.getDefaultState(), Settings.copy(SKYROOT_PLANKS)), buildingBlock());
        SKYROOT_TRAPDOOR = register("skyroot_trapdoor", new AetherTrapdoorBlock(Settings.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        SKYROOT_DOOR = register("skyroot_door", new AetherDoorBlock(Settings.copy(Blocks.OAK_DOOR)), buildingBlock());
        SKYROOT_BUTTON = register("skyroot_button", createWoodenButtonBlock(), buildingBlock());
        SKYROOT_PRESSURE_PLATE = register("skyroot_pressure_plate", createWoodenPressurePlateBlock(MapColor.OAK_TAN), buildingBlock());

        GOLDEN_OAK_SAPLING = register("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), createSaplingProperties().luminance(state -> 7)), buildingBlock());
        POTTED_GOLDEN_OAK_SAPLING = register("potted_golden_oak_sapling", createPottedBlock(GOLDEN_OAK_SAPLING));
        GOLDEN_OAK_LOG = register("golden_oak_log", createLogBlock(MapColor.OAK_TAN, MapColor.RED), buildingBlock());
        GOLDEN_OAK_WOOD = register("golden_oak_wood", createLogBlock(MapColor.OAK_TAN, MapColor.RED), buildingBlock());
        STRIPPED_GOLDEN_OAK_LOG = register("stripped_golden_oak_log", createLogBlock(MapColor.RED, MapColor.RED), buildingBlock());
        STRIPPED_GOLDEN_OAK_WOOD = register("stripped_golden_oak_wood", createLogBlock(MapColor.RED, MapColor.RED), buildingBlock());
        GOLDEN_OAK_LEAVES = register("golden_oak_leaves", createLeavesBlock(), buildingBlock());


        final Settings GOLDEN_OAK_WOOD = Settings.of(Material.WOOD, MapColor.RED).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
        GOLDEN_OAK_PLANKS = register("golden_oak_planks", new Block(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_FENCE = register("golden_oak_fence", new FenceBlock(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_FENCE_GATE = register("golden_oak_fence_gate", new FenceGateBlock(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_SLAB = register("golden_oak_slab", new SlabBlock(Settings.copy(GOLDEN_OAK_PLANKS)), buildingBlock());
        GOLDEN_OAK_STAIRS = register("golden_oak_stairs", new StairsBlock(GOLDEN_OAK_PLANKS.getDefaultState(), Settings.copy(GOLDEN_OAK_PLANKS)), buildingBlock());
        GOLDEN_OAK_TRAPDOOR = register("golden_oak_trapdoor", new AetherTrapdoorBlock(Settings.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        GOLDEN_OAK_DOOR = register("golden_oak_door", new AetherDoorBlock(Settings.copy(Blocks.OAK_DOOR)), buildingBlock());
        GOLDEN_OAK_BUTTON = register("golden_oak_button", createWoodenButtonBlock(), buildingBlock());
        GOLDEN_OAK_PRESSURE_PLATE = register("golden_oak_pressure_plate", createWoodenPressurePlateBlock(MapColor.RED), buildingBlock());

        ORANGE_SAPLING = register("orange_sapling", new AetherSaplingBlock(new OrangeSaplingGenerator(), createSaplingProperties()), buildingBlock());
        POTTED_ORANGE_SAPLING = register("potted_orange_sapling", createPottedBlock(ORANGE_SAPLING));
        ORANGE_LOG = register("orange_log", createLogBlock(MapColor.SPRUCE_BROWN, MapColor.PINK), buildingBlock());
        ORANGE_WOOD = register("orange_wood", createLogBlock(MapColor.SPRUCE_BROWN, MapColor.PINK), buildingBlock());
        STRIPPED_ORANGE_LOG = register("stripped_orange_log", createLogBlock(MapColor.PINK, MapColor.PINK), buildingBlock());
        STRIPPED_ORANGE_WOOD = register("stripped_orange_wood", createLogBlock(MapColor.PINK, MapColor.PINK), buildingBlock());
        ORANGE_LEAVES = register("orange_leaves", new AetherFruitingLeaves(createLeavesProperties(0, BlockSoundGroup.AZALEA_LEAVES), AetherItems.ORANGE), buildingBlock());

        final Settings ORANGE_WOOD = Settings.of(Material.WOOD, MapColor.PINK).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
        ORANGE_PLANKS = register("orange_planks", new Block(ORANGE_WOOD), buildingBlock());
        ORANGE_FENCE = register("orange_fence", new FenceBlock(ORANGE_WOOD), buildingBlock());
        ORANGE_FENCE_GATE = register("orange_fence_gate", new FenceGateBlock(ORANGE_WOOD), buildingBlock());
        ORANGE_SLAB = register("orange_slab", new SlabBlock(ORANGE_WOOD), buildingBlock());
        ORANGE_STAIRS = register("orange_stairs", new StairsBlock(ORANGE_PLANKS.getDefaultState(), ORANGE_WOOD), buildingBlock());
        ORANGE_TRAPDOOR = register("orange_trapdoor", new AetherTrapdoorBlock(Settings.copy(GOLDEN_OAK_TRAPDOOR)), buildingBlock());
        ORANGE_DOOR = register("orange_door", new AetherDoorBlock(Settings.copy(GOLDEN_OAK_DOOR)), buildingBlock());
        ORANGE_BUTTON = register("orange_button", createWoodenButtonBlock(), buildingBlock());
        ORANGE_PRESSURE_PLATE = register("orange_pressure_plate", createWoodenPressurePlateBlock(MapColor.PINK), buildingBlock());

        CRYSTAL_SAPLING = register("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), createSaplingProperties().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), buildingBlock());
        POTTED_CRYSTAL_SAPLING = register("potted_crystal_sapling", createPottedBlock(CRYSTAL_SAPLING));
        CRYSTAL_LOG = register("crystal_log", createLogBlock(MapColor.GRAY, MapColor.LIGHT_GRAY), buildingBlock());
        CRYSTAL_WOOD = register("crystal_wood", createLogBlock(MapColor.GRAY, MapColor.LIGHT_GRAY), buildingBlock());
        STRIPPED_CRYSTAL_LOG = register("stripped_crystal_log", createLogBlock(MapColor.LIGHT_GRAY, MapColor.LIGHT_GRAY), buildingBlock());
        STRIPPED_CRYSTAL_WOOD = register("stripped_crystal_wood", createLogBlock(MapColor.LIGHT_GRAY, MapColor.LIGHT_GRAY), buildingBlock());
        CRYSTAL_LEAVES = register("crystal_leaves", createCrystalLeavesBlock(), buildingBlock());
        CRYSTAL_TRAPDOOR = register("crystal_trapdoor", new AetherTrapdoorBlock(Settings.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        CRYSTAL_DOOR = register("crystal_door", new AetherDoorBlock(Settings.copy(Blocks.OAK_DOOR)), buildingBlock());
        CRYSTAL_BUTTON = register("crystal_button", createWoodenButtonBlock(), buildingBlock());
        CRYSTAL_PRESSURE_PLATE = register("crystal_pressure_plate", createWoodenPressurePlateBlock(MapColor.LIGHT_GRAY), buildingBlock());

        final Settings CRYSTAL_WOOD = Settings.of(Material.WOOD, MapColor.LIGHT_GRAY).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
        CRYSTAL_PLANKS = register("crystal_planks", new Block(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_FENCE = register("crystal_fence", new FenceBlock(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_FENCE_GATE = register("crystal_fence_gate", new FenceGateBlock(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_SLAB = register("crystal_slab", new SlabBlock(Settings.copy(CRYSTAL_PLANKS)), buildingBlock());
        CRYSTAL_STAIRS = register("crystal_stairs", new StairsBlock(CRYSTAL_PLANKS.getDefaultState(), Settings.copy(CRYSTAL_PLANKS)), buildingBlock());
        WISTERIA_LOG = register("wisteria_log", createLogBlock(MapColor.YELLOW, MapColor.RED), buildingBlock());
        WISTERIA_WOOD = register("wisteria_wood", createLogBlock(MapColor.YELLOW, MapColor.RED), buildingBlock());
        STRIPPED_WISTERIA_LOG = register("stripped_wisteria_log", createLogBlock(MapColor.RED, MapColor.RED), buildingBlock());
        STRIPPED_WISTERIA_WOOD = register("stripped_wisteria_wood", createLogBlock(MapColor.RED, MapColor.RED), buildingBlock());

        final Settings ROSE_WISTERIA = Settings.of(Material.LEAVES, MapColor.PINK).noCollision().strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        ROSE_WISTERIA_LEAVES = register("rose_wisteria_leaves", new AetherLeavesBlock(ROSE_WISTERIA, false), buildingBlock());
        ROSE_WISTERIA_LEAF_PILE = register("rose_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(BlockSoundGroup.VINE)), buildingBlock());
        ROSE_WISTERIA_SAPLING = register("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), createSaplingProperties()), buildingBlock());
        POTTED_ROSE_WISTERIA_SAPLING = register("potted_rose_wisteria_sapling", createPottedBlock(ROSE_WISTERIA_SAPLING));
        ROSE_WISTERIA_HANGER = register("rose_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(BlockSoundGroup.GRASS)), buildingBlock());

        final Settings FROST_WISTERIA = Settings.of(Material.LEAVES, MapColor.LIGHT_BLUE).noCollision().strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        FROST_WISTERIA_LEAVES = register("frost_wisteria_leaves", new AetherLeavesBlock(FROST_WISTERIA, false), buildingBlock());
        FROST_WISTERIA_LEAF_PILE = register("frost_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(BlockSoundGroup.VINE)), buildingBlock());
        FROST_WISTERIA_SAPLING = register("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), createSaplingProperties()), buildingBlock());
        POTTED_FROST_WISTERIA_SAPLING = register("potted_frost_wisteria_sapling", createPottedBlock(FROST_WISTERIA_SAPLING));
        FROST_WISTERIA_HANGER = register("frost_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(BlockSoundGroup.GRASS)), buildingBlock());

        final Settings LAVENDER_WISTERIA = Settings.of(Material.LEAVES, MapColor.MAGENTA).noCollision().strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        LAVENDER_WISTERIA_LEAVES = register("lavender_wisteria_leaves", new AetherLeavesBlock(LAVENDER_WISTERIA, false), buildingBlock());
        LAVENDER_WISTERIA_LEAF_PILE = register("lavender_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(BlockSoundGroup.VINE)), buildingBlock());
        LAVENDER_WISTERIA_SAPLING = register("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), createSaplingProperties()), buildingBlock());
        POTTED_LAVENDER_WISTERIA_SAPLING = register("potted_lavender_wisteria_sapling", createPottedBlock(LAVENDER_WISTERIA_SAPLING));
        LAVENDER_WISTERIA_HANGER = register("lavender_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(BlockSoundGroup.GRASS)), buildingBlock());

        final Vec3i[] BOREAL_COLORS = new Vec3i[]{ RenderUtils.toRGB(0x59CDFF), RenderUtils.toRGB(0x3affcb), RenderUtils.toRGB(0x599CFF), RenderUtils.toRGB(0x8158FE) };
        final Settings BOREAL_WISTERIA = Settings.of(Material.LEAVES, MapColor.CYAN).noCollision().strength(0.05F).sounds(BlockSoundGroup.AZALEA_LEAVES).luminance(state -> 7).ticksRandomly().nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never).emissiveLighting(AetherBlocks::always).postProcess(AetherBlocks::always);
        BOREAL_WISTERIA_LEAVES = register("boreal_wisteria_leaves", new AuralLeavesBlock(BOREAL_WISTERIA, false, BOREAL_COLORS), buildingBlock());
        BOREAL_WISTERIA_SAPLING = register("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), createSaplingProperties().luminance(state -> 5)), buildingBlock());
        POTTED_BOREAL_WISTERIA_SAPLING = register("potted_boreal_wisteria_sapling", createPottedBlock(BOREAL_WISTERIA_SAPLING));
        BOREAL_WISTERIA_HANGER = register("boreal_wisteria_hanger", new AuralHangerBlock(BOREAL_WISTERIA, BOREAL_COLORS), buildingBlock());

        final Settings WISTERIA_WOOD = Settings.of(Material.WOOD, MapColor.YELLOW).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
        WISTERIA_PLANKS = register("wisteria_planks", new Block(WISTERIA_WOOD), buildingBlock());
        WISTERIA_FENCE = register("wisteria_fence", new FenceBlock(WISTERIA_WOOD), buildingBlock());
        WISTERIA_FENCE_GATE = register("wisteria_fence_gate", new FenceGateBlock(WISTERIA_WOOD), buildingBlock());
        WISTERIA_SLAB = register("wisteria_slab", new SlabBlock(Settings.copy(WISTERIA_PLANKS)), buildingBlock());
        WISTERIA_STAIRS = register("wisteria_stairs", new StairsBlock(WISTERIA_PLANKS.getDefaultState(), Settings.copy(WISTERIA_PLANKS)), buildingBlock());
        WISTERIA_TRAPDOOR = register("wisteria_trapdoor", new AetherTrapdoorBlock(Settings.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        WISTERIA_DOOR = register("wisteria_door", new AetherDoorBlock(Settings.copy(Blocks.OAK_DOOR)), buildingBlock());
        WISTERIA_BUTTON = register("wisteria_button", createWoodenButtonBlock(), buildingBlock());
        WISTERIA_PRESSURE_PLATE = register("wisteria_pressure_plate", createWoodenPressurePlateBlock(MapColor.YELLOW), buildingBlock());

//        SUN_ALTAR = register("sun_altar", null);
//        TREASURE_CHEST = register("treasure_chest", null);
//        WHITE_DYED_AERCLOUD = register("white_dyed_aercloud", null);
//        YELLOW_DYED_AERCLOUD = register("yellow_dyed_aercloud", null);
        ZANITE_BLOCK = register("zanite_block", new Block(Settings.of(Material.METAL).strength(3.0f, -1.0f).sounds(BlockSoundGroup.METAL)), buildingBlock());
        BLOCK_OF_GRAVITITE = register("block_of_gravitite", new FloatingBlock(false, Settings.of(Material.METAL).strength(3.0f, -1.0f).sounds(BlockSoundGroup.METAL)), buildingBlock());
        ZANITE_CHAIN = register("zanite_chain", new ChainBlock(Settings.copy(Blocks.CHAIN)), buildingBlock());
        AMBROSIUM_LANTERN = register("ambrosium_lantern", new AmbrosiumLanternBlock(Settings.of(Material.METAL).hardness(3.5f).requiresTool().luminance(state -> 15).resistance(1.0f).sounds(BlockSoundGroup.LANTERN)), buildingBlock());

        ANCIENT_FLOWER = register("ancient_flower", new FlowerBlock(StatusEffects.ABSORPTION, 20, Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), buildingBlock());
        ATARAXIA = register("ataraxia", new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), buildingBlock());
        CLOUDSBLUFF = register("cloudsbluff", new FlowerBlock(StatusEffects.SLOW_FALLING, 6, Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), buildingBlock());
        DRIGEAN = register("drigean", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), buildingBlock());
        LUMINAR = register("luminar", new FlowerBlock(StatusEffects.GLOWING, 9, Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), buildingBlock());

        SWET_DROP = register("swet_drop", new SwetDropBlock(Settings.of(Material.SOLID_ORGANIC).breakInstantly().noCollision().mapColor(MapColor.CLEAR), AetherEntityTypes.WHITE_SWET));
        BLUE_SWET_DROP = register("blue_swet_drop", new SwetDropBlock(Settings.of(Material.SOLID_ORGANIC).breakInstantly().noCollision().mapColor(MapColor.CLEAR), AetherEntityTypes.BLUE_SWET));
        GOLDEN_SWET_DROP = register("golden_swet_drop", new SwetDropBlock(Settings.of(Material.SOLID_ORGANIC).breakInstantly().noCollision().mapColor(MapColor.CLEAR), AetherEntityTypes.GOLDEN_SWET));
        PURPLE_SWET_DROP = register("purple_swet_drop", new SwetDropBlock(Settings.of(Material.SOLID_ORGANIC).breakInstantly().noCollision().mapColor(MapColor.CLEAR), AetherEntityTypes.PURPLE_SWET));
    }

    static {
        // Logs and woods
        for (Block block : new Block[]{
                SKYROOT_LOG,
                STRIPPED_SKYROOT_LOG,
                GOLDEN_OAK_LOG,
                STRIPPED_GOLDEN_OAK_LOG,
                CRYSTAL_LOG,
                STRIPPED_CRYSTAL_LOG,
                WISTERIA_LOG,
                STRIPPED_WISTERIA_LOG,
                SKYROOT_WOOD,
                STRIPPED_SKYROOT_WOOD,
                GOLDEN_OAK_WOOD,
                STRIPPED_GOLDEN_OAK_WOOD,
                CRYSTAL_WOOD,
                STRIPPED_CRYSTAL_WOOD,
                WISTERIA_WOOD,
                STRIPPED_WISTERIA_WOOD
        }) {
            registerFlammable(block, 5, 5);
        }
        // Plank stuff
        for (Block block : new Block[]{
                SKYROOT_PLANKS,
                SKYROOT_SLAB,
                SKYROOT_FENCE_GATE,
                SKYROOT_FENCE,
                SKYROOT_STAIRS,
                GOLDEN_OAK_PLANKS,
                GOLDEN_OAK_SLAB,
                GOLDEN_OAK_FENCE_GATE,
                GOLDEN_OAK_FENCE,
                GOLDEN_OAK_STAIRS,
                CRYSTAL_PLANKS,
                CRYSTAL_SLAB,
                CRYSTAL_FENCE_GATE,
                CRYSTAL_FENCE,
                CRYSTAL_STAIRS,
                WISTERIA_PLANKS,
                WISTERIA_SLAB,
                WISTERIA_FENCE_GATE,
                WISTERIA_FENCE,
                WISTERIA_STAIRS
        }) {
            registerFlammable(block, 20, 5);
        }
        // Leaves and leaf stuff
        for (Block block : new Block[]{
                SKYROOT_LEAVES,
                GOLDEN_OAK_LEAVES,
                CRYSTAL_LEAVES,
                ROSE_WISTERIA_LEAVES,
                FROST_WISTERIA_LEAVES,
                LAVENDER_WISTERIA_LEAVES,
                SKYROOT_LEAF_PILE,
                ROSE_WISTERIA_LEAF_PILE,
                ROSE_WISTERIA_HANGER,
                FROST_WISTERIA_LEAF_PILE,
                FROST_WISTERIA_HANGER,
                LAVENDER_WISTERIA_LEAF_PILE,
                LAVENDER_WISTERIA_HANGER
        }) {
            registerFlammable(block, 60, 30);
            ComposterBlock.registerCompostableItem(0.3F, block);
        }
        // Saplings
        for (Block block : new Block[]{
                SKYROOT_SAPLING,
                GOLDEN_OAK_SAPLING,
                CRYSTAL_SAPLING,
                ROSE_WISTERIA_SAPLING,
                FROST_WISTERIA_SAPLING,
                LAVENDER_WISTERIA_SAPLING,
                BOREAL_WISTERIA_SAPLING
        }) {
            ComposterBlock.registerCompostableItem(0.3F, block);
        }
        // Grass
        for (Block block : new Block[]{
                AETHER_GRASS,
                AETHER_TALL_GRASS,
                AETHER_FERN,
                AETHER_BUSH,
                FLUTEGRASS
        }) {
            registerFlammable(block, 100, 60);
            ComposterBlock.registerCompostableItem(0.5F, block);
        }
        // Flowers
        for (Block block : new Block[]{
                ANCIENT_FLOWER,
                ATARAXIA,
                CLOUDSBLUFF,
                DRIGEAN,
                LUMINAR
        }) {
            ComposterBlock.registerCompostableItem(0.65F, block);
        }
        // Misc
        registerFlammable(SKYROOT_BOOKSHELF, 20, 30);
    }

    private static Item.Settings buildingBlock() {
        return new Item.Settings().group(AetherItemGroups.Blocks);
    }

    private static Block register(String id, Block block, Item.Settings settings) {
        return register(id, block, true, settings);
    }

    private static void registerFlammable(Block block, int flammability, int encouragement) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.registerFlammableBlock(block, flammability, encouragement);
    }

    private static Block register(String id, Block block, boolean registerAsBlockItem, Item.Settings settings) {
        Identifier trueId = Aether.locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        if (registerAsBlockItem) Registry.register(Registry.ITEM, trueId, new BlockItem(block, settings));
        return block;
    }

    private static Block register(String id, Block block, Block rootBlock, Item.Settings settings) {
        Identifier trueId = Aether.locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        Registry.register(Registry.ITEM, trueId, new WallStandingBlockItem(rootBlock, block, settings));
        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, Aether.locate(id), block);
    }

    private static PillarBlock createLogBlock(MapColor topMaterialColor, MapColor sideMaterialColor) {
        return new PillarBlock(Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    }

    private static Settings createLeafPileBlock(BlockSoundGroup sounds) {
        return Settings.of(Material.REPLACEABLE_PLANT).strength(0.2f).sounds(sounds).nonOpaque().suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
    }

    private static Settings createWisteriaHangerBlock(BlockSoundGroup sounds) {
        return Settings.of(Material.DECORATION).strength(0.2f).noCollision().breakInstantly().sounds(sounds).nonOpaque().suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
    }

    private static Settings createLeavesProperties(@Nullable Integer luminance, @Nullable BlockSoundGroup sounds) {
        sounds = (sounds != null ? sounds : BlockSoundGroup.GRASS);
        Settings properties = Settings.of(Material.LEAVES).strength(0.2f).ticksRandomly().sounds(sounds).nonOpaque().allowsSpawning(AetherBlocks::ocelotOrParrot).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        if (luminance != null) {
            properties = properties.luminance(ignored -> luminance);
        }
        return properties;
    }

    private static Settings createSaplingProperties() {
        return Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);
    }

    private static AetherLeavesBlock createLeavesBlock(@Nullable Integer luminance, @Nullable BlockSoundGroup sounds) {
        return new AetherLeavesBlock(createLeavesProperties(luminance, sounds), true);
    }

    private static AetherLeavesBlock createLeavesBlock() {
        return createLeavesBlock(null, null);
    }

    private static CrystalLeavesBlock createCrystalLeavesBlock(@Nullable BlockSoundGroup sounds) {
        sounds = (sounds != null ? sounds : BlockSoundGroup.AMETHYST_BLOCK);
        return new CrystalLeavesBlock(createLeavesProperties(null, sounds));
    }

    private static CrystalLeavesBlock createCrystalLeavesBlock() {
        return createCrystalLeavesBlock(null);
    }

    private static FlowerPotBlock createPottedBlock(Block sourceBlock) {
        return new FlowerPotBlock(sourceBlock, Settings.of(Material.DECORATION).breakInstantly().nonOpaque());
    }

    private static WoodenButtonBlock createWoodenButtonBlock() {
        return new AetherWoodenButtonBlock(Settings.of(Material.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    }

    private static PressurePlateBlock createWoodenPressurePlateBlock(MapColor color) {
        return new AetherPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, Settings.of(Material.WOOD, color).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    }

    public static void init() {
        // N/A
    }

    private static void putBlocks(RenderLayer layer, Block... blocks) {
        Arrays.stream(blocks).forEach(block -> RenderLayers.BLOCKS.put(block, layer));
    }

    private static void putFluids(RenderLayer layer, Fluid... blocks) {
        Arrays.stream(blocks).forEach(block -> RenderLayers.FLUIDS.put(block, layer));
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        putBlocks(RenderLayer.getTranslucent(),
                BLUE_PORTAL, QUICKSOIL_GLASS, QUICKSOIL_GLASS_PANE, AEROGEL,
                COLD_AERCLOUD, BLUE_AERCLOUD, PINK_AERCLOUD, GOLDEN_AERCLOUD
        );
        putFluids(RenderLayer.getTranslucent(),
                DENSE_AERCLOUD_STILL
        );
        putBlocks(RenderLayer.getCutout(),
                SKYROOT_SAPLING, GOLDEN_OAK_SAPLING, ORANGE_SAPLING, CRYSTAL_SAPLING,
                ROSE_WISTERIA_SAPLING, FROST_WISTERIA_SAPLING, LAVENDER_WISTERIA_SAPLING, BOREAL_WISTERIA_SAPLING,
                AETHER_GRASS, AETHER_TALL_GRASS, AETHER_FERN, AETHER_BUSH, FLUTEGRASS, BLUEBERRY_BUSH,
                ROSE_WISTERIA_HANGER, LAVENDER_WISTERIA_HANGER, FROST_WISTERIA_HANGER, BOREAL_WISTERIA_HANGER,
                AMBROSIUM_TORCH, AMBROSIUM_TORCH_WALL,
                SKYROOT_TRAPDOOR, GOLDEN_OAK_TRAPDOOR, ORANGE_TRAPDOOR, CRYSTAL_TRAPDOOR, WISTERIA_TRAPDOOR,
                POTTED_SKYROOT_SAPLING, POTTED_GOLDEN_OAK_SAPLING, POTTED_ORANGE_SAPLING, POTTED_CRYSTAL_SAPLING, POTTED_AETHER_FERN,
                POTTED_ROSE_WISTERIA_SAPLING, POTTED_FROST_WISTERIA_SAPLING, POTTED_LAVENDER_WISTERIA_SAPLING, POTTED_BOREAL_WISTERIA_SAPLING,
                SKYROOT_DOOR, GOLDEN_OAK_DOOR, ORANGE_DOOR, CRYSTAL_DOOR, WISTERIA_DOOR,
                ANCIENT_FLOWER, ATARAXIA, CLOUDSBLUFF, DRIGEAN, LUMINAR
        );
        putBlocks(RenderLayer.getCutoutMipped(),
                AETHER_GRASS_BLOCK, ZANITE_CHAIN, AMBROSIUM_LANTERN,
                SKYROOT_LEAVES, GOLDEN_OAK_LEAVES, ORANGE_LEAVES, CRYSTAL_LEAVES,
                ROSE_WISTERIA_LEAVES, FROST_WISTERIA_LEAVES, LAVENDER_WISTERIA_LEAVES, BOREAL_WISTERIA_LEAVES,
                SKYROOT_LEAF_PILE, ROSE_WISTERIA_LEAF_PILE, FROST_WISTERIA_LEAF_PILE, LAVENDER_WISTERIA_LEAF_PILE
        );
        FluidRenderSetup.setupFluidRendering(DENSE_AERCLOUD_STILL, null, Aether.locate("dense_aercloud"), 0xFFFFFF);
    }

    private static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    public static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

    public static boolean always(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return true;
    }

    private static Boolean ocelotOrParrot(BlockState blockState, BlockView blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }
}
