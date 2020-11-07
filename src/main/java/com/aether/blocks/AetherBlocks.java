package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.decorative.*;
import com.aether.blocks.natural.AetherGrassBlock;
import com.aether.blocks.natural.AetherSaplingBlock;
import com.aether.blocks.natural.BlueberryBushBlock;
import com.aether.blocks.natural.EnchantedAetherGrassBlock;
import com.aether.items.AetherItemGroups;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class AetherBlocks {
    public static final Block AEROGEL;
    public static final Block AEROGEL_SLAB;
    public static final Block AEROGEL_STAIRS;
    public static final Block AEROGEL_WALL;
    public static final Block AETHER_DIRT;
    public static final Block AETHER_ENCHANTED_GRASS;
    public static final Block AETHER_FARMLAND;
    public static final Block AETHER_GRASS;
    public static final Block AETHER_GRASS_PATH;
    public static final Block AMBROSIUM_ORE;
    public static final Block AMBROSIUM_TORCH;
    public static final Block AMBROSIUM_TORCH_WALL;
    public static final Block ANGELIC_SLAB;
    public static final Block ANGELIC_STAIRS;
    public static final Block ANGELIC_STONE;
    public static final Block ANGELIC_CRACKED_STONE;
    public static final Block ANGELIC_STONE_TRAP;
    public static final Block ANGELIC_WALL;
    //    public static final Block BERRY_BUSH;
//    public static final Block BERRY_BUSH_STEM;
//    public static final Block BLACK_DYED_AERCLOUD;
//    public static final Block BLUE_AERCLOUD;
//    public static final Block BLUE_DYED_AERCLOUD;
    public static final Block BLUE_PORTAL;
    //    public static final Block BROWN_DYED_AERCLOUD;
    public static final Block CARVED_SLAB;
    public static final Block CARVED_STAIRS;
    public static final Block CARVED_STONE;
    public static final Block CARVED_STONE_TRAP;
    public static final Block CARVED_WALL;
    //    public static final Block CHEST_MIMIC;
//    public static final Block COLD_AERCLOUD;
//    public static final Block CRYSTAL_FRUIT_LEAVES;
//    public static final Block CRYSTAL_LEAVES;
//    public static final Block CRYSTAL_SAPLING;
//    public static final Block CYAN_DYED_AERCLOUD;
//    public static final Block DECORATED_HOLIDAY_LEAVES;
//    public static final Block ENCHANTED_GRAVITITE;
//    public static final Block ENCHANTER;
//    public static final Block FREEZER;
//    public static final Block GOLDEN_AERCLOUD;
//    public static final Block GOLDEN_OAK_LEAVES;
//    public static final Block GOLDEN_OAK_LOG;
//    public static final Block GOLDEN_OAK_SAPLING;
//    public static final Block GOLDEN_OAK_WOOD;
//    public static final Block GRAVITITE_ORE;
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
    //    public static final Block ICESTONE;
//    public static final Block ICESTONE_SLAB;
//    public static final Block ICESTONE_STAIRS;
//    public static final Block ICESTONE_WALL;
//    public static final Block INCUBATOR;
    public static final Block LIGHT_ANGELIC_SLAB;
    public static final Block LIGHT_ANGELIC_STAIRS;
    public static final Block LIGHT_ANGELIC_STONE;
    public static final Block LIGHT_ANGELIC_STONE_TRAP;
    public static final Block LIGHT_ANGELIC_WALL;
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
    public static final Block MOSSY_HOLYSTONE;
    public static final Block MOSSY_HOLYSTONE_SLAB;
    public static final Block MOSSY_HOLYSTONE_STAIRS;
    public static final Block MOSSY_HOLYSTONE_WALL;
    //    public static final Block ORANGE_DYED_AERCLOUD;
//    public static final Block PILLAR;
//    public static final Block PILLAR_TOP;
//    public static final Block PINK_AERCLOUD;
//    public static final Block PINK_DYED_AERCLOUD;
//    public static final Block POTTED_PURPLE_FLOWER;
//    public static final Block POTTED_WHITE_FLOWER;
//    public static final Block PRESENT;
//    public static final Block PURPLE_DYED_AERCLOUD;
//    public static final Block PURPLE_FLOWER;
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
    public static final Block SKYROOT_BOOKSHELF;
    public static final Block SKYROOT_FENCE;
    public static final Block SKYROOT_FENCE_GATE;
    //    public static final Block SKYROOT_LEAVES;
    public static final Block SKYROOT_LOG;
    public static final Block SKYROOT_PLANKS;
    //    public static final Block SKYROOT_SAPLING;
    public static final Block SKYROOT_SLAB;
    public static final Block SKYROOT_STAIRS;
    public static final Block SKYROOT_SAPLING;
    public static final Block SKYROOT_LEAVES;
    public static final Block STRIPPED_SKYROOT_LOG;
    public static final Block GOLDEN_OAK_SAPLING;
    public static final Block GOLDEN_OAK_LOG;
    public static final Block STRIPPED_GOLDEN_OAK_LOG;
    public static final Block GOLDEN_OAK_LEAVES;
    public static final Block GOLDEN_OAK_PLANKS;
    public static final Block GOLDEN_OAK_FENCE;
    public static final Block GOLDEN_OAK_FENCE_GATE;
    public static final Block GOLDEN_OAK_SLAB;
    public static final Block GOLDEN_OAK_STAIRS;
    public static final Block CRYSTAL_SAPLING;
    public static final Block CRYSTAL_LOG;
    public static final Block STRIPPED_CRYSTAL_LOG;
    public static final Block CRYSTAL_LEAVES;
    public static final Block CRYSTAL_PLANKS;
    public static final Block CRYSTAL_FENCE;
    public static final Block CRYSTAL_FENCE_GATE;
    public static final Block CRYSTAL_SLAB;
    public static final Block CRYSTAL_STAIRS;
    //    public static final Block SKYROOT_WOOD;
//    public static final Block STRIPPED_SKYROOT_LOG;
//    public static final Block STRIPPED_SKYROOT_WOOD;
//    public static final Block SUN_ALTAR;
//    public static final Block TREASURE_CHEST;
//    public static final Block WHITE_DYED_AERCLOUD;
//    public static final Block WHITE_FLOWER;
//    public static final Block YELLOW_DYED_AERCLOUD;
    public static final Block ZANITE_BLOCK;
    public static final Block ZANITE_ORE;
    private static final Block BLUEBERRY_BUSH;

    static {
        AETHER_DIRT = register("aether_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT)), buildingBlock());
        AETHER_GRASS = register("aether_grass", new AetherGrassBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).materialColor(MaterialColor.CYAN)), buildingBlock());
        AETHER_ENCHANTED_GRASS = register("enchanted_aether_grass", new EnchantedAetherGrassBlock(FabricBlockSettings.copyOf(AETHER_GRASS).materialColor(MaterialColor.GOLD)), buildingBlock());
        AMBROSIUM_ORE = register("ambrosium_ore", new Block(FabricBlockSettings.copyOf(Blocks.COAL_ORE)), buildingBlock());
        BLUE_PORTAL = register("blue_portal", new PortalBlock(AbstractBlock.Settings.of(Material.PORTAL, MaterialColor.BLUE).nonOpaque().noCollision().ticksRandomly().dropsNothing().blockVision(AetherBlocks::never).strength(-1.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 11)));
        HOLYSTONE = register("holystone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).strength(0.5F, 10.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        HOLYSTONE_BRICK = register("holystone_brick", new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).materialColor(MaterialColor.WHITE)), buildingBlock());
        MOSSY_HOLYSTONE = register("mossy_holystone", new Block(FabricBlockSettings.copyOf(Blocks.MOSSY_COBBLESTONE).materialColor(MaterialColor.WHITE)), buildingBlock());
        ZANITE_ORE = register("zanite_ore", new Block(FabricBlockSettings.copyOf(Blocks.IRON_ORE)), buildingBlock());
//        CRYSTAL_SAPLING = register("crystal_sapling", null);
        AEROGEL = register("aerogel", new Block(FabricBlockSettings.of(Material.SOIL).strength(1.0F, 2000.0F).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        AEROGEL_SLAB = register("aerogel_slab", new AetherSlabBlock(AEROGEL.getDefaultState()), buildingBlock());
        AEROGEL_STAIRS = register("aerogel_stairs", new AetherStairsBlock(AEROGEL.getDefaultState()), buildingBlock());
        AEROGEL_WALL = register("aerogel_wall", new AetherWallBlock(AEROGEL.getDefaultState()), buildingBlock());
        AETHER_FARMLAND = register("aether_farmland", new AetherFarmlandBlock(FabricBlockSettings.of(Material.SOIL).ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRAVEL).blockVision(AetherBlocks::always).suffocates(AetherBlocks::always)), buildingBlock());
        AETHER_GRASS_PATH = register("aether_grass_path", new AetherGrassPathBlock(), buildingBlock());
        AMBROSIUM_TORCH = register("ambrosium_torch", new AmbrosiumTorchBlock(), false, buildingBlock());
        AMBROSIUM_TORCH_WALL = register("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(), AMBROSIUM_TORCH, buildingBlock());
        ANGELIC_STONE = register("angelic_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        ANGELIC_CRACKED_STONE = register("angelic_stone_cracked", new Block(FabricBlockSettings.copyOf(ANGELIC_STONE)), buildingBlock());
        ANGELIC_SLAB = register("angelic_slab", new AetherSlabBlock(ANGELIC_STONE.getDefaultState()), buildingBlock());
        ANGELIC_STAIRS = register("angelic_stairs", new AetherStairsBlock(ANGELIC_STONE.getDefaultState()), buildingBlock());
        ANGELIC_STONE_TRAP = register("angelic_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)));
        ANGELIC_WALL = register("angelic_wall", new AetherWallBlock(ANGELIC_STONE.getDefaultState()), buildingBlock());
        BLUEBERRY_BUSH = register("blueberry_bush", new BlueberryBushBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).collidable(false)), buildingBlock());
//        BERRY_BUSH_STEM = register("berry_bush_stem", null);
//        BLACK_DYED_AERCLOUD = register("black_dyed_aercloud", null);
//        BLUE_AERCLOUD = register("blue_aercloud", null);
//        BLUE_DYED_AERCLOUD = register("blue_dyed_aercloud", null);
//        BROWN_DYED_AERCLOUD = register("brown_dyed_aercloud", null);
        CARVED_STONE = register("carved_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        CARVED_STONE_TRAP = register("carved_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)));
        CARVED_SLAB = register("carved_slab", new AetherSlabBlock(CARVED_STONE.getDefaultState()), buildingBlock());
        CARVED_STAIRS = register("carved_stairs", new AetherStairsBlock(CARVED_STONE.getDefaultState()), buildingBlock());
        CARVED_WALL = register("carved_wall", new AetherWallBlock(CARVED_STONE.getDefaultState()), buildingBlock());
//        CHEST_MIMIC = register("chest_mimic", null);
//        COLD_AERCLOUD = register("cold_aercloud", null);
//        CRYSTAL_FRUIT_LEAVES = register("crystal_fruit_leaves", null);
//        CRYSTAL_LEAVES = register("crystal_leaves", null);
//        CYAN_DYED_AERCLOUD = register("cyan_dyed_aercloud", null);
//        DECORATED_HOLIDAY_LEAVES = register("decorated_holiday_leaves", null);
//        ENCHANTED_GRAVITITE = register("enchanted_gravitite", null);
//        ENCHANTER = register("enchanter", null);
//        FREEZER = register("freezer", null);
//        GOLDEN_AERCLOUD = register("golden_aercloud", null);
//        GOLDEN_OAK_LEAVES = register("golden_oak_leaves", null);
//        GOLDEN_OAK_LOG = register("golden_oak_log", null);
//        GOLDEN_OAK_SAPLING = register("golden_oak_sapling", null);
//        GOLDEN_OAK_WOOD = register("golden_oak_wood", null);
//        GRAVITITE_ORE = register("gravitite_ore", null);
//        GREEN_DYED_AERCLOUD = register("green_dyed_aercloud", null);
//        GREY_DYED_AERCLOUD = register("grey_dyed_aercloud", null);
        HELLFIRE_STONE = register("hellfire_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        HELLFIRE_CRACKED_STONE = register("hellfire_stone_cracked", new Block(FabricBlockSettings.copyOf(HELLFIRE_STONE)), buildingBlock());
        HELLFIRE_STONE_TRAP = register("hellfire_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)));
        HELLFIRE_WALL = register("hellfire_wall", new AetherWallBlock(HELLFIRE_STONE.getDefaultState()), buildingBlock());
        HELLFIRE_SLAB = register("hellfire_slab", new AetherSlabBlock(HELLFIRE_STONE.getDefaultState()), buildingBlock());
        HELLFIRE_STAIRS = register("hellfire_stairs", new AetherStairsBlock(HELLFIRE_STONE.getDefaultState()), buildingBlock());
//        HOLIDAY_LEAVES = register("holiday_leaves", null);
        HOLYSTONE_BRICK_SLAB = register("holystone_brick_slab", new AetherSlabBlock(HOLYSTONE_BRICK.getDefaultState()), buildingBlock());
        HOLYSTONE_BRICK_STAIRS = register("holystone_brick_stairs", new AetherStairsBlock(HOLYSTONE_BRICK.getDefaultState()), buildingBlock());
        HOLYSTONE_BRICK_WALL = register("holystone_brick_wall", new AetherWallBlock(HOLYSTONE_BRICK.getDefaultState()), buildingBlock());
        HOLYSTONE_SLAB = register("holystone_slab", new AetherSlabBlock(HOLYSTONE.getDefaultState()), buildingBlock());
        HOLYSTONE_STAIRS = register("holystone_stairs", new AetherStairsBlock(HOLYSTONE.getDefaultState()), buildingBlock());
        HOLYSTONE_WALL = register("holystone_wall", new AetherWallBlock(HOLYSTONE.getDefaultState()), buildingBlock());
//        ICESTONE = register("icestone", null);
//        ICESTONE_SLAB = register("icestone_slab", null);
//        ICESTONE_STAIRS = register("icestone_stairs", null);
//        ICESTONE_WALL = register("icestone_wall", null);
//        INCUBATOR = register("incubator", null);
        LIGHT_ANGELIC_STONE = register("light_angelic_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).luminance(11).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_ANGELIC_STONE_TRAP = register("light_angelic_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).luminance(11).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_ANGELIC_SLAB = register("light_angelic_slab", new AetherSlabBlock(LIGHT_ANGELIC_STONE.getDefaultState()), buildingBlock());
        LIGHT_ANGELIC_STAIRS = register("light_angelic_stairs", new AetherStairsBlock(LIGHT_ANGELIC_STONE.getDefaultState()), buildingBlock());
        LIGHT_ANGELIC_WALL = register("light_angelic_wall", new AetherWallBlock(LIGHT_ANGELIC_STONE.getDefaultState()), buildingBlock());
//        LIGHT_BLUE_DYED_AERCLOUD = register("light_blue_dyed_aercloud", null);
//        LIGHT_GREY_DYED_AERCLOUD = register("light_grey_dyed_aercloud", null);
        LIGHT_HELLFIRE_STONE = register("light_hellfire_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        LIGHT_HELLFIRE_STONE_TRAP = register("light_hellfire_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)));
        LIGHT_HELLFIRE_SLAB = register("light_hellfire_slab", new AetherSlabBlock(LIGHT_HELLFIRE_STONE.getDefaultState()), buildingBlock());
        LIGHT_HELLFIRE_STAIRS = register("light_hellfire_stairs", new AetherStairsBlock(LIGHT_HELLFIRE_STONE.getDefaultState()), buildingBlock());
        LIGHT_HELLFIRE_WALL = register("light_hellfire_wall", new AetherWallBlock(LIGHT_HELLFIRE_STONE.getDefaultState()), buildingBlock());
//        LIME_DYED_AERCLOUD = register("lime_dyed_aercloud", null);
//        LOCKED_ANGELIC_STONE = register("locked_angelic_stone", null);
//        LOCKED_CARVED_STONE = register("locked_carved_stone", null);
//        LOCKED_HELLFIRE_STONE = register("locked_hellfire_stone", null);
//        LOCKED_LIGHT_ANGELIC_STONE = register("locked_light_angelic_stone", null);
//        LOCKED_LIGHT_HELLFIRE_STONE = register("locked_light_hellfire_stone", null);
//        LOCKED_SENTRY_STONE = register("locked_sentry_stone", null);
//        MAGENTA_DYED_AERCLOUD = register("magenta_dyed_aercloud", null);
        MOSSY_HOLYSTONE_SLAB = register("mossy_holystone_slab", new AetherSlabBlock(MOSSY_HOLYSTONE.getDefaultState()), buildingBlock());
        MOSSY_HOLYSTONE_STAIRS = register("mossy_holystone_stairs", new AetherStairsBlock(MOSSY_HOLYSTONE.getDefaultState()), buildingBlock());
        MOSSY_HOLYSTONE_WALL = register("mossy_holystone_wall", new AetherWallBlock(MOSSY_HOLYSTONE.getDefaultState()), buildingBlock());
//        ORANGE_DYED_AERCLOUD = register("orange_dyed_aercloud", null);
//        PILLAR = register("pillar", null);
//        PILLAR_TOP = register("pillar_top", null);
//        PINK_AERCLOUD = register("pink_aercloud", null);
//        PINK_DYED_AERCLOUD = register("pink_dyed_aercloud", null);
//        POTTED_PURPLE_FLOWER = register("potted_purple_flower", new FlowerPotBlock());
//        POTTED_WHITE_FLOWER = register("potted_white_flower", null);
//        PRESENT = register("present", null);
//        PURPLE_DYED_AERCLOUD = register("purple_dyed_aercloud", null);
//        PURPLE_FLOWER = register("purple_flower", null);
        QUICKSOIL = register("quicksoil", new Block(FabricBlockSettings.of(Material.AGGREGATE).strength(0.5F, -1.0F).sounds(BlockSoundGroup.SAND)), buildingBlock());
        QUICKSOIL_GLASS = register("quicksoil_glass", new Block(FabricBlockSettings.of(Material.GLASS).luminance(14).strength(0.2F, -1.0F).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        QUICKSOIL_GLASS_PANE = register("quicksoil_glass_pane", new QuicksoilGlassPaneBlock(), buildingBlock());
//        RED_DYED_AERCLOUD = register("red_dyed_aercloud", null);
        SENTRY_STONE = register("sentry_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5F).resistance(1.0F).sounds(BlockSoundGroup.STONE)), buildingBlock());
        SENTRY_CRACKED_STONE = register("sentry_stone_cracked", new Block(FabricBlockSettings.copyOf(SENTRY_STONE)), buildingBlock());
        LIGHT_SENTRY_STONE = register("light_sentry_stone", new Block(FabricBlockSettings.copyOf(SENTRY_STONE).luminance(ignored -> 10)), buildingBlock());
        SENTRY_STONE_TRAP = register("sentry_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0F).resistance(6000000.0F).sounds(BlockSoundGroup.STONE)));
        SENTRY_SLAB = register("sentry_slab", new AetherSlabBlock(SENTRY_STONE.getDefaultState()), buildingBlock());
        SENTRY_STAIRS = register("sentry_stairs", new AetherStairsBlock(SENTRY_STONE.getDefaultState()), buildingBlock());
        SENTRY_WALL = register("sentry_wall", new AetherWallBlock(SENTRY_STONE.getDefaultState()), buildingBlock());
        SKYROOT_SAPLING = register("skyroot_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), buildingBlock());
        SKYROOT_LOG = register("skyroot_log", createLogBlock(MaterialColor.GREEN, MaterialColor.WOOD), buildingBlock());
        STRIPPED_SKYROOT_LOG = register("stripped_skyroot_log", createLogBlock(MaterialColor.WOOD, MaterialColor.WOOD), buildingBlock());
        SKYROOT_LEAVES = register("skyroot_leaves", createLeavesBlock(), buildingBlock());
        SKYROOT_PLANKS = register("skyroot_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)), buildingBlock());
        SKYROOT_BOOKSHELF = register("skyroot_bookshelf", new Block(FabricBlockSettings.of(Material.WOOD).strength(1.5F).sounds(BlockSoundGroup.WOOD)), buildingBlock());
        SKYROOT_FENCE = register("skyroot_fence", new FenceBlock(FabricBlockSettings.copy(SKYROOT_PLANKS)), buildingBlock());
        SKYROOT_FENCE_GATE = register("skyroot_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(AetherBlocks.SKYROOT_PLANKS)), buildingBlock());
        SKYROOT_SLAB = register("skyroot_slab", new AetherSlabBlock(SKYROOT_PLANKS.getDefaultState()), buildingBlock());
        SKYROOT_STAIRS = register("skyroot_stairs", new AetherStairsBlock(SKYROOT_PLANKS.getDefaultState()), buildingBlock());
        GOLDEN_OAK_SAPLING = register("golden_oak_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), buildingBlock());
        GOLDEN_OAK_LOG = register("golden_oak_log", createLogBlock(MaterialColor.WOOD, MaterialColor.RED), buildingBlock());
        STRIPPED_GOLDEN_OAK_LOG = register("stripped_golden_oak_log", createLogBlock(MaterialColor.RED, MaterialColor.RED), buildingBlock());
        GOLDEN_OAK_LEAVES = register("golden_oak_leaves", createLeavesBlock(), buildingBlock());
        GOLDEN_OAK_PLANKS = register("golden_oak_planks", new Block(FabricBlockSettings.copyOf(SKYROOT_PLANKS).materialColor(MaterialColor.RED)), buildingBlock());
        GOLDEN_OAK_FENCE = register("golden_oak_fence", new FenceBlock(FabricBlockSettings.copyOf(GOLDEN_OAK_PLANKS)), buildingBlock());
        GOLDEN_OAK_FENCE_GATE = register("golden_oak_fence_gate", new FenceGateBlock(FabricBlockSettings.copyOf(GOLDEN_OAK_PLANKS)), buildingBlock());
        GOLDEN_OAK_SLAB = register("golden_oak_slab", new AetherSlabBlock(GOLDEN_OAK_PLANKS.getDefaultState()), buildingBlock());
        GOLDEN_OAK_STAIRS = register("golden_oak_stairs", new AetherStairsBlock(GOLDEN_OAK_PLANKS.getDefaultState()), buildingBlock());
        CRYSTAL_SAPLING = register("crystal_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        CRYSTAL_LOG = register("crystal_log", createLogBlock(MaterialColor.GRAY, MaterialColor.LIGHT_GRAY), buildingBlock());
        STRIPPED_CRYSTAL_LOG = register("stripped_crystal_log", createLogBlock(MaterialColor.LIGHT_GRAY, MaterialColor.LIGHT_GRAY), buildingBlock());
        CRYSTAL_LEAVES = register("crystal_leaves", createLeavesBlock(BlockSoundGroup.GLASS), buildingBlock());
        CRYSTAL_PLANKS = register("crystal_planks", new Block(FabricBlockSettings.copyOf(SKYROOT_PLANKS).materialColor(MaterialColor.LIGHT_GRAY)), buildingBlock());
        CRYSTAL_FENCE = register("crystal_fence", new FenceBlock(FabricBlockSettings.copyOf(CRYSTAL_PLANKS)), buildingBlock());
        CRYSTAL_FENCE_GATE = register("crystal_fence_gate", new FenceGateBlock(FabricBlockSettings.copyOf(CRYSTAL_PLANKS)), buildingBlock());
        CRYSTAL_SLAB = register("crystal_slab", new AetherSlabBlock(CRYSTAL_PLANKS.getDefaultState()), buildingBlock());
        CRYSTAL_STAIRS = register("crystal_stairs", new AetherStairsBlock(CRYSTAL_PLANKS.getDefaultState()), buildingBlock());
//        STRIPPED_SKYROOT_LOG = register("stripped_skyroot_log", null);
//        STRIPPED_SKYROOT_WOOD = register("stripped_skyroot_wood", null);
//        SUN_ALTAR = register("sun_altar", null);
//        TREASURE_CHEST = register("treasure_chest", null);
//        WHITE_DYED_AERCLOUD = register("white_dyed_aercloud", null);
//        WHITE_FLOWER = register("white_flower", null);
//        YELLOW_DYED_AERCLOUD = register("yellow_dyed_aercloud", null);
        ZANITE_BLOCK = register("zanite_block", new Block(FabricBlockSettings.of(Material.METAL).strength(3.0F, -1.0F).sounds(BlockSoundGroup.METAL)), buildingBlock());
    }

    private static Item.Settings buildingBlock() {
        return new FabricItemSettings().group(AetherItemGroups.BLOCKS);
    }

    private static Block register(String id, Block block, Item.Settings settings) {
        return register(id, block, true, settings);
    }

    private static Block register(String id, Block block, boolean registerItem, Item.Settings settings) {
        Identifier trueId = new Identifier(Aether.MODID, id);
        Registry.register(Registry.BLOCK, trueId, block);
        if (registerItem) Registry.register(Registry.ITEM, trueId, new BlockItem(block, settings));
        return block;
    }

    private static Block register(String id, Block block, Block rootBlock, Item.Settings settings) {
        Identifier trueId = new Identifier(Aether.MODID, id);
        Registry.register(Registry.BLOCK, trueId, block);
        Registry.register(Registry.ITEM, trueId, new WallStandingBlockItem(rootBlock, block, settings));
        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, id), block);
    }

    private static PillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    private static LeavesBlock createLeavesBlock() {
        return new LeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never));
    }

    private static LeavesBlock createLeavesBlock(BlockSoundGroup sounds) {
        return new LeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(sounds).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never));
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BLUEBERRY_BUSH, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BLUEBERRY_BUSH, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(QUICKSOIL_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(QUICKSOIL_GLASS, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(QUICKSOIL_GLASS_PANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(QUICKSOIL_GLASS_PANE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AEROGEL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(AEROGEL_SLAB, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(AEROGEL_STAIRS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(AEROGEL_WALL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(AMBROSIUM_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AMBROSIUM_TORCH_WALL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SKYROOT_SAPLING, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(GOLDEN_OAK_SAPLING, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(CRYSTAL_SAPLING, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(SKYROOT_LEAVES, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(GOLDEN_OAK_LEAVES, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CRYSTAL_LEAVES, RenderLayer.getTranslucent());
    }

    public static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

    public static boolean always(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return true;
    }
}
