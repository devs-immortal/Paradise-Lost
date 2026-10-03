package net.id.paradise_lost.world.feature.placement;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public final class ParadiseLostPlacementModifiers {
    private static final RegistrationProvider<PlacementModifierType<?>> TYPES =
            RegistrationProvider.get(Registries.PLACEMENT_MODIFIER_TYPE, ModConstants.MODID);

    public static final PlacementModifierType<RiverBankPlacement> RIVER_BANK =
            register("river_bank", () -> RiverBankPlacement.CODEC);

    private ParadiseLostPlacementModifiers() {
    }

    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, PlacementModifierType<P> type) {
        TYPES.register(id, () -> type);
        return type;
    }

    /** Call from ParadiseLostFeatures.init() so the type is registered before data loads. */
    public static void init() {
    }
}
