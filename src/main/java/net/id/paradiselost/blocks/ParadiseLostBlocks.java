package net.id.paradiselost.blocks;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.blocks.blockentity.ParadiseLostChestBlock;
import net.id.paradiselost.blocks.decorative.ParadiseLostDirtPathBlock;
import net.id.paradiselost.blocks.decorative.AmbrosiumLanternBlock;
import net.id.paradiselost.blocks.decorative.AmbrosiumTorchBlock;
import net.id.paradiselost.blocks.decorative.AmbrosiumWallTorchBlock;
import net.id.paradiselost.blocks.mechanical.FoodBowlBlock;
import net.id.paradiselost.blocks.mechanical.IncubatorBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostGrassBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostQuicksoilBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostSaplingBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostSnowyBlock;
import net.id.paradiselost.blocks.natural.aercloud.AercloudBlock;
import net.id.paradiselost.blocks.natural.aercloud.BlueAercloudBlock;
import net.id.paradiselost.blocks.natural.aercloud.GoldenAercloudBlock;
import net.id.paradiselost.blocks.natural.aercloud.PinkAercloudBlock;
import net.id.paradiselost.blocks.natural.crop.AmadrysCropBlock;
import net.id.paradiselost.blocks.natural.crop.BlueberryBushBlock;
import net.id.paradiselost.blocks.natural.crop.FlaxCropBlock;
import net.id.paradiselost.blocks.natural.crop.SwetrootCropBlock;
import net.id.paradiselost.blocks.natural.plant.*;
import net.id.paradiselost.blocks.natural.tree.*;
import net.id.paradiselost.fluids.ParadiseLostFluids;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.util.ParadiseLostSignType;
import net.id.paradiselost.util.RenderUtils;
import net.id.paradiselost.world.feature.tree.generator.*;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy;
import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.of;
import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.*;
import static net.minecraft.block.Blocks.*;

@SuppressWarnings("unused")
public class ParadiseLostBlocks {

    protected static Settings unbreakable(AbstractBlock.Settings settings) {
        return settings.strength(-1f, 3600000f);
    }

    // Grass Blocks
    private static Settings grassBlock() {
        return copy(Blocks.GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f);
    }

    public static final ParadiseLostGrassBlock GRASS_BLOCK = add("grass", new ParadiseLostGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable());
    public static final ParadiseLostGrassBlock ENCHANTED_GRASS = add("enchanted_grass", new ParadiseLostGrassBlock(grassBlock().mapColor(MapColor.GOLD)));
    public static final ParadiseLostSnowyBlock FROZEN_GRASS = add("frozen_grass", new ParadiseLostSnowyBlock(grassBlock().mapColor(MapColor.WHITE).strength(2F).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    // Soil Blocks
    public static final Block DIRT = add("dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), tillable(), flattenable());
    public static final Block COARSE_DIRT = add("coarse_dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), coarseTillable(), flattenable());
    public static final Block PERMAFROST = add("permafrost", new Block(copy(Blocks.DIRT).strength(2f).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    public static final FarmlandBlock FARMLAND = add("farmland", new ParadiseLostFarmlandBlock(copy(Blocks.FARMLAND)));
    public static final ParadiseLostDirtPathBlock DIRT_PATH = add("grass_path", new ParadiseLostDirtPathBlock(copy(Blocks.DIRT_PATH)));
    public static final Block QUICKSOIL = add("quicksoil", new ParadiseLostQuicksoilBlock(of(Material.AGGREGATE).strength(0.5f, -1f).slipperiness(1F).velocityMultiplier(1.102F).sounds(BlockSoundGroup.SAND)));

    // Glass Blocks
    private static Settings quicksoilGlass() {
        return copy(GLASS).strength(0.2f, -1f).slipperiness(1f).velocityMultiplier(1.102f).luminance(state -> 14);
    }

    public static final GlassBlock QUICKSOIL_GLASS = add("quicksoil_glass", new GlassBlock(quicksoilGlass()), translucentRenderLayer);
    public static final PaneBlock QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", new ParadiseLostPaneBlock(quicksoilGlass()), translucentRenderLayer);

    // Aerclouds
    private static Settings aercloud() {
        return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never);
    }

    public static final AercloudBlock COLD_AERCLOUD = add("cold_aercloud", new AercloudBlock(aercloud().mapColor(MapColor.WHITE)), translucentRenderLayer);
    public static final BlueAercloudBlock BLUE_AERCLOUD = add("blue_aercloud", new BlueAercloudBlock(aercloud().mapColor(MapColor.LIGHT_BLUE)), translucentRenderLayer);
    public static final PinkAercloudBlock PINK_AERCLOUD = add("pink_aercloud", new PinkAercloudBlock(aercloud().mapColor(MapColor.PINK)), translucentRenderLayer);
    public static final GoldenAercloudBlock GOLDEN_AERCLOUD = add("golden_aercloud", new GoldenAercloudBlock(aercloud().mapColor(MapColor.GOLD)), translucentRenderLayer);
    // Fluids
    public static final FluidBlock DENSE_AERCLOUD = add("dense_aercloud", new FluidBlock(ParadiseLostFluids.DENSE_AERCLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});
    public static final FluidBlock SPRING_WATER = add("spring_water", new FluidBlock(ParadiseLostFluids.SPRING_WATER, of(Material.WATER).noCollision().strength(100f).dropsNothing()) {});
    // Organic Extra
    public static final Block ICESTONE = add("icestone", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block AEROGEL = add("aerogel", new Block(of(Material.SOIL).strength(1f, 1200f).sounds(BlockSoundGroup.GLASS).solidBlock(never).nonOpaque()), translucentRenderLayer);

    // Smooth Holystone
    private static Settings holystone() {
        return of(Material.STONE, MapColor.WHITE_GRAY).requiresTool().strength(0.5f, 1f).sounds(BlockSoundGroup.STONE);
    }

    public static final Block HOLYSTONE = add("holystone", new Block(holystone()));
    public static final SlabBlock HOLYSTONE_SLAB = add("holystone_slab", new SlabBlock(holystone()));
    public static final ParadiseLostStairsBlock HOLYSTONE_STAIRS = add("holystone_stairs", new ParadiseLostStairsBlock(HOLYSTONE.getDefaultState(), holystone()));
    public static final WallBlock HOLYSTONE_WALL = add("holystone_wall", new WallBlock(holystone()));

    // Cobbled Holystone
    private static Settings cobbledHolystone() {
        return holystone().strength(0.4f, 8f);
    }

    public static final Block COBBLED_HOLYSTONE = add("cobbled_holystone", new Block(cobbledHolystone()));
    public static final SlabBlock COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", new SlabBlock(cobbledHolystone()));
    public static final ParadiseLostStairsBlock COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", new ParadiseLostStairsBlock(COBBLED_HOLYSTONE.getDefaultState(), cobbledHolystone()));
    public static final WallBlock COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", new WallBlock(cobbledHolystone()));

    // Mossy Holystone
    private static Settings mossyCobbledHolystone() {
        return cobbledHolystone().mapColor(MapColor.PALE_GREEN);
    }

    public static final Block MOSSY_HOLYSTONE = add("mossy_holystone", new Block(mossyCobbledHolystone()));
    public static final Block GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", new Block(mossyCobbledHolystone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final SlabBlock MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", new SlabBlock(mossyCobbledHolystone()));
    public static final ParadiseLostStairsBlock MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", new ParadiseLostStairsBlock(MOSSY_HOLYSTONE.getDefaultState(), mossyCobbledHolystone()));
    public static final WallBlock MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", new WallBlock(mossyCobbledHolystone()));

    // Holystone Bricks
    private static Settings holystoneBrick() {
        return holystone().strength(1.5f, 6f);
    }

    public static final Block HOLYSTONE_BRICK = add("holystone_brick", new Block(holystoneBrick()));
    public static final SlabBlock HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", new SlabBlock(holystoneBrick()));
    public static final ParadiseLostStairsBlock HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", new ParadiseLostStairsBlock(HOLYSTONE_BRICK.getDefaultState(), holystoneBrick()));
    public static final WallBlock HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", new WallBlock(holystoneBrick()));

    // Dungeon Blocks
    private static Settings carvedStone() {
        return of(Material.STONE).hardness(0.5f).resistance(1f).sounds(BlockSoundGroup.STONE);
    }

    public static final Block CARVED_STONE = add("carved_stone", new Block(carvedStone()));
    public static final SlabBlock CARVED_STONE_SLAB = add("carved_stone_slab", new SlabBlock(carvedStone()));
    public static final ParadiseLostStairsBlock CARVED_STONE_STAIRS = add("carved_stone_stairs", new ParadiseLostStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
    public static final WallBlock CARVED_STONE_WALL = add("carved_stone_wall", new WallBlock(carvedStone()));

    public static final Block MOSSY_CARVED_STONE = add("mossy_carved_stone", new Block(carvedStone()));
    public static final SlabBlock MOSSY_CARVED_STONE_SLAB = add("mossy_carved_stone_slab", new SlabBlock(carvedStone()));
    public static final ParadiseLostStairsBlock MOSSY_CARVED_STONE_STAIRS = add("mossy_carved_stone_stairs", new ParadiseLostStairsBlock(CARVED_STONE.getDefaultState(), carvedStone()));
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

    // Skyroot Wood
    private static final WoodTypeFactory skyroot = new WoodTypeFactory(MapColor.GREEN, MapColor.TERRACOTTA_GREEN);
    public static final SaplingBlock SKYROOT_SAPLING = add("skyroot_sapling", new ParadiseLostSaplingBlock(new SkyrootSaplingGenerator(), skyroot.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_SKYROOT_SAPLING = add("potted_skyroot_sapling", new FlowerPotBlock(SKYROOT_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock SKYROOT_LOG = add("skyroot_log", new PillarBlock(skyroot.log()), flammableLog);
    public static final PillarBlock MOTTLED_SKYROOT_LOG = add("mottled_skyroot_log", new PillarBlock(skyroot.log()), flammableLog);
    public static final ChuteBlock MOTTLED_SKYROOT_FALLEN_LOG = add("mottled_skyroot_fallen_log", new ChuteBlock(skyroot.log()), flammableLog, cutoutRenderLayer);
    public static final PillarBlock SKYROOT_WOOD = add("skyroot_wood", new PillarBlock(skyroot.wood()), flammableLog);
    public static final PillarBlock STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", new PillarBlock(skyroot.strippedLog()), flammableLog, strippedFrom(SKYROOT_LOG));
    public static final PillarBlock STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", new PillarBlock(skyroot.strippedWood()), flammableLog, strippedFrom(SKYROOT_WOOD));
    public static final LeavesBlock SKYROOT_LEAVES = add("skyroot_leaves", new LeavesBlock(skyroot.leaves()), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", new LeafPileBlock(skyroot.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block SKYROOT_PLANKS = add("skyroot_planks", new Block(skyroot.planks()), flammablePlanks);
    public static final Block SKYROOT_BOOKSHELF = add("skyroot_bookshelf", new Block(copy(BOOKSHELF).mapColor(skyroot.plankColor())), flammable(30, 20));
    public static final FenceBlock SKYROOT_FENCE = add("skyroot_fence", new FenceBlock(skyroot.planks()), flammablePlanks);
    public static final FenceGateBlock SKYROOT_FENCE_GATE = add("skyroot_fence_gate", new FenceGateBlock(skyroot.planks()), flammablePlanks);
    public static final SlabBlock SKYROOT_SLAB = add("skyroot_slab", new SlabBlock(skyroot.planks()), flammablePlanks);
    public static final ParadiseLostStairsBlock SKYROOT_STAIRS = add("skyroot_stairs", new ParadiseLostStairsBlock(SKYROOT_PLANKS.getDefaultState(), skyroot.planks()), flammablePlanks);
    public static final ParadiseLostTrapdoorBlock SKYROOT_TRAPDOOR = add("skyroot_trapdoor", new ParadiseLostTrapdoorBlock(skyroot.trapdoor()), cutoutRenderLayer);
    public static final ParadiseLostDoorBlock SKYROOT_DOOR = add("skyroot_door", new ParadiseLostDoorBlock(skyroot.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock SKYROOT_BUTTON = add("skyroot_button", new ParadiseLostWoodenButtonBlock(skyroot.button()));
    public static final ParadiseLostPressurePlateBlock SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", new ParadiseLostPressurePlateBlock(ActivationRule.EVERYTHING, skyroot.pressurePlate()));
    public static final SignBlock SKYROOT_SIGN = addImmediately("skyroot_sign", new SignBlock(skyroot.sign(), ParadiseLostSignType.SKYROOT), signBlockEntity);
    public static final WallSignBlock SKYROOT_WALL_SIGN = addImmediately("skyroot_wall_sign", new WallSignBlock(skyroot.wallSign().dropsLike(SKYROOT_SIGN), ParadiseLostSignType.SKYROOT), signBlockEntity);
    // Golden Oak Wood
    private static final WoodTypeFactory goldenOak = new WoodTypeFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final SaplingBlock GOLDEN_OAK_SAPLING = add("golden_oak_sapling", new ParadiseLostSaplingBlock(new GoldenOakSaplingGenerator(), goldenOak.sapling().luminance(state -> 7)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_GOLDEN_OAK_SAPLING = add("potted_golden_oak_sapling", new FlowerPotBlock(GOLDEN_OAK_SAPLING, flowerPot().luminance(state -> 7)), cutoutRenderLayer);
    public static final GoldenOakLogBlock GOLDEN_OAK_LOG = add("golden_oak_log", new GoldenOakLogBlock(goldenOak.log()), flammableLog);
    public static final PillarBlock GOLDEN_OAK_WOOD = add("golden_oak_wood", new PillarBlock(goldenOak.log()), flammableLog);
    public static final PillarBlock STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", new PillarBlock(goldenOak.strippedLog()), flammableLog, strippedFrom(GOLDEN_OAK_LOG));
    public static final PillarBlock STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", new PillarBlock(goldenOak.strippedWood()), flammableLog, strippedFrom(GOLDEN_OAK_WOOD));
    public static final ParadiseLostLeavesBlock GOLDEN_OAK_LEAVES = add("golden_oak_leaves", new ParadiseLostLeavesBlock(goldenOak.leaves().luminance((value -> 11)), true), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block GOLDEN_OAK_PLANKS = add("golden_oak_planks", new Block(goldenOak.planks()), flammablePlanks);
    public static final FenceBlock GOLDEN_OAK_FENCE = add("golden_oak_fence", new FenceBlock(goldenOak.planks()), flammablePlanks);
    public static final FenceGateBlock GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", new FenceGateBlock(goldenOak.planks()), flammablePlanks);
    public static final SlabBlock GOLDEN_OAK_SLAB = add("golden_oak_slab", new SlabBlock(goldenOak.planks()), flammablePlanks);
    public static final ParadiseLostStairsBlock GOLDEN_OAK_STAIRS = add("golden_oak_stairs", new ParadiseLostStairsBlock(GOLDEN_OAK_PLANKS.getDefaultState(), goldenOak.planks()), flammablePlanks);
    public static final ParadiseLostTrapdoorBlock GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", new ParadiseLostTrapdoorBlock(goldenOak.trapdoor()), cutoutRenderLayer);
    public static final ParadiseLostDoorBlock GOLDEN_OAK_DOOR = add("golden_oak_door", new ParadiseLostDoorBlock(goldenOak.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock GOLDEN_OAK_BUTTON = add("golden_oak_button", new ParadiseLostWoodenButtonBlock(goldenOak.button()));
    public static final ParadiseLostPressurePlateBlock GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", new ParadiseLostPressurePlateBlock(ActivationRule.EVERYTHING, goldenOak.pressurePlate()));
    public static final SignBlock GOLDEN_OAK_SIGN = addImmediately("golden_oak_sign", new SignBlock(goldenOak.sign(), ParadiseLostSignType.GOLDEN_OAK), signBlockEntity);
    public static final WallSignBlock GOLDEN_OAK_WALL_SIGN = addImmediately("golden_oak_wall_sign", new WallSignBlock(goldenOak.wallSign().dropsLike(GOLDEN_OAK_SIGN), ParadiseLostSignType.GOLDEN_OAK), signBlockEntity);
    // Orange Wood
    private static final WoodTypeFactory orange = new WoodTypeFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final SaplingBlock ORANGE_SAPLING = add("orange_sapling", new ParadiseLostSaplingBlock(new OrangeSaplingGenerator(), orange.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ORANGE_SAPLING = add("potted_orange_sapling", new FlowerPotBlock(ORANGE_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock ORANGE_LOG = add("orange_log", new PillarBlock(orange.log()), flammableLog);
    public static final PillarBlock ORANGE_WOOD = add("orange_wood", new PillarBlock(orange.wood()), flammableLog);
    public static final PillarBlock STRIPPED_ORANGE_LOG = add("stripped_orange_log", new PillarBlock(orange.strippedLog()), flammableLog, strippedFrom(ORANGE_LOG));
    public static final PillarBlock STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", new PillarBlock(orange.strippedWood()), flammableLog, strippedFrom(ORANGE_WOOD));
    public static final FruitingLeavesBlock ORANGE_LEAVES = add("orange_leaves", new FruitingLeavesBlock(orange.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), () -> ParadiseLostItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block ORANGE_PLANKS = add("orange_planks", new Block(orange.planks()), flammablePlanks);
    public static final FenceBlock ORANGE_FENCE = add("orange_fence", new FenceBlock(orange.planks()), flammablePlanks);
    public static final FenceGateBlock ORANGE_FENCE_GATE = add("orange_fence_gate", new FenceGateBlock(orange.planks()), flammablePlanks);
    public static final SlabBlock ORANGE_SLAB = add("orange_slab", new SlabBlock(orange.planks()), flammablePlanks);
    public static final ParadiseLostStairsBlock ORANGE_STAIRS = add("orange_stairs", new ParadiseLostStairsBlock(ORANGE_PLANKS.getDefaultState(), orange.planks()), flammablePlanks);
    public static final ParadiseLostTrapdoorBlock ORANGE_TRAPDOOR = add("orange_trapdoor", new ParadiseLostTrapdoorBlock(orange.trapdoor()), cutoutRenderLayer);
    public static final ParadiseLostDoorBlock ORANGE_DOOR = add("orange_door", new ParadiseLostDoorBlock(orange.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock ORANGE_BUTTON = add("orange_button", new ParadiseLostWoodenButtonBlock(orange.button()));
    public static final ParadiseLostPressurePlateBlock ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", new ParadiseLostPressurePlateBlock(ActivationRule.EVERYTHING, orange.pressurePlate()));
    public static final SignBlock ORANGE_SIGN = addImmediately("orange_sign", new SignBlock(orange.sign(), ParadiseLostSignType.ORANGE), signBlockEntity);
    public static final WallSignBlock ORANGE_WALL_SIGN = addImmediately("orange_wall_sign", new WallSignBlock(orange.wallSign().dropsLike(ORANGE_SIGN), ParadiseLostSignType.ORANGE), signBlockEntity);
    // Crystal Wood
    private static final WoodTypeFactory crystal = new WoodTypeFactory(MapColor.IRON_GRAY, MapColor.LICHEN_GREEN, MapColor.LIGHT_BLUE);
    public static final SaplingBlock CRYSTAL_SAPLING = add("crystal_sapling", new ParadiseLostSaplingBlock(new CrystalSaplingGenerator(), crystal.sapling().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_CRYSTAL_SAPLING = add("potted_crystal_sapling", new FlowerPotBlock(CRYSTAL_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final PillarBlock CRYSTAL_LOG = add("crystal_log", new PillarBlock(crystal.log()), flammableLog);
    public static final PillarBlock CRYSTAL_WOOD = add("crystal_wood", new PillarBlock(crystal.wood()), flammableLog);
    public static final PillarBlock STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", new PillarBlock(crystal.strippedLog()), flammableLog, strippedFrom(CRYSTAL_LOG));
    public static final PillarBlock STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", new PillarBlock(crystal.strippedWood()), flammableLog, strippedFrom(CRYSTAL_WOOD));
    public static final CrystalLeavesBlock CRYSTAL_LEAVES = add("crystal_leaves", new CrystalLeavesBlock(crystal.leaves().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block CRYSTAL_PLANKS = add("crystal_planks", new Block(crystal.planks()), flammablePlanks);
    public static final FenceBlock CRYSTAL_FENCE = add("crystal_fence", new FenceBlock(crystal.planks()), flammablePlanks);
    public static final FenceGateBlock CRYSTAL_FENCE_GATE = add("crystal_fence_gate", new FenceGateBlock(crystal.planks()), flammablePlanks);
    public static final SlabBlock CRYSTAL_SLAB = add("crystal_slab", new SlabBlock(crystal.planks()), flammablePlanks);
    public static final ParadiseLostStairsBlock CRYSTAL_STAIRS = add("crystal_stairs", new ParadiseLostStairsBlock(CRYSTAL_PLANKS.getDefaultState(), crystal.planks()), flammablePlanks);
    public static final ParadiseLostTrapdoorBlock CRYSTAL_TRAPDOOR = add("crystal_trapdoor", new ParadiseLostTrapdoorBlock(crystal.trapdoor()), cutoutRenderLayer);
    public static final ParadiseLostDoorBlock CRYSTAL_DOOR = add("crystal_door", new ParadiseLostDoorBlock(crystal.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock CRYSTAL_BUTTON = add("crystal_button", new ParadiseLostWoodenButtonBlock(crystal.button()));
    public static final ParadiseLostPressurePlateBlock CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", new ParadiseLostPressurePlateBlock(ActivationRule.EVERYTHING, crystal.pressurePlate()));
    public static final SignBlock CRYSTAL_SIGN = addImmediately("crystal_sign", new SignBlock(crystal.sign(), ParadiseLostSignType.CRYSTAL), signBlockEntity);
    public static final WallSignBlock CRYSTAL_WALL_SIGN = addImmediately("crystal_wall_sign", new WallSignBlock(crystal.wallSign().dropsLike(CRYSTAL_SIGN), ParadiseLostSignType.CRYSTAL), signBlockEntity);
    // Wisteria Wood
    private static final WoodTypeFactory wisteria = new WoodTypeFactory(MapColor.PALE_YELLOW, MapColor.BROWN);
    public static final PillarBlock WISTERIA_LOG = add("wisteria_log", new PillarBlock(wisteria.log()), flammableLog);
    public static final PillarBlock WISTERIA_WOOD = add("wisteria_wood", new PillarBlock(wisteria.wood()), flammableLog);
    public static final PillarBlock STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", new PillarBlock(wisteria.strippedLog()), flammableLog, strippedFrom(WISTERIA_LOG));
    public static final PillarBlock STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", new PillarBlock(wisteria.strippedWood()), flammableLog, strippedFrom(WISTERIA_WOOD));
    public static final Block WISTERIA_PLANKS = add("wisteria_planks", new Block(wisteria.planks()), flammablePlanks);
    public static final FenceBlock WISTERIA_FENCE = add("wisteria_fence", new FenceBlock(wisteria.planks()), flammablePlanks);
    public static final FenceGateBlock WISTERIA_FENCE_GATE = add("wisteria_fence_gate", new FenceGateBlock(wisteria.planks()), flammablePlanks);
    public static final SlabBlock WISTERIA_SLAB = add("wisteria_slab", new SlabBlock(wisteria.planks()), flammablePlanks);
    public static final ParadiseLostStairsBlock WISTERIA_STAIRS = add("wisteria_stairs", new ParadiseLostStairsBlock(WISTERIA_PLANKS.getDefaultState(), wisteria.planks()), flammablePlanks);
    public static final ParadiseLostTrapdoorBlock WISTERIA_TRAPDOOR = add("wisteria_trapdoor", new ParadiseLostTrapdoorBlock(wisteria.trapdoor()), cutoutRenderLayer);
    public static final ParadiseLostDoorBlock WISTERIA_DOOR = add("wisteria_door", new ParadiseLostDoorBlock(wisteria.door()), cutoutRenderLayer);
    public static final WoodenButtonBlock WISTERIA_BUTTON = add("wisteria_button", new ParadiseLostWoodenButtonBlock(wisteria.button()));
    public static final ParadiseLostPressurePlateBlock WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", new ParadiseLostPressurePlateBlock(ActivationRule.EVERYTHING, wisteria.pressurePlate()));
    public static final SignBlock WISTERIA_SIGN = addImmediately("wisteria_sign", new SignBlock(wisteria.sign(), ParadiseLostSignType.WISTERIA), signBlockEntity);
    public static final WallSignBlock WISTERIA_WALL_SIGN = addImmediately("wisteria_wall_sign", new WallSignBlock(wisteria.wallSign().dropsLike(WISTERIA_SIGN), ParadiseLostSignType.WISTERIA), signBlockEntity);

    private static final WoodTypeFactory roseWisteria = wisteria.withLeafColor(MapColor.PINK);
    public static final WisteriaLeavesBlock ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new WisteriaLeavesBlock(roseWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new LeafPileBlock(roseWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new ParadiseLostSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new ParadiseLostHangerBlock(roseWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory frostWisteria = wisteria.withLeafColor(MapColor.LIGHT_BLUE);
    public static final WisteriaLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new WisteriaLeavesBlock(frostWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new LeafPileBlock(frostWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new ParadiseLostSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new ParadiseLostHangerBlock(frostWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory lavenderWisteria = wisteria.withLeafColor(MapColor.MAGENTA);
    public static final WisteriaLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new WisteriaLeavesBlock(lavenderWisteria.wisteriaLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new LeafPileBlock(lavenderWisteria.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new ParadiseLostSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteria.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new ParadiseLostHangerBlock(lavenderWisteria.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodTypeFactory borealWisteria = wisteria.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] borealWisteriaColors = new Vec3i[]{RenderUtils.toRGB(0xa6ffdd), RenderUtils.toRGB(0x96e5ff), RenderUtils.toRGB(0xd6b3ff), RenderUtils.toRGB(0xffadc6)};
    public static final AuralLeavesBlock BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteria.auralWisteriaLeaves(), false, borealWisteriaColors), flammableLeaves);
    public static final SaplingBlock BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new ParadiseLostSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteria.sapling().luminance(state -> 5)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)), cutoutRenderLayer);
    public static final AuralHangerBlock BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteria.auralHanger(), borealWisteriaColors), flammableLeaves, auralCutoutMippedRenderLayer);

    // Grasses
    private static Settings shrub() {
        return copy(Blocks.GRASS).mapColor(MapColor.PALE_GREEN);
    }

    public static final ParadiseLostBrushBlock GRASS = add("grass_plant", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock GRASS_FLOWERING = add("grass_flowering", new ParadiseLostBrushBlock(shrub().mapColor(MapColor.WHITE)), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock SHORT_GRASS = add("short_grass", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostTallBrushBlock TALL_GRASS = add("tall_grass", new ParadiseLostTallBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock FERN = add("fern", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock LOST_FERN = add("potted_fern", new FlowerPotBlock(FERN, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostBrushBlock BUSH = add("bush", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock FLUTEGRASS = add("flutegrass", new ParadiseLostBrushBlock(shrub().mapColor(MapColor.GOLD), ParadiseLostBlockTags.FLUTEGRASS_VALID_GROUND, true), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock SHAMROCK = add("shamrock", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), 0.99999), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock MALT_SPRIG = add("malt_sprig", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), 1), flammablePlant, cutoutRenderLayer);
    public static final TallWaterPlantBlock HONEY_NETTLE = add("honey_nettle", new TallWaterPlantBlock(shrub().sounds(BlockSoundGroup.SMALL_DRIPLEAF)), cutoutRenderLayer);
    public static final ParadiseLostSeagrassBlock HALOPHIA = add("halophia", new ParadiseLostSeagrassBlock(shrub().sounds(BlockSoundGroup.WET_GRASS)), cutoutRenderLayer);
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

    public static final WallClingingPlantBlock ROOTCAP = add("rootcap", new WallClingingPlantBlock(copy(BROWN_MUSHROOM), ParadiseLostBlockTags.FUNGI_CLINGABLES), cutoutRenderLayer);

    public static final AmadrysCropBlock AMADRYS = add("amadrys", new AmadrysCropBlock(shrub().mapColor(MapColor.PINK)), flammablePlant, cutoutRenderLayer);
    public static final FlaxCropBlock FLAX = add("flax", new FlaxCropBlock(shrub().mapColor(MapColor.OAK_TAN)), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostHangingRootsBlock WILD_SWETROOT = add("wild_swetroot", new ParadiseLostHangingRootsBlock(shrub().mapColor(MapColor.OAK_TAN)), flammablePlant, cutoutRenderLayer);
    public static final SwetrootCropBlock SWETROOT = add("swetroot", new SwetrootCropBlock(shrub().mapColor(MapColor.BLUE)), flammablePlant, cutoutRenderLayer);

    public static final Block FLAXWEAVE_CUSHION = add("flaxweave_cushion", new Block(Settings.of(Material.WOOL).mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F)), flammable(40, 10));

    public static final BlueberryBushBlock BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(of(Material.PLANT).strength(0.2f)
            .ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().suffocates(never).blockVision(never).noCollision()), flammablePlant, cutoutRenderLayer);

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
    public static final ParadiseLostTallBrushBlock WILD_FLAX = add("wild_flax", new ParadiseLostTallBrushBlock(flower()), flammablePlant, cutoutMippedRenderLayer);

    // Ores
    public static final OreBlock AMBROSIUM_ORE = add("ambrosium_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final OreBlock ZANITE_ORE = add("zanite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final FloatingBlock GRAVITITE_ORE = add("gravitite_ore", new FloatingBlock(false, of(Material.STONE).requiresTool().strength(5f).sounds(BlockSoundGroup.STONE), UniformIntProvider.create(0, 2)));
    public static final Block ZANITE_BLOCK = add("zanite_block", new Block(of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final FloatingBlock BLOCK_OF_GRAVITITE = add("block_of_gravitite", new FloatingBlock(false, of(Material.METAL).strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    // Misc
    public static final FloatingBlock GRAVITITE_LEVITATOR = add("gravitite_levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));
    public static final ChainBlock ZANITE_CHAIN = add("zanite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final AmbrosiumLanternBlock AMBROSIUM_LANTERN = add("ambrosium_lantern", new AmbrosiumLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);
    public static final ParadiseLostPortalBlock BLUE_PORTAL = add("blue_portal", new ParadiseLostPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucentRenderLayer);

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

//    public static final SwetDropBlock SWET_DROP = add("swet_drop", new SwetDropBlock(swetDrop(), () -> ParadiseLostEntityTypes.WHITE_SWET));
//    public static final SwetDropBlock BLUE_SWET_DROP = add("blue_swet_drop", new SwetDropBlock(swetDrop(), () -> ParadiseLostEntityTypes.BLUE_SWET));
//    public static final SwetDropBlock GOLDEN_SWET_DROP = add("golden_swet_drop", new SwetDropBlock(swetDrop(), () -> ParadiseLostEntityTypes.GOLDEN_SWET));
//    public static final SwetDropBlock PURPLE_SWET_DROP = add("purple_swet_drop", new SwetDropBlock(swetDrop(), () -> ParadiseLostEntityTypes.PURPLE_SWET));
    // Usables
    public static final IncubatorBlock INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);

    //dungeon
//    public static final DungeonSwitchBlock DUNGEON_SWITCH = add("dungeonswitch", new DungeonSwitchBlock(of(Material.METAL, MapColor.BLUE).strength(-1.0F, 3600000.0F)));

    // Chests
    /* 24Chrome: I removed all but skyroot from the creative menu, maybe we'll add them later but for now only skyroot will have textures! */
    public static final ParadiseLostChestBlock CRYSTAL_CHEST = add("crystal_chest", new ParadiseLostChestBlock(of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD), ()-> ParadiseLostBlockEntityTypes.CRYSTAL_CHEST));
    public static final ParadiseLostChestBlock GOLDEN_OAK_CHEST = add("golden_oak_chest", new ParadiseLostChestBlock(of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD), ()-> ParadiseLostBlockEntityTypes.GOLDEN_OAK_CHEST));
    public static final ParadiseLostChestBlock ORANGE_CHEST = add("orange_chest", new ParadiseLostChestBlock(of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD), ()-> ParadiseLostBlockEntityTypes.ORANGE_CHEST));
    public static final ParadiseLostChestBlock SKYROOT_CHEST = add("skyroot_chest", new ParadiseLostChestBlock(of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD), ()-> ParadiseLostBlockEntityTypes.SKYROOT_CHEST));
    public static final ParadiseLostChestBlock WISTERIA_CHEST = add("wisteria_chest", new ParadiseLostChestBlock(of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD), ()-> ParadiseLostBlockEntityTypes.WISTERIA_CHEST));

    @SafeVarargs
    private static <V extends Block> V add(String id, V block, Action<? super V>... additionalActions) {
        return ParadiseLostRegistryQueues.BLOCK.add(locate(id), block, additionalActions);
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
        ParadiseLostRegistryQueues.BLOCK.register();
    }

    /**
     * Takes tree colours, and produces various block settings for that tree to ease the creation of wooden block sets
     */
    private record WoodTypeFactory(MapColor woodColor, MapColor barkColor, MapColor leafColor,
                                   MapColor plankColor) {
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

        public Settings sign() {
            return copy(OAK_SIGN).mapColor(this.plankColor);
        }

        public Settings wallSign() {
            return copy(OAK_WALL_SIGN).mapColor(this.plankColor);
        }

        private static Settings aural(Settings settings) {
            return settings.strength(0.05f).luminance(state -> 12).emissiveLighting(always).postProcess(always);
        }
    }

    private static class ParadiseLostWoodenButtonBlock extends WoodenButtonBlock {
        protected ParadiseLostWoodenButtonBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostDoorBlock extends DoorBlock {
        public ParadiseLostDoorBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostFarmlandBlock extends FarmlandBlock {
        public ParadiseLostFarmlandBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostPaneBlock extends PaneBlock {
        public ParadiseLostPaneBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostPressurePlateBlock extends PressurePlateBlock {
        public ParadiseLostPressurePlateBlock(ActivationRule type, Settings settings) {
            super(type, settings);
        }
    }

    private static class ParadiseLostStairsBlock extends StairsBlock {
        public ParadiseLostStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }

    private static class ParadiseLostTrapdoorBlock extends TrapdoorBlock {
        public ParadiseLostTrapdoorBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostWeightedPressurePlateBlock extends WeightedPressurePlateBlock {
        public ParadiseLostWeightedPressurePlateBlock(int weight, Settings settings) {
            super(weight, settings);
        }
    }

    private static class ParadiseLostHangingRootsBlock extends HangingRootsBlock {
        protected ParadiseLostHangingRootsBlock(Settings settings) {
            super(settings);
        }
    }
}
