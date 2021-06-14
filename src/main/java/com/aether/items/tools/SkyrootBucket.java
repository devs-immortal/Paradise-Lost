package com.aether.items.tools;

import com.aether.items.AetherItems;
import net.kyrptonaught.customportalapi.util.CustomPortalFluidProvider;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SkyrootBucket extends Item implements CustomPortalFluidProvider {
    private final Fluid containedBlock;

    public SkyrootBucket(Settings settings) {
        super(settings);
        this.containedBlock = Fluids.EMPTY;
    }

    public SkyrootBucket(Fluid containedFluidIn, Settings settings) {
        super(settings);
        this.containedBlock = containedFluidIn;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack currentStack = playerIn.getStackInHand(handIn);
        BlockHitResult hitResult = raycast(worldIn, playerIn, this.containedBlock == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

        if (currentStack.getItem() != AetherItems.SKYROOT_WATER_BUCKET && currentStack.getItem() != AetherItems.SKYROOT_BUCKET) {
            playerIn.setCurrentHand(handIn);
            return new TypedActionResult<>(ActionResult.PASS, currentStack);
        }

        if (hitResult == null) {
            return new TypedActionResult<>(ActionResult.PASS, currentStack);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hitResult.getBlockPos();

            if (worldIn.canPlayerModifyAt(playerIn, hitPos) && playerIn.canPlaceOn(hitPos, hitResult.getSide(), currentStack)) {
                if (this.containedBlock == Fluids.EMPTY) {
                    BlockState hitState = worldIn.getBlockState(hitPos);

                    if (hitState.getBlock() instanceof FluidDrainable) {
                        Fluid fluid = (hitState.getFluidState().getFluid());

                        if (fluid == Fluids.WATER) {
                            ((FluidDrainable) hitState.getBlock()).tryDrainFluid(worldIn, hitPos, hitState);
                            playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
                            playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, AetherItems.SKYROOT_WATER_BUCKET);

                            return new TypedActionResult<>(ActionResult.SUCCESS, fillStack);
                        }
                    }

                    return new TypedActionResult<>(ActionResult.FAIL, currentStack);
                } else {
                    BlockState hitBlockState = worldIn.getBlockState(hitPos);
                    BlockPos adjustedPos = hitBlockState.getBlock() instanceof FluidFillable ? hitPos : hitResult.getBlockPos().offset(hitResult.getSide());

                    this.placeLiquid(playerIn, worldIn, adjustedPos, hitResult);

                    playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
                    return new TypedActionResult<>(ActionResult.SUCCESS, this.emptyBucket(currentStack, playerIn));
                }
            } else {
                return new TypedActionResult<>(ActionResult.FAIL, currentStack);
            }
        } else {
            return new TypedActionResult<>(ActionResult.PASS, currentStack);
        }
    }

    public ItemStack finishUsing(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity)
            return this.onBucketContentsConsumed(stack, worldIn, (PlayerEntity) entityLiving);
        return super.finishUsing(stack, worldIn, entityLiving);
    }

    public ItemStack onBucketContentsConsumed(ItemStack stack, World world, PlayerEntity playerEntity) {

        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity entityPlayerMp = (ServerPlayerEntity) playerEntity;
            Criteria.CONSUME_ITEM.trigger(entityPlayerMp, stack);
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!playerEntity.isCreative()) stack.setCount(stack.getCount() - 1);

        if (stack.getItem() == AetherItems.SKYROOT_POISON_BUCKET) {
            //player.inflictPoison(500);
        } else if (stack.getItem() == AetherItems.SKYROOT_REMEDY_BUCKET) {
            //player.inflictCure(200);
        } else if (stack.getItem() == AetherItems.SKYROOT_MILK_BUCKET) {
            if (!world.isClient) playerEntity.clearStatusEffects();
        }
        return stack.isEmpty() ? new ItemStack(AetherItems.SKYROOT_BUCKET) : stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (stack.getItem() != AetherItems.SKYROOT_WATER_BUCKET && stack.getItem() != AetherItems.SKYROOT_BUCKET)
            return UseAction.DRINK;
        return UseAction.NONE;
    }

    protected ItemStack emptyBucket(ItemStack stackIn, PlayerEntity playerIn) {
        return !playerIn.getAbilities().creativeMode ? new ItemStack(AetherItems.SKYROOT_BUCKET) : stackIn;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, PlayerEntity player, Item fullBucket) {
        if (player.getAbilities().creativeMode) {
            return emptyBuckets;
        } else {
            emptyBuckets.setCount(emptyBuckets.getCount() - 1);
            if (emptyBuckets.isEmpty()) {
                return new ItemStack(fullBucket);
            } else {
                if (!player.getInventory().insertStack(new ItemStack(fullBucket)))
                    player.dropItem(new ItemStack(fullBucket), false);
                return emptyBuckets;
            }
        }
    }

    public boolean placeLiquid(PlayerEntity playerIn, World worldIn, BlockPos posIn, BlockHitResult hitResult) {
        if (!(this.containedBlock instanceof FlowableFluid)) {
            return false;
        } else {
            BlockState stateIn = worldIn.getBlockState(posIn);
            Material material = stateIn.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = material.isReplaceable();

            if (worldIn.isAir(posIn) || flag || flag1 || stateIn.getBlock() instanceof FluidFillable && ((FluidFillable) stateIn.getBlock()).canFillWithFluid(worldIn, posIn, stateIn, this.containedBlock)) {
                if (worldIn.getRegistryKey().equals(World.NETHER)) {
                    int i = posIn.getX();
                    int j = posIn.getY();
                    int k = posIn.getZ();
                    worldIn.playSound(playerIn, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (stateIn.getBlock() instanceof FluidFillable) {
                    if (((FluidFillable) stateIn.getBlock()).tryFillWithFluid(worldIn, posIn, stateIn, ((FlowableFluid) this.containedBlock).getStill(false))) {
                        this.playEmptySound(playerIn, worldIn, posIn);
                    }
                } else {
                    if (!worldIn.isClient && (flag || flag1) && !material.isLiquid()) {
                        worldIn.breakBlock(posIn, true);
                    }

                    this.playEmptySound(playerIn, worldIn, posIn);
                    worldIn.setBlockState(posIn, this.containedBlock.getDefaultState().getBlockState(), 11);
                }

                return true;
            } else {
                return hitResult != null && this.placeLiquid(playerIn, worldIn, hitResult.getBlockPos().offset(hitResult.getSide()), null);
            }
        }
    }

    protected void playEmptySound(PlayerEntity player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return stack.getItem() == AetherItems.SKYROOT_REMEDY_BUCKET ? Rarity.RARE : super.getRarity(stack);
    }

    @Override
    public Fluid getFluidContent() {
        return containedBlock;
    }

    @Override
    public ItemStack emptyContents(ItemStack itemStack, PlayerEntity playerEntity) {
        return emptyBucket(itemStack, playerEntity);
    }
}