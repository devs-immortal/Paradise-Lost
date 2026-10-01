package net.id.paradise_lost.world.gen.carver;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

@SuppressWarnings("unused")
public class ParadiseLostCarvers {
    private static final RegistrationProvider<WorldCarver<?>> CARVERS =
            RegistrationProvider.get(Registries.CARVER, ModConstants.MODID);

    public static final WorldCarver<CloudCarverConfig> CLOUD_CARVER =
            register("cloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    private static <T extends CarverConfiguration> WorldCarver<T> register(String name, WorldCarver<T> carver) {

        CARVERS.register(name, () -> carver);
        return carver;
    }

    public static void init() {
    }
}
