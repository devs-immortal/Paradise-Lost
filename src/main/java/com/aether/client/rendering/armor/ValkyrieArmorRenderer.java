package com.aether.client.rendering.armor;

import com.aether.client.model.armor.ValkyrieArmorModel;
import com.aether.client.model.armor.ZaniteArmorModel;
import com.aether.items.armor.ValkyrieArmor;
import com.aether.items.armor.ZaniteArmor;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class ValkyrieArmorRenderer extends GeoArmorRenderer<ValkyrieArmor> {
    public ValkyrieArmorRenderer() {
        super(new ValkyrieArmorModel());
    }
}
