package net.id.paradiselost.client.rendering.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class PopomEntityRenderState extends LivingEntityRenderState {
    public int furSize;
    public float headAngle;
}
