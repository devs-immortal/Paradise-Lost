package net.id.paradise_lost.block.natural.plant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallClingingPlantBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<WallClingingPlantBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(TagKey.hashedCodec(Registries.BLOCK).fieldOf("clingable_blocks").forGetter((block) -> {
            return block.clingableBlocks;
        }), propertiesCodec()).apply(instance, WallClingingPlantBlock::new);
    });
    protected static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.NORTH, Block.box(0, 4, 0, 16, 12, 6),
            Direction.EAST, Block.box(10, 4, 0, 16, 12, 16),
            Direction.SOUTH, Block.box(0, 4, 10, 16, 12, 16),
            Direction.WEST, Block.box(0, 4, 0, 6, 12, 16)
    );
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final @Nullable TagKey<Block> clingableBlocks;

    public WallClingingPlantBlock(@Nullable TagKey<Block> clingableBlocks, Properties settings) {
        super(settings);
        this.clingableBlocks = clingableBlocks;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var wall = pos.relative(state.getValue(FACING));
        return canClingTo(world.getBlockState(wall));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var side = ctx.getClickedFace().getOpposite();
        if (side.get2DDataValue() >= 0) {
            return defaultBlockState().setValue(FACING, ctx.getClickedFace().getOpposite());
        }
        return null;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return true;
    }

    public boolean canClingTo(BlockState state) {
        return clingableBlocks == null || state.is(clingableBlocks);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

        builder.add(FACING);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING).getOpposite();
        return random.nextFloat() < 0.4 && hasRoomToGrow(world, pos, facing);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockState capState = BlockRegistry.ROOTCAP_BLOCK.get().defaultBlockState();
        Direction facing = state.getValue(FACING).getOpposite();
        for (int l = 0; l <= 1; l++) {
            for (int m = -1; m <= 1; m++) {
                world.setBlockAndUpdate(pos.relative(facing, l).relative(facing.getClockWise(Direction.Axis.Y), m), capState);
            }
        }
    }

    private boolean hasRoomToGrow(Level world, BlockPos pos, Direction facing) {
        for (int l = 0; l <= 2; l++) {
            for (int m = -1; m <= 1; m++) {
                BlockState blockState2 = world.getBlockState(pos.relative(facing, l).relative(facing.getClockWise(Direction.Axis.Y), m));
                if (!blockState2.isAir() && !blockState2.is(BlockTags.LEAVES) && !blockState2.is(this)) {
                    return false;
                }
            }
        }
        return true;
    }
}
