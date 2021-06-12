package com.aether.api.moa;

import net.minecraft.resources.ResourceLocation;

public class MoaProperties {

    private final int maxJumps;
    private final float moaSpeed;
    private final ResourceLocation location;
    private final ResourceLocation saddleLocation;

    public MoaProperties(int maxJumps, float moaSpeed, ResourceLocation location, ResourceLocation saddleLocation) {
        this.maxJumps = maxJumps;
        this.moaSpeed = moaSpeed;
        this.location = location;
        this.saddleLocation = saddleLocation;
    }

    public int getMaxJumps() {
        return this.maxJumps;
    }

    public float getMoaSpeed() {
        return this.moaSpeed;
    }

    public ResourceLocation getCustomTexture(boolean isSaddled) {
        return isSaddled ? this.saddleLocation : this.location;
    }

}