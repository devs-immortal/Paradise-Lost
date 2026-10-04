package net.id.paradise_lost.entity.passive;

import java.util.Optional;
import net.minecraft.world.entity.EntitySpawnReason;
import net.id.paradise_lost.mixin.entity.MobAccessor;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.entity.ai.EatFlowersGoal;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public class PopomEntity extends Animal {

    private static final EntityDataAccessor<Integer> FUR_SIZE;
    private int eatingTimer;

    public PopomEntity(EntityType<? extends PopomEntity> entityType, Level world) {
        super(entityType, world);
        this.updateLootTable();
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FUR_SIZE, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.1));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.9));
        this.goalSelector.addGoal(3, new TemptGoal(this, 0.8, stack -> stack.is(ItemTags.SMALL_FLOWERS), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.05));
        this.goalSelector.addGoal(5, new EatFlowersGoal(this, 0.9));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        super.registerGoals();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_AMBIENT;
    }

    protected void playHurtSound(DamageSource damageSource) {
        this.playSound(ParadiseLostSoundEvents.ENTITY_POPOM_HURT, this.getSoundVolume(), this.getVoicePitch() + 0.3F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_DEATH;
    }

    public static AttributeSupplier.Builder createPopomAttributes() {
        return createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            int furSize = this.getFurSize();
            if (this.level() instanceof ServerLevel serverLevel && furSize > 1) {
                this.level().playSound(null, this, ParadiseLostSoundEvents.ENTITY_POPOM_HARVEST, SoundSource.PLAYERS, 1.0F, 1.0F);
                int i = furSize == 2 ? 1 + this.random.nextInt(2) : 2 + this.random.nextInt(3);

                for (int j = 0; j < i; j++) {
                    ItemEntity itemEntity = this.spawnAtLocation(serverLevel, ItemRegistry.POPOM_JELLY.get());
                    if (itemEntity != null) {
                        itemEntity.setDeltaMovement(
                                itemEntity.getDeltaMovement()
                                        .add(
                                                ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                                                (this.random.nextFloat() * 0.05F),
                                                ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)
                                        )
                        );
                    }
                }
                this.setFurSize(0);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return EntityRegistry.POPOM.get().create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FurSize", this.entityData.get(FUR_SIZE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFurSize(compound.getInt("FurSize"));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FLOWERS);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatingTimer = Math.max(0, this.eatingTimer - 1);
        }

        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.EAT_GRASS) {
            this.eatingTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    public float getHeadAngle(float delta) {
        if (this.eatingTimer > 4 && this.eatingTimer <= 36) {
            float f = ((float) (this.eatingTimer - 4) - delta) / 32.0F;
            return (float) (Math.PI / 5) + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatingTimer > 0 ? (float) (Math.PI / 5) : this.getXRot() * (float) (Math.PI / 180.0);
        }
    }

    public int getFurSize() {
        return this.entityData.get(FUR_SIZE);
    }

    public void setFurSize(int size) {
        this.entityData.set(FUR_SIZE, size);
        this.updateLootTable();
    }

    public void eat() {
        this.setFurSize(getFurSize() + 1);
    }

    // Mob#getLootTable is final, so the loot table for the current fur size is stored in Mob#lootTable instead.
    private void updateLootTable() {
        ((MobAccessor) this).paradiseLost$setLootTable(Optional.of(switch (this.getFurSize()) {
            case 2 -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_2;
            case 3 -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_3;
            default -> ParadiseLostLootTables.POPOM_JELLY_LEVEL_0;
        }));
    }

    static {
        FUR_SIZE = SynchedEntityData.defineId(PopomEntity.class, EntityDataSerializers.INT);
    }
}
