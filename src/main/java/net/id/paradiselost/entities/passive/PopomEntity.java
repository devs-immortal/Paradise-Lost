package net.id.paradiselost.entities.passive;

import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.EntityType;
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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PopomEntity extends AnimalEntity {

    private static final TrackedData<Integer> FUR_SIZE;

    public PopomEntity(EntityType<? extends PopomEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUR_SIZE, 0);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.1));
        this.goalSelector.add(2, new AnimalMateGoal(this, 0.9));
        this.goalSelector.add(3, new TemptGoal(this, 0.8, stack -> stack.isIn(ParadiseLostItemTags.MOA_TEMPTABLES), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.05));
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
/*    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_POPOM_HURT;
    }*/

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

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
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

    public int getFurSize() {
        return this.dataTracker.get(FUR_SIZE);
    }

    static {
        FUR_SIZE = DataTracker.registerData(PopomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
