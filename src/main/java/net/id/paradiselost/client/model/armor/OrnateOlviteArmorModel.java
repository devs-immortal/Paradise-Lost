package net.id.paradiselost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(EnvType.CLIENT)
public class OrnateOlviteArmorModel extends BipedEntityModel<BipedEntityRenderState> {

    public OrnateOlviteArmorModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        var modelData = getModelData(new Dilation(1F), 0F);
        var headModelData = modelData.getRoot().getChild(EntityModelPartNames.HEAD);
        headModelData.addChild(
                EntityModelPartNames.HAT,
                ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.5F)),
                ModelTransform.NONE
        );
//        headModelData.addChild(
//                "fan",
//                ModelPartBuilder.create().uv(0, 16).cuboid(-2F, -3F, -4F, 10.0F, 6.0F, 2.0F, new Dilation(0.75F, 0F, 0F)),
//                ModelTransform.of(-3.0F, -7.0F, 8.0F, -0.6545F, 0.0F, 0.0F)
//        );
        headModelData.addChild(
                "plume",
                ModelPartBuilder.create().uv(32, 16).cuboid(-1.0F, -12.5F, -3.0F, 2.0F, 6.0F, 9.0F, new Dilation(0.25F)),
                ModelTransform.NONE
        ); // z is forward-/back+   y is up-/down+    x is left-/right+

        return TexturedModelData.of(modelData, 64, 32);
    }
}
