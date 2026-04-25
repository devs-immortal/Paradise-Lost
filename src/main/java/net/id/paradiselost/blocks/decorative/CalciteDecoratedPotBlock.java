package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CalciteDecoratedPotBlock extends DecoratedPotBlock {
    public CalciteDecoratedPotBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CalciteDecoratedPotBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            if (world.isClient) {
                return ActionResult.CONSUME;
            } else {
                ItemStack itemStack = decoratedPotBlockEntity.getStack();
                if (!stack.isEmpty() && (itemStack.isEmpty() || ItemStack.areItemsAndComponentsEqual(itemStack, stack) && itemStack.getCount() < itemStack.getMaxCount())) {
                    decoratedPotBlockEntity.wobble(CalciteDecoratedPotBlockEntity.WobbleType.POSITIVE);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    ItemStack itemStack2 = stack.splitUnlessCreative(1, player);
                    float f;
                    if (decoratedPotBlockEntity.isEmpty()) {
                        decoratedPotBlockEntity.setStack(itemStack2);
                        f = (float) itemStack2.getCount() / (float) itemStack2.getMaxCount();
                    } else {
                        itemStack.increment(1);
                        f = (float) itemStack.getCount() / (float) itemStack.getMaxCount();
                    }

                    world.playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.BLOCKS, 1.0F, 0.7F + 0.5F * f);
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.DUST_PLUME, (double) pos.getX() + 0.5, (double) pos.getY() + 1.2, (double) pos.getZ() + 0.5, 7, 0.0, 0.0, 0.0, 0.0);
                    }

                    decoratedPotBlockEntity.markDirty();
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
                }
            }
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            world.playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            decoratedPotBlockEntity.wobble(CalciteDecoratedPotBlockEntity.WobbleType.NEGATIVE);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            builder.addDynamicDrop(SHERDS_DYNAMIC_DROP_ID, lootConsumer -> {
                for (Item item : decoratedPotBlockEntity.getSherds().toList()) {
                    lootConsumer.accept(item.getDefaultStack());
                }
            });
        }

        return super.getDroppedStacks(state, builder);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity
                ? decoratedPotBlockEntity.asStack()
                : super.getPickStack(world, pos, state);
    }

}
