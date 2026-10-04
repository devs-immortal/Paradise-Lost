package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.decorative.CalciteFlowerPotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {

    private static final Map<Block, Block> POTTED_BY_PLANT = new IdentityHashMap<>();

    @Shadow
    protected abstract boolean isEmpty();

    public FlowerPotBlockMixin(Properties settings) {
        super(settings);
    }

    private static boolean hasCalcite(BlockState state) {
        return state.hasProperty(CalciteFlowerPotBlock.IS_CALCITE);
    }

    private static Block pottedFor(Block plant) {
        return POTTED_BY_PLANT.getOrDefault(plant, Blocks.AIR);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Block content, Properties settings, CallbackInfo ci) {
        if (content != Blocks.AIR) {
            POTTED_BY_PLANT.put(content, (Block) (Object) this);
        }
        BlockState defaultState = this.defaultBlockState();
        if (hasCalcite(defaultState)) {
            this.registerDefaultState(defaultState.setValue(CalciteFlowerPotBlock.IS_CALCITE, false));
        }
    }

    @Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
    public void onUseWithItem(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.is(BlockRegistry.CALCITE_FLOWER_POT.get())) {
            BlockState blockState = (stack.getItem() instanceof BlockItem blockItem
                    ? pottedFor(blockItem.getBlock())
                    : Blocks.AIR)
                    .defaultBlockState();
            if (blockState.isAir()) {
                cir.setReturnValue(InteractionResult.TRY_WITH_EMPTY_HAND);
            } else if (!this.isEmpty()) {
                cir.setReturnValue(InteractionResult.CONSUME);
            } else if (!hasCalcite(blockState)) {

                cir.setReturnValue(InteractionResult.TRY_WITH_EMPTY_HAND);
            } else {
                world.setBlock(pos, blockState.setValue(CalciteFlowerPotBlock.IS_CALCITE, true), Block.UPDATE_ALL);
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.POT_FLOWER);
                stack.consume(1, player);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            cir.cancel();
        }
    }

    @Inject(method = "useWithoutItem", at = @At(value = "RETURN"))
    public void onUse(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (!this.isEmpty() && hasCalcite(state) && state.getValue(CalciteFlowerPotBlock.IS_CALCITE)) {
            world.setBlock(pos, BlockRegistry.CALCITE_FLOWER_POT.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        var drops = super.getDrops(state, builder);
        if (hasCalcite(state) && state.getValue(CalciteFlowerPotBlock.IS_CALCITE)) {
            return drops.stream().map(itemStack -> {
                if (itemStack.is(Items.FLOWER_POT)) {
                    return new ItemStack(BlockRegistry.CALCITE_FLOWER_POT.get(), itemStack.getCount());
                } else if (itemStack.is(Items.BRICK)) {
                    return new ItemStack(Items.CALCITE, itemStack.getCount());
                } else {
                    return itemStack;
                }
            }).toList();
        } else {
            return drops;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CalciteFlowerPotBlock.IS_CALCITE);
    }

}
