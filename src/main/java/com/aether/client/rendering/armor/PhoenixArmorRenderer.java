package com.aether.client.rendering.armor;

import com.aether.client.model.armor.PhoenixArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class PhoenixArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public PhoenixArmorRenderer() {
        super(new PhoenixArmorModel());
    }
}
