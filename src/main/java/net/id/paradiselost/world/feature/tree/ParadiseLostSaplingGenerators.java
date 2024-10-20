package net.id.paradiselost.world.feature.tree;

import net.id.paradiselost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ParadiseLostSaplingGenerators {

    public static final SaplingGenerator AUREL;
    public static final SaplingGenerator MOTTLED_AUREL;
    public static final SaplingGenerator THICKET_AUREL;
    public static final SaplingGenerator MOTHER_AUREL;
    public static final SaplingGenerator ORANGE;
    public static final SaplingGenerator ROSE_WISTERIA;
    public static final SaplingGenerator LAVENDER_WISTERIA;
    public static final SaplingGenerator FROST_WISTERIA;

    static {
        AUREL = new SaplingGenerator("pl_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.AUREL_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_AUREL_TREE), Optional.empty(), Optional.empty());
        MOTTLED_AUREL = new SaplingGenerator("pl_mottled_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.MOTTLED_AUREL), Optional.of(ParadiseLostTreeConfiguredFeatures.DWARF_MOTTLED_AUREL), Optional.empty(), Optional.empty());
        THICKET_AUREL = new SaplingGenerator("pl_thicket_aurel", 0.0F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.THICKET_AUREL_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        MOTHER_AUREL = new SaplingGenerator("pl_mother_aurel", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.MOTHER_AUREL_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        ORANGE = new SaplingGenerator("pl_orange", 0.0F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.ORANGE_TREE), Optional.empty(), Optional.empty(), Optional.empty());
        ROSE_WISTERIA = new SaplingGenerator("pl_rose_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.ROSE_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_ROSE_WISTERIA_TREE), Optional.empty(), Optional.empty());
        LAVENDER_WISTERIA = new SaplingGenerator("pl_lavender_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.LAVENDER_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_LAVENDER_WISTERIA_TREE), Optional.empty(), Optional.empty());
        FROST_WISTERIA = new SaplingGenerator("pl_frost_wisteria", 0.1F, Optional.empty(), Optional.empty(), Optional.of(ParadiseLostTreeConfiguredFeatures.FROST_WISTERIA_TREE), Optional.of(ParadiseLostTreeConfiguredFeatures.FANCY_FROST_WISTERIA_TREE), Optional.empty(), Optional.empty());
    }
}
