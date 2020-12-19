package com.aether.client.rendering.armor;

import com.aether.client.model.armor.NeptuneArmorModel;
import com.aether.items.armor.AetherArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class NeptuneArmorRenderer extends GeoArmorRenderer<AetherArmor> {
    public NeptuneArmorRenderer() {
        super(new NeptuneArmorModel());
    }
}
