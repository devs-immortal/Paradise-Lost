package net.id.aether.blocks.natural;

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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TallCropBlock extends CropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    public TallCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
        if (this.getHalf(state) == DoubleBlockHalf.UPPER) {
            return;
        }
        tryGrow(state, world, pos, random, 25F);
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
        int i = this.getAge(state) + this.getGrowthAmount(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        world.setBlockState(pos, this.withAge(i), 2);
        if (i > 3 && canGrowUp(world, pos, state, i)) {
            world.setBlockState(pos.up(), this.withAgeAndHalf(i, DoubleBlockHalf.UPPER), 2);
        }
    }

    private boolean canGrowUp(World world, BlockPos pos, BlockState state, int age) {
        return world.getBlockState(pos.up()).isOf(this) || world.getBlockState(pos.up()).getMaterial().isReplaceable();
    }

    protected void tryGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, float upperBound) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int) (upperBound / f) + 1) == 0) {
                    if (i + 1 > 3) {
                        if (world.getBlockState(pos.up()).isOf(this) || world.getBlockState(pos.up()).getMaterial().isReplaceable()) {
                            world.setBlockState(pos, this.withAge(i + 1), 2);
                            world.setBlockState(pos.up(), this.withAgeAndHalf(i + 1, DoubleBlockHalf.UPPER), 2);
                        }
                    } else {
                        world.setBlockState(pos, this.withAge(i + 1), 2);
                    }
                }
            }
        }
    }

    public BlockState withAge(int age) {
        return this.withAgeAndHalf(age, DoubleBlockHalf.LOWER);
    }

    public BlockState withAgeAndHalf(int age, DoubleBlockHalf half) {
        return this.getDefaultState().with(this.getAgeProperty(), age).with(HALF, half);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(AGE);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos);
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER && blockState.get(AGE) > 3;
        }
    }

    protected DoubleBlockHalf getHalf(BlockState state) {
        return state.get(HALF);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (state.get(AGE) <= 3) {
                return super.getOutlineShape(state, world, pos, context);
            } else {
                return Block.createCuboidShape(0, 0, 0,16, 16, 16);
            }
        } else {
            return super.getOutlineShape(this.withAge(state.get(AGE) - 4), world, pos, context);
        }
    }

    // below code is (mostly) copied from TallPlantBlock


//
//    public static void placeAt(WorldAccess world, BlockState state, BlockPos pos, int flags) {
//        BlockPos blockPos = pos.up();
//        world.setBlockState(pos, withWaterloggedState(world, pos, state.with(HALF, DoubleBlockHalf.LOWER)), flags);
//        world.setBlockState(blockPos, withWaterloggedState(world, blockPos, state.with(HALF, DoubleBlockHalf.UPPER)), flags);
//    }

    public static BlockState withWaterloggedState(WorldView world, BlockPos pos, BlockState state) {
        return state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, world.isWater(pos)) : state;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        return blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx) ? this.withAgeAndHalf(0, DoubleBlockHalf.LOWER) : null;
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos.down(), this.withAgeAndHalf(state.get(AGE), DoubleBlockHalf.LOWER), 3);
        } else {
            if (state.get(AGE) > 3) {
                world.setBlockState(pos.up(), this.withAgeAndHalf(state.get(AGE), DoubleBlockHalf.UPPER), 3);
            }
        }
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                world.setBlockState(blockPos, blockState2, 35);
                world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
            }
        }
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            onBreakInCreative(world, pos, state, player);
            if (!player.isCreative()) {
                dropStacks(state, world, pos, null, player, player.getMainHandStack());
            }
        }

        super.onBreak(world, pos, state, player);
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
    }
}
