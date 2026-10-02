package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ModConstants.MODID)
public final class ParadiseLostDataGenerator {
    private ParadiseLostDataGenerator() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existing = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        var withDamage = generator.addProvider(event.includeServer(), new ParadiseLostDamageTypesProvider(packOutput, lookup)).getRegistryProvider();
        var withEnchantments = generator.addProvider(event.includeServer(), new ParadiseLostEnchantmentProvider(packOutput, withDamage)).getRegistryProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(
                packOutput,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ParadiseLostBlockLootProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ParadiseLostEntityLootProvider::new, LootContextParamSets.ENTITY),
                        new LootTableProvider.SubProviderEntry(ParadiseLostChestLootProvider::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(ParadiseLostArchaeologyLootProvider::new, LootContextParamSets.ARCHAEOLOGY),
                        new LootTableProvider.SubProviderEntry(ParadiseLostEquipmentLootProvider::new, LootContextParamSets.EQUIPMENT),
                        new LootTableProvider.SubProviderEntry(ParadiseLostGameplayLootProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ParadiseLostPopomLootProvider::new, LootContextParamSets.ENTITY)
                ),
                withEnchantments
        ));
        generator.addProvider(event.includeServer(), new ParadiseLostCompostablesProvider(packOutput, withEnchantments));
        generator.addProvider(event.includeServer(), new ParadiseLostFuelsProvider(packOutput, withEnchantments));
        generator.addProvider(event.includeServer(), new ParadiseLostStrippablesProvider(packOutput, withEnchantments));
        generator.addProvider(event.includeServer(), new ParadiseLostMoaDatapackProvider(packOutput, withEnchantments));

        BlockTagsProvider blockTags = new ParadiseLostBlockTagsProvider(packOutput, withEnchantments, existing);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ParadiseLostItemTagsProvider(packOutput, withEnchantments, blockTags.contentsGetter(), existing));
        generator.addProvider(event.includeServer(), new ParadiseLostBiomeTagsProvider(packOutput, withEnchantments, existing));
        generator.addProvider(event.includeServer(), new ParadiseLostEntityTypeTagsProvider(packOutput, withEnchantments));
        generator.addProvider(event.includeServer(), new ParadiseLostDamageTypeTagsProvider(packOutput, withEnchantments, existing));
        generator.addProvider(event.includeServer(), new ParadiseLostEnchantmentTagsProvider(packOutput, withEnchantments, existing));
        generator.addProvider(event.includeServer(), new ParadiseLostStructureTagsProvider(packOutput, withEnchantments, existing));

        generator.addProvider(event.includeServer(), new ParadiseLostRecipeProvider(packOutput, withEnchantments));
        generator.addProvider(event.includeServer(), new ParadiseLostAdvancementProvider(packOutput, withEnchantments, existing));

        generator.addProvider(event.includeClient(), new ParadiseLostStaticModelsProvider(packOutput, existing));
        generator.addProvider(event.includeClient(), new ParadiseLostBlockStateProvider(packOutput, existing));
        generator.addProvider(event.includeClient(), new ParadiseLostItemModelProvider(packOutput, existing));
        generator.addProvider(event.includeClient(), new ParadiseLostSoundDefinitionsProvider(packOutput, existing));
        generator.addProvider(event.includeClient(), new ParadiseLostParticleDescriptionProvider(packOutput, existing));
        generator.addProvider(event.includeServer(), new ParadiseLostWorldGenProvider(packOutput, withEnchantments));
    }
}
