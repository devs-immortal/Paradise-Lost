package net.id.paradiselost.world.feature.tree;

import com.mojang.serialization.MapCodec;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.tree.placers.PointedBallFoliagePlacer;
import net.id.paradiselost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradiselost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ParadiseLostTreeHell {

    public static FoliagePlacerType<WisteriaFoliagePlacer> WISTERIA_FOLIAGE;
    public static FoliagePlacerType<PointedBallFoliagePlacer> POINTED_BALL_FOLIAGE;
    public static TrunkPlacerType<WisteriaTrunkPlacer> WISTERIA_TRUNK;
    public static TrunkPlacerType<OvergrownTrunkPlacer> OVERGROWN_TRUNK;

    public static void init() {
        WISTERIA_FOLIAGE = registerFoliage("wisteria_foliage_placer", WisteriaFoliagePlacer.CODEC);
        POINTED_BALL_FOLIAGE = registerFoliage("pointed_ball_foliage_placer", PointedBallFoliagePlacer.CODEC);
        WISTERIA_TRUNK = registerTrunk("wisteria_trunk_placer", WisteriaTrunkPlacer.CODEC);
        OVERGROWN_TRUNK = registerTrunk("overgrown_trunk_placer", OvergrownTrunkPlacer.CODEC);
    }

    private static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(String name, MapCodec<P> codec) {
        return Registry.register(Registries.FOLIAGE_PLACER_TYPE, ParadiseLost.locate(name), new FoliagePlacerType(codec));
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(String name, MapCodec<P> codec) {
        return Registry.register(Registries.TRUNK_PLACER_TYPE, ParadiseLost.locate(name), new TrunkPlacerType(codec));
    }
}
