package net.id.paradiselost.world.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

import static net.id.paradiselost.ParadiseLost.locate;

/**
 * Generates the Paradise Lost biome instances and registers them with Minecraft. Things in here don't have to be particularly
 * speedy because they are only done once.
 *
 * 1.19 update notes:
 * - Biome categories appear to be gone, verify that they have not been replaced.
 *
 * @author Gudenau
 */
public final class ParadiseLostBiomes {
    public static final RegistryKey<Biome> AUTUMNAL_TUNDRA_KEY = of("autumnal_tundra");
    public static final RegistryKey<Biome> CONTINENTAL_PLATEAU_KEY = of("continental_plateau");
    public static final RegistryKey<Biome> HIGHLANDS_PLAINS_KEY = of("highlands");
    public static final RegistryKey<Biome> HIGHLANDS_FOREST_KEY = of("highlands_forest");
    public static final RegistryKey<Biome> HIGHLANDS_GRAND_GLADE_KEY = of("highlands_grand_glade");
    public static final RegistryKey<Biome> HIGHLANDS_SHIELD_KEY = of("highlands_shield");
    public static final RegistryKey<Biome> HIGHLANDS_THICKET_KEY = of("highlands_thicket");
    public static final RegistryKey<Biome> TRADEWINDS_KEY = of("tradewinds");
    public static final RegistryKey<Biome> WISTERIA_WOODS_KEY = of("wisteria_woods");

    public static void init() {
    }

    private static RegistryKey<Biome> of(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, locate(name));
    }

}
