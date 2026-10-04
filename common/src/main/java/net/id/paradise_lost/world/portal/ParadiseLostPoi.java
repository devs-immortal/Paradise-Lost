package net.id.paradise_lost.world.portal;

import com.google.common.collect.ImmutableSet;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public final class ParadiseLostPoi {
    public static final ResourceKey<PoiType> BLUE_PORTAL_KEY =
            ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ParadiseLost.locate("blue_portal"));

    private static final RegistrationProvider<PoiType> POI =
            RegistrationProvider.get(Registries.POINT_OF_INTEREST_TYPE, ModConstants.MODID);

    public static final RegistryObject<PoiType, PoiType> BLUE_PORTAL = POI.register(
            "blue_portal",
            () -> new PoiType(getBlockStates(BlockRegistry.BLUE_PORTAL.get()), 0, 1)
    );

    private ParadiseLostPoi() {}

    public static void init() {

    }

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
