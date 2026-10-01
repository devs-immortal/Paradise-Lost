package net.id.paradise_lost;

import net.id.paradise_lost.block.ParadiseLostBlockSets;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.ParadiseLostWoodTypes;
import net.id.paradise_lost.block.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradise_lost.commands.ParadiseLostCommands;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.MoaBreedingRegistry;
import net.id.paradise_lost.registry.MoaRaceRegistry;
import net.id.paradise_lost.registry.MoaSpawnRegistry;
import net.id.paradise_lost.entity.ModEntities;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.registry.CreativeTabRegistry;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.recipe.ParadiseLostRecipeTypes;
import net.id.paradise_lost.screen.ParadiseLostScreens;
import net.id.paradise_lost.util.ParadiseLostCriteria;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.ParadiseLostMapDecorationTypes;
import net.id.paradise_lost.world.dimension.ParadiseLostBiomes;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.id.paradise_lost.world.feature.ParadiseLostFeatures;
import net.id.paradise_lost.world.gen.carver.ParadiseLostCarvers;

public final class ModCommon {
    private ModCommon() {}

    public static void init() {
        Services.PLATFORM.getPlatformName();
        doRegistrations();
    }

    public static void doRegistrations() {
        ParadiseLostDamageTypes.init();
        ParadiseLostCarvers.init();
        ParadiseLostFeatures.init();
        ParadiseLostBiomes.init();
        ParadiseLostDimension.init();
        ParadiseLostBlockSets.init();
        ParadiseLostWoodTypes.init();
        BlockRegistry.init();
        EntityRegistry.init();
        ItemRegistry.init();
        ModEntities.init();
        CreativeTabRegistry.init();

        ParadiseLostBlockEntityTypes.init();
        ParadiseLostMapDecorationTypes.init();
        ParadiseLostRecipeTypes.init();
        ParadiseLostCommands.init();
        ParadiseLostGameRules.init();
        ParadiseLostSoundEvents.init();
        MoaRaceRegistry.init();
        MoaSpawnRegistry.init();
        MoaBreedingRegistry.init();
        ParadiseLostScreens.init();
        ParadiseLostDataComponentTypes.init();
        ParadiseLostParticleTypes.init();
        ParadiseLostDimension.initPortal();
        ParadiseLostCriteria.init();
        Services.COMPAT.initCompat();
    }
}
