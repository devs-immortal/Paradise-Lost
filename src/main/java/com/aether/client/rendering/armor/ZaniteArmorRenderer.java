package com.aether.client.rendering.armor;

import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class ZaniteArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public ZaniteArmorRenderer() {
        super(new ZaniteArmorModel());
    }
}
