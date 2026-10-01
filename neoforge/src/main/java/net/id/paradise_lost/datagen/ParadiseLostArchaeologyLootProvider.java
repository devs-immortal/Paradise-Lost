package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ParadiseLostArchaeologyLootProvider implements LootTableSubProvider {
    public ParadiseLostArchaeologyLootProvider(HolderLookup.Provider registries) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ParadiseLostLootTables.REMAINS_COMMON, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(Items.CALCITE))
                        .add(LootItem.lootTableItem(Items.CALCITE))
                        .add(LootItem.lootTableItem(ItemRegistry.SOL_POTTERY_SHERD.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.SOL_POTTERY_SHERD.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.COO_POTTERY_SHERD.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.COO_POTTERY_SHERD.get()))
                        .add(LootItem.lootTableItem(BlockRegistry.CALCITE_FLOWER_POT.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.CHERINE.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.CHERINE.get()))
                        .add(LootItem.lootTableItem(ItemRegistry.XP_CIRCLET.get())
                                .apply(SetComponentsFunction.setComponent(
                                        ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE,
                                        new ParadiseLostDataComponentTypes.XpCircletChargeComponent(100))))
                        .add(LootItem.lootTableItem(ItemRegistry.XP_CIRCLET.get()))
                        .add(LootItem.lootTableItem(Items.STONE_PICKAXE))
                        .add(LootItem.lootTableItem(Items.STONE_PICKAXE))
                        .add(LootItem.lootTableItem(Items.STONE_SHOVEL)))
                .setRandomSequence(ModConstants.id("archaeology/remains_common")));
    }
}
