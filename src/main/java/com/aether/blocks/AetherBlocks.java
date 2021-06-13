package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.aercloud.*;
import com.aether.blocks.decorative.*;
import com.aether.blocks.natural.*;
import com.aether.client.rendering.block.FluidRenderSetup;
import com.aether.entities.util.RenderUtils;
import com.aether.items.AetherItemGroups;
import com.aether.world.feature.tree.*;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

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
    public static final Block SKYROOT_PLANKS;
    public static final Block SKYROOT_SLAB;
    public static final Block SKYROOT_STAIRS;
    public static final Block SKYROOT_SAPLING;
    public static final Block SKYROOT_TRAPDOOR;
    public static final Block POTTED_SKYROOT_SAPLING;
    public static final Block SKYROOT_LEAVES;
    public static final Block SKYROOT_LEAF_PILE;
    public static final Block STRIPPED_SKYROOT_LOG;
    public static final Block GOLDEN_OAK_SAPLING;
    public static final Block POTTED_GOLDEN_OAK_SAPLING;
    public static final Block GOLDEN_OAK_LOG;
    public static final Block STRIPPED_GOLDEN_OAK_LOG;
    public static final Block GOLDEN_OAK_LEAVES;
    public static final Block GOLDEN_OAK_PLANKS;
    public static final Block GOLDEN_OAK_FENCE;
    public static final Block GOLDEN_OAK_FENCE_GATE;
    public static final Block GOLDEN_OAK_SLAB;
    public static final Block GOLDEN_OAK_STAIRS;
    public static final Block GOLDEN_OAK_DOOR;
    public static final Block GOLDEN_OAK_TRAPDOOR;
    public static final Block CRYSTAL_SAPLING;
    public static final Block POTTED_CRYSTAL_SAPLING;
    public static final Block CRYSTAL_LOG;
    public static final Block STRIPPED_CRYSTAL_LOG;
    public static final Block CRYSTAL_LEAVES;
    public static final Block CRYSTAL_PLANKS;
    public static final Block CRYSTAL_FENCE;
    public static final Block CRYSTAL_FENCE_GATE;
    public static final Block CRYSTAL_SLAB;
    public static final Block CRYSTAL_STAIRS;
    public static final Block CRYSTAL_DOOR;
    public static final Block CRYSTAL_TRAPDOOR;
    //    public static final Block SKYROOT_WOOD;
//    public static final Block STRIPPED_SKYROOT_WOOD;
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
    public static final Block STRIPPED_WISTERIA_LOG;
    public static final Block WISTERIA_PLANKS;
    public static final Block WISTERIA_SLAB;
    public static final Block WISTERIA_STAIRS;
    public static final Block WISTERIA_FENCE;
    public static final Block WISTERIA_FENCE_GATE;
    public static final Block WISTERIA_DOOR;
    public static final Block WISTERIA_TRAPDOOR;
    //    public static final Block YELLOW_DYED_AERCLOUD;
    public static final Block ZANITE_BLOCK;
    public static final Block ZANITE_ORE;
    public static final Block BLUEBERRY_BUSH;
    public static final FlowingFluid DENSE_AERCLOUD_STILL;
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
    //public static final Block SPORECAP_PINK;
    //public static final Block SPORECAP_BROWN;
    //public static final Block ROOTCAP;
    //public static final Block CINNABAR_ROSE;
    public static final Block ZANITE_CHAIN;
    public static final Block AMBROSIUM_LANTERN;



    static {
        AETHER_DIRT = register("aether_dirt", new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.3f).instabreak().sound(SoundType.GRAVEL)), buildingBlock());

        // We're going to be using this more than once, so we might as well store it in a variable.
        final BlockBehaviour.Properties GRASS_BLOCKS = BlockBehaviour.Properties.of(Material.GRASS).instabreak().color(MaterialColor.COLOR_CYAN).strength(0.4f).randomTicks().sound(SoundType.GRASS);
        AETHER_GRASS_BLOCK = register("aether_grass", new AetherGrassBlock(GRASS_BLOCKS), buildingBlock());

        // Note that because we're changing the material color (which mutates the `BlockSettings`), we want to
        // make a copy of the settings first.

        // Copying block settings is okay in this case, but copying the settings of another block is *not* something
        // you should do unless the new block is *directly related* to the block you're copying from. If the blocks just
        // happen to have several things in common, you should make a new settings object from scratch instead.
        AETHER_ENCHANTED_GRASS = register("enchanted_aether_grass", new EnchantedAetherGrassBlock(GRASS_BLOCKS.color(MaterialColor.GOLD)), buildingBlock());

        final BlockBehaviour.Properties GRASS = BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS);
        AETHER_GRASS = register("aether_grass_plant", new AetherBrushBlock(GRASS), buildingBlock());
        AETHER_TALL_GRASS = register("aether_tall_grass", new DoublePlantBlock(GRASS), buildingBlock());
        AETHER_FERN = register("aether_fern", new AetherBrushBlock(GRASS), buildingBlock());
        POTTED_AETHER_FERN = register("potted_aether_fern", createPottedBlock(AETHER_FERN));
        AETHER_BUSH = register("aether_bush", new AetherBrushBlock(GRASS), buildingBlock());
        AMBROSIUM_ORE = register("ambrosium_ore", new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), buildingBlock());
        BLUE_PORTAL = register("blue_portal", new AetherPortalBlock(BlockBehaviour.Properties.of(Material.PORTAL, MaterialColor.COLOR_BLUE).noCollission().randomTicks().noOcclusion().isViewBlocking(AetherBlocks::never).strength(-1.0f).sound(SoundType.GLASS).lightLevel((state) -> 11)));
        HOLYSTONE = register("holystone", new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(0.5f, 10.0f).sound(SoundType.STONE)), buildingBlock());
        COBBLED_HOLYSTONE = register("cobbled_holystone", new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(0.4f, 8.0f).sound(SoundType.STONE)), buildingBlock());
        HOLYSTONE_BRICK = register("holystone_brick", new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).color(MaterialColor.SNOW)), buildingBlock());
        MOSSY_HOLYSTONE = register("mossy_holystone", new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).color(MaterialColor.SNOW)), buildingBlock());
        GOLDEN_MOSSY_HOLYSTONE = register("golden_mossy_holystone", new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).color(MaterialColor.SNOW)), buildingBlock());
        ZANITE_ORE = register("zanite_ore", new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), buildingBlock());
        AEROGEL = register("aerogel", new Block(BlockBehaviour.Properties.of(Material.DIRT).strength(1.0f, 2000.0f).sound(SoundType.GLASS).isRedstoneConductor(AetherBlocks::never).noOcclusion()), buildingBlock());
        AETHER_FARMLAND = register("aether_farmland", new AetherFarmlandBlock(BlockBehaviour.Properties.of(Material.DIRT).randomTicks().strength(0.6f).sound(SoundType.GRAVEL).isViewBlocking(AetherBlocks::always).isSuffocating(AetherBlocks::always)), buildingBlock());
        AETHER_DIRT_PATH = register("aether_grass_path", new AetherDirtPathBlock(), buildingBlock());
        AMBROSIUM_TORCH = register("ambrosium_torch",  new AmbrosiumTorchBlock(), false, buildingBlock());
        AMBROSIUM_TORCH_WALL = register("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(), false, buildingBlock());
        Registry.register(Registry.ITEM, Aether.locate("ambrosium_torch"), new StandingAndWallBlockItem(AMBROSIUM_TORCH, AMBROSIUM_TORCH_WALL, buildingBlock()));

        final BlockBehaviour.Properties ANGELIC_STONES = BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).explosionResistance(1.0f).sound(SoundType.STONE);
        ANGELIC_STONE = register("angelic_stone", new Block(ANGELIC_STONES), buildingBlock());
        ANGELIC_CRACKED_STONE = register("angelic_stone_cracked", new Block(ANGELIC_STONES), buildingBlock());
        ANGELIC_SLAB = register("angelic_slab", new AetherSlabBlock(ANGELIC_STONE.defaultBlockState()), buildingBlock());
        ANGELIC_STAIRS = register("angelic_stairs", new AetherStairsBlock(ANGELIC_STONE.defaultBlockState()), buildingBlock());
//        ANGELIC_STONE_TRAP = register("angelic_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).hardness(-1.0f).resistance(6000000.0f).sounds(BlockSoundGroup.STONE)));
        ANGELIC_WALL = register("angelic_wall", new AetherWallBlock(ANGELIC_STONE.defaultBlockState()), buildingBlock());
        BLUEBERRY_BUSH = register("blueberry_bush", new BlueberryBushBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(AetherBlocks::canSpawnOnLeaves).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never).noCollission()), buildingBlock());
//        BLACK_DYED_AERCLOUD = register("black_dyed_aercloud", null);
//        BLUE_DYED_AERCLOUD = register("blue_dyed_aercloud", null);
//        BROWN_DYED_AERCLOUD = register("brown_dyed_aercloud", null);
        CARVED_STONE = register("carved_stone", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).explosionResistance(1.0f).sound(SoundType.STONE)), buildingBlock());
        CARVED_STONE_TRAP = register("carved_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        CARVED_SLAB = register("carved_slab", new AetherSlabBlock(CARVED_STONE.defaultBlockState()), buildingBlock());
        CARVED_STAIRS = register("carved_stairs", new AetherStairsBlock(CARVED_STONE.defaultBlockState()), buildingBlock());
        CARVED_WALL = register("carved_wall", new AetherWallBlock(CARVED_STONE.defaultBlockState()), buildingBlock());
//        CHEST_MIMIC = register("chest_mimic", null);
        COLD_AERCLOUD = register("cold_aercloud", new BaseAercloudBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.SNOW).strength(0.2F).sound(SoundType.WOOL).noOcclusion()), buildingBlock());
        BLUE_AERCLOUD = register("blue_aercloud", new BlueAercloudBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_LIGHT_BLUE).strength(0.2F).sound(SoundType.WOOL).noOcclusion()), buildingBlock());
        PINK_AERCLOUD = register("pink_aercloud", new PinkAercloudBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_PINK).strength(0.2F).sound(SoundType.WOOL).noOcclusion()), buildingBlock());
//        CYAN_DYED_AERCLOUD = register("cyan_dyed_aercloud", null);
//        DECORATED_HOLIDAY_LEAVES = register("decorated_holiday_leaves", null);
//        ENCHANTED_GRAVITITE = register("enchanted_gravitite", null);
//        ENCHANTER = register("enchanter", null);
//        FREEZER = register("freezer", null);
        GOLDEN_AERCLOUD = register("golden_aercloud", new GoldenAercloudBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_YELLOW).strength(0.2F).sound(SoundType.WOOL).noOcclusion()), buildingBlock());
        DENSE_AERCLOUD_STILL = Registry.register(Registry.FLUID, Aether.locate("dense_aercloud"), new DenseAercloudFluid());
        DENSE_AERCLOUD = register("dense_aercloud", new LiquidBlock(DENSE_AERCLOUD_STILL, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()) {
        });
        GRAVITITE_ORE = register("gravitite_ore", new FloatingBlock(false, BlockBehaviour.Properties.of(Material.STONE).strength(5.0F).sound(SoundType.STONE), UniformInt.of(0, 2)), buildingBlock());
        GRAVITITE_LEVITATOR = register("gravitite_levitator", new FloatingBlock(true, BlockBehaviour.Properties.of(Material.WOOD).strength(3.0F, 3.0F).sound(SoundType.WOOD)), buildingBlock());
//        GREEN_DYED_AERCLOUD = register("green_dyed_aercloud", null);
//        GREY_DYED_AERCLOUD = register("grey_dyed_aercloud", null);

        final BlockBehaviour.Properties HELLFIRE_STONES = BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).explosionResistance(1.0f).sound(SoundType.STONE);
        HELLFIRE_STONE = register("hellfire_stone", new Block(HELLFIRE_STONES), buildingBlock());
        HELLFIRE_CRACKED_STONE = register("hellfire_stone_cracked", new Block(HELLFIRE_STONES), buildingBlock());
        HELLFIRE_STONE_TRAP = register("hellfire_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        HELLFIRE_WALL = register("hellfire_wall", new AetherWallBlock(HELLFIRE_STONE.defaultBlockState()), buildingBlock());
        HELLFIRE_SLAB = register("hellfire_slab", new AetherSlabBlock(HELLFIRE_STONE.defaultBlockState()), buildingBlock());
        HELLFIRE_STAIRS = register("hellfire_stairs", new AetherStairsBlock(HELLFIRE_STONE.defaultBlockState()), buildingBlock());
//        HOLIDAY_LEAVES = register("holiday_leaves", null);
        HOLYSTONE_BRICK_SLAB = register("holystone_brick_slab", new AetherSlabBlock(HOLYSTONE_BRICK.defaultBlockState()), buildingBlock());
        HOLYSTONE_BRICK_STAIRS = register("holystone_brick_stairs", new AetherStairsBlock(HOLYSTONE_BRICK.defaultBlockState()), buildingBlock());
        HOLYSTONE_BRICK_WALL = register("holystone_brick_wall", new AetherWallBlock(HOLYSTONE_BRICK.defaultBlockState()), buildingBlock());
        HOLYSTONE_SLAB = register("holystone_slab", new AetherSlabBlock(HOLYSTONE.defaultBlockState()), buildingBlock());
        HOLYSTONE_STAIRS = register("holystone_stairs", new AetherStairsBlock(HOLYSTONE.defaultBlockState()), buildingBlock());
        HOLYSTONE_WALL = register("holystone_wall", new AetherWallBlock(HOLYSTONE.defaultBlockState()), buildingBlock());
//        ICESTONE = register("icestone", null);
//        ICESTONE_SLAB = register("icestone_slab", null);
//        ICESTONE_STAIRS = register("icestone_stairs", null);
//        ICESTONE_WALL = register("icestone_wall", null);
//        INCUBATOR = register("incubator", null);
        LIGHT_ANGELIC_STONE = register("light_angelic_stone", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).lightLevel(state -> 11).explosionResistance(1.0f).sound(SoundType.STONE)), buildingBlock());
        LIGHT_ANGELIC_STONE_TRAP = register("light_angelic_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).lightLevel(state -> 11).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        LIGHT_ANGELIC_SLAB = register("light_angelic_slab", new AetherSlabBlock(LIGHT_ANGELIC_STONE.defaultBlockState()), buildingBlock());
        LIGHT_ANGELIC_STAIRS = register("light_angelic_stairs", new AetherStairsBlock(LIGHT_ANGELIC_STONE.defaultBlockState()), buildingBlock());
        LIGHT_ANGELIC_WALL = register("light_angelic_wall", new AetherWallBlock(LIGHT_ANGELIC_STONE.defaultBlockState()), buildingBlock());
        LIGHT_CARVED_STONE = register("light_carved_stone", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).lightLevel(state -> 11).explosionResistance(1.0f).sound(SoundType.STONE)), buildingBlock());
        LIGHT_CARVED_STONE_TRAP = register("light_carved_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).lightLevel(state -> 11).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        LIGHT_CARVED_SLAB = register("light_carved_slab", new AetherSlabBlock(LIGHT_CARVED_STONE.defaultBlockState()), buildingBlock());
        LIGHT_CARVED_STAIRS = register("light_carved_stairs", new AetherStairsBlock(LIGHT_CARVED_STONE.defaultBlockState()), buildingBlock());
        LIGHT_CARVED_WALL = register("light_carved_wall", new AetherWallBlock(LIGHT_CARVED_STONE.defaultBlockState()), buildingBlock());
//        LIGHT_BLUE_DYED_AERCLOUD = register("light_blue_dyed_aercloud", null);
//        LIGHT_GREY_DYED_AERCLOUD = register("light_grey_dyed_aercloud", null);
        LIGHT_HELLFIRE_STONE = register("light_hellfire_stone", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).explosionResistance(1.0f).sound(SoundType.STONE)), buildingBlock());
        LIGHT_HELLFIRE_STONE_TRAP = register("light_hellfire_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        LIGHT_HELLFIRE_SLAB = register("light_hellfire_slab", new AetherSlabBlock(LIGHT_HELLFIRE_STONE.defaultBlockState()), buildingBlock());
        LIGHT_HELLFIRE_STAIRS = register("light_hellfire_stairs", new AetherStairsBlock(LIGHT_HELLFIRE_STONE.defaultBlockState()), buildingBlock());
        LIGHT_HELLFIRE_WALL = register("light_hellfire_wall", new AetherWallBlock(LIGHT_HELLFIRE_STONE.defaultBlockState()), buildingBlock());
//        LIME_DYED_AERCLOUD = register("lime_dyed_aercloud", null);
//        LOCKED_ANGELIC_STONE = register("locked_angelic_stone", null);
//        LOCKED_CARVED_STONE = register("locked_carved_stone", null);
//        LOCKED_HELLFIRE_STONE = register("locked_hellfire_stone", null);
//        LOCKED_LIGHT_ANGELIC_STONE = register("locked_light_angelic_stone", null);
//        LOCKED_LIGHT_HELLFIRE_STONE = register("locked_light_hellfire_stone", null);
//        LOCKED_SENTRY_STONE = register("locked_sentry_stone", null);
//        MAGENTA_DYED_AERCLOUD = register("magenta_dyed_aercloud", null);
        COBBLED_HOLYSTONE_SLAB = register("cobbled_holystone_slab", new AetherSlabBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
        COBBLED_HOLYSTONE_STAIRS = register("cobbled_holystone_stairs", new AetherStairsBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
        COBBLED_HOLYSTONE_WALL = register("cobbled_holystone_wall", new AetherWallBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
        MOSSY_HOLYSTONE_SLAB = register("mossy_holystone_slab", new AetherSlabBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
        MOSSY_HOLYSTONE_STAIRS = register("mossy_holystone_stairs", new AetherStairsBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
        MOSSY_HOLYSTONE_WALL = register("mossy_holystone_wall", new AetherWallBlock(MOSSY_HOLYSTONE.defaultBlockState()), buildingBlock());
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
        QUICKSOIL = register("quicksoil", new Block(BlockBehaviour.Properties.of(Material.SAND).strength(0.5f, -1.0f).friction(1.0F).speedFactor(1.102F).sound(SoundType.SAND)), buildingBlock());
        QUICKSOIL_GLASS = register("quicksoil_glass", new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).lightLevel(state -> 14).strength(0.2f, -1.0f).sound(SoundType.GLASS).noOcclusion().isRedstoneConductor(AetherBlocks::never)), buildingBlock());
        QUICKSOIL_GLASS_PANE = register("quicksoil_glass_pane", new QuicksoilGlassPaneBlock(), buildingBlock());
//        RED_DYED_AERCLOUD = register("red_dyed_aercloud", null);
        FLUTEGRASS = register("flutegrass", new AetherBrushBlock(GRASS.color(MaterialColor.GOLD), ImmutableSet.of(QUICKSOIL), true), buildingBlock());
        final BlockBehaviour.Properties SENTRY_STONES = BlockBehaviour.Properties.of(Material.STONE).destroyTime(0.5f).explosionResistance(1.0f).sound(SoundType.STONE);
        SENTRY_STONE = register("sentry_stone", new Block(SENTRY_STONES), buildingBlock());
        SENTRY_CRACKED_STONE = register("sentry_stone_cracked", new Block(SENTRY_STONES), buildingBlock());
        LIGHT_SENTRY_STONE = register("light_sentry_stone", new Block(SENTRY_STONES.lightLevel(ignored -> 10)), buildingBlock());
        SENTRY_STONE_TRAP = register("sentry_stone_trap", new Block(BlockBehaviour.Properties.of(Material.STONE).destroyTime(-1.0f).explosionResistance(6000000.0f).sound(SoundType.STONE)));
        SENTRY_SLAB = register("sentry_slab", new AetherSlabBlock(SENTRY_STONE.defaultBlockState()), buildingBlock());
        SENTRY_STAIRS = register("sentry_stairs", new AetherStairsBlock(SENTRY_STONE.defaultBlockState()), buildingBlock());
        SENTRY_WALL = register("sentry_wall", new AetherWallBlock(SENTRY_STONE.defaultBlockState()), buildingBlock());
        LIGHT_SENTRY_SLAB = register("light_sentry_slab", new AetherSlabBlock(LIGHT_SENTRY_STONE.defaultBlockState()), buildingBlock());
        LIGHT_SENTRY_STAIRS = register("light_sentry_stairs", new AetherStairsBlock(LIGHT_SENTRY_STONE.defaultBlockState()), buildingBlock());
        LIGHT_SENTRY_WALL = register("light_sentry_wall", new AetherWallBlock(LIGHT_SENTRY_STONE.defaultBlockState()), buildingBlock());

        final BlockBehaviour.Properties SAPLINGS = BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
        SKYROOT_SAPLING = register("skyroot_sapling", new AetherSaplingBlock(new SkyrootSaplingGenerator(), SAPLINGS), buildingBlock());
        POTTED_SKYROOT_SAPLING = register("potted_skyroot_sapling", createPottedBlock(SKYROOT_SAPLING));
        SKYROOT_LOG = register("skyroot_log", createLogBlock(MaterialColor.COLOR_GREEN, MaterialColor.WOOD), buildingBlock());
        STRIPPED_SKYROOT_LOG = register("stripped_skyroot_log", createLogBlock(MaterialColor.WOOD, MaterialColor.WOOD), buildingBlock());
        SKYROOT_LEAVES = register("skyroot_leaves", createLeavesBlock(), buildingBlock());
        SKYROOT_LEAF_PILE = register("skyroot_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(SoundType.VINE)), buildingBlock());

        final BlockBehaviour.Properties SKYROOT_WOOD = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
        SKYROOT_PLANKS = register("skyroot_planks", new Block(SKYROOT_WOOD), buildingBlock());
        SKYROOT_BOOKSHELF = register("skyroot_bookshelf", new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5f).sound(SoundType.WOOD)), buildingBlock());
        SKYROOT_FENCE = register("skyroot_fence", new FenceBlock(SKYROOT_WOOD), buildingBlock());
        SKYROOT_FENCE_GATE = register("skyroot_fence_gate", new FenceGateBlock(SKYROOT_WOOD), buildingBlock());
        SKYROOT_SLAB = register("skyroot_slab", new AetherSlabBlock(SKYROOT_PLANKS.defaultBlockState()), buildingBlock());
        SKYROOT_STAIRS = register("skyroot_stairs", new AetherStairsBlock(SKYROOT_PLANKS.defaultBlockState()), buildingBlock());
        SKYROOT_TRAPDOOR = register("skyroot_trapdoor", new AetherTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        SKYROOT_DOOR = register("skyroot_door", new AetherDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)), buildingBlock());

        GOLDEN_OAK_SAPLING = register("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), SAPLINGS), buildingBlock());
        POTTED_GOLDEN_OAK_SAPLING = register("potted_golden_oak_sapling", createPottedBlock(GOLDEN_OAK_SAPLING));
        GOLDEN_OAK_LOG = register("golden_oak_log", createLogBlock(MaterialColor.WOOD, MaterialColor.COLOR_RED), buildingBlock());
        STRIPPED_GOLDEN_OAK_LOG = register("stripped_golden_oak_log", createLogBlock(MaterialColor.COLOR_RED, MaterialColor.COLOR_RED), buildingBlock());
        GOLDEN_OAK_LEAVES = register("golden_oak_leaves", createLeavesBlock(), buildingBlock());


        final BlockBehaviour.Properties GOLDEN_OAK_WOOD = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).strength(2.0F, 3.0F).sound(SoundType.WOOD);
        GOLDEN_OAK_PLANKS = register("golden_oak_planks", new Block(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_FENCE = register("golden_oak_fence", new FenceBlock(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_FENCE_GATE = register("golden_oak_fence_gate", new FenceGateBlock(GOLDEN_OAK_WOOD), buildingBlock());
        GOLDEN_OAK_SLAB = register("golden_oak_slab", new AetherSlabBlock(GOLDEN_OAK_PLANKS.defaultBlockState()), buildingBlock());
        GOLDEN_OAK_STAIRS = register("golden_oak_stairs", new AetherStairsBlock(GOLDEN_OAK_PLANKS.defaultBlockState()), buildingBlock());
        GOLDEN_OAK_TRAPDOOR = register("golden_oak_trapdoor", new AetherTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        GOLDEN_OAK_DOOR = register("golden_oak_door", new AetherDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)), buildingBlock());

        CRYSTAL_SAPLING = register("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), SAPLINGS.sound(SoundType.GLASS)), buildingBlock());
        POTTED_CRYSTAL_SAPLING = register("potted_crystal_sapling", createPottedBlock(CRYSTAL_SAPLING));
        CRYSTAL_LOG = register("crystal_log", createLogBlock(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_LIGHT_GRAY), buildingBlock());
        STRIPPED_CRYSTAL_LOG = register("stripped_crystal_log", createLogBlock(MaterialColor.COLOR_LIGHT_GRAY, MaterialColor.COLOR_LIGHT_GRAY), buildingBlock());
        CRYSTAL_LEAVES = register("crystal_leaves", createCrystalLeavesBlock(SoundType.GLASS), buildingBlock());
        CRYSTAL_TRAPDOOR = register("crystal_trapdoor", new AetherTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        CRYSTAL_DOOR = register("crystal_door", new AetherDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)), buildingBlock());

        final BlockBehaviour.Properties CRYSTAL_WOOD = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD);
        CRYSTAL_PLANKS = register("crystal_planks", new Block(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_FENCE = register("crystal_fence", new FenceBlock(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_FENCE_GATE = register("crystal_fence_gate", new FenceGateBlock(CRYSTAL_WOOD), buildingBlock());
        CRYSTAL_SLAB = register("crystal_slab", new AetherSlabBlock(CRYSTAL_PLANKS.defaultBlockState()), buildingBlock());
        CRYSTAL_STAIRS = register("crystal_stairs", new AetherStairsBlock(CRYSTAL_PLANKS.defaultBlockState()), buildingBlock());
        WISTERIA_LOG = register("wisteria_log", createLogBlock(MaterialColor.COLOR_YELLOW, MaterialColor.COLOR_RED), buildingBlock());
        STRIPPED_WISTERIA_LOG = register("stripped_wisteria_log", createLogBlock(MaterialColor.COLOR_RED, MaterialColor.COLOR_RED), buildingBlock());

        final BlockBehaviour.Properties ROSE_WISTERIA = BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_PINK).noCollission().strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never);
        ROSE_WISTERIA_LEAVES = register("rose_wisteria_leaves", new AetherLeavesBlock(ROSE_WISTERIA, false), buildingBlock());
        ROSE_WISTERIA_LEAF_PILE = register("rose_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(SoundType.VINE)), buildingBlock());
        ROSE_WISTERIA_SAPLING = register("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), SAPLINGS), buildingBlock());
        POTTED_ROSE_WISTERIA_SAPLING = register("potted_rose_wisteria_sapling", createPottedBlock(ROSE_WISTERIA_SAPLING));
        ROSE_WISTERIA_HANGER = register("rose_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(SoundType.GRASS)), buildingBlock());

        final BlockBehaviour.Properties FROST_WISTERIA = BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_LIGHT_BLUE).noCollission().strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never);
        FROST_WISTERIA_LEAVES = register("frost_wisteria_leaves", new AetherLeavesBlock(FROST_WISTERIA, false), buildingBlock());
        FROST_WISTERIA_LEAF_PILE = register("frost_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(SoundType.VINE)), buildingBlock());
        FROST_WISTERIA_SAPLING = register("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), SAPLINGS), buildingBlock());
        POTTED_FROST_WISTERIA_SAPLING = register("potted_frost_wisteria_sapling", createPottedBlock(FROST_WISTERIA_SAPLING));
        FROST_WISTERIA_HANGER = register("frost_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(SoundType.GRASS)), buildingBlock());

        final BlockBehaviour.Properties LAVENDER_WISTERIA = BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_MAGENTA).noCollission().strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never);
        LAVENDER_WISTERIA_LEAVES = register("lavender_wisteria_leaves", new AetherLeavesBlock(LAVENDER_WISTERIA, false), buildingBlock());
        LAVENDER_WISTERIA_LEAF_PILE = register("lavender_wisteria_leaf_pile", new AetherLeafPileBlock(createLeafPileBlock(SoundType.VINE)), buildingBlock());
        LAVENDER_WISTERIA_SAPLING = register("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), SAPLINGS), buildingBlock());
        POTTED_LAVENDER_WISTERIA_SAPLING = register("potted_lavender_wisteria_sapling", createPottedBlock(LAVENDER_WISTERIA_SAPLING));
        LAVENDER_WISTERIA_HANGER = register("lavender_wisteria_hanger", new AetherHangerBlock(createWisteriaHangerBlock(SoundType.GRASS)), buildingBlock());

        final Vec3i[] BOREAL_COLORS = new Vec3i[]{ RenderUtils.toRGB(0x59CDFF), RenderUtils.toRGB(0x3affcb), RenderUtils.toRGB(0x599CFF), RenderUtils.toRGB(0x8158FE) };
        final BlockBehaviour.Properties BOREAL_WISTERIA = BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_CYAN).noCollission().strength(0.05F).sound(SoundType.GLASS).lightLevel(state -> 7).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never).emissiveRendering(AetherBlocks::always).hasPostProcess(AetherBlocks::always);
        BOREAL_WISTERIA_LEAVES = register("boreal_wisteria_leaves", new AuralLeavesBlock(BOREAL_WISTERIA, false, BOREAL_COLORS), buildingBlock());
        BOREAL_WISTERIA_SAPLING = register("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), SAPLINGS.lightLevel(state -> 7)), buildingBlock());
        POTTED_BOREAL_WISTERIA_SAPLING = register("potted_boreal_wisteria_sapling", createPottedBlock(BOREAL_WISTERIA_SAPLING));
        BOREAL_WISTERIA_HANGER = register("boreal_wisteria_hanger", new AuralHangerBlock(BOREAL_WISTERIA, BOREAL_COLORS), buildingBlock());

        final BlockBehaviour.Properties WISTERIA_WOOD = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD);
        WISTERIA_PLANKS = register("wisteria_planks", new Block(WISTERIA_WOOD), buildingBlock());
        WISTERIA_FENCE = register("wisteria_fence", new FenceBlock(WISTERIA_WOOD), buildingBlock());
        WISTERIA_FENCE_GATE = register("wisteria_fence_gate", new FenceGateBlock(WISTERIA_WOOD), buildingBlock());
        WISTERIA_SLAB = register("wisteria_slab", new AetherSlabBlock(WISTERIA_PLANKS.defaultBlockState()), buildingBlock());
        WISTERIA_STAIRS = register("wisteria_stairs", new AetherStairsBlock(WISTERIA_PLANKS.defaultBlockState()), buildingBlock());
        WISTERIA_TRAPDOOR = register("wisteria_trapdoor", new AetherTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)), buildingBlock());
        WISTERIA_DOOR = register("wisteria_door", new AetherDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)), buildingBlock());

//        SUN_ALTAR = register("sun_altar", null);
//        TREASURE_CHEST = register("treasure_chest", null);
//        WHITE_DYED_AERCLOUD = register("white_dyed_aercloud", null);
//        YELLOW_DYED_AERCLOUD = register("yellow_dyed_aercloud", null);
        ZANITE_BLOCK = register("zanite_block", new Block(BlockBehaviour.Properties.of(Material.METAL).strength(3.0f, -1.0f).sound(SoundType.METAL)), buildingBlock());
        BLOCK_OF_GRAVITITE = register("block_of_gravitite", new FloatingBlock(false, BlockBehaviour.Properties.of(Material.METAL).strength(3.0f, -1.0f).sound(SoundType.METAL)), buildingBlock());
        ZANITE_CHAIN = register("zanite_chain", new ChainBlock(BlockBehaviour.Properties.copy(Blocks.CHAIN)), buildingBlock());
        AMBROSIUM_LANTERN = register("ambrosium_lantern", new AmbrosiumLanternBlock(BlockBehaviour.Properties.of(Material.METAL).destroyTime(3.5f).requiresCorrectToolForDrops().lightLevel(state -> 15).explosionResistance(1.0f).sound(SoundType.LANTERN)), buildingBlock());

        ANCIENT_FLOWER = register("ancient_flower", new FlowerBlock(MobEffects.ABSORPTION, 20, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), buildingBlock());
        ATARAXIA = register("ataraxia", new FlowerBlock(MobEffects.HARM, 1, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), buildingBlock());
        CLOUDSBLUFF = register("cloudsbluff", new FlowerBlock(MobEffects.SLOW_FALLING, 6, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), buildingBlock());
        DRIGEAN = register("drigean", new FlowerBlock(MobEffects.FIRE_RESISTANCE, 8, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), buildingBlock());
        LUMINAR = register("luminar", new FlowerBlock(MobEffects.GLOWING, 9, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), buildingBlock());


    }

    static {
        // Logs
        for (Block block : new Block[]{
                SKYROOT_LOG,
                STRIPPED_SKYROOT_LOG,
                GOLDEN_OAK_LOG,
                STRIPPED_GOLDEN_OAK_LOG,
                CRYSTAL_LOG,
                STRIPPED_CRYSTAL_LOG,
                WISTERIA_LOG,
                STRIPPED_WISTERIA_LOG
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
        }
        // Grass
        for (Block block : new Block[]{
                AETHER_GRASS,
                AETHER_TALL_GRASS,
                AETHER_FERN,
                AETHER_BUSH,
                FLUTEGRASS,
                BLUEBERRY_BUSH
        }) {
            registerFlammable(block, 100, 60);
        }
        // Misc
        registerFlammable(SKYROOT_BOOKSHELF, 20, 30);

    }

    private static Item.Properties buildingBlock() {
        return new Item.Properties().tab(AetherItemGroups.Blocks);
    }

    private static Block register(String id, Block block, Item.Properties settings) {
        return register(id, block, true, settings);
    }

    private static void registerFlammable(Block block, int flammability, int encouragement) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(block, flammability, encouragement);
    }

    private static Block register(String id, Block block, boolean registerAsBlockItem, Item.Properties settings) {
        ResourceLocation trueId = Aether.locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        if (registerAsBlockItem) Registry.register(Registry.ITEM, trueId, new BlockItem(block, settings));
        return block;
    }

    private static Block register(String id, Block block, Block rootBlock, Item.Properties settings) {
        ResourceLocation trueId = Aether.locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        Registry.register(Registry.ITEM, trueId, new StandingAndWallBlockItem(rootBlock, block, settings));
        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, Aether.locate(id), block);
    }

    private static RotatedPillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, (blockState) -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0f).sound(SoundType.WOOD));
    }

    private static AetherLeavesBlock createLeavesBlock() {
        return new AetherLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never), true);
    }

    private static BlockBehaviour.Properties createLeafPileBlock(SoundType sounds) {
        return BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).strength(0.2f).sound(sounds).noOcclusion().isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never);
    }

    private static BlockBehaviour.Properties createWisteriaHangerBlock(SoundType sounds) {
        return BlockBehaviour.Properties.of(Material.DECORATION).strength(0.2f).noCollission().instabreak().sound(sounds).noOcclusion().isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never);
    }

    private static AetherLeavesBlock createLeavesBlock(SoundType sounds) {
        return new AetherLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2f).randomTicks().sound(sounds).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never), true);
    }

    private static AetherLeavesBlock createLeavesBlock(int luminance) {
        return new AetherLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never).lightLevel(ignored -> luminance), true);
    }

    private static CrystalLeavesBlock createCrystalLeavesBlock(SoundType sounds) {
        return new CrystalLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2f).randomTicks().sound(sounds).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating(AetherBlocks::never).isViewBlocking(AetherBlocks::never));
    }

    private static FlowerPotBlock createPottedBlock(Block sourceBlock) {
        return new FlowerPotBlock(sourceBlock, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion());
    }

    public static void init() {

    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        // TODO: Replace with a mixin to ItemBlockRenderTypes
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                BLUE_PORTAL, QUICKSOIL_GLASS, QUICKSOIL_GLASS_PANE, AEROGEL,
                COLD_AERCLOUD, BLUE_AERCLOUD, PINK_AERCLOUD, GOLDEN_AERCLOUD
        );
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(),
                DENSE_AERCLOUD_STILL
        );
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                SKYROOT_SAPLING, GOLDEN_OAK_SAPLING, CRYSTAL_SAPLING,
                ROSE_WISTERIA_SAPLING, FROST_WISTERIA_SAPLING, LAVENDER_WISTERIA_SAPLING, BOREAL_WISTERIA_SAPLING,
                AETHER_GRASS, AETHER_TALL_GRASS, AETHER_FERN, AETHER_BUSH, FLUTEGRASS, BLUEBERRY_BUSH,
                ROSE_WISTERIA_HANGER, LAVENDER_WISTERIA_HANGER, FROST_WISTERIA_HANGER, BOREAL_WISTERIA_HANGER,
                AMBROSIUM_TORCH, AMBROSIUM_TORCH_WALL,
                SKYROOT_TRAPDOOR, GOLDEN_OAK_TRAPDOOR, CRYSTAL_TRAPDOOR, WISTERIA_TRAPDOOR,
                POTTED_SKYROOT_SAPLING, POTTED_GOLDEN_OAK_SAPLING, POTTED_CRYSTAL_SAPLING, POTTED_AETHER_FERN,
                POTTED_ROSE_WISTERIA_SAPLING, POTTED_FROST_WISTERIA_SAPLING, POTTED_LAVENDER_WISTERIA_SAPLING, POTTED_BOREAL_WISTERIA_SAPLING,
                SKYROOT_DOOR, GOLDEN_OAK_DOOR, CRYSTAL_DOOR, WISTERIA_DOOR,
                ANCIENT_FLOWER, ATARAXIA, CLOUDSBLUFF, DRIGEAN, LUMINAR
        );
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutoutMipped(),
                AETHER_GRASS_BLOCK, ZANITE_CHAIN,
                SKYROOT_LEAVES, GOLDEN_OAK_LEAVES, CRYSTAL_LEAVES,
                ROSE_WISTERIA_LEAVES, FROST_WISTERIA_LEAVES, LAVENDER_WISTERIA_LEAVES, BOREAL_WISTERIA_LEAVES,
                SKYROOT_LEAF_PILE, ROSE_WISTERIA_LEAF_PILE, FROST_WISTERIA_LEAF_PILE, LAVENDER_WISTERIA_LEAF_PILE
        );
        FluidRenderSetup.setupDenseAercloudRenderingBecauseItJustNeedsToBeASpecialSnowflakeWithOnlyAStillState(DENSE_AERCLOUD_STILL, Aether.locate("dense_aercloud"));
    }

    private static Boolean canSpawnOnLeaves(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    public static boolean never(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return false;
    }

    public static boolean always(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return true;
    }
}
