package net.id.paradiselost.entities.passive;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.ai.EatFlowersGoal;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.loot.ParadiseLostLootTables;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PopomEntity extends AnimalEntity {

    private static final TrackedData<Integer> FUR_SIZE;
    private int eatingTimer;

    public PopomEntity(EntityType<? extends PopomEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUR_SIZE, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.1));
        this.goalSelector.add(2, new AnimalMateGoal(this, 0.9));
        this.goalSelector.add(3, new TemptGoal(this, 0.8, stack -> stack.isIn(ItemTags.SMALL_FLOWERS), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.05));
        this.goalSelector.add(5, new EatFlowersGoal(this, 0.9));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        super.initGoals();
    }

    // Custom sounds for Popom
    @Override
    protected SoundEvent getAmbientSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_AMBIENT;
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        this.playSound(ParadiseLostSoundEvents.ENTITY_POPOM_HURT, this.getSoundVolume(), this.getSoundPitch() + 0.3F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_DEATH;
    }

    // Define attributes for Popom
    public static DefaultAttributeContainer.Builder createPopomAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            int furSize = this.getFurSize();
            if (!this.getWorld().isClient && furSize > 1) {
                this.getWorld().playSoundFromEntity(null, this, ParadiseLostSoundEvents.ENTITY_POPOM_HARVEST, SoundCategory.PLAYERS, 1.0F, 1.0F);
                int i = furSize == 2 ? 1 + this.random.nextInt(2) : 2 + this.random.nextInt(3);

                for (int j = 0; j < i; j++) {
                    ItemEntity itemEntity = this.dropItem(ParadiseLostItems.POPOM_JELLY);
                    if (itemEntity != null) {
                        itemEntity.setVelocity(
                                itemEntity.getVelocity()
                                        .add(
                                                ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                                                (this.random.nextFloat() * 0.05F),
                                                ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)
                                        )
                        );
                    }
                }
                this.setFurSize(0);
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ParadiseLostEntityTypes.POPOM.create(world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("FurSize", this.dataTracker.get(FUR_SIZE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.dataTracker.set(FUR_SIZE, compound.getInt("FurSize"));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.FLOWERS);
    }

    @Override
    public void tickMovement() {
        if (this.getWorld().isClient) {
            this.eatingTimer = Math.max(0, this.eatingTimer - 1);
        }

        super.tickMovement();
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART) {
            this.eatingTimer = 40;
        } else {
            super.handleStatus(status);
        }
    }

    public float getHeadAngle(float delta) {
        if (this.eatingTimer > 4 && this.eatingTimer <= 36) {
            float f = (this.eatingTimer - 4 - delta) / 32.0F;
            return (float) (Math.PI / 5) + 0.21991149F * MathHelper.sin(f * 28.7F);
        } else {
            return this.eatingTimer > 0 ? (float) (Math.PI / 5) : this.getPitch() * (float) (Math.PI / 180.0);
        }
    }

    public int getFurSize() {
        return this.dataTracker.get(FUR_SIZE);
    }

    public void setFurSize(int size) {
        this.dataTracker.set(FUR_SIZE, size);
    }

    public void eat() {
        this.setFurSize(getFurSize() + 1);
    }

    @Override
    public RegistryKey<LootTable> getLootTableId() {
        return switch (this.getFurSize()) {
            case 2 -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_2;
            case 3 -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_3;
            default -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_0;
        };
    }

    static {
        FUR_SIZE = DataTracker.registerData(PopomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
