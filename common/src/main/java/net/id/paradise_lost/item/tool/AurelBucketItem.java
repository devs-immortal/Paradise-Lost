package net.id.paradise_lost.item.tool;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class AurelBucketItem extends Item implements DispensibleContainerItem {
    private final Fluid containedFluid;
    private final Block containedBlock;

    public AurelBucketItem(Item.Properties settings) {
        super(settings);
        this.containedFluid = Fluids.EMPTY;
        this.containedBlock = null;
    }

    public AurelBucketItem(Fluid containedFluidIn, Item.Properties settings) {
        super(settings);
        this.containedFluid = containedFluidIn;
        this.containedBlock = null;
    }

    public AurelBucketItem(Block containedBlockIn, Item.Properties settings) {
        super(settings);
        this.containedFluid = null;
        this.containedBlock = containedBlockIn;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack currentStack = playerIn.getItemInHand(handIn);
        BlockHitResult hitResult = getPlayerPOVHitResult(worldIn, playerIn, this.containedFluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        if (currentStack.getItem() == ItemRegistry.AUREL_MILK_BUCKET.get()) {
            playerIn.startUsingItem(handIn);
            return new InteractionResultHolder<>(InteractionResult.PASS, currentStack);
        }

        if (hitResult == null) {
            return new InteractionResultHolder<>(InteractionResult.PASS, currentStack);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hitResult.getBlockPos();

            if (worldIn.mayInteract(playerIn, hitPos) && playerIn.mayUseItemAt(hitPos, hitResult.getDirection(), currentStack)) {
                if (this.containedFluid == Fluids.EMPTY) {
                    BlockState hitState = worldIn.getBlockState(hitPos);

                    if (hitState.getBlock() instanceof BucketPickup) {
                        if (hitState.getFluidState().getType() == Fluids.WATER) {
                            ((BucketPickup) hitState.getBlock()).pickupBlock(playerIn, worldIn, hitPos, hitState);
                            playerIn.awardStat(Stats.ITEM_USED.get(this));
                            playerIn.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, ItemRegistry.AUREL_WATER_BUCKET.get());

                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, fillStack);
                        } else if (hitState.is(Blocks.POWDER_SNOW)) {
                            ((BucketPickup) hitState.getBlock()).pickupBlock(playerIn, worldIn, hitPos, hitState);
                            playerIn.awardStat(Stats.ITEM_USED.get(this));
                            playerIn.playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, ItemRegistry.AUREL_POWDER_SNOW_BUCKET.get());

                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, fillStack);
                        }
                    }

                    return new InteractionResultHolder<>(InteractionResult.FAIL, currentStack);
                } else {
                    BlockState hitBlockState = worldIn.getBlockState(hitPos);
                    BlockPos adjustedPos = hitBlockState.getBlock() instanceof LiquidBlockContainer && this.containedFluid == Fluids.WATER ? hitPos : hitPos.relative(hitResult.getDirection());

                    if (!this.emptyContents(playerIn, worldIn, adjustedPos, hitResult)) {
                        return new InteractionResultHolder<>(InteractionResult.FAIL, currentStack);
                    }

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, this.emptyBucket(currentStack, playerIn));
                }
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, currentStack);
            }
        } else {
            return new InteractionResultHolder<>(InteractionResult.PASS, currentStack);
        }
    }

    protected ItemStack emptyBucket(ItemStack stackIn, Player playerIn) {
        return !playerIn.getAbilities().instabuild ? new ItemStack(ItemRegistry.AUREL_BUCKET.get()) : stackIn;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, Player player, Item fullBucket) {
        return ItemUtils.createFilledResult(emptyBuckets, player, new ItemStack(fullBucket));
    }

    protected void playEmptyingSound(@Nullable Player player, LevelAccessor world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level world, ItemStack stack, BlockPos pos) {

    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (this.containedBlock != null) {
            if (!world.isInWorldBounds(pos) || !world.getBlockState(pos).canBeReplaced()) {
                return false;
            }
            if (!world.isClientSide) {
                world.setBlock(pos, this.containedBlock.defaultBlockState(), Block.UPDATE_ALL);
            }
            this.playEmptyingSound(player, world, pos, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
            return true;
        }

        if (!(this.containedFluid instanceof FlowingFluid flowingFluid)) {
            return false;
        }

        BlockState state = world.getBlockState(pos);
        boolean replaceable = state.canBeReplaced(flowingFluid);
        boolean fillable = state.getBlock() instanceof LiquidBlockContainer container && container.canPlaceLiquid(player, world, pos, state, flowingFluid);

        if (!state.isAir() && !replaceable && !fillable) {
            return hitResult != null && this.emptyContents(player, world, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
        }

        if (world.dimensionType().ultraWarm() && flowingFluid.is(FluidTags.WATER)) {
            world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            for (int i = 0; i < 8; ++i) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
            }
            return true;
        }

        if (fillable) {
            ((LiquidBlockContainer) state.getBlock()).placeLiquid(world, pos, state, flowingFluid.getSource(false));
            this.playEmptyingSound(player, world, pos, SoundEvents.BUCKET_EMPTY);
            return true;
        }

        if (!world.isClientSide && replaceable && !state.liquid()) {
            world.destroyBlock(pos, true);
        }

        if (!world.setBlock(pos, flowingFluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL_IMMEDIATE) && !state.getFluidState().isSource()) {
            return false;
        }

        this.playEmptyingSound(player, world, pos, SoundEvents.BUCKET_EMPTY);
        return true;
    }
}
