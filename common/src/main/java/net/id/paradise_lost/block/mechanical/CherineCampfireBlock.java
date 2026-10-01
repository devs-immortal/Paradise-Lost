package net.id.paradise_lost.block.mechanical;

import net.id.paradise_lost.block.blockentity.ParadiseLostBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.id.paradise_lost.block.blockentity.CherineCampfireBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CherineCampfireBlock extends CampfireBlock {
    public CherineCampfireBlock(boolean emitsParticles, int fireDamage, Properties settings) {
        super(emitsParticles, fireDamage, settings);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CherineCampfireBlockEntity campfireBlockEntity) {
            ItemStack itemStack = player.getItemInHand(hand);
            Optional<RecipeHolder<CampfireCookingRecipe>> optional = campfireBlockEntity.getRecipeFor(itemStack);
            if (optional.isPresent()) {
                if (!world.isClientSide && campfireBlockEntity.addItem(player, player.hasInfiniteMaterials() ? itemStack.copy() : itemStack, ((CampfireCookingRecipe) ((RecipeHolder) optional.get()).value()).getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return ItemInteractionResult.SUCCESS;
                }

                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CherineCampfireBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (world.isClientSide) {
            return state.getValue(LIT) ? createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntity::clientTick) : null;
        } else {
            return state.getValue(LIT) ? createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntity::litServerTick) : createTickerHelper(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE.get(), CherineCampfireBlockEntity::unlitServerTick);
        }
    }
}
