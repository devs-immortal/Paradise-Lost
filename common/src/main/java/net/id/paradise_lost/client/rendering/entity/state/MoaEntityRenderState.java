package net.id.paradise_lost.client.rendering.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class MoaEntityRenderState extends LivingEntityRenderState {
    public boolean saddled;
    public boolean hasChest;
    public boolean inAir;
    public float legPitch;
    public float wingRoll;
    public float wingYaw;
    public float horizontalSpeed;
    public float moaScale = 1.0F;
    public ResourceLocation texture;
}
