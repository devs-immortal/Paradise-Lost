package net.id.paradise_lost.entity.passive;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.id.paradise_lost.entity.hostile.IEnlightenable;
import net.id.paradise_lost.entity.hostile.SentinelEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class QuintEntity extends PathfinderMob implements Enemy {

    private static final double ENLIGHTEN_FOLLOW_RANGE = 18;
    private static final double ENLIGHTEN_RANGE = 2;

    public QuintEntity(EntityType<? extends QuintEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 20;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        if (!world.getBlockState(pos.below(1)).isAir() || !world.getBlockState(pos.below(2)).isAir())
            return 10.0F;
        if (!world.getBlockState(pos.below(3)).isAir() || !world.getBlockState(pos.below(4)).isAir())
            return 8.0F;
        return world.getBlockState(pos).isAir() ? 6.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EnlightenGoal(this));
        this.goalSelector.addGoal(2, new GetCloseToEnlightenGoal(this));
        this.goalSelector.addGoal(3, new FloatIdleGoal(this));
        this.goalSelector.addGoal(4, new RandomlyFloatGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        birdNavigation.setCanOpenDoors(true);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanPassDoors(true);
        return birdNavigation;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public void lookAt(EntityAnchorArgument.Anchor anchorPoint, Vec3 target) {
    }

    @Override
    public void absRotateTo(float yaw, float pitch) {
    }

    @Override
    protected void setRot(float yaw, float pitch) {
    }

    @Nullable
    private IEnlightenable findValidEnlightenTarget(double range) {
        var world = this.level();
        var pos = this.position();
        var possibleEnvoys = world.getEntities(this, AABB.ofSize(pos, range, range, range), this::isEnlightenableEntity);
        for (var pe : possibleEnvoys) {
            if (this.hasLineOfSight(pe)) {
                return (IEnlightenable) pe;
            }
        }
        return null;
    }

    private boolean isEnlightenableEntity(Entity ent) {
        return (ent instanceof EnvoyEntity envoy && !envoy.getEnlightened())
                || (ent instanceof SentinelEntity sentinel && !sentinel.getEnlightened());
    }

    @Override
    public void tick() {
        if (this.level().isClientSide && this.random.nextInt(3) == 0) {
            this.level().addParticle(ParadiseLostParticleTypes.LIT_CLOUD,
                    this.getRandomX(0.2), (this.getY() + 0.15 + this.random.nextDouble() * 0.4), this.getRandomZ(0.2),
                    (this.random.nextDouble() - 0.5) * 0.05, -this.random.nextDouble() * 0.025, (this.random.nextDouble() - 0.5) * 0.05
            );
        }
        super.tick();
    }

    static class GetCloseToEnlightenGoal extends Goal {

        protected final QuintEntity mob;
        @Nullable
        protected IEnlightenable target;

        GetCloseToEnlightenGoal(QuintEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            var valid = this.mob.findValidEnlightenTarget(ENLIGHTEN_FOLLOW_RANGE);
            if (valid != null) {
                target = valid;
                return mob.random.nextInt(10) == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return mob.navigation.isInProgress();
        }

        @Override
        public void start() {
            if (target != null) {
                mob.navigation.moveTo(mob.navigation.createPath(((Entity) target).blockPosition(), 1), 2.0);
            }
        }
    }

    static class EnlightenGoal extends Goal {

        protected final QuintEntity mob;
        @Nullable
        protected IEnlightenable target;

        EnlightenGoal(QuintEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            var valid = this.mob.findValidEnlightenTarget(ENLIGHTEN_RANGE);
            if (valid != null) {
                target = valid;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return (mob.navigation.isInProgress() || (target != null && !target.getEnlightened())) && ((Entity) target).distanceTo(this.mob) < 6;
        }

        @Override
        public void tick() {
            if (target != null && ((Entity) target).distanceTo(this.mob) < 1.0) {
                target.setEnlightened(true);
                this.mob.discard();
            }
        }

        @Override
        public void start() {
            if (target != null) {
                mob.navigation.moveTo(mob.navigation.createPath(((Entity) target).blockPosition(), 1), 2.0);
            }
        }
    }

    static class FloatIdleGoal extends Goal {

        protected final QuintEntity mob;
        protected int timer;

        FloatIdleGoal(QuintEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.random.nextInt(2) == 0;
        }

        @Override
        public boolean canContinueToUse() {
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.navigation.isDone() && mob.random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return (mob.navigation.isInProgress() || ++this.delay < 80);
        }

        @Override
        public void start() {
            Vec3 vec3d = this.getRandomLocation();
            if (vec3d != null) {
                mob.navigation.moveTo(mob.navigation.createPath(BlockPos.containing(vec3d), 1), 1.0);
            }
        }

        @Override
        public void stop() {
            this.delay = 0;
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vec3d2 = mob.getViewVector(0.0F);
            Vec3 vec3d3 = HoverRandomPos.getPos(mob, 8, 7, vec3d2.x, vec3d2.z, (float) (Math.PI / 2), 5, 2);
            return vec3d3 != null ? vec3d3 : AirAndWaterRandomPos.getPos(mob, 8, 4, -2, vec3d2.x, vec3d2.z, (float) (Math.PI / 2));
        }
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return true;
    }

    public static AttributeSupplier.Builder createQuintAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }
}
