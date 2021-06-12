package com.aether.items.tools;

import com.aether.items.AetherItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SkyrootBucket extends Item /*implements CustomPortalFluidProvider*/ {
    private final Fluid containedBlock;

    public SkyrootBucket(Properties settings) {
        super(settings);
        this.containedBlock = Fluids.EMPTY;
    }

    public SkyrootBucket(Fluid containedFluidIn, Properties settings) {
        super(settings);
        this.containedBlock = containedFluidIn;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack currentStack = playerIn.getItemInHand(handIn);
        BlockHitResult hitResult = getPlayerPOVHitResult(worldIn, playerIn, this.containedBlock == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        if (currentStack.getItem() != AetherItems.SKYROOT_WATER_BUCKET && currentStack.getItem() != AetherItems.SKYROOT_BUCKET) {
            playerIn.startUsingItem(handIn);
            return new InteractionResultHolder<>(InteractionResult.PASS, currentStack);
        }

        if (hitResult == null) {
            return new InteractionResultHolder<>(InteractionResult.PASS, currentStack);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hitResult.getBlockPos();

            if (worldIn.mayInteract(playerIn, hitPos) && playerIn.mayUseItemAt(hitPos, hitResult.getDirection(), currentStack)) {
                if (this.containedBlock == Fluids.EMPTY) {
                    BlockState hitState = worldIn.getBlockState(hitPos);

                    if (hitState.getBlock() instanceof BucketPickup) {
                        Fluid fluid = (hitState.getFluidState().getType());

                        if (fluid == Fluids.WATER) {
                            ((BucketPickup) hitState.getBlock()).pickupBlock(worldIn, hitPos, hitState);
                            playerIn.awardStat(Stats.ITEM_USED.get(this));
                            playerIn.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack fillStack = this.fillBucket(currentStack, playerIn, AetherItems.SKYROOT_WATER_BUCKET);

                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, fillStack);
                        }
                    }

                    return new InteractionResultHolder<>(InteractionResult.FAIL, currentStack);
                } else {
                    BlockState hitBlockState = worldIn.getBlockState(hitPos);
                    BlockPos adjustedPos = hitBlockState.getBlock() instanceof LiquidBlockContainer ? hitPos : hitResult.getBlockPos().relative(hitResult.getDirection());

                    this.placeLiquid(playerIn, worldIn, adjustedPos, hitResult);

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

    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player)
            return this.onBucketContentsConsumed(stack, worldIn, (Player) entityLiving);
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    public ItemStack onBucketContentsConsumed(ItemStack stack, Level world, Player playerEntity) {

        if (playerEntity instanceof ServerPlayer) {
            ServerPlayer entityPlayerMp = (ServerPlayer) playerEntity;
            CriteriaTriggers.CONSUME_ITEM.trigger(entityPlayerMp, stack);
            playerEntity.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!playerEntity.isCreative()) stack.setCount(stack.getCount() - 1);

        if (stack.getItem() == AetherItems.SKYROOT_POISON_BUCKET) {
            //player.inflictPoison(500);
        } else if (stack.getItem() == AetherItems.SKYROOT_REMEDY_BUCKET) {
            //player.inflictCure(200);
        } else if (stack.getItem() == AetherItems.SKYROOT_MILK_BUCKET) {
            if (!world.isClientSide) playerEntity.removeAllEffects();
        }
        return stack.isEmpty() ? new ItemStack(AetherItems.SKYROOT_BUCKET) : stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (stack.getItem() != AetherItems.SKYROOT_WATER_BUCKET && stack.getItem() != AetherItems.SKYROOT_BUCKET)
            return UseAnim.DRINK;
        return UseAnim.NONE;
    }

    protected ItemStack emptyBucket(ItemStack stackIn, Player playerIn) {
        return !playerIn.getAbilities().instabuild ? new ItemStack(AetherItems.SKYROOT_BUCKET) : stackIn;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, Player player, Item fullBucket) {
        if (player.getAbilities().instabuild) {
            return emptyBuckets;
        } else {
            emptyBuckets.setCount(emptyBuckets.getCount() - 1);
            if (emptyBuckets.isEmpty()) {
                return new ItemStack(fullBucket);
            } else {
                if (!player.getInventory().add(new ItemStack(fullBucket)))
                    player.drop(new ItemStack(fullBucket), false);
                return emptyBuckets;
            }
        }
    }

    public boolean placeLiquid(Player playerIn, Level worldIn, BlockPos posIn, BlockHitResult hitResult) {
        if (!(this.containedBlock instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState stateIn = worldIn.getBlockState(posIn);
            Material material = stateIn.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = material.isReplaceable();

            if (worldIn.isEmptyBlock(posIn) || flag || flag1 || stateIn.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) stateIn.getBlock()).canPlaceLiquid(worldIn, posIn, stateIn, this.containedBlock)) {
                if (worldIn.dimension().equals(Level.NETHER)) {
                    int i = posIn.getX();
                    int j = posIn.getY();
                    int k = posIn.getZ();
                    worldIn.playSound(playerIn, posIn, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (stateIn.getBlock() instanceof LiquidBlockContainer) {
                    if (((LiquidBlockContainer) stateIn.getBlock()).placeLiquid(worldIn, posIn, stateIn, ((FlowingFluid) this.containedBlock).getSource(false))) {
                        this.playEmptySound(playerIn, worldIn, posIn);
                    }
                } else {
                    if (!worldIn.isClientSide && (flag || flag1) && !material.isLiquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    this.playEmptySound(playerIn, worldIn, posIn);
                    worldIn.setBlock(posIn, this.containedBlock.defaultFluidState().createLegacyBlock(), 11);
                }

                return true;
            } else {
                return hitResult != null && this.placeLiquid(playerIn, worldIn, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
            }
        }
    }

    protected void playEmptySound(Player player, Level worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return stack.getItem() == AetherItems.SKYROOT_REMEDY_BUCKET ? Rarity.RARE : super.getRarity(stack);
    }

//    @Override
//    public Fluid getFluidContent() {
//        return containedBlock;
//    }
//
//    @Override
//    public ItemStack emptyContents(ItemStack itemStack, PlayerEntity playerEntity) {
//        return emptyBucket(itemStack, playerEntity);
//    }
}