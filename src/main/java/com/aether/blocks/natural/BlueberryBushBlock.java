package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlueberryBushBlock extends SweetBerryBushBlock {

    public BlueberryBushBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (state.getValue(AGE) > 0 && entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3(0.900000011920929D, 0.75D, 0.900000011920929D));
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int i = state.getValue(AGE);
        boolean mature = i == 3;
        BlockState floor = world.getBlockState(pos.below());
        double mod = floor.is(AetherBlocks.AETHER_ENCHANTED_GRASS) ? 2 : floor.is(AetherBlocks.AETHER_FARMLAND) ? 1.5 : 1;
        if (!mature && player.getItemInHand(hand).getItem() == Items.BONE_MEAL) {
            return InteractionResult.PASS;
        } else if (i > 1) {
            int berries = world.random.nextInt(2) + 1;
            popResource(world, pos, new ItemStack(AetherItems.BLUEBERRY, (int) (berries + (mature ? 1 : 0) * mod)));
            world.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            world.setBlock(pos, state.setValue(AGE, 1), 2);
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(this.asItem());
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(AetherBlocks.AETHER_GRASS_BLOCK) || floor.is(AetherBlocks.AETHER_ENCHANTED_GRASS) || floor.is(AetherBlocks.AETHER_FARMLAND) || floor.is(AetherBlocks.AETHER_DIRT) || super.mayPlaceOn(floor, world, pos);
    }
}