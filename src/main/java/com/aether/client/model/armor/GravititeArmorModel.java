package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.GravititeArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GravititeArmorModel extends AnimatedGeoModel<GravititeArmor> {

    @Override
    public Identifier getModelLocation(GravititeArmor object) {
        return Aether.locate("geo/gravitite_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GravititeArmor object) {
        return Aether.locate("textures/armor/gravitite_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(GravititeArmor animatable) {
        return Aether.locate("animations/armor/gravitite_armor.animation");
    }
}