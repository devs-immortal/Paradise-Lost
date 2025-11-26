package net.id.paradiselost;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.blocks.ParadiseLostBlockSets;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.ParadiseLostWoodTypes;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.armor.ParadiseLostModels;
import net.id.paradiselost.client.rendering.block.ParadiseLostBlockEntityRenderers;
import net.id.paradiselost.client.rendering.entity.ParadiseLostEntityRenderers;
import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.client.rendering.util.ParadiseLostColorProviders;
import net.id.paradiselost.commands.ParadiseLostCommands;
import net.id.paradiselost.compat.EveryCompCompat;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaRaces;
import net.id.paradiselost.items.ParadiseLostItemGroups;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.armor.XpCircletItem;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.id.paradiselost.recipe.ParadiseLostRecipeTypes;
import net.id.paradiselost.screen.ParadiseLostScreens;
import net.id.paradiselost.util.ParadiseLostAliasFix;
import net.id.paradiselost.util.ParadiseLostCriteria;
import net.id.paradiselost.util.ParadiseLostDamageTypes;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.paradiselost.world.ParadiseLostGameRules;
import net.id.paradiselost.world.ParadiseLostMapDecorationTypes;
import net.id.paradiselost.world.dimension.ParadiseLostBiomes;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.id.paradiselost.world.feature.ParadiseLostFeatures;
import net.id.paradiselost.world.gen.carver.ParadiseLostCarvers;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Docs for Paradise Lost are sometimes written long after the code itself has been written, and oftentimes by different
 * authors than the author of the code itself. If you have any questions or concerns regarding documentation, please
 * contact either the doc author or the code author, or both, via our <a
 * href="https://discord.com/invite/TvuNtNYEvr">Discord</a>.
 * <br><br>
 * The doc author can usually be found at the end of the first doc of the class, next to a tilde.
 * <br><br>
 * The person(s) next to the @author tag are, as expected, the people who have written the code.
 * <br><br>
 * ~ Jack
 * <br><br>
 * A list of developers can be found in {@code resources/fabric.mod.json}.
 */
public class ParadiseLost implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "paradise_lost";
    public static final Logger LOG = LogUtils.getLogger();

    /**
     * Creates a new {@link Identifier} based on the passed location.
     * <p>
     * If the location contains a collin `:` it will be split and handled like normal, otherwise it will use the default
     * namespace contained in {@link #MOD_ID} instead of the default "minecraft" namespace.
     *
     * @param location The location to use
     * @return The new {@link Identifier} instance
     */
    public static Identifier locate(String location) {
        if (location.contains(":")) {
            return Identifier.of(location);
        } else {
            return Identifier.of(MOD_ID, location);
        }
    }

    @Override
    public void onInitialize() {
        ParadiseLostDamageTypes.init();
        ParadiseLostCarvers.init();
        ParadiseLostFeatures.init();
        ParadiseLostBiomes.init();
        ParadiseLostDimension.init();
        ParadiseLostBlockSets.init();
        ParadiseLostWoodTypes.init();
        ParadiseLostBlocks.init();
        ParadiseLostEntityTypes.init();
        ParadiseLostItems.init();
        ParadiseLostItemGroups.init();
        ParadiseLostAliasFix.init();
        ParadiseLostBlockEntityTypes.init();
        ParadiseLostMapDecorationTypes.init();
        ParadiseLostRecipeTypes.init();
        ParadiseLostCommands.init();
        ParadiseLostGameRules.init();
        ParadiseLostSoundEvents.init();
        MoaRaces.init();
        ParadiseLostScreens.init();
        ParadiseLostParticles.init();
        ParadiseLostDataComponentTypes.init();
        ParadiseLostDimension.initPortal();
        ParadiseLostCriteria.init();
        if (FabricLoader.getInstance().isModLoaded("everycomp")) {
            EveryCompCompat.init();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        initializeCrowdin();
        ParadiseLostModels.initClient();
        ParadiseLostModelLayers.initClient();
        ParadiseLostEntityRenderers.initClient();
        ParadiseLostColorProviders.initClient();
        ParadiseLostBlockEntityRenderers.initClient();
        ParadiseLostParticles.Client.init();
        ParadiseLostScreens.initClient();
        // temp until better method in 1.21.5 I think?
        ModelPredicateProviderRegistry.register(
                ParadiseLostItems.XP_CIRCLET,
                locate("charged"),
                (stack, world, entity, seed) -> stack.getItem() instanceof XpCircletItem && XpCircletItem.isCharged(stack) ? 1.0F : 0.0F
        );
    }

    @Environment(EnvType.CLIENT)
    private void initializeCrowdin() {
        // No code changes for when the mod isn't present. :-)
        if (FabricLoader.getInstance().isModLoaded("crowdin-translate")) {
            try {
                var CrowdinTranslate = Class.forName("de.guntram.mcmod.crowdintranslate.CrowdinTranslate");
                var lookup = MethodHandles.lookup();
                var downloadTranslations = lookup.findStatic(CrowdinTranslate, "downloadTranslations", MethodType.methodType(void.class, String.class));
                downloadTranslations.invokeExact(MOD_ID);
            } catch (Throwable e) {
                LOG.warn("Failed to setup Crowdin Translate", e);
            }
        }
    }

}
