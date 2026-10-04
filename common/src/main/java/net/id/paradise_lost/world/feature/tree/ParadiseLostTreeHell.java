package net.id.paradise_lost.world.feature.tree;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.world.feature.tree.placers.PointedBallFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.OvergrownTrunkPlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaFoliagePlacer;
import net.id.paradise_lost.world.feature.tree.placers.WisteriaTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ParadiseLostTreeHell {
    private static final RegistrationProvider<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            RegistrationProvider.get(Registries.FOLIAGE_PLACER_TYPE, ModConstants.MODID);
    private static final RegistrationProvider<TrunkPlacerType<?>> TRUNK_PLACERS =
            RegistrationProvider.get(Registries.TRUNK_PLACER_TYPE, ModConstants.MODID);

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
        FoliagePlacerType<P> type = new FoliagePlacerType<>(codec);
        FOLIAGE_PLACERS.register(name, () -> type);
        return type;
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(String name, MapCodec<P> codec) {
        TrunkPlacerType<P> type = new TrunkPlacerType<>(codec);
        TRUNK_PLACERS.register(name, () -> type);
        return type;
    }
}
