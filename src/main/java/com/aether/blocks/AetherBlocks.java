package com.aether.blocks;

import com.aether.blocks.aercloud.*;
import com.aether.blocks.decorative.AetherDirtPathBlock;
import com.aether.blocks.decorative.AmbrosiumLanternBlock;
import com.aether.blocks.decorative.AmbrosiumTorchBlock;
import com.aether.blocks.decorative.AmbrosiumTorchWallBlock;
import com.aether.fluids.AetherFluids;
import com.aether.blocks.mechanical.FoodBowlBlock;
import com.aether.blocks.mechanical.IncubatorBlock;
import com.aether.blocks.natural.*;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.RenderUtils;
import com.aether.items.AetherItems;
import com.aether.mixin.block.BlocksAccessor;
import com.aether.registry.RegistryQueue;
import com.aether.world.feature.tree.*;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.AbstractBlock.ContextPredicate;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;

import static com.aether.registry.RegistryQueue.onClient;
import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.AbstractBlock.Settings.of;
import static net.minecraft.block.Blocks.*;

@SuppressWarnings("unused")
public class AetherBlocks {
    private static final RegistryQueue<Block> queue = new RegistryQueue<>(Registry.BLOCK, 256);

    private static final ContextPredicate never = (state, view, pos) -> false;
    private static final ContextPredicate always = (state, view, pos) -> true;

    private static Settings unbreakable(Settings settings) { return settings.strength(-1f, 3600000f); }
    private static Settings flowerPot() { return copy(POTTED_OAK_SAPLING); }

    private static Consumer<Block> flammable(int spread, int burn) { return block -> ((FireBlock) FIRE).registerFlammableBlock(block, spread, burn); }
    private static final Consumer<Block> flammableLog = flammable(5, 5);
    private static final Consumer<Block> flammablePlanks = flammable(20, 5);
    private static final Consumer<Block> flammableLeaves = flammable(60, 30);
    private static final Consumer<Block> flammablePlant = flammable(60, 100);
    private static final Consumer<Block> translucenRenderLayer = onClient(block -> RenderLayers.BLOCKS.put(block, RenderLayer.getTranslucent()));
    private static final Consumer<Block> cutoutRenderLayer = onClient(block -> RenderLayers.BLOCKS.put(block, RenderLayer.getCutout()));
    private static final Consumer<Block> cutoutMippedRenderLayer = onClient(block -> RenderLayers.BLOCKS.put(block, RenderLayer.getCutoutMipped()));


    /*
    Begin blocks
     */

    private static Settings grassBlock() { return copy(GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f); }
    public static final Block AETHER_GRASS_BLOCK = add("aether_grass", new AetherGrassBlock(grassBlock()), cutoutMippedRenderLayer);
    public static final Block AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", new EnchantedAetherGrassBlock(grassBlock().mapColor(MapColor.GOLD)));

    public static final Block AETHER_DIRT = add("aether_dirt", new Block(copy(DIRT).strength(0.3f)));
    public static final Block AETHER_FARMLAND = add("aether_farmland", new FarmlandBlock(copy(FARMLAND)));
    public static final Block AETHER_DIRT_PATH = add("aether_grass_path", new AetherDirtPathBlock(copy(DIRT_PATH)));
    public static final Block QUICKSOIL = add("quicksoil", new Block(of(Material.AGGREGATE).strength(0.5f, -1f).slipperiness(1F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)));

    private static Settings quicksoilGlass() { return copy(GLASS).strength(0.2f, -1f).slipperiness(1f).velocityMultiplier(1.102f).luminance(state -> 14); }
    public static final Block QUICKSOIL_GLASS = add("quicksoil_glass", new GlassBlock(quicksoilGlass()), translucenRenderLayer);
    public static final Block QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", new PaneBlock(quicksoilGlass()), translucenRenderLayer);

    public static final Block ICESTONE = add("icestone", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block AEROGEL = add("aerogel", new Block(of(Material.SOIL).strength(1f, 1200f).sounds(BlockSoundGroup.GLASS).solidBlock(never).nonOpaque()), translucenRenderLayer);

    private static Settings aercloud() { return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never); }
    public static final Block COLD_AERCLOUD = add("cold_aercloud", new BaseAercloudBlock(aercloud().mapColor(MapColor.WHITE)), translucenRenderLayer);
    public static final Block BLUE_AERCLOUD = add("blue_aercloud", new BlueAercloudBlock(aercloud().mapColor(MapColor.LIGHT_BLUE)), translucenRenderLayer);
    public static final Block PINK_AERCLOUD = add("pink_aercloud", new PinkAercloudBlock(aercloud().mapColor(MapColor.PINK)), translucenRenderLayer);
    public static final Block GOLDEN_AERCLOUD = add("golden_aercloud", new GoldenAercloudBlock(aercloud().mapColor(MapColor.GOLD)), translucenRenderLayer);

    public static final Block DENSE_AERCLOUD = add("dense_aercloud", new FluidBlock(AetherFluids.DENSE_AERCLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});

    private static Settings holystone() { return of(Material.STONE, MapColor.WHITE_GRAY).requiresTool().strength(0.5f, 1f).sounds(BlockSoundGroup.STONE); }
    public static final Block HOLYSTONE = add("holystone", new Block(holystone()));
    public static final Block HOLYSTONE_SLAB = add("holystone_slab", new SlabBlock(holystone()));
    public static final Block HOLYSTONE_STAIRS = add("holystone_stairs", new StairsBlock(HOLYSTONE.getDefaultState(), holystone()));
    public static final Block HOLYSTONE_WALL = add("holystone_wall", new WallBlock(holystone()));

    private static Settings cobbledHolystone() { return holystone().strength(0.4f, 8f); }
    public static final Block COBBLED_HOLYSTONE = add("cobbled_holystone", new Block(cobbledHolystone()));
    public static final Block COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", new SlabBlock(cobbledHolystone()));
    public static final Block COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", new StairsBlock(COBBLED_HOLYSTONE.getDefaultState(), cobbledHolystone()));
    public static final Block COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", new WallBlock(cobbledHolystone()));

    private static Settings mossyCobbledHolystone() { return cobbledHolystone().mapColor(MapColor.PALE_GREEN); }
    public static final Block MOSSY_HOLYSTONE = add("mossy_holystone", new Block(mossyCobbledHolystone()));
    public static final Block GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", new Block(mossyCobbledHolystone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final Block MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", new SlabBlock(mossyCobbledHolystone()));
    public static final Block MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", new StairsBlock(MOSSY_HOLYSTONE.getDefaultState(), mossyCobbledHolystone()));
    public static final Block MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", new WallBlock(mossyCobbledHolystone()));

    private static Settings holystoneBrick() { return holystone().strength(1.5f, 6f); }
    public static final Block HOLYSTONE_BRICK = add("holystone_brick", new Block(holystoneBrick()));
    public static final Block HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", new SlabBlock(holystoneBrick()));
    public static final Block HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", new StairsBlock(HOLYSTONE_BRICK.getDefaultState(), holystoneBrick()));
    public static final Block HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", new WallBlock(holystoneBrick()));

    private static Settings angelicStone() { return of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE); }
    public static final Block ANGELIC_STONE = add("angelic_stone", new Block(angelicStone()));
    public static final Block ANGELIC_CRACKED_STONE = add("angelic_stone_cracked", new Block(angelicStone()));
    public static final Block ANGELIC_SLAB = add("angelic_slab", new SlabBlock(angelicStone()));
    public static final Block ANGELIC_STAIRS = add("angelic_stairs", new StairsBlock(ANGELIC_STONE.getDefaultState(), angelicStone()));
    public static final Block ANGELIC_WALL = add("angelic_wall", new WallBlock(angelicStone()));

    private static Settings lightAngelicStone() { return angelicStone().luminance(state -> 11); }
    public static final Block LIGHT_ANGELIC_STONE = add("light_angelic_stone", new Block(lightAngelicStone()));
    public static final Block LIGHT_ANGELIC_STONE_TRAP = add("light_angelic_stone_trap", new Block(unbreakable(lightAngelicStone())));
    public static final Block LIGHT_ANGELIC_SLAB = add("light_angelic_slab", new SlabBlock(lightAngelicStone()));
    public static final Block LIGHT_ANGELIC_STAIRS = add("light_angelic_stairs", new StairsBlock(LIGHT_ANGELIC_STONE.getDefaultState(), lightAngelicStone()));
    public static final Block LIGHT_ANGELIC_WALL = add("light_angelic_wall", new WallBlock(lightAngelicStone()));

    private static Settings hellfireStone() { return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE); }
    public static final Block HELLFIRE_STONE = add("hellfire_stone", new Block(hellfireStone()));
    public static final Block HELLFIRE_CRACKED_STONE = add("hellfire_stone_cracked", new Block(hellfireStone()));
    public static final Block HELLFIRE_STONE_TRAP = add("hellfire_stone_trap", new Block(unbreakable(hellfireStone())));
    public static final Block HELLFIRE_SLAB = add("hellfire_slab", new SlabBlock(hellfireStone()));
    public static final Block HELLFIRE_STAIRS = add("hellfire_stairs", new StairsBlock(HELLFIRE_STONE.getDefaultState(), hellfireStone()));
    public static final Block HELLFIRE_WALL = add("hellfire_wall", new WallBlock(hellfireStone()));

    private static Settings lightHellfireStone() { return hellfireStone(); }
    public static final Block LIGHT_HELLFIRE_STONE = add("light_hellfire_stone", new Block(lightHellfireStone()));
    public static final Block LIGHT_HELLFIRE_STONE_TRAP = add("light_hellfire_stone_trap", new Block(unbreakable(lightHellfireStone())));
    public static final Block LIGHT_HELLFIRE_SLAB = add("light_hellfire_slab", new SlabBlock(lightHellfireStone()));
    public static final Block LIGHT_HELLFIRE_STAIRS = add("light_hellfire_stairs", new StairsBlock(LIGHT_HELLFIRE_STONE.getDefaultState(), lightHellfireStone()));
    public static final Block LIGHT_HELLFIRE_WALL = add("light_hellfire_wall", new WallBlock(lightHellfireStone()));

    private static Settings carvedStone() { return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE); }
    public static final Block CARVED_STONE = add("carved_stone", new Block(carvedStone()));
    public static final Block CARVED_STONE_TRAP = add("carved_stone_trap", new Block(unbreakable(carvedStone())));
    public static final Block CARVED_SLAB = add("carved_slab", new SlabBlock(carvedStone()));
    public static final Block CARVED_STAIRS = add("carved_stairs", new StairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final Block CARVED_WALL = add("carved_wall", new WallBlock(carvedStone()));

    private static Settings lightCarvedStone() { return carvedStone().luminance(state -> 11); }
    public static final Block LIGHT_CARVED_STONE = add("light_carved_stone", new Block(lightCarvedStone()));
    public static final Block LIGHT_CARVED_STONE_TRAP = add("light_carved_stone_trap", new Block(unbreakable(lightCarvedStone())));
    public static final Block LIGHT_CARVED_SLAB = add("light_carved_slab", new SlabBlock(lightCarvedStone()));
    public static final Block LIGHT_CARVED_STAIRS = add("light_carved_stairs", new StairsBlock(LIGHT_CARVED_STONE.getDefaultState(), lightCarvedStone()));
    public static final Block LIGHT_CARVED_WALL = add("light_carved_wall", new WallBlock(lightCarvedStone()));

    private static Settings sentryStone() { return of(Material.STONE).hardness(0.5f).resistance(1.0f).sounds(BlockSoundGroup.STONE); }
    public static final Block SENTRY_STONE = add("sentry_stone", new Block(sentryStone()));
    public static final Block SENTRY_CRACKED_STONE = add("sentry_stone_cracked", new Block(sentryStone()));
    public static final Block SENTRY_STONE_TRAP = add("sentry_stone_trap", new Block(unbreakable(sentryStone())));
    public static final Block SENTRY_SLAB = add("sentry_slab", new SlabBlock(sentryStone()));
    public static final Block SENTRY_STAIRS = add("sentry_stairs", new StairsBlock(SENTRY_STONE.getDefaultState(), sentryStone()));
    public static final Block SENTRY_WALL = add("sentry_wall", new WallBlock(sentryStone()));

    private static Settings lightSentryStone() { return sentryStone().luminance(state -> 10); }
    public static final Block LIGHT_SENTRY_STONE = add("light_sentry_stone", new Block(lightSentryStone()));
    public static final Block LIGHT_SENTRY_SLAB = add("light_sentry_slab", new SlabBlock(lightSentryStone()));
    public static final Block LIGHT_SENTRY_STAIRS = add("light_sentry_stairs", new StairsBlock(LIGHT_SENTRY_STONE.getDefaultState(), lightSentryStone()));
    public static final Block LIGHT_SENTRY_WALL = add("light_sentry_wall", new WallBlock(lightSentryStone()));

    private static final WoodTypeFactory skyroot = new WoodTypeFactory(MapColor.GREEN, MapColor.TERRACOTTA_GREEN);
    public static final Block SKYROOT_SAPLING = add("skyroot_sapling", new AetherSaplingBlock(new SkyrootSaplingGenerator(), skyroot.sapling()), cutoutRenderLayer);
    public static final Block POTTED_SKYROOT_SAPLING = add("potted_skyroot_sapling", new FlowerPotBlock(SKYROOT_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block SKYROOT_LOG = add("skyroot_log", new PillarBlock(skyroot.log()), flammableLog);
    public static final Block SKYROOT_WOOD = add("skyroot_wood", new PillarBlock(skyroot.wood()), flammableLog);
    public static final Block STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", new PillarBlock(skyroot.strippedLog()), flammableLog);
    public static final Block STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", new PillarBlock(skyroot.strippedWood()), flammableLog);
    public static final Block SKYROOT_LEAVES = add("skyroot_leaves", new LeavesBlock(skyroot.leaves()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", new AetherLeafPileBlock(skyroot.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block SKYROOT_PLANKS = add("skyroot_planks", new Block(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_BOOKSHELF = add("skyroot_bookshelf", new Block(copy(BOOKSHELF).mapColor(skyroot.plankColor())), flammable(30, 20));
    public static final Block SKYROOT_FENCE = add("skyroot_fence", new FenceBlock(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_FENCE_GATE = add("skyroot_fence_gate", new FenceGateBlock(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_SLAB = add("skyroot_slab", new SlabBlock(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_STAIRS = add("skyroot_stairs", new StairsBlock(SKYROOT_PLANKS.getDefaultState(), skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_TRAPDOOR = add("skyroot_trapdoor", new TrapdoorBlock(skyroot.trapdoor()), cutoutRenderLayer);
    public static final Block SKYROOT_DOOR = add("skyroot_door", new DoorBlock(skyroot.door()), cutoutRenderLayer);
    public static final Block SKYROOT_BUTTON = add("skyroot_button", new AetherWoodenButtonBlock(skyroot.button()));
    public static final Block SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, skyroot.pressurePlate()));

    private static final WoodTypeFactory goldenOak = new WoodTypeFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final Block GOLDEN_OAK_SAPLING = add("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), goldenOak.sapling().luminance(state -> 7)), cutoutRenderLayer);
    public static final Block POTTED_GOLDEN_OAK_SAPLING = add("potted_golden_oak_sapling", new FlowerPotBlock(GOLDEN_OAK_SAPLING, flowerPot().luminance(state -> 7)), cutoutRenderLayer);
    public static final Block GOLDEN_OAK_LOG = add("golden_oak_log", new PillarBlock(goldenOak.log()), flammableLog);
    public static final Block GOLDEN_OAK_WOOD = add("golden_oak_wood", new PillarBlock(goldenOak.log()), flammableLog);
    public static final Block STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", new PillarBlock(goldenOak.strippedLog()), flammableLog);
    public static final Block STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", new PillarBlock(goldenOak.strippedWood()), flammableLog);
    public static final Block GOLDEN_OAK_LEAVES = add("golden_oak_leaves", new LeavesBlock(goldenOak.leaves()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block GOLDEN_OAK_PLANKS = add("golden_oak_planks", new Block(goldenOak.planks()), flammablePlanks);
    public static final Block GOLDEN_OAK_FENCE = add("golden_oak_fence", new FenceBlock(goldenOak.planks()), flammablePlanks);
    public static final Block GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", new FenceGateBlock(goldenOak.planks()), flammablePlanks);
    public static final Block GOLDEN_OAK_SLAB = add("golden_oak_slab", new SlabBlock(goldenOak.planks()), flammablePlanks);
    public static final Block GOLDEN_OAK_STAIRS = add("golden_oak_stairs", new StairsBlock(GOLDEN_OAK_PLANKS.getDefaultState(), goldenOak.planks()), flammablePlanks);
    public static final Block GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", new TrapdoorBlock(goldenOak.trapdoor()), cutoutRenderLayer);
    public static final Block GOLDEN_OAK_DOOR = add("golden_oak_door", new DoorBlock(goldenOak.door()), cutoutRenderLayer);
    public static final Block GOLDEN_OAK_BUTTON = add("golden_oak_button", new AetherWoodenButtonBlock(goldenOak.button()));
    public static final Block GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, goldenOak.pressurePlate()));

    private static final WoodTypeFactory orange = new WoodTypeFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final Block ORANGE_SAPLING = add("orange_sapling", new AetherSaplingBlock(new OrangeSaplingGenerator(), orange.sapling()), cutoutRenderLayer);
    public static final Block POTTED_ORANGE_SAPLING = add("potted_orange_sapling", new FlowerPotBlock(ORANGE_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block ORANGE_LOG = add("orange_log", new PillarBlock(orange.log()), flammableLog);
    public static final Block ORANGE_WOOD = add("orange_wood", new PillarBlock(orange.wood()), flammableLog);
    public static final Block STRIPPED_ORANGE_LOG = add("stripped_orange_log", new PillarBlock(orange.strippedLog()), flammableLog);
    public static final Block STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", new PillarBlock(orange.strippedWood()), flammableLog);
    public static final Block ORANGE_LEAVES = add("orange_leaves", new AetherFruitingLeaves(orange.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), AetherItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ORANGE_PLANKS = add("orange_planks", new Block(orange.planks()), flammablePlanks);
    public static final Block ORANGE_FENCE = add("orange_fence", new FenceBlock(orange.planks()), flammablePlanks);
    public static final Block ORANGE_FENCE_GATE = add("orange_fence_gate", new FenceGateBlock(orange.planks()), flammablePlanks);
    public static final Block ORANGE_SLAB = add("orange_slab", new SlabBlock(orange.planks()), flammablePlanks);
    public static final Block ORANGE_STAIRS = add("orange_stairs", new StairsBlock(ORANGE_PLANKS.getDefaultState(), orange.planks()), flammablePlanks);
    public static final Block ORANGE_TRAPDOOR = add("orange_trapdoor", new TrapdoorBlock(orange.trapdoor()), cutoutRenderLayer);
    public static final Block ORANGE_DOOR = add("orange_door", new DoorBlock(orange.door()), cutoutRenderLayer);
    public static final Block ORANGE_BUTTON = add("orange_button", new AetherWoodenButtonBlock(orange.button()));
    public static final Block ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, orange.pressurePlate()));

    private static final WoodTypeFactory crystal = new WoodTypeFactory(MapColor.IRON_GRAY, MapColor.LICHEN_GREEN, MapColor.LIGHT_BLUE);
    public static final Block CRYSTAL_SAPLING = add("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), crystal.sapling().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), cutoutRenderLayer);
    public static final Block POTTED_CRYSTAL_SAPLING = add("potted_crystal_sapling", new FlowerPotBlock(CRYSTAL_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block CRYSTAL_LOG = add("crystal_log", new PillarBlock(crystal.log()), flammableLog);
    public static final Block CRYSTAL_WOOD = add("crystal_wood", new PillarBlock(crystal.wood()), flammableLog);
    public static final Block STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", new PillarBlock(crystal.strippedLog()), flammableLog);
    public static final Block STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", new PillarBlock(crystal.strippedWood()), flammableLog);
    public static final Block CRYSTAL_LEAVES = add("crystal_leaves", new CrystalLeavesBlock(crystal.leaves().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block CRYSTAL_PLANKS = add("crystal_planks", new Block(crystal.planks()), flammablePlanks);
    public static final Block CRYSTAL_FENCE = add("crystal_fence", new FenceBlock(crystal.planks()), flammablePlanks);
    public static final Block CRYSTAL_FENCE_GATE = add("crystal_fence_gate", new FenceGateBlock(crystal.planks()), flammablePlanks);
    public static final Block CRYSTAL_SLAB = add("crystal_slab", new SlabBlock(crystal.planks()), flammablePlanks);
    public static final Block CRYSTAL_STAIRS = add("crystal_stairs", new StairsBlock(CRYSTAL_PLANKS.getDefaultState(), crystal.planks()), flammablePlanks);
    public static final Block CRYSTAL_TRAPDOOR = add("crystal_trapdoor", new TrapdoorBlock(crystal.trapdoor()), cutoutRenderLayer);
    public static final Block CRYSTAL_DOOR = add("crystal_door", new DoorBlock(crystal.door()), cutoutRenderLayer);
    public static final Block CRYSTAL_BUTTON = add("crystal_button", new AetherWoodenButtonBlock(crystal.button()));
    public static final Block CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, crystal.pressurePlate()));

    private static final WoodTypeFactory wisteria = new WoodTypeFactory(MapColor.PALE_YELLOW, MapColor.BROWN);
    public static final Block WISTERIA_LOG = add("wisteria_log", new PillarBlock(wisteria.log()), flammableLog);
    public static final Block WISTERIA_WOOD = add("wisteria_wood", new PillarBlock(wisteria.wood()), flammableLog);
    public static final Block STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", new PillarBlock(wisteria.strippedLog()), flammableLog);
    public static final Block STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", new PillarBlock(wisteria.strippedWood()), flammableLog);
    public static final Block WISTERIA_PLANKS = add("wisteria_planks", new Block(wisteria.planks()), flammablePlanks);
    public static final Block WISTERIA_FENCE = add("wisteria_fence", new FenceBlock(wisteria.planks()), flammablePlanks);
    public static final Block WISTERIA_FENCE_GATE = add("wisteria_fence_gate", new FenceGateBlock(wisteria.planks()), flammablePlanks);
    public static final Block WISTERIA_SLAB = add("wisteria_slab", new SlabBlock(wisteria.planks()), flammablePlanks);
    public static final Block WISTERIA_STAIRS = add("wisteria_stairs", new StairsBlock(WISTERIA_PLANKS.getDefaultState(), wisteria.planks()), flammablePlanks);
    public static final Block WISTERIA_TRAPDOOR = add("wisteria_trapdoor", new TrapdoorBlock(wisteria.trapdoor()), cutoutRenderLayer);
    public static final Block WISTERIA_DOOR = add("wisteria_door", new DoorBlock(wisteria.door()), cutoutRenderLayer);
    public static final Block WISTERIA_BUTTON = add("wisteria_button", new AetherWoodenButtonBlock(wisteria.button()));
    public static final Block WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, wisteria.pressurePlate()));

    private static final WoodTypeFactory roseWisteria = wisteria.withLeafColor(MapColor.PINK);
    public static final Block ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new AetherLeavesBlock(roseWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new AetherLeafPileBlock(roseWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteria.sapling()), cutoutRenderLayer);
    public static final Block POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new AetherHangerBlock(roseWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory frostWisteria = wisteria.withLeafColor(MapColor.LIGHT_BLUE);
    public static final Block FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new AetherLeavesBlock(frostWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new AetherLeafPileBlock(frostWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteria.sapling()), cutoutRenderLayer);
    public static final Block POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new AetherHangerBlock(frostWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory lavenderWisteria = wisteria.withLeafColor(MapColor.MAGENTA);
    public static final Block LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new AetherLeavesBlock(lavenderWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new AetherLeafPileBlock(lavenderWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteria.sapling()), cutoutRenderLayer);
    public static final Block POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final Block LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new AetherHangerBlock(lavenderWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory borealWisteria = wisteria.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] borealWisteriaColors = new Vec3i[]{RenderUtils.toRGB(0x59CDFF), RenderUtils.toRGB(0x3AffCB), RenderUtils.toRGB(0x599CFF), RenderUtils.toRGB(0x8158FE)};
    public static final Block BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteria.auralWisteriaLeaves(), false, borealWisteriaColors), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteria.sapling().luminance(state -> 5)), cutoutRenderLayer);
    public static final Block POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)), cutoutRenderLayer);
    public static final Block BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteria.auralHanger(), borealWisteriaColors), flammableLeaves, cutoutRenderLayer);

    private static Settings shrub() { return copy(GRASS).mapColor(MapColor.PALE_GREEN); }
    public static final Block AETHER_GRASS = add("aether_grass_plant", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final Block AETHER_TALL_GRASS = add("aether_tall_grass", new TallPlantBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final Block AETHER_FERN = add("aether_fern", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_AETHER_FERN = add("potted_aether_fern", new FlowerPotBlock(AETHER_FERN, flowerPot()), cutoutRenderLayer);
    public static final Block AETHER_BUSH = add("aether_bush", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final Block FLUTEGRASS = add("flutegrass", new AetherBrushBlock(shrub().mapColor(MapColor.GOLD), ImmutableSet.of(QUICKSOIL), true), flammablePlant, cutoutRenderLayer);

    private static Settings flower() { return copy(DANDELION); }
    public static final Block ANCIENT_FLOWER = add("ancient_flower", new FlowerBlock(StatusEffects.ABSORPTION, 20, flower()), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_ANCIENT_FLOWER = add("potted_ancient_flower", new FlowerPotBlock(ANCIENT_FLOWER, flowerPot()), cutoutRenderLayer);
    public static final Block ATARAXIA = add("ataraxia", new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, flower()), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_ATARAXIA = add("potted_ataraxia", new FlowerPotBlock(ATARAXIA, flowerPot()), cutoutRenderLayer);
    public static final Block CLOUDSBLUFF = add("cloudsbluff", new FlowerBlock(StatusEffects.SLOW_FALLING, 6, flower()), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_CLOUDSBLUFF = add("potted_cloudsbluff", new FlowerPotBlock(CLOUDSBLUFF, flowerPot()), cutoutRenderLayer);
    public static final Block DRIGEAN = add("drigean", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, flower()), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_DRIGEAN = add("potted_drigean", new FlowerPotBlock(DRIGEAN, flowerPot()), cutoutRenderLayer);
    public static final Block LUMINAR = add("luminar", new FlowerBlock(StatusEffects.GLOWING, 9, flower().luminance(value -> 3)), flammablePlant, cutoutRenderLayer);
    public static final Block POTTED_LUMINAR = add("potted_luminar", new FlowerPotBlock(LUMINAR, flowerPot().luminance(value -> 3)), cutoutRenderLayer);

    public static final Block BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(of(Material.PLANT).strength(0.2f)
            .ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BlocksAccessor::callCanSpawnOnLeaves)
            .suffocates(never).blockVision(never).noCollision()), flammablePlant, cutoutRenderLayer);

    public static final Block AMBROSIUM_ORE = add("ambrosium_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final Block ZANITE_ORE = add("zanite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final Block GRAVITITE_ORE = add("gravitite_ore", new FloatingBlock(false, of(Material.STONE).requiresTool().strength(5f).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)));
    public static final Block ZANITE_BLOCK = add("zanite_block", new Block(of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block BLOCK_OF_GRAVITITE = add("block_of_gravitite", new FloatingBlock(false, of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block GRAVITITE_LEVITATOR = add("gravitite_levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));
    public static final Block ZANITE_CHAIN = add("zanite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final Block AMBROSIUM_LANTERN = add("ambrosium_lantern", new AmbrosiumLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);

    private static Settings ambrosiumTorch() { return copy(TORCH).ticksRandomly().luminance(state -> 15); }
    public static final Block AMBROSIUM_TORCH = add("ambrosium_torch", new AmbrosiumTorchBlock(ambrosiumTorch()), cutoutRenderLayer);
    public static final Block AMBROSIUM_TORCH_WALL = add("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(ambrosiumTorch().dropsLike(AMBROSIUM_TORCH)), cutoutRenderLayer);

    public static final Block BLUE_PORTAL = add("blue_portal", new AetherPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucenRenderLayer);
    public static final Block INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final Block FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()));

    private static Settings swetDrop() { return of(Material.SOLID_ORGANIC, MapColor.CLEAR).breakInstantly().noCollision(); }
    public static final Block SWET_DROP = add("swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.WHITE_SWET));
    public static final Block BLUE_SWET_DROP = add("blue_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.BLUE_SWET));
    public static final Block GOLDEN_SWET_DROP = add("golden_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.GOLDEN_SWET));
    public static final Block PURPLE_SWET_DROP = add("purple_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.PURPLE_SWET));


    @SafeVarargs
    @SuppressWarnings("RedundantSuppression")
    private static Block add(String name, Block block, Consumer<Block>... additionalActions) {
        return queue.add(name, block, additionalActions);
    }

    public static void init() {
        queue.register();
    }

    /**
     * Takes tree colours, and produces various block settings for that tree to ease the creation of wooden block sets
     */
    private static record WoodTypeFactory(MapColor woodColor, MapColor barkColor, MapColor leafColor, MapColor plankColor) {
        public WoodTypeFactory(MapColor woodColor, MapColor barkColor, MapColor leafColor) {
            this(woodColor, barkColor, leafColor, woodColor);
        }

        public WoodTypeFactory(MapColor woodColor, MapColor barkColor) {
            this(woodColor, barkColor, MapColor.DARK_GREEN, woodColor);
        }

        public WoodTypeFactory withLeafColor(MapColor color) {
            return new WoodTypeFactory(this.woodColor, this.barkColor, color, this.plankColor);
        }

        public Settings log() {
            Settings log = copy(OAK_LOG);
            ((AbstractBlockSettingsAccessor) log).setMapColorProvider(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? this.woodColor : this.barkColor);
            return log;
        }

        public Settings wood() {
            return copy(OAK_WOOD).mapColor(this.barkColor);
        }

        public Settings strippedLog() {
            return copy(STRIPPED_OAK_LOG).mapColor(this.woodColor);
        }

        public Settings strippedWood() {
            return copy(STRIPPED_OAK_WOOD).mapColor(this.woodColor);
        }

        public Settings sapling() {
            return copy(OAK_SAPLING).mapColor(this.leafColor);
        }

        public Settings leaves() {
            return copy(OAK_LEAVES).mapColor(this.leafColor);
        }

        public Settings wisteriaLeaves() {
            return this.leaves().noCollision().allowsSpawning((state, world, pos, type) -> false);
        }

        public Settings auralWisteriaLeaves() {
            return aural(this.wisteriaLeaves());
        }

        public Settings hanger() {
            return of(Material.DECORATION).strength(0.2f).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).suffocates(never).blockVision(never);
        }

        public Settings auralHanger() {
            return aural(this.hanger());
        }

        public Settings leafPile() {
            return of(Material.REPLACEABLE_PLANT, this.leafColor).strength(0.2f).sounds(BlockSoundGroup.VINE).nonOpaque().suffocates(never).blockVision(never);
        }

        public Settings planks() {
            return copy(OAK_PLANKS).mapColor(this.plankColor);
        }

        public Settings trapdoor() {
            return copy(OAK_TRAPDOOR).mapColor(this.plankColor);
        }

        public Settings door() {
            return copy(OAK_DOOR).mapColor(this.plankColor);
        }

        public Settings button() {
            return copy(OAK_BUTTON);
        }

        public Settings pressurePlate() {
            return copy(OAK_BUTTON).mapColor(this.plankColor);
        }

        private static Settings aural(Settings settings) {
            return settings.strength(0.05f).luminance(state -> 7).emissiveLighting(always).postProcess(always);
        }
    }

    private static class AetherWoodenButtonBlock extends WoodenButtonBlock {
        protected AetherWoodenButtonBlock(Settings settings) {
            super(settings);
        }
    }
}
