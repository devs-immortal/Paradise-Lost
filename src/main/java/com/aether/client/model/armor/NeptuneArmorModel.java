package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.NeptuneArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NeptuneArmorModel extends AnimatedGeoModel<NeptuneArmor> {

    @Override
    public Identifier getModelLocation(NeptuneArmor object) {
        return new Identifier(Aether.MOD_ID, "geo/neptune_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(NeptuneArmor object) {
        return new Identifier(Aether.MOD_ID, "textures/armor/neptune_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(NeptuneArmor animatable) {
        return new Identifier(Aether.MOD_ID, "animations/armor/neptune_armor.animation");
    }
}
