package net.id.paradiselost.client.rendering.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;

@Environment(EnvType.CLIENT)
public class EnvoyEntityRenderState extends SkeletonEntityRenderState {
    public boolean enlightened;
}
