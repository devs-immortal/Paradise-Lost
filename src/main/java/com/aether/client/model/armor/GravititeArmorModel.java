package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.GravititeArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GravititeArmorModel extends AnimatedGeoModel<GravititeArmor> {

    @Override
    public Identifier getModelLocation(GravititeArmor object) {
        return new Identifier(Aether.MOD_ID, "geo/gravitite_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GravititeArmor object) {
        return new Identifier(Aether.MOD_ID, "textures/armor/gravitite_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(GravititeArmor animatable) {
        return new Identifier(Aether.MOD_ID, "animations/armor/gravitite_armor.animation");
    }
}
