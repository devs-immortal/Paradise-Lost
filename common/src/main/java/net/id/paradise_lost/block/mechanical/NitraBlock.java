package net.id.paradise_lost.block.mechanical;

import net.id.paradise_lost.world.ExplosionExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NitraBlock extends Block {

    private static final float BASE_EXPLOSIVE_POWER = 2.5F;

    public NitraBlock(Properties settings) {
        super(settings);
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            if (world.hasNeighborSignal(pos)) {
                world.scheduleTick(pos, this, 1);
            }

        }
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.hasNeighborSignal(pos)) {
            world.scheduleTick(pos, this, 1);
        }

    }

    public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
        float sourcePower = ((ExplosionExtensions) explosion).getPower();
        if (!world.isClientSide && sourcePower > 0.5F) {
            ignite(world, pos, sourcePower - 0.5F);
        }
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        ignite(world, pos, BASE_EXPLOSIVE_POWER, null);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        world.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        world.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, random.nextLong());
    }

    public static void ignite(Level world, BlockPos pos, float power) {
        ignite(world, pos, power, null);
    }

    private static void ignite(Level world, BlockPos pos, float power, @Nullable LivingEntity igniter) {
        Explosion explosion = new Explosion(world, igniter, pos.getX(), pos.getY() + 0.5D, pos.getZ(), power, false, Explosion.BlockInteraction.DESTROY);
        if (!world.isClientSide) {
            explosion.explode();
            world.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
        ((ExplosionExtensions) explosion).affectWorld(true, SoundEvents.GENERIC_EXPLODE.value());
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
            return super.useItemOn(stack, state, world, pos, player, hand, hit);
        } else {
            ignite(world, pos, BASE_EXPLOSIVE_POWER, player);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                } else {
                    itemStack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }
    }

    public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!world.isClientSide) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(world, blockPos)) {
                ignite(world, blockPos, BASE_EXPLOSIVE_POWER, entity instanceof LivingEntity ? (LivingEntity) entity : null);
                world.removeBlock(blockPos, false);
            }
        }

    }

    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }
}
