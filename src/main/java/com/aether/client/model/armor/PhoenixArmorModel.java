package com.aether.client.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class PhoenixArmorModel extends BipedEntityModel<LivingEntity> {
    public ModelPart beak;


    public PhoenixArmorModel(ModelPart root) {
        super(root);
        //this.head.setTextureOffset(24, 0).addCuboid(-2.0F, -5.125F, -7.125F, 4.0F, 1.0F, 2.0F, 0.5F, 0.125F, 0.125F);
    }


}