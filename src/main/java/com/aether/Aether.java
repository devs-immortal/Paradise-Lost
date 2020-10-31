package com.aether;

import com.aether.blocks.AetherBlocks;
import com.aether.dimension.AetherDimension;
import com.aether.items.AetherItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.Identifier;

public class Aether implements ModInitializer, ClientModInitializer {

    public static final String MODID = "the_aether";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Aether.MODID, Aether.MODID);

    @Override
    public void onInitialize() {
        AetherDimension.setupDimension();
        ServerTickEvents.END_SERVER_TICK.register(AetherEvents::ServerTickEnd);
        UseBlockCallback.EVENT.register(AetherEvents::UseBlock);
    }

    @Override
    public void onInitializeClient() {
        AetherBlocks.clientInitialization();
        AetherItems.clientInitialization();
    }
}
