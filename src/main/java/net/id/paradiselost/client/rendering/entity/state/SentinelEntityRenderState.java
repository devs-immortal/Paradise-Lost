package net.id.paradiselost.client.rendering.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(EnvType.CLIENT)
public class SentinelEntityRenderState extends BipedEntityRenderState {
    public boolean enlightened;
}
