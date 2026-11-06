package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.world.feature.structure.processor.TrialSpawnerMarkerProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class StructureProcessorsRegistry {
    private static final RegistrationProvider<StructureProcessorType<?>> STRUCTURE_PROCESSOR_REGISTRATION_PROVIDER =
            RegistrationProvider.get(BuiltInRegistries.STRUCTURE_PROCESSOR, ModConstants.MOD_ID);


    public static final RegistryObject<StructureProcessorType<?>, StructureProcessorType<TrialSpawnerMarkerProcessor>>
            SUPPORT_CHECKER_PROCESSOR = STRUCTURE_PROCESSOR_REGISTRATION_PROVIDER.register("support_checker",
                () -> () -> TrialSpawnerMarkerProcessor.CODEC);


    public static void init() {
    }
}
