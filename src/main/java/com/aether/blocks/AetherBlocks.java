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
    public static final Block aerogel;
    public static final Block aerogel_slab;
    public static final Block aerogel_stairs;
    public static final Block aerogel_wall;
    public static final Block aether_dirt;
    public static final Block aether_enchanted_grass;
    public static final Block aether_farmland;
    public static final Block aether_grass;
    public static final Block aether_grass_path;
    public static final Block ambrosium_ore;
    public static final Block ambrosium_torch;
    public static final Block ambrosium_torch_wall;
    public static final Block angelic_slab;
    public static final Block angelic_stairs;
    public static final Block angelic_stone;
    public static final Block angelic_cracked_stone;
    public static final Block angelic_stone_trap;
    public static final Block angelic_wall;
    //    public static final Block berry_bush;
//    public static final Block berry_bush_stem;
//    public static final Block black_dyed_aercloud;
//    public static final Block blue_aercloud;
//    public static final Block blue_dyed_aercloud;
    public static final Block blue_portal;
    //    public static final Block brown_dyed_aercloud;
    public static final Block carved_slab;
    public static final Block carved_stairs;
    public static final Block carved_stone;
    public static final Block carved_stone_trap;
    public static final Block carved_wall;
    //    public static final Block chest_mimic;
//    public static final Block cold_aercloud;
//    public static final Block crystal_fruit_leaves;
//    public static final Block crystal_leaves;
//    public static final Block crystal_sapling;
//    public static final Block cyan_dyed_aercloud;
//    public static final Block decorated_holiday_leaves;
//    public static final Block enchanted_gravitite;
//    public static final Block enchanter;
//    public static final Block freezer;
//    public static final Block golden_aercloud;
//    public static final Block golden_oak_leaves;
//    public static final Block golden_oak_log;
//    public static final Block golden_oak_sapling;
//    public static final Block golden_oak_wood;
//    public static final Block gravitite_ore;
//    public static final Block green_dyed_aercloud;
//    public static final Block grey_dyed_aercloud;
    public static final Block hellfire_slab;
    public static final Block hellfire_stairs;
    public static final Block hellfire_stone;
    public static final Block hellfire_cracked_stone;
    public static final Block hellfire_stone_trap;
    public static final Block hellfire_wall;
    //    public static final Block holiday_leaves;
    public static final Block holystone;
    public static final Block holystone_brick;
    public static final Block holystone_brick_slab;
    public static final Block holystone_brick_stairs;
    public static final Block holystone_brick_wall;
    public static final Block holystone_slab;
    public static final Block holystone_stairs;
    public static final Block holystone_wall;
    //    public static final Block icestone;
//    public static final Block icestone_slab;
//    public static final Block icestone_stairs;
//    public static final Block icestone_wall;
//    public static final Block incubator;
    public static final Block light_angelic_slab;
    public static final Block light_angelic_stairs;
    public static final Block light_angelic_stone;
    public static final Block light_angelic_stone_trap;
    public static final Block light_angelic_wall;
    //    public static final Block light_blue_dyed_aercloud;
//    public static final Block light_grey_dyed_aercloud;
    public static final Block light_hellfire_slab;
    public static final Block light_hellfire_stairs;
    public static final Block light_hellfire_stone;
    public static final Block light_hellfire_stone_trap;
    public static final Block light_hellfire_wall;
    //    public static final Block lime_dyed_aercloud;
//    public static final Block locked_angelic_stone;
//    public static final Block locked_carved_stone;
//    public static final Block locked_hellfire_stone;
//    public static final Block locked_light_angelic_stone;
//    public static final Block locked_light_hellfire_stone;
//    public static final Block locked_sentry_stone;
//    public static final Block magenta_dyed_aercloud;
    public static final Block mossy_holystone;
    public static final Block mossy_holystone_slab;
    public static final Block mossy_holystone_stairs;
    public static final Block mossy_holystone_wall;
    //    public static final Block orange_dyed_aercloud;
//    public static final Block pillar;
//    public static final Block pillar_top;
//    public static final Block pink_aercloud;
//    public static final Block pink_dyed_aercloud;
//    public static final Block potted_purple_flower;
//    public static final Block potted_white_flower;
//    public static final Block present;
//    public static final Block purple_dyed_aercloud;
//    public static final Block purple_flower;
    public static final Block quicksoil;
    public static final Block quicksoil_glass;
    public static final Block quicksoil_glass_pane;
    //    public static final Block red_dyed_aercloud;
    public static final Block sentry_slab;
    public static final Block sentry_stairs;
    public static final Block sentry_stone;
    public static final Block sentry_cracked_stone;
    public static final Block sentry_stone_trap;
    public static final Block sentry_wall;
    public static final Block light_sentry_stone;
    public static final Block skyroot_bookshelf;
    public static final Block skyroot_fence;
    public static final Block skyroot_fence_gate;
    //    public static final Block skyroot_leaves;
    public static final Block skyroot_log;
    public static final Block skyroot_planks;
    //    public static final Block skyroot_sapling;
    public static final Block skyroot_slab;
    public static final Block skyroot_stairs;
    public static final Block skyroot_sapling;
    public static final Block skyroot_leaves;
    public static final Block stripped_skyroot_log;
    public static final Block golden_oak_sapling;
    public static final Block golden_oak_log;
    public static final Block stripped_golden_oak_log;
    public static final Block golden_oak_leaves;
    public static final Block golden_oak_planks;
    public static final Block golden_oak_fence;
    public static final Block golden_oak_fence_gate;
    public static final Block golden_oak_slab;
    public static final Block golden_oak_stairs;
    public static final Block crystal_sapling;
    public static final Block crystal_log;
    public static final Block stripped_crystal_log;
    public static final Block crystal_leaves;
    public static final Block crystal_planks;
    public static final Block crystal_fence;
    public static final Block crystal_fence_gate;
    public static final Block crystal_slab;
    public static final Block crystal_stairs;
    //    public static final Block skyroot_wood;
//    public static final Block stripped_skyroot_log;
//    public static final Block stripped_skyroot_wood;
//    public static final Block sun_altar;
//    public static final Block treasure_chest;
//    public static final Block white_dyed_aercloud;
//    public static final Block white_flower;
//    public static final Block yellow_dyed_aercloud;
    public static final Block zanite_block;
    public static final Block zanite_ore;
    private static final Block blueberry_bush;

    static {
        aether_dirt = register("aether_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT)), buildingBlock());
        aether_grass = register("aether_grass", new AetherGrassBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).materialColor(MaterialColor.CYAN)), buildingBlock());
        aether_enchanted_grass = register("enchanted_aether_grass", new EnchantedAetherGrassBlock(FabricBlockSettings.copyOf(aether_grass).materialColor(MaterialColor.GOLD)), buildingBlock());
        ambrosium_ore = register("ambrosium_ore", new Block(FabricBlockSettings.copyOf(Blocks.COAL_ORE)), buildingBlock());
        blue_portal = register("blue_portal", new PortalBlock(AbstractBlock.Settings.of(Material.PORTAL, MaterialColor.BLUE).nonOpaque().noCollision().ticksRandomly().dropsNothing().blockVision(AetherBlocks::never).strength(-1.0f).sounds(BlockSoundGroup.GLASS).luminance((state) -> 11)));
        holystone = register("holystone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).strength(0.5f, 10.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        holystone_brick = register("holystone_brick", new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).materialColor(MaterialColor.WHITE)), buildingBlock());
        mossy_holystone = register("mossy_holystone", new Block(FabricBlockSettings.copyOf(Blocks.MOSSY_COBBLESTONE).materialColor(MaterialColor.WHITE)), buildingBlock());
        zanite_ore = register("zanite_ore", new Block(FabricBlockSettings.copyOf(Blocks.IRON_ORE)), buildingBlock());
//        crystal_sapling = register("crystal_sapling", null);
        aerogel = register("aerogel", new Block(FabricBlockSettings.of(Material.SOIL).strength(1.0f, 2000.0f).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        aerogel_slab = register("aerogel_slab", new AetherSlabBlock(aerogel.getDefaultState()), buildingBlock());
        aerogel_stairs = register("aerogel_stairs", new AetherStairsBlock(aerogel.getDefaultState()), buildingBlock());
        aerogel_wall = register("aerogel_wall", new AetherWallBlock(aerogel.getDefaultState()), buildingBlock());
        aether_farmland = register("aether_farmland", new AetherFarmlandBlock(FabricBlockSettings.of(Material.SOIL).ticksRandomly().strength(0.6f).sounds(BlockSoundGroup.GRAVEL).blockVision(AetherBlocks::always).suffocates(AetherBlocks::always)), buildingBlock());
        aether_grass_path = register("aether_grass_path", new AetherGrassPathBlock(), buildingBlock());
        ambrosium_torch = register("ambrosium_torch", new AmbrosiumTorchBlock(), false, buildingBlock());
        ambrosium_torch_wall = register("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(), ambrosium_torch, buildingBlock());
        angelic_stone = register("angelic_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        angelic_cracked_stone = register("angelic_stone_cracked", new Block(FabricBlockSettings.copyOf(angelic_stone)), buildingBlock());
        angelic_slab = register("angelic_slab", new AetherSlabBlock(angelic_stone.getDefaultState()), buildingBlock());
        angelic_stairs = register("angelic_stairs", new AetherStairsBlock(angelic_stone.getDefaultState()), buildingBlock());
        angelic_stone_trap = register("angelic_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        angelic_wall = register("angelic_wall", new AetherWallBlock(angelic_stone.getDefaultState()), buildingBlock());
        blueberry_bush = register("blueberry_bush", new BlueberryBushBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).collidable(false)), buildingBlock());
//        berry_bush_stem = register("berry_bush_stem", null);
//        black_dyed_aercloud = register("black_dyed_aercloud", null);
//        blue_aercloud = register("blue_aercloud", null);
//        blue_dyed_aercloud = register("blue_dyed_aercloud", null);
//        brown_dyed_aercloud = register("brown_dyed_aercloud", null);
        carved_stone = register("carved_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        carved_stone_trap = register("carved_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        carved_slab = register("carved_slab", new AetherSlabBlock(carved_stone.getDefaultState()), buildingBlock());
        carved_stairs = register("carved_stairs", new AetherStairsBlock(carved_stone.getDefaultState()), buildingBlock());
        carved_wall = register("carved_wall", new AetherWallBlock(carved_stone.getDefaultState()), buildingBlock());
//        chest_mimic = register("chest_mimic", null);
//        cold_aercloud = register("cold_aercloud", null);
//        crystal_fruit_leaves = register("crystal_fruit_leaves", null);
//        crystal_leaves = register("crystal_leaves", null);
//        cyan_dyed_aercloud = register("cyan_dyed_aercloud", null);
//        decorated_holiday_leaves = register("decorated_holiday_leaves", null);
//        enchanted_gravitite = register("enchanted_gravitite", null);
//        enchanter = register("enchanter", null);
//        freezer = register("freezer", null);
//        golden_aercloud = register("golden_aercloud", null);
//        golden_oak_leaves = register("golden_oak_leaves", null);
//        golden_oak_log = register("golden_oak_log", null);
//        golden_oak_sapling = register("golden_oak_sapling", null);
//        golden_oak_wood = register("golden_oak_wood", null);
//        gravitite_ore = register("gravitite_ore", null);
//        green_dyed_aercloud = register("green_dyed_aercloud", null);
//        grey_dyed_aercloud = register("grey_dyed_aercloud", null);
        hellfire_stone = register("hellfire_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        hellfire_cracked_stone = register("hellfire_stone_cracked", new Block(FabricBlockSettings.copyOf(hellfire_stone)), buildingBlock());
        hellfire_stone_trap = register("hellfire_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        hellfire_wall = register("hellfire_wall", new AetherWallBlock(hellfire_stone.getDefaultState()), buildingBlock());
        hellfire_slab = register("hellfire_slab", new AetherSlabBlock(hellfire_stone.getDefaultState()), buildingBlock());
        hellfire_stairs = register("hellfire_stairs", new AetherStairsBlock(hellfire_stone.getDefaultState()), buildingBlock());
//        holiday_leaves = register("holiday_leaves", null);
        holystone_brick_slab = register("holystone_brick_slab", new AetherSlabBlock(holystone_brick.getDefaultState()), buildingBlock());
        holystone_brick_stairs = register("holystone_brick_stairs", new AetherStairsBlock(holystone_brick.getDefaultState()), buildingBlock());
        holystone_brick_wall = register("holystone_brick_wall", new AetherWallBlock(holystone_brick.getDefaultState()), buildingBlock());
        holystone_slab = register("holystone_slab", new AetherSlabBlock(holystone.getDefaultState()), buildingBlock());
        holystone_stairs = register("holystone_stairs", new AetherStairsBlock(holystone.getDefaultState()), buildingBlock());
        holystone_wall = register("holystone_wall", new AetherWallBlock(holystone.getDefaultState()), buildingBlock());
//        icestone = register("icestone", null);
//        icestone_slab = register("icestone_slab", null);
//        icestone_stairs = register("icestone_stairs", null);
//        icestone_wall = register("icestone_wall", null);
//        incubator = register("incubator", null);
        light_angelic_stone = register("light_angelic_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).luminance(11).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        light_angelic_stone_trap = register("light_angelic_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).luminance(11).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        light_angelic_slab = register("light_angelic_slab", new AetherSlabBlock(light_angelic_stone.getDefaultState()), buildingBlock());
        light_angelic_stairs = register("light_angelic_stairs", new AetherStairsBlock(light_angelic_stone.getDefaultState()), buildingBlock());
        light_angelic_wall = register("light_angelic_wall", new AetherWallBlock(light_angelic_stone.getDefaultState()), buildingBlock());
//        light_blue_dyed_aercloud = register("light_blue_dyed_aercloud", null);
//        light_grey_dyed_aercloud = register("light_grey_dyed_aercloud", null);
        light_hellfire_stone = register("light_hellfire_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        light_hellfire_stone_trap = register("light_hellfire_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        light_hellfire_slab = register("light_hellfire_slab", new AetherSlabBlock(light_hellfire_stone.getDefaultState()), buildingBlock());
        light_hellfire_stairs = register("light_hellfire_stairs", new AetherStairsBlock(light_hellfire_stone.getDefaultState()), buildingBlock());
        light_hellfire_wall = register("light_hellfire_wall", new AetherWallBlock(light_hellfire_stone.getDefaultState()), buildingBlock());
//        lime_dyed_aercloud = register("lime_dyed_aercloud", null);
//        locked_angelic_stone = register("locked_angelic_stone", null);
//        locked_carved_stone = register("locked_carved_stone", null);
//        locked_hellfire_stone = register("locked_hellfire_stone", null);
//        locked_light_angelic_stone = register("locked_light_angelic_stone", null);
//        locked_light_hellfire_stone = register("locked_light_hellfire_stone", null);
//        locked_sentry_stone = register("locked_sentry_stone", null);
//        magenta_dyed_aercloud = register("magenta_dyed_aercloud", null);
        mossy_holystone_slab = register("mossy_holystone_slab", new AetherSlabBlock(mossy_holystone.getDefaultState()), buildingBlock());
        mossy_holystone_stairs = register("mossy_holystone_stairs", new AetherStairsBlock(mossy_holystone.getDefaultState()), buildingBlock());
        mossy_holystone_wall = register("mossy_holystone_wall", new AetherWallBlock(mossy_holystone.getDefaultState()), buildingBlock());
//        orange_dyed_aercloud = register("orange_dyed_aercloud", null);
//        pillar = register("pillar", null);
//        pillar_top = register("pillar_top", null);
//        pink_aercloud = register("pink_aercloud", null);
//        pink_dyed_aercloud = register("pink_dyed_aercloud", null);
//        potted_purple_flower = register("potted_purple_flower", null);
//        potted_white_flower = register("potted_white_flower", null);
//        present = register("present", null);
//        purple_dyed_aercloud = register("purple_dyed_aercloud", null);
//        purple_flower = register("purple_flower", null);
        quicksoil = register("quicksoil", new Block(FabricBlockSettings.of(Material.AGGREGATE).strength(0.5f, -1.0f).sounds(BlockSoundGroup.SAND)), buildingBlock());
        quicksoil_glass = register("quicksoil_glass", new Block(FabricBlockSettings.of(Material.GLASS).luminance(14).strength(0.2f, -1.0f).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        quicksoil_glass_pane = register("quicksoil_glass_pane", new QuicksoilGlassPaneBlock(), buildingBlock());
//        red_dyed_aercloud = register("red_dyed_aercloud", null);
        sentry_stone = register("sentry_stone", new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE)), buildingBlock());
        sentry_cracked_stone = register("sentry_stone_cracked", new Block(FabricBlockSettings.copyOf(sentry_stone)), buildingBlock());
        light_sentry_stone = register("light_sentry_stone", new Block(FabricBlockSettings.copyOf(sentry_stone).luminance(ignored -> 10)), buildingBlock());
        sentry_stone_trap = register("sentry_stone_trap", new Block(FabricBlockSettings.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        sentry_slab = register("sentry_slab", new AetherSlabBlock(sentry_stone.getDefaultState()), buildingBlock());
        sentry_stairs = register("sentry_stairs", new AetherStairsBlock(sentry_stone.getDefaultState()), buildingBlock());
        sentry_wall = register("sentry_wall", new AetherWallBlock(sentry_stone.getDefaultState()), buildingBlock());
        skyroot_sapling = register("skyroot_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), buildingBlock());
        skyroot_log = register("skyroot_log", createLogBlock(MaterialColor.GREEN, MaterialColor.WOOD), buildingBlock());
        stripped_skyroot_log = register("stripped_skyroot_log", createLogBlock(MaterialColor.WOOD, MaterialColor.WOOD), buildingBlock());
        skyroot_leaves = register("skyroot_leaves", createLeavesBlock(), buildingBlock());
        skyroot_planks = register("skyroot_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)), buildingBlock());
        skyroot_bookshelf = register("skyroot_bookshelf", new Block(FabricBlockSettings.of(Material.WOOD).strength(1.5f).sounds(BlockSoundGroup.WOOD)), buildingBlock());
        skyroot_fence = register("skyroot_fence", new FenceBlock(FabricBlockSettings.copy(skyroot_planks)), buildingBlock());
        skyroot_fence_gate = register("skyroot_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(AetherBlocks.skyroot_planks)), buildingBlock());
        skyroot_slab = register("skyroot_slab", new AetherSlabBlock(skyroot_planks.getDefaultState()), buildingBlock());
        skyroot_stairs = register("skyroot_stairs", new AetherStairsBlock(skyroot_planks.getDefaultState()), buildingBlock());
        golden_oak_sapling = register("golden_oak_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), buildingBlock());
        golden_oak_log = register("golden_oak_log", createLogBlock(MaterialColor.WOOD, MaterialColor.RED), buildingBlock());
        stripped_golden_oak_log = register("stripped_golden_oak_log", createLogBlock(MaterialColor.RED, MaterialColor.RED), buildingBlock());
        golden_oak_leaves = register("golden_oak_leaves", createLeavesBlock(), buildingBlock());
        golden_oak_planks = register("golden_oak_planks", new Block(FabricBlockSettings.copyOf(skyroot_planks).materialColor(MaterialColor.RED)), buildingBlock());
        golden_oak_fence = register("golden_oak_fence", new FenceBlock(FabricBlockSettings.copyOf(golden_oak_planks)), buildingBlock());
        golden_oak_fence_gate = register("golden_oak_fence_gate", new FenceGateBlock(FabricBlockSettings.copyOf(golden_oak_planks)), buildingBlock());
        golden_oak_slab = register("golden_oak_slab", new AetherSlabBlock(golden_oak_planks.getDefaultState()), buildingBlock());
        golden_oak_stairs = register("golden_oak_stairs", new AetherStairsBlock(golden_oak_planks.getDefaultState()), buildingBlock());
        crystal_sapling = register("crystal_sapling", new AetherSaplingBlock(null, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING).sounds(BlockSoundGroup.GLASS)), buildingBlock());
        crystal_log = register("crystal_log", createLogBlock(MaterialColor.GRAY, MaterialColor.LIGHT_GRAY), buildingBlock());
        stripped_crystal_log = register("stripped_crystal_log", createLogBlock(MaterialColor.LIGHT_GRAY, MaterialColor.LIGHT_GRAY), buildingBlock());
        crystal_leaves = register("crystal_leaves", createLeavesBlock(BlockSoundGroup.GLASS), buildingBlock());
        crystal_planks = register("crystal_planks", new Block(FabricBlockSettings.copyOf(skyroot_planks).materialColor(MaterialColor.LIGHT_GRAY)), buildingBlock());
        crystal_fence = register("crystal_fence", new FenceBlock(FabricBlockSettings.copyOf(crystal_planks)), buildingBlock());
        crystal_fence_gate = register("crystal_fence_gate", new FenceGateBlock(FabricBlockSettings.copyOf(crystal_planks)), buildingBlock());
        crystal_slab = register("crystal_slab", new AetherSlabBlock(crystal_planks.getDefaultState()), buildingBlock());
        crystal_stairs = register("crystal_stairs", new AetherStairsBlock(crystal_planks.getDefaultState()), buildingBlock());
//        stripped_skyroot_log = register("stripped_skyroot_log", null);
//        stripped_skyroot_wood = register("stripped_skyroot_wood", null);
//        sun_altar = register("sun_altar", null);
//        treasure_chest = register("treasure_chest", null);
//        white_dyed_aercloud = register("white_dyed_aercloud", null);
//        white_flower = register("white_flower", null);
//        yellow_dyed_aercloud = register("yellow_dyed_aercloud", null);
        zanite_block = register("zanite_block", new Block(FabricBlockSettings.of(Material.METAL).strength(3.0f, -1.0f).sounds(BlockSoundGroup.METAL)), buildingBlock());
    }

    private static Item.Settings buildingBlock() {
        return new FabricItemSettings().group(AetherItemGroups.Blocks);
    }

    private static Block register(String id, Block block, Item.Settings settings) {
        return register(id, block, true, settings);
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

    private static PillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    }

    private static LeavesBlock createLeavesBlock() {
        return new LeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never));
    }

    private static LeavesBlock createLeavesBlock(BlockSoundGroup sounds) {
        return new LeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2f).ticksRandomly().sounds(sounds).nonOpaque().allowsSpawning((a, b, c, d) -> false).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never));
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(blue_portal, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blueberry_bush, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blueberry_bush, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(quicksoil_glass, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(quicksoil_glass, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(quicksoil_glass_pane, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(quicksoil_glass_pane, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(aerogel, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(aerogel_slab, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(aerogel_stairs, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(aerogel_wall, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ambrosium_torch, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ambrosium_torch_wall, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(skyroot_sapling, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(golden_oak_sapling, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(crystal_sapling, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(skyroot_leaves, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(golden_oak_leaves, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(crystal_leaves, RenderLayer.getTranslucent());
    }

    public static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

    public static boolean always(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return true;
    }
}
