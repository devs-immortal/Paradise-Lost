package net.id.paradise_lost.item.tool;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack currentStack = playerIn.getItemInHand(handIn);
        BlockHitResult hitResult = getPlayerPOVHitResult(worldIn, playerIn, this.containedFluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        if (currentStack.getItem() == ItemRegistry.AUREL_MILK_BUCKET.get()) {
            playerIn.startUsingItem(handIn);
            return InteractionResult.PASS;
        }

        if (hitResult == null) {
            return InteractionResult.PASS;
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

                            return InteractionResult.SUCCESS.heldItemTransformedTo(fillStack);
                        } else if (hitState.is(Blocks.POWDER_SNOW)) {
                            ((BucketPickup) hitState.getBlock()).pickupBlock(playerIn, worldIn, hitPos, hitState);
                            playerIn.awardStat(Stats.ITEM_USED.get(this));
                            playerIn.playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, ItemRegistry.AUREL_POWDER_SNOW_BUCKET.get());

                            return InteractionResult.SUCCESS.heldItemTransformedTo(fillStack);
                        }
                    }

                    return InteractionResult.FAIL;
                } else {
                    BlockState hitBlockState = worldIn.getBlockState(hitPos);
                    BlockPos adjustedPos = hitBlockState.getBlock() instanceof LiquidBlockContainer ? hitPos : hitResult.getBlockPos().relative(hitResult.getDirection());

                    this.placeLiquid(playerIn, worldIn, adjustedPos, hitResult);

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResult.SUCCESS.heldItemTransformedTo(this.emptyBucket(currentStack, playerIn));
                }
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected ItemStack emptyBucket(ItemStack stackIn, Player playerIn) {
        return !playerIn.getAbilities().instabuild ? new ItemStack(ItemRegistry.AUREL_BUCKET.get()) : stackIn;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, Player player, Item fullBucket) {
        return ItemUtils.createFilledResult(emptyBuckets, player, new ItemStack(fullBucket));
    }

    public boolean placeLiquid(Player playerIn, Level worldIn, BlockPos posIn, BlockHitResult hitResult) {
        if (this.containedBlock != null) {
            if (worldIn.isInWorldBounds(posIn) && worldIn.getBlockState(posIn).canBeReplaced()) {
                if (!worldIn.isClientSide) {
                    worldIn.setBlock(posIn, this.containedBlock.defaultBlockState(), Block.UPDATE_ALL);
                }
                this.playEmptyingSound(playerIn, worldIn, posIn, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
                return true;
            } else {
                return false;
            }
        } else if (!(this.containedFluid instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState stateIn = worldIn.getBlockState(posIn);
            boolean flag = !stateIn.isSolid();
            boolean flag1 = stateIn.canBeReplaced();

            if (worldIn.isEmptyBlock(posIn) || flag || flag1 || stateIn.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) stateIn.getBlock()).canPlaceLiquid(playerIn, worldIn, posIn, stateIn, this.containedFluid)) {
                if (worldIn.dimension().equals(Level.NETHER)) {
                    int i = posIn.getX();
                    int j = posIn.getY();
                    int k = posIn.getZ();
                    worldIn.playSound(playerIn, posIn, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (stateIn.getBlock() instanceof LiquidBlockContainer) {
                    if (((LiquidBlockContainer) stateIn.getBlock()).placeLiquid(worldIn, posIn, stateIn, ((FlowingFluid) this.containedFluid).getSource(false))) {
                        this.playEmptyingSound(playerIn, worldIn, posIn, SoundEvents.BUCKET_EMPTY);
                    }
                } else {
                    if (!worldIn.isClientSide && (flag || flag1) && !stateIn.liquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    this.playEmptyingSound(playerIn, worldIn, posIn, SoundEvents.BUCKET_EMPTY);
                    worldIn.setBlock(posIn, this.containedFluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);
                }

                return true;
            } else {
                return hitResult != null && this.placeLiquid(playerIn, worldIn, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
            }
        }
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
        Block block;
        boolean bl;
        LiquidBlockContainer fluidFillable;
        BlockState blockState;
        boolean var10000;
        label82: {
            blockState = world.getBlockState(pos);
            block = blockState.getBlock();
            bl = blockState.canBeReplaced(Fluids.WATER);
            if (!blockState.isAir() && !bl) {
                label80: {
                    if (block instanceof LiquidBlockContainer) {
                        fluidFillable = (LiquidBlockContainer) block;
                        if (fluidFillable.canPlaceLiquid(player, world, pos, blockState, Fluids.WATER)) {
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
            return hitResult != null && this.emptyContents(player, world, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
        } else if (world.dimensionType().ultraWarm()) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
            }

            return true;
        } else {
            if (block instanceof LiquidBlockContainer) {
                fluidFillable = (LiquidBlockContainer) block;
                fluidFillable.placeLiquid(world, pos, blockState, Fluids.WATER.getSource(false));
                this.playEmptyingSound(player, world, pos, SoundEvents.BUCKET_EMPTY);
                return true;
            }

            if (!world.isClientSide && bl && !blockState.liquid()) {
                world.destroyBlock(pos, true);
            }

            if (!world.setBlock(pos, Fluids.WATER.defaultFluidState().createLegacyBlock(), 11) && !blockState.getFluidState().isSource()) {
                return false;
            } else {
                this.playEmptyingSound(player, world, pos, SoundEvents.BUCKET_EMPTY);
                return true;
            }
        }
    }
}
