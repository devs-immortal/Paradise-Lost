package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.ai.EatAetherGrassGoal;
import com.aether.items.AetherItems;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class SheepuffEntity extends AnimalEntity {

    private static final TrackedData<Boolean> PUFFY = DataTracker.registerData(SheepuffEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Byte> COLOR = DataTracker.registerData(SheepuffEntity.class, TrackedDataHandlerRegistry.BYTE);
    @SuppressWarnings("RedundantCast")
    private static final Map<DyeColor, float[]> COLORS = Maps.newEnumMap((Map<DyeColor, float[]>) Arrays.stream(DyeColor.values()).collect(Collectors.toMap((dyeColor_1) -> dyeColor_1, SheepuffEntity::getDyedColor)));
    private static final Map<DyeColor, ItemConvertible> DROPS;

    static {
        DROPS = Util.make(Maps.<DyeColor, ItemConvertible>newEnumMap(DyeColor.class), (enumMap_1) -> {
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

    public SheepuffEntity(World world) {
        super(AetherEntityTypes.SHEEPUFF, world);
    }

    private static float[] getDyedColor(DyeColor color) {
        if (color == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] floats_1 = color.getColorComponents();

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

    private static CraftingInventory createDyeMixingCraftingInventory(DyeColor parentColor, DyeColor mateColor) {
        CraftingInventory craftingInventory_1 = new CraftingInventory(new ScreenHandler(null, -1) {
            @Override
            public boolean canUse(PlayerEntity playerIn) {
                return false;
            }
        }, 2, 1);

        craftingInventory_1.setStack(0, new ItemStack(DyeItem.byColor(parentColor)));
        craftingInventory_1.setStack(1, new ItemStack(DyeItem.byColor(mateColor)));

        return craftingInventory_1;
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513D);
    }

    @Override
    protected void initGoals() {
        this.eatGrassGoal = new EatAetherGrassGoal(this);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.1D, Ingredient.ofItems(AetherItems.BLUEBERRY), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(5, this.eatGrassGoal);
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void mobTick() {
        this.sheepTimer = this.eatGrassGoal.getTimer();
        super.mobTick();
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        super.tickMovement();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(COLOR, (byte) 0);
        this.dataTracker.startTracking(PUFFY, false);
    }

    @Override
    public Identifier getLootTableId() {
        if (this.isSheared()) {
            return this.getType().getLootTableId();
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
    public void handleStatus(byte id) //handleStatusUpdate
    {
        if (id == 10) this.sheepTimer = 40;
        else super.handleStatus(id);
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

            return 0.62831855F + 0.21991149F * MathHelper.sin(float_2 * 28.7F);
        } else {
            return this.sheepTimer > 0 ? 0.62831855F : this.getPitch() * 0.017453292F;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (heldItem.getItem() == Items.SHEARS && !this.isSheared() && !this.isBaby()) {
            this.dropItems();

            heldItem.damage(1, playerIn, null);
        }

        return super.interactMob(playerIn, handIn);
    }

    public void dropItems() {
        if (!this.world.isClient) {
            if (this.isPuffed()) this.setPuffed(false);
            else this.setSheared(true);

            int int_1 = 1 + this.random.nextInt(3);

            for (int int_2 = 0; int_2 < int_1; ++int_2) {
                ItemEntity item = this.dropItem(DROPS.get(this.getColor()), 1);

                if (item != null)
                    item.setVelocity(this.getVelocity().add(this.random.nextFloat() - this.random.nextFloat() * 0.1F, this.random.nextFloat() * 0.05F, this.random.nextFloat() - this.random.nextFloat() * 0.1F));
            }
        }

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);

        compound.putBoolean("sheared", this.isSheared());
        compound.putByte("color", (byte) this.getColor().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);

        this.setSheared(compound.getBoolean("sheared"));
        this.setColor(DyeColor.byId(compound.getByte("color")));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.dataTracker.get(COLOR) & 15);
    }

    public void setColor(DyeColor dye) {
        byte byte_1 = this.dataTracker.get(COLOR);

        this.dataTracker.set(COLOR, (byte) (byte_1 & 240 | dye.getId() & 15));
    }

    public boolean isSheared() {
        return (this.dataTracker.get(COLOR) & 16) != 0;
    }

    public void setSheared(boolean sheared) {
        byte byte_1 = this.dataTracker.get(COLOR);

        if (sheared) this.dataTracker.set(COLOR, (byte) (byte_1 | 16));
        else this.dataTracker.set(COLOR, (byte) (byte_1 & -17));
    }

    public boolean isPuffed() {
        return this.dataTracker.get(PUFFY);
    }

    public void setPuffed(boolean puffy) {
        this.dataTracker.set(PUFFY, puffy);
    }

    public SheepuffEntity createChild(ServerWorld world, PassiveEntity entity) {
        SheepuffEntity sheepEntity_1 = (SheepuffEntity) entity;
        SheepuffEntity sheepEntity_2 = new SheepuffEntity(this.world);

        sheepEntity_2.setColor(this.getChildColor(this, sheepEntity_1));

        return sheepEntity_2;
    }

    @Override
    public void onEatingGrass() {
        if (!this.isSheared()) this.setPuffed(true);
        else this.setSheared(false);

        if (this.isBaby()) this.growUp(60);
    }

    @Override
    public net.minecraft.entity.EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason SpawnReason, net.minecraft.entity.EntityData entityData, NbtCompound compound) {
        entityData = super.initialize(world, difficulty, SpawnReason, entityData, compound);

        this.setColor(generateDefaultColor(world.getRandom()));

        return entityData;
    }

    private DyeColor getChildColor(AnimalEntity entity, AnimalEntity mate) {
        DyeColor parentColor = ((SheepuffEntity) entity).getColor();
        DyeColor mateColor = ((SheepuffEntity) mate).getColor();

        CraftingInventory craftingInventory_1 = createDyeMixingCraftingInventory(parentColor, mateColor);
        // TODO: ???
        Optional<Item> optionalItem = this.world.getRecipeManager().getAllMatches(RecipeType.CRAFTING, craftingInventory_1, this.world).stream().map((typedRecipe_1) -> typedRecipe_1.craft(craftingInventory_1)).map(ItemStack::getItem).findAny();

        optionalItem = optionalItem.filter(DyeItem.class::isInstance);

        return optionalItem.map(DyeItem.class::cast).map(DyeItem::getColor).orElseGet(() -> this.world.random.nextBoolean() ? parentColor : mateColor);
    }

    @Override
    public float getActiveEyeHeight(EntityPose pos, EntityDimensions size) {
        return 0.95F * size.height;
    }
}