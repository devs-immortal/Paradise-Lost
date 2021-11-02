package net.id.aether.entities.passive.moa;

import net.id.aether.component.MoaGenes;
import net.minecraft.util.math.MathHelper;

public enum MoaAttributes {
    GROUND_SPEED(0.24F, 1F, 0.152F),
    GLIDING_SPEED(0.055F, 0.25F, 0.039F),
    GLIDING_DECAY(0.9F, 0.5F, 0.08F),
    JUMPING_STRENGTH(0.25F, 0.15F, 0.02F),
    DROP_MULTIPLIER(1, 6, 1),
    MAX_HEALTH(10, 40, 6);

    public final float min, max, gradeInterval;

    MoaAttributes(float min, float max, float gradeInterval) {
        this.min = min;
        this.max = max;
        this.gradeInterval = gradeInterval;
    }

    private static final String[] tiers = new String[]{"F", "D", "C", "B", "A", "S", "S+"};

    public String getRatingTier(MoaEntity moa) {
        if (min > max) return getRatingTierInverse(moa);
        float attribute = getAttribute(moa);
        if (attribute <= min) return tiers[0];
        if (attribute >= max) return tiers[tiers.length - 1];
        double lerp = (MathHelper.getLerpProgress(attribute, min, max) * (tiers.length - 2));
        return tiers[(int) lerp + 1];
    }

    //used when smaller is better
    public String getRatingTierInverse(MoaEntity moa) {
        if (min < max) return getRatingTier(moa);
        float attribute = getAttribute(moa);
        if (attribute >= min) return tiers[0];
        if (attribute <= max) return tiers[tiers.length - 1];
        double lerp = (MathHelper.getLerpProgress(attribute, min, max) * (tiers.length - 2));
        return tiers[(int) lerp + 1];
    }

    public float getAttribute(MoaEntity moa) {
        return moa.getGenes().getAttribute(this);
    }

    public float fromBreeding(MoaGenes parentA, MoaGenes parentB, boolean increase) {
        float stat = (parentA.getAttribute(this) / 2) + (parentB.getAttribute(this) / 2);
        if (parentA.getAffinity() == this) {
            stat += gradeInterval / 3;
        }
        if (parentB.getAffinity() == this) {
            stat += gradeInterval / 3;
        }
        if (increase) {
            stat += gradeInterval / 4;
        }
        return stat;
    }
}
