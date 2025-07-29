package net.id.paradiselost.entities.hostile;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Util;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class QuintEntity extends PathAwareEntity implements Monster {

    private static final double ENLIGHTEN_FOLLOW_RANGE = 18;
    private static final double ENLIGHTEN_RANGE = 2;

    public QuintEntity(EntityType<? extends QuintEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (!world.getBlockState(pos.down(1)).isAir() || !world.getBlockState(pos.down(2)).isAir())
            return 10.0F;
        if (!world.getBlockState(pos.down(3)).isAir() || !world.getBlockState(pos.down(4)).isAir())
            return 8.0F;
        return world.getBlockState(pos).isAir() ? 6.0F : 0.0F;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EnlightenGoal(this));
        this.goalSelector.add(2, new GetCloseToEnlightenGoal(this));
        this.goalSelector.add(3, new FloatIdleGoal(this));
        this.goalSelector.add(4, new RandomlyFloatGoal(this));
    }

    @Debug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        birdNavigation.setCanPathThroughDoors(true);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }


    @Nullable
    private EnvoyEntity findValidEnvoy(double range) {
        var world = this.getWorld();
        var pos = this.getPos();
        var possibleEnvoys = world.getOtherEntities(this, Box.of(pos, range, range, range), this::isValidEnvoy);
        for (var pe : possibleEnvoys) {
            if (this.canSee(pe)) {
                return (EnvoyEntity) pe;
            }
        }
        return null;
    }

    private boolean isValidEnvoy(Entity ent) {
        return ent instanceof EnvoyEntity && !((EnvoyEntity) ent).getEnlightened();
    }

    static class GetCloseToEnlightenGoal extends Goal {

        protected final QuintEntity mob;
        @Nullable
        protected EnvoyEntity target;

        GetCloseToEnlightenGoal(QuintEntity mob) {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            var valid = this.mob.findValidEnvoy(ENLIGHTEN_FOLLOW_RANGE);
            if (valid != null) {
                target = valid;
                return mob.random.nextInt(10) == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return mob.navigation.isFollowingPath();
        }

        @Override
        public void start() {
            if (target != null) {
                mob.navigation.startMovingAlong(mob.navigation.findPathTo(target.getBlockPos(), 1), 1.0);
            }
        }
    }

    static class EnlightenGoal extends Goal {

        protected final QuintEntity mob;
        @Nullable
        protected EnvoyEntity target;

        EnlightenGoal(QuintEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            var valid = this.mob.findValidEnvoy(ENLIGHTEN_RANGE);
            if (valid != null) {
                target = valid;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return mob.navigation.isFollowingPath() || (target != null && !target.getEnlightened());
        }

        @Override
        public void tick() {
            if (target != null && target.distanceTo(this.mob) < 0.75) {
                target.setEnlightened(true);
                this.mob.discard();
            }
        }
    }

    static class FloatIdleGoal extends Goal {

        protected final QuintEntity mob;
        protected int timer;

        FloatIdleGoal(QuintEntity mob) {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            return mob.random.nextInt(2) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return timer < 0;
        }

        @Override
        public void start() {
            this.timer = 200;
        }

        @Override
        public void tick() {
            timer--;
        }
    }

    static class RandomlyFloatGoal extends Goal {

        protected final QuintEntity mob;
        protected int delay = 0;

        RandomlyFloatGoal(QuintEntity mob) {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            return mob.navigation.isIdle() && mob.random.nextInt(10) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return (mob.navigation.isFollowingPath() || ++this.delay < 80);
        }

        @Override
        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                mob.navigation.startMovingAlong(mob.navigation.findPathTo(BlockPos.ofFloored(vec3d), 1), 1.0);
            }
        }

        @Override
        public void stop() {
            this.delay = 0;
        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d2 = mob.getRotationVec(0.0F);
            Vec3d vec3d3 = AboveGroundTargeting.find(mob, 8, 7, vec3d2.x, vec3d2.z, (float) (Math.PI / 2), 5, 2);
            return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(mob, 8, 4, -2, vec3d2.x, vec3d2.z, (float) (Math.PI / 2));
        }
    }

    public static boolean canMobSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return true;
    }

    public static DefaultAttributeContainer.Builder createQuintAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }
}
