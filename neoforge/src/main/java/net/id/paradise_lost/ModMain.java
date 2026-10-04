package net.id.paradise_lost;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.id.paradise_lost.networking.AttachmentSyncS2CPacket;
import net.id.paradise_lost.data.ParadiseLostDataEntries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.commands.ParadiseLostCommands;
import net.id.paradise_lost.entity.ModEntities;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.screen.NeoForgeScreens;
import net.id.paradise_lost.services.NeoForgeNetworkHelper;
import net.id.paradise_lost.services.NeoForgePlatformHelper;
import net.id.paradise_lost.util.ParadiseLostAliasFix;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

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
        NeoForge.EVENT_BUS.addListener(this::stripLogs);
        NeoForge.EVENT_BUS.addListener(PlayerEvent.StartTracking.class, event -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                AttachmentSyncS2CPacket.sendAll(event.getTarget(), player);
            }
        });
        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> syncOwnAttachments(event.getEntity()));
        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerRespawnEvent.class, event -> syncOwnAttachments(event.getEntity()));
        NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerChangedDimensionEvent.class, event -> syncOwnAttachments(event.getEntity()));

        modBus.addListener(RegisterEvent.class, event -> CommonDataAttachments.init());
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ParadiseLostCommands.registerAll(event.getDispatcher());
    }

    // NeoForge 21.3 has no strippables data map, so stripping goes through the tool modification event.
    private void stripLogs(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.AXE_STRIP) {
            return;
        }
        BlockState state = event.getState();
        Block stripped = ParadiseLostDataEntries.strippables().get(state.getBlock());
        if (stripped != null) {
            event.setFinalState(stripped.withPropertiesOf(state));
        }
    }

    private static void syncOwnAttachments(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            AttachmentSyncS2CPacket.sendAll(serverPlayer, serverPlayer);
        }
    }

    private void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ModConstants.MODID).versioned("1").optional();
        NeoForgeNetworkHelper.setRegistrar(registrar);
        PacketHandler.registerPackets();
        registrar.playToClient(AttachmentSyncS2CPacket.TYPE, AttachmentSyncS2CPacket.CODEC, (payload, context) -> payload.handleClient());
    }

    private void commonSetup(FMLCommonSetupEvent event) {

        event.enqueueWork(ParadiseLostAliasFix::init);
        event.enqueueWork(EntityRegistry::registerSpawnPlacements);
    }
}
