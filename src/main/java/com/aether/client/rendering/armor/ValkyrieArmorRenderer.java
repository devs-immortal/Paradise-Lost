package com.aether.client.rendering.armor;

import com.aether.client.model.armor.ValkyrieArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class ValkyrieArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public ValkyrieArmorRenderer() {
        super(new ValkyrieArmorModel());
    }
}
