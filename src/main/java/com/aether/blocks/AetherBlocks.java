package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.aercloud.*;
import com.aether.blocks.blockentity.FoodBowlBlockEntity;
import com.aether.blocks.blockentity.IncubatorBlockEntity;
import com.aether.blocks.decorative.AetherDirtPathBlock;
import com.aether.blocks.decorative.AmbrosiumLanternBlock;
import com.aether.blocks.decorative.AmbrosiumTorchBlock;
import com.aether.blocks.decorative.AmbrosiumTorchWallBlock;
import com.aether.fluids.AetherFluids;
import com.aether.blocks.mechanical.FoodBowlBlock;
import com.aether.blocks.mechanical.IncubatorBlock;
import com.aether.blocks.natural.*;
import com.aether.client.rendering.block.FluidRenderSetup;
import com.aether.client.rendering.block.IncubatorBlockEntityRenderer;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.RenderUtils;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import com.aether.village.AetherVillagerProfessionExtensions;
import com.aether.world.feature.tree.*;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.aether.Aether.locate;
import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.AbstractBlock.Settings.of;

@SuppressWarnings("unused")
public class AetherBlocks {
    private static final List<BlockRegistryEntry> blockRegistryEntries = new ArrayList<>(256);
    private static final Consumer<Block> log = flammable(5, 5);
    private static final Consumer<Block> planks = flammable(20, 5);
    private static final Consumer<Block> leaves = flammable(60, 30);
    private static final Consumer<Block> plant = flammable(60, 100);

    public static final Block AETHER_DIRT = add("aether_dirt", new Block(copy(Blocks.DIRT).strength(0.3f)));

    private static Settings grassBlock() { return copy(Blocks.GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f); }
    public static final Block AETHER_GRASS_BLOCK = add("aether_grass", new AetherGrassBlock(grassBlock()));
    public static final Block AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", new EnchantedAetherGrassBlock(grassBlock().mapColor(MapColor.GOLD)));

    public static final Block ICESTONE = add("icestone", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block QUICKSOIL = add("quicksoil", new Block(of(Material.AGGREGATE).strength(0.5f, -1f).slipperiness(1F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)));



    public static final Block AMBROSIUM_ORE = add("ambrosium_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3.0F, 3.0F), UniformIntProvider.create(0, 2)));
    public static final Block BLUE_PORTAL = add("blue_portal", new AetherPortalBlock(copy(Blocks.NETHER_PORTAL).nonOpaque().blockVision(AetherBlocks::never).mapColor(MapColor.BLUE)));

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

    public static final Block ZANITE_ORE = add("zanite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f, 3f), UniformIntProvider.create(0, 2)));
    public static final Block AEROGEL = add("aerogel", new Block(of(Material.SOIL).strength(1f, 1200f).sounds(BlockSoundGroup.GLASS).solidBlock(AetherBlocks::never).nonOpaque()));
    public static final Block AETHER_FARMLAND = add("aether_farmland", new FarmlandBlock(copy(Blocks.FARMLAND)));
    public static final Block AETHER_DIRT_PATH = add("aether_grass_path", new AetherDirtPathBlock(copy(Blocks.DIRT_PATH)));

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

    private static Settings ambrosiumTorch() { return copy(Blocks.TORCH).ticksRandomly().luminance(state -> 15); }
    public static final Block AMBROSIUM_TORCH = add("ambrosium_torch", new AmbrosiumTorchBlock(ambrosiumTorch()));
    public static final Block AMBROSIUM_TORCH_WALL = add("ambrosium_wall_torch", new AmbrosiumTorchWallBlock(ambrosiumTorch().dropsLike(AMBROSIUM_TORCH)));

    static {
        Registry.register(Registry.ITEM, locate("ambrosium_torch"), new WallStandingBlockItem(AMBROSIUM_TORCH, AMBROSIUM_TORCH_WALL, buildingBlock()));
    }

    public static final Block BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(of(Material.PLANT)
            .strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(AetherBlocks::canSpawnOnLeaves)
            .suffocates(AetherBlocks::never).blockVision(AetherBlocks::never).noCollision()), plant);



    private static Settings aercloud() { return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque(); }
    public static final Block COLD_AERCLOUD = add("cold_aercloud", new BaseAercloudBlock(aercloud().mapColor(MapColor.WHITE)));
    public static final Block BLUE_AERCLOUD = add("blue_aercloud", new BlueAercloudBlock(aercloud().mapColor(MapColor.LIGHT_BLUE)));
    public static final Block PINK_AERCLOUD = add("pink_aercloud", new PinkAercloudBlock(aercloud().mapColor(MapColor.PINK)));
    public static final Block GOLDEN_AERCLOUD = add("golden_aercloud", new GoldenAercloudBlock(aercloud().mapColor(MapColor.GOLD)));
    public static final Block DENSE_AERCLOUD = add("dense_aercloud", new FluidBlock(AetherFluids.DENSE_AERCLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});

    public static final Block INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()));

    public static final Block GRAVITITE_ORE = add("gravitite_ore", new FloatingBlock(false, of(Material.STONE).requiresTool().strength(5f).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)));
    public static final Block GRAVITITE_LEVITATOR = add("gravitite_levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));

    private static Settings quicksoilGlass() { return copy(Blocks.GLASS).strength(0.2f, -1f).slipperiness(1f).velocityMultiplier(1.102f).luminance(state -> 14); }
    public static final Block QUICKSOIL_GLASS = add("quicksoil_glass", new GlassBlock(quicksoilGlass()));
    public static final Block QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", new PaneBlock(quicksoilGlass()));


    private static final WoodTypeFactory skyroot = new WoodTypeFactory(MapColor.GREEN, MapColor.TERRACOTTA_GREEN);
    public static final Block SKYROOT_SAPLING = add("skyroot_sapling", new AetherSaplingBlock(new SkyrootSaplingGenerator(), skyroot.sapling()));
    public static final Block POTTED_SKYROOT_SAPLING = add("potted_skyroot_sapling", new FlowerPotBlock(SKYROOT_SAPLING, flowerPot()));
    public static final Block SKYROOT_LOG = add("skyroot_log", new PillarBlock(skyroot.log()), log);
    public static final Block SKYROOT_WOOD = add("skyroot_wood", new PillarBlock(skyroot.wood()), log);
    public static final Block STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", new PillarBlock(skyroot.strippedLog()), log);
    public static final Block STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", new PillarBlock(skyroot.strippedWood()), log);
    public static final Block SKYROOT_LEAVES = add("skyroot_leaves", new LeavesBlock(skyroot.leaves()), leaves);
    public static final Block SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", new AetherLeafPileBlock(skyroot.leafPile()), leaves);
    public static final Block SKYROOT_PLANKS = add("skyroot_planks", new Block(skyroot.planks()), planks);
    public static final Block SKYROOT_BOOKSHELF = add("skyroot_bookshelf", new Block(copy(Blocks.BOOKSHELF).mapColor(skyroot.plankColor())), flammable(30, 20));
    public static final Block SKYROOT_FENCE = add("skyroot_fence", new FenceBlock(skyroot.planks()), planks);
    public static final Block SKYROOT_FENCE_GATE = add("skyroot_fence_gate", new FenceGateBlock(skyroot.planks()), planks);
    public static final Block SKYROOT_SLAB = add("skyroot_slab", new SlabBlock(skyroot.planks()), planks);
    public static final Block SKYROOT_STAIRS = add("skyroot_stairs", new StairsBlock(SKYROOT_PLANKS.getDefaultState(), skyroot.planks()), planks);
    public static final Block SKYROOT_TRAPDOOR = add("skyroot_trapdoor", new TrapdoorBlock(skyroot.trapdoor()));
    public static final Block SKYROOT_DOOR = add("skyroot_door", new DoorBlock(skyroot.door()));
    public static final Block SKYROOT_BUTTON = add("skyroot_button", new AetherWoodenButtonBlock(skyroot.button()));
    public static final Block SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, skyroot.pressurePlate()));

    private static final WoodTypeFactory goldenOak = new WoodTypeFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final Block GOLDEN_OAK_SAPLING = add("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), goldenOak.sapling().luminance(state -> 7)));
    public static final Block POTTED_GOLDEN_OAK_SAPLING = add("potted_golden_oak_sapling", new FlowerPotBlock(GOLDEN_OAK_SAPLING, flowerPot().luminance(state -> 7)));
    public static final Block GOLDEN_OAK_LOG = add("golden_oak_log", new PillarBlock(goldenOak.log()), log);
    public static final Block GOLDEN_OAK_WOOD = add("golden_oak_wood", new PillarBlock(goldenOak.log()), log);
    public static final Block STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", new PillarBlock(goldenOak.strippedLog()), log);
    public static final Block STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", new PillarBlock(goldenOak.strippedWood()), log);
    public static final Block GOLDEN_OAK_LEAVES = add("golden_oak_leaves", new LeavesBlock(goldenOak.leaves()), leaves);
    public static final Block GOLDEN_OAK_PLANKS = add("golden_oak_planks", new Block(goldenOak.planks()), planks);
    public static final Block GOLDEN_OAK_FENCE = add("golden_oak_fence", new FenceBlock(goldenOak.planks()), planks);
    public static final Block GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", new FenceGateBlock(goldenOak.planks()), planks);
    public static final Block GOLDEN_OAK_SLAB = add("golden_oak_slab", new SlabBlock(goldenOak.planks()), planks);
    public static final Block GOLDEN_OAK_STAIRS = add("golden_oak_stairs", new StairsBlock(GOLDEN_OAK_PLANKS.getDefaultState(), goldenOak.planks()), planks);
    public static final Block GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", new TrapdoorBlock(goldenOak.trapdoor()));
    public static final Block GOLDEN_OAK_DOOR = add("golden_oak_door", new DoorBlock(goldenOak.door()));
    public static final Block GOLDEN_OAK_BUTTON = add("golden_oak_button", new AetherWoodenButtonBlock(goldenOak.button()));
    public static final Block GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, goldenOak.pressurePlate()));

    private static final WoodTypeFactory orange = new WoodTypeFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final Block ORANGE_SAPLING = add("orange_sapling", new AetherSaplingBlock(new OrangeSaplingGenerator(), orange.sapling()));
    public static final Block POTTED_ORANGE_SAPLING = add("potted_orange_sapling", new FlowerPotBlock(ORANGE_SAPLING, flowerPot()));
    public static final Block ORANGE_LOG = add("orange_log", new PillarBlock(orange.log()), log);
    public static final Block ORANGE_WOOD = add("orange_wood", new PillarBlock(orange.wood()), log);
    public static final Block STRIPPED_ORANGE_LOG = add("stripped_orange_log", new PillarBlock(orange.strippedLog()), log);
    public static final Block STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", new PillarBlock(orange.strippedWood()), log);
    public static final Block ORANGE_LEAVES = add("orange_leaves", new AetherFruitingLeaves(orange.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), AetherItems.ORANGE), leaves);
    public static final Block ORANGE_PLANKS = add("orange_planks", new Block(orange.planks()), planks);
    public static final Block ORANGE_FENCE = add("orange_fence", new FenceBlock(orange.planks()), planks);
    public static final Block ORANGE_FENCE_GATE = add("orange_fence_gate", new FenceGateBlock(orange.planks()), planks);
    public static final Block ORANGE_SLAB = add("orange_slab", new SlabBlock(orange.planks()), planks);
    public static final Block ORANGE_STAIRS = add("orange_stairs", new StairsBlock(ORANGE_PLANKS.getDefaultState(), orange.planks()), planks);
    public static final Block ORANGE_TRAPDOOR = add("orange_trapdoor", new TrapdoorBlock(orange.trapdoor()));
    public static final Block ORANGE_DOOR = add("orange_door", new DoorBlock(orange.door()));
    public static final Block ORANGE_BUTTON = add("orange_button", new AetherWoodenButtonBlock(orange.button()));
    public static final Block ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, orange.pressurePlate()));

    private static final WoodTypeFactory crystal = new WoodTypeFactory(MapColor.IRON_GRAY, MapColor.LICHEN_GREEN, MapColor.LIGHT_BLUE);
    public static final Block CRYSTAL_SAPLING = add("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), crystal.sapling().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)));
    public static final Block POTTED_CRYSTAL_SAPLING = add("potted_crystal_sapling", new FlowerPotBlock(CRYSTAL_SAPLING, flowerPot()));
    public static final Block CRYSTAL_LOG = add("crystal_log", new PillarBlock(crystal.log()), log);
    public static final Block CRYSTAL_WOOD = add("crystal_wood", new PillarBlock(crystal.wood()), log);
    public static final Block STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", new PillarBlock(crystal.strippedLog()), log);
    public static final Block STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", new PillarBlock(crystal.strippedWood()), log);
    public static final Block CRYSTAL_LEAVES = add("crystal_leaves", new CrystalLeavesBlock(crystal.leaves().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), leaves);
    public static final Block CRYSTAL_PLANKS = add("crystal_planks", new Block(crystal.planks()), planks);
    public static final Block CRYSTAL_FENCE = add("crystal_fence", new FenceBlock(crystal.planks()), planks);
    public static final Block CRYSTAL_FENCE_GATE = add("crystal_fence_gate", new FenceGateBlock(crystal.planks()), planks);
    public static final Block CRYSTAL_SLAB = add("crystal_slab", new SlabBlock(crystal.planks()), planks);
    public static final Block CRYSTAL_STAIRS = add("crystal_stairs", new StairsBlock(CRYSTAL_PLANKS.getDefaultState(), crystal.planks()), planks);
    public static final Block CRYSTAL_TRAPDOOR = add("crystal_trapdoor", new TrapdoorBlock(crystal.trapdoor()));
    public static final Block CRYSTAL_DOOR = add("crystal_door", new DoorBlock(crystal.door()));
    public static final Block CRYSTAL_BUTTON = add("crystal_button", new AetherWoodenButtonBlock(crystal.button()));
    public static final Block CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, crystal.pressurePlate()));

    private static final WoodTypeFactory wisteria = new WoodTypeFactory(MapColor.PALE_YELLOW, MapColor.BROWN);
    public static final Block WISTERIA_LOG = add("wisteria_log", createLogBlock(MapColor.YELLOW, MapColor.RED), log);
    public static final Block WISTERIA_WOOD = add("wisteria_wood", createLogBlock(MapColor.YELLOW, MapColor.RED), log);
    public static final Block STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", createLogBlock(MapColor.RED, MapColor.RED), log);
    public static final Block STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", createLogBlock(MapColor.RED, MapColor.RED), log);
    public static final Block WISTERIA_PLANKS = add("wisteria_planks", new Block(wisteria.planks()), planks);
    public static final Block WISTERIA_FENCE = add("wisteria_fence", new FenceBlock(wisteria.planks()), planks);
    public static final Block WISTERIA_FENCE_GATE = add("wisteria_fence_gate", new FenceGateBlock(wisteria.planks()), planks);
    public static final Block WISTERIA_SLAB = add("wisteria_slab", new SlabBlock(wisteria.planks()), planks);
    public static final Block WISTERIA_STAIRS = add("wisteria_stairs", new StairsBlock(WISTERIA_PLANKS.getDefaultState(), wisteria.planks()), planks);
    public static final Block WISTERIA_TRAPDOOR = add("wisteria_trapdoor", new TrapdoorBlock(wisteria.trapdoor()));
    public static final Block WISTERIA_DOOR = add("wisteria_door", new DoorBlock(wisteria.door()));
    public static final Block WISTERIA_BUTTON = add("wisteria_button", new AetherWoodenButtonBlock(wisteria.button()));
    public static final Block WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", new PressurePlateBlock(ActivationRule.EVERYTHING, wisteria.pressurePlate()));

    private static final WoodTypeFactory roseWisteria = wisteria.withLeafColor(MapColor.PINK);
    public static final Block ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new AetherLeavesBlock(roseWisteria.wisteriaLeaves(), false), leaves);
    public static final Block ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new AetherLeafPileBlock(roseWisteria.leafPile()), leaves);
    public static final Block ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteria.sapling()));
    public static final Block POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()));
    public static final Block ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new AetherHangerBlock(roseWisteria.hanger()), leaves);

    private static final WoodTypeFactory frostWisteria = wisteria.withLeafColor(MapColor.LIGHT_BLUE);
    public static final Block FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new AetherLeavesBlock(frostWisteria.wisteriaLeaves(), false), leaves);
    public static final Block FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new AetherLeafPileBlock(frostWisteria.leafPile()), leaves);
    public static final Block FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteria.sapling()));
    public static final Block POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()));
    public static final Block FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new AetherHangerBlock(frostWisteria.hanger()), leaves);

    private static final WoodTypeFactory lavenderWisteria = wisteria.withLeafColor(MapColor.MAGENTA);
    public static final Block LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new AetherLeavesBlock(lavenderWisteria.wisteriaLeaves(), false), leaves);
    public static final Block LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new AetherLeafPileBlock(lavenderWisteria.leafPile()), leaves);
    public static final Block LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteria.sapling()));
    public static final Block POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()));
    public static final Block LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new AetherHangerBlock(lavenderWisteria.hanger()), leaves);

    private static final WoodTypeFactory borealWisteria = wisteria.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] borealWisteriaColors = new Vec3i[]{RenderUtils.toRGB(0x59CDFF), RenderUtils.toRGB(0x3AffCB), RenderUtils.toRGB(0x599CFF), RenderUtils.toRGB(0x8158FE)};
    public static final Block BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteria.auralWisteriaLeaves(), false, borealWisteriaColors), leaves);
    public static final Block BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteria.sapling().luminance(state -> 5)));
    public static final Block POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)));
    public static final Block BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteria.auralHanger(), borealWisteriaColors), leaves);

    public static final Block ZANITE_BLOCK = add("zanite_block", new Block(of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block BLOCK_OF_GRAVITITE = add("block_of_gravitite", new FloatingBlock(false, of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block ZANITE_CHAIN = add("zanite_chain", new ChainBlock(copy(Blocks.CHAIN)));
    public static final Block AMBROSIUM_LANTERN = add("ambrosium_lantern", new AmbrosiumLanternBlock(of(Material.METAL).hardness(3.5f).requiresTool().luminance(state -> 15).resistance(1f).sounds(BlockSoundGroup.LANTERN)));

    private static Settings shrub() { return copy(Blocks.GRASS).mapColor(MapColor.PALE_GREEN); }
    public static final Block AETHER_GRASS = add("aether_grass_plant", new AetherBrushBlock(shrub()), plant);
    public static final Block AETHER_TALL_GRASS = add("aether_tall_grass", new TallPlantBlock(shrub()), plant);
    public static final Block AETHER_FERN = add("aether_fern", new AetherBrushBlock(shrub()), plant);
    public static final Block POTTED_AETHER_FERN = add("potted_aether_fern", new FlowerPotBlock(AETHER_FERN, flowerPot()));
    public static final Block AETHER_BUSH = add("aether_bush", new AetherBrushBlock(shrub()), plant);
    public static final Block FLUTEGRASS = add("flutegrass", new AetherBrushBlock(shrub().mapColor(MapColor.GOLD), ImmutableSet.of(QUICKSOIL), true), plant);

    private static Settings flower() { return copy(Blocks.DANDELION); }
    public static final Block ANCIENT_FLOWER = add("ancient_flower", new FlowerBlock(StatusEffects.ABSORPTION, 20, flower()), plant);
    public static final Block POTTED_ANCIENT_FLOWER = add("potted_ancient_flower", new FlowerPotBlock(ANCIENT_FLOWER, flowerPot()));
    public static final Block ATARAXIA = add("ataraxia", new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, flower()), plant);
    public static final Block POTTED_ATARAXIA = add("potted_ataraxia", new FlowerPotBlock(ATARAXIA, flowerPot()));
    public static final Block CLOUDSBLUFF = add("cloudsbluff", new FlowerBlock(StatusEffects.SLOW_FALLING, 6, flower()), plant);
    public static final Block POTTED_CLOUDSBLUFF = add("potted_cloudsbluff", new FlowerPotBlock(CLOUDSBLUFF, flowerPot()));
    public static final Block DRIGEAN = add("drigean", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, flower()), plant);
    public static final Block POTTED_DRIGEAN = add("potted_drigean", new FlowerPotBlock(DRIGEAN, flowerPot()));
    public static final Block LUMINAR = add("luminar", new FlowerBlock(StatusEffects.GLOWING, 9, flower().luminance(value -> 3)), plant);
    public static final Block POTTED_LUMINAR = add("potted_luminar", new FlowerPotBlock(LUMINAR, flowerPot().luminance(value -> 3)));

    private static Settings swetDrop() { return of(Material.SOLID_ORGANIC, MapColor.CLEAR).breakInstantly().noCollision(); }
    public static final Block SWET_DROP = add("swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.WHITE_SWET));
    public static final Block BLUE_SWET_DROP = add("blue_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.BLUE_SWET));
    public static final Block GOLDEN_SWET_DROP = add("golden_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.GOLDEN_SWET));
    public static final Block PURPLE_SWET_DROP = add("purple_swet_drop", new SwetDropBlock(swetDrop(), AetherEntityTypes.PURPLE_SWET));

    //  BlockEntities


    private static Settings unbreakable(Settings settings) {
        return settings.strength(-1f, 3600000f);
    }

    private static Settings flowerPot() {
        return copy(Blocks.POTTED_OAK_SAPLING);
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
        registerFarmland(AETHER_FARMLAND);
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

    private static void registerFarmland(Block block) {
        ((AetherVillagerProfessionExtensions) VillagerProfession.FARMER).addSecondaryJobSite(block);
    }

    private static Block register(String id, Block block, boolean registerAsBlockItem, Item.Settings settings) {
        Identifier trueId = locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        if (registerAsBlockItem) Registry.register(Registry.ITEM, trueId, new BlockItem(block, settings));
        return block;
    }

    private static Block register(String id, Block block, Block rootBlock, Item.Settings settings) {
        Identifier trueId = locate(id);
        Registry.register(Registry.BLOCK, trueId, block);
        Registry.register(Registry.ITEM, trueId, new WallStandingBlockItem(rootBlock, block, settings));
        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, locate(id), block);
    }

    private static Block add(String name, Block block, Consumer<Block> additionalAction) {
        blockRegistryEntries.add(new BlockRegistryEntry(name, block, additionalAction));
        return block;
    }

    private static Block add(String name, Block block) {
        return add(name, block, null);
    }

    private static Consumer<Block> flammable(int spread, int burn) {
        return block -> ((FireBlock) Blocks.FIRE).registerFlammableBlock(block, spread, burn);
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType.BlockEntityFactory<T> factory, Block ... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, locate(id), BlockEntityType.Builder.create(factory, blocks).build(null));
    }

    private static AetherPillarBlock createLogBlock(MapColor topMaterialColor, MapColor sideMaterialColor) {
        return new AetherPillarBlock(of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    }

    private static Settings createLeafPileBlock(BlockSoundGroup sounds) {
        return of(Material.REPLACEABLE_PLANT).strength(0.2f).sounds(sounds).nonOpaque().suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
    }

    private static Settings createWisteriaHangerBlock(BlockSoundGroup sounds) {
        return of(Material.DECORATION).strength(0.2f).noCollision().breakInstantly().sounds(sounds).nonOpaque().suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
    }

    private static Settings createLeavesProperties(@Nullable Integer luminance, @Nullable BlockSoundGroup sounds) {
        sounds = (sounds != null ? sounds : BlockSoundGroup.GRASS);
        Settings properties = of(Material.LEAVES).strength(0.2f).ticksRandomly().sounds(sounds).nonOpaque().allowsSpawning(AetherBlocks::ocelotOrParrot).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        if (luminance != null) {
            properties = properties.luminance(ignored -> luminance);
        }
        return properties;
    }

    private static Settings createSaplingProperties() {
        return of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);
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
        return new FlowerPotBlock(sourceBlock, of(Material.DECORATION).breakInstantly().nonOpaque());
    }

    private static AbstractButtonBlock createButtonBlock(Block sourceBlock) {
        return createButtonBlock(sourceBlock.getDefaultState());
    }

    private static AbstractButtonBlock createButtonBlock(Block sourceBlock, SoundEvent[] clickSounds) {
        return createButtonBlock(sourceBlock.getDefaultState(), clickSounds);
    }

    private static AbstractButtonBlock createButtonBlock(Block sourceBlock, SoundEvent[] clickSounds, boolean isWooden) {
        return createButtonBlock(sourceBlock.getDefaultState(), clickSounds, isWooden);
    }

    private static AbstractButtonBlock createButtonBlock(BlockState sourceBlock) {
        boolean isWooden = sourceBlock.getMaterial() == Material.WOOD || sourceBlock.getMaterial() == Material.NETHER_WOOD;
        SoundEvent[] clickSounds = new SoundEvent[] {
                (isWooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON),
                (isWooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
        };
        return createButtonBlock(sourceBlock, clickSounds, isWooden);
    }

    private static AbstractButtonBlock createButtonBlock(BlockState sourceBlock, SoundEvent[] clickSounds) {
        boolean isWooden = sourceBlock.getMaterial() == Material.WOOD || sourceBlock.getMaterial() == Material.NETHER_WOOD;
        return createButtonBlock(sourceBlock, clickSounds, isWooden);
    }

    private static AbstractButtonBlock createButtonBlock(BlockState sourceBlock, SoundEvent[] clickSounds, boolean isWooden) {
        return new AbstractButtonBlock(isWooden, of(Material.DECORATION).noCollision().strength(0.5F).sounds(sourceBlock.getSoundGroup())) {
            @Override
            protected SoundEvent getClickSound(boolean powered) {
                clickSounds[0] = (clickSounds[0] != null ? clickSounds[0] : (isWooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON));
                clickSounds[1] = (clickSounds[1] != null ? clickSounds[1] : (isWooden ? SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF));
                return powered ? clickSounds[0] : clickSounds[1];
            }
        };
    }

    private static PressurePlateBlock createPressurePlateBlock(Block sourceBlock) {
        return createPressurePlateBlock(sourceBlock, ActivationRule.EVERYTHING);
    }

    private static PressurePlateBlock createPressurePlateBlock(Block sourceBlock, ActivationRule rule) {
        return createPressurePlateBlock(sourceBlock.getDefaultState(), rule);
    }

    private static PressurePlateBlock createPressurePlateBlock(BlockState sourceBlock) {
        return createPressurePlateBlock(sourceBlock, ActivationRule.EVERYTHING);
    }

    private static PressurePlateBlock createPressurePlateBlock(BlockState sourceBlock, ActivationRule rule) {
        return new PressurePlateBlock(rule, of(sourceBlock.getMaterial(), sourceBlock.getBlock().getDefaultMapColor()).noCollision().strength(0.5F).sounds(sourceBlock.getSoundGroup()));
    }

    private static WeightedPressurePlateBlock createWeightedPressurePlateBlock(Block sourceBlock) {
        return createWeightedPressurePlateBlock(sourceBlock.getDefaultState());
    }

    private static WeightedPressurePlateBlock createWeightedPressurePlateBlock(Block sourceBlock, int weight) {
        return createWeightedPressurePlateBlock(sourceBlock.getDefaultState(), weight);
    }

    private static WeightedPressurePlateBlock createWeightedPressurePlateBlock(BlockState sourceBlock) {
        return createWeightedPressurePlateBlock(sourceBlock, (int) sourceBlock.getBlock().getHardness());
    }

    private static WeightedPressurePlateBlock createWeightedPressurePlateBlock(BlockState sourceBlock, int weight) {
        return new WeightedPressurePlateBlock(weight, of(sourceBlock.getMaterial(), sourceBlock.getBlock().getDefaultMapColor()).requiresTool().noCollision().strength(0.5F).sounds(sourceBlock.getSoundGroup()));
    }

    public static void init() {
        // N/A
    }

    public static void register() {
        for (BlockRegistryEntry entry : blockRegistryEntries) {
            Registry.register(Registry.BLOCK, locate(entry.id()), entry.block());
            if (entry.additionalAction() != null) {
                entry.additionalAction().accept(entry.block());
            }
        }
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
                AETHER_GRASS_BLOCK, ZANITE_CHAIN, AMBROSIUM_LANTERN, INCUBATOR,
                SKYROOT_LEAVES, GOLDEN_OAK_LEAVES, ORANGE_LEAVES, CRYSTAL_LEAVES,
                ROSE_WISTERIA_LEAVES, FROST_WISTERIA_LEAVES, LAVENDER_WISTERIA_LEAVES, BOREAL_WISTERIA_LEAVES,
                SKYROOT_LEAF_PILE, ROSE_WISTERIA_LEAF_PILE, FROST_WISTERIA_LEAF_PILE, LAVENDER_WISTERIA_LEAF_PILE
        );

        putFluids(RenderLayer.getTranslucent(),
                AetherFluids.DENSE_AERCLOUD
        );

        FluidRenderSetup.setupFluidRendering(AetherFluids.DENSE_AERCLOUD, null, locate("dense_aercloud"), 0xFFFFFF);
    }

    private static <T extends BlockEntity> void registerBER(BlockEntityType<T> type, BlockEntityRendererFactory<T> factory) {
        BlockEntityRendererRegistry.INSTANCE.register(type, factory);
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
            Settings log = Settings.copy(Blocks.OAK_LOG);
            ((AbstractBlockSettingsAccessor) log).setMapColorProvider(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? this.woodColor : this.barkColor);
            return log;
        }

        public Settings wood() {
            return Settings.copy(Blocks.OAK_WOOD).mapColor(this.barkColor);
        }

        public Settings strippedLog() {
            return Settings.copy(Blocks.STRIPPED_OAK_LOG).mapColor(this.woodColor);
        }

        public Settings strippedWood() {
            return Settings.copy(Blocks.STRIPPED_OAK_WOOD).mapColor(this.woodColor);
        }

        public Settings sapling() {
            return Settings.copy(Blocks.OAK_SAPLING).mapColor(this.leafColor);
        }

        public Settings leaves() {
            return Settings.copy(Blocks.OAK_LEAVES).mapColor(this.leafColor);
        }

        public Settings wisteriaLeaves() {
            return this.leaves().noCollision().allowsSpawning((state, world, pos, type) -> false);
        }

        public Settings auralWisteriaLeaves() {
            return aural(this.wisteriaLeaves());
        }

        public Settings hanger() {
            return of(Material.DECORATION).strength(0.2f).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        }

        public Settings auralHanger() {
            return aural(this.hanger());
        }

        public Settings leafPile() {
            return of(Material.REPLACEABLE_PLANT, this.leafColor).strength(0.2f).sounds(BlockSoundGroup.VINE).nonOpaque().suffocates(AetherBlocks::never).blockVision(AetherBlocks::never);
        }

        public Settings planks() {
            return Settings.copy(Blocks.OAK_PLANKS).mapColor(this.plankColor);
        }

        public Settings trapdoor() {
            return Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(this.plankColor);
        }

        public Settings door() {
            return Settings.copy(Blocks.OAK_DOOR).mapColor(this.plankColor);
        }

        public Settings button() {
            return Settings.copy(Blocks.OAK_BUTTON);
        }

        public Settings pressurePlate() {
            return Settings.copy(Blocks.OAK_BUTTON).mapColor(this.plankColor);
        }

        private static Settings aural(Settings settings) {
            return settings.strength(0.05f).luminance(state -> 7).emissiveLighting(AetherBlocks::always).postProcess(AetherBlocks::always);
        }
    }

    private static record RegistryEntry<T>(String id, T entry, @Nullable Consumer<T> additionalAction) {
    }

    private static class AetherWoodenButtonBlock extends WoodenButtonBlock {
        protected AetherWoodenButtonBlock(Settings settings) {
            super(settings);
        }
    }
}
