package net.id.paradise_lost.client;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.services.NeoForgeClientHelper;
import net.id.paradise_lost.client.model.CalciteFlowerPotModel;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.rendering.armor.OrnateOlviteArmorClientExtensions;
import net.id.paradise_lost.client.rendering.block.PalaceDoorBlockEntityRendererNeoForge;
import net.id.paradise_lost.client.rendering.particle.CherineFlameParticle;
import net.id.paradise_lost.client.rendering.particle.LevitaBloopParticle;
import net.id.paradise_lost.client.rendering.particle.LevitationTotemParticle;
import net.id.paradise_lost.client.rendering.particle.LitCloudParticle;
import net.id.paradise_lost.client.rendering.particle.MotherAurelLeafParticle;
import net.id.paradise_lost.client.rendering.util.ParadiseLostColorProviders;
import net.id.paradise_lost.client.screen.MoaScreen;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.screen.ParadiseLostScreens;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ModConstants.MODID, dist = Dist.CLIENT)
public class NeoForgeClient {
    public NeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, NeoForgeClient::registerRenderers);
        modBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, NeoForgeClient::registerLayers);
        modBus.addListener(RegisterParticleProvidersEvent.class, NeoForgeClient::registerParticles);
        modBus.addListener(RegisterMenuScreensEvent.class, NeoForgeClient::registerScreens);
        modBus.addListener(RegisterColorHandlersEvent.Block.class, NeoForgeClient::registerBlockColors);
        modBus.addListener(RegisterColorHandlersEvent.Item.class, NeoForgeClient::registerItemColors);
        modBus.addListener(RegisterClientExtensionsEvent.class, NeoForgeClient::registerClientExtensions);
        modBus.addListener(FMLClientSetupEvent.class, NeoForgeClient::onClientSetup);

        modBus.addListener(ModelEvent.ModifyBakingResult.class, CalciteFlowerPotModel::onModifyBakingResult);

        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, NeoForgeClient::onSelectMusic);
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new OrnateOlviteArmorClientExtensions(), ItemRegistry.OLVITE_HELMET_ORNATE.get());
    }

    private static void onSelectMusic(SelectMusicEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            event.setMusic(ParadiseLostSoundEvents.PARADISE_MUSIC_SOUND);
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegistration.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer,
                PalaceDoorBlockEntityRendererNeoForge::new);
    }

    private static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ParadiseLostModelLayers.ENTRIES.forEach((layer, definition) ->
                event.registerLayerDefinition(layer, () -> definition));
    }

    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParadiseLostParticleTypes.CHERINE_FLAME, CherineFlameParticle.DefaultFactory::new);
        event.registerSpriteSet(ParadiseLostParticleTypes.MOTHER_AUREL_LEAF, MotherAurelLeafParticle.DefaultFactory::new);
        event.registerSpriteSet(ParadiseLostParticleTypes.LEVITA_BLOOP, LevitaBloopParticle.DefaultFactory::new);
        event.registerSpriteSet(ParadiseLostParticleTypes.LEVITATION_TOTEM, LevitationTotemParticle.DefaultFactory::new);
        event.registerSpriteSet(ParadiseLostParticleTypes.LIT_CLOUD, LitCloudParticle.DefaultFactory::new);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ParadiseLostScreens.MOA.get(), MoaScreen::new);
    }

    private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        ParadiseLostColorProviders.registerBlocks((color, blocks) -> event.register(color, blocks));
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ParadiseLostColorProviders.registerItems((color, blocks) -> {
            Item[] items = new Item[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                items[i] = blocks[i].asItem();
            }
            event.register(color, items);
        });
    }

    private static void onClientSetup(FMLClientSetupEvent event) {

        ClientRegistration.registerItemProperties();
        event.enqueueWork(NeoForgeClientHelper::applyDeferredRenderLayers);
    }
}
