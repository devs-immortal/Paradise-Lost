package net.id.paradiselost.world.feature.tree;

import com.mojang.serialization.Codec;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ParadiseLostTreeHell {

    public static FoliagePlacerType<WisteriaFoliagePlacer> WISTERIA_FOLIAGE;
    public static TrunkPlacerType<WisteriaTrunkPlacer> WISTERIA_TRUNK;
    public static TrunkPlacerType<OvergrownTrunkPlacer> OVERGROWN_TRUNK;
    private static Constructor<FoliagePlacerType> foliageConstructor;
    private static Constructor<TrunkPlacerType> trunkConstructor;

    static {
        try {
            foliageConstructor = FoliagePlacerType.class.getDeclaredConstructor(Codec.class);
            foliageConstructor.setAccessible(true);
            trunkConstructor = TrunkPlacerType.class.getDeclaredConstructor(Codec.class);
            trunkConstructor.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(String name, Codec<P> codec) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return Registry.register(Registries.FOLIAGE_PLACER_TYPE, ParadiseLost.locate(name), foliageConstructor.newInstance(codec));
    }

    public static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(String name, Codec<P> codec) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return Registry.register(Registries.TRUNK_PLACER_TYPE, ParadiseLost.locate(name), trunkConstructor.newInstance(codec));
    }

    public static void init() {
        try {
            WISTERIA_FOLIAGE = registerFoliage("wisteria_folliage_placer", WisteriaFoliagePlacer.CODEC);
            WISTERIA_TRUNK = registerTrunk("wisteria_trunk_placer", WisteriaTrunkPlacer.CODEC);
            OVERGROWN_TRUNK = registerTrunk("overgrown_trunk_placer", OvergrownTrunkPlacer.CODEC);
            foliageConstructor.setAccessible(false);
            trunkConstructor.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
