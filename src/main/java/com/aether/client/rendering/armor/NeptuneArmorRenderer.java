package com.aether.client.rendering.armor;

import com.aether.client.model.armor.NeptuneArmorModel;
import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.NeptuneArmor;
import com.aether.items.armor.ZaniteArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class NeptuneArmorRenderer extends GeoArmorRenderer<NeptuneArmor> {
    public NeptuneArmorRenderer() {
        super(new NeptuneArmorModel());
    }
}
