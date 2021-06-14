package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

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
        this.goalSelector.add(0, new AerwhaleEntity.FlyGoal(1.0));

        //this.goalSelector.add(0, new WanderAroundGoal(this, 0.5));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
//        return false;
//    }


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
        protected final double speed;

        public FlyGoal(double d) {
            this.speed = d;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        // I had to comment out all System.outs becuase it was lagging my IDE

        public boolean canStart() {
//            if (this.mob.getRandom().nextInt(10) != 0) {
//                return false;
//            }

            Vec3d vec3d = null;//this.getWanderTarget();
            if (vec3d == null) {
//                System.out.println("Target was null");
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
//                System.out.println("Target found:"+targetX+", "+targetY+", "+targetZ);
                return true;
            }
        }

//        @Nullable
//        protected Vec3d getWanderTarget() {
//            return TargetFinder.findAirTarget(mob, 16, 6, Vec3d.ZERO, 180.0F, 20, 4);
//        }


        public boolean shouldContinue() {
            if (this.mob.getRandom().nextInt(30) != 0) {
//                System.out.println("Continuing path...");
                return true;
            }
//            System.out.println("Stopping path...");
            return false;

//            if (this.mob.getNavigation().isNearPathStartPos()) {
//                System.out.println("Continuing path...");
//            } else {
//                System.out.println("Stopping path...");
//            }
//            return this.mob.getNavigation().isNearPathStartPos();
        }

        public void start() {
//            System.out.println("Starting nav to:"+targetX+", "+targetY+", "+targetZ);
            this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, 0.5);
        }

        public void stop() {
//            System.out.println("Stopping nav...");
            this.mob.getNavigation().stop();
            super.stop();
        }

    }
}
