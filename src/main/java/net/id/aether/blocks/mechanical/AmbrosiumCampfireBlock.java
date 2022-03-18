package net.id.aether.blocks.mechanical;

import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.blocks.blockentity.AmbrosiumCampfireBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AmbrosiumCampfireBlock extends CampfireBlock {
    public AmbrosiumCampfireBlock(boolean emitsParticles, int fireDamage, Settings settings) {
        super(emitsParticles, fireDamage, settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AmbrosiumCampfireBlockEntity) {
            AmbrosiumCampfireBlockEntity campfireBlockEntity = (AmbrosiumCampfireBlockEntity)blockEntity;
            ItemStack itemStack = player.getStackInHand(hand);
            Optional<CampfireCookingRecipe> optional = campfireBlockEntity.getRecipeFor(itemStack);
            if (optional.isPresent()) {
                if (!world.isClient && campfireBlockEntity.addItem(player.getAbilities().creativeMode ? itemStack.copy() : itemStack, ((CampfireCookingRecipe)optional.get()).getCookTime())) {
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
        return new AmbrosiumCampfireBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return state.get(LIT) ? checkType(type, AetherBlockEntityTypes.AMBROSIUM_CAMPFIRE, AmbrosiumCampfireBlockEntity::clientTick) : null;
        } else {
            return state.get(LIT) ? checkType(type, AetherBlockEntityTypes.AMBROSIUM_CAMPFIRE, AmbrosiumCampfireBlockEntity::litServerTick) : checkType(type, AetherBlockEntityTypes.AMBROSIUM_CAMPFIRE, AmbrosiumCampfireBlockEntity::unlitServerTick);
        }
    }
}
