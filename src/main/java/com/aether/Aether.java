package com.aether;

import com.aether.api.MoaAPI;
import com.aether.blocks.AetherBlocks;
import com.aether.blocks.blockentity.AetherBlockEntityTypes;
import com.aether.client.model.AetherArmorModels;
import com.aether.client.model.AetherModelPredicates;
import com.aether.client.rendering.entity.AetherEntityRenderers;
import com.aether.client.rendering.entity.layer.AetherModelLayers;
import com.aether.client.rendering.particle.AetherParticles;
import com.aether.commands.AetherCommands;
import com.aether.entities.AetherEntityTypes;
import com.aether.fluids.AetherFluids;
import com.aether.items.AetherItems;
import com.aether.loot.AetherLootNumberProviderTypes;
import com.aether.registry.TrinketSlotRegistry;
import com.aether.world.AetherGameRules;
import com.aether.world.dimension.AetherDimension;
import com.aether.world.feature.AetherConfiguredFeatures;
import com.aether.world.feature.AetherFeatures;
import com.aether.world.feature.tree.AetherTreeHell;
import com.aether.world.gen.AetherCarvers;
import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
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
        TrinketSlotRegistry.init();
        AetherCarvers.init();
        AetherTreeHell.init();
        AetherFeatures.registerFeatures();
        AetherConfiguredFeatures.registerFeatures();
        AetherDimension.setupDimension();
        AetherBlocks.init();
        AetherFluids.init();
        AetherEntityTypes.init();
        AetherItems.init();
        AetherBlockEntityTypes.init();
        AetherCommands.init();
        AetherGameRules.init();
        AetherLootNumberProviderTypes.init();
        MoaAPI.init();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations("aether", MOD_ID);
        AetherModelPredicates.init();
        AetherArmorModels.registerArmorModels();
        AetherModelLayers.initClient();
        AetherEntityRenderers.initClient();
        AetherBlockEntityTypes.initClient();
        AetherFluids.initClient();
        AetherParticles.initClient();
    }
}
