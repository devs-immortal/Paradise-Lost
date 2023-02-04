package net.id.paradiselost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class PhoenixArmorModel extends BipedEntityModel<LivingEntity> { //24: Left this in to repurpose it later because I liked it

    public ModelPart head;

    public PhoenixArmorModel(ModelPart root) {
        super(root);
        this.head = root.getChild(EntityModelPartNames.HEAD);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = getModelData(Dilation.NONE, 0);
        ModelPartData modelPartData = modelData.getRoot().getChild(EntityModelPartNames.HEAD);
        modelPartData.addChild("beak", ModelPartBuilder.create()
                        .uv(24, 0)
                        .cuboid(-2.0F, -5.125F, -6.125F, 4.0F, 1.0F, 2.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 32);
    }
}
