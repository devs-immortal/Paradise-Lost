package net.id.aether.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.blocks.decorative.*;
import net.id.aether.blocks.mechanical.*;
import net.id.aether.blocks.natural.*;
import net.id.aether.blocks.natural.aercloud.*;
import net.id.aether.blocks.natural.crop.*;
import net.id.aether.blocks.natural.plant.*;
import net.id.aether.blocks.natural.tree.*;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.AetherItems;
import net.id.aether.registry.AetherRegistryQueues;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.util.RenderUtils;
import net.id.aether.world.feature.tree.generator.*;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.id.incubus_core.woodtypefactory.api.WoodSettingsFactory;
import net.id.incubus_core.woodtypefactory.api.WoodTypeFactory;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy;
import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.of;
import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlockActions.*;
import static net.minecraft.block.Blocks.*;

@SuppressWarnings("unused")
public class AetherBlocks {

    protected static Settings unbreakable(AbstractBlock.Settings settings) {
        return settings.strength(-1f, 3600000f);
    }

    // Grass Blocks
    private static Settings grassBlock() {
        return copy(GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f);
    }

    public static final AetherGrassBlock AETHER_GRASS_BLOCK = add("aether_grass", new AetherGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable());
    public static final AetherGrassBlock AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", new AetherGrassBlock(grassBlock().mapColor(MapColor.GOLD)));
    public static final AetherSnowyBlock AETHER_FROZEN_GRASS = add("aether_frozen_grass", new AetherSnowyBlock(grassBlock().mapColor(MapColor.WHITE).strength(2F).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    // Soil Blocks
    public static final Block AETHER_DIRT = add("aether_dirt", new Block(copy(DIRT).strength(0.3f)), tillable(), flattenable());
    public static final Block COARSE_AETHER_DIRT = add("coarse_aether_dirt", new Block(copy(DIRT).strength(0.3f)), coarseTillable(), flattenable());
    public static final Block PERMAFROST = add("permafrost", new Block(copy(DIRT).strength(2f).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    public static final FarmlandBlock AETHER_FARMLAND = add("aether_farmland", new AetherFarmlandBlock(copy(FARMLAND)));
    public static final AetherDirtPathBlock AETHER_DIRT_PATH = add("aether_grass_path", new AetherDirtPathBlock(copy(DIRT_PATH)));
    public static final Block QUICKSOIL = add("quicksoil", new AetherQuicksoilBlock(of(Material.AGGREGATE).strength(0.5f, -1f).slipperiness(1F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)));
    public static final Block PACKED_SWEDROOT = add("packed_swedroot", new Block(of(Material.WOOD).strength(2f).sounds(BlockSoundGroup.SHROOMLIGHT)));

    // Glass Blocks
    private static Settings quicksoilGlass() {
        return copy(GLASS).strength(0.2f, -1f).slipperiness(1f).velocityMultiplier(1.102f).luminance(state -> 14);
    }

    public static final GlassBlock QUICKSOIL_GLASS = add("quicksoil_glass", new GlassBlock(quicksoilGlass()), translucentRenderLayer);
    public static final PaneBlock QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", new AetherPaneBlock(quicksoilGlass()), translucentRenderLayer);

    // Aerclouds
    private static Settings aercloud() {
        return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never);
    }

    public static final AercloudBlock COLD_AERCLOUD = add("cold_aercloud", new AercloudBlock(aercloud().mapColor(MapColor.WHITE)), translucentRenderLayer);
    public static final BlueAercloudBlock BLUE_AERCLOUD = add("blue_aercloud", new BlueAercloudBlock(aercloud().mapColor(MapColor.LIGHT_BLUE)), translucentRenderLayer);
    public static final PinkAercloudBlock PINK_AERCLOUD = add("pink_aercloud", new PinkAercloudBlock(aercloud().mapColor(MapColor.PINK)), translucentRenderLayer);
    public static final GoldenAercloudBlock GOLDEN_AERCLOUD = add("golden_aercloud", new GoldenAercloudBlock(aercloud().mapColor(MapColor.GOLD)), translucentRenderLayer);
    // Fluids
    public static final FluidBlock DENSE_AERCLOUD = add("dense_aercloud", new FluidBlock(AetherFluids.DENSE_AERCLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});
    public static final FluidBlock SPRING_WATER = add("spring_water", new FluidBlock(AetherFluids.SPRING_WATER, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});
    // Organic Extra
    public static final Block ICESTONE = add("icestone", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block AEROGEL = add("aerogel", new Block(of(Material.SOIL).strength(1f, 1200f).sounds(BlockSoundGroup.GLASS).solidBlock(never).nonOpaque()), translucentRenderLayer);

    // Smooth Holystone
    private static Settings holystone() {
        return of(Material.STONE, MapColor.WHITE_GRAY).requiresTool().strength(0.5f, 1f).sounds(BlockSoundGroup.STONE);
    }

    public static final Block HOLYSTONE = add("holystone", new Block(holystone()));
    public static final SlabBlock HOLYSTONE_SLAB = add("holystone_slab", new SlabBlock(holystone()));
    public static final AetherStairsBlock HOLYSTONE_STAIRS = add("holystone_stairs", new AetherStairsBlock(HOLYSTONE.getDefaultState(), holystone()));
    public static final WallBlock HOLYSTONE_WALL = add("holystone_wall", new WallBlock(holystone()));

    // Cobbled Holystone
    private static Settings cobbledHolystone() {
        return holystone().strength(0.4f, 8f);
    }

    public static final Block COBBLED_HOLYSTONE = add("cobbled_holystone", new Block(cobbledHolystone()));
    public static final SlabBlock COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", new SlabBlock(cobbledHolystone()));
    public static final AetherStairsBlock COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", new AetherStairsBlock(COBBLED_HOLYSTONE.getDefaultState(), cobbledHolystone()));
    public static final WallBlock COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", new WallBlock(cobbledHolystone()));

    // Mossy Holystone
    private static Settings mossyCobbledHolystone() {
        return cobbledHolystone().mapColor(MapColor.PALE_GREEN);
    }

    public static final Block MOSSY_HOLYSTONE = add("mossy_holystone", new Block(mossyCobbledHolystone()));
    public static final Block GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", new Block(mossyCobbledHolystone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final SlabBlock MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", new SlabBlock(mossyCobbledHolystone()));
    public static final AetherStairsBlock MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", new AetherStairsBlock(MOSSY_HOLYSTONE.getDefaultState(), mossyCobbledHolystone()));
    public static final WallBlock MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", new WallBlock(mossyCobbledHolystone()));

    // Holystone Bricks
    private static Settings holystoneBrick() {
        return holystone().strength(1.5f, 6f);
    }

    public static final Block HOLYSTONE_BRICK = add("holystone_brick", new Block(holystoneBrick()));
    public static final SlabBlock HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", new SlabBlock(holystoneBrick()));
    public static final AetherStairsBlock HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", new AetherStairsBlock(HOLYSTONE_BRICK.getDefaultState(), holystoneBrick()));
    public static final WallBlock HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", new WallBlock(holystoneBrick()));

    // Dungeon Blocks
    private static Settings carvedStone() {
        return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE);
    }

    public static final Block CARVED_STONE = add("carved_stone", new Block(carvedStone()));
    public static final SlabBlock CARVED_STONE_SLAB = add("carved_stone_slab", new SlabBlock(carvedStone()));
    public static final AetherStairsBlock CARVED_STONE_STAIRS = add("carved_stone_stairs", new AetherStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final WallBlock CARVED_STONE_WALL = add("carved_stone_wall", new WallBlock(carvedStone()));

    public static final Block MOSSY_CARVED_STONE = add("mossy_carved_stone", new Block(carvedStone()));
    public static final SlabBlock MOSSY_CARVED_STONE_SLAB = add("mossy_carved_stone_slab", new SlabBlock(carvedStone()));
    public static final AetherStairsBlock MOSSY_CARVED_STONE_STAIRS = add("mossy_carved_stone_stairs", new AetherStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final WallBlock MOSSY_CARVED_STONE_WALL = add("mossy_carved_stone_wall", new WallBlock(carvedStone()));

    public static final Block CRACKED_CARVED_STONE = add("cracked_carved_stone", new Block(carvedStone()));
    public static final Block GLYPHED_CARVED_STONE = add("glyphed_carved_stone", new Block(carvedStone().luminance(value -> 3)));
    public static final Block CARVED_STONE_PANEL = add("carved_stone_panel", new Block(carvedStone()));
    public static final Block CARVED_STONE_PANEL_LIT = add("carved_stone_panel_lit", new Block(carvedStone().luminance(value -> 12)));
    public static final Block CARVED_STONE_EYE = add("carved_stone_eye", new Block(carvedStone()));
    public static final Block CARVED_STONE_EYE_LIT = add("carved_stone_eye_lit", new Block(carvedStone().luminance(value -> 8)));

    protected static Settings flowerPot() {
        return copy(POTTED_OAK_SAPLING);
    }

    public static final CampfireBlock AMBROSIUM_CAMPFIRE = add("ambrosium_campfire", new AmbrosiumCampfireBlock(false, 1, Settings.copy(CAMPFIRE)), cutoutRenderLayer);

    // Skyroot Wood
    private static final WoodSettingsFactory skyrootColors = new WoodSettingsFactory(MapColor.GREEN, MapColor.TERRACOTTA_GREEN);
    public static final WoodTypeFactory SKYROOT = new WoodTypeFactory(skyrootColors, locate("skyroot"), new SkyrootSaplingGenerator());

    public static final SaplingBlock SKYROOT_SAPLING = SKYROOT.sapling();
    public static final FlowerPotBlock POTTED_SKYROOT_SAPLING = SKYROOT.pottedSapling();
    public static final PillarBlock SKYROOT_LOG = SKYROOT.log();
    public static final PillarBlock MOTTLED_SKYROOT_LOG = add("mottled_skyroot_log", new PillarBlock(skyrootColors.log()), flammableLog);
    public static final ChuteBlock MOTTLED_SKYROOT_FALLEN_LOG = add("mottled_skyroot_fallen_log", new ChuteBlock(skyrootColors.log()), flammableLog, cutoutRenderLayer);
    public static final PillarBlock SKYROOT_WOOD = SKYROOT.wood();
    public static final PillarBlock STRIPPED_SKYROOT_LOG = SKYROOT.strippedLog();
    public static final PillarBlock STRIPPED_SKYROOT_WOOD = SKYROOT.strippedWood();
    public static final LeavesBlock SKYROOT_LEAVES = SKYROOT.leaves();
    public static final LeafPileBlock SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", new LeafPileBlock(skyrootColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block SKYROOT_PLANKS = SKYROOT.planks();
    public static final Block SKYROOT_BOOKSHELF = add("skyroot_bookshelf", new Block(copy(BOOKSHELF).mapColor(skyrootColors.plankColor())), flammable(30, 20));
    public static final FenceBlock SKYROOT_FENCE = SKYROOT.fence();
    public static final FenceGateBlock SKYROOT_FENCE_GATE = SKYROOT.fenceGate();
    public static final SlabBlock SKYROOT_SLAB = SKYROOT.slab();
    public static final StairsBlock SKYROOT_STAIRS = SKYROOT.stairs();
    public static final TrapdoorBlock SKYROOT_TRAPDOOR = SKYROOT.trapdoor();
    public static final DoorBlock SKYROOT_DOOR = SKYROOT.door();
    public static final WoodenButtonBlock SKYROOT_BUTTON = SKYROOT.button();
    public static final PressurePlateBlock SKYROOT_PRESSURE_PLATE = SKYROOT.pressurePlate();
    public static final SignBlock SKYROOT_SIGN = SKYROOT.signFactory().signBlock;
    public static final WallSignBlock SKYROOT_WALL_SIGN = SKYROOT.signFactory().wallSignBlock;
    // Golden Oak Wood
    private static final WoodSettingsFactory goldenOakColors = new WoodSettingsFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final WoodTypeFactory GOLDEN_OAK = new WoodTypeFactory(goldenOakColors, locate("golden_oak"));

    public static final SaplingBlock GOLDEN_OAK_SAPLING = add("golden_oak_sapling", new AetherSaplingBlock(new GoldenOakSaplingGenerator(), goldenOakColors.sapling().luminance(state -> 7)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_GOLDEN_OAK_SAPLING = add("potted_golden_oak_sapling", new FlowerPotBlock(GOLDEN_OAK_SAPLING, flowerPot().luminance(state -> 7)), cutoutRenderLayer);
    public static final PillarBlock GOLDEN_OAK_LOG = add("golden_oak_log", new GoldenOakLogBlock(goldenOakColors.log()), flammableLog);
    public static final PillarBlock GOLDEN_OAK_WOOD = GOLDEN_OAK.wood();
    public static final PillarBlock STRIPPED_GOLDEN_OAK_LOG = GOLDEN_OAK.strippedLog();
    public static final PillarBlock STRIPPED_GOLDEN_OAK_WOOD = GOLDEN_OAK.strippedWood();
    public static final AetherLeavesBlock GOLDEN_OAK_LEAVES = add("golden_oak_leaves", new AetherLeavesBlock(goldenOakColors.leaves().luminance((value -> 11)), true), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block GOLDEN_OAK_PLANKS = GOLDEN_OAK.planks();
    public static final FenceBlock GOLDEN_OAK_FENCE = GOLDEN_OAK.fence();
    public static final FenceGateBlock GOLDEN_OAK_FENCE_GATE = GOLDEN_OAK.fenceGate();
    public static final SlabBlock GOLDEN_OAK_SLAB = GOLDEN_OAK.slab();
    public static final StairsBlock GOLDEN_OAK_STAIRS = GOLDEN_OAK.stairs();
    public static final TrapdoorBlock GOLDEN_OAK_TRAPDOOR = GOLDEN_OAK.trapdoor();
    public static final DoorBlock GOLDEN_OAK_DOOR = GOLDEN_OAK.door();
    public static final WoodenButtonBlock GOLDEN_OAK_BUTTON = GOLDEN_OAK.button();
    public static final PressurePlateBlock GOLDEN_OAK_PRESSURE_PLATE = GOLDEN_OAK.pressurePlate();
    // TODO (b1.7): Fix with datafixer. Name change from aether_<wood_type>_sign to the_aether_<wood_type>_sign
    public static final SignBlock GOLDEN_OAK_SIGN = GOLDEN_OAK.signFactory().signBlock;
    public static final WallSignBlock GOLDEN_OAK_WALL_SIGN = GOLDEN_OAK.signFactory().wallSignBlock;
    // Orange Wood
    private static final WoodSettingsFactory orangeColors = new WoodSettingsFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final WoodTypeFactory ORANGE = new WoodTypeFactory(orangeColors, locate("orange"), new OrangeSaplingGenerator());

    public static final SaplingBlock ORANGE_SAPLING = ORANGE.sapling();
    public static final FlowerPotBlock POTTED_ORANGE_SAPLING = ORANGE.pottedSapling();
    public static final PillarBlock ORANGE_LOG = ORANGE.log();
    public static final PillarBlock ORANGE_WOOD = ORANGE.wood();
    public static final PillarBlock STRIPPED_ORANGE_LOG = ORANGE.strippedLog();
    public static final PillarBlock STRIPPED_ORANGE_WOOD = ORANGE.strippedWood();
    public static final FruitingLeavesBlock ORANGE_LEAVES = add("orange_leaves", new FruitingLeavesBlock(orangeColors.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), () -> AetherItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ORANGE_PLANKS = ORANGE.planks();
    public static final FenceBlock ORANGE_FENCE = ORANGE.fence();
    public static final FenceGateBlock ORANGE_FENCE_GATE = ORANGE.fenceGate();
    public static final SlabBlock ORANGE_SLAB = ORANGE.slab();
    public static final StairsBlock ORANGE_STAIRS = ORANGE.stairs();
    public static final TrapdoorBlock ORANGE_TRAPDOOR = ORANGE.trapdoor();
    public static final DoorBlock ORANGE_DOOR = ORANGE.door();
    public static final WoodenButtonBlock ORANGE_BUTTON = ORANGE.button();
    public static final PressurePlateBlock ORANGE_PRESSURE_PLATE = ORANGE.pressurePlate();
    public static final SignBlock ORANGE_SIGN = ORANGE.signFactory().signBlock;
    public static final WallSignBlock ORANGE_WALL_SIGN = ORANGE.signFactory().wallSignBlock;
    // Crystal Wood
    private static final WoodSettingsFactory crystalColors = new WoodSettingsFactory(MapColor.IRON_GRAY, MapColor.LICHEN_GREEN, MapColor.LIGHT_BLUE);
    public static final WoodTypeFactory CRYSTAL = new WoodTypeFactory(crystalColors, locate("crystal"));

    public static final SaplingBlock CRYSTAL_SAPLING = add("crystal_sapling", new AetherSaplingBlock(new CrystalSaplingGenerator(), crystalColors.sapling().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_CRYSTAL_SAPLING = add("potted_crystal_sapling", new FlowerPotBlock(CRYSTAL_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock CRYSTAL_LOG = CRYSTAL.log();
    public static final PillarBlock CRYSTAL_WOOD = CRYSTAL.wood();
    public static final PillarBlock STRIPPED_CRYSTAL_LOG = CRYSTAL.strippedLog();
    public static final PillarBlock STRIPPED_CRYSTAL_WOOD = CRYSTAL.strippedWood();
    public static final CrystalLeavesBlock CRYSTAL_LEAVES = add("crystal_leaves", new CrystalLeavesBlock(crystalColors.leaves().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block CRYSTAL_PLANKS = CRYSTAL.planks();
    public static final FenceBlock CRYSTAL_FENCE = CRYSTAL.fence();
    public static final FenceGateBlock CRYSTAL_FENCE_GATE = CRYSTAL.fenceGate();
    public static final SlabBlock CRYSTAL_SLAB = CRYSTAL.slab();
    public static final StairsBlock CRYSTAL_STAIRS = CRYSTAL.stairs();
    public static final TrapdoorBlock CRYSTAL_TRAPDOOR = CRYSTAL.trapdoor();
    public static final DoorBlock CRYSTAL_DOOR = CRYSTAL.door();
    public static final WoodenButtonBlock CRYSTAL_BUTTON = CRYSTAL.button();
    public static final PressurePlateBlock CRYSTAL_PRESSURE_PLATE = CRYSTAL.pressurePlate();
    public static final SignBlock CRYSTAL_SIGN = CRYSTAL.signFactory().signBlock;
    public static final WallSignBlock CRYSTAL_WALL_SIGN = CRYSTAL.signFactory().wallSignBlock;
    // Wisteria Wood
    private static final WoodSettingsFactory wisteriaColors = new WoodSettingsFactory(MapColor.PALE_YELLOW, MapColor.BROWN);
    public static final WoodTypeFactory WISTERIA = new WoodTypeFactory(wisteriaColors, locate("wisteria"));

    public static final PillarBlock WISTERIA_LOG = WISTERIA.log();
    public static final PillarBlock WISTERIA_WOOD = WISTERIA.wood();
    public static final PillarBlock STRIPPED_WISTERIA_LOG = WISTERIA.strippedLog();
    public static final PillarBlock STRIPPED_WISTERIA_WOOD = WISTERIA.strippedWood();
    public static final Block WISTERIA_PLANKS = WISTERIA.planks();
    public static final FenceBlock WISTERIA_FENCE = WISTERIA.fence();
    public static final FenceGateBlock WISTERIA_FENCE_GATE = WISTERIA.fenceGate();
    public static final SlabBlock WISTERIA_SLAB = WISTERIA.slab();
    public static final StairsBlock WISTERIA_STAIRS = WISTERIA.stairs();
    public static final TrapdoorBlock WISTERIA_TRAPDOOR = WISTERIA.trapdoor();
    public static final DoorBlock WISTERIA_DOOR = WISTERIA.door();
    public static final WoodenButtonBlock WISTERIA_BUTTON = WISTERIA.button();
    public static final PressurePlateBlock WISTERIA_PRESSURE_PLATE = WISTERIA.pressurePlate();
    public static final SignBlock WISTERIA_SIGN = WISTERIA.signFactory().signBlock;
    public static final WallSignBlock WISTERIA_WALL_SIGN = WISTERIA.signFactory().wallSignBlock;

    private static final WoodSettingsFactory roseWisteriaColors = wisteriaColors.withLeafColor(MapColor.PINK);
    public static final WisteriaLeavesBlock ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new WisteriaLeavesBlock(roseWisteriaColors.noCollideLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new LeafPileBlock(roseWisteriaColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new AetherSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new AetherHangerBlock(roseWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory frostWisteriaColors = wisteriaColors.withLeafColor(MapColor.LIGHT_BLUE);
    public static final WisteriaLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new WisteriaLeavesBlock(frostWisteriaColors.noCollideLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new LeafPileBlock(frostWisteriaColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new AetherSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new AetherHangerBlock(frostWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory lavenderWisteriaColors = wisteriaColors.withLeafColor(MapColor.MAGENTA);
    public static final WisteriaLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new WisteriaLeavesBlock(lavenderWisteriaColors.noCollideLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new LeafPileBlock(lavenderWisteriaColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new AetherSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final AetherHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new AetherHangerBlock(lavenderWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory borealWisteriaColors = wisteriaColors.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] auralLeafColors = new Vec3i[]{RenderUtils.toRGB(0xa6ffdd), RenderUtils.toRGB(0x96e5ff), RenderUtils.toRGB(0xd6b3ff), RenderUtils.toRGB(0xffadc6)};
    public static final AuralLeavesBlock BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteriaColors.auralNoCollideLeaves(), false, auralLeafColors), flammableLeaves);
    public static final SaplingBlock BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new AetherSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteriaColors.sapling().luminance(state -> 5)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)), cutoutRenderLayer);
    public static final AuralHangerBlock BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteriaColors.auralHanger(), auralLeafColors), flammableLeaves, auralCutoutMippedRenderLayer);

    // Grasses
    private static Settings shrub() {
        return copy(GRASS).mapColor(MapColor.PALE_GREEN);
    }

    public static final AetherBrushBlock AETHER_GRASS = add("aether_grass_plant", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_GRASS_FLOWERING = add("aether_grass_flowering", new AetherBrushBlock(shrub().mapColor(MapColor.WHITE)), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_SHORT_GRASS = add("aether_short_grass", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherTallBrushBlock AETHER_TALL_GRASS = add("aether_tall_grass", new AetherTallBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_FERN = add("aether_fern", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_AETHER_FERN = add("potted_aether_fern", new FlowerPotBlock(AETHER_FERN, flowerPot()), cutoutRenderLayer);
    public static final AetherBrushBlock AETHER_BUSH = add("aether_bush", new AetherBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final AetherBrushBlock FLUTEGRASS = add("flutegrass", new AetherBrushBlock(shrub().mapColor(MapColor.GOLD), AetherBlockTags.FLUTEGRASS_VALID_GROUND, true), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock SHAMROCK = add("shamrock", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), 0.99999), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock MALT_SPRIG = add("malt_sprig", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), 1), flammablePlant, cutoutRenderLayer);
    public static final TallWaterPlantBlock HONEY_NETTLE = add("honey_nettle", new TallWaterPlantBlock(shrub().sounds(BlockSoundGroup.SMALL_DRIPLEAF)), cutoutRenderLayer);
    public static final AetherSeagrassBlock HALOPHIA = add("halophia", new AetherSeagrassBlock(shrub().sounds(BlockSoundGroup.WET_GRASS)), cutoutRenderLayer);
    public static final GiantLilypadBlock GIANT_LILY = add("giant_lily", new GiantLilypadBlock(copy(LILY_PAD).mapColor(MapColor.PALE_GREEN)), cutoutMippedRenderLayer);
    public static final WeepingCloudburstBlock WEEPING_CLOUDBURST = add("weeping_cloudburst", new WeepingCloudburstBlock(copy(SUGAR_CANE).mapColor(MapColor.PALE_PURPLE)), cutoutMippedRenderLayer);
    public static final MossStarBlock MOSS_STAR = add("moss_star", new MossStarBlock(copy(MOSS_BLOCK).dynamicBounds().luminance(state -> state.get(Properties.WATERLOGGED) ? 5 : 0)), cutoutRenderLayer);
    public static final MossBallBlock MOSS_BALL = add("moss_ball", new MossBallBlock(copy(MOSS_BLOCK).dynamicBounds()), flammablePlant);
    public static final Block LIVERWORT = add("liverwort", new Block(copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES)));
    public static final CarpetBlock LIVERWORT_CARPET = add("liverwort_carpet", new CarpetBlock(copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES)));

    private static Settings lichen() {
        return copy(OAK_WOOD).mapColor(MapColor.DULL_RED).sounds(BlockSoundGroup.MOSS_BLOCK);
    }

    public static final LichenBlock LICHEN = add("lichen", new LichenBlock(lichen().ticksRandomly(), false));
    public static final LichenPileBlock LICHEN_PILE = add("lichen_pile", new LichenPileBlock(lichen(), false));
    public static final LichenBlock LUCATIEL_LICHEN = add("lucatiel_lichen", new LichenBlock(lichen().ticksRandomly(), true));
    public static final LichenPileBlock LUCATIEL_LICHEN_PILE = add("lucatiel_lichen_pile", new LichenPileBlock(lichen(), true));

    public static final GlowLichenBlock SWEDROOT_SPREAD = add("swedroot_spread", new GlowLichenBlock(Settings.of(Material.REPLACEABLE_PLANT, MapColor.OAK_TAN).noCollision().strength(1F).sounds(BlockSoundGroup.SHROOMLIGHT)), cutoutRenderLayer);

    public static final WallClingingPlantBlock ROOTCAP = add("rootcap", new WallClingingPlantBlock(copy(BROWN_MUSHROOM), AetherBlockTags.FUNGI_CLINGABLES), cutoutRenderLayer);
    public static final AetherMushroomPlantBlock BROWN_SPORECAP = add("brown_sporecap", new AetherMushroomPlantBlock(copy(BROWN_MUSHROOM), BlockTags.MUSHROOM_GROW_BLOCK), cutoutRenderLayer);
    public static final AetherHangingMushroomPlantBlock PINK_SPORECAP = add("pink_sporecap", new AetherHangingMushroomPlantBlock(copy(BROWN_MUSHROOM), BlockTags.MUSHROOM_GROW_BLOCK), cutoutRenderLayer);

    public static final AmadrysCropBlock AMADRYS = add("amadrys", new AmadrysCropBlock(shrub().mapColor(MapColor.PINK)), flammablePlant, cutoutMippedRenderLayer);
    public static final FlaxCropBlock FLAX = add("flax", new FlaxCropBlock(shrub().mapColor(MapColor.OAK_TAN)), flammablePlant, cutoutRenderLayer);
    public static final SwetrootCropBlock SWEDROOT = add("swedroot", new SwetrootCropBlock(shrub().mapColor(MapColor.BLUE)), flammablePlant, cutoutRenderLayer);

    public static final Block FLAXWEAVE_CUSHION = add("flaxweave_cushion", new Block(Settings.of(Material.WOOL).mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F)), flammable(40, 10));

    public static final BlueberryBushBlock BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(of(Material.PLANT).strength(0.2f)
            .ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().suffocates(never).blockVision(never).noCollision()), flammablePlant, cutoutRenderLayer);

    public static final FourBiteCakeBlock CHEESECAKE = add("halflight_cheesecake", new FourBiteCakeBlock(Settings.copy(CAKE)));

    public static final SixFacingBlock AMADRYS_BUNDLE = add("amadrys_bundle", new SixFacingBlock(Settings.copy(HAY_BLOCK)));


    // Flowers
    private static Settings flower() {
        return copy(DANDELION);
    }

    public static final FlowerBlock ANCIENT_FLOWER = add("ancient_flower", new FlowerBlock(StatusEffects.ABSORPTION, 20, flower()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ANCIENT_FLOWER = add("potted_ancient_flower", new FlowerPotBlock(ANCIENT_FLOWER, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock ATARAXIA = add("ataraxia", new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, flower()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ATARAXIA = add("potted_ataraxia", new FlowerPotBlock(ATARAXIA, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock CLOUDSBLUFF = add("cloudsbluff", new FlowerBlock(StatusEffects.SLOW_FALLING, 6, flower()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_CLOUDSBLUFF = add("potted_cloudsbluff", new FlowerPotBlock(CLOUDSBLUFF, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock DRIGEAN = add("drigean", new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, flower()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_DRIGEAN = add("potted_drigean", new FlowerPotBlock(DRIGEAN, flowerPot()), cutoutRenderLayer);
    public static final FlowerBlock LUMINAR = add("luminar", new FlowerBlock(StatusEffects.GLOWING, 9, flower().luminance(value -> 3)), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LUMINAR = add("potted_luminar", new FlowerPotBlock(LUMINAR, flowerPot().luminance(value -> 3)), cutoutRenderLayer);

    // Tall Flowers
    public static final AetherTallBrushBlock WILD_FLAX = add("wild_flax", new AetherTallBrushBlock(flower()), flammablePlant, cutoutMippedRenderLayer);

    // Ores
    public static final OreBlock AMBROSIUM_ORE = add("ambrosium_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final OreBlock ZANITE_ORE = add("zanite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final FloatingBlock GRAVITITE_ORE = add("gravitite_ore", new FloatingBlock(false, of(Material.STONE).requiresTool().strength(5f).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)));
    public static final Block AMBROSIUM_BLOCK = add("ambrosium_block", new Block(of(Material.METAL).requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.STONE)));
    public static final Block ZANITE_BLOCK = add("zanite_block", new Block(of(Material.METAL).requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final FloatingBlock BLOCK_OF_GRAVITITE = add("block_of_gravitite", new FloatingBlock(false, of(Material.METAL).requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    // Misc
    public static final FloatingBlock GRAVITITE_LEVITATOR = add("gravitite_levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));
    public static final ChainBlock ZANITE_CHAIN = add("zanite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final AmbrosiumLanternBlock AMBROSIUM_LANTERN = add("ambrosium_lantern", new AmbrosiumLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);
    public static final AetherPortalBlock BLUE_PORTAL = add("blue_portal", new AetherPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucentRenderLayer);

    // Torches
    private static Settings ambrosiumTorch() {
        return copy(TORCH).ticksRandomly().luminance(state -> 15);
    }

    public static final AmbrosiumTorchBlock AMBROSIUM_TORCH = addImmediately("ambrosium_torch", new AmbrosiumTorchBlock(ambrosiumTorch()), cutoutRenderLayer);
    public static final AmbrosiumWallTorchBlock AMBROSIUM_TORCH_WALL = addImmediately("ambrosium_wall_torch", new AmbrosiumWallTorchBlock(ambrosiumTorch().dropsLike(AMBROSIUM_TORCH)), cutoutRenderLayer);

    // Swet Drops
    private static Settings swetDrop() {
        return of(Material.SOLID_ORGANIC, MapColor.CLEAR).breakInstantly().noCollision();
    }

//    public static final SwetDropBlock SWET_DROP = add("swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.WHITE_SWET));
//    public static final SwetDropBlock BLUE_SWET_DROP = add("blue_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.BLUE_SWET));
//    public static final SwetDropBlock GOLDEN_SWET_DROP = add("golden_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.GOLDEN_SWET));
//    public static final SwetDropBlock PURPLE_SWET_DROP = add("purple_swet_drop", new SwetDropBlock(swetDrop(), () -> AetherEntityTypes.PURPLE_SWET));
    // Usables
    public static final IncubatorBlock INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);

    //dungeon
//    public static final DungeonSwitchBlock DUNGEON_SWITCH = add("dungeonswitch", new DungeonSwitchBlock(of(Material.METAL, MapColor.BLUE).strength(-1.0F, 3600000.0F)));

    // Chests
    /* 24Chrome: I removed all but skyroot from the creative menu, maybe we'll add them later but for now only skyroot will have textures! */
    public static final ChestBlock CRYSTAL_CHEST = CRYSTAL.chestFactory().chest;
    public static final ChestBlock GOLDEN_OAK_CHEST = GOLDEN_OAK.chestFactory().chest;
    public static final ChestBlock ORANGE_CHEST = ORANGE.chestFactory().chest;
    public static final ChestBlock SKYROOT_CHEST = SKYROOT.chestFactory().chest;
    public static final ChestBlock WISTERIA_CHEST = WISTERIA.chestFactory().chest;

    @SafeVarargs
    private static <V extends Block> V add(String id, V block, Action<? super V>... additionalActions) {
        return AetherRegistryQueues.BLOCK.add(locate(id), block, additionalActions);
    }

    /*
       This is the same thing the add method above, but it doesn't wait to register or perform the actions.
       This is required because some of the block settings code uses ID caches, so without it some blocks
       behave like air.
     */
    @SafeVarargs
    private static <T extends Block> T addImmediately(String name, T block, Action<? super T>... actions) {
        var id = locate(name);
        Registry.register(Registry.BLOCK, id, block);
        for (var action : actions) {
            action.accept(id, block);
        }
        return block;
    }

    public static void init() {
        AetherRegistryQueues.BLOCK.register();
        for (var woodType : new WoodTypeFactory[]{SKYROOT, GOLDEN_OAK, CRYSTAL, ORANGE, WISTERIA}) {
            woodType.registerRemainingBlocks();
            woodType.registerFlammability();
            woodType.registerStripping();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        for (var woodType : new WoodTypeFactory[]{SKYROOT, GOLDEN_OAK, CRYSTAL, ORANGE, WISTERIA}) {
            woodType.registerClient();
            woodType.registerRenderLayers();
        }
    }

    private static class AetherFarmlandBlock extends FarmlandBlock {
        public AetherFarmlandBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherPaneBlock extends PaneBlock {
        public AetherPaneBlock(Settings settings) {
            super(settings);
        }
    }

    private static class AetherStairsBlock extends StairsBlock {
        public AetherStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }
}
