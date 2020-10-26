package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.dimension.AetherDimension;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Aether implements ModInitializer, ClientModInitializer {

    public static final String MODID = "the_aether";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Aether.MODID, Aether.MODID);

    @Override
    public void onInitialize() {
        AetherDimension.setupDimension();
        AetherBlocks.initialization();
    }
    @Override
    public void onInitializeClient() {
        AetherBlocks.clientInitialization();
    }
}
