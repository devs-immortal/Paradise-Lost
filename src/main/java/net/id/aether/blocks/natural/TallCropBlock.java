package net.id.aether.blocks.natural;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
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
import net.minecraft.world.World;
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
        int i = this.getAge(state) + this.getGrowthAmount(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        world.setBlockState(pos, this.withAge(i), 2);
        if (i > 3) {
            world.setBlockState(pos.up(), this.withAge(i), 2);
        }
    }

    protected void tryGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, float upperBound) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int) (upperBound / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), 2);
                    if (i + 1 > 3) {
                        world.setBlockState(pos.up(), this.withAgeAndHalf(i + 1, DoubleBlockHalf.UPPER), 2);
                    }
                }
            }
        }
    }

    public BlockState withAgeAndHalf(int age, DoubleBlockHalf half) {
        return this.getDefaultState().with(this.getAgeProperty(), age).with(HALF, half);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(AGE);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            return super.canPlaceAt(state, world, pos);
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER && blockState.get(AGE) > 3;
        }
    }

    protected DoubleBlockHalf getHalf(BlockState state) {
        return state.get(HALF);
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
        return blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx) ? super.getPlacementState(ctx) : null;
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos blockPos = pos.up();
        world.setBlockState(blockPos, withWaterloggedState(world, blockPos, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                world.setBlockState(blockPos, blockState2, 35);
                world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
            }
        }
    }

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

}
