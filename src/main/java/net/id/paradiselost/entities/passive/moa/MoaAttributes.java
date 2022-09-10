package net.id.paradiselost.entities.passive.moa;

import net.id.paradiselost.component.MoaGenes;

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

    public String getRatingTierTranslationKey(MoaEntity moa) {
        if (min > max) {
            return getRatingTierInverse(moa);
        }

        float attribute = getAttribute(moa);

        if (attribute <= min) {
            return getRatingTierTranslationKey(1);
        }
        if (attribute >= max) {
            return getRatingTierTranslationKey(7);
        }

        double out = (attribute - min) / gradeInterval;

        return getRatingTierTranslationKey((int) (out + 2));
    }

    private String getRatingTierTranslationKey(int tier) {
        return "moa.attribute.tier." + tier;
    }

    //used when smaller is better
    public String getRatingTierInverse(MoaEntity moa) {
        float attribute = getAttribute(moa);

        if (attribute <= min) {
            return getRatingTierTranslationKey(1);
        }
        if (attribute >= max) {
            return getRatingTierTranslationKey(7);
        }

        double out = (attribute - min) / gradeInterval;

        return getRatingTierTranslationKey((int) (out + 1));
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

    public String getTranslationKey(){
        return "moa.attribute." + this.name().toLowerCase();
    }
}
