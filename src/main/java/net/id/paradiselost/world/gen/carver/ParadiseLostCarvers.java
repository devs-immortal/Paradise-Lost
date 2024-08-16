package net.id.paradiselost.world.gen.carver;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;

@SuppressWarnings("unused")
public class ParadiseLostCarvers {

    public static final Carver<CloudCarverConfig> CLOUD_CARVER = register("cloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registries.CARVER, ParadiseLost.locate(name), carver);
    }

    public static void init() {
    }

}
