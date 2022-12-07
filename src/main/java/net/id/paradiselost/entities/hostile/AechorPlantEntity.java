package net.id.paradiselost.entities.hostile;

import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.id.paradiselost.entities.projectile.PoisonNeedleEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class AechorPlantEntity extends ParadiseLostAnimalEntity implements RangedAttackMob {
    public final int poisonRemaining;
    public float sinage;
    public int size;

    public AechorPlantEntity(EntityType<? extends AechorPlantEntity> entityType, World world) {
        super(entityType, world);

        this.size = this.random.nextInt(4) + 1;
        this.sinage = this.random.nextFloat() * 6F;
        this.poisonRemaining = this.random.nextInt(4) + 2;

        this.setPosition(Math.floor(this.getX()) + 0.5, this.getY(), Math.floor(this.getZ()) + 0.5);
    }

    public static DefaultAttributeContainer.Builder createAechorPlantAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    public EntityDimensions getSizeForStatus(EntityPose entityPose_1) {
        return EntityDimensions.changing(0.75F + ((float) size * 0.125F), 0.5F + ((float) size * 0.075F));
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        goalSelector.add(4, new ProjectileAttackGoal(this, 0.0D, 30, 1.0F));
        targetSelector.add(1, new RevengeGoal(this, AechorPlantEntity.class));
        targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, false,
                entity -> !(entity instanceof AechorPlantEntity)));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.getBlockState(this.getBlockPos().down(1)).isIn(ParadiseLostBlockTags.AECHOR_PLANT_VALID_GROUND)) {
            this.kill();
        }

        if (this.hurtTime > 0) {
            this.sinage += 0.9F;
        } else {
            sinage += (getAttacker() != null ? 0.3f : 0.1f);
        }
        if (sinage > 3.141593F * 2F) {
            sinage -= (3.141593F * 2F);
        }
    }

    @Override
    public boolean canSee(Entity entity) {
        double distance = distanceTo(entity);
        return distance <= 6.0F && super.canSee(entity);
    }

    public static boolean canSpawn(EntityType<? extends AechorPlantEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(ParadiseLostBlockTags.AECHOR_PLANT_VALID_GROUND)
                && world.getBlockState(pos).isAir()
                && world.getBaseLightLevel(pos, 0) > 8;
    }

    @Override
    public void attack(LivingEntity targetIn, float distFactor) {
        PoisonNeedleEntity needle = new PoisonNeedleEntity(this, this.world);
        double x = targetIn.getX() - this.getX();
        double y = targetIn.getBoundingBox().minY + (double) (targetIn.getHeight() / 3.0F) - needle.getY();
        double z = targetIn.getZ() - this.getZ();
        double distance = Math.sqrt((float) (x * x + z * z));

        needle.setVelocity(x, y + distance * 0.20000000298023224D, z, 1.0F, (float) (14 - this.world.getDifficulty().getId() * 4));
        this.playSound(ParadiseLostSoundEvents.ENTITY_AECHOR_PLANT_SHOOT, 1.0F, 1.2F / (this.getRandom().nextFloat() * 0.2F + 0.9F));
        this.world.spawnEntity(needle);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (heldItem.getItem() == ParadiseLostItems.SKYROOT_BUCKET && !player.getAbilities().creativeMode) {
            heldItem.setCount(heldItem.getCount() - 1);

//            if (heldItem.isEmpty()) {
//                player.setStackInHand(hand, new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET));
//            } else if (!player.getInventory().insertStack(new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET))) {
//                player.dropItem(new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET), false);
//            }

            return ActionResult.SUCCESS;
        } else {
            return super.interactMob(player, hand);
        }
    }

    @Override
    public Vec3d getVelocity() {
        return Vec3d.ZERO;
    }

    @Override
    public void setVelocity(Vec3d velocity) {
    }

    @Override
    public void takeKnockback(double strength, double xRatio, double zRatio) {
        if (getHealth() <= 0.0F) {
            super.takeKnockback(strength, xRatio, zRatio);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("size", this.size);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.size = compound.getInt("size");
    }

    @Override
    protected void pushAway(Entity entityIn) {
        if (!entityIn.isConnectedThroughVehicle(this)) {
            if (!this.noClip && !entityIn.noClip) {
                double d0 = this.getX() - entityIn.getX();
                double d1 = this.getZ() - entityIn.getZ();
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = Math.sqrt((float) d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;

                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
//                    d0 = d0 * (double) (1.0F - entityIn.pushthrough); //TODO: What is pushthrough?
//                    d1 = d1 * (double) (1.0F - entityIn.pushthrough);

                    if (!entityIn.hasPassengers()) {
                        entityIn.addVelocity(-d0, 0.0D, -d1);
                    }
                }
            }
        }
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entityIn) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_AECHOR_PLANT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
