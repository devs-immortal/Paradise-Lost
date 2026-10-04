package net.id.paradise_lost.world.feature.structure;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostStructureSets {
    private ParadiseLostStructureSets() {}

    public static final ResourceKey<StructureSet> AUREL_TOWER = of("aurel_tower");
    public static final ResourceKey<StructureSet> BIRDCAGE = of("birdcage");
    public static final ResourceKey<StructureSet> PALACE = of("palace");
    public static final ResourceKey<StructureSet> REMAINS = of("remains");
    public static final ResourceKey<StructureSet> VAULT = of("vault");

    public static ResourceKey<StructureSet> of(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, id(name));
    }

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        context.register(AUREL_TOWER, new StructureSet(
                List.of(StructureSet.entry(structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, id("aurel_tower"))), 1)),
                new RandomSpreadStructurePlacement(28, 7, RandomSpreadType.LINEAR, 42069)));
        context.register(BIRDCAGE, new StructureSet(
                List.of(StructureSet.entry(structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, id("birdcage"))), 1)),
                new RandomSpreadStructurePlacement(60, 18, RandomSpreadType.LINEAR, 60030)));
        context.register(PALACE, new StructureSet(
                List.of(StructureSet.entry(structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, id("palace"))), 1)),
                new RandomSpreadStructurePlacement(70, 23, RandomSpreadType.LINEAR, 7222)));
        context.register(REMAINS, new StructureSet(
                List.of(StructureSet.entry(structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, id("remains"))), 1)),
                new RandomSpreadStructurePlacement(24, 6, RandomSpreadType.LINEAR, 3456)));
        context.register(VAULT, new StructureSet(
                List.of(StructureSet.entry(structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, id("vault"))), 1)),
                new RandomSpreadStructurePlacement(40, 16, RandomSpreadType.LINEAR, 60000)));
    }
}
