package net.id.paradise_lost.world.feature.tree;

import net.id.paradise_lost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;
import java.util.Optional;

public class ParadiseLostSaplingGenerators {

    public static final TreeGrower AUREL;
    public static final TreeGrower MOTTLED_AUREL;
    public static final TreeGrower THICKET_AUREL;
    public static final TreeGrower MOTHER_AUREL;
    public static final TreeGrower MENTH;
    public static final TreeGrower ROSE_WISTERIA;
    public static final TreeGrower LAVENDER_WISTERIA;
    public static final TreeGrower FROST_WISTERIA;

    static {
        AUREL = new TreeGrower("pl_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.AUREL_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_AUREL_TREE), Optional.empty(), Optional.empty());
        MOTTLED_AUREL = new TreeGrower("pl_mottled_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.MOTTLED_AUREL), Optional.of(ParadiseLostTreeConfiguredFeatures.DWARF_MOTTLED_AUREL), Optional.empty(), Optional.empty());
        THICKET_AUREL = new TreeGrower("pl_thicket_aurel", 0.0F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.THICKET_AUREL_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        MOTHER_AUREL = new TreeGrower("pl_mother_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.MOTHER_AUREL_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        MENTH = new TreeGrower("pl_menth", 0.0F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.MENTH_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        ROSE_WISTERIA = new TreeGrower("pl_rose_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.ROSE_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE), Optional.empty(), Optional.empty());
        LAVENDER_WISTERIA = new TreeGrower("pl_lavender_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.LAVENDER_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE), Optional.empty(), Optional.empty());
        FROST_WISTERIA = new TreeGrower("pl_frost_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.FROST_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_FROST_WISTERIA_TREE), Optional.empty(), Optional.empty());
    }
}
