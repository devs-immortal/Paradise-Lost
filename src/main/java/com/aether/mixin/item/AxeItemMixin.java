package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.utils.AetherTiers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(AxeItem.class)
public class AxeItemMixin extends MiningToolItem {

    protected AxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (this.getMaterial() == AetherTiers.Gravitite.getDefaultTier() && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
            cir.setReturnValue(ActionResult.SUCCESS);
        }

        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Map<Block, Block> AETHER_STRIPPED_BLOCKS = new HashMap<>();
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.CRYSTAL_LOG, AetherBlocks.STRIPPED_CRYSTAL_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.GOLDEN_OAK_LOG, AetherBlocks.STRIPPED_GOLDEN_OAK_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.SKYROOT_LOG, AetherBlocks.STRIPPED_SKYROOT_LOG);
        Block block = AETHER_STRIPPED_BLOCKS.get(blockState.getBlock());

        if (block != null) {
            PlayerEntity playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClient) {
                world.setBlockState(blockPos, block.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), 11);
                if (playerEntity != null)
                    context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
