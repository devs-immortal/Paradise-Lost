package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(AxeItem.class)
public class AxeItemMixin extends DiggerItem {

    protected AxeItemMixin(float attackDamage, float attackSpeed, Tier material, Tag<Block> effectiveBlocks, Properties settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Map<Block, Block> AETHER_STRIPPED_BLOCKS = new HashMap<>();
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.CRYSTAL_LOG, AetherBlocks.STRIPPED_CRYSTAL_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.GOLDEN_OAK_LOG, AetherBlocks.STRIPPED_GOLDEN_OAK_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.SKYROOT_LOG, AetherBlocks.STRIPPED_SKYROOT_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.WISTERIA_LOG, AetherBlocks.STRIPPED_WISTERIA_LOG);

        Block block = AETHER_STRIPPED_BLOCKS.get(blockState.getBlock());

        if (block != null) {
            Player playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockPos, block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)), 11);
                if (playerEntity != null)
                    context.getItemInHand().hurtAndBreak(1, playerEntity, (p) -> p.broadcastBreakEvent(context.getHand()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}
