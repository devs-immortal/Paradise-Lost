package com.aether.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class PhoenixArmorModel extends HumanoidModel<LivingEntity> {
    public ModelPart beak;


    public PhoenixArmorModel(ModelPart root) {
        super(root);
        //this.head.setTextureOffset(24, 0).addCuboid(-2.0F, -5.125F, -7.125F, 4.0F, 1.0F, 2.0F, 0.5F, 0.125F, 0.125F);
    }


}