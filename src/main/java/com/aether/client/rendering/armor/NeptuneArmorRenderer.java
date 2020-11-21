package com.aether.client.rendering.armor;

import com.aether.client.model.armor.NeptuneArmorModel;
import com.aether.items.armor.NeptuneArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class NeptuneArmorRenderer extends GeoArmorRenderer<NeptuneArmor> {
    public NeptuneArmorRenderer() {
        super(new NeptuneArmorModel());
    }
}
