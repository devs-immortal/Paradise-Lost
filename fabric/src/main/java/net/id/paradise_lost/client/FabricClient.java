package net.id.paradise_lost.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.paradise_lost.client.model.armor.ParadiseLostModels;
import net.id.paradise_lost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradise_lost.client.rendering.util.FabricColorProviders;
import net.id.paradise_lost.screen.FabricScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.id.paradise_lost.client.rendering.block.PalaceDoorBlockEntityRenderer;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricScreens.registerClient();
        ClientRegistration.registerItemProperties();
        ClientRegistration.registerRenderers(EntityRendererRegistry::register, BlockEntityRenderers::register,
                PalaceDoorBlockEntityRenderer::new);
        ParadiseLostModels.init();
        ParadiseLostParticles.initClient();
        FabricColorProviders.initClient();
    }
}
