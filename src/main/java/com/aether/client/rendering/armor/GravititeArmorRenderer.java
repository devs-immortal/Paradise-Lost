package com.aether.client.rendering.armor;

import com.aether.client.model.armor.GravititeArmorModel;
import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.GravititeArmor;
import com.aether.items.armor.ZaniteArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class GravititeArmorRenderer extends GeoArmorRenderer<GravititeArmor> {
    public GravititeArmorRenderer() {
        super(new GravititeArmorModel());
    }
}
