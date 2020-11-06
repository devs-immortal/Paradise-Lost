package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.dimension.AetherDimension;
import com.aether.items.AetherItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
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

    @Override
    public void onInitialize() {
        AetherDimension.setupDimension();
        ServerTickEvents.END_SERVER_TICK.register(AetherEvents::ServerTickEnd);
    }

    @Override
    public void onInitializeClient() {
        AetherBlocks.clientInitialization();
        AetherItems.clientInitialization();
    }
}
