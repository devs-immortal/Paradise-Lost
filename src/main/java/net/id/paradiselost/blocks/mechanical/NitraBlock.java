package net.id.paradiselost.blocks.mechanical;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class NitraBlock extends Block {

    private static final float BASE_EXPLOSIVE_POWER = 2.5F;

    public NitraBlock(Settings settings) {
        super(settings);
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.isReceivingRedstonePower(pos)) {
                world.scheduleBlockTick(pos, this, 1);
            }

        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }

    }

    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        float sourcePower = explosion.getPower();
        if (sourcePower > 0.5F) {
            ignite(world, pos, sourcePower - 0.5F);
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        ignite(world, pos, BASE_EXPLOSIVE_POWER, null);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
    }

    public static void ignite(World world, BlockPos pos, float power) {
        ignite(world, pos, power, null);
    }

    private static void ignite(World world, BlockPos pos, float power, @Nullable LivingEntity igniter) {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.createExplosion(igniter, pos.getX(), pos.getY() + 0.5D, pos.getZ(), power, World.ExplosionSourceType.TNT);
            serverWorld.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        } else {
            ignite(world, pos, BASE_EXPLOSIVE_POWER, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
                    itemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
                } else {
                    itemStack.decrement(1);
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(item));
            return world.isClient ? ActionResult.SUCCESS : ActionResult.SUCCESS_SERVER;
        }
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (world instanceof ServerWorld serverWorld) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.canModifyAt(serverWorld, blockPos)) {
                ignite(world, blockPos, BASE_EXPLOSIVE_POWER, entity instanceof LivingEntity ? (LivingEntity) entity : null);
                world.removeBlock(blockPos, false);
            }
        }

    }

    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }
}
