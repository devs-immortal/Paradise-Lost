package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.entity.state.EnvoyEntityRenderState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;

@Environment(EnvType.CLIENT)
public class EnvoyEntityModel extends SkeletonEntityModel<EnvoyEntityRenderState> {
    public EnvoyEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}
