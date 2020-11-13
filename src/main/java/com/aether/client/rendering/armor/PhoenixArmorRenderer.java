package com.aether.client.rendering.armor;

import com.aether.client.model.armor.PhoenixArmorModel;
import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.PhoenixArmor;
import com.aether.items.armor.ZaniteArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class PhoenixArmorRenderer extends GeoArmorRenderer<PhoenixArmor> {
    public PhoenixArmorRenderer() {
        super(new PhoenixArmorModel());
    }
}
