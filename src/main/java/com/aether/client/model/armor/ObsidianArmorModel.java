package com.aether.client.model.armor;

import com.aether.Aether;
import com.aether.items.armor.AetherArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ObsidianArmorModel extends AnimatedGeoModel<AetherArmor> {

    @Override
    public Identifier getModelLocation(AetherArmor object) {
        return Aether.locate("geo/obsidian_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(AetherArmor object) {
        return Aether.locate("textures/armor/obsidian_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(AetherArmor animatable) {
        return Aether.locate("animations/armor/obsidian_armor.animation");
    }
}