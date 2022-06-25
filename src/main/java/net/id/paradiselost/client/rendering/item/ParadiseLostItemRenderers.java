package net.id.paradiselost.client.rendering.item;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.items.ParadiseLostItems;

@Environment(EnvType.CLIENT)
public final class ParadiseLostItemRenderers {
    private ParadiseLostItemRenderers(){}
    
    public static void initClient(){
        TrinketRendererRegistry.registerRenderer(ParadiseLostItems.CLOUD_PARACHUTE, (TrinketRenderer) ParadiseLostItems.CLOUD_PARACHUTE);
        TrinketRendererRegistry.registerRenderer(ParadiseLostItems.GOLDEN_CLOUD_PARACHUTE, (TrinketRenderer) ParadiseLostItems.GOLDEN_CLOUD_PARACHUTE);
    }
}
