package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class AerwhaleEntity extends AetherAnimalEntity {

    public AerwhaleEntity(Level world) {
        super(AetherEntityTypes.AERWHALE, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.add(0, new SwimGoal(this));
        //this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D, 20));
        //this.goalSelector.add(3, new WanderAroundGoal(this, 1.0D, 15));
        //this.goalSelector.add(0, new EscapeDangerGoal(this, 1.0D));
        this.goalSelector.addGoal(0, new AerwhaleEntity.FlyGoal(1.0));

        //this.goalSelector.add(0, new WanderAroundGoal(this, 0.5));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
//        return false;
//    }


    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }


    @Override
    public boolean onClimbable() {
        return false;
    }


    class FlyGoal extends Goal {

        protected final PathfinderMob mob = AerwhaleEntity.this;
        protected double targetX;
        protected double targetY;
        protected double targetZ;
        protected final double speed;

        public FlyGoal(double d) {
            this.speed = d;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        // I had to comment out all System.outs becuase it was lagging my IDE

        public boolean canUse() {
//            if (this.mob.getRandom().nextInt(10) != 0) {
//                return false;
//            }

            Vec3 vec3d = null;//this.getWanderTarget();
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


        public boolean canContinueToUse() {
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
            this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, 0.5);
        }

        public void stop() {
//            System.out.println("Stopping nav...");
            this.mob.getNavigation().stop();
            super.stop();
        }

    }
}
