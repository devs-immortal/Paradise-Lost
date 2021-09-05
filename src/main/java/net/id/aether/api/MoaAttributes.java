package net.id.aether.api;

import net.id.aether.component.MoaGenes;

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
