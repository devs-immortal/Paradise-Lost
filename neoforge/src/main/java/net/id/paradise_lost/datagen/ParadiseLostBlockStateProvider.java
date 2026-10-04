package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.ParadiseLostPortalBlock;
import net.id.paradise_lost.block.decorative.SixFacingBlock;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.id.paradise_lost.block.mechanical.FourBiteCakeBlock;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.id.paradise_lost.block.natural.crop.TallCropBlock;
import net.id.paradise_lost.block.natural.tree.ParadiseLostHangerBlock;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.EnumMap;
import java.util.function.IntFunction;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Items;

public class ParadiseLostBlockStateProvider extends BlockStateProvider {

    private final ExistingFileHelper existingFileHelper;

    public ParadiseLostBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModConstants.MODID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        wood(BlockRegistry.AUREL_WOODSTUFF, BlockRegistry.AUREL_SIGNS);
        wood(BlockRegistry.MOTHER_AUREL_WOODSTUFF, BlockRegistry.MOTHER_AUREL_SIGNS);
        wood(BlockRegistry.MENTH_WOODSTUFF, BlockRegistry.MENTH_SIGNS);
        wood(BlockRegistry.WISTERIA_WOODSTUFF, BlockRegistry.WISTERIA_SIGNS);

        cube(BlockRegistry.FLOESTONE.get());
        cube(BlockRegistry.COBBLED_FLOESTONE.get());
        cube(BlockRegistry.MOSSY_FLOESTONE.get());
        cube(BlockRegistry.GOLDEN_MOSSY_FLOESTONE.get());
        cube(BlockRegistry.FLOESTONE_BRICK.get());
        cube(BlockRegistry.CHISELED_FLOESTONE.get());
        cube(BlockRegistry.SMOOTH_FLOESTONE.get());
        cube(BlockRegistry.OLVITE_BLOCK.get());
        cube(BlockRegistry.CHERINE_BLOCK.get());
        columnWithItem(BlockRegistry.AUREL_BOOKSHELF,
                modLoc("block/aurel_bookshelf"), modLoc("block/aurel_planks"));

        cube(BlockRegistry.HELIOLITH.get());
        cube(BlockRegistry.SMOOTH_HELIOLITH.get());
        cube(BlockRegistry.LEVITA_BRICK_SET.block().get());
        cube(BlockRegistry.CHISELED_LEVITA_BRICK.get());
        cube(BlockRegistry.CALCITE_TILES_SET.block().get());
        cube(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get());
        cube(BlockRegistry.DIRT.get());
        cube(BlockRegistry.COARSE_DIRT.get());
        cube(BlockRegistry.COLD_CLOUD.get());
        cube(BlockRegistry.BLUE_CLOUD.get());
        cube(BlockRegistry.GOLDEN_CLOUD.get());
        directionalCloud(BlockRegistry.GREEN_CLOUD, modLoc("block/green_cloud_top"),
                modLoc("block/green_cloud_bottom"), modLoc("block/green_cloud_side"));
        slabStairsWall(BlockRegistry.FLOESTONE_SLAB.get(), BlockRegistry.FLOESTONE_STAIRS.get(), BlockRegistry.FLOESTONE_WALL.get(), BlockRegistry.FLOESTONE.get());
        slabStairsWall(BlockRegistry.COBBLED_FLOESTONE_SLAB.get(), BlockRegistry.COBBLED_FLOESTONE_STAIRS.get(), BlockRegistry.COBBLED_FLOESTONE_WALL.get(), BlockRegistry.COBBLED_FLOESTONE.get());
        slabStairsWall(BlockRegistry.MOSSY_FLOESTONE_SLAB.get(), BlockRegistry.MOSSY_FLOESTONE_STAIRS.get(), BlockRegistry.MOSSY_FLOESTONE_WALL.get(), BlockRegistry.MOSSY_FLOESTONE.get());
        slabStairsWall(BlockRegistry.FLOESTONE_BRICK_SLAB.get(), BlockRegistry.FLOESTONE_BRICK_STAIRS.get(), BlockRegistry.FLOESTONE_BRICK_WALL.get(), BlockRegistry.FLOESTONE_BRICK.get());
        cube(BlockRegistry.MOSSY_FLOESTONE_BRICK.get());
        slabStairsWall(BlockRegistry.MOSSY_FLOESTONE_BRICK_SLAB.get(), BlockRegistry.MOSSY_FLOESTONE_BRICK_STAIRS.get(), BlockRegistry.MOSSY_FLOESTONE_BRICK_WALL.get(), BlockRegistry.MOSSY_FLOESTONE_BRICK.get());
        slabStairs(BlockRegistry.SMOOTH_FLOESTONE_SLAB.get(), BlockRegistry.SMOOTH_FLOESTONE_STAIRS.get(), BlockRegistry.SMOOTH_FLOESTONE.get());
        slabStairsWall(BlockRegistry.HELIOLITH_SLAB.get(), BlockRegistry.HELIOLITH_STAIRS.get(), BlockRegistry.HELIOLITH_WALL.get(), BlockRegistry.HELIOLITH.get());
        slabStairs(BlockRegistry.SMOOTH_HELIOLITH_SLAB.get(), BlockRegistry.SMOOTH_HELIOLITH_STAIRS.get(), BlockRegistry.SMOOTH_HELIOLITH.get());

        cube(BlockRegistry.THATCH_SET.block().get());
        simpleSet(BlockRegistry.THATCH_SET);
        simpleSet(BlockRegistry.LEVITA_BRICK_SET);
        simpleSet(BlockRegistry.CALCITE_TILES_SET);
        wallOnly(BlockRegistry.CALCITE_TILES_WALL.get(), BlockRegistry.CALCITE_TILES_SET.block().get());
        simpleSet(BlockRegistry.BLOOMED_CALCITE_TILES_SET);
        wallOnly(BlockRegistry.BLOOMED_CALCITE_TILES_WALL.get(), BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get());

        cube(BlockRegistry.BURNISHED_STONE_SET.block().get());
        simpleSet(BlockRegistry.BURNISHED_STONE_SET);
        wallOnly(BlockRegistry.BURNISHED_STONE_WALL.get(), BlockRegistry.BURNISHED_STONE_SET.block().get());
        columnWithItem(BlockRegistry.BURNISHED_STONE_PLAQUE, modLoc("block/burnished_stone_plaque"),
                modLoc("block/burnished_stone"));

        cube(BlockRegistry.GOLDEN_AMBER_TILE.get());
        slabStairs(BlockRegistry.GOLDEN_AMBER_TILE_SLAB.get(), BlockRegistry.GOLDEN_AMBER_TILE_STAIRS.get(), BlockRegistry.GOLDEN_AMBER_TILE.get());

        cube(BlockRegistry.LEVITA.get());
        cube(BlockRegistry.PERMAFROST.get());
        cube(BlockRegistry.PACKED_SWEDROOT.get());
        cube(BlockRegistry.LIVERWORT.get());
        carpet(BlockRegistry.LIVERWORT_CARPET, blockTexture(BlockRegistry.LIVERWORT.get()));
        cube(BlockRegistry.BLOOMED_CALCITE.get());
        cube(BlockRegistry.METAMORPHIC_SHELL.get());
        cube(BlockRegistry.REFINED_SURTRUM_BLOCK.get());
        cube(BlockRegistry.CHERINE_ORE.get());
        cube(BlockRegistry.OLVITE_ORE.get());
        cube(BlockRegistry.FLOESTONE_REDSTONE_ORE.get());
        cube(BlockRegistry.SURTRUM.get());
        cube(BlockRegistry.LEVITA_ORE.get());
        dustedBlock(BlockRegistry.SUSPICIOUS_DIRT.get(), "suspicious_dirt");
        bottomTop(BlockRegistry.NITRA_BUNCH, modLoc("block/nitra_bunch_side"),
                modLoc("block/nitra_bunch_bottom"), modLoc("block/nitra_bunch_top"));
        bottomTop(BlockRegistry.LEVITATOR, modLoc("block/levitator_side"),
                modLoc("block/smooth_floestone"), modLoc("block/smooth_floestone"));

        buttonWithItem(BlockRegistry.FLOESTONE_BUTTON, blockTexture(BlockRegistry.FLOESTONE.get()));
        pressurePlateWithItem(BlockRegistry.FLOESTONE_PRESSURE_PLATE, blockTexture(BlockRegistry.FLOESTONE.get()));
        olvitePressurePlate();

        mottledAurelWood();

        wisteriaColor("rose", BlockRegistry.ROSE_WISTERIA_LEAVES.get(), BlockRegistry.ROSE_WISTERIA_LEAF_PILE.get(),
                BlockRegistry.ROSE_WISTERIA_SAPLING.get(), BlockRegistry.POTTED_ROSE_WISTERIA_SAPLING.get(), BlockRegistry.ROSE_WISTERIA_HANGER.get());
        wisteriaColor("frost", BlockRegistry.FROST_WISTERIA_LEAVES.get(), BlockRegistry.FROST_WISTERIA_LEAF_PILE.get(),
                BlockRegistry.FROST_WISTERIA_SAPLING.get(), BlockRegistry.POTTED_FROST_WISTERIA_SAPLING.get(), BlockRegistry.FROST_WISTERIA_HANGER.get());
        wisteriaColor("lavender", BlockRegistry.LAVENDER_WISTERIA_LEAVES.get(), BlockRegistry.LAVENDER_WISTERIA_LEAF_PILE.get(),
                BlockRegistry.LAVENDER_WISTERIA_SAPLING.get(), BlockRegistry.POTTED_LAVENDER_WISTERIA_SAPLING.get(), BlockRegistry.LAVENDER_WISTERIA_HANGER.get());

        highlandsGrass(BlockRegistry.HIGHLANDS_GRASS.get(), "highlands_grass", "grass_snowy");
        frozenGrass();
        rotatedPath(BlockRegistry.DIRT_PATH.get(), "grass_path");
        rotatedPath(BlockRegistry.PERMAFROST_PATH.get(), "frozen_path");
        farmland();

        tintedCross(BlockRegistry.GRASS.get(), modLoc("block/grass"));
        existingSimple(BlockRegistry.GRASS_FLOWERING.get());
        floweringGrassItem();
        tintedCross(BlockRegistry.SHORT_GRASS.get());
        tintedCross(BlockRegistry.FERN.get());
        tintedCross(BlockRegistry.BUSH.get());
        existingRotated(BlockRegistry.SHAMROCK);
        groundcover(BlockRegistry.MALT_SPRIG, "malt_sprig", "malt_sprig_tall");
        crossPlant(BlockRegistry.BROWN_SPORECAP.get());
        existingSimple(BlockRegistry.PINK_SPORECAP.get());

        itemFromTexture(BlockRegistry.PINK_SPORECAP.get(), modLoc("item/pink_sporecap_inventory"));

        doublePlant(BlockRegistry.TALL_GRASS.get(), "tall_grass_bottom", "tall_grass_top");
        doublePlant(BlockRegistry.WILD_FLAX.get(), "wild_flax_bottom", "wild_flax_top");
        doublePlant(BlockRegistry.HONEY_NETTLE.get(), "honey_nettle_bottom", "honey_nettle_top");

        flower(BlockRegistry.ANCIENT_FLOWER.get(), BlockRegistry.POTTED_ANCIENT_FLOWER.get());
        flower(BlockRegistry.ATARAXIA.get(), BlockRegistry.POTTED_ATARAXIA.get());
        flower(BlockRegistry.CLOUDSBLUFF.get(), BlockRegistry.POTTED_CLOUDSBLUFF.get());
        flower(BlockRegistry.DRIGEAN.get(), BlockRegistry.POTTED_DRIGEAN.get());
        flower(BlockRegistry.LUMINAR.get(), BlockRegistry.POTTED_LUMINAR.get());
        pottedPlant(BlockRegistry.POTTED_FERN.get(), BlockRegistry.FERN.get(), blockTexture(BlockRegistry.FERN.get()));

        cropStages(BlockRegistry.AMADRYS.get(), age -> "amadrys_stage" + age);
        cropStages(BlockRegistry.SWEDROOT.get(), ParadiseLostBlockStateProvider::swedrootStage);
        cropStages(BlockRegistry.NITRA.get(), ParadiseLostBlockStateProvider::nitraStage);
        integerStages(BlockRegistry.BLACKCURRANT_BUSH.get(), BlockStateProperties.AGE_3, 4,
                age -> "blackcurrant_bush_stage" + age);
        flaxCrop();

        blockWithSlab(BlockRegistry.FLAXWEAVE_CUSHION, BlockRegistry.FLAXWEAVE_CUSHION_SLAB,
                blockTexture(BlockRegistry.FLAXWEAVE_CUSHION.get()));

        pistonLike(BlockRegistry.AMADRYS_BUNDLE, modLoc("block/amadrys_bundle_top"),
                modLoc("block/amadrys_bundle_bottom"), modLoc("block/amadrys_bundle_side"));
        horizontalExisting(BlockRegistry.ROOTCAP.get());

        existingTorch(BlockRegistry.CHERINE_TORCH, BlockRegistry.CHERINE_TORCH_WALL);
        itemFromTexture(BlockRegistry.CHERINE_TORCH, modLoc("block/cherine_torch"));
        existingCampfire(BlockRegistry.CHERINE_CAMPFIRE);
        existingLantern(BlockRegistry.CHERINE_LANTERN);

        axisExisting(BlockRegistry.OLVITE_CHAIN);

        existingWithItem(BlockRegistry.CALCITE_FLOWER_POT);
        existingWithItem(BlockRegistry.CALCITE_DECORATED_POT);
        existingWithItem(BlockRegistry.INCUBATOR);
        existingWithItem(BlockRegistry.NEST);
        foodBowl(BlockRegistry.FOOD_BOWL, "food_bowl", "food_bowl_full");
        treeTap();

        bluePortal();
        barsLike(BlockRegistry.GOLDEN_AMBER_BARS);
        itemFromTexture(BlockRegistry.GOLDEN_AMBER_BARS, modLoc("block/golden_amber_bars"));
        existingCheesecake();
        railLike(BlockRegistry.LEVITA_RAIL);
        itemFromTexture(BlockRegistry.LEVITA_RAIL, modLoc("block/levita_rail"));

        everyStateUses(BlockRegistry.PALACE_DOOR.get(), existingBlockModel(BlockRegistry.PALACE_DOOR.get()));
        everyStateUses(BlockRegistry.PALACE_DOOR_EXTENSION.get(), existingModel("palace_door"));
        simpleBlock(BlockRegistry.SURTRUM_AIR.get(), models().getExistingFile(mcLoc("block/air")));
        hugeMushroomLike(BlockRegistry.BROWN_SPORECAP_BLOCK.get(), "brown_sporecap_block_side", "brown_sporecap_block_top");
        itemFromBlockModel(BlockRegistry.BROWN_SPORECAP_BLOCK, "brown_sporecap_block_inventory");
        hugeMushroomLike(BlockRegistry.PINK_SPORECAP_BLOCK.get(), "pink_sporecap_block");
        itemFromBlockModel(BlockRegistry.PINK_SPORECAP_BLOCK, "pink_sporecap_block_inventory");
        hugeMushroomLike(BlockRegistry.ROOTCAP_BLOCK.get(), "rootcap_block");
        itemFromBlockModel(BlockRegistry.ROOTCAP_BLOCK, "rootcap_block_inventory");
        existingBurnishedStoneScript();
        itemFromBlockModel(BlockRegistry.BURNISHED_STONE_SCRIPT, "burnished_stone_a");
    }

    private void mottledAurelWood() {
        ResourceLocation side = sideTex(BlockRegistry.MOTTLED_AUREL_LOG.get());
        rotatedPillar(BlockRegistry.MOTTLED_AUREL_LOG, side, topTex(BlockRegistry.MOTTLED_AUREL_LOG.get()));
        simpleBlockWithItem(BlockRegistry.MOTTLED_AUREL_WOOD.get(),
                models().cubeAll(name(BlockRegistry.MOTTLED_AUREL_WOOD.get()), side));
        axisExisting(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG);
        leafPile(BlockRegistry.AUREL_LEAF_PILE, BlockRegistry.AUREL_WOODSTUFF.leaves().get());
    }
    private void wood(BlockRegistry.WoodBlockSet wood, BlockRegistry.SignSet signs) {
        ResourceLocation planks = blockTexture(wood.plank().get());
        ResourceLocation logSide = sideTex(wood.log().get());
        ResourceLocation logTop = topTex(wood.log().get());
        ResourceLocation strippedSide = sideTex(wood.strippedLog().get());
        ResourceLocation strippedTop = topTex(wood.strippedLog().get());

        rotatedPillar(wood.log().get(), logSide, logTop);

        simpleBlockWithItem(wood.wood().get(), models().cubeAll(name(wood.wood().get()), logSide));
        rotatedPillar(wood.strippedLog().get(), strippedSide, strippedTop);
        simpleBlockWithItem(wood.strippedWood().get(), models().cubeAll(name(wood.strippedWood().get()), strippedSide));

        simpleBlockWithItem(wood.plank().get(), cubeAll(wood.plank().get()));
        stairsBlock(wood.plankStairs().get(), planks);
        blockItem(wood.plankStairs().get());
        slabBlock(wood.plankSlab().get(), planks, planks);
        blockItem(wood.plankSlab().get());
        fenceBlock(wood.fence().get(), planks);
        itemModels().fenceInventory(name(wood.fence().get()), planks);
        fenceGateBlock(wood.fenceGate().get(), planks);
        itemModels().fenceGate(name(wood.fenceGate().get()), planks);
        doorBlockWithRenderType(wood.door().get(),
                modLoc("block/" + name(wood.door().get()) + "_bottom"),
                modLoc("block/" + name(wood.door().get()) + "_top"),
                "cutout");
        itemModels().basicItem(wood.door().get().asItem());
        trapdoorBlockWithRenderType(wood.trapdoor().get(), blockTexture(wood.trapdoor().get()), true, "cutout");
        blockItem(wood.trapdoor().get(), "_bottom");
        buttonWithItem(wood.button(), planks);
        pressurePlateWithItem(wood.pressurePlate(), planks);

        if (wood.leaves() != null) {
            leaves(wood.leaves().get());
        }
        if (wood.sapling() != null) {
            sapling(wood.sapling().get());
        }
        if (wood.flowerPot() != null && wood.sapling() != null) {
            pottedPlant(wood.flowerPot().get(), wood.sapling().get(), blockTexture(wood.sapling().get()));
        }
        signBlock(signs.sign().get(), signs.wallSign().get(), planks);
        itemModels().basicItem(signs.sign().get().asItem());
        hangingSign(signs.hangingSign().get(), signs.wallHangingSign().get(), planks);
        itemModels().basicItem(signs.hangingSign().get().asItem());
    }

    private void wisteriaColor(String prefix, Block leaves, Block leafPile, Block sapling, Block pottedSapling, Block hanger) {
        leaves(leaves);
        simpleBlockWithItem(leafPile, leafPileModel(name(leafPile), blockTexture(leaves)));
        sapling(sapling);
        pottedPlant(pottedSapling, sapling, blockTexture(sapling));
        ModelFile body = models().withExistingParent(prefix + "_wisteria_hanger", modLoc("block/wisteria_hanger"))
                .texture("leaves", blockTexture(leaves))
                .renderType("cutout");
        ModelFile tip = models().withExistingParent(prefix + "_wisteria_hanger_plant", modLoc("block/wisteria_hanger"))
                .texture("leaves", modLoc("block/" + prefix + "_wisteria_hanger_tip"))
                .renderType("cutout");
        getVariantBuilder(hanger).partialState().with(ParadiseLostHangerBlock.TIP, false)
                .modelForState().modelFile(body).addModel();
        getVariantBuilder(hanger).partialState().with(ParadiseLostHangerBlock.TIP, true)
                .modelForState().modelFile(tip).addModel();
        blockItem(hanger);
    }

    private void simpleSet(BlockRegistry.SimpleBlockSet set) {
        ResourceLocation tex = blockTexture(set.block().get());
        stairsBlock(set.stairs().get(), tex);
        blockItem(set.stairs().get());
        slabBlock(set.slab().get(), tex, tex);
        blockItem(set.slab().get());
    }

    private void slabStairs(SlabBlock slab, StairBlock stairs, Block textureBlock) {
        ResourceLocation tex = blockTexture(textureBlock);
        stairsBlock(stairs, tex);
        blockItem(stairs);
        slabBlock(slab, tex, tex);
        blockItem(slab);
    }

    private void slabStairsWall(SlabBlock slab, StairBlock stairs, WallBlock wall, Block textureBlock) {
        slabStairs(slab, stairs, textureBlock);
        ResourceLocation tex = blockTexture(textureBlock);
        wallBlock(wall, tex);
        itemModels().wallInventory(name(wall), tex);
    }

    private void wallOnly(WallBlock wall, Block textureBlock) {
        ResourceLocation tex = blockTexture(textureBlock);
        wallBlock(wall, tex);
        itemModels().wallInventory(name(wall), tex);
    }

    protected <T extends RotatedPillarBlock> void rotatedPillar(RegistryObject<Block, T> block, ResourceLocation side, ResourceLocation end) {
        rotatedPillar(block.get(), block.getId().getPath(), side, end);
    }

    protected void rotatedPillar(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
        rotatedPillar(block, name(block), side, end);
    }

    private void rotatedPillar(RotatedPillarBlock block, String path, ResourceLocation side, ResourceLocation end) {
        axisVariants(block, models().cubeColumn(path, side, end));
        blockItem(block);
    }

    private static ConfiguredModel.Builder quarterTurns(ModelFile model, int rotations) {
        ConfiguredModel.Builder builder = ConfiguredModel.builder().modelFile(model);
        for (int i = 1; i < rotations; i++) {

            builder = builder.nextModel().modelFile(model).rotationY(i * (360 / rotations));
        }
        return builder;
    }

    private void yRotations(Block block, ModelFile model, int steps) {
        getVariantBuilder(block).partialState().setModels(quarterTurns(model, steps).build());
        blockItem(block);
    }

    private void snowOrRotated(Block block, ModelFile snowy, ModelFile normal, int steps) {
        getVariantBuilder(block).partialState().with(SnowyDirtBlock.SNOWY, true)
                .modelForState().modelFile(snowy).addModel();
        getVariantBuilder(block).partialState().with(SnowyDirtBlock.SNOWY, false)
                .setModels(quarterTurns(normal, steps).build());
        blockItem(block);
    }

    private void axisVariants(RotatedPillarBlock block, ModelFile model) {
        getVariantBuilder(block).partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(model).addModel();
        getVariantBuilder(block).partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(model).rotationX(90).addModel();
        getVariantBuilder(block).partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
    }

    private void hangingSign(CeilingHangingSignBlock sign, WallHangingSignBlock wall, ResourceLocation texture) {
        ModelFile model = models().sign(name(sign), texture);
        simpleBlock(sign, model);
        simpleBlock(wall, model);
    }

    private void cube(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    private <T extends Block> void existingWithItem(RegistryObject<Block, T> block) {
        existingWithItem(block.get());
    }

    private void existingWithItem(Block block) {

        simpleBlock(block, existingBlockModel(block));
        blockItem(block);
    }

    private ModelFile existingBlockModel(Block block) {
        return models().getExistingFile(modLoc("block/" + name(block)));
    }

    private ModelFile existingModel(String blockPath) {
        return models().getExistingFile(modLoc("block/" + blockPath));
    }

    private <T extends RotatedPillarBlock> void axisExisting(RegistryObject<Block, T> block) {
        axisVariants(block.get(), existingBlockModel(block.get()));
        if (block.get().asItem() != Items.AIR) {
            blockItem(block.get());
        }
    }

    private void highlandsGrass(Block block, String normalModel, String snowyModel) {
        snowOrRotated(block, existingModel(snowyModel), existingModel(normalModel), 4);
    }

    private void frozenGrass() {

        snowOrRotated(BlockRegistry.FROZEN_GRASS.get(), existingModel("frozen_grass"), existingModel("frozen_grass"), 2);
    }

    private void rotatedPath(Block block, String modelName) {
        yRotations(block, existingModel(modelName), 4);
    }

    private void farmland() {
        Block block = BlockRegistry.FARMLAND.get();
        ModelFile dry = existingModel("farmland");
        ModelFile moist = existingModel("farmland_moist");
        for (int i = 0; i < 7; i++) {
            getVariantBuilder(block).partialState().with(FarmBlock.MOISTURE, i)
                    .modelForState().modelFile(dry).addModel();
        }
        getVariantBuilder(block).partialState().with(FarmBlock.MOISTURE, 7)
                .modelForState().modelFile(moist).addModel();
        blockItem(block);
    }

    private void tintedCross(Block block) {
        tintedCross(block, blockTexture(block));
    }

    private void tintedCross(Block block, ResourceLocation texture) {
        simpleBlock(block, models().withExistingParent(name(block), mcLoc("block/tinted_cross"))
                .texture("cross", texture)
                .renderType("cutout"));

        itemFromTexture(block, texture);
    }

    private void existingSimple(Block block) {

        existingWithItem(block);
    }

    private void floweringGrassItem() {
        itemModels().withExistingParent(name(BlockRegistry.GRASS_FLOWERING.get()), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/grass"))
                .texture("layer1", modLoc("block/grass_flowers"));
    }

    private void everyStateUses(Block block, ModelFile model) {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
    }

    private <T extends Block> void existingRotated(RegistryObject<Block, T> block) {
        yRotations(block.get(), existingBlockModel(block.get()), 4);
    }

    protected <T extends Block> void groundcover(RegistryObject<Block, T> block, String shortModel, String tallModel) {
        Block groundcover = block.get();
        ModelFile small = existingModel(shortModel);
        ModelFile tall = existingModel(tallModel);
        getVariantBuilder(groundcover).partialState().setModels(
                ConfiguredModel.builder().modelFile(small).nextModel()
                        .modelFile(small).rotationY(180).nextModel()
                        .modelFile(tall).nextModel()
                        .modelFile(tall).rotationY(180)
                        .build()
        );
        blockItem(groundcover);
    }

    protected <T extends Block> void foodBowl(RegistryObject<Block, T> bowl, String emptyModel, String fullModel) {
        Block block = bowl.get();
        ModelFile empty = existingModel(emptyModel);
        ModelFile full = existingModel(fullModel);
        for (Direction.Axis axis : List.of(Direction.Axis.X, Direction.Axis.Z)) {

            int y = axis == Direction.Axis.X ? 90 : 0;
            getVariantBuilder(block).partialState()
                    .with(FoodBowlBlock.AXIS, axis).with(FoodBowlBlock.FULL, false)
                    .modelForState().modelFile(empty).rotationY(y).addModel();
            getVariantBuilder(block).partialState()
                    .with(FoodBowlBlock.AXIS, axis).with(FoodBowlBlock.FULL, true)
                    .modelForState().modelFile(full).rotationY(y).addModel();
        }
        blockItem(block);
    }

    private <T extends LanternBlock> void existingLantern(RegistryObject<Block, T> block) {
        existingLantern(block.get());
    }

    private <T extends CampfireBlock> void existingCampfire(RegistryObject<Block, T> block) {
        existingCampfire(block.get());
    }

    private <T extends Block, W extends Block> void existingTorch(RegistryObject<Block, T> torch, RegistryObject<Block, W> wallTorch) {
        existingTorch(torch.get(), wallTorch.get());
    }

    private void crossPlant(Block block) {
        simpleBlock(block, models().cross(name(block), blockTexture(block)).renderType("cutout"));

        itemFromTexture(block, blockTexture(block));
    }

    private void leaves(Block block) {
        simpleBlockWithItem(block, models().withExistingParent(name(block), mcLoc("block/leaves"))
                .texture("all", blockTexture(block))
                .renderType("cutout_mipped"));
    }

    private void sapling(Block block) {
        simpleBlock(block, models().cross(name(block), blockTexture(block)).renderType("cutout"));
        itemFromTexture(block, blockTexture(block));
    }

    private void flower(Block flower, Block potted) {
        crossPlant(flower);
        pottedPlant(potted, flower, blockTexture(flower));
    }

    private void doublePlant(Block block, String bottomPath, String topPath) {
        getVariantBuilder(block).partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                .modelForState().modelFile(existingModel(bottomPath)).addModel();
        getVariantBuilder(block).partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                .modelForState().modelFile(existingModel(topPath)).addModel();

        itemFromTexture(block, modLoc("block/" + topPath));
    }

    private void integerStages(Block block, IntegerProperty ageProp, int values, IntFunction<String> modelName) {
        for (int value = 0; value < values; value++) {
            getVariantBuilder(block).partialState().with(ageProp, value)
                    .modelForState().modelFile(existingModel(modelName.apply(value))).addModel();
        }
    }

    private void halfStages(Block block, DoubleBlockHalf half, int values, IntFunction<String> modelName) {
        for (int value = 0; value < values; value++) {
            getVariantBuilder(block).partialState()
                    .with(TallCropBlock.HALF, half)
                    .with(CropBlock.AGE, value)
                    .modelForState().modelFile(existingModel(modelName.apply(value))).addModel();
        }
    }

    private void cropStages(Block block, IntFunction<String> stageName) {
        integerStages(block, CropBlock.AGE, 8, stageName);
    }

    private static String swedrootStage(int age) {
        if (age < 3) {
            return "swedroot_stage0";
        }
        if (age < 5) {
            return "swedroot_stage1";
        }
        if (age < 7) {
            return "swedroot_stage2";
        }
        return "swedroot_stage3";
    }

    private static String nitraStage(int age) {
        if (age < 2) {
            return "nitra_stage0";
        }
        if (age < 5) {
            return "nitra_stage1";
        }
        if (age < 7) {
            return "nitra_stage2";
        }
        return "nitra_stage3";
    }

    private void flaxCrop() {
        Block block = BlockRegistry.FLAX.get();
        halfStages(block, DoubleBlockHalf.LOWER, 8, age -> "flax_bottom_" + age);

        halfStages(block, DoubleBlockHalf.UPPER, 8, age -> "flax_top_" + Math.max(0, age - 4));
    }

    protected <T extends Block> void pistonLike(RegistryObject<Block, T> block, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {
        ModelFile model = models().withExistingParent(name(block.get()), mcLoc("block/template_piston"))
                .texture("platform", top)
                .texture("bottom", bottom)
                .texture("side", side);
        sixWay(block.get(), model);
    }

    protected <T extends Block> void directionalCloud(RegistryObject<Block, T> cloud, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {
        Block block = cloud.get();
        ModelFile model = models().withExistingParent(name(block), mcLoc("block/template_piston"))
                .texture("platform", top)
                .texture("bottom", bottom)
                .texture("side", side);
        sixWay(block, model);

        itemModels().getBuilder(name(block))
                .parent(models().getExistingFile(mcLoc("block/cube_bottom_top")))
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("side", side);
    }

    private void sixWay(Block block, ModelFile model) {
        for (Direction direction : Direction.values()) {
            var builder = getVariantBuilder(block).partialState().with(BlockStateProperties.FACING, direction).modelForState().modelFile(model);
            switch (direction) {
                case DOWN -> builder.rotationX(90).addModel();
                case UP -> builder.rotationX(270).addModel();
                case EAST -> builder.rotationY(90).addModel();
                case SOUTH -> builder.rotationY(180).addModel();
                case WEST -> builder.rotationY(270).addModel();
                default -> builder.addModel();
            }
        }
        blockItem(block);
    }

    private void horizontalExisting(Block block) {
        ModelFile model = existingBlockModel(block);

        horizontalBlock(block, model, 0);
    }

    private void treeTap() {
        Block block = BlockRegistry.TREE_TAP.get();
        ModelFile model = existingBlockModel(block);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yaw = switch (direction) {
                case NORTH -> 90;
                case EAST -> 180;
                case SOUTH -> 270;
                default -> 0;
            };
            getVariantBuilder(block).partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .modelForState().modelFile(model).rotationY(yaw).addModel();
        }
        blockItem(block);
    }

    private void existingCampfire(CampfireBlock block) {
        ModelFile lit = existingBlockModel(block);
        ModelFile unlit = existingModel(name(block) + "_off");
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yaw = campfireYaw(direction);
            getVariantBuilder(block).partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .with(CampfireBlock.LIT, true)
                    .modelForState().modelFile(lit).rotationY(yaw).addModel();
            getVariantBuilder(block).partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .with(CampfireBlock.LIT, false)
                    .modelForState().modelFile(unlit).rotationY(yaw).addModel();
        }
        blockItem(block);
    }

    private static int campfireYaw(Direction facing) {
        return switch (facing) {
            case SOUTH -> 0;
            case WEST -> 90;
            case NORTH -> 180;
            case EAST -> 270;
            default -> 0;
        };
    }

    private void dustedBlock(Block block, String baseName) {
        integerStages(block, BlockStateProperties.DUSTED, 4, dusted -> baseName + "_" + dusted);

        itemFromBlockModel(block, baseName + "_0");
    }

    private void existingTorch(Block torch, Block wallTorch) {
        simpleBlock(torch, existingBlockModel(torch));
        horizontalBlock(wallTorch, existingBlockModel(wallTorch), 90);
    }

    private void bluePortal() {
        Block block = BlockRegistry.BLUE_PORTAL.get();
        getVariantBuilder(block).partialState().with(ParadiseLostPortalBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(existingModel("blue_portal_ns")).addModel();
        getVariantBuilder(block).partialState().with(ParadiseLostPortalBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(existingModel("blue_portal_ew")).addModel();

        getVariantBuilder(block).partialState().with(ParadiseLostPortalBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(existingModel("blue_portal_flat")).addModel();
    }

    private void existingCheesecake() {
        Block block = BlockRegistry.CHEESECAKE.get();

        integerStages(block, FourBiteCakeBlock.BITES, 4,
                bite -> bite == 0 ? "halflight_cheesecake" : "halflight_cheesecake_slice" + bite);
        itemFromTexture(block, modLoc("item/halflight_cheesecake"));
    }

    private void existingBurnishedStoneScript() {
        Block block = BlockRegistry.BURNISHED_STONE_SCRIPT.get();
        ConfiguredModel.Builder builder = ConfiguredModel.builder().modelFile(existingModel("burnished_stone_a"));

        for (char letter = 'b'; letter <= 'z'; letter++) {
            builder = builder.nextModel().modelFile(existingModel("burnished_stone_" + letter));
        }
        getVariantBuilder(block).partialState().setModels(builder.build());
    }

    private void hugeMushroomLike(Block block, String modelName) {
        hugeMushroomLike(block, modelName, modelName);
    }

    private void hugeMushroomLike(Block block, String modelName, String topModelName) {
        ModelFile skin = existingModel(modelName);
        ModelFile top = existingModel(topModelName);
        ModelFile inside = models().getExistingFile(mcLoc("block/mushroom_block_inside"));
        MultiPartBlockStateBuilder multipart = getMultipartBuilder(block);

        multipart.part().modelFile(skin).addModel().condition(BlockStateProperties.NORTH, true);
        multipart.part().modelFile(skin).uvLock(true).rotationY(90).addModel().condition(BlockStateProperties.EAST, true);
        multipart.part().modelFile(skin).uvLock(true).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true);
        multipart.part().modelFile(skin).uvLock(true).rotationY(270).addModel().condition(BlockStateProperties.WEST, true);
        multipart.part().modelFile(top).uvLock(true).rotationX(270).addModel().condition(BlockStateProperties.UP, true);
        multipart.part().modelFile(top).uvLock(true).rotationX(90).addModel().condition(BlockStateProperties.DOWN, true);

        multipart.part().modelFile(inside).addModel().condition(BlockStateProperties.NORTH, false);
        multipart.part().modelFile(inside).uvLock(false).rotationY(90).addModel().condition(BlockStateProperties.EAST, false);
        multipart.part().modelFile(inside).uvLock(false).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false);
        multipart.part().modelFile(inside).uvLock(false).rotationY(270).addModel().condition(BlockStateProperties.WEST, false);
        multipart.part().modelFile(inside).uvLock(false).rotationX(270).addModel().condition(BlockStateProperties.UP, false);
        multipart.part().modelFile(inside).uvLock(false).rotationX(90).addModel().condition(BlockStateProperties.DOWN, false);
    }

    private <T extends Block> void barsLike(RegistryObject<Block, T> block) {
        ModelFile post = existingModel("golden_amber_bars_post");
        ModelFile postEnds = existingModel("golden_amber_bars_post_ends");
        ModelFile side = existingModel("golden_amber_bars_side");
        ModelFile sideAlt = existingModel("golden_amber_bars_side_alt");
        MultiPartBlockStateBuilder multipart = getMultipartBuilder(block.get());

        multipart.part().modelFile(postEnds).addModel()
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.WEST, false);
        multipart.part().modelFile(post).addModel()
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.WEST, false);

        multipart.part().modelFile(side).addModel().condition(BlockStateProperties.NORTH, true);
        multipart.part().modelFile(side).rotationY(90).addModel().condition(BlockStateProperties.EAST, true);
        multipart.part().modelFile(sideAlt).addModel().condition(BlockStateProperties.SOUTH, true);
        multipart.part().modelFile(sideAlt).rotationY(90).addModel().condition(BlockStateProperties.WEST, true);
    }

    protected <T extends BaseRailBlock> void railLike(RegistryObject<Block, T> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        String path = blockRegistryObject.getId().getPath();

        @SuppressWarnings("unchecked")
        Map<RailGeometry, ModelFile>[] byPower = new Map[4];
        for (boolean triggered : new boolean[] {false, true}) {
            for (boolean powered : new boolean[] {false, true}) {
                String base = path + (triggered ? "_triggered" : "") + (powered ? "_on" : "");
                ResourceLocation texture = modLoc("block/" + base);
                Map<RailGeometry, ModelFile> geometry = new EnumMap<>(RailGeometry.class);
                geometry.put(RailGeometry.FLAT, railModel(base, "rail_flat", texture));
                geometry.put(RailGeometry.RAISED_NE, railModel(base + "_raised_ne", "template_rail_raised_ne", texture));
                geometry.put(RailGeometry.RAISED_SW, railModel(base + "_raised_sw", "template_rail_raised_sw", texture));
                byPower[powerIndex(triggered, powered)] = geometry;
            }
        }

        getVariantBuilder(block).forAllStatesExcept(state -> {
            boolean powered = state.getValue(BlockStateProperties.POWERED);
            boolean triggered = state.getValue(LevitaRailBlock.TRIGGERED);
            RailShape shape = state.getValue(PoweredRailBlock.SHAPE);

            RailGeometry geometry = switch (shape) {
                case ASCENDING_NORTH, ASCENDING_EAST -> RailGeometry.RAISED_NE;
                case ASCENDING_SOUTH, ASCENDING_WEST -> RailGeometry.RAISED_SW;
                default -> RailGeometry.FLAT;
            };

            boolean quarterTurn = switch (shape) {
                case ASCENDING_EAST, ASCENDING_WEST, EAST_WEST -> true;
                default -> false;
            };

            return ConfiguredModel.builder()
                    .modelFile(byPower[powerIndex(triggered, powered)].get(geometry))
                    .rotationY(quarterTurn ? 90 : 0)
                    .build();
        }, BlockStateProperties.WATERLOGGED);
    }

    private enum RailGeometry {FLAT, RAISED_NE, RAISED_SW}

    private static int powerIndex(boolean triggered, boolean powered) {
        return (triggered ? 2 : 0) + (powered ? 1 : 0);
    }

    private ModelFile railModel(String name, String vanillaTemplate, ResourceLocation texture) {
        return models().withExistingParent(name, mcLoc("block/" + vanillaTemplate)).texture("rail", texture);
    }

    private void existingLantern(Block block) {
        getVariantBuilder(block).partialState().with(LanternBlock.HANGING, false)
                .modelForState().modelFile(existingBlockModel(block)).addModel();
        getVariantBuilder(block).partialState().with(LanternBlock.HANGING, true)
                .modelForState().modelFile(existingModel(name(block) + "_hanging")).addModel();
        blockItem(block);
    }

    private void olvitePressurePlate() {
        existingPowered(BlockRegistry.OLVITE_PRESSURE_PLATE.get(), BlockStateProperties.POWERED,
                "olvite_pressure_plate", "olvite_pressure_plate_down");
    }

    protected <T extends Block> void itemFromTexture(RegistryObject<Block, T> block, ResourceLocation texture) {
        itemFromTexture(block.get(), texture);
    }

    private void itemFromTexture(Block block, ResourceLocation texture) {
        itemModels().withExistingParent(name(block), mcLoc("item/generated")).texture("layer0", texture);
    }

    protected <T extends Block> void itemFromBlockModel(RegistryObject<Block, T> block, String blockModelPath) {
        itemFromBlockModel(block.get(), blockModelPath);
    }

    private void itemFromBlockModel(Block block, String blockModelPath) {
        itemModels().withExistingParent(name(block), modLoc("block/" + blockModelPath));
    }

    protected <T extends Block> void columnWithItem(RegistryObject<Block, T> block, ResourceLocation side, ResourceLocation end) {
        simpleBlockWithItem(block.get(), models().cubeColumn(name(block.get()), side, end));
    }

    protected <T extends Block> void carpet(RegistryObject<Block, T> block, ResourceLocation texture) {
        simpleBlockWithItem(block.get(), models().carpet(name(block.get()), texture));
    }

    protected <T extends Block> void bottomTop(RegistryObject<Block, T> block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        simpleBlockWithItem(block.get(), models().cubeBottomTop(name(block.get()), side, bottom, top));
    }

    protected <T extends Block, S extends SlabBlock> void blockWithSlab(RegistryObject<Block, T> block, RegistryObject<Block, S> slab, ResourceLocation texture) {
        simpleBlockWithItem(block.get(), models().cubeAll(name(block.get()), texture));
        slabBlock(slab.get(), texture, texture);
        blockItem(slab.get());
    }

    protected <T extends ButtonBlock> void buttonWithItem(RegistryObject<Block, T> button, ResourceLocation texture) {
        buttonBlock(button.get(), texture);
        itemModels().buttonInventory(name(button.get()), texture);
    }

    protected <T extends PressurePlateBlock> void pressurePlateWithItem(RegistryObject<Block, T> plate, ResourceLocation texture) {
        pressurePlateBlock(plate.get(), texture);
        blockItem(plate.get());
    }

    protected <T extends Block> void leafPile(RegistryObject<Block, T> pile, Block leaves) {
        simpleBlockWithItem(pile.get(), leafPileModel(name(pile.get()), blockTexture(leaves)));
    }

    private ModelFile leafPileModel(String name, ResourceLocation leaves) {
        return models().withExistingParent(name, modLoc("block/leaf_pile"))
                .texture("leaves", leaves)
                .renderType("cutout_mipped");
    }

    private void pottedPlant(Block potted, Block plant, ResourceLocation texture) {
        simpleBlock(potted, models().withExistingParent(name(potted), mcLoc("block/flower_pot_cross"))
                .texture("plant", texture)
                .renderType("cutout"));
    }

    private void existingPowered(Block block, BooleanProperty property, String offModel, String onModel) {
        getVariantBuilder(block).partialState().with(property, false)
                .modelForState().modelFile(existingModel(offModel)).addModel();
        getVariantBuilder(block).partialState().with(property, true)
                .modelForState().modelFile(existingModel(onModel)).addModel();
        blockItem(block);
    }

    private void blockItem(Block block) {
        ResourceLocation sprite = modLoc("item/" + name(block));
        if (existingFileHelper.exists(sprite, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            itemFromTexture(block, sprite);
        } else {
            itemFromBlockModel(block, name(block));
        }
    }

    private void blockItem(Block block, String modelSuffix) {
        itemModels().withExistingParent(name(block), modLoc("block/" + name(block) + modelSuffix));
    }

    private ResourceLocation sideTex(Block log) {
        return modLoc("block/" + name(log) + "_side");
    }

    private ResourceLocation topTex(Block log) {
        return modLoc("block/" + name(log) + "_top");
    }

    private static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
