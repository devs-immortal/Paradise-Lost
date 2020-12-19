package com.aether.client.rendering.armor;

import com.aether.client.model.armor.ObsidianArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class ObsidianArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public ObsidianArmorRenderer() {
        super(new ObsidianArmorModel());
    }
}
