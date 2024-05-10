package net.id.paradiselost.blocks.natural.crop;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

/**
 * A crop block that is two blocks tall.
 * This class is fully usable on its own, but it is recommended to extend it.
 */
@SuppressWarnings("unused")
public class TallCropBlock extends CropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public final int lastSingleBlockAge;

    /**
     * @param lastSingleBlockAge The highest age for which this block is one block tall.
     */
    // For PL flax, lastSingleBlockAge is 3.
    public TallCropBlock(Settings settings, int lastSingleBlockAge) {
        super(settings);
        this.lastSingleBlockAge = lastSingleBlockAge;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
        this.tryGrow(state, world, pos, random, 25F);
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.down();
            state = world.getBlockState(pos);
        }
        if (!state.isOf(this)){
            return;
        }
        int newAge = this.getAge(state) + this.getGrowthAmount(world);
        int maxAge = this.getMaxAge();
        if (newAge > maxAge) {
            newAge = maxAge;
        }

        if (newAge > this.lastSingleBlockAge && canGrowUp(world, pos, state, newAge)) {
            world.setBlockState(pos, this.withAge(newAge), Block.NOTIFY_LISTENERS);
            world.setBlockState(pos.up(), this.withAgeAndHalf(newAge, DoubleBlockHalf.UPPER), Block.NOTIFY_LISTENERS);
        } else {
            world.setBlockState(pos, this.withAge(Math.min(newAge, lastSingleBlockAge)), Block.NOTIFY_LISTENERS);
        }
    }

    private boolean canGrowUp(World world, BlockPos pos, BlockState state, int age) {
        return world.getBlockState(pos.up()).isOf(this) || world.getBlockState(pos.up()).getMaterial().isReplaceable();
    }

    /**
     * Tries to grow the crop. Call me in randomTick().
     * Will not do anything if the block state is upper.
     *
     * @param upperBound The inverse of the probability of the crop growing with zero moisture.
     *                   <br>E.g. If upperBound is 25F, the crop with no moisture will grow about 1 in every 25 attempts.
     *                   The more moisture, the more likely.
     */
    @SuppressWarnings("SameParameterValue")
    protected void tryGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, float upperBound) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) return;

        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float moisture = getAvailableMoisture(this, world, pos);
                // More likely if there's more moisture
                if (random.nextInt((int) (upperBound / moisture) + 1) == 0) {
                    if (age >= Block.NOTIFY_LISTENERS) {
                        if (world.getBlockState(pos.up()).isOf(this) || world.getBlockState(pos.up()).getMaterial().isReplaceable()) {
                            world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
                            world.setBlockState(pos.up(), this.withAgeAndHalf(age + 1, DoubleBlockHalf.UPPER), Block.NOTIFY_LISTENERS);
                        }
                    } else {
                        world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    /**
     * Returns the bottom block state for the given age.
     */
    @Override
    public BlockState withAge(int age) {
        return this.withAgeAndHalf(age, DoubleBlockHalf.LOWER);
    }
    
    public BlockState withAgeAndHalf(int age, DoubleBlockHalf half) {
        return this.getDefaultState().with(this.getAgeProperty(), age).with(HALF, half);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(AGE);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos);
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER && blockState.get(AGE) > this.lastSingleBlockAge;
        }
    }

    protected DoubleBlockHalf getHalf(BlockState state) {
        return state.get(HALF);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (state.get(AGE) <= this.lastSingleBlockAge) {
                return super.getOutlineShape(state, world, pos, context);
            } else {
                // Fill in the bottom block if the plant is two-tall
                return Block.createCuboidShape(0, 0, 0,16, 16, 16);
            }
        } else {
            return super.getOutlineShape(this.withAge(state.get(AGE) - this.lastSingleBlockAge - 1), world, pos, context);
        }
    }

    // below code is (mostly) copied from TallPlantBlock

    public static BlockState withWaterloggedState(WorldView world, BlockPos pos, BlockState state) {
        return state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, world.isWater(pos)) : state;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return (state.get(AGE) <= lastSingleBlockAge || neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) ? state : Blocks.AIR.getDefaultState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        return blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx) ? this.withAgeAndHalf(0, DoubleBlockHalf.LOWER) : null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        // Place the other half
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos.down(), this.withAgeAndHalf(state.get(AGE), DoubleBlockHalf.LOWER), Block.NOTIFY_ALL);
        } else {
            if (state.get(AGE) > this.lastSingleBlockAge) {
                world.setBlockState(pos.up(), this.withAgeAndHalf(state.get(AGE), DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
            }
        }
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            // Break the other half
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                world.setBlockState(blockPos, blockState2, Block.SKIP_DROPS | Block.NOTIFY_ALL);
                world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                dropStacks(state, world, pos, null, player, player.getMainHandStack());
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
    }
}
