package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.AetherArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NeptuneArmorModel extends AnimatedGeoModel<AetherArmor> {

    @Override
    public Identifier getModelLocation(AetherArmor object) {
        return Aether.locate("geo/neptune_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(AetherArmor object) {
        return Aether.locate("textures/armor/neptune_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(AetherArmor animatable) {
        return Aether.locate("animations/armor/neptune_armor.animation");
    }
}