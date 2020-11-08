package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import com.aether.registry.AetherAPIRegistry;
import com.aether.world.dimension.AetherDimension;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aether implements ModInitializer, ClientModInitializer {

    public static final String modId = "the_aether";
    public static final Logger modLogger = LogManager.getFormatterLogger(modId);

    public static Identifier locate(String location) {
        return new Identifier(modId, location);
    }

    @Override
    public void onInitialize() {
        CrowdinTranslate.downloadTranslations("aether", modId);
        AetherDimension.setupDimension();
        AetherAPIRegistry.register();
    }

    @Override
    public void onInitializeClient() {
        AetherBlocks.clientInitialization();
        AetherItems.clientInitialization();
    }
}
