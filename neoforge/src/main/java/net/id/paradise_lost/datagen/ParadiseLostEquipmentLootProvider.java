package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ParadiseLostEquipmentLootProvider implements LootTableSubProvider {
    private final HolderLookup.Provider registries;

    public ParadiseLostEquipmentLootProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);

        output.accept(ParadiseLostLootTables.BIRDCAGE_ENVOY, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(NestedLootTable.inlineLootTable(LootTable.lootTable()
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_HELMET.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_CHESTPLATE.get())))
                                        .build()).setWeight(4))
                        .add(NestedLootTable.inlineLootTable(LootTable.lootTable()
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_HELMET.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_CHESTPLATE.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_LEGGINGS.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_BOOTS.get())))
                                        .build()).setWeight(1)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(ItemRegistry.OLVITE_SWORD.get(), 4))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_SWORD.get())
                                .apply(new SetEnchantmentsFunction.Builder(false)
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(1.0F))))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_AXE.get())
                                .apply(new SetEnchantmentsFunction.Builder(false)
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(1.0F))))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_AXE.get())))
                .setRandomSequence(ModConstants.id("equipment/birdcage_envoy")));

        output.accept(ParadiseLostLootTables.BIRDCAGE_ENVOY_OMINOUS, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(NestedLootTable.inlineLootTable(LootTable.lootTable()
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_HELMET.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_CHESTPLATE.get())))
                                        .build()).setWeight(4))
                        .add(NestedLootTable.inlineLootTable(LootTable.lootTable()
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .when(LootItemRandomChanceCondition.randomChance(0.5F))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_HELMET.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_CHESTPLATE.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_LEGGINGS.get())))
                                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                                                .add(LootItem.lootTableItem(ItemRegistry.OLVITE_BOOTS.get())))
                                        .build()).setWeight(1)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(ItemRegistry.OLVITE_SWORD.get(), 2))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_SWORD.get())
                                .apply(new SetEnchantmentsFunction.Builder(false)
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(4.0F))))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_AXE.get())
                                .apply(new SetEnchantmentsFunction.Builder(false)
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(1.0F))
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(2.0F))))
                        .add(LootItem.lootTableItem(ItemRegistry.OLVITE_AXE.get())))
                .setRandomSequence(ModConstants.id("equipment/birdcage_envoy_ominous")));
    }

    private static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }
}
