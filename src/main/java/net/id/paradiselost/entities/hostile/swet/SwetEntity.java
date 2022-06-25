package net.id.paradiselost.entities.hostile.swet;

import net.id.paradiselost.entities.block.FloatingBlockEntity;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.id.paradiselost.util.ParadiseLostDamageSources;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntConsumer;
import java.util.function.Predicate;

public abstract class SwetEntity extends SlimeEntity {
    protected static final EntityAttributeModifier knockbackResistanceModifier = new EntityAttributeModifier(
            "Temporary swet knockback resistance",
            1,
            EntityAttributeModifier.Operation.ADDITION);
    public IntConsumer setRandomLookTimer;
    protected int initialSize = 2;
    protected float massStuck = 0;

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
        init();
    }

    public static DefaultAttributeContainer.Builder createSwetAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0);
    }

    protected static boolean isAbsorbable(Entity entity, World world) {
        if (entity.isCollidable()) {
            return false;
        }

        if (!((entity instanceof LivingEntity)
                || (entity instanceof TntEntity)
                || (entity instanceof TntMinecartEntity)
                || (entity instanceof FloatingBlockEntity)
                /* ArmorStands are LivingEntities */
        )) {
            return false;
        }

        boolean canPickupNonPlayers = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
        boolean isPet = (entity instanceof TameableEntity pet && pet.isTamed());
        boolean isEligiblePlayer = (entity instanceof PlayerEntity player && !player.getAbilities().flying);
        boolean isEligiblePet = isPet && world.getDifficulty() != Difficulty.EASY;
        boolean isEligibleNonPlayer = !(entity instanceof PlayerEntity || isPet) && canPickupNonPlayers;

        return !entity.isSneaking() && (isEligiblePlayer || isEligiblePet || isEligibleNonPlayer);
    }

    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && (world.getBottomY() + 192 > pos.getY()) && canMobSpawn(type, world, spawnReason, pos, random);
    }

    // TODO: Use PathAwareEntity at some point (PL-1.7)
    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return !isLeashed();
    }

    @Override
    protected void updateLeash() {
        super.updateLeash();
        Entity entity = getHoldingEntity();
        if (entity != null && entity.world == world) {
            setPositionTarget(entity.getBlockPos(), 5);
            float f = distanceTo(entity);
//            if (this instanceof TameableEntity && ((TameableEntity)this).isInSittingPose()) {
//                if (f > 10.0f) {
//                    this.detachLeash(true, true);
//                }
//                return;
//            }
            updateForLeashLength(f);
            if (f > 10.0f) {
                detachLeash(true, true);
                goalSelector.disableControl(Goal.Control.MOVE);
            } else if (f > 6.0f) {
                double d = (entity.getX() - getX()) / (double)f;
                double e = (entity.getY() - getY()) / (double)f;
                double g = (entity.getZ() - getZ()) / (double)f;
                setVelocity(getVelocity().add(Math.copySign(d * d * 0.4, d), Math.copySign(e * e * 0.4, e), Math.copySign(g * g * 0.4, g)));
            } else {
                goalSelector.enableControl(Goal.Control.MOVE);
                Vec3d vec3d = new Vec3d(entity.getX() - getX(), entity.getY() - getY(), entity.getZ() - getZ()).normalize().multiply(Math.max(f - 2.0f, 0.0f));
                getNavigation().startMovingTo(getX() + vec3d.x, getY() + vec3d.y, getZ() + vec3d.z, getRunFromLeashSpeed());
            }
        }
    }

    protected double getRunFromLeashSpeed() {
        return 1.0;
    }

    protected void updateForLeashLength(float leashLength) {
        // This would contain circumstances for cutting off activities after certain lengths
        // Swets dont do much so....do nothing here?
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isIn(ParadiseLostItemTags.GROWS_SWETS)) {
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            setSize(getSize() + 1, true);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void initGoals() {
        // Replace the inherited slime target selectors with one that avoids chasing absorbed players, and ignores iron golems
        super.initGoals();
        targetSelector.clear();
        targetSelector.add(1, new FollowUnabsorbedTargetGoal<>(
                this, PlayerEntity.class, 10, true, false, (player) ->
                Math.abs(player.getY() - getY()) <= 4.0D &&
                        !(FollowUnabsorbedTargetGoal.canAbsorb(this, player))
        ));
    }

    protected void init() {
        setHealth(getMaxHealth());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Oversize", getSize() >= 20);
    }

    @Override
    public void tick() {
        // Entities don't have onEntityCollision, so this does that
        if (!isDead()) {
            massStuck = 0;
            world.getOtherEntities(this, getBoundingBox().stretch(0.9, 0.9, 0.9)).forEach((entity) -> {
                Box box = entity.getBoundingBox();
                massStuck += box.getXLength() * box.getYLength() * box.getZLength();
            });
            world.getOtherEntities(this, getBoundingBox()).forEach(this::onEntityCollision);
        }
        
        super.tick();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        // Already taken care of in tick()
    }

    @Override
    protected void damage(LivingEntity target) {
        if (isAlive()) {
            int i = getSize();
            if (squaredDistanceTo(target) < 0.6 * (double)i * (0.6 * (double)i) && canSee(target) && target.damage(ParadiseLostDamageSources.swet(this), getDamageAmount())) {
                playSound(ParadiseLostSoundEvents.ENTITY_SWET_ATTACK, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
                applyDamageEffects(this, target);
            }
        }
    }

    protected void onEntityCollision(Entity entity) {
        // special absorption rules
        if (entity instanceof SwetEntity swet) {
            if (getSize() >= swet.getSize() && !swet.isDead()) {
                setSize(MathHelper.ceil(MathHelper.sqrt(getSize() * getSize() + swet.getSize() * swet.getSize())), true);
                swet.discard();
            }
            return;
        }
        // Make items ride the swet. They often shake free with the jiggle physics
        if (entity instanceof ItemEntity item) {
            if (item.getStack().isIn(ParadiseLostItemTags.GROWS_SWETS)) {
                setSize(getSize() + 1, false);
                item.remove(RemovalReason.KILLED);
                return;
            }
            item.startRiding(this, true);
            return;
        }
        boolean absorbable = isAbsorbable(entity, world);
        if (absorbable) {
            // The higher this number, the stiffer the wobble is
            if (massStuck < 1) {
                massStuck = 1;
            }
            // dampened oscillator (nonlinear restoring force): x'' = -μx' - kx
            Vec3d center = getBoundingBox().getCenter().add(0,0.45F * getBoundingBox().getYLength() - (getSize() == 0 ? -0.25F : 1),0);
            Vec3d suckVelocity = // acceleration (x'')
                    center.subtract(entity.getPos()) // entity displacement (-x)
                            .multiply(MathHelper.clamp(0.25 + massStuck / 100, 0, 1)) // coefficient (k)
                            .add(
                                    getVelocity().subtract(entity.getVelocity()) // delta velocity (-x')
                                            .multiply(0.45 / massStuck / getSize()) // coefficient (μ)
                            );

            double maxSpeed = getSize() * 0.1 + 0.25;
            if (suckVelocity.length() != 0) {
                // clamp the suck velocity
                suckVelocity = suckVelocity.multiply(Math.min(1, maxSpeed / suckVelocity.length()));
            }

            entity.setVelocity(entity.getVelocity().add(suckVelocity));
            entity.velocityDirty = true;
            entity.fallDistance = 0;
        }

        if (entity instanceof LivingEntity livingEntity) {
            // Hack to prevent knockback; TODO: find a better way to prevent knockback
            EntityAttributeInstance knockbackResistance = livingEntity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            if (absorbable && knockbackResistance != null) {
                knockbackResistance.addTemporaryModifier(knockbackResistanceModifier);
                damage(livingEntity);
                knockbackResistance.removeModifier(knockbackResistanceModifier);
            } else {
                damage(livingEntity);
            }
        }
    }

    // Prevent pushing entities away, as this interferes with sucking them in
    @Override
    public boolean isPushable() {
        return false;
    }

    // Same as above
    @Override
    protected void pushAway(Entity entity) {
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setSize(int size, boolean heal) {
        super.setSize(size, heal);
        int clampedSize = MathHelper.clamp(size, 1, 127);
        float sqrtClampedSize = MathHelper.sqrt(clampedSize);
        getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * sqrtClampedSize + 0.1);
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(12 * sqrtClampedSize + 1);
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(0.25 * clampedSize + sqrtClampedSize);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setSize(initialSize, true);
        getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier(
                "Random spawn bonus",
                random.nextGaussian() * 0.05D,
                EntityAttributeModifier.Operation.MULTIPLY_BASE));
        setLeftHanded(random.nextFloat() < 0.05F);

        return entityData;
    }

    // Prevents duplicate entities
    @Override
    public void remove(RemovalReason reason) {
        setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            emitGameEvent(GameEvent.ENTITY_DIE);
        }
    }

    @Override
    protected Identifier getLootTableId() {
        return getType().getLootTableId();
    }
    
    /**
     * Called from {@link net.id.paradiselost.mixin.entity.SlimeEntityMixin the mixin} to override the slime particles.
     */
    public void spawnSwetParticles() {
        int size = getSize();
        for(int i = 0; i < size * 8; i++) {
            float trig = random.nextFloat() * 6.2831855F;
            float mag = random.nextFloat() * 0.5F + 0.5F;
            float offset = size * 0.5F * mag;
            float xOff = MathHelper.sin(trig) * offset;
            float zOff = MathHelper.cos(trig) * offset;
            world.addParticle(createParticle(), getX() + xOff, getY(), getZ() + zOff, 0, 0, 0);
        }
    }
    
    /**
     * Creates a particle for this swet. This replaces the vanilla slime effect.
     *
     * @return The particle effect to use
     */
    protected abstract ParticleEffect createParticle();
    
    protected static class FollowUnabsorbedTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public FollowUnabsorbedTargetGoal(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
        }

        protected static boolean canAbsorb(Entity swet, Entity target) {
            return !target.isSneaking() && !(target instanceof PlayerEntity player && player.getAbilities().flying) &&
                    swet.getBoundingBox().expand(0, 0.5, 0).offset(0, 0.25, 0).intersects(target.getBoundingBox());
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !(canAbsorb(mob, mob.getTarget()));
        }
    }
}
