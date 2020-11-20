package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.SentryBoots;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SentryBootModel extends AnimatedGeoModel<SentryBoots> {

    @Override
    public Identifier getModelLocation(SentryBoots object) {
        return Aether.locate("geo/sentry_boots.geo.json");
    }

    @Override
    public Identifier getTextureLocation(SentryBoots object) {
        return Aether.locate("textures/armor/sentry_boots.png");
    }

    @Override
    public Identifier getAnimationFileLocation(SentryBoots animatable) {
        return Aether.locate("animations/armor/sentry_boots.animation");
    }
}
