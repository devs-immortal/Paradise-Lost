package net.id.paradiselost.blocks;

import net.id.paradiselost.blocks.decorative.*;
import net.id.paradiselost.blocks.mechanical.*;
import net.id.paradiselost.blocks.natural.*;
import net.id.paradiselost.blocks.natural.cloud.*;
import net.id.paradiselost.blocks.natural.crop.*;
import net.id.paradiselost.blocks.natural.plant.*;
import net.id.paradiselost.blocks.natural.tree.*;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.id.paradiselost.world.feature.tree.ParadiseLostSaplingGenerators;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import static net.id.paradiselost.blocks.ParadiseLostBlockActions.*;
import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.AbstractBlock.Settings.create;
import static net.minecraft.block.Blocks.*;
import static net.id.paradiselost.blocks.BlockRegistration.*;

@SuppressWarnings("unused")
public class ParadiseLostBlocks {

	protected static Settings unbreakable(AbstractBlock.Settings settings) {
        return settings.strength(-1f, 3600000f);
    }

    // Grass Blocks
    private static Settings grassBlock() {
        return copy(Blocks.GRASS_BLOCK).mapColor(MapColor.LICHEN_GREEN).strength(0.4f);
    }
    private static Settings permafrost() {
        return copy(Blocks.DIRT).strength(2f).sounds(BlockSoundGroup.GILDED_BLACKSTONE);
    }

    // Soil Blocks
    public static final FarmlandBlock FARMLAND = add("farmland", ParadiseLostFarmlandBlock::new, copy(Blocks.FARMLAND));
    public static final ParadiseLostDirtPathBlock DIRT_PATH = add("grass_path", settings -> new ParadiseLostDirtPathBlock(settings, () -> ParadiseLostBlocks.DIRT), copy(Blocks.DIRT_PATH).mapColor(MapColor.TERRACOTTA_CYAN));
    public static final ParadiseLostDirtPathBlock PERMAFROST_PATH = add("frozen_path", settings -> new ParadiseLostDirtPathBlock(settings, () -> ParadiseLostBlocks.PERMAFROST), permafrost());
    public static final ParadiseLostGrassBlock HIGHLANDS_GRASS = add("highlands_grass", ParadiseLostGrassBlock::new, grassBlock(), cutoutMippedRenderLayer, tillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final ParadiseLostSnowyBlock FROZEN_GRASS = add("frozen_grass", ParadiseLostSnowyBlock::new, grassBlock().mapColor(MapColor.WHITE).strength(2F).sounds(BlockSoundGroup.GILDED_BLACKSTONE), flattenable(ParadiseLostBlocks.PERMAFROST_PATH));

    public static final Block DIRT = add("dirt", Block::new, copy(Blocks.DIRT).strength(0.3f).mapColor(MapColor.BROWN), tillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final Block COARSE_DIRT = add("coarse_dirt", Block::new, copy(Blocks.DIRT).mapColor(MapColor.BROWN).strength(0.3f), coarseTillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final FloatingBlock LEVITA = add("levita", settings -> new FloatingBlock(false, settings), copy(Blocks.GRAVEL).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).strength(0.3f));
    public static final Block PERMAFROST = add("permafrost", Block::new, permafrost(), flattenable(ParadiseLostBlocks.PERMAFROST_PATH));
    public static final Block PACKED_SWEDROOT = add("packed_swedroot", Block::new, create().strength(2f).sounds(BlockSoundGroup.SHROOMLIGHT).instrument(NoteBlockInstrument.BANJO));
    public static final Block LIVERWORT = add("liverwort", Block::new, copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES));
    public static final CarpetBlock LIVERWORT_CARPET = add("liverwort_carpet", CarpetBlock::new, copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES));

    public static final SimpleBlockSet THATCH_SET = registerSimpleBlockSet("thatch", create().mapColor(MapColor.PALE_YELLOW).strength(0.3f).sounds(BlockSoundGroup.GRASS).instrument(NoteBlockInstrument.BANJO));


    // Clouds
    private static Settings cloud() {
        return create().strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never);
    }

    public static final ParadiseLostCloudBlock COLD_CLOUD = add("cold_cloud", ParadiseLostCloudBlock::new, cloud().mapColor(MapColor.LIGHT_BLUE_GRAY), translucentRenderLayer);
    public static final BlueParadiseLostCloudBlock BLUE_CLOUD = add("blue_cloud", BlueParadiseLostCloudBlock::new, cloud().mapColor(MapColor.CYAN), translucentRenderLayer);
    public static final GoldenParadiseLostCloudBlock GOLDEN_CLOUD = add("golden_cloud", GoldenParadiseLostCloudBlock::new, cloud().mapColor(MapColor.GOLD), translucentRenderLayer);
    public static final DirectionalParadiseLostCloudBlock GREEN_CLOUD = add("green_cloud", DirectionalParadiseLostCloudBlock::new, cloud().mapColor(MapColor.EMERALD_GREEN), translucentRenderLayer);

    // Floestone
    private static Settings floestone() {
        return create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(0.5f, 5f).sounds(BlockSoundGroup.STONE).instrument(NoteBlockInstrument.BASEDRUM);
    }

    public static final Block FLOESTONE = add("floestone", Block::new, floestone());
    public static final SlabBlock FLOESTONE_SLAB = add("floestone_slab", SlabBlock::new, floestone());
    public static final ParadiseLostStairsBlock FLOESTONE_STAIRS = add("floestone_stairs", settings -> new ParadiseLostStairsBlock(FLOESTONE.getDefaultState(), settings), floestone());
    public static final WallBlock FLOESTONE_WALL = add("floestone_wall", WallBlock::new, floestone());

    // Cobbled Floestone
    private static Settings cobbledFloestone() {
        return floestone().strength(0.4f, 8f);
    }

    public static final Block COBBLED_FLOESTONE = add("cobbled_floestone", Block::new, cobbledFloestone().mapColor(MapColor.LIGHT_GRAY));
    public static final SlabBlock COBBLED_FLOESTONE_SLAB = add("cobbled_floestone_slab", SlabBlock::new, cobbledFloestone().mapColor(MapColor.LIGHT_GRAY));
    public static final ParadiseLostStairsBlock COBBLED_FLOESTONE_STAIRS = add("cobbled_floestone_stairs", settings -> new ParadiseLostStairsBlock(COBBLED_FLOESTONE.getDefaultState(), settings), cobbledFloestone().mapColor(MapColor.LIGHT_GRAY));
    public static final WallBlock COBBLED_FLOESTONE_WALL = add("cobbled_floestone_wall", WallBlock::new, cobbledFloestone().mapColor(MapColor.LIGHT_GRAY));

    // Mossy Floestone
    private static Settings mossyCobbledFloestone() {
        return cobbledFloestone().mapColor(MapColor.PALE_GREEN);
    }

    public static final Block MOSSY_FLOESTONE = add("mossy_floestone", Block::new, mossyCobbledFloestone().mapColor(MapColor.LICHEN_GREEN));
    public static final Block GOLDEN_MOSSY_FLOESTONE = add("golden_mossy_floestone", Block::new, mossyCobbledFloestone().strength(2f, 6f).mapColor(MapColor.GOLD));
    public static final SlabBlock MOSSY_FLOESTONE_SLAB = add("mossy_floestone_slab", SlabBlock::new, mossyCobbledFloestone().mapColor(MapColor.LICHEN_GREEN));
    public static final ParadiseLostStairsBlock MOSSY_FLOESTONE_STAIRS = add("mossy_floestone_stairs", settings -> new ParadiseLostStairsBlock(MOSSY_FLOESTONE.getDefaultState(), settings), mossyCobbledFloestone().mapColor(MapColor.LICHEN_GREEN));
    public static final WallBlock MOSSY_FLOESTONE_WALL = add("mossy_floestone_wall", WallBlock::new, mossyCobbledFloestone().mapColor(MapColor.LICHEN_GREEN));

    // Floestone brick
    private static Settings floestoneBrick() {
        return floestone().strength(1.5f, 6f);
    }

    public static final Block FLOESTONE_BRICK = add("floestone_brick", Block::new, floestoneBrick());
    public static final Block CHISELED_FLOESTONE = add("chiseled_floestone", Block::new, floestoneBrick());
    public static final SlabBlock FLOESTONE_BRICK_SLAB = add("floestone_brick_slab", SlabBlock::new, floestoneBrick());
    public static final ParadiseLostStairsBlock FLOESTONE_BRICK_STAIRS = add("floestone_brick_stairs", settings -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), settings), floestoneBrick());
    public static final WallBlock FLOESTONE_BRICK_WALL = add("floestone_brick_wall", WallBlock::new, floestoneBrick());
    public static final Block SMOOTH_FLOESTONE = add("smooth_floestone", Block::new, floestoneBrick());
    public static final SlabBlock SMOOTH_FLOESTONE_SLAB = add("smooth_floestone_slab", SlabBlock::new, floestoneBrick());
    public static final ParadiseLostStairsBlock SMOOTH_FLOESTONE_STAIRS = add("smooth_floestone_stairs", settings -> new ParadiseLostStairsBlock(SMOOTH_FLOESTONE.getDefaultState(), settings), floestoneBrick());

    // Heliolith
    public static final Block HELIOLITH = add("heliolith", Block::new, floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final Block SMOOTH_HELIOLITH = add("smooth_heliolith", Block::new, floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final SlabBlock HELIOLITH_SLAB = add("heliolith_slab", SlabBlock::new, floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final SlabBlock SMOOTH_HELIOLITH_SLAB = add("smooth_heliolith_slab", SlabBlock::new, floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final ParadiseLostStairsBlock HELIOLITH_STAIRS = add("heliolith_stairs", settings -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), settings), floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final ParadiseLostStairsBlock SMOOTH_HELIOLITH_STAIRS = add("smooth_heliolith_stairs", settings -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), settings), floestone().mapColor(MapColor.TERRACOTTA_WHITE));
    public static final WallBlock HELIOLITH_WALL = add("heliolith_wall", WallBlock::new, floestone());

    // Levita Brick
    public static final SimpleBlockSet LEVITA_BRICK_SET = registerSimpleBlockSet("levita_brick", create().mapColor(MapColor.LIGHT_BLUE_GRAY).strength(0.3f, 3f).sounds(BlockSoundGroup.CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final Block CHISELED_LEVITA_BRICK = add("chiseled_levita_brick", Block::new, create().mapColor(MapColor.LIGHT_BLUE_GRAY).strength(0.3f, 3f).sounds(BlockSoundGroup.CALCITE).instrument(NoteBlockInstrument.BASEDRUM));

    // Burnished Stone
    private static Settings burnishedStone() {
        return create().mapColor(MapColor.DEEPSLATE_GRAY).strength(4f, 6f).requiresTool().instrument(NoteBlockInstrument.BASEDRUM);
    }
    public static final SimpleBlockSet BURNISHED_STONE_SET = registerSimpleBlockSet("burnished_stone", burnishedStone());
    public static final WallBlock BURNISHED_STONE_WALL = add("burnished_stone_wall", WallBlock::new, burnishedStone());
    public static final Block BURNISHED_STONE_PLAQUE = add("burnished_stone_plaque", Block::new, burnishedStone());
    public static final Block BURNISHED_STONE_SCRIPT = add("burnished_stone_script", Block::new, burnishedStone());


    private static Settings amberTiles() {
        return create().mapColor(MapColor.GOLD).requiresTool().sounds(BlockSoundGroup.CALCITE).strength(2, 6).instrument(NoteBlockInstrument.BASEDRUM);
    }

    // Golden Amber Tile
    public static final Block GOLDEN_AMBER_TILE = add("golden_amber_tile", Block::new, amberTiles());
    public static final SlabBlock GOLDEN_AMBER_TILE_SLAB = add("golden_amber_tile_slab", SlabBlock::new, amberTiles());
    public static final ParadiseLostStairsBlock GOLDEN_AMBER_TILE_STAIRS = add("golden_amber_tile_stairs", settings -> new ParadiseLostStairsBlock(GOLDEN_AMBER_TILE.getDefaultState(), settings), amberTiles());

    // Misc
    public static final CropGrowthBlock BLOOMED_CALCITE = add("bloomed_calcite", settings -> new CropGrowthBlock(settings, 2), copy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM).ticksRandomly());
    public static final SimpleBlockSet CALCITE_TILES_SET = registerSimpleBlockSet("calcite_tiles", copy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final WallBlock CALCITE_TILES_WALL = add("calcite_tiles_wall", WallBlock::new, floestone());
    public static final SimpleBlockSet BLOOMED_CALCITE_TILES_SET = registerSimpleBlockSet("bloomed_calcite_tiles", copy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final WallBlock BLOOMED_CALCITE_TILES_WALL = add("bloomed_calcite_tiles_wall", WallBlock::new, floestone());

    protected static Settings flowerPot() {
        return copy(POTTED_OAK_SAPLING);
    }

    public static final CalciteFlowerPotBlock CALCITE_FLOWER_POT = add("calcite_flower_pot", CalciteFlowerPotBlock::new, copy(FLOWER_POT));
    public static final CalciteDecoratedPotBlock CALCITE_DECORATED_POT = add("calcite_decorated_pot", CalciteDecoratedPotBlock::new, copy(DECORATED_POT).mapColor(MapColor.WHITE));

    public static final CampfireBlock CHERINE_CAMPFIRE = add("cherine_campfire", settings -> new CherineCampfireBlock(false, 1, settings), copy(CAMPFIRE).instrument(NoteBlockInstrument.BASS), cutoutRenderLayer);

    public static final PalaceDoorBlock PALACE_DOOR = add("palace_door", PalaceDoorBlock::new, copy(BEDROCK).nonOpaque().mapColor(MapColor.YELLOW));
    public static final PalaceDoorExtensionBlock PALACE_DOOR_EXTENSION = add("palace_door_extension", PalaceDoorExtensionBlock::new, copy(BEDROCK).nonOpaque().mapColor(MapColor.YELLOW));

    protected static Settings leafPile() {
        return create().strength(0.2f).sounds(BlockSoundGroup.VINE).replaceable().nonOpaque().suffocates(never).blockVision(never).pistonBehavior(PistonBehavior.DESTROY);
    }
    // Aurel Wood
    public static final WoodBlockSet AUREL_WOODSTUFF = registerWoodBlockSet(ParadiseLostWoodTypes.AUREL, ParadiseLostBlockSets.AUREL, ParadiseLostSaplingGenerators.AUREL, MapColor.TERRACOTTA_BROWN, MapColor.TERRACOTTA_BROWN, MapColor.PALE_GREEN);
    public static final PillarBlock MOTTLED_AUREL_LOG = add("mottled_aurel_log", PillarBlock::new, copy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedLog()));
    public static final PillarBlock MOTTLED_AUREL_WOOD = add("mottled_aurel_wood", PillarBlock::new, copy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PALE_YELLOW), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedWood()));
    public static final ChuteBlock MOTTLED_AUREL_FALLEN_LOG = add("mottled_aurel_fallen_log", ChuteBlock::new, copy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE), flammableLog, cutoutRenderLayer);
    public static final LeafPileBlock AUREL_LEAF_PILE = add("aurel_leaf_pile", LeafPileBlock::new, leafPile().mapColor(MapColor.PALE_GREEN), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block AUREL_BOOKSHELF = add("aurel_bookshelf", Block::new, copy(BOOKSHELF).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN), flammable(30, 20));
    public static final SignSet AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.AUREL);
    // Mother Aurel Wood
    public static final WoodBlockSet MOTHER_AUREL_WOODSTUFF = registerWoodBlockSetMotherAurel();
    public static final SignSet MOTHER_AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.MOTHER_AUREL);
    // Menth Wood
    public static final WoodBlockSet MENTH_WOODSTUFF = registerWoodBlockSet(ParadiseLostWoodTypes.MENTH, ParadiseLostBlockSets.MENTH, ParadiseLostSaplingGenerators.MENTH, MapColor.LICHEN_GREEN, MapColor.LICHEN_GREEN, MapColor.TERRACOTTA_MAGENTA);
    public static final SignSet MENTH_SIGNS = registerSignSet(ParadiseLostWoodTypes.MENTH);
    // Wisteria Wood
    public static final WoodBlockSet WISTERIA_WOODSTUFF = registerWoodBlockSetWisteria();
    public static final SignSet WISTERIA_SIGNS = registerSignSet(ParadiseLostWoodTypes.WISTERIA);

    protected static Settings wisteriaLeaf() {
        return copy(OAK_LEAVES);
    }
    protected static Settings wisteriaHanger() {
        return create().pistonBehavior(PistonBehavior.DESTROY).strength(0.2f).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).suffocates(never).blockVision(never);
    }
    protected static Settings wisteriaSapling() {
        return copy(Blocks.OAK_SAPLING);
    }

    public static final WisteriaLeavesBlock ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", WisteriaLeavesBlock::new, wisteriaLeaf().mapColor(MapColor.PINK), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", LeafPileBlock::new, leafPile().mapColor(MapColor.PINK), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", settings -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.ROSE_WISTERIA, settings), wisteriaSapling().mapColor(MapColor.PINK), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", settings -> new FlowerPotBlock(ROSE_WISTERIA_SAPLING, settings), flowerPot(), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", ParadiseLostHangerBlock::new, wisteriaHanger().mapColor(MapColor.PINK), flammableLeaves, cutoutRenderLayer);

    public static final WisteriaLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", WisteriaLeavesBlock::new, wisteriaLeaf().mapColor(MapColor.LIGHT_BLUE), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", LeafPileBlock::new, leafPile().mapColor(MapColor.LIGHT_BLUE), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", settings -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.FROST_WISTERIA, settings), wisteriaSapling().mapColor(MapColor.LIGHT_BLUE), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", settings -> new FlowerPotBlock(FROST_WISTERIA_SAPLING, settings), flowerPot(), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", ParadiseLostHangerBlock::new, wisteriaHanger().mapColor(MapColor.LIGHT_BLUE), flammableLeaves, cutoutRenderLayer);

    public static final WisteriaLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", WisteriaLeavesBlock::new, wisteriaLeaf().mapColor(MapColor.MAGENTA), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", LeafPileBlock::new, leafPile().mapColor(MapColor.MAGENTA), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", settings -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.LAVENDER_WISTERIA, settings), wisteriaSapling().mapColor(MapColor.MAGENTA), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", settings -> new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, settings), flowerPot(), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", ParadiseLostHangerBlock::new, wisteriaHanger().mapColor(MapColor.MAGENTA), flammableLeaves, cutoutRenderLayer);

    // Grasses
    private static Settings shrub() {
        return copy(Blocks.SHORT_GRASS).offset(AbstractBlock.OffsetType.XZ).mapColor(MapColor.PALE_GREEN);
    }
    private static Settings crop() {
        return copy(Blocks.WHEAT).mapColor(MapColor.PALE_GREEN);
    }

    public static final ParadiseLostBrushBlock GRASS = add("grass_plant", ParadiseLostBrushBlock::new, shrub(), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock GRASS_FLOWERING = add("grass_flowering", ParadiseLostBrushBlock::new, shrub().mapColor(MapColor.WHITE), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock SHORT_GRASS = add("short_grass", ParadiseLostBrushBlock::new, shrub(), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostTallBrushBlock TALL_GRASS = add("tall_grass", ParadiseLostTallBrushBlock::new, shrub(), flammablePlant, cutoutRenderLayer);
    public static final ParadiseLostBrushBlock FERN = add("fern", ParadiseLostBrushBlock::new, shrub(), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FERN = add("potted_fern", settings -> new FlowerPotBlock(FERN, settings), flowerPot(), cutoutRenderLayer);
    public static final ParadiseLostBrushBlock BUSH = add("bush", ParadiseLostBrushBlock::new, shrub(), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock SHAMROCK = add("shamrock", GroundcoverBlock::new, shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock MALT_SPRIG = add("malt_sprig", GroundcoverBlock::new, shrub().sounds(BlockSoundGroup.AZALEA_LEAVES), flammablePlant, cutoutRenderLayer);
    public static final TallWaterPlantBlock HONEY_NETTLE = add("honey_nettle", TallWaterPlantBlock::new, shrub().sounds(BlockSoundGroup.SMALL_DRIPLEAF), cutoutRenderLayer);

    public static final WallClingingPlantBlock ROOTCAP = add("rootcap", settings -> new WallClingingPlantBlock(ParadiseLostBlockTags.FUNGI_CLINGABLES, settings), copy(BROWN_MUSHROOM), cutoutRenderLayer);
    public static final ParadiseLostMushroomPlantBlock BROWN_SPORECAP = add("brown_sporecap", settings -> new ParadiseLostMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, ParadiseLostTreeConfiguredFeatures.HUGE_BROWN_SPORECAP, settings), copy(BROWN_MUSHROOM), cutoutRenderLayer);
    public static final ParadiseLostHangingMushroomPlantBlock PINK_SPORECAP = add("pink_sporecap", settings -> new ParadiseLostHangingMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, settings), copy(BROWN_MUSHROOM), cutoutRenderLayer);

    public static final Block ROOTCAP_BLOCK = add("rootcap_block", MushroomBlock::new, copy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final Block BROWN_SPORECAP_BLOCK = add("brown_sporecap_block", MushroomBlock::new, copy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.BROWN));
    public static final Block PINK_SPORECAP_BLOCK = add("pink_sporecap_block", MushroomBlock::new, copy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_PINK));

    public static final AmadrysCropBlock AMADRYS = add("amadrys", AmadrysCropBlock::new, crop().mapColor(MapColor.PINK), flammablePlant, cutoutMippedRenderLayer);
    public static final FlaxCropBlock FLAX = add("flax", FlaxCropBlock::new, crop().mapColor(MapColor.OAK_TAN), flammablePlant, cutoutRenderLayer);
    public static final SwedrootCropBlock SWEDROOT = add("swedroot", SwedrootCropBlock::new, shrub().offset(AbstractBlock.OffsetType.NONE).mapColor(MapColor.BLUE), flammablePlant, cutoutRenderLayer);
    public static final CropBlock NITRA = add("nitra", CropBlock::new, crop().mapColor(MapColor.PALE_YELLOW), flammablePlant, cutoutMippedRenderLayer);

    public static final Block FLAXWEAVE_CUSHION = add("flaxweave_cushion", FlaxweaveCushionBlock::new, create().mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F).instrument(NoteBlockInstrument.GUITAR), flammable(40, 10));
    public static final SlabBlock FLAXWEAVE_CUSHION_SLAB = add("flaxweave_cushion_slab", FlaxweaveCushionSlabBlock::new, create().mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F).instrument(NoteBlockInstrument.GUITAR), flammable(40, 10));

    public static final BlackcurrantBushBlock BLACKCURRANT_BUSH = add("blackcurrant_bush", BlackcurrantBushBlock::new, create().strength(0.2f)
            .ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().suffocates(never).blockVision(never).noCollision().mapColor(MapColor.PURPLE), flammablePlant, cutoutRenderLayer);

    public static final FourBiteCakeBlock CHEESECAKE = add("halflight_cheesecake", FourBiteCakeBlock::new, copy(CAKE));

    public static final SixFacingBlock AMADRYS_BUNDLE = add("amadrys_bundle", SixFacingBlock::new, copy(HAY_BLOCK));

    // Flowers
    private static Settings flower() {
        return copy(DANDELION);
    }

    public static final FlowerBlock ANCIENT_FLOWER = add("ancient_flower", settings -> new FlowerBlock(StatusEffects.ABSORPTION, 20, settings), flower(), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ANCIENT_FLOWER = add("potted_ancient_flower", settings -> new FlowerPotBlock(ANCIENT_FLOWER, settings), flowerPot(), cutoutRenderLayer);
    public static final FlowerBlock ATARAXIA = add("ataraxia", settings -> new FlowerBlock(StatusEffects.INSTANT_DAMAGE, 1, settings), flower(), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ATARAXIA = add("potted_ataraxia", settings -> new FlowerPotBlock(ATARAXIA, settings), flowerPot(), cutoutRenderLayer);
    public static final FlowerBlock CLOUDSBLUFF = add("cloudsbluff", settings -> new FlowerBlock(StatusEffects.SLOW_FALLING, 6, settings), flower(), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_CLOUDSBLUFF = add("potted_cloudsbluff", settings -> new FlowerPotBlock(CLOUDSBLUFF, settings), flowerPot(), cutoutRenderLayer);
    public static final FlowerBlock DRIGEAN = add("drigean", settings -> new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8, settings), flower(), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_DRIGEAN = add("potted_drigean", settings -> new FlowerPotBlock(DRIGEAN, settings), flowerPot(), cutoutRenderLayer);
    public static final FlowerBlock LUMINAR = add("luminar", settings -> new FlowerBlock(StatusEffects.GLOWING, 9, settings), flower().luminance(value -> 3), flammablePlant, cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LUMINAR = add("potted_luminar", settings -> new FlowerPotBlock(LUMINAR, settings), flowerPot().luminance(value -> 3), cutoutRenderLayer);

    // Tall Flowers
    public static final ParadiseLostTallBrushBlock WILD_FLAX = add("wild_flax", ParadiseLostTallBrushBlock::new, flower(), flammablePlant, cutoutMippedRenderLayer);
    
    // Ores
    public static final ExperienceDroppingBlock CHERINE_ORE = add("cherine_ore", settings -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings), create().requiresTool().strength(1f, 3f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final ExperienceDroppingBlock OLVITE_ORE = add("olvite_ore", settings -> new ExperienceDroppingBlock(UniformIntProvider.create(1, 3), settings), create().requiresTool().strength(1.5f, 3f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final RedstoneOreBlock FLOESTONE_REDSTONE_ORE = add("floestone_redstone_ore", RedstoneOreBlock::new, copy(REDSTONE_ORE).strength(1.5f, 3f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final ExperienceDroppingBlock SURTRUM = add("surtrum", settings -> new SurtrumOreBlock(UniformIntProvider.create(2, 5), settings), create().sounds(BlockSoundGroup.NETHER_GOLD_ORE).requiresTool().strength(9f, 20f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final Block METAMORPHIC_SHELL = add("metamorphic_shell", Block::new, create().sounds(BlockSoundGroup.TUFF).requiresTool().strength(40f, 6f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final PoofBlock SURTRUM_AIR = add("surtrum_air", PoofBlock::new, create().replaceable().sounds(BlockSoundGroup.NETHER_GOLD_ORE));
    public static final FloatingBlock LEVITA_ORE = add("levita_ore", settings -> new FloatingBlock(false, settings, UniformIntProvider.create(4, 7)), create().requiresTool().strength(4f).instrument(NoteBlockInstrument.BASEDRUM));
    public static final Block CHERINE_BLOCK = add("cherine_block", Block::new, create().requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.STONE));
    public static final Block OLVITE_BLOCK = add("olvite_block", Block::new, create().requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE));
    public static final Block REFINED_SURTRUM_BLOCK = add("refined_surtrum_block", Block::new, create().requiresTool().strength(4f, -1f).sounds(BlockSoundGroup.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE));
    // Misc
    public static final ButtonBlock FLOESTONE_BUTTON = add("floestone_button", settings -> new ParadiseLostButtonBlock(BlockSetType.STONE, 20, settings), Settings.create().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY));
    public static final PressurePlateBlock FLOESTONE_PRESSURE_PLATE = add("floestone_pressure_plate", settings -> new ParadiseLostPressurePlateBlock(BlockSetType.STONE, settings), copy(STONE_PRESSURE_PLATE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final OlvitePressurePlateBlock OLVITE_PRESSURE_PLATE = add("olvite_pressure_plate", OlvitePressurePlateBlock::new, create().mapColor(MapColor.PALE_GREEN).solid().instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY));
    public static final FloatingBlock LEVITATOR = add("levitator", settings -> new FloatingBlock(true, settings), create().strength(3f, 3f).sounds(BlockSoundGroup.STONE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final ChainBlock OLVITE_CHAIN = add("olvite_chain", ChainBlock::new, copy(CHAIN), cutoutMippedRenderLayer);
    public static final CherineLanternBlock CHERINE_LANTERN = add("cherine_lantern", CherineLanternBlock::new, copy(LANTERN).resistance(1f), cutoutMippedRenderLayer);
    public static final ParadiseLostPortalBlock BLUE_PORTAL = add("blue_portal", ParadiseLostPortalBlock::new, copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE), translucentRenderLayer);

    private static Settings cherineTorch() {
        return copy(TORCH).ticksRandomly().luminance(state -> 15);
    }

    public static final CherineTorchBlock CHERINE_TORCH = add("cherine_torch", CherineTorchBlock::new, cherineTorch(), cutoutRenderLayer);
    public static final CherineWallTorchBlock CHERINE_TORCH_WALL = add("cherine_wall_torch", CherineWallTorchBlock::new, cherineTorch().lootTable(CHERINE_TORCH.getLootTableKey()), cutoutRenderLayer);
    public static final PaneBlock GOLDEN_AMBER_BARS = add("golden_amber_bars", PaneBlock::new, copy(IRON_BARS), cutoutMippedRenderLayer);

    // Usables
    public static final BrushableBlock SUSPICIOUS_DIRT = add("suspicious_dirt", settings -> new BrushableBlock(DIRT, SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL, SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE, settings), copy(DIRT).strength(0.25F).sounds(BlockSoundGroup.SUSPICIOUS_GRAVEL).pistonBehavior(PistonBehavior.DESTROY));
    public static final IncubatorBlock INCUBATOR = add("incubator", IncubatorBlock::new, create().mapColor(MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), cutoutMippedRenderLayer);
    public static final IncubatorBlock NEST = add("nest", settings -> new IncubatorBlock(settings, 0.35F), create().mapColor(MapColor.PALE_YELLOW).strength(0.3f).sounds(BlockSoundGroup.GRASS).nonOpaque(), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", FoodBowlBlock::new, create().mapColor(MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), cutoutMippedRenderLayer);
	public static final Block TREE_TAP = add("tree_tap", TreeTapBlock::new, create().mapColor(MapColor.SPRUCE_BROWN).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque().ticksRandomly(), cutoutRenderLayer);
    public static final NitraBlock NITRA_BUNCH = add("nitra_bunch", NitraBlock::new, create().mapColor(MapColor.PALE_YELLOW).strength(0.5f).sounds(BlockSoundGroup.WET_GRASS));
    public static final Block LEVITA_RAIL = add("levita_rail", LevitaRailBlock::new, create().noCollision().strength(0.7F).sounds(BlockSoundGroup.METAL), cutoutMippedRenderLayer);

    public static void init() {
    }

}
