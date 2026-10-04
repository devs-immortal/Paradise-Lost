package net.id.paradise_lost.block.decorative;

import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CalciteDecoratedPotBlock extends DecoratedPotBlock {
    public CalciteDecoratedPotBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CalciteDecoratedPotBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            if (world.isClientSide) {
                return ItemInteractionResult.CONSUME;
            } else {
                ItemStack itemStack = decoratedPotBlockEntity.getTheItem();
                if (!stack.isEmpty() && (itemStack.isEmpty() || ItemStack.isSameItemSameComponents(itemStack, stack) && itemStack.getCount() < itemStack.getMaxStackSize())) {
                    decoratedPotBlockEntity.wobble(CalciteDecoratedPotBlockEntity.WobbleType.POSITIVE);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    ItemStack itemStack2 = stack.consumeAndReturn(1, player);
                    float f;
                    if (decoratedPotBlockEntity.isEmpty()) {
                        decoratedPotBlockEntity.setTheItem(itemStack2);
                        f = (float) itemStack2.getCount() / (float) itemStack2.getMaxStackSize();
                    } else {
                        itemStack.grow(1);
                        f = (float) itemStack.getCount() / (float) itemStack.getMaxStackSize();
                    }

                    world.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * f);
                    if (world instanceof ServerLevel serverWorld) {
                        serverWorld.sendParticles(ParticleTypes.DUST_PLUME, (double) pos.getX() + 0.5, (double) pos.getY() + 1.2, (double) pos.getZ() + 0.5, 7, 0.0, 0.0, 0.0, 0.0);
                    }

                    decoratedPotBlockEntity.setChanged();
                    world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            world.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
            decoratedPotBlockEntity.wobble(CalciteDecoratedPotBlockEntity.WobbleType.NEGATIVE);
            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity) {
            builder.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, lootConsumer -> {
                PotDecorations sherds = decoratedPotBlockEntity.getSherds();
                for (Optional<Item> sherd : List.of(sherds.back(), sherds.left(), sherds.right(), sherds.front())) {
                    lootConsumer.accept(sherd.orElse(Items.CALCITE).getDefaultInstance());
                }
            });
        }

        return super.getDrops(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof CalciteDecoratedPotBlockEntity decoratedPotBlockEntity
                ? decoratedPotBlockEntity.asStack()
                : super.getCloneItemStack(world, pos, state);
    }

}
