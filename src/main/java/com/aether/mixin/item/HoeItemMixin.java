package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Map<Block, BlockState> AETHER_TILLED_BLOCKS = new HashMap<>();
        AETHER_TILLED_BLOCKS.put(AetherBlocks.AETHER_GRASS, AetherBlocks.AETHER_FARMLAND.getDefaultState());
        AETHER_TILLED_BLOCKS.put(AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_FARMLAND.getDefaultState());
        AETHER_TILLED_BLOCKS.put(AetherBlocks.AETHER_DIRT_PATH, AetherBlocks.AETHER_FARMLAND.getDefaultState());

        if (context.getSide() != Direction.DOWN && world.getBlockState(blockPos.up()).isAir()) {
            BlockState blockState = (BlockState)AETHER_TILLED_BLOCKS.get(world.getBlockState(blockPos).getBlock());
            if (blockState != null) {
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState, 11);
                    if (playerEntity != null)
                        context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
                }
                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}
