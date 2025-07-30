package net.id.paradiselost.datagen;

import dev.thomasglasser.sherdsapi.api.data.FabricSherdDatagenSuite;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.registry.RegistryBuilder;

public class ParadiseLostDataGen implements DataGeneratorEntrypoint {

    private final FabricSherdDatagenSuite sherdSuite = new FabricSherdDatagenSuite(ParadiseLost.MOD_ID);

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        //pack.addProvider(LootTableGen::new);
        //sherdSuite.makeSherdSuite("sol", ParadiseLostItems.SOL_POTTERY_SHERD).build(pack);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        //sherdSuite.buildRegistry(registryBuilder);
    }
}
