package net.id.aether.client.model.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class ParachuteTrinketModel extends BipedEntityModel<LivingEntity> {

    public ParachuteTrinketModel(ModelPart root) {
        super(root);
        this.setVisible(false);
        this.body.visible = true;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0f);
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData chute = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0)
                .cuboid(-16f, -24f, -8f, 32f, 12f, 16f), ModelTransform.NONE);
        chute.addChild("cable", ModelPartBuilder.create().uv(0, 32)
                .cuboid(-8f, -12f, 0f, 16f, 12f, 0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 96, 48);
    }
}