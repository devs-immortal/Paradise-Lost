package net.id.aether.blocks.natural.plant;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class MossBallBlock extends PlantBlock implements Waterloggable, Fertilizable {

    public static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 12, 14);

    public MossBallBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(Properties.WATERLOGGED, ctx.getWorld().isWater(ctx.getBlockPos()));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.WATERLOGGED);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.09F;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(Properties.WATERLOGGED);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int j = 1;
        int l = 0;
        int m = pos.getX() - 2;
        int n = 0;

        for(int o = 0; o < 5; ++o) {
            for(int p = 0; p < j; ++p) {
                int q = 2 + pos.getY() - 1;

                for(int r = q - 2; r < q; ++r) {
                    BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                    if (blockPos != pos && random.nextInt(6) == 0 && world.getBlockState(blockPos).isOf(Blocks.WATER)) {
                        world.setBlockState(blockPos, getDefaultState(), Block.NOTIFY_ALL);
                    }
                }
            }

            if (l < 2) {
                j += 2;
                ++n;
            } else {
                j -= 2;
                --n;
            }

            ++l;
        }
    }
}
