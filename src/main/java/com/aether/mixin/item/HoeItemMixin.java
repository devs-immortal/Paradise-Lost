package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(HoeItem.class)
public class HoeItemMixin extends DiggerItem {

    protected HoeItemMixin(float attackDamage, float attackSpeed, Tier material, Tag<Block> effectiveBlocks, Properties settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Map<Block, BlockState> AETHER_TILLED_BLOCKS = new HashMap<>();
        AETHER_TILLED_BLOCKS.put(AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_FARMLAND.defaultBlockState());
        AETHER_TILLED_BLOCKS.put(AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_FARMLAND.defaultBlockState());
        if (context.getClickedFace() != Direction.DOWN && world.getBlockState(blockPos.above()).isAir()) {
            BlockState blockState = AETHER_TILLED_BLOCKS.get(world.getBlockState(blockPos).getBlock());
            if (blockState != null) {
                Player playerEntity = context.getPlayer();
                world.playSound(playerEntity, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide) {
                    world.setBlock(blockPos, blockState, 11);
                    if (playerEntity != null)
                        context.getItemInHand().hurtAndBreak(1, playerEntity, (p) -> p.broadcastBreakEvent(context.getHand()));
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            }
        }
    }
}
