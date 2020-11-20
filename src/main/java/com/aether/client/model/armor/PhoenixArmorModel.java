package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.PhoenixArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PhoenixArmorModel extends AnimatedGeoModel<PhoenixArmor> {

    @Override
    public Identifier getModelLocation(PhoenixArmor object) {
        return Aether.locate("geo/phoenix_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(PhoenixArmor object) {
        return Aether.locate("textures/armor/phoenix_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(PhoenixArmor animatable) {
        return Aether.locate("animations/armor/phoenix_armor.animation");
    }
}
