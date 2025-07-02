package net.id.paradiselost.entities.passive.moa;

import java.util.EnumSet;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MoaWanderAroundGoal extends Goal {
    public static final int DEFAULT_CHANCE = 120;
    protected final PathAwareEntity mob;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected final double speed;
    protected int chance;
    protected boolean ignoringChance;
    private final boolean canDespawn;

    public MoaWanderAroundGoal(PathAwareEntity mob, double speed) {
        this(mob, speed, 120);
    }

    public MoaWanderAroundGoal(PathAwareEntity mob, double speed, int chance) {
        this(mob, speed, chance, true);
    }

    public MoaWanderAroundGoal(PathAwareEntity entity, double speed, int chance, boolean canDespawn) {
        this.mob = entity;
        this.speed = speed;
        this.chance = chance;
        this.canDespawn = canDespawn;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && this.mob.getDespawnCounter() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(toGoalTicks(this.chance)) != 0) {
                    return false;
                }
            }

            Vec3d bestTarget = null;
            double highestY = Double.NEGATIVE_INFINITY;

            //Take 3 samples, get the one with the highest Y value
            for (int i = 0; i < 2; i++) {
                Vec3d sampleTarget = this.getWanderTarget();
                if (sampleTarget != null && sampleTarget.y > highestY) {
                    highestY = sampleTarget.y;
                    bestTarget = sampleTarget;
                }
            }

            if (bestTarget == null) {
                return false;
            } else {
                //Set new target to high Y one
                this.targetX = bestTarget.x;
                this.targetY = bestTarget.y;
                this.targetZ = bestTarget.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        return NoPenaltyTargeting.find(this.mob, 9, 8);
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && !this.mob.hasControllingPassenger();
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }

    public void ignoreChanceOnce() {
        this.ignoringChance = true;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
