package net.id.paradiselost.blocks.mechanical;

import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.blocks.blockentity.CherineCampfireBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CherineCampfireBlock extends CampfireBlock {
    public CherineCampfireBlock(boolean emitsParticles, int fireDamage, Settings settings) {
        super(emitsParticles, fireDamage, settings);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CherineCampfireBlockEntity campfire) {
            ItemStack itemStack = player.getStackInHand(hand);
            Optional<RecipeEntry<CampfireCookingRecipe>> optional = campfire.getRecipeFor(itemStack);
            if (optional.isPresent()) {
                if (!world.isClient && campfire.addItem(player.getAbilities().creativeMode ? itemStack.copy() : itemStack, optional.get().value().getCookingTime())) {
                    player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return ActionResult.SUCCESS;
                }

                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CherineCampfireBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return state.get(LIT) ? validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntity::clientTick) : null;
        } else {
            return state.get(LIT) ? validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntity::litServerTick) : validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntity::unlitServerTick);
        }
    }
}
