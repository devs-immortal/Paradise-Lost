package com.aether.entities.hostile;

import com.aether.items.AetherItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

public abstract class SwetEntity extends SlimeEntity {
    public IntConsumer setRandomLookTimer;
    protected int initialSize = 2;
    protected float massStuck = 0;
    protected static final EntityAttributeModifier knockbackResistanceModifier = new EntityAttributeModifier(
            "Temporary swet knockback resistance",
            1,
            EntityAttributeModifier.Operation.ADDITION);

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
        this.init();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack.isOf(AetherItems.SWET_SPAWN_EGG)
                || stack.isOf(AetherItems.BLUE_SWET_SPAWN_EGG)
                || stack.isOf(AetherItems.PURPLE_SWET_SPAWN_EGG)
                || stack.isOf(AetherItems.GOLDEN_SWET_SPAWN_EGG)
                || stack.isOf(AetherItems.SWET_BALL)){
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            this.setSize(this.getSize() + 1, true);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void initGoals() {
        // Replace the inherited slime target selectors with one that avoids chasing absorbed players, and ignores iron golems
        super.initGoals();
        this.targetSelector.clear();
        this.targetSelector.add(1, new FollowUnabsorbedTargetGoal<>(
                this, PlayerEntity.class, 10, true, false, (player) ->
                Math.abs(player.getY() - this.getY()) <= 4.0D &&
                        !(canAbsorb(this, player))
        ));
    }

    protected void init() {
        this.setHealth(getMaxHealth());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    public static DefaultAttributeContainer.Builder createSwetAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Oversize", this.getSize()>=20);
    }

    @Override
    public void tick() {
        // Entities don't have onEntityCollision, so this does that
        if (!this.isDead()) {
            massStuck = 0;
            world.getOtherEntities(this, this.getBoundingBox().stretch(0.9, 0.9, 0.9)).forEach((entity) -> {
                Box box = entity.getBoundingBox();
                massStuck += box.getXLength() * box.getYLength() * box.getZLength();
            });
            world.getOtherEntities(this, this.getBoundingBox()).forEach(this::onEntityCollision);
        }
        super.tick();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        // Already taken care of in tick()
    }

    protected void onEntityCollision(Entity entity){
        if (entity instanceof SwetEntity swet) {
            if (this.getSize() >= swet.getSize() && !swet.isDead()) {
                this.setSize(MathHelper.ceil(MathHelper.sqrt(this.getSize() * this.getSize() + swet.getSize() * swet.getSize())), true);
                swet.discard();
            }
            return;
        }
        if (entity.isCollidable()){
            return;
        }
        // vehicles
        if (entity instanceof BoatEntity || entity instanceof MinecartEntity){
            return;
        }
        // Move this to vermillion swets (?) once they are added.
        // Ask Azzy about it 🤷‍
//        if (entity instanceof TntMinecartEntity tnt){
//            if (!tnt.isPrimed() && this.getSize() >= 4){
//                tnt.prime();
//            }
//        }
        // Make items ride the swet. They often shake free with the jiggle physics
        if (entity instanceof ItemEntity item) {
            if (item.getStack().getItem() == AetherItems.SWET_BALL) {
                this.setSize(this.getSize() + 1, false);
                item.remove(RemovalReason.KILLED);
                return;
            }
            item.startRiding(this, true);
            return;
        }
        boolean canPickupNonPlayers = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
        boolean isPet = (entity instanceof TameableEntity pet && pet.isTamed());
        boolean isEligiblePet = isPet && world.getDifficulty() != Difficulty.EASY;
        boolean isEligibleNonPlayer = !(entity instanceof PlayerEntity || isPet) && canPickupNonPlayers;
        boolean canBePickedUp = isAbsorbable(entity) && (entity instanceof PlayerEntity || isEligiblePet || isEligibleNonPlayer);
        if (canBePickedUp) {
            // The higher the number this is multiplied by, the stiffer the wobble is
            // If the wobbles feel too sharp, try changing the clamp below
            if (massStuck < 1){
                massStuck = 1;
            }
            Vec3d suckVelocity = this.getBoundingBox().getCenter().subtract(entity.getPos()).multiply(MathHelper.clamp(0.25 + massStuck/100,0,1))
                    .add(this.getVelocity().subtract(entity.getVelocity()).multiply(0.45 / massStuck / this.getSize()));
            Vec3d newVelocity = entity.getVelocity().add(suckVelocity);
            double velocityClamp = this.getSize() * 0.1 + 0.25;
            entity.setVelocity(MathHelper.clamp(newVelocity.getX(), -velocityClamp, velocityClamp),
                    Math.min(newVelocity.getY(), 0.25),
                    MathHelper.clamp(newVelocity.getZ(), -velocityClamp, velocityClamp));
            entity.velocityDirty = true;
            entity.fallDistance = 0;
        }

        if (entity instanceof LivingEntity livingEntity) {
            // Hack to prevent knockback; TODO: find a better way to prevent knockback
            EntityAttributeInstance knockbackResistance = livingEntity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            if (canBePickedUp && knockbackResistance != null) {
                knockbackResistance.addTemporaryModifier(knockbackResistanceModifier);
                this.damage(livingEntity);
                knockbackResistance.removeModifier(knockbackResistanceModifier);
            } else {
                this.damage(livingEntity);
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
    public void setSize(int size, boolean heal){
        super.setSize(size, heal);
        int clampedSize = MathHelper.clamp(size, 1, 127);
        float sqrtClampedSize = MathHelper.sqrt(clampedSize);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2*sqrtClampedSize + 0.1);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(12*sqrtClampedSize + 1);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(0.25*clampedSize + sqrtClampedSize);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt){
        setSize(initialSize, true);
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier(
                "Random spawn bonus",
                this.random.nextGaussian() * 0.05D,
                EntityAttributeModifier.Operation.MULTIPLY_BASE));
        this.setLeftHanded(this.random.nextFloat() < 0.05F);

        return entityData;
    }

    // Prevents duplicate entities
    @Override
    public void remove(RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.emitGameEvent(GameEvent.ENTITY_KILLED);
        }
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleTypes.SPLASH;
    }

    @Override
    protected Identifier getLootTableId() {
        return this.getType().getLootTableId();
    }

    protected static boolean canAbsorb(Entity swet, Entity target) {
        return isAbsorbable(target) &&
                swet.getBoundingBox().expand(0, 0.5, 0).offset(0, 0.25, 0).intersects(target.getBoundingBox());
    }

    protected static boolean isAbsorbable(Entity entity) {
        return !(entity.isSneaking() || entity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().flying);
    }

    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && canMobSpawn(type, world, spawnReason, pos, random);
    }

    protected static class FollowUnabsorbedTargetGoal<T extends LivingEntity> extends FollowTargetGoal<T> {
        public FollowUnabsorbedTargetGoal(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !(canAbsorb(this.mob, this.mob.getTarget()));
        }
    }
}
