package com.aether.api.moa;

import net.minecraft.util.Identifier;

public class MoaProperties {

    private int maxJumps;

    private float moaSpeed;

    private Identifier location;

    private Identifier saddleLocation;

    public MoaProperties(int maxJumps, float moaSpeed, Identifier location, Identifier saddleLocation) {
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

    public Identifier getCustomTexture(boolean isSaddled) {
        return isSaddled ? this.saddleLocation : this.location;
    }

}