package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import com.aether.world.dimension.AetherDimension;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class Aether implements ModInitializer, ClientModInitializer {

    public static final String MODID = "the_aether";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Aether.MODID, Aether.MODID);
    public static final Logger AETHER_LOG = LogManager.getLogger(MODID);
    public static final Set<Block> PORTAL_BLOCKS = new HashSet<>();

    static {
        PORTAL_BLOCKS.add(Blocks.GLOWSTONE);
    }

    @Override
    public void onInitialize() {
        AetherDimension.setupDimension();
    }

    @Override
    public void onInitializeClient() {
        AetherBlocks.clientInitialization();
        AetherItems.clientInitialization();
    }
}
