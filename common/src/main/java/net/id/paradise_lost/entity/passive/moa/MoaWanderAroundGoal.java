package net.id.paradise_lost.entity.passive.moa;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MoaWanderAroundGoal extends Goal {
    public static final int DEFAULT_CHANCE = 120;
    protected final PathfinderMob mob;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected final double speed;
    protected int chance;
    protected boolean ignoringChance;
    private final boolean canDespawn;

    public MoaWanderAroundGoal(PathfinderMob mob, double speed) {
        this(mob, speed, 120);
    }

    public MoaWanderAroundGoal(PathfinderMob mob, double speed, int chance) {
        this(mob, speed, chance, true);
    }

    public MoaWanderAroundGoal(PathfinderMob entity, double speed, int chance, boolean canDespawn) {
        this.mob = entity;
        this.speed = speed;
        this.chance = chance;
        this.canDespawn = canDespawn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && this.mob.getNoActionTime() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(reducedTickDelay(this.chance)) != 0) {
                    return false;
                }
            }

            Vec3 bestTarget = null;
            double highestY = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < 2; i++) {
                Vec3 sampleTarget = this.getWanderTarget();
                if (sampleTarget != null && sampleTarget.y > highestY) {
                    highestY = sampleTarget.y;
                    bestTarget = sampleTarget;
                }
            }

            if (bestTarget == null) {
                return false;
            } else {

                this.targetX = bestTarget.x;
                this.targetY = bestTarget.y;
                this.targetZ = bestTarget.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3 getWanderTarget() {
        return DefaultRandomPos.getPos(this.mob, 9, 8);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && !this.mob.hasControllingPassenger();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
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
