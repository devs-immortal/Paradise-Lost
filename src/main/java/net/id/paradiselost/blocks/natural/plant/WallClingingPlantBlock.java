package net.id.paradiselost.blocks.natural.plant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallClingingPlantBlock extends PlantBlock implements Fertilizable {

    public static final MapCodec<WallClingingPlantBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TagKey.codec(RegistryKeys.BLOCK).fieldOf("clingable_blocks").forGetter(block -> block.clingableBlocks),
                    createSettingsCodec()
            ).apply(instance, WallClingingPlantBlock::new)
    );
    protected static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.NORTH, Block.createCuboidShape(0, 4, 0, 16, 12, 6),
            Direction.EAST, Block.createCuboidShape(10, 4, 0, 16, 12, 16),
            Direction.SOUTH, Block.createCuboidShape(0, 4, 10, 16, 12, 16),
            Direction.WEST, Block.createCuboidShape(0, 4, 0, 6, 12, 16)
    );
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private final @Nullable TagKey<Block> clingableBlocks;

    public WallClingingPlantBlock(@Nullable TagKey<Block> clingableBlocks, Settings settings) {
        super(settings);
        this.clingableBlocks = clingableBlocks;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var wall = pos.offset(state.get(FACING));
        return canClingTo(world.getBlockState(wall));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var side = ctx.getSide().getOpposite();
        if (side.getHorizontal() >= 0) {
            return getDefaultState().with(FACING, ctx.getSide().getOpposite());
        }
        return null;
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return true;
    }

    public boolean canClingTo(BlockState state) {
        return clingableBlocks == null || state.isIn(clingableBlocks);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING).getOpposite();
        return random.nextFloat() < 0.4 && hasRoomToGrow(world, pos, facing);
    }

    @Override
    public void grow(ServerWorld world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        BlockState capState = ParadiseLostBlocks.ROOTCAP_BLOCK.getDefaultState();
        Direction facing = state.get(FACING).getOpposite();
        for (int l = 0; l <= 1; l++) {
            for (int m = -1; m <= 1; m++) {
                world.setBlockState(pos.offset(facing, l).offset(facing.rotateClockwise(Direction.Axis.Y), m), capState);
            }
        }
    }

    private boolean hasRoomToGrow(World world, BlockPos pos, Direction facing) {
        for (int l = 0; l <= 2; l++) {
            for (int m = -1; m <= 1; m++) {
                BlockState blockState2 = world.getBlockState(pos.offset(facing, l).offset(facing.rotateClockwise(Direction.Axis.Y), m));
                if (!blockState2.isAir() && !blockState2.isIn(BlockTags.LEAVES) && !blockState2.isOf(this)) {
                    return false;
                }
            }
        }
        return true;
    }
}
