package com.aether.client.rendering.armor;

import com.aether.client.model.armor.GravititeArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class GravititeArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public GravititeArmorRenderer() {
        super(new GravititeArmorModel());
    }
}
