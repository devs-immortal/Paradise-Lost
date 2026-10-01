package net.id.paradise_lost.client.model.entity;

import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;

public class EnvoyEntityModel<T extends EnvoyEntity> extends SkeletonModel<T> {
    public EnvoyEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}
