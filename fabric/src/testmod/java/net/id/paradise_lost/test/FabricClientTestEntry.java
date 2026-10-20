package net.id.paradise_lost.test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.id.paradise_lost.test.client.ClientTestRunner;

public class FabricClientTestEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTestRunner.init(ModResourcePackUtil::createDefaultDataConfiguration);
    }
}
