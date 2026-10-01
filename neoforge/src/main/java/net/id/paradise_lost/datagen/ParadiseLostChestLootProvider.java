package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ParadiseLostChestLootProvider implements LootTableSubProvider {
    private static final TagKey<Structure> VAULT_STRUCTURES = TagKey.create(Registries.STRUCTURE, ModConstants.id("vault"));
    private static final ResourceKey<MapDecorationType> VAULT_DECORATION =
            ResourceKey.create(Registries.MAP_DECORATION_TYPE, ModConstants.id("vault"));

    private final HolderLookup.Provider registries;

    public ParadiseLostChestLootProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ParadiseLostLootTables.AUREL_TOWER, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(2))
                        .add(item(Items.STRING, 2).apply(count(1, 3)))
                        .add(item(Items.STICK, 3).apply(count(1, 10)))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 2).apply(count(1, 5))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.OLVITE_SPYGLASS.get(), 1))
                        .add(item(Items.SADDLE, 1)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.5F))
                        .add(LootItem.lootTableItem(ItemRegistry.AUREL_BUCKET.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.AUREL_WATER_BUCKET.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.5F))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap()
                                        .setDestination(VAULT_STRUCTURES)
                                        .setMapDecoration(registries.lookupOrThrow(Registries.MAP_DECORATION_TYPE).getOrThrow(VAULT_DECORATION))
                                        .setZoom((byte) 1)
                                        .setSkipKnownStructures(false))
                                .apply(SetNameFunction.setName(
                                        Component.translatable("filled_map.paradise_lost.vault"),
                                        SetNameFunction.Target.ITEM_NAME))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemRegistry.NITRA_SEED.get()).apply(count(1, 2))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.BLACKCURRANT.get(), 2).apply(count(1, 5)))
                        .add(item(ItemRegistry.AMADRYS_BUSHEL.get(), 2).apply(count(1, 3)))
                        .add(item(ItemRegistry.CHERINE.get(), 1).apply(count(3, 9)))));

        output.accept(ParadiseLostLootTables.BIRDCAGE_TOMB, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5))
                        .add(item(Items.BONE, 5).apply(count(1, 4)))
                        .add(item(ItemRegistry.FLAX_THREAD.get(), 2).apply(count(1, 2)))
                        .add(item(BlockRegistry.BLOOMED_CALCITE.get(), 1).apply(count(1, 2))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                        .add(item(ItemRegistry.TOTEM_OF_LEVITATION.get(), 2))
                        .add(item(ItemRegistry.FLAXWEAVE.get(), 5).apply(count(1, 3)))
                        .add(item(BlockRegistry.PINK_SPORECAP.get(), 3).apply(count(1, 2))))
                .setRandomSequence(ModConstants.id("chests/birdcage/tomb")));

        output.accept(ParadiseLostLootTables.BIRDCAGE_TOMB_BARE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(4))
                        .add(item(Items.BONE, 5).apply(count(1, 2)))
                        .add(item(ItemRegistry.FLAX_THREAD.get(), 2))
                        .add(item(BlockRegistry.BLOOMED_CALCITE.get(), 1)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.FLAXWEAVE.get(), 3))
                        .add(item(BlockRegistry.PINK_SPORECAP.get(), 3).apply(count(1, 3))))
                .setRandomSequence(ModConstants.id("chests/birdcage/tomb_bare")));

        output.accept(ParadiseLostLootTables.PALACE_ARTISAN, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(3))
                        .add(item(BlockRegistry.BLOOMED_CALCITE.get(), 1).apply(count(0, 2)))
                        .add(item(Items.CALCITE, 4).apply(count(1, 3)))
                        .add(item(BlockRegistry.CALCITE_FLOWER_POT.get(), 2).apply(count(0, 2))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(BlockRegistry.CALCITE_DECORATED_POT.get(), 5))
                        .add(item(ItemRegistry.SOL_POTTERY_SHERD.get(), 4)))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(item(ItemRegistry.GOLDEN_AMBER.get(), 2).apply(count(2, 6)))
                        .add(item(BlockRegistry.GOLDEN_AMBER_TILE.get(), 2).apply(count(1, 4))))
                .withPool(floatyLeggingsPool())
                .setRandomSequence(ModConstants.id("chests/palace/artisan")));

        output.accept(ParadiseLostLootTables.PALACE_KEY, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.OLVITE_SWORD.get(), 1).apply(damage(0.8F, 1.0F)))
                        .add(item(Items.BOOK, 2)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.PALACE_KEY.get(), 1)))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(ItemRegistry.GOLDEN_AMBER.get(), 2).apply(count(0, 3)))
                        .add(item(Items.GOLD_NUGGET, 1).apply(count(1, 3))))
                .setRandomSequence(ModConstants.id("chests/palace/artisan")));

        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        output.accept(ParadiseLostLootTables.PALACE_LIBRARY, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                        .add(item(Items.PAPER, 1).apply(count(0, 2)))
                        .add(item(Items.BOOK, 4).apply(count(2, 4)))
                        .add(item(ItemRegistry.FLAXWEAVE.get(), 2).apply(count(1, 3))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(BlockRegistry.FLAXWEAVE_CUSHION.get(), 5).apply(count(1, 2)))
                        .add(item(Items.BOOK, 4).apply(count(1, 3))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 4))
                        .add(item(Items.WRITABLE_BOOK, 1))
                        .add(item(Items.BOOK, 6).apply(EnchantWithLevelsFunction.enchantWithLevels(registries, ConstantValue.exactly(30.0F))
                                .fromOptions(enchantments.getOrThrow(EnchantmentTags.ON_RANDOM_LOOT)))))
                .setRandomSequence(ModConstants.id("chests/palace/library")));

        output.accept(ParadiseLostLootTables.PALACE_SECRET_JUNK, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(3))
                        .add(item(ItemRegistry.OLVITE_NUGGET.get(), 1).apply(count(0, 2)))
                        .add(item(Items.STICK, 4).apply(count(1, 3)))
                        .add(item(Items.GLASS_BOTTLE, 3).apply(count(0, 2)))
                        .add(item(Items.PAPER, 2)))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.OLVITE_CHESTPLATE.get(), 2).apply(damage(0.4F, 0.8F)))
                        .add(item(ItemRegistry.OLVITE_AXE.get(), 5).apply(damage(0.2F, 0.7F)))
                        .add(item(ItemRegistry.OLVITE_PICKAXE.get(), 6).apply(damage(0.1F, 0.6F))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(item(ItemRegistry.OLVITE_HELMET_ORNATE.get(), 1).apply(damage(0.6F, 0.9F))))
                .withPool(floatyLeggingsPool())
                .setRandomSequence(ModConstants.id("chests/palace/artisan")));

        output.accept(ParadiseLostLootTables.PALACE_SWEETS, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                        .add(item(ItemRegistry.AMADRYS_BREAD.get(), 3).apply(count(0, 2)))
                        .add(item(ItemRegistry.AMADRYS_BREAD_GLAZED.get(), 5).apply(count(0, 3)))
                        .add(item(ItemRegistry.BLACKCURRANT_COOKIE.get(), 6).apply(count(1, 3))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.AMADRYS_BREAD_GLAZED_FILLED.get(), 5).apply(count(1, 2)))
                        .add(item(ItemRegistry.BLACKCURRANT_PIE.get(), 4).apply(count(0, 2))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 3))
                        .add(item(Items.GLASS_BOTTLE, 1))
                        .add(item(Items.BOWL, 1))
                        .add(item(ItemRegistry.BLACKCURRANT.get(), 1)))
                .setRandomSequence(ModConstants.id("chests/palace/sweets")));

        output.accept(ParadiseLostLootTables.VAULT_FOOD, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 1).apply(count(0, 2)))
                        .add(item(ItemRegistry.AMADRYS_BREAD.get(), 4).apply(count(2, 4)))
                        .add(item(ItemRegistry.SWEDROOT.get(), 2).apply(count(1, 3)))
                        .add(item(ItemRegistry.AMADRYS_BUSHEL.get(), 3).apply(count(2, 6))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.AMADRYS_BREAD_GLAZED.get(), 5).apply(count(2, 5)))
                        .add(item(Items.SUGAR, 4).apply(count(1, 2)))
                        .add(item(BlockRegistry.AMADRYS_BUNDLE.get(), 2).apply(count(0, 1)))
                        .add(item(BlockRegistry.BROWN_SPORECAP.get(), 1).apply(count(0, 3))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(item(BlockRegistry.MOTHER_AUREL_WOODSTUFF.sapling().get(), 8).apply(count(1, 3)))
                        .add(item(BlockRegistry.ANCIENT_FLOWER.get(), 3))
                        .add(item(ItemRegistry.GLAZED_GOLD_HOE.get(), 1).apply(damage(0.8F, 1.5F))))
                .setRandomSequence(ModConstants.id("chests/vault/food")));

        output.accept(ParadiseLostLootTables.VAULT_JUNK_BLOCKS, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(Items.BOOK, 3).apply(count(0, 2)))
                        .add(item(ItemRegistry.OLVITE_NUGGET.get(), 1).apply(count(1, 3)))
                        .add(item(ItemRegistry.GOLDEN_AMBER.get(), 2).apply(count(2, 4)))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 1).apply(count(1, 2))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(ItemRegistry.CHERINE.get(), 3).apply(count(1, 2)))
                        .add(item(ItemRegistry.OLVITE.get(), 4).apply(count(2, 4)))
                        .add(item(Items.REDSTONE, 2).apply(count(1, 4)))
                        .add(item(ItemRegistry.AUREL_BUCKET.get(), 1)))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(-4, 1))
                        .add(item(ItemRegistry.GLAZED_GOLD_AXE.get(), 2).apply(damage(0.3F, 1.0F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_PICKAXE.get(), 2).apply(damage(0.3F, 1.0F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_SHOVEL.get(), 2).apply(damage(0.3F, 1.0F))))
                .setRandomSequence(ModConstants.id("chests/vault/junk_blocks")));

        output.accept(ParadiseLostLootTables.VAULT_JUNK_DEBRIS, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(ItemRegistry.OLVITE_NUGGET.get(), 4).apply(count(0, 2)))
                        .add(item(BlockRegistry.LEVITA.get(), 4).apply(count(1, 2)))
                        .add(item(ItemRegistry.OLVITE.get(), 3).apply(count(1, 2))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(Items.REDSTONE, 5).apply(count(1, 2)))
                        .add(item(ItemRegistry.CHERINE.get(), 2).apply(count(1, 3)))
                        .add(item(BlockRegistry.BROWN_SPORECAP.get(), 1).apply(count(0, 1))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(-1, 1))
                        .add(LootItem.lootTableItem(Items.BOOK).apply(count(1, 3))))
                .setRandomSequence(ModConstants.id("chests/vault/junk_debris")));

        output.accept(ParadiseLostLootTables.VAULT_JUNK_LOOT, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(Items.GOLD_NUGGET, 3).apply(count(1, 3)))
                        .add(item(ItemRegistry.GOLDEN_AMBER.get(), 2).apply(count(1, 6)))
                        .add(item(Items.BOOK, 2).apply(count(0, 3)))
                        .add(item(ItemRegistry.CHERINE.get(), 1).apply(count(1, 3))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                        .add(item(ItemRegistry.OLVITE.get(), 4).apply(count(2, 6)))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 3).apply(count(1, 3)))
                        .add(item(BlockRegistry.BROWN_SPORECAP.get(), 1).apply(count(0, 2)))
                        .add(item(BlockRegistry.MOTHER_AUREL_WOODSTUFF.sapling().get(), 2)))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(-1, 1))
                        .add(item(Items.RAW_GOLD, 2).apply(count(1, 2)))
                        .add(item(BlockRegistry.CHERINE_LANTERN.get(), 1).apply(count(0, 2))))
                .setRandomSequence(ModConstants.id("chests/vault/junk_loot")));

        output.accept(ParadiseLostLootTables.VAULT_JUNK_UTILITY, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.OLVITE.get(), 3).apply(count(0, 2)))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 2).apply(count(1, 4)))
                        .add(item(BlockRegistry.CHERINE_LANTERN.get(), 2).apply(count(0, 1))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.OLVITE_NUGGET.get(), 5).apply(count(0, 3)))
                        .add(item(ItemRegistry.AMADRYS_BREAD.get(), 5).apply(count(2, 4)))
                        .add(item(Items.EXPERIENCE_BOTTLE, 2).apply(count(3, 7))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(-2, 1))
                        .add(item(BlockRegistry.TREE_TAP.get(), 1))
                        .add(item(ItemRegistry.OLVITE_BLOODSTONE.get(), 3))
                        .add(item(ItemRegistry.GLAZED_GOLD_UPGRADE.get(), 3)))
                .setRandomSequence(ModConstants.id("chests/vault/junk_utility")));

        output.accept(ParadiseLostLootTables.VAULT_VALUABLE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemRegistry.GLAZED_GOLD_UPGRADE.get())))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(ItemRegistry.GLAZED_GOLD_UPGRADE.get(), 4))
                        .add(item(ItemRegistry.GLAZED_GOLD_SWORD.get(), 1).apply(damage(0.6F, 1.5F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_CHESTPLATE.get(), 1).apply(damage(0.6F, 1.5F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_HELMET.get(), 1).apply(damage(0.6F, 1.5F))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                        .add(item(ItemRegistry.GLAZED_GOLD_PICKAXE.get(), 1).apply(damage(0.6F, 1.5F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_SHOVEL.get(), 1).apply(damage(0.6F, 1.5F)))
                        .add(item(ItemRegistry.CHERINE.get(), 3).apply(count(1, 5)))
                        .add(item(ItemRegistry.GOLDEN_AMBER.get(), 4).apply(count(4, 8))))
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 2))
                        .add(item(ItemRegistry.GLAZED_GOLD_UPGRADE.get(), 1))
                        .add(item(BlockRegistry.MOTHER_AUREL_WOODSTUFF.sapling().get(), 4).apply(count(1, 2)))
                        .add(item(Items.EXPERIENCE_BOTTLE, 5).apply(count(0, 2)))
                        .add(item(ItemRegistry.GLAZED_GOLD_LEGGINGS.get(), 1).apply(damage(0.3F, 1.0F)))
                        .add(item(ItemRegistry.GLAZED_GOLD_BOOTS.get(), 2).apply(damage(0.3F, 1.0F))))
                .setRandomSequence(ModConstants.id("chests/vault/valuable")));

        output.accept(ParadiseLostLootTables.POTS_GENERIC, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(ItemRegistry.AMADRYS_BUSHEL.get(), 5).apply(count(4, 8)))
                        .add(item(ItemRegistry.CHERINE.get(), 5).apply(count(1, 6)))
                        .add(item(ItemRegistry.OLVITE_NUGGET.get(), 5).apply(count(2, 5)))
                        .add(item(ItemRegistry.OLVITE.get(), 5)))
                .setRandomSequence(ModConstants.id("pots/remains")));

        output.accept(ParadiseLostLootTables.POTS_REMAINS, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(ItemRegistry.FLAXWEAVE.get(), 15).apply(count(1, 3)))
                        .add(item(ItemRegistry.FLAX_THREAD.get(), 10).apply(count(3, 9)))
                        .add(item(ItemRegistry.FLAXSEED.get(), 5).apply(count(2, 3)))
                        .add(item(ItemRegistry.OLVITE.get(), 5))
                        .add(item(ItemRegistry.LEVITA_GEM.get(), 3)))
                .setRandomSequence(ModConstants.id("pots/remains")));

        output.accept(ParadiseLostLootTables.SPAWNER_GENERIC_FOOD, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(4))
                        .add(item(ItemRegistry.AMADRYS_BREAD.get(), 6).apply(count(1, 3)))
                        .add(item(ItemRegistry.AMADRYS_BUSHEL.get(), 5).apply(count(4, 7)))
                        .add(item(BlockRegistry.AMADRYS_BUNDLE.get(), 3).apply(count(1, 2)))
                        .add(item(ItemRegistry.AMADRYS_NOODLES.get(), 3))
                        .add(item(ItemRegistry.AMADRYS_BREAD_GLAZED.get(), 1).apply(count(1, 4)))));

        output.accept(ParadiseLostLootTables.SPAWNER_LEVITATION_RESOURCES, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(item(ItemRegistry.LEVITA_GEM.get(), 5).apply(count(1, 2)))
                        .add(item(BlockRegistry.LEVITATOR.get(), 3))
                        .add(item(ItemRegistry.LEVITA_WAND.get(), 1))
                        .add(item(ItemRegistry.TOTEM_OF_LEVITATION.get(), 6))));

        output.accept(ParadiseLostLootTables.SPAWNER_UTILITY, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                        .add(item(ItemRegistry.CHERINE_TORCH.get(), 6).apply(count(4, 16)))
                        .add(item(BlockRegistry.NITRA_BUNCH.get(), 4).apply(count(1, 4)))
                        .add(item(BlockRegistry.BLUE_CLOUD.get(), 4).apply(count(2, 6)))
                        .add(item(ItemRegistry.NITRA_BULB.get(), 3).apply(count(3, 8)))
                        .add(item(BlockRegistry.FLAXWEAVE_CUSHION.get(), 1))
                        .add(item(ItemRegistry.NITRA_SEED.get(), 1))));
    }

    private static LootPool.Builder floatyLeggingsPool() {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(0.35F))
                .add(item(ItemRegistry.FLOATY_LEGGINGS.get(), 1).apply(damage(0.7F, 1.0F)));
    }

    private static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }

    private static LootItemConditionalFunction.Builder<?> count(float min, float max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    private static LootItemConditionalFunction.Builder<?> damage(float min, float max) {
        return SetItemDamageFunction.setDamage(UniformGenerator.between(min, max));
    }
}
