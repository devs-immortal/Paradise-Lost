package com.aether.mixin.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.ZaniteArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ZaniteArmorModel extends AnimatedGeoModel<ZaniteArmor> {

    @Override
    public Identifier getModelLocation(ZaniteArmor object) {
        return new Identifier(Aether.modId, "geo/zanite_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ZaniteArmor object) {
        return new Identifier(Aether.modId, "texures/armor/zanite_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ZaniteArmor animatable) {
        return new Identifier(Aether.modId, "animations/armor/zanite_armor.animation.json");
    }
}
