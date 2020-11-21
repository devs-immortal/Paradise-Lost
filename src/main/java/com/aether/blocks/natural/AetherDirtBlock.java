package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AetherDirtBlock extends Block {

    public AetherDirtBlock(Settings settings) {
        super(settings.strength(0.3f).sounds(BlockSoundGroup.GRAVEL));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Supplier<Stream<ItemStack>> sup = () -> StreamSupport.stream(player.getItemsHand().spliterator(), false);
        boolean isShovel = sup.get().anyMatch(stack -> ShovelItem.class.isAssignableFrom(stack.getItem().getClass()));
        boolean isHoe = sup.get().anyMatch(stack -> HoeItem.class.isAssignableFrom(stack.getItem().getClass()));

        if (isShovel) {
            world.setBlockState(pos, AetherBlocks.AETHER_DIRT_PATH.getDefaultState());
            return ActionResult.SUCCESS;
        } else if (isHoe) {
            world.setBlockState(pos, AetherBlocks.AETHER_FARMLAND.getDefaultState());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
