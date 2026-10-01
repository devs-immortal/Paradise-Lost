package net.id.paradise_lost.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;

public class OrnateOlviteArmorModel extends HumanoidModel<LivingEntity> {

    public OrnateOlviteArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        var modelData = createMesh(new CubeDeformation(1F), 0F);
        var headModelData = modelData.getRoot().getChild(PartNames.HEAD);
        headModelData.addOrReplaceChild(
                PartNames.HAT,
                CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F)),
                PartPose.ZERO
        );

        headModelData.addOrReplaceChild(
                "plume",
                CubeListBuilder.create().texOffs(32, 16).addBox(-1.0F, -12.5F, -3.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.25F)),
                PartPose.ZERO
        );

        return LayerDefinition.create(modelData, 64, 32);
    }
}
