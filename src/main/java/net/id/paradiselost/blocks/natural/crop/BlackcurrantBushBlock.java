package net.id.paradiselost.blocks.natural.crop;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlackcurrantBushBlock extends SweetBerryBushBlock {

    public BlackcurrantBushBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.get(AGE) > 0 && entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.900000011920929D, 0.75D, 0.900000011920929D));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = state.get(AGE);
        boolean mature = i == 3;
        if (!mature && player.getStackInHand(hand).getItem() == Items.BONE_MEAL) {
            return ActionResult.PASS;
        } else if (i > 1) {
            tryPickBerries(world, pos, state);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.FAIL;
        }
    }

    private void tryPickBerries(World world, BlockPos pos, BlockState state) {
        boolean mature = state.get(AGE) == 3;
        BlockState floor = world.getBlockState(pos.down());
        double mod = floor.isOf(ParadiseLostBlocks.FARMLAND) ? 1.5 : 1;
        int berries = world.random.nextInt(2) + 1;
        dropStack(world, pos, new ItemStack(ParadiseLostItems.BLACKCURRANT, (int) (berries + (mature ? 1 : 0) * mod)));
        world.playSound(null, pos, ParadiseLostSoundEvents.BLOCK_BLACKCURRANT_BUSH_PICK_BLUEBERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
        world.setBlockState(pos, state.with(AGE, 1), Block.NOTIFY_LISTENERS);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(this.asItem());
    }
}
