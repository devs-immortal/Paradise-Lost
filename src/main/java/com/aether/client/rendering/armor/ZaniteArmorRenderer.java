package com.aether.client.rendering.armor;

import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.ZaniteArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class ZaniteArmorRenderer extends GeoArmorRenderer<ZaniteArmor> {
    public ZaniteArmorRenderer() {
        super(new ZaniteArmorModel());
    }
}
