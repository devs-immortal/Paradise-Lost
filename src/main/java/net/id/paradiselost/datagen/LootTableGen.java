package net.id.paradiselost.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.id.paradiselost.blocks.BlockRegistration;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.CompletableFuture;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

public class LootTableGen extends FabricBlockLootTableProvider {

    protected LootTableGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        // Soil Blocks
        addDrop(FARMLAND, DIRT);
        addDrop(DIRT_PATH, DIRT);
        addDrop(PERMAFROST_PATH, PERMAFROST);
        addDrop(HIGHLANDS_GRASS, block -> this.drops(block, DIRT));
        addDrop(FROZEN_GRASS, block -> this.drops(block, PERMAFROST));
        addDrop(DIRT);
        addDrop(COARSE_DIRT);
        addDrop(LEVITA);
        addDrop(PERMAFROST);
        addDrop(PACKED_SWEDROOT);

        // Clouds
        addDrop(COLD_CLOUD);
        addDrop(BLUE_CLOUD);
        addDrop(GOLDEN_CLOUD);

        // Floestone
        addDrop(FLOESTONE, block -> this.drops(block, COBBLED_FLOESTONE));
        addDrop(FLOESTONE_SLAB, this::slabDrops);
        addDrop(FLOESTONE_STAIRS);
        addDrop(FLOESTONE_WALL);
        // Cobbled Floestone
        addDrop(COBBLED_FLOESTONE);
        addDrop(COBBLED_FLOESTONE_SLAB, this::slabDrops);
        addDrop(COBBLED_FLOESTONE_STAIRS);
        addDrop(COBBLED_FLOESTONE_WALL);
        // Mossy Floestone
        addDrop(MOSSY_FLOESTONE);
        addDrop(GOLDEN_MOSSY_FLOESTONE);
        addDrop(MOSSY_FLOESTONE_SLAB, this::slabDrops);
        addDrop(MOSSY_FLOESTONE_STAIRS);
        addDrop(MOSSY_FLOESTONE_WALL);
        // Floestone Bricks
        addDrop(FLOESTONE_BRICK);
        addDrop(CHISELED_FLOESTONE);
        addDrop(FLOESTONE_BRICK_SLAB, this::slabDrops);
        addDrop(FLOESTONE_BRICK_STAIRS);
        addDrop(FLOESTONE_BRICK_WALL);

        // Heliolith
        addDrop(HELIOLITH);
        addDrop(SMOOTH_HELIOLITH);
        addDrop(HELIOLITH_SLAB, this::slabDrops);
        addDrop(SMOOTH_HELIOLITH_SLAB, this::slabDrops);
        addDrop(HELIOLITH_STAIRS);
        addDrop(SMOOTH_HELIOLITH_STAIRS);
        addDrop(HELIOLITH_WALL);

        // Levita Brick
        addSimpleBlockSetDrops(LEVITA_BRICK_SET);

        // Golden Amber Tile
        addDrop(GOLDEN_AMBER_TILE);
        addDrop(GOLDEN_AMBER_TILE_SLAB, this::slabDrops);
        addDrop(GOLDEN_AMBER_TILE_STAIRS);


        // Misc
        addDrop(BLOOMED_CALCITE);
        addDrop(CHERINE_CAMPFIRE, this::campfireDrops);


        // Aurel Wood
        addWoodBlockSetDrops(AUREL_WOODSTUFF, 0.05F, 0.0625F, 0.083333336F, 0.1F);
        addDrop(MOTTLED_AUREL_LOG);
        addDrop(MOTTLED_AUREL_FALLEN_LOG);
        addDropsWithShears(AUREL_LEAF_PILE);
        addDrop(AUREL_BOOKSHELF, block -> this.drops(block, Items.BOOK, ConstantLootNumberProvider.create(3.0F)));
        addDrop(MOTTLED_AUREL_FALLEN_LOG);
        addSignSetDrops(AUREL_SIGNS);

        // Mother Aurel Wood
        addWoodBlockSetDrops(MOTHER_AUREL_WOODSTUFF, 0.05F, 0.0625F, 0.083333336F, 0.1F);
        addSignSetDrops(MOTHER_AUREL_SIGNS);

        // Orange Wood
        addWoodBlockSetDrops(ORANGE_WOODSTUFF, 0.05F, 0.0625F, 0.083333336F, 0.1F);
        addSignSetDrops(ORANGE_SIGNS);

        // Wisteria Wood
        addDrop(WISTERIA_WOODSTUFF.log());
        addDrop(WISTERIA_WOODSTUFF.wood());
        addDrop(WISTERIA_WOODSTUFF.strippedLog());
        addDrop(WISTERIA_WOODSTUFF.strippedWood());
        addDrop(WISTERIA_WOODSTUFF.plank());
        addDrop(WISTERIA_WOODSTUFF.plankStairs());
        addDrop(WISTERIA_WOODSTUFF.plankSlab(), this::slabDrops);
        addDrop(WISTERIA_WOODSTUFF.fence());
        addDrop(WISTERIA_WOODSTUFF.fenceGate());
        addDrop(WISTERIA_WOODSTUFF.door(), this::doorDrops);
        addDrop(WISTERIA_WOODSTUFF.trapdoor());
        addDrop(WISTERIA_WOODSTUFF.button());
        addDrop(WISTERIA_WOODSTUFF.pressurePlate());
        addSignSetDrops(WISTERIA_SIGNS);

        addDrop(ROSE_WISTERIA_LEAVES, block -> leavesDrops(ROSE_WISTERIA_LEAVES, ROSE_WISTERIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(ROSE_WISTERIA_LEAF_PILE);
        addDrop(ROSE_WISTERIA_SAPLING);
        addPottedPlantDrops(POTTED_ROSE_WISTERIA_SAPLING);
        addDropsWithShears(ROSE_WISTERIA_HANGER);

        addDrop(FROST_WISTERIA_LEAVES, block -> leavesDrops(FROST_WISTERIA_LEAVES, FROST_WISTERIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(FROST_WISTERIA_LEAF_PILE);
        addDrop(FROST_WISTERIA_SAPLING);
        addPottedPlantDrops(POTTED_FROST_WISTERIA_SAPLING);
        addDropsWithShears(FROST_WISTERIA_HANGER);

        addDrop(LAVENDER_WISTERIA_LEAVES, block -> leavesDrops(LAVENDER_WISTERIA_LEAVES, LAVENDER_WISTERIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDropsWithShears(LAVENDER_WISTERIA_LEAF_PILE);
        addDrop(LAVENDER_WISTERIA_SAPLING);
        addPottedPlantDrops(POTTED_LAVENDER_WISTERIA_SAPLING);
        addDropsWithShears(LAVENDER_WISTERIA_HANGER);

        // Grasses
        addDropsWithShears(GRASS);
        addDropsWithShears(GRASS_FLOWERING);
        addDropsWithShears(SHORT_GRASS);
        addDrop(TALL_GRASS, block -> tallPlantNoSeedsDrops(block, GRASS));
        addDropsWithShears(FERN);
        pottedPlantDrops(POTTED_FERN);
        addDropsWithShears(BUSH);
        addDropsWithShears(SHAMROCK);
        addDropsWithShears(MALT_SPRIG);
        addDrop(HONEY_NETTLE, block -> shearsWeightedDrops(HONEY_NETTLE, ParadiseLostItems.AMADRYS_BUSHEL, UniformLootNumberProvider.create(2, 4)));
        addDrop(LIVERWORT);
        addDrop(LIVERWORT_CARPET);

        addDrop(ROOTCAP);
        addDrop(BROWN_SPORECAP);
        addDrop(PINK_SPORECAP);

        addDrop(AMADRYS, block -> cropDrops(AMADRYS, ParadiseLostItems.AMADRYS_BUSHEL, ParadiseLostItems.AMADRYS_BUSHEL,
                BlockStatePropertyLootCondition.builder(AMADRYS)
                        .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 7))));

        addDrop(FLAX, block -> cropDrops(FLAX, ParadiseLostItems.FLAXSEED, ParadiseLostItems.FLAX_THREAD,
                BlockStatePropertyLootCondition.builder(FLAX)
                        .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 7).exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))));

        addDrop(SWEDROOT, block -> cropDrops(SWEDROOT, ParadiseLostItems.SWEDROOT, ParadiseLostItems.SWEDROOT,
                BlockStatePropertyLootCondition.builder(SWEDROOT)
                        .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 7))));

        addDrop(NITRA, block -> cropDrops(NITRA, ParadiseLostItems.NITRA_BULB, ParadiseLostItems.NITRA_SEED,
                BlockStatePropertyLootCondition.builder(NITRA)
                        .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 7))));

        addDrop(FLAXWEAVE_CUSHION);

        addDrop(BLACKCURRANT_BUSH, this::bushDrops);

        // Flowers
        addDrop(ANCIENT_FLOWER);
        addPottedPlantDrops(POTTED_ANCIENT_FLOWER);
        addDrop(ATARAXIA);
        addPottedPlantDrops(POTTED_ATARAXIA);
        addDrop(CLOUDSBLUFF);
        addPottedPlantDrops(POTTED_CLOUDSBLUFF);
        addDrop(DRIGEAN);
        addPottedPlantDrops(POTTED_DRIGEAN);
        addDrop(LUMINAR);
        addPottedPlantDrops(POTTED_LUMINAR);

        // Tall Flowers
        addDrop(WILD_FLAX, wildFlaxDrops());

        // Ores
        addOreDrops(CHERINE_ORE, ParadiseLostItems.CHERINE);
        addOreDrops(OLVITE_ORE, ParadiseLostItems.OLVITE);
        addOreDrops(SURTRUM, ParadiseLostItems.RAW_SURTRUM);
        addDrop(METAMORPHIC_SHELL);
        addOreDrops(LEVITA_ORE, ParadiseLostItems.LEVITA_GEM);
        addDrop(CHERINE_BLOCK);
        addDrop(OLVITE_BLOCK);
        addDrop(REFINED_SURTRUM_BLOCK);

        // Misc
        addDrop(FLOESTONE_BUTTON);
        addDrop(FLOESTONE_PRESSURE_PLATE);
        addDrop(LEVITATOR);
        addDrop(OLVITE_CHAIN);
        addDrop(CHERINE_LANTERN);
        addDrop(CHERINE_TORCH);

        // Usables
        addDrop(INCUBATOR);
        addDrop(FOOD_BOWL);
        addDrop(TREE_TAP);
        addDrop(NITRA_BUNCH);

    }

    // Helper Functions
    private LootTable.Builder tallPlantNoSeedsDrops(Block tallPlant, Block shortPlant) {
        LootPoolEntry.Builder<?> builder = ItemEntry.builder(shortPlant)
                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                .conditionally(WITH_SHEARS);
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .with(builder)
                                .conditionally(
                                        BlockStatePropertyLootCondition.builder(tallPlant).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))
                                )
                                .conditionally(
                                        LocationCheckLootCondition.builder(
                                                LocationPredicate.Builder.create()
                                                        .block(BlockPredicate.Builder.create().blocks(tallPlant).state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))),
                                                new BlockPos(0, 1, 0)
                                        )
                                )
                )
                .pool(
                        LootPool.builder()
                                .with(builder)
                                .conditionally(
                                        BlockStatePropertyLootCondition.builder(tallPlant).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))
                                )
                                .conditionally(
                                        LocationCheckLootCondition.builder(
                                                LocationPredicate.Builder.create()
                                                        .block(BlockPredicate.Builder.create().blocks(tallPlant).state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))),
                                                new BlockPos(0, -1, 0)
                                        )
                                )
                );
    }

    private LootTable.Builder shearsWeightedDrops(Block block, ItemConvertible drop, LootNumberProvider provider) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        LootPoolEntry.Builder<?> builder = this.applyExplosionDecay(
                block,
                ItemEntry.builder(drop)
                        .apply(SetCountLootFunction.builder(provider))
                        .apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))
        );
        return dropsWithShears(block, builder);
    }

    private LootTable.Builder campfireDrops(Block block) {
        return this.dropsWithSilkTouch(
                block,
                this.addSurvivesExplosionCondition(
                        block, ItemEntry.builder(Items.CHARCOAL).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                )
        );
    }

    private LootTable.Builder bushDrops(Block block) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.applyExplosionDecay(
                block,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .conditionally(
                                                BlockStatePropertyLootCondition.builder(BLACKCURRANT_BUSH)
                                                .properties(StatePredicate.Builder.create().exactMatch(SweetBerryBushBlock.AGE, 3)))
                                        .with(ItemEntry.builder(block).apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))
                        )
        );
    }

    private LootTable.Builder wildFlaxDrops() {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        var condition = BlockStatePropertyLootCondition.builder(WILD_FLAX)
                .properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER));
        return this.applyExplosionDecay(
                WILD_FLAX,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .with(ItemEntry.builder(ParadiseLostItems.FLAX_THREAD).conditionally(condition))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3)))
                        )
                        .pool(
                                LootPool.builder()
                                        .conditionally(condition)
                                        .with(ItemEntry.builder(ParadiseLostItems.FLAXSEED).apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))
                        )
        );
    }

    private void addDropsWithShears(Block block) {
        addDrop(block, BlockLootTableGenerator::dropsWithShears);
    }

    private void addOreDrops(Block withSilkTouch, Item withoutSilkTouch) {
        addDrop(withSilkTouch, block -> oreDrops(block, withoutSilkTouch));
    }

    private void addSimpleBlockSetDrops(BlockRegistration.SimpleBlockSet set) {
        addDrop(set.block());
        addDrop(set.slab(), this::slabDrops);
        addDrop(set.stairs());
    }

    private void addWoodBlockSetDrops(BlockRegistration.WoodBlockSet set, float... saplingChance) {
        addDrop(set.sapling());
        addPottedPlantDrops(set.flowerPot());
        addDrop(set.log());
        addDrop(set.wood());
        addDrop(set.strippedLog());
        addDrop(set.strippedWood());
        addDrop(set.leaves(), b -> leavesDrops(set.leaves(), set.sapling(), saplingChance));
        addDrop(set.plank());
        addDrop(set.plankStairs());
        addDrop(set.plankSlab(), this::slabDrops);
        addDrop(set.fence());
        addDrop(set.fenceGate());
        addDrop(set.door(), this::doorDrops);
        addDrop(set.trapdoor());
        addDrop(set.button());
        addDrop(set.pressurePlate());
    }

    private void addSignSetDrops(BlockRegistration.SignSet set) {
        addDrop(set.sign());
        addDrop(set.wallSign());
        addDrop(set.hangingSign());
        addDrop(set.wallHangingSign());
    }

}
