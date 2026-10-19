package net.id.paradise_lost.gametest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;

public class FabricClientTestEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTestRunner.init(ModResourcePackUtil::createDefaultDataConfiguration);
    }
}
