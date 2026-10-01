package net.id.paradise_lost.datagen;

import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ParadiseLostPopomLootProvider implements LootTableSubProvider {
    public ParadiseLostPopomLootProvider(HolderLookup.Provider registries) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ParadiseLostLootTables.POPOM_JELLY_LEVEL_0, popomJelly(4, -1, 1));
        output.accept(ParadiseLostLootTables.POPOM_JELLY_LEVEL_2, popomJelly(3, 1, 3));
        output.accept(ParadiseLostLootTables.POPOM_JELLY_LEVEL_3, popomJelly(2, 2, 5));
    }

    private static LootTable.Builder popomJelly(int whiteWoolWeight, float jellyMin, float jellyMax) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3)).setBonusRolls(ConstantValue.exactly(0))
                        .add(item(Items.WHITE_WOOL, whiteWoolWeight))
                        .add(item(Items.MAGENTA_WOOL, 1)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(ItemRegistry.POPOM_JELLY.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(jellyMin, jellyMax)))));
    }

    private static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }
}
