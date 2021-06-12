package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ShovelItem.class)
public class ShovelItemMixin extends DiggerItem {

    protected ShovelItemMixin(float attackDamage, float attackSpeed, Tier material, Tag<Block> effectiveBlocks, Properties settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Map<Block, BlockState> AETHER_PATH_STATES = new HashMap<>();
        AETHER_PATH_STATES.put(AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_DIRT_PATH.defaultBlockState());
        AETHER_PATH_STATES.put(AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_DIRT_PATH.defaultBlockState());

        if (context.getClickedFace() == Direction.DOWN) {
            cir.setReturnValue(InteractionResult.PASS);
        } else {
            Player playerEntity = context.getPlayer();
            BlockState blockState2 = AETHER_PATH_STATES.get(blockState.getBlock());
            BlockState blockState3 = null;
            if (blockState2 != null && world.getBlockState(blockPos.above()).isAir()) {
                world.playSound(playerEntity, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockState3 = blockState2;
            } else if (blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                if (!world.isClientSide()) world.levelEvent(null, 1009, blockPos, 0);

                CampfireBlock.dowse(playerEntity, world, blockPos, blockState);
                blockState3 = blockState.setValue(CampfireBlock.LIT, false);
            }

            if (blockState3 != null) {
                if (!world.isClientSide) {
                    world.setBlock(blockPos, blockState3, 11);
                    if (playerEntity != null)
                        context.getItemInHand().hurtAndBreak(1, playerEntity, (p) -> p.broadcastBreakEvent(context.getHand()));
                }

                cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            }
        }
    }
}
