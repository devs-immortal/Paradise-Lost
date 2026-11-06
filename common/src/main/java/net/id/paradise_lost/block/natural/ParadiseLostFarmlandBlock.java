package net.id.paradise_lost.block.natural;

import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import static net.id.paradise_lost.registry.BlockRegistry.DIRT;

public class ParadiseLostFarmlandBlock extends FarmBlock {
    public ParadiseLostFarmlandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return !this.defaultBlockState().canSurvive(ctx.getLevel(), ctx.getClickedPos())
                ? DIRT.get().defaultBlockState()
                : super.getStateForPlacement(ctx);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            turnToParadiseDirt(null, state, level, pos);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);
        if (!Services.PLATFORM.isFarmlandNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), Block.UPDATE_CLIENTS);
            } else if (!shouldMaintainFarmland(level, pos)) {
                turnToParadiseDirt(null, state, level, pos);
            }
        } else if (moisture < MAX_MOISTURE) {
            level.setBlock(pos, state.setValue(MOISTURE, MAX_MOISTURE), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide && Services.PLATFORM.shouldTrampleFarmland(level, pos, fallDistance, entity)) {
            turnToParadiseDirt(entity, state, level, pos);
        }
        entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
    }

    public static void turnToParadiseDirt(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        BlockState dirt = pushEntitiesUp(state, DIRT.get().defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, dirt);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, dirt));
    }

    private static boolean shouldMaintainFarmland(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }
}
