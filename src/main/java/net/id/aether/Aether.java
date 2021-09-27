package net.id.aether;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
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
import net.id.aether.world.AetherGameRules;
import net.id.aether.world.dimension.AetherDimension;
import net.id.aether.world.feature.AetherConfiguredFeatures;
import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.id.aether.world.gen.carver.AetherCarvers;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aether implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "the_aether";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static Identifier locate(String location) {
        return new Identifier(MOD_ID, location);
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
    }
}
