package net.id.paradise_lost.block.mechanical;

import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.server.level.ServerLevel;
import net.id.paradise_lost.block.blockentity.ParadiseLostBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.id.paradise_lost.block.blockentity.CherineCampfireBlockEntity;
import org.jetbrains.annotations.Nullable;


public class CherineCampfireBlock extends CampfireBlock {
    public CherineCampfireBlock(boolean emitsParticles, int fireDamage, Properties settings) {
        super(emitsParticles, fireDamage, settings);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CherineCampfireBlockEntity campfireBlockEntity) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (world.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(itemStack)) {
                if (world instanceof ServerLevel serverLevel && campfireBlockEntity.addItem(serverLevel, player, itemStack)) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS_SERVER;
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CherineCampfireBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (world instanceof ServerLevel serverLevel) {
            if (state.getValue(LIT)) {
                return createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), (tickLevel, pos, tickState, blockEntity) -> CherineCampfireBlockEntity.litServerTick(serverLevel, pos, tickState, blockEntity));
            }
            return createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntity::unlitServerTick);
        }
        return state.getValue(LIT) ? createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntity::clientTick) : null;
    }
}
