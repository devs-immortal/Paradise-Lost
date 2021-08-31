package net.id.aether.api.moa;

import net.minecraft.util.Identifier;

public class MoaProperties {

    private final int maxJumps;
    private final float moaSpeed;
    private final Identifier location;
    private final Identifier saddleLocation;

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