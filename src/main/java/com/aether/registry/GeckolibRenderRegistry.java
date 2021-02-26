package com.aether.registry;

import com.aether.client.rendering.armor.*;
import com.aether.items.armor.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class GeckolibRenderRegistry {
    public static void initClient() {
        GeoArmorRenderer.registerArmorRenderer(ZaniteArmor.class, new ZaniteArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(GravititeArmor.class, new GravititeArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(ValkyrieArmor.class, new ValkyrieArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(NeptuneArmor.class, new NeptuneArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(PhoenixArmor.class, new PhoenixArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(ObsidianArmor.class, new ObsidianArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(ZaniteArmor.class, new ZaniteArmorRenderer());
    }
}