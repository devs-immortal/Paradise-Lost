package net.id.paradise_lost.datagen;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.HashSet;
import java.util.Set;

import static net.id.paradise_lost.registry.BlockRegistry.*;
import net.minecraft.world.flag.FeatureFlags;

public class ParadiseLostBlockLootProvider extends BlockLootSubProvider {

    private final Set<Block> knownBlocks = new HashSet<>();

    public ParadiseLostBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        knownBlocks.add(block);
        super.add(block, builder);
    }

    @Override
    public void generate() {

        addDrop(FARMLAND.get(), DIRT.get());
        addDrop(DIRT_PATH.get(), DIRT.get());
        addDrop(PERMAFROST_PATH.get(), PERMAFROST.get());
        addDrop(HIGHLANDS_GRASS.get(), block -> createSingleItemTableWithSilkTouch(block, DIRT.get()));
        addDrop(FROZEN_GRASS.get(), block -> createSingleItemTableWithSilkTouch(block, PERMAFROST.get()));
        addDrop(DIRT.get());
        addDrop(COARSE_DIRT.get());
        addDrop(LEVITA.get(), this::levitaDrops);
        addDrop(PERMAFROST.get());
        addDrop(PACKED_SWEDROOT.get());

        addDrop(COLD_CLOUD.get());
        addDrop(BLUE_CLOUD.get());
        addDrop(GOLDEN_CLOUD.get());
        addDrop(GREEN_CLOUD.get());

        addDrop(FLOESTONE.get(), block -> createSingleItemTableWithSilkTouch(block, COBBLED_FLOESTONE.get()));
        addDrop(FLOESTONE_SLAB.get(), this::createSlabItemTable);
        addDrop(FLOESTONE_STAIRS.get());
        addDrop(FLOESTONE_WALL.get());

        addDrop(COBBLED_FLOESTONE.get());
        addDrop(COBBLED_FLOESTONE_SLAB.get(), this::createSlabItemTable);
        addDrop(COBBLED_FLOESTONE_STAIRS.get());
        addDrop(COBBLED_FLOESTONE_WALL.get());

        addDrop(MOSSY_FLOESTONE.get());
        addDrop(GOLDEN_MOSSY_FLOESTONE.get());
        addDrop(MOSSY_FLOESTONE_SLAB.get(), this::createSlabItemTable);
        addDrop(MOSSY_FLOESTONE_STAIRS.get());
        addDrop(MOSSY_FLOESTONE_WALL.get());

        addDrop(FLOESTONE_BRICK.get());
        addDrop(CHISELED_FLOESTONE.get());
        addDrop(FLOESTONE_BRICK_SLAB.get(), this::createSlabItemTable);
        addDrop(FLOESTONE_BRICK_STAIRS.get());
        addDrop(FLOESTONE_BRICK_WALL.get());
        addDrop(MOSSY_FLOESTONE_BRICK.get());
        addDrop(MOSSY_FLOESTONE_BRICK_SLAB.get(), this::createSlabItemTable);
        addDrop(MOSSY_FLOESTONE_BRICK_STAIRS.get());
        addDrop(MOSSY_FLOESTONE_BRICK_WALL.get());
        addDrop(SMOOTH_FLOESTONE.get());
        addDrop(SMOOTH_FLOESTONE_SLAB.get(), this::createSlabItemTable);
        addDrop(SMOOTH_FLOESTONE_STAIRS.get());

        addDrop(HELIOLITH.get());
        addDrop(SMOOTH_HELIOLITH.get());
        addDrop(HELIOLITH_SLAB.get(), this::createSlabItemTable);
        addDrop(SMOOTH_HELIOLITH_SLAB.get(), this::createSlabItemTable);
        addDrop(HELIOLITH_STAIRS.get());
        addDrop(SMOOTH_HELIOLITH_STAIRS.get());
        addDrop(HELIOLITH_WALL.get());

        addSimpleBlockSetDrops(LEVITA_BRICK_SET);
        addDrop(CHISELED_LEVITA_BRICK.get());

        addSimpleBlockSetDrops(THATCH_SET);
        addDrop(NEST.get());
        addDrop(LEVITA_RAIL.get());
        addDrop(GOLDEN_AMBER_BARS.get());
        addDrop(CALCITE_FLOWER_POT.get());
        addDrop(CALCITE_DECORATED_POT.get(), this::decoratedPotTable);

        addSimpleBlockSetDrops(BURNISHED_STONE_SET);
        addDrop(BURNISHED_STONE_WALL.get());
        addDrop(BURNISHED_STONE_PLAQUE.get());
        addDrop(BURNISHED_STONE_SCRIPT.get());

        addDrop(GOLDEN_AMBER_TILE.get());
        addDrop(GOLDEN_AMBER_TILE_SLAB.get(), this::createSlabItemTable);
        addDrop(GOLDEN_AMBER_TILE_STAIRS.get());

        addSimpleBlockSetDrops(CALCITE_TILES_SET);
        addDrop(CALCITE_TILES_WALL.get());
        addSimpleBlockSetDrops(BLOOMED_CALCITE_TILES_SET);
        addDrop(BLOOMED_CALCITE_TILES_WALL.get());

        addDrop(BLOOMED_CALCITE.get());
        addDrop(CHERINE_CAMPFIRE.get(), this::campfireDrops);

        addWoodBlockSetDrops(AUREL_WOODSTUFF, 0.05F, 0.0625F, 0.083333336F, 0.1F);
        addDrop(MOTTLED_AUREL_LOG.get());
        addDrop(MOTTLED_AUREL_WOOD.get());
        addDrop(MOTTLED_AUREL_FALLEN_LOG.get());
        addDropsWithShears(AUREL_LEAF_PILE.get());
        addDrop(AUREL_BOOKSHELF.get(), block -> createSingleItemTableWithSilkTouch(block, Items.BOOK));
        addDrop(MOTTLED_AUREL_FALLEN_LOG.get());
        addSignSetDrops(AUREL_SIGNS);

        addWoodBlockSetDrops(MOTHER_AUREL_WOODSTUFF, 0.005F, 0.00625F, 0.0083333336F, 0.01F);
        addSignSetDrops(MOTHER_AUREL_SIGNS);

        addWoodBlockSetDrops(MENTH_WOODSTUFF, 0.05F, 0.0625F, 0.083333336F, 0.1F);
        addSignSetDrops(MENTH_SIGNS);

        addDrop(WISTERIA_WOODSTUFF.log().get());
        addDrop(WISTERIA_WOODSTUFF.wood().get());
        addDrop(WISTERIA_WOODSTUFF.strippedLog().get());
        addDrop(WISTERIA_WOODSTUFF.strippedWood().get());
        addDrop(WISTERIA_WOODSTUFF.plank().get());
        addDrop(WISTERIA_WOODSTUFF.plankStairs().get());
        addDrop(WISTERIA_WOODSTUFF.plankSlab().get(), this::createSlabItemTable);
        addDrop(WISTERIA_WOODSTUFF.fence().get());
        addDrop(WISTERIA_WOODSTUFF.fenceGate().get());
        addDrop(WISTERIA_WOODSTUFF.door().get(), this::createDoorTable);
        addDrop(WISTERIA_WOODSTUFF.trapdoor().get());
        addDrop(WISTERIA_WOODSTUFF.button().get());
        addDrop(WISTERIA_WOODSTUFF.pressurePlate().get());
        addSignSetDrops(WISTERIA_SIGNS);

        addDrop(ROSE_WISTERIA_LEAVES.get(), block -> createLeavesDrops(ROSE_WISTERIA_LEAVES.get(), ROSE_WISTERIA_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(ROSE_WISTERIA_LEAF_PILE.get());
        addDrop(ROSE_WISTERIA_SAPLING.get());
        addPottedPlantDrops(POTTED_ROSE_WISTERIA_SAPLING.get());
        addDropsWithShears(ROSE_WISTERIA_HANGER.get());

        addDrop(FROST_WISTERIA_LEAVES.get(), block -> createLeavesDrops(FROST_WISTERIA_LEAVES.get(), FROST_WISTERIA_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(FROST_WISTERIA_LEAF_PILE.get());
        addDrop(FROST_WISTERIA_SAPLING.get());
        addPottedPlantDrops(POTTED_FROST_WISTERIA_SAPLING.get());
        addDropsWithShears(FROST_WISTERIA_HANGER.get());

        addDrop(LAVENDER_WISTERIA_LEAVES.get(), block -> createLeavesDrops(LAVENDER_WISTERIA_LEAVES.get(), LAVENDER_WISTERIA_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(LAVENDER_WISTERIA_LEAF_PILE.get());
        addDrop(LAVENDER_WISTERIA_SAPLING.get());
        addPottedPlantDrops(POTTED_LAVENDER_WISTERIA_SAPLING.get());
        addDropsWithShears(LAVENDER_WISTERIA_HANGER.get());

        addDropsWithShears(GRASS.get());
        addDropsWithShears(GRASS_FLOWERING.get());
        addDropsWithShears(SHORT_GRASS.get());
        addDrop(TALL_GRASS.get(), block -> tallPlantNoSeedsDrops(block, GRASS.get()));
        addDropsWithShears(FERN.get());
        pottedPlantDrops(POTTED_FERN.get());
        addDropsWithShears(BUSH.get());
        addDropsWithShears(SHAMROCK.get());
        addDropsWithShears(MALT_SPRIG.get());
        addDrop(HONEY_NETTLE.get(), block -> shearsWeightedDrops(HONEY_NETTLE.get(), ItemRegistry.AMADRYS_BUSHEL.get(), UniformGenerator.between(2, 4)));
        addDrop(LIVERWORT.get());
        addDrop(LIVERWORT_CARPET.get());

        addDrop(ROOTCAP.get());
        addDrop(BROWN_SPORECAP.get());
        addDrop(PINK_SPORECAP.get());
        addDrop(ROOTCAP_BLOCK.get());
        addDrop(BROWN_SPORECAP_BLOCK.get());
        addDrop(PINK_SPORECAP_BLOCK.get());

        addDrop(AMADRYS.get(), block -> createCropDrops(AMADRYS.get(), ItemRegistry.AMADRYS_BUSHEL.get(), ItemRegistry.AMADRYS_BUSHEL.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(AMADRYS.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));

        addDrop(FLAX.get(), block -> createCropDrops(FLAX.get(), ItemRegistry.FLAXSEED.get(), ItemRegistry.FLAX_THREAD.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(FLAX.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7).hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))));

        addDrop(SWEDROOT.get(), block -> createCropDrops(SWEDROOT.get(), ItemRegistry.SWEDROOT.get(), ItemRegistry.SWEDROOT.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(SWEDROOT.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));

        addDrop(NITRA.get(), block -> createCropDrops(NITRA.get(), ItemRegistry.NITRA_BULB.get(), ItemRegistry.NITRA_SEED.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(NITRA.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));

        addDrop(FLAXWEAVE_CUSHION.get());
        addDrop(FLAXWEAVE_CUSHION_SLAB.get(), this::createSlabItemTable);

        addDrop(BLACKCURRANT_BUSH.get(), this::bushDrops);
        addDrop(CHEESECAKE.get());
        addDrop(AMADRYS_BUNDLE.get());

        addDrop(ANCIENT_FLOWER.get());
        addPottedPlantDrops(POTTED_ANCIENT_FLOWER.get());
        addDrop(ATARAXIA.get());
        addPottedPlantDrops(POTTED_ATARAXIA.get());
        addDrop(CLOUDSBLUFF.get());
        addPottedPlantDrops(POTTED_CLOUDSBLUFF.get());
        addDrop(DRIGEAN.get());
        addPottedPlantDrops(POTTED_DRIGEAN.get());
        addDrop(LUMINAR.get());
        addPottedPlantDrops(POTTED_LUMINAR.get());

        addDrop(WILD_FLAX.get(), wildFlaxDrops());

        addOreDrops(CHERINE_ORE.get(), ItemRegistry.CHERINE.get());
        addOreDrops(OLVITE_ORE.get(), ItemRegistry.OLVITE.get());
        addDrop(FLOESTONE_REDSTONE_ORE.get(), this::createRedstoneOreDrops);
        addOreDrops(SURTRUM.get(), ItemRegistry.RAW_SURTRUM.get());
        addDrop(METAMORPHIC_SHELL.get());
        addOreDrops(LEVITA_ORE.get(), ItemRegistry.LEVITA_GEM.get());
        addDrop(CHERINE_BLOCK.get());
        addDrop(OLVITE_BLOCK.get());
        addDrop(REFINED_SURTRUM_BLOCK.get());

        addDrop(FLOESTONE_BUTTON.get());
        addDrop(FLOESTONE_PRESSURE_PLATE.get());
        addDrop(OLVITE_PRESSURE_PLATE.get());
        addDrop(LEVITATOR.get());
        addDrop(OLVITE_CHAIN.get());
        addDrop(CHERINE_LANTERN.get());
        addDrop(CHERINE_TORCH.get());

        addDrop(INCUBATOR.get());
        addDrop(FOOD_BOWL.get());
        addDrop(TREE_TAP.get());
        addDrop(NITRA_BUNCH.get());

    }

    private LootTable.Builder tallPlantNoSeedsDrops(Block tallPlant, Block shortPlant) {
        LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(shortPlant)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                .when(HAS_SHEARS);
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .add(builder)
                                .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tallPlant).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))
                                )
                                .when(
                                        LocationCheck.checkLocation(
                                                LocationPredicate.Builder.location()
                                                        .setBlock(BlockPredicate.Builder.block().of(tallPlant).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))),
                                                new BlockPos(0, 1, 0)
                                        )
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .add(builder)
                                .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tallPlant).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))
                                )
                                .when(
                                        LocationCheck.checkLocation(
                                                LocationPredicate.Builder.location()
                                                        .setBlock(BlockPredicate.Builder.block().of(tallPlant).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))),
                                                new BlockPos(0, -1, 0)
                                        )
                                )
                );
    }

    private LootTable.Builder levitaDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionCondition(
                        block,
                        LootItem.lootTableItem(ItemRegistry.LEVITA_SHARD.get())
                                .when(
                                        BonusLevelTableCondition.bonusLevelFlatChance(impl.getOrThrow(Enchantments.FORTUNE), 0.07F, 0.12F, 0.185F, 0.65F)
                                )
                                .otherwise(LootItem.lootTableItem(block))
                )
        );
    }

    private LootTable.Builder shearsWeightedDrops(Block block, ItemLike drop, NumberProvider provider) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootPoolEntryContainer.Builder<?> builder = this.applyExplosionDecay(
                block,
                LootItem.lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(provider))
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))
        );
        return createShearsDispatchTable(block, builder);
    }

    private LootTable.Builder campfireDrops(Block block) {
        return this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionCondition(block, LootItem.lootTableItem(ItemRegistry.CHERINE.get()))
        );
    }

    private LootTable.Builder bushDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.applyExplosionDecay(
                block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .when(
                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(BLACKCURRANT_BUSH.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))
                                        .add(LootItem.lootTableItem(block).apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))
                        )
        );
    }

    private LootTable.Builder wildFlaxDrops() {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        var condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(WILD_FLAX.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        return this.applyExplosionDecay(
                WILD_FLAX.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .add(LootItem.lootTableItem(ItemRegistry.FLAX_THREAD.get()).when(condition))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .when(condition)
                                        .add(LootItem.lootTableItem(ItemRegistry.FLAXSEED.get()).apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))
                        )
        );
    }

    private void addDropsWithShears(Block block) {
        addDrop(block, b -> createShearsOnlyDrop(b));
    }

    private void addOreDrops(Block withSilkTouch, Item withoutSilkTouch) {
        addDrop(withSilkTouch, block -> createOreDrop(block, withoutSilkTouch));
    }

    private void addSimpleBlockSetDrops(BlockRegistry.SimpleBlockSet set) {
        addDrop(set.block().get());
        addDrop(set.slab().get(), this::createSlabItemTable);
        addDrop(set.stairs().get());
    }

    private void addWoodBlockSetDrops(BlockRegistry.WoodBlockSet set, float... saplingChance) {
        if (set.sapling() != null) {
            addDrop(set.sapling().get());
        }
        if (set.flowerPot() != null) {
            addPottedPlantDrops(set.flowerPot().get());
        }
        addDrop(set.log().get());
        addDrop(set.wood().get());
        addDrop(set.strippedLog().get());
        addDrop(set.strippedWood().get());
        if (set.leaves() != null && set.sapling() != null) {
            addDrop(set.leaves().get(), b -> createLeavesDrops(set.leaves().get(), set.sapling().get(), saplingChance));
        }
        addDrop(set.plank().get());
        addDrop(set.plankStairs().get());
        addDrop(set.plankSlab().get(), this::createSlabItemTable);
        addDrop(set.fence().get());
        addDrop(set.fenceGate().get());
        addDrop(set.door().get(), this::createDoorTable);
        addDrop(set.trapdoor().get());
        addDrop(set.button().get());
        addDrop(set.pressurePlate().get());
    }

    private void addSignSetDrops(BlockRegistry.SignSet set) {
        addDrop(set.sign().get());
        addDrop(set.wallSign().get());
        addDrop(set.hangingSign().get());
        addDrop(set.wallHangingSign().get());
    }

    private void addDrop(Block block) {
        dropSelf(block);
    }

    private void addDrop(Block block, Block drop) {
        add(block, createSingleItemTable(drop));
    }

    private void addDrop(Block block, java.util.function.Function<Block, LootTable.Builder> function) {
        add(block, function);
    }

    private void addDrop(Block block, LootTable.Builder builder) {
        add(block, builder);
    }

    private LootTable.Builder decoratedPotTable(Block block) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        DynamicLoot.dynamicEntry(DecoratedPotBlock.SHERDS_DYNAMIC_DROP_ID)
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DecoratedPotBlock.CRACKED, true))
                                                )
                                                .otherwise(
                                                        LootItem.lootTableItem(block)
                                                                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.POT_DECORATIONS))
                                                )
                                )
                );
    }

    private void addPottedPlantDrops(Block block) {
        dropPottedContents(block);
    }

    private void pottedPlantDrops(Block block) {
        dropPottedContents(block);
    }

}
