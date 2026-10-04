package net.id.paradise_lost.block.natural.crop;

import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.FarmBlock;

@SuppressWarnings("unused")
public class TallCropBlock extends CropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public final int lastSingleBlockAge;

    public TallCropBlock(Properties settings, int lastSingleBlockAge) {
        super(settings);
        this.lastSingleBlockAge = lastSingleBlockAge;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.tryGrow(state, world, pos, random, 25F);
    }

    @Override
    public void growCrops(Level world, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
            state = world.getBlockState(pos);
        }
        if (!state.is(this)) {
            return;
        }
        int newAge = this.getAge(state) + this.getBonemealAgeIncrease(world);
        int maxAge = this.getMaxAge();
        if (newAge > maxAge) {
            newAge = maxAge;
        }

        if (newAge > this.lastSingleBlockAge && canGrowUp(world, pos, state, newAge)) {
            world.setBlock(pos, this.getStateForAge(newAge), Block.UPDATE_CLIENTS);
            world.setBlock(pos.above(), this.withAgeAndHalf(newAge, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);
        } else {
            world.setBlock(pos, this.getStateForAge(Math.min(newAge, lastSingleBlockAge)), Block.UPDATE_CLIENTS);
        }
    }

    private boolean canGrowUp(Level world, BlockPos pos, BlockState state, int age) {
        return world.getBlockState(pos.above()).is(this) || world.getBlockState(pos.above()).canBeReplaced();
    }

    @SuppressWarnings("SameParameterValue")
    protected void tryGrow(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, float upperBound) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return;

        if (world.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float moisture = growthSpeed(this, world, pos);

                if (random.nextInt((int) (upperBound / moisture) + 1) == 0) {
                    if (age >= Block.UPDATE_CLIENTS) {
                        if (world.getBlockState(pos.above()).is(this) || world.getBlockState(pos.above()).canBeReplaced()) {
                            world.setBlock(pos, this.getStateForAge(age + 1), Block.UPDATE_CLIENTS);
                            world.setBlock(pos.above(), this.withAgeAndHalf(age + 1, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);
                        }
                    } else {
                        world.setBlock(pos, this.getStateForAge(age + 1), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    private static float growthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float speed = 1.0F;
        BlockPos below = pos.below();

        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                float local = 0.0F;
                BlockState soil = level.getBlockState(below.offset(x, 0, z));
                if (soil.is(Blocks.FARMLAND)) {
                    local = 1.0F;
                    if (soil.getValue(FarmBlock.MOISTURE) > 0) {
                        local = 3.0F;
                    }
                }
                if (x != 0 || z != 0) {
                    local /= 4.0F;
                }
                speed += local;
            }
        }

        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        boolean horizontal = level.getBlockState(west).is(block) || level.getBlockState(east).is(block);
        boolean vertical = level.getBlockState(north).is(block) || level.getBlockState(south).is(block);
        if (horizontal && vertical) {
            speed /= 2.0F;
        } else {
            boolean diagonal = level.getBlockState(west.north()).is(block)
                    || level.getBlockState(east.north()).is(block)
                    || level.getBlockState(east.south()).is(block)
                    || level.getBlockState(west.south()).is(block);
            if (diagonal) {
                speed /= 2.0F;
            }
        }
        return speed;
    }

    @Override
    public BlockState getStateForAge(int age) {
        return this.withAgeAndHalf(age, DoubleBlockHalf.LOWER);
    }

    public BlockState withAgeAndHalf(int age, DoubleBlockHalf half) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age).setValue(HALF, half);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(AGE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.below();
            return this.mayPlaceOn(world.getBlockState(blockPos), world, blockPos);
        } else {
            BlockState blockState = world.getBlockState(pos.below());
            return blockState.is(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER && blockState.getValue(AGE) > this.lastSingleBlockAge;
        }
    }

    protected DoubleBlockHalf getHalf(BlockState state) {
        return state.getValue(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (state.getValue(AGE) <= this.lastSingleBlockAge) {
                return super.getShape(state, world, pos, context);
            } else {

                return Block.box(0, 0, 0, 16, 16, 16);
            }
        } else {
            return super.getShape(this.getStateForAge(Math.max(state.getValue(AGE) - this.lastSingleBlockAge - 1, 0)), world, pos, context);
        }
    }

    public static BlockState withWaterloggedState(LevelReader world, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, world.isWaterAt(pos)) : state;
    }

    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return (state.getValue(AGE) <= lastSingleBlockAge || neighborState.is(this) && neighborState.getValue(HALF) != doubleBlockHalf) ? state : Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        return blockPos.getY() < world.getMaxY() && world.getBlockState(blockPos.above()).canBeReplaced(ctx) ? this.withAgeAndHalf(0, DoubleBlockHalf.LOWER) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            world.setBlock(pos.below(), this.withAgeAndHalf(state.getValue(AGE), DoubleBlockHalf.LOWER), Block.UPDATE_ALL);
        } else {
            if (state.getValue(AGE) > this.lastSingleBlockAge) {
                world.setBlock(pos.above(), this.withAgeAndHalf(state.getValue(AGE), DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
            }
        }
    }

    protected static void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {

            BlockPos blockPos = pos.below();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(state.getBlock()) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                world.setBlock(blockPos, blockState2, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                dropResources(state, world, pos, null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
    }
}
