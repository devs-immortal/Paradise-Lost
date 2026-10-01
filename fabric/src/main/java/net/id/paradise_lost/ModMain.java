package net.id.paradise_lost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.screen.FabricScreens;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.id.paradise_lost.commands.ParadiseLostCommands;
import net.id.paradise_lost.entity.ModEntities;
import net.id.paradise_lost.util.ParadiseLostAliasFix;
import net.id.paradise_lost.registry.EntityRegistry;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Services.PLATFORM.getPlatformName();
        ModCommon.init();

        ParadiseLostAliasFix.init();
        CommonDataAttachments.init();
        EntityRegistry.registerSpawnPlacements();
        FabricScreens.register();
        ModEntities.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

        PacketHandler.registerPackets();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                ParadiseLostCommands.registerAll(dispatcher));
    }
}
