package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.ai.EatAetherGrassGoal;
import com.aether.items.AetherItems;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class SheepuffEntity extends Animal {

    private static final EntityDataAccessor<Boolean> PUFFY = SynchedEntityData.defineId(SheepuffEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(SheepuffEntity.class, EntityDataSerializers.BYTE);
    @SuppressWarnings("RedundantCast")
    private static final Map<DyeColor, float[]> COLORS = Maps.newEnumMap((Map<DyeColor, float[]>) Arrays.stream(DyeColor.values()).collect(Collectors.toMap((dyeColor_1) -> dyeColor_1, SheepuffEntity::getDyedColor)));
    private static final Map<DyeColor, ItemLike> DROPS;

    static {
        DROPS = Util.make(Maps.<DyeColor, ItemLike>newEnumMap(DyeColor.class), (enumMap_1) -> {
            enumMap_1.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
            enumMap_1.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
            enumMap_1.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
            enumMap_1.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
            enumMap_1.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
            enumMap_1.put(DyeColor.LIME, Blocks.LIME_WOOL);
            enumMap_1.put(DyeColor.PINK, Blocks.PINK_WOOL);
            enumMap_1.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
            enumMap_1.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
            enumMap_1.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
            enumMap_1.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
            enumMap_1.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
            enumMap_1.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
            enumMap_1.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
            enumMap_1.put(DyeColor.RED, Blocks.RED_WOOL);
            enumMap_1.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
        });
    }

    private EatAetherGrassGoal eatGrassGoal;
    private int sheepTimer;

    public SheepuffEntity(Level world) {
        super(AetherEntityTypes.SHEEPUFF, world);
    }

    private static float[] getDyedColor(DyeColor color) {
        if (color == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] floats_1 = color.getTextureDiffuseColors();

            return new float[]{floats_1[0] * 0.75F, floats_1[1] * 0.75F, floats_1[2] * 0.75F};
        }
    }

    @Environment(EnvType.CLIENT)
    public static float[] getRgbColor(DyeColor dyeColor_1) {
        return COLORS.get(dyeColor_1);
    }

    public static DyeColor generateDefaultColor(Random random) {
        int int_1 = random.nextInt(100);

        if (int_1 < 5) {
            return DyeColor.BLACK;
        } else if (int_1 < 10) {
            return DyeColor.GRAY;
        } else if (int_1 < 15) {
            return DyeColor.LIGHT_GRAY;
        } else if (int_1 < 18) {
            return DyeColor.BROWN;
        } else {
            return random.nextInt(500) == 0 ? DyeColor.PINK : DyeColor.WHITE;
        }
    }

    private static CraftingContainer createDyeMixingCraftingInventory(DyeColor parentColor, DyeColor mateColor) {
        CraftingContainer craftingInventory_1 = new CraftingContainer(new AbstractContainerMenu(null, -1) {
            @Override
            public boolean stillValid(Player playerIn) {
                return false;
            }
        }, 2, 1);

        craftingInventory_1.setItem(0, new ItemStack(DyeItem.byColor(parentColor)));
        craftingInventory_1.setItem(1, new ItemStack(DyeItem.byColor(mateColor)));

        return craftingInventory_1;
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D);
    }

    @Override
    protected void registerGoals() {
        this.eatGrassGoal = new EatAetherGrassGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(AetherItems.BLUEBERRY), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, this.eatGrassGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        this.sheepTimer = this.eatGrassGoal.getTimer();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        super.aiStep();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(COLOR, (byte) 0);
        this.entityData.define(PUFFY, false);
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        if (this.isSheared()) {
            return this.getType().getDefaultLootTable();
        } else {
            return null;
            /*try {
                switch (this.getColor()) {
                    case WHITE:
                    default:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_WHITE;
                    case ORANGE:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_ORANGE;
                    case MAGENTA:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_MAGENTA;
                    case LIGHT_BLUE:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_LIGHT_BLUE;
                    case YELLOW:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_YELLOW;
                    case LIME:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_LIME;
                    case PINK:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_PINK;
                    case GRAY:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_GRAY;
                    case LIGHT_GRAY:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_LIGHT_GRAY;
                    case CYAN:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_CYAN;
                    case PURPLE:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_PURPLE;
                    case BLUE:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_BLUE;
                    case BROWN:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_BROWN;
                    case GREEN:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_GREEN;
                    case RED:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_RED;
                    case BLACK:
                        return AetherLootTableList.ENTITIES_SHEEPUFF_BLACK;
                }
            } catch (Exception ex) {
                // This only exists just to ensure ENTITIES_SHEEPUFF is in use
                return AetherLootTableList.ENTITIES_SHEEPUFF;
            }*/
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handleEntityEvent(byte id) //handleStatusUpdate
    {
        if (id == 10) this.sheepTimer = 40;
        else super.handleEntityEvent(id);
    }

    @Environment(EnvType.CLIENT)
    public float getHeadRotationPointY(float float_1) {
        if (this.sheepTimer <= 0) return 0.0F;
        else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) return 1.0F;
        else
            return this.sheepTimer < 4 ? ((float) this.sheepTimer - float_1) / 4.0F : -((float) (this.sheepTimer - 40) - float_1) / 4.0F;
    }

    @Environment(EnvType.CLIENT)
    public float getHeadRotationAngleX(float float_1) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float float_2 = ((float) (this.sheepTimer - 4) - float_1) / 32.0F;

            return 0.62831855F + 0.21991149F * Mth.sin(float_2 * 28.7F);
        } else {
            return this.sheepTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Override
    public InteractionResult mobInteract(Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (heldItem.getItem() == Items.SHEARS && !this.isSheared() && !this.isBaby()) {
            this.dropItems();

            heldItem.hurtAndBreak(1, playerIn, null);
        }

        return super.mobInteract(playerIn, handIn);
    }

    public void dropItems() {
        if (!this.level.isClientSide) {
            if (this.isPuffed()) this.setPuffed(false);
            else this.setSheared(true);

            int int_1 = 1 + this.random.nextInt(3);

            for (int int_2 = 0; int_2 < int_1; ++int_2) {
                ItemEntity item = this.spawnAtLocation(DROPS.get(this.getColor()), 1);

                if (item != null)
                    item.setDeltaMovement(this.getDeltaMovement().add(this.random.nextFloat() - this.random.nextFloat() * 0.1F, this.random.nextFloat() * 0.05F, this.random.nextFloat() - this.random.nextFloat() * 0.1F));
            }
        }

        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("sheared", this.isSheared());
        compound.putByte("color", (byte) this.getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setSheared(compound.getBoolean("sheared"));
        this.setColor(DyeColor.byId(compound.getByte("color")));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(COLOR) & 15);
    }

    public void setColor(DyeColor dye) {
        byte byte_1 = this.entityData.get(COLOR);

        this.entityData.set(COLOR, (byte) (byte_1 & 240 | dye.getId() & 15));
    }

    public boolean isSheared() {
        return (this.entityData.get(COLOR) & 16) != 0;
    }

    public void setSheared(boolean sheared) {
        byte byte_1 = this.entityData.get(COLOR);

        if (sheared) this.entityData.set(COLOR, (byte) (byte_1 | 16));
        else this.entityData.set(COLOR, (byte) (byte_1 & -17));
    }

    public boolean isPuffed() {
        return this.entityData.get(PUFFY);
    }

    public void setPuffed(boolean puffy) {
        this.entityData.set(PUFFY, puffy);
    }

    public SheepuffEntity getBreedOffspring(ServerLevel world, AgeableMob entity) {
        SheepuffEntity sheepEntity_1 = (SheepuffEntity) entity;
        SheepuffEntity sheepEntity_2 = new SheepuffEntity(this.level);

        sheepEntity_2.setColor(this.getChildColor(this, sheepEntity_1));

        return sheepEntity_2;
    }

    @Override
    public void ate() {
        if (!this.isSheared()) this.setPuffed(true);
        else this.setSheared(false);

        if (this.isBaby()) this.ageUp(60);
    }

    @Override
    public net.minecraft.world.entity.SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType SpawnReason, net.minecraft.world.entity.SpawnGroupData entityData, CompoundTag compound) {
        entityData = super.finalizeSpawn(world, difficulty, SpawnReason, entityData, compound);

        this.setColor(generateDefaultColor(world.getRandom()));

        return entityData;
    }

    private DyeColor getChildColor(Animal entity, Animal mate) {
        DyeColor parentColor = ((SheepuffEntity) entity).getColor();
        DyeColor mateColor = ((SheepuffEntity) mate).getColor();

        CraftingContainer craftingInventory_1 = createDyeMixingCraftingInventory(parentColor, mateColor);
        // TODO: ???
        Optional<Item> optionalItem = this.level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, craftingInventory_1, this.level).stream().map((typedRecipe_1) -> typedRecipe_1.assemble(craftingInventory_1)).map(ItemStack::getItem).findAny();

        optionalItem = optionalItem.filter(DyeItem.class::isInstance);

        return optionalItem.map(DyeItem.class::cast).map(DyeItem::getDyeColor).orElseGet(() -> this.level.random.nextBoolean() ? parentColor : mateColor);
    }

    @Override
    public float getStandingEyeHeight(Pose pos, EntityDimensions size) {
        return 0.95F * size.height;
    }
}