package net.id.paradiselost.entities.passive;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.id.paradiselost.entities.hostile.IEnlightenable;
import net.id.paradiselost.entities.hostile.KeeperEntity;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

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

    @Override
    public void lookAt(EntityAnchorArgumentType.EntityAnchor anchorPoint, Vec3d target) {
    }

    @Override
    public void setAngles(float yaw, float pitch) {
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
    }


    @Nullable
    private IEnlightenable findValidEnlightenTarget(double range) {
        var world = this.getWorld();
        var pos = this.getPos();
        var possibleEnvoys = world.getOtherEntities(this, Box.of(pos, range, range, range), this::isEnlightenableEntity);
        for (var pe : possibleEnvoys) {
            if (this.canSee(pe)) {
                return (IEnlightenable) pe;
            }
        }
        return null;
    }

    private boolean isEnlightenableEntity(Entity ent) {
        return (ent instanceof EnvoyEntity envoy && !envoy.getEnlightened()) ||
         (ent instanceof KeeperEntity keeper && !keeper.getEnlightened());
    }

    @Override
    public void tick() {
        if (this.getWorld().isClient && this.random.nextInt(3) == 0) {
            this.getWorld().addParticle(ParadiseLostParticles.LIT_CLOUD,
                    this.getParticleX(0.2), (this.getY() + 0.15 + this.random.nextDouble() * 0.4), this.getParticleZ(0.2),
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
            this.setControls(EnumSet.of(Goal.Control.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            var valid = this.mob.findValidEnlightenTarget(ENLIGHTEN_FOLLOW_RANGE);
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
                mob.navigation.startMovingAlong(mob.navigation.findPathTo(((Entity) target).getBlockPos(), 1), 2.0);
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
        public boolean canStart() {
            var valid = this.mob.findValidEnlightenTarget(ENLIGHTEN_RANGE);
            if (valid != null) {
                target = valid;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return (mob.navigation.isFollowingPath() || (target != null && !target.getEnlightened())) && ((Entity) target).distanceTo(this.mob) < 6;
        }

        @Override
        public void tick() {
            System.out.println(((Entity) target).distanceTo(this.mob));
            if (target != null && ((Entity) target).distanceTo(this.mob) < 1.0) {
                target.setEnlightened(true);
                this.mob.discard();
            }
        }

        @Override
        public void start() {
            if (target != null) {
                mob.navigation.startMovingAlong(mob.navigation.findPathTo(((Entity) target).getBlockPos(), 1), 2.0);
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
