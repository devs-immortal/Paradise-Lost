package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.ValkyrieArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ValkyrieArmorModel extends AnimatedGeoModel<ValkyrieArmor> {

    @Override
    public Identifier getModelLocation(ValkyrieArmor object) {
        return new Identifier(Aether.MOD_ID, "geo/valkyrie_armor/valkyrie_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ValkyrieArmor object) {
        return new Identifier(Aether.MOD_ID, "textures/models/armor/valkyrie_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ValkyrieArmor animatable) {
        return new Identifier(Aether.MOD_ID, "animations/valkyrie_armor/wings_idle.animation");
    }
}
