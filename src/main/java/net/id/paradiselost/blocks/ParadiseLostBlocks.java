package net.id.paradiselost.blocks;

import net.id.paradiselost.blocks.BlockRegistration.ParadiseLostFarmlandBlock;
import net.id.paradiselost.blocks.BlockRegistration.ParadiseLostStairsBlock;
import net.id.paradiselost.blocks.BlockRegistration.SignSet;
import net.id.paradiselost.blocks.BlockRegistration.SimpleBlockSet;
import net.id.paradiselost.blocks.BlockRegistration.WoodBlockSet;
import net.id.paradiselost.blocks.decorative.*;
import net.id.paradiselost.blocks.mechanical.*;
import net.id.paradiselost.blocks.natural.*;
import net.id.paradiselost.blocks.natural.cloud.*;
import net.id.paradiselost.blocks.natural.crop.*;
import net.id.paradiselost.blocks.natural.plant.*;
import net.id.paradiselost.blocks.natural.tree.*;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.tree.ParadiseLostSaplingGenerators;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy;
import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.create;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.*;
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
    public static final FarmlandBlock FARMLAND = add("farmland", new ParadiseLostFarmlandBlock(copy(Blocks.FARMLAND)));
    public static final ParadiseLostDirtPathBlock DIRT_PATH = add("grass_path", new ParadiseLostDirtPathBlock(copy(Blocks.DIRT_PATH), () -> ParadiseLostBlocks.DIRT));
    public static final ParadiseLostDirtPathBlock PERMAFROST_PATH = add("frozen_path", new ParadiseLostDirtPathBlock(permafrost(), () -> ParadiseLostBlocks.PERMAFROST));
    public static final ParadiseLostGrassBlock HIGHLANDS_GRASS = add("highlands_grass", new ParadiseLostGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final ParadiseLostSnowyBlock FROZEN_GRASS = add("frozen_grass", new ParadiseLostSnowyBlock(grassBlock().mapColor(MapColor.WHITE).strength(2F).sounds(BlockSoundGroup.GILDED_BLACKSTONE)), flattenable(ParadiseLostBlocks.PERMAFROST_PATH));

    public static final Block DIRT = add("dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), tillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final Block COARSE_DIRT = add("coarse_dirt", new Block(copy(Blocks.DIRT).strength(0.3f)), coarseTillable(), flattenable(ParadiseLostBlocks.DIRT_PATH));
    public static final FloatingBlock LEVITA = add("levita", new FloatingBlock(false, copy(Blocks.GRAVEL).strength(0.3f)));
    public static final Block PERMAFROST = add("permafrost", new Block(permafrost()), flattenable(ParadiseLostBlocks.PERMAFROST_PATH));
    public static final Block PACKED_SWEDROOT = add("packed_swedroot", new Block(create().strength(2f).sounds(BlockSoundGroup.SHROOMLIGHT)));
    public static final Block LIVERWORT = add("liverwort", new Block(copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES)));
    public static final CarpetBlock LIVERWORT_CARPET = add("liverwort_carpet", new CarpetBlock(copy(MOSS_BLOCK).sounds(BlockSoundGroup.AZALEA_LEAVES)));


    // Clouds
    private static Settings cloud() {
        return create().strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(never).suffocates(never).blockVision(never);
    }

    public static final ParadiseLostCloudBlock COLD_CLOUD = add("cold_cloud", new ParadiseLostCloudBlock(cloud().mapColor(MapColor.WHITE)), translucentRenderLayer);
    public static final BlueParadiseLostCloudBlock BLUE_CLOUD = add("blue_cloud", new BlueParadiseLostCloudBlock(cloud().mapColor(MapColor.LIGHT_BLUE)), translucentRenderLayer);
    public static final GoldenParadiseLostCloudBlock GOLDEN_CLOUD = add("golden_cloud", new GoldenParadiseLostCloudBlock(cloud().mapColor(MapColor.GOLD)), translucentRenderLayer);

    // Floestone
    private static Settings floestone() {
        return create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(0.5f, 5f).sounds(BlockSoundGroup.STONE);
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
    public static final Block CHISELED_FLOESTONE = add("chiseled_floestone", new Block(floestoneBrick()));
    public static final SlabBlock FLOESTONE_BRICK_SLAB = add("floestone_brick_slab", new SlabBlock(floestoneBrick()));
    public static final ParadiseLostStairsBlock FLOESTONE_BRICK_STAIRS = add("floestone_brick_stairs", new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), floestoneBrick()));
    public static final WallBlock FLOESTONE_BRICK_WALL = add("floestone_brick_wall", new WallBlock(floestoneBrick()));

    // Heliolith
    public static final Block HELIOLITH = add("heliolith", new Block(floestone()));
    public static final Block SMOOTH_HELIOLITH = add("smooth_heliolith", new Block(floestone()));
    public static final SlabBlock HELIOLITH_SLAB = add("heliolith_slab", new SlabBlock(floestone()));
    public static final SlabBlock SMOOTH_HELIOLITH_SLAB = add("smooth_heliolith_slab", new SlabBlock(floestone()));
    public static final ParadiseLostStairsBlock HELIOLITH_STAIRS = add("heliolith_stairs", new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), floestone()));
    public static final ParadiseLostStairsBlock SMOOTH_HELIOLITH_STAIRS = add("smooth_heliolith_stairs", new ParadiseLostStairsBlock(FLOESTONE_BRICK.getDefaultState(), floestone()));
    public static final WallBlock HELIOLITH_WALL = add("heliolith_wall", new WallBlock(floestone()));

    // Levita Brick
    public static final SimpleBlockSet LEVITA_BRICK_SET = registerSimpleBlockSet("levita_brick", create().mapColor(MapColor.LIGHT_BLUE_GRAY).strength(0.3f, 3f).sounds(BlockSoundGroup.CALCITE));

    private static Settings amberTiles() {
        return create().mapColor(MapColor.GOLD).requiresTool().sounds(BlockSoundGroup.CALCITE).strength(2, 6);
    }

    // Golden Amber Tile
    public static final Block GOLDEN_AMBER_TILE = add("golden_amber_tile", new Block(amberTiles()));
    public static final SlabBlock GOLDEN_AMBER_TILE_SLAB = add("golden_amber_tile_slab", new SlabBlock(amberTiles()));
    public static final ParadiseLostStairsBlock GOLDEN_AMBER_TILE_STAIRS = add("golden_amber_tile_stairs", new ParadiseLostStairsBlock(GOLDEN_AMBER_TILE.getDefaultState(), amberTiles()));

    // Misc
    public static final CropGrowthBlock BLOOMED_CALCITE = add("bloomed_calcite", new CropGrowthBlock(copy(CALCITE).ticksRandomly(), 2));

    protected static Settings flowerPot() {
        return copy(POTTED_OAK_SAPLING);
    }

    public static final CampfireBlock CHERINE_CAMPFIRE = add("cherine_campfire", new CherineCampfireBlock(false, 1, Settings.copy(CAMPFIRE)), cutoutRenderLayer);

    protected static Settings leafPile() {
        return create().strength(0.2f).sounds(BlockSoundGroup.VINE).replaceable().nonOpaque().suffocates(never).blockVision(never).pistonBehavior(PistonBehavior.DESTROY);
    }
    // Aurel Wood
    public static final WoodBlockSet AUREL_WOODSTUFF = registerWoodBlockSet(ParadiseLostWoodTypes.AUREL, ParadiseLostBlockSets.AUREL, ParadiseLostSaplingGenerators.AUREL, MapColor.DARK_RED, MapColor.DARK_RED, MapColor.PALE_GREEN);
    public static final PillarBlock MOTTLED_AUREL_LOG = add("mottled_aurel_log", new PillarBlock(copy(OAK_LOG).mapColor(MapColor.DARK_RED)), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedLog()));
    public static final PillarBlock MOTTLED_AUREL_WOOD = add("mottled_aurel_wood", new PillarBlock(copy(OAK_LOG).mapColor(MapColor.PALE_YELLOW)), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedWood()));
    public static final ChuteBlock MOTTLED_AUREL_FALLEN_LOG = add("mottled_aurel_fallen_log", new ChuteBlock(copy(OAK_LOG).mapColor(MapColor.DARK_RED)), flammableLog, cutoutRenderLayer);
    public static final LeafPileBlock AUREL_LEAF_PILE = add("aurel_leaf_pile", new LeafPileBlock(leafPile().mapColor(MapColor.PALE_GREEN)), flammableLeaves, cutoutMippedRenderLayer);
    public static final Block AUREL_BOOKSHELF = add("aurel_bookshelf", new Block(copy(BOOKSHELF).mapColor(MapColor.DARK_RED)), flammable(30, 20));
    public static final SignSet AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.AUREL);
    // Mother Aurel Wood
    public static final WoodBlockSet MOTHER_AUREL_WOODSTUFF = registerWoodBlockSetMotherAurel();
    public static final SignSet MOTHER_AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.MOTHER_AUREL);
    // Orange Wood
    public static final WoodBlockSet ORANGE_WOODSTUFF = registerWoodBlockSetOrange();
    public static final SignSet ORANGE_SIGNS = registerSignSet(ParadiseLostWoodTypes.ORANGE);
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

    public static final WisteriaLeavesBlock ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.PINK)), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", new LeafPileBlock(leafPile().mapColor(MapColor.PINK)), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.ROSE_WISTERIA, wisteriaSapling().mapColor(MapColor.PINK)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_ROSE_WISTERIA_SAPLING = add("potted_rose_wisteria_sapling", new FlowerPotBlock(ROSE_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.PINK)), flammableLeaves, cutoutRenderLayer);

    public static final WisteriaLeavesBlock FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.LIGHT_BLUE)), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", new LeafPileBlock(leafPile().mapColor(MapColor.LIGHT_BLUE)), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.FROST_WISTERIA, wisteriaSapling().mapColor(MapColor.LIGHT_BLUE)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_FROST_WISTERIA_SAPLING = add("potted_frost_wisteria_sapling", new FlowerPotBlock(FROST_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.LIGHT_BLUE)), flammableLeaves, cutoutRenderLayer);

    public static final WisteriaLeavesBlock LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.MAGENTA)), flammableLeaves, cutoutMippedRenderLayer);
    public static final LeafPileBlock LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", new LeafPileBlock(leafPile().mapColor(MapColor.MAGENTA)), flammableLeaves, cutoutMippedRenderLayer);
    public static final SaplingBlock LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.LAVENDER_WISTERIA, wisteriaSapling().mapColor(MapColor.MAGENTA)), cutoutRenderLayer);
    public static final FlowerPotBlock POTTED_LAVENDER_WISTERIA_SAPLING = add("potted_lavender_wisteria_sapling", new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING, flowerPot()), cutoutRenderLayer);
    public static final ParadiseLostHangerBlock LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.MAGENTA)), flammableLeaves, cutoutRenderLayer);

    // Grasses
    private static Settings shrub() {
        return copy(Blocks.SHORT_GRASS).offset(AbstractBlock.OffsetType.XZ).mapColor(MapColor.PALE_GREEN);
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
    public static final GroundcoverBlock SHAMROCK = add("shamrock", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES)), flammablePlant, cutoutRenderLayer);
    public static final GroundcoverBlock MALT_SPRIG = add("malt_sprig", new GroundcoverBlock(shrub().sounds(BlockSoundGroup.AZALEA_LEAVES)), flammablePlant, cutoutRenderLayer);
    public static final TallWaterPlantBlock HONEY_NETTLE = add("honey_nettle", new TallWaterPlantBlock(shrub().sounds(BlockSoundGroup.SMALL_DRIPLEAF)), cutoutRenderLayer);

    public static final WallClingingPlantBlock ROOTCAP = add("rootcap", new WallClingingPlantBlock(ParadiseLostBlockTags.FUNGI_CLINGABLES, copy(BROWN_MUSHROOM)), cutoutRenderLayer);
    public static final ParadiseLostMushroomPlantBlock BROWN_SPORECAP = add("brown_sporecap", new ParadiseLostMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, copy(BROWN_MUSHROOM)), cutoutRenderLayer);
    public static final ParadiseLostHangingMushroomPlantBlock PINK_SPORECAP = add("pink_sporecap", new ParadiseLostHangingMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, copy(BROWN_MUSHROOM)), cutoutRenderLayer);

    public static final AmadrysCropBlock AMADRYS = add("amadrys", new AmadrysCropBlock(crop().mapColor(MapColor.PINK)), flammablePlant, cutoutMippedRenderLayer);
    public static final FlaxCropBlock FLAX = add("flax", new FlaxCropBlock(crop().mapColor(MapColor.OAK_TAN)), flammablePlant, cutoutRenderLayer);
    public static final SwedrootCropBlock SWEDROOT = add("swedroot", new SwedrootCropBlock(shrub().offset(AbstractBlock.OffsetType.NONE).mapColor(MapColor.BLUE)), flammablePlant, cutoutRenderLayer);
    public static final CropBlock NITRA = add("nitra", new CropBlock(crop().mapColor(MapColor.PALE_YELLOW)), flammablePlant, cutoutMippedRenderLayer);

    public static final Block FLAXWEAVE_CUSHION = add("flaxweave_cushion", new FlaxweaveCushionBlock(create().mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.WOOL).strength(0.2F)), flammable(40, 10));

    public static final BlackcurrantBushBlock BLACKCURRANT_BUSH = add("blackcurrant_bush", new BlackcurrantBushBlock(create().strength(0.2f)
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
    public static final ExperienceDroppingBlock CHERINE_ORE = add("cherine_ore", new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), create().requiresTool().strength(1f, 3f)));
    public static final ExperienceDroppingBlock OLVITE_ORE = add("olvite_ore", new ExperienceDroppingBlock(UniformIntProvider.create(1, 3), create().requiresTool().strength(1.5f, 3f)));
    public static final RedstoneOreBlock FLOESTONE_REDSTONE_ORE = add("floestone_redstone_ore", new RedstoneOreBlock(copy(REDSTONE_ORE).strength(1.5f, 3f)));
    public static final ExperienceDroppingBlock SURTRUM = add("surtrum", new SurtrumOreBlock(UniformIntProvider.create(2, 5), create().sounds(BlockSoundGroup.NETHER_GOLD_ORE).requiresTool().strength(9f, 20f)));
    public static final Block METAMORPHIC_SHELL = add("metamorphic_shell", new Block(create().sounds(BlockSoundGroup.TUFF).requiresTool().strength(40f, 6f)));
    public static final PoofBlock SURTRUM_AIR = add("surtrum_air", new PoofBlock(create().replaceable().sounds(BlockSoundGroup.NETHER_GOLD_ORE)));
    public static final FloatingBlock LEVITA_ORE = add("levita_ore", new FloatingBlock(false, create().requiresTool().strength(4f), UniformIntProvider.create(4, 7)));
    public static final Block CHERINE_BLOCK = add("cherine_block", new Block(create().requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.STONE)));
    public static final Block OLVITE_BLOCK = add("olvite_block", new Block(create().requiresTool().strength(3f, -1f).sounds(BlockSoundGroup.METAL)));
    public static final Block REFINED_SURTRUM_BLOCK = add("refined_surtrum_block", new Block(create().requiresTool().strength(4f, -1f).sounds(BlockSoundGroup.METAL)));
    // Misc
    public static final ButtonBlock FLOESTONE_BUTTON = add("floestone_button", new ParadiseLostButtonBlock(BlockSetType.STONE, 20, Settings.create().noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)));
    public static final PressurePlateBlock FLOESTONE_PRESSURE_PLATE = add("floestone_pressure_plate", new ParadiseLostPressurePlateBlock(BlockSetType.STONE, copy(STONE_PRESSURE_PLATE)));
    public static final FloatingBlock LEVITATOR = add("levitator", new FloatingBlock(true, create().strength(3f, 3f).sounds(BlockSoundGroup.STONE)));
    public static final ChainBlock OLVITE_CHAIN = add("olvite_chain", new ChainBlock(copy(CHAIN)), cutoutMippedRenderLayer);
    public static final CherineLanternBlock CHERINE_LANTERN = add("cherine_lantern", new CherineLanternBlock(copy(LANTERN).resistance(1f)), cutoutMippedRenderLayer);
    public static final ParadiseLostPortalBlock BLUE_PORTAL = add("blue_portal", new ParadiseLostPortalBlock(copy(NETHER_PORTAL).nonOpaque().blockVision(never).mapColor(MapColor.BLUE)), translucentRenderLayer);

    private static Settings cherineTorch() {
        return copy(TORCH).ticksRandomly().luminance(state -> 15);
    }

    public static final CherineTorchBlock CHERINE_TORCH = add("cherine_torch", new CherineTorchBlock(cherineTorch()), cutoutRenderLayer);
    public static final CherineWallTorchBlock CHERINE_TORCH_WALL = add("cherine_wall_torch", new CherineWallTorchBlock(cherineTorch().dropsLike(CHERINE_TORCH)), cutoutRenderLayer);

    // Usables
    public static final IncubatorBlock INCUBATOR = add("incubator", new IncubatorBlock(create().mapColor(MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
    public static final FoodBowlBlock FOOD_BOWL = add("food_bowl", new FoodBowlBlock(create().mapColor(MapColor.DULL_RED).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), cutoutMippedRenderLayer);
	public static final Block TREE_TAP = add("tree_tap", new TreeTapBlock(create().mapColor(MapColor.SPRUCE_BROWN).strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque().ticksRandomly()), cutoutRenderLayer);
    public static final NitraBlock NITRA_BUNCH = add("nitra_bunch", new NitraBlock(create().mapColor(MapColor.PALE_YELLOW).strength(0.5f).sounds(BlockSoundGroup.WET_GRASS)));

    public static void init() {
    }

}
