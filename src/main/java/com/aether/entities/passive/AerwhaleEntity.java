package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

public class AerwhaleEntity extends AetherAnimalEntity {

    public AerwhaleEntity(World world) {
        super(AetherEntityTypes.AERWHALE, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    protected void initGoals() {
        //this.goalSelector.add(0, new SwimGoal(this));
        //this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D, 20));
        //this.goalSelector.add(3, new WanderAroundGoal(this, 1.0D, 15));
        //this.goalSelector.add(0, new EscapeDangerGoal(this, 1.0D));
        this.goalSelector.add(0, new AerwhaleEntity.FlyGoal());
        //this.goalSelector.add(0, new WanderAroundGoal(this, 0.5));
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    class FlyGoal extends Goal {

        protected final PathAwareEntity mob = AerwhaleEntity.this;
        protected double targetX;
        protected double targetY;
        protected double targetZ;

        public boolean canStart() {
            if (this.mob.getRandom().nextInt(10) != 0) {
                return false;
            }

            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                return true;
            }
        }

        @Nullable
        protected Vec3d getWanderTarget() {
            return TargetFinder.findAirTarget(AerwhaleEntity.this, 16, 6, Vec3d.ZERO, 20.0F, 16, 8);
        }

        public boolean shouldContinue() {
            return !this.mob.getNavigation().isIdle() && !this.mob.hasPassengers();
        }

        public void start() {
            this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, 1.0);
        }

        public void stop() {
            this.mob.getNavigation().stop();
            super.stop();
        }

    }
}
