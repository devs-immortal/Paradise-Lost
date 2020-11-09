package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import com.aether.world.dimension.AetherDimension;
import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.HashSet;
import java.util.Set;

public class Aether implements ModInitializer, ClientModInitializer {

    public static final String modId = "the_aether";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Aether.modId, Aether.modId);
    public static final Logger modLogger = LogManager.getLogger(modId);
    public static final Set<Block> PORTAL_BLOCKS = new HashSet<>();

    static {
        PORTAL_BLOCKS.add(Blocks.GLOWSTONE);
    }

    public static Identifier locate(String location) {
        return new Identifier(modId, location);
    }

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        CrowdinTranslate.downloadTranslations("aether", modId);
        AetherDimension.setupDimension();
        //AetherAPIRegistry.register();
    }

    @Override
    public void onInitializeClient() {
        GeckoLib.initialize();
        AetherBlocks.clientInitialization();
        AetherItems.clientInitialization();
        //GeckolibRenderRegistry.init();
    }
}
