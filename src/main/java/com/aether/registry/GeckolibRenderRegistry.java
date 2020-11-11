package com.aether.registry;

import com.aether.items.armor.ZaniteArmor;
import com.aether.mixin.client.renderer.armor.ZaniteArmorRenderer;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class GeckolibRenderRegistry {

    public static void init() {
        GeoArmorRenderer.registerArmorRenderer(ZaniteArmor.class, new ZaniteArmorRenderer());
    }
}
