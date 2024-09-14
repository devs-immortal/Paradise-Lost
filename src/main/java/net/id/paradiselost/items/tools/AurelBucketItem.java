package net.id.paradiselost.items.tools;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class AurelBucketItem extends Item implements FluidModificationItem {
    private final Fluid containedBlock;

    public AurelBucketItem(Settings settings) {
        super(settings);
        this.containedBlock = Fluids.EMPTY;
    }

    public AurelBucketItem(Fluid containedFluidIn, Settings settings) {
        super(settings);
        this.containedBlock = containedFluidIn;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack currentStack = playerIn.getStackInHand(handIn);
        BlockHitResult hitResult = raycast(worldIn, playerIn, this.containedBlock == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

        if (currentStack.getItem() != ParadiseLostItems.AUREL_WATER_BUCKET && currentStack.getItem() != ParadiseLostItems.AUREL_BUCKET) {
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
                            ((FluidDrainable) hitState.getBlock()).tryDrainFluid(playerIn, worldIn, hitPos, hitState);
                            playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
                            playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, ParadiseLostItems.AUREL_WATER_BUCKET);

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

    @Override
    public ItemStack finishUsing(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            return onBucketContentsConsumed(stack, worldIn, (PlayerEntity) entityLiving);
        }
        return super.finishUsing(stack, worldIn, entityLiving);
    }

    public ItemStack onBucketContentsConsumed(ItemStack stack, World world, PlayerEntity playerEntity) {

        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity entityPlayerMp = (ServerPlayerEntity) playerEntity;
            Criteria.CONSUME_ITEM.trigger(entityPlayerMp, stack);
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!playerEntity.isCreative()) {
            stack.setCount(stack.getCount() - 1);
        }

        if (stack.getItem() == ParadiseLostItems.AUREL_MILK_BUCKET) {
            if (!world.isClient) {
                playerEntity.clearStatusEffects();
            }
        }
        return stack.isEmpty() ? new ItemStack(ParadiseLostItems.AUREL_BUCKET) : stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (stack.getItem() != ParadiseLostItems.AUREL_WATER_BUCKET && stack.getItem() != ParadiseLostItems.AUREL_BUCKET) {
            return UseAction.DRINK;
        }
        return UseAction.NONE;
    }

    protected ItemStack emptyBucket(ItemStack stackIn, PlayerEntity playerIn) {
        return !playerIn.getAbilities().creativeMode ? new ItemStack(ParadiseLostItems.AUREL_BUCKET) : stackIn;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, PlayerEntity player, Item fullBucket) {
        if (player.getAbilities().creativeMode) {
            return emptyBuckets;
        } else {
            emptyBuckets.setCount(emptyBuckets.getCount() - 1);
            if (emptyBuckets.isEmpty()) {
                return new ItemStack(fullBucket);
            } else {
                if (!player.getInventory().insertStack(new ItemStack(fullBucket))) {
                    player.dropItem(new ItemStack(fullBucket), false);
                }
                return emptyBuckets;
            }
        }
    }

    public boolean placeLiquid(PlayerEntity playerIn, World worldIn, BlockPos posIn, BlockHitResult hitResult) {
        if (!(this.containedBlock instanceof FlowableFluid)) {
            return false;
        } else {
            BlockState stateIn = worldIn.getBlockState(posIn);
            boolean flag = !stateIn.isSolid();
            boolean flag1 = stateIn.isReplaceable();

            if (worldIn.isAir(posIn) || flag || flag1 || stateIn.getBlock() instanceof FluidFillable && ((FluidFillable) stateIn.getBlock()).canFillWithFluid(playerIn, worldIn, posIn, stateIn, this.containedBlock)) {
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
                        this.playEmptyingSound(playerIn, worldIn, posIn);
                    }
                } else {
                    if (!worldIn.isClient && (flag || flag1) && !stateIn.isLiquid()) {
                        worldIn.breakBlock(posIn, true);
                    }

                    this.playEmptyingSound(playerIn, worldIn, posIn);
                    worldIn.setBlockState(posIn, this.containedBlock.getDefaultState().getBlockState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                }

                return true;
            } else {
                return hitResult != null && this.placeLiquid(playerIn, worldIn, hitResult.getBlockPos().offset(hitResult.getSide()), null);
            }
        }
    }

    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        FluidModificationItem.super.onEmptied(player, world, stack, pos);
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        FlowableFluid flowableFluid;
        Block block;
        boolean bl;
        FluidFillable fluidFillable;
        BlockState blockState;
        boolean var10000;
        label82: {
            blockState = world.getBlockState(pos);
            block = blockState.getBlock();
            bl = blockState.canBucketPlace(Fluids.WATER);
            if (!blockState.isAir() && !bl) {
                label80: {
                    if (block instanceof FluidFillable) {
                        fluidFillable = (FluidFillable) block;
                        if (fluidFillable.canFillWithFluid(player, world, pos, blockState, Fluids.WATER)) {
                            break label80;
                        }
                    }

                    var10000 = false;
                    break label82;
                }
            }

            var10000 = true;
        }

        boolean bl2 = var10000;
        if (!bl2) {
            return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
        } else if (world.getDimension().ultrawarm()) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
            }

            return true;
        } else {
            if (block instanceof FluidFillable) {
                fluidFillable = (FluidFillable) block;
                fluidFillable.tryFillWithFluid(world, pos, blockState, Fluids.WATER.getStill(false));
                this.playEmptyingSound(player, world, pos);
                return true;
            }

            if (!world.isClient && bl && !blockState.isLiquid()) {
                world.breakBlock(pos, true);
            }

            if (!world.setBlockState(pos, Fluids.WATER.getDefaultState().getBlockState(), 11) && !blockState.getFluidState().isStill()) {
                return false;
            } else {
                this.playEmptyingSound(player, world, pos);
                return true;
            }
        }
    }
}
