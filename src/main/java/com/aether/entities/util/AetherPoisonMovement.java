package com.aether.entities.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class AetherPoisonMovement {

    private final LivingEntity entity;
    public int ticks = 0;
    public double rotD, motD;

    public AetherPoisonMovement(LivingEntity entity) {
        this.entity = entity;
    }

    public void onUpdate() {
        int timeUntilHit = this.ticks % 50;

        if (!this.entity.isAlive()) {
            this.ticks = 0;
        } else {
            if (this.ticks < 0) {
                this.ticks++;
                return;
            }

            if (this.ticks == 0) return;
            this.distract();

            if (timeUntilHit == 0) this.entity.hurt(DamageSource.MAGIC, 1.0F);
            this.ticks--;
        }
    }

    public boolean inflictPoison(int ticks) {
        if (this.ticks != 0) return false;
        this.ticks = ticks;
        return true;
    }

    public boolean applyCure(int ticks) {
        if (this.ticks != 0) return false;
        this.ticks = ticks;
        return true;
    }

    public void distract() {
        double gaussian = this.entity.level.random.nextGaussian();
        double newMotD = 0.1D * gaussian;
        double newRotD = (Math.PI / 4D) * gaussian;

        this.motD = 0.2D * newMotD + (0.8D) * this.motD;
        this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(this.motD, 0.0D, this.motD));
        this.rotD = 0.125D * newRotD + (1.0D - 0.125D) * this.rotD;

        this.entity.setYRot((float) ((double) this.entity.getYRot() + rotD));
        this.entity.setXRot((float) ((double) this.entity.getXRot() + rotD));
    }
}