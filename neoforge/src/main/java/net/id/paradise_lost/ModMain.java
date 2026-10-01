package net.id.paradise_lost;

import net.id.paradise_lost.entity.ModEntities;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.services.NeoForgeNetworkHelper;
import net.id.paradise_lost.services.NeoForgePlatformHelper;
import net.id.paradise_lost.commands.ParadiseLostCommands;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.screen.NeoForgeScreens;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.id.paradise_lost.util.ParadiseLostAliasFix;
import net.id.paradise_lost.registry.EntityRegistry;

@Mod(ModConstants.MODID)
public class ModMain {
    public ModMain(IEventBus modBus, ModContainer modContainer) {
        NeoForgePlatformHelper.bindModBus(modBus);
        Services.PLATFORM.getPlatformName();
        ModCommon.init();
        NeoForgeScreens.register();
        modBus.<EntityAttributeCreationEvent>addListener(event -> ModEntities.registerEntityAttributes(event::put));
        modBus.addListener(this::commonSetup);

        modBus.addListener(this::setupPackets);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        modBus.addListener(RegisterEvent.class, event -> CommonDataAttachments.init());
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ParadiseLostCommands.registerAll(event.getDispatcher());
    }

    private void setupPackets(RegisterPayloadHandlersEvent event) {
        NeoForgeNetworkHelper.setRegistrar(event.registrar(ModConstants.MODID).versioned("1").optional());
        PacketHandler.registerPackets();
    }

    private void commonSetup(FMLCommonSetupEvent event) {

        event.enqueueWork(ParadiseLostAliasFix::init);
        event.enqueueWork(EntityRegistry::registerSpawnPlacements);
    }
}
