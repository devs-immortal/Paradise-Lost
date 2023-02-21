package net.id.paradiselost.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.incubus_core.woodtypefactory.api.chest.ChestFactory;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.decorative.*;
import net.id.paradiselost.blocks.mechanical.*;
import net.id.paradiselost.blocks.natural.*;
import net.id.paradiselost.blocks.natural.cloud.*;
import net.id.paradiselost.blocks.natural.crop.*;
import net.id.paradiselost.blocks.natural.plant.*;
import net.id.paradiselost.blocks.natural.tree.*;
import net.id.paradiselost.fluids.ParadiseLostFluids;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.util.RenderUtils;
import net.id.paradiselost.world.feature.tree.generator.*;
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

import java.util.List;

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

    public static final ParadiseLostGrassBlock HIGHLANDS_GRASS = add("highlands_grass", new ParadiseLostGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable());
    public static final ParadiseLostSnowyBlock FROZEN_GRASS = add("frozen_grass", new ParadiseLostSnowyBlock(grassBlock().mapColor(MapColor.WHITE).strength(2F).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    // Soil Blocks
    public static final Block DIRT = add("dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), tillable(), flattenable());
    public static final Block COARSE_DIRT = add("coarse_dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), coarseTillable(), flattenable());
    public static final Block PERMAFROST = add("permafrost", new Block(copy(Blocks.DIRT).strength(2f).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable());
    public static final FarmlandBlock FARMLAND = add("farmland", new ParadiseLostFarmlandBlock(copy(Blocks.FARMLAND)));
    public static final ParadiseLostDirtPathBlock DIRT_PATH = add("grass_path", new ParadiseLostDirtPathBlock(copy(Blocks.DIRT_PATH)));
    public static final Block PACKED_SWEDROOT = add("packed_swedroot", new Block(of(Material.WOOD).strength(2f).sounds(BlockSoundGroup.SHROOMLIGHT)));

    // Glass Blocks

    // Clouds
    private static Settings cloud() {
        return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never);
    }

    public static final ParadiseLostCloudBlock COLD_CLOUD = add("cold_cloud", new ParadiseLostCloudBlock(cloud().mapColor(MapColor.WHITE)), translucentRenderLayer);
    public static final BlueParadiseLostCloudBlock BLUE_CLOUD = add("blue_cloud", new BlueParadiseLostCloudBlock(cloud().mapColor(MapColor.LIGHT_BLUE)), translucentRenderLayer);
    public static final PinkParadiseLostCloudBlock PINK_CLOUD = add("pink_cloud", new PinkParadiseLostCloudBlock(cloud().mapColor(MapColor.PINK)), translucentRenderLayer);
    public static final GoldenParadiseLostCloudBlock GOLDEN_CLOUD = add("golden_cloud", new GoldenParadiseLostCloudBlock(cloud().mapColor(MapColor.GOLD)), translucentRenderLayer);
    // Fluids
    public static final FluidBlock DENSE_CLOUD = add("dense_cloud", new FluidBlock(ParadiseLostFluids.DENSE_CLOUD, of(Material.WATER).noCollision().strength(100f).dropsNothing()));
    public static final FluidBlock SPRING_WATER = add("spring_water", new FluidBlock(ParadiseLostFluids.SPRING_WATER, of(Material.WATER).noCollision().strength(100f).dropsNothing()));
    // Organic Extra
    public static final Block VITROULITE = add("vitroulite", new Block(of(Material.DENSE_ICE).requiresTool().hardness(0.5f).sounds(BlockSoundGroup.GLASS)));

    // Smooth Floestone
    private static Settings floestone() {
        return of(Material.STONE, MapColor.WHITE_GRAY).requiresTool().strength(0.5f, 1f).sounds(BlockSoundGroup.STONE);
    }

    public static final Block FLOESTONE = add("floestone", new Block(floestone()));
    public static final SlabBlock FLOESTONE_SLAB = add("floestone_slab", new SlabBlock(floestone()));
    public static final ParadiseLostStairsBlock FLOESTONE_STAIRS = add("floestone_stairs", new ParadiseLostStairsBlock(FLOESTONE.getDefaultState(), floestone()));
    public static final WallBlock FLOESTONE_WALL = add("floestone_wall", new WallBlock(floestone()));

    // Cobbled Floestone
    private static Settings cobbledFloestone() {
        return floestone().strength(0.4f, 8f);
    }

    public static final Block COBBLED_FLOESTONE = add("cobbled_floestone", new Block(cobbledFloestone()));
    public static final SlabBlock COBBLED_FLOESTONE_SLAB = add("cobbled_floestone_slab", new SlabBlock(cobbledFloestone()));
    public static final ParadiseLostStairsBlock COBBLED_FLOESTONE_STAIRS = add("cobbled_floestone_stairs", new ParadiseLostStairsBlock(COBBLED_FLOESTONE.getDefaultState(), cobbledFloestone()));
    public static final WallBlock COBBLED_FLOESTONE_WALL = add("cobbled_floestone_wall", new WallBlock(cobbledFloestone()));

    // Mossy Floestone
    private static Settings mossyCobbledFloestone() {
        return cobbledFloestone().mapColor(MapColor.PALE_GREEN);
    }

    public static final Block MOSSY_FLOESTONE = add("mossy_floestone", new Block(mossyCobbledFloestone()));
    public static final Block GOLDEN_MOSSY_FLOESTONE = add("golden_mossy_floestone", new Block(mossyCobbledFloestone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final SlabBlock MOSSY_FLOESTONE_SLAB = add("mossy_floestone_slab", new SlabBlock(mossyCobbledFloestone()));
    public static final ParadiseLostStairsBlock MOSSY_FLOESTONE_STAIRS = add("mossy_floestone_stairs", new ParadiseLostStairsBlock(MOSSY_FLOESTONE.getDefaultState(), mossyCobbledFloestone()));
    public static final WallBlock MOSSY_FLOESTONE_WALL = add("mossy_floestone_wall", new WallBlock(mossyCobbledFloestone()));

    // Floestone Bricks
    private static Settings floestoneBrick() {
        return floestone().strength(1.5f, 6f);
    }

    public static final Block FLOESTONE_BRICK = add("floestone_brick", new Block(floestoneBrick()));
    public static final SlabBlock FLOESTONE_BRICK_SLAB = add("floestone_brick_slab", new SlabBlock(floestoneBrick()));
    public static final ParadiseLostStairsBlock FLOESTONE_BRICK_STAIRS = add("floestone_brick_stairs", new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), floestoneBrick()));
    public static final WallBlock FLOESTONE_BRICK_WALL = add("floestone_brick_wall", new WallBlock(floestoneBrick()));

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

    private static Settings amberTiles() {
        return of(Material.STONE, MapColor.GOLD).requiresTool().sounds(BlockSoundGroup.CALCITE).strength(2, 6);
    }

    public static final Block GOLDEN_AMBER_TILE = add("golden_amber_tile", new Block(amberTiles()));
    public static final SlabBlock GOLDEN_AMBER_TILE_SLAB = add("golden_amber_tile_slab", new SlabBlock(amberTiles()));
    public static final ParadiseLostStairsBlock GOLDEN_AMBER_TILE_STAIRS = add("golden_amber_tile_stairs", new ParadiseLostStairsBlock(GOLDEN_AMBER_TILE.getDefaultState(), amberTiles()));

    protected static Settings flowerPot() {
        return copy(POTTED_OAK_SAPLING);
    }

    public static final CampfireBlock CHERINE_CAMPFIRE = add("cherine_campfire", new CherineCampfireBlock(false, 1, Settings.copy(CAMPFIRE)), cutoutRenderLayer);

    // Aurel Wood
    private static final WoodSettingsFactory aurelColors = new WoodSettingsFactory(MapColor.DARK_RED, MapColor.DARK_RED);
    public static final WoodTypeFactory AUREL = new WoodTypeFactory(aurelColors, locate("aurel"), new AurelSaplingGenerator());
    public static final ChestFactory AUREL_CHEST_FACTORY = new ChestFactory(ParadiseLost.MOD_ID, "skyroot", AUREL.settings.chest());


    public static final SaplingBlock AUREL_SAPLING = AUREL.sapling();
    public static final FlowerPotBlock POTTED_AUREL_SAPLING = AUREL.pottedSapling();
    public static final PillarBlock AUREL_LOG = AUREL.log();
    public static final PillarBlock AUREL_WOOD = AUREL.wood();
    public static final PillarBlock STRIPPED_AUREL_LOG = AUREL.strippedLog();
    public static final PillarBlock STRIPPED_AUREL_WOOD = AUREL.strippedWood();
    public static final PillarBlock MOTTLED_AUREL_LOG = add("mottled_aurel_log", new PillarBlock(aurelColors.log()), flammableLog, stripsTo(STRIPPED_AUREL_LOG));
    public static final ChuteBlock MOTTLED_AUREL_FALLEN_LOG = add("mottled_aurel_fallen_log", new ChuteBlock(aurelColors.log()), flammableLog, cutoutRenderLayer);
    public static final LeavesBlock AUREL_LEAVES = AUREL.leaves();
    public static final LeafPileBlock AUREL_LEAF_PILE = add("aurel_leaf_pile", new LeafPileBlock(aurelColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block AUREL_PLANKS = AUREL.planks();
    public static final Block AUREL_BOOKSHELF = add("aurel_bookshelf", new Block(copy(BOOKSHELF).mapColor(aurelColors.plankColor())), flammable(30, 20));
    public static final FenceBlock AUREL_FENCE = AUREL.fence();
    public static final FenceGateBlock AUREL_FENCE_GATE = AUREL.fenceGate();
    public static final SlabBlock AUREL_SLAB = AUREL.slab();
    public static final StairsBlock AUREL_STAIRS = AUREL.stairs();
    public static final TrapdoorBlock AUREL_TRAPDOOR = AUREL.trapdoor();
    public static final DoorBlock AUREL_DOOR = AUREL.door();
    public static final WoodenButtonBlock AUREL_BUTTON = AUREL.button();
    public static final PressurePlateBlock AUREL_PRESSURE_PLATE = AUREL.pressurePlate();
    public static final SignBlock AUREL_SIGN = AUREL.signFactory().signBlock;
    public static final WallSignBlock AUREL_WALL_SIGN = AUREL.signFactory().wallSignBlock;
    // Mother Aurel Wood
    private static final WoodSettingsFactory motherAurelColors = new WoodSettingsFactory(MapColor.OAK_TAN, MapColor.TERRACOTTA_RED, MapColor.GOLD, MapColor.TERRACOTTA_RED);
    public static final WoodTypeFactory MOTHER_AUREL = new WoodTypeFactory(motherAurelColors, locate("mother_aurel"));
    public static final ChestFactory MOTHER_AUREL_CHEST_FACTORY = new ChestFactory(ParadiseLost.MOD_ID, "golden_oak", MOTHER_AUREL.settings.chest());

    public static final SaplingBlock MOTHER_AUREL_SAPLING = add("mother_aurel_sapling", new ParadiseLostSaplingBlock(new MotherAurelSaplingGenerator(), motherAurelColors.sapling().luminance(state -> 7)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_MOTHER_AUREL_SAPLING = add("potted_mother_aurel_sapling", new FlowerPotBlock(MOTHER_AUREL_SAPLING, flowerPot().luminance(state -> 7)), cutoutRenderLayer);
    public static final PillarBlock MOTHER_AUREL_LOG = MOTHER_AUREL.log();
    public static final PillarBlock MOTHER_AUREL_WOOD = MOTHER_AUREL.wood();
    public static final PillarBlock STRIPPED_MOTHER_AUREL_LOG = MOTHER_AUREL.strippedLog();
    public static final PillarBlock STRIPPED_MOTHER_AUREL_WOOD = MOTHER_AUREL.strippedWood();
    public static final ParadiseLostLeavesBlock MOTHER_AUREL_LEAVES = add("mother_aurel_leaves", new ParadiseLostLeavesBlock(motherAurelColors.leaves().luminance((value -> 11)), true), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block MOTHER_AUREL_PLANKS = MOTHER_AUREL.planks();
    public static final FenceBlock MOTHER_AUREL_FENCE = MOTHER_AUREL.fence();
    public static final FenceGateBlock MOTHER_AUREL_FENCE_GATE = MOTHER_AUREL.fenceGate();
    public static final SlabBlock MOTHER_AUREL_SLAB = MOTHER_AUREL.slab();
    public static final StairsBlock MOTHER_AUREL_STAIRS = MOTHER_AUREL.stairs();
    public static final TrapdoorBlock MOTHER_AUREL_TRAPDOOR = MOTHER_AUREL.trapdoor();
    public static final DoorBlock MOTHER_AUREL_DOOR = MOTHER_AUREL.door();
    public static final WoodenButtonBlock MOTHER_AUREL_BUTTON = MOTHER_AUREL.button();
    public static final PressurePlateBlock MOTHER_AUREL_PRESSURE_PLATE = MOTHER_AUREL.pressurePlate();
    public static final SignBlock MOTHER_AUREL_SIGN = MOTHER_AUREL.signFactory().signBlock;
    public static final WallSignBlock MOTHER_AUREL_WALL_SIGN = MOTHER_AUREL.signFactory().wallSignBlock;
    // Orange Wood
    private static final WoodSettingsFactory orangeColors = new WoodSettingsFactory(MapColor.RAW_IRON_PINK, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.GREEN);
    public static final WoodTypeFactory ORANGE = new WoodTypeFactory(orangeColors, locate("orange"), new OrangeSaplingGenerator());

    public static final SaplingBlock ORANGE_SAPLING = ORANGE.sapling();
    public static final FlowerPotBlock POTTED_ORANGE_SAPLING = ORANGE.pottedSapling();
    public static final PillarBlock ORANGE_LOG = ORANGE.log();
    public static final PillarBlock ORANGE_WOOD = ORANGE.wood();
    public static final PillarBlock STRIPPED_ORANGE_LOG = ORANGE.strippedLog();
    public static final PillarBlock STRIPPED_ORANGE_WOOD = ORANGE.strippedWood();
    public static final FruitingLeavesBlock ORANGE_LEAVES = add("orange_leaves", new FruitingLeavesBlock(orangeColors.leaves().sounds(BlockSoundGroup.AZALEA_LEAVES), () -> ParadiseLostItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
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
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new ParadiseLostSaplingBlock(new RoseWisteriaSaplingGenerator(), roseWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new ParadiseLostHangerBlock(roseWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory frostWisteriaColors = wisteriaColors.withLeafColor(MapColor.LIGHT_BLUE);
    public static final WisteriaLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new WisteriaLeavesBlock(frostWisteriaColors.noCollideLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new LeafPileBlock(frostWisteriaColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new ParadiseLostSaplingBlock(new FrostWisteriaSaplingGenerator(), frostWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new ParadiseLostHangerBlock(frostWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory lavenderWisteriaColors = wisteriaColors.withLeafColor(MapColor.MAGENTA);
    public static final WisteriaLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new WisteriaLeavesBlock(lavenderWisteriaColors.noCollideLeaves(), false), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new LeafPileBlock(lavenderWisteriaColors.leafPile()), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new ParadiseLostSaplingBlock(new LavenderWisteriaSaplingGenerator(), lavenderWisteriaColors.sapling()), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new ParadiseLostHangerBlock(lavenderWisteriaColors.hanger()), flammableLeaves, cutoutRenderLayer);

    private static final WoodSettingsFactory borealWisteriaColors = wisteriaColors.withLeafColor(MapColor.CYAN);
    private static final Vec3i[] auralLeafColors = new Vec3i[]{RenderUtils.toRGB(0xa6ffdd), RenderUtils.toRGB(0x96e5ff), RenderUtils.toRGB(0xd6b3ff), RenderUtils.toRGB(0xffadc6)};
    public static final AuralLeavesBlock BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", new AuralLeavesBlock(borealWisteriaColors.auralNoCollideLeaves(), false, auralLeafColors), flammableLeaves);
    public static final SaplingBlock BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", new ParadiseLostSaplingBlock(new BorealWisteriaSaplingGenerator(), borealWisteriaColors.sapling().luminance(state -> 5)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_BOREAL_WISTERIA_SAPLING = add("potted_boreal_wisteria_sapling", new FlowerPotBlock(BOREAL_WISTERIA_SAPLING, flowerPot().luminance(state -> 5)), cutoutRenderLayer);
    public static final AuralHangerBlock BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", new AuralHangerBlock(borealWisteriaColors.auralHanger(), auralLeafColors), flammableLeaves, auralCutoutMippedRenderLayer);

    // Grasses
    private static Settings shrub() {
        return copy(Blocks.GRASS).mapColor(MapColor.PALE_GREEN);
    }
    private static Settings crop() {
        return copy(Blocks.WHEAT).mapColor(MapColor.PALE_GREEN);
    }

    public static final ParadiseLostBrushBlock GRASS = add("grass_plant", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock GRASS_FLOWERING = add("grass_flowering", new ParadiseLostBrushBlock(shrub().mapColor(MapColor.WHITE)), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock SHORT_GRASS = add("short_grass", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostTallBrushBlock TALL_GRASS = add("tall_grass", new ParadiseLostTallBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock FERN = add("fern", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FERN = add("potted_fern", new FlowerPotBlock(FERN, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostBrushBlock BUSH = add("bush", new ParadiseLostBrushBlock(shrub()), flammablePlant, cutoutRenderLayer);
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

    public static final GlowLichenBlock SWEDROOT_SPREAD = add("swedroot_spread", new GlowLichenBlock(Settings.of(Material.REPLACEABLE_PLANT, MapColor.OAK_TAN).noCollision().strength(1F).sounds(BlockSoundGroup.SHROOMLIGHT)), cutoutRenderLayer);

    public static final WallClingingPlantBlock ROOTCAP = add("rootcap", new WallClingingPlantBlock(copy(BROWN_MUSHROOM), ParadiseLostBlockTags.FUNGI_CLINGABLES), cutoutRenderLayer);
    public static final ParadiseLostMushroomPlantBlock BROWN_SPORECAP = add("brown_sporecap", new ParadiseLostMushroomPlantBlock(copy(BROWN_MUSHROOM), BlockTags.MUSHROOM_GROW_BLOCK), cutoutRenderLayer);
    public static final ParadiseLostHangingMushroomPlantBlock PINK_SPORECAP = add("pink_sporecap", new ParadiseLostHangingMushroomPlantBlock(copy(BROWN_MUSHROOM), BlockTags.MUSHROOM_GROW_BLOCK), cutoutRenderLayer);

    public static final AmadrysCropBlock AMADRYS = add("amadrys", new AmadrysCropBlock(crop().mapColor(MapColor.PINK)), flammablePlant, cutoutMippedRenderLayer);
    public static final FlaxCropBlock FLAX = add("flax", new FlaxCropBlock(crop().mapColor(MapColor.OAK_TAN)), flammablePlant, cutoutRenderLayer);
    public static final SwedrootCropBlock SWEDROOT = add("swedroot", new SwedrootCropBlock(shrub().mapColor(MapColor.BLUE)), flammablePlant, cutoutRenderLayer);

    public static final Block FLAXWEAVE_CUSHION = add("flaxweave_cushion", new Block(Settings.of(Material.WOOL).mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F)), flammable(40, 10));

    public static final BlackcurrantBushBlock BLACKCURRANT_BUSH = add("blackcurrant_bush", new BlackcurrantBushBlock(of(Material.PLANT).strength(0.2f)
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
    public static final ParadiseLostTallBrushBlock WILD_FLAX = add("wild_flax", new ParadiseLostTallBrushBlock(flower()), flammablePlant, cutoutMippedRenderLayer);

    // Ores
    public static final OreBlock CHERINE_ORE = add("cherine_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(0, 2)));
    public static final OreBlock OLVITE_ORE = add("olvite_ore", new OreBlock(of(Material.STONE).requiresTool().strength(3f), UniformIntProvider.create(1, 3)));
    public static final OreBlock SURTRUM = add("surtrum", new SurtrumOreBlock(of(Material.STONE).sounds(BlockSoundGroup.NETHER_GOLD_ORE).requiresTool().strength(9f, 12f), UniformIntProvider.create(2, 5)));
    public static final Block CHERINE_BLOCK = add("cherine_block", new Block(of(Material.METAL).requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.STONE)));
    public static final Block OLVITE_BLOCK = add("olvite_block", new Block(of(Material.METAL).requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block REFINED_SURTRUM_BLOCK = add("refined_surtrum_block", new Block(of(Material.METAL).requiresTool().strength(4f, -1f).sounds(BlockSoundGroup.METAL)));
    // Misc
    public static final FloatingBlock LEVITATOR = add("levitator", new FloatingBlock(true, of(Material.WOOD).strength(3f, 3f).sounds(BlockSoundGroup.WOOD)));
    public static final ChainBlock OLVITE_CHAIN = add("olvite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final CherineLanternBlock CHERINE_LANTERN = add("cherine_lantern", new CherineLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);
    public static final ParadiseLostPortalBlock BLUE_PORTAL = add("blue_portal", new ParadiseLostPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucentRenderLayer);

    // Torches
    private static Settings cherineTorch() {
        return copy(TORCH).ticksRandomly().luminance(state -> 15);
    }

    public static final CherineTorchBlock CHERINE_TORCH = addImmediately("cherine_torch", new CherineTorchBlock(cherineTorch()), cutoutRenderLayer);
    public static final CherineWallTorchBlock CHERINE_TORCH_WALL = addImmediately("cherine_wall_torch", new CherineWallTorchBlock(cherineTorch().dropsLike(CHERINE_TORCH)), cutoutRenderLayer);

    // Usables
    public static final IncubatorBlock INCUBATOR = add("incubator", new IncubatorBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", new FoodBowlBlock(of(Material.WOOD, MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);

    //dungeon
//    public static final DungeonSwitchBlock DUNGEON_SWITCH = add("dungeonswitch", new DungeonSwitchBlock(of(Material.METAL, MapColor.BLUE).strength(-1.0F, 3600000.0F)));

    // Chests
    public static final ChestFactory CRYSTAL_CHEST_FACTORY = new ChestFactory(ParadiseLost.MOD_ID, "crystal", AUREL.settings.chest());

    public static final ChestBlock CRYSTAL_CHEST = add("crystal_chest", CRYSTAL_CHEST_FACTORY.chest);
    public static final ChestBlock MOTHER_AUREL_CHEST = add("golden_oak_chest", MOTHER_AUREL_CHEST_FACTORY.chest);
    public static final ChestBlock ORANGE_CHEST = ORANGE.chestFactory().chest;
    public static final ChestBlock AUREL_CHEST = add("skyroot_chest", AUREL_CHEST_FACTORY.chest);
    public static final ChestBlock WISTERIA_CHEST = WISTERIA.chestFactory().chest;

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

        for (var woodType : List.of(AUREL, MOTHER_AUREL, ORANGE, WISTERIA)) {
            woodType.registerCreatedBlocks();
            woodType.registerFlammability();
            woodType.registerStripping();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        for (var woodType : List.of(AUREL, MOTHER_AUREL, ORANGE, WISTERIA)) {
            woodType.registerBlockEntityRenderers();
            woodType.registerRenderLayers();
        }
        AUREL_CHEST_FACTORY.registerChestRenderers();
        MOTHER_AUREL_CHEST_FACTORY.registerChestRenderers();
        CRYSTAL_CHEST_FACTORY.registerChestRenderers();
    }

    private static class ParadiseLostFarmlandBlock extends FarmlandBlock {
        ParadiseLostFarmlandBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostPaneBlock extends PaneBlock {
        ParadiseLostPaneBlock(Settings settings) {
            super(settings);
        }
    }

    private static class ParadiseLostStairsBlock extends StairsBlock {
        ParadiseLostStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }
}
