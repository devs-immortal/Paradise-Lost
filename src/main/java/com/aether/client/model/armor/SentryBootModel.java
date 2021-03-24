package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.AetherArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SentryBootModel extends AnimatedGeoModel<AetherArmor> {

    @Override
    public Identifier getModelLocation(AetherArmor object) {
        return Aether.locate("geo/sentry_boots/sentry_boots.geo.json");
    }

    @Override
    public Identifier getTextureLocation(AetherArmor object) {
        return Aether.locate("textures/models/armor/sentry_boots.png");
    }

    @Override
    public Identifier getAnimationFileLocation(AetherArmor animatable) {
        return Aether.locate("animations/sentry_boots/sentry_boots.animation");
    }
}