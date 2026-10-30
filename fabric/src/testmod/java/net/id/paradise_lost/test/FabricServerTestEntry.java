package net.id.paradise_lost.test;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.id.paradise_lost.test.server.ServerTestRunner;

public class FabricServerTestEntry implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerTestRunner.init();
    }
}
