package com.aether.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;

public class ScaledModelPart extends ModelPart {
    public float scaleX = 1, scaleY = 1, scaleZ = 1;

    public ScaledModelPart(Model model) {
        super(model);
    }

    public ScaledModelPart(Model model, int textureOffsetU, int textureOffsetV) {
        super(model, textureOffsetU, textureOffsetV);
    }

    public ScaledModelPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV) {
        super(textureWidth, textureHeight, textureOffsetU, textureOffsetV);
    }

    public void uniformScale(float scale) {
        scaleX = scale;
        scaleY = scale;
        scaleZ = scale;
    }
}
