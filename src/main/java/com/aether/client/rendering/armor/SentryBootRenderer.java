package com.aether.client.rendering.armor;

import com.aether.client.model.armor.SentryBootModel;
import com.aether.items.armor.SentryBoots;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

public class SentryBootRenderer extends GeoArmorRenderer<SentryBoots> {
    public SentryBootRenderer() {
        super(new SentryBootModel());
    }
}
