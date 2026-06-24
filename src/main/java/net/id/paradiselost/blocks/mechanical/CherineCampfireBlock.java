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
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CherineCampfireBlock extends CampfireBlock {
    public CherineCampfireBlock(boolean emitsParticles, int fireDamage, Settings settings) {
        super(emitsParticles, fireDamage, settings);
    }
    
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CherineCampfireBlockEntity campfireBlockEntity) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (world.getRecipeManager().getPropertySet(RecipePropertySet.CAMPFIRE_INPUT).canUse(itemStack)) {
                if (world instanceof ServerWorld serverWorld && campfireBlockEntity.addItem(serverWorld, player, itemStack)) {
                    player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return ActionResult.SUCCESS_SERVER;
                }

                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CherineCampfireBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world instanceof ServerWorld serverWorld) {
            if (state.get(LIT)) {
                return validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, (tickWorld, pos, tickState, blockEntity) -> CherineCampfireBlockEntity.litServerTick(serverWorld, pos, tickState, blockEntity));
            }
            return validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntity::unlitServerTick);
        }
        return state.get(LIT) ? validateTicker(type, ParadiseLostBlockEntityTypes.CHERINE_CAMPFIRE, CherineCampfireBlockEntity::clientTick) : null;
    }
}
