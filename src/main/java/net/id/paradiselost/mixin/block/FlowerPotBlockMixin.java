package net.id.paradiselost.mixin.block;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.decorative.CalciteFlowerPotBlock;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {

    @Final
    @Shadow
    private static Map<Block, Block> CONTENT_TO_POTTED;

    @Shadow
    protected abstract boolean isEmpty();

    public FlowerPotBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Block content, Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(CalciteFlowerPotBlock.IS_CALCITE, false));
    }

    @Inject(method = "onUseWithItem", at = @At(value = "HEAD"), cancellable = true)
    public void onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if (state.isOf(ParadiseLostBlocks.CALCITE_FLOWER_POT)) {
            BlockState blockState = (stack.getItem() instanceof BlockItem blockItem
                    ? CONTENT_TO_POTTED.getOrDefault(blockItem.getBlock(), Blocks.AIR)
                    : Blocks.AIR)
                    .getDefaultState();
            if (blockState.isAir()) {
                cir.setReturnValue(ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
            } else if (!this.isEmpty()) {
                cir.setReturnValue(ItemActionResult.CONSUME);
            } else {
                world.setBlockState(pos, blockState.with(CalciteFlowerPotBlock.IS_CALCITE, true), Block.NOTIFY_ALL);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.incrementStat(Stats.POT_FLOWER);
                stack.decrementUnlessCreative(1, player);
                cir.setReturnValue(ItemActionResult.success(world.isClient));
            }
            cir.cancel();
        }
    }

    @Inject(method = "onUse", at = @At(value = "RETURN"))
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!this.isEmpty() && state.get(CalciteFlowerPotBlock.IS_CALCITE)) {
            world.setBlockState(pos, ParadiseLostBlocks.CALCITE_FLOWER_POT.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        var drops = super.getDroppedStacks(state, builder);
        if (state.get(CalciteFlowerPotBlock.IS_CALCITE)) {
            return drops.stream().map(itemStack -> {
                if (itemStack.isOf(Items.FLOWER_POT)) {
                    return new ItemStack(ParadiseLostItems.CALCITE_FLOWER_POT, itemStack.getCount());
                } else {
                    return itemStack;
                }
            }).toList();
        } else {
            return drops;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CalciteFlowerPotBlock.IS_CALCITE);
    }

}
