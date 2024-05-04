package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;

@Environment(EnvType.CLIENT)
public class EnvoyEntityModel<T extends EnvoyEntity> extends SkeletonEntityModel<T> {
    public EnvoyEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}
