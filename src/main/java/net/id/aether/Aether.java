package net.id.aether;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.client.rendering.util.AetherTextures;
import net.id.aether.client.model.armor.AetherArmorModels;
import net.id.aether.client.model.AetherModelPredicates;
import net.id.aether.client.rendering.util.AetherColorProviders;
import net.id.aether.client.rendering.entity.AetherEntityRenderers;
import net.id.aether.client.model.AetherModelLayers;
import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.commands.AetherCommands;
import net.id.aether.effect.condition.Conditions;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.passive.moa.MoaRaces;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.AetherItems;
import net.id.aether.loot.AetherLootNumberProviderTypes;
import net.id.aether.registry.AetherRegistries;
import net.id.aether.util.AetherSoundEvents;
import net.id.aether.world.AetherGameRules;
import net.id.aether.world.dimension.AetherDimension;
import net.id.aether.world.feature.AetherConfiguredFeatures;
import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.id.aether.world.gen.carver.AetherCarvers;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Docs for The Aether Reborn are sometimes written long after
 * the code itself has been written, and oftentimes by different
 * authors than the author of the code itself.
 * If you have any questions or concerns regarding documentation,
 * please contact either the doc author or the code author, or
 * both, via our <a, href="https://discord.gg/eRsJ6F3Wng">Discord</a>.
 * <br><br>
 * The doc author can usually be found at the end of the first doc
 * of the class, next to a tilde.
 * <br><br>
 * The person(s) next to the @author tag are, as expected, the people
 * who have written the code.
 * <br><br>
 * ~ Jack
 * <br><br>
 * A list of developers can be found in {@code resources/fabric.mod.json}.
 */
public class Aether implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "the_aether";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static Identifier locate(String location) {
        if(location.contains(":")){
            return new Identifier(location);
        }else{
            return new Identifier(MOD_ID, location);
        }
    }

    @Override
    public void onInitialize() {
        AetherRegistries.init();
        AetherCarvers.init();
        AetherTreeHell.init();
        AetherFeatures.init();
        AetherConfiguredFeatures.init();
        AetherDimension.init();
        AetherBlocks.init();
        AetherFluids.init();
        AetherEntityTypes.init();
        AetherItems.init();
        AetherBlockEntityTypes.init();
        AetherCommands.init();
        AetherGameRules.init();
        AetherLootNumberProviderTypes.init();
        AetherSoundEvents.init();
        Conditions.init();
        MoaRaces.init();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations("aether", MOD_ID);
        AetherModelPredicates.initClient();
        AetherArmorModels.initClient();
        AetherModelLayers.initClient();
        AetherEntityRenderers.initClient();
        AetherColorProviders.initClient();
        AetherBlockEntityTypes.initClient();
        AetherParticles.initClient();
        AetherTextures.init();
    }
}
