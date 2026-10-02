package net.id.paradiselost.client.rendering.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MoaEntityRenderState extends LivingEntityRenderState {
    public boolean saddled;
    public boolean hasChest;
    public boolean inAir;
    public float legPitch;
    public float wingRoll;
    public float wingYaw;
    public float horizontalSpeed;
    public boolean glowing;
    public float moaScale = 1.0F;
    public Identifier texture;
}
