package com.aether.world.feature.tree;

import com.aether.Aether;
import com.aether.world.feature.tree.placers.WisteriaFoliagePlacer;
import com.aether.world.feature.tree.placers.WisteriaTrunkPlacer;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AetherTreeHell {

    private static Constructor<FoliagePlacerType> foliageConstructor;
    private static Constructor<TrunkPlacerType> trunkConstructor;

    public static FoliagePlacerType<WisteriaFoliagePlacer> WISTERIA_FOLIAGE;

    public static TrunkPlacerType<WisteriaTrunkPlacer> WISTERIA_TRUNK;

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(String name, Codec<P> codec) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return Registry.register(Registry.FOLIAGE_PLACER_TYPES, Aether.locate(name), foliageConstructor.newInstance(codec));
    }

    public static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(String name, Codec<P> codec) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return Registry.register(Registry.TRUNK_PLACER_TYPES, Aether.locate(name), trunkConstructor.newInstance(codec));
    }

    public static void init() {
        try {
            WISTERIA_FOLIAGE = registerFoliage("wisteria_folliage_placer", WisteriaFoliagePlacer.CODEC);
            WISTERIA_TRUNK = registerTrunk("wisteria_trunk_placer", WisteriaTrunkPlacer.CODEC);
            foliageConstructor.setAccessible(false);
            trunkConstructor.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
}
