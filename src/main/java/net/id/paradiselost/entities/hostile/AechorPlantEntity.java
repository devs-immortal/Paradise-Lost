package net.id.paradiselost.entities.hostile;

import net.id.paradiselost.entities.passive.ParadiseLostAnimalEntity;
import net.id.paradiselost.entities.projectile.PoisonNeedleEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.*;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class AechorPlantEntity extends ParadiseLostAnimalEntity implements RangedAttackMob {
    public final int poisonRemaining;
    public float sinage;
    public int size;

    public AechorPlantEntity(EntityType<? extends AechorPlantEntity> entityType, World world) {
        super(entityType, world);

        size = random.nextInt(4) + 1;
        sinage = random.nextFloat() * 6F;
        poisonRemaining = random.nextInt(4) + 2;

        setPosition(getX(), getY(), getZ());
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

        if (!world.getBlockState(getBlockPos().down(1)).isIn(ParadiseLostBlockTags.AECHOR_PLANT_VALID_GROUND)) {
            kill();
        }

        if (hurtTime > 0) {
            sinage += 0.9F;
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

    @Override
    public boolean canSpawn(WorldAccess worldIn, SpawnReason SpawnReason) {
        return worldIn.getBlockState(getBlockPos().down(1)).isIn(ParadiseLostBlockTags.AECHOR_PLANT_VALID_GROUND)
                && worldIn.getBaseLightLevel(getBlockPos(), 0) > 8;
    }

    @Override
    public void attack(LivingEntity targetIn, float distFactor) {
        PoisonNeedleEntity needle = new PoisonNeedleEntity(this, world);
        double x = targetIn.getX() - getX();
        double y = targetIn.getBoundingBox().minY + (double)(targetIn.getHeight() / 3.0F) - needle.getY();
        double z = targetIn.getZ() - getZ();
        double distance = Math.sqrt((float) (x * x + z * z));

        needle.setVelocity(x, y + distance * 0.20000000298023224D, z, 1.0F, (float)(14 - world.getDifficulty().getId() * 4));
        playSound(ParadiseLostSoundEvents.ENTITY_AECHOR_PLANT_SHOOT, 1.0F, 1.2F / (getRandom().nextFloat() * 0.2F + 0.9F));
        world.spawnEntity(needle);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (heldItem.getItem() == ParadiseLostItems.SKYROOT_BUCKET && !player.getAbilities().creativeMode) {
            heldItem.setCount(heldItem.getCount() - 1);

            if (heldItem.isEmpty()) {
                player.setStackInHand(hand, new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET));
            } else if (!player.getInventory().insertStack(new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET))) {
                player.dropItem(new ItemStack(ParadiseLostItems.SKYROOT_POISON_BUCKET), false);
            }

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
        compound.putInt("size", size);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        size = compound.getInt("size");
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
