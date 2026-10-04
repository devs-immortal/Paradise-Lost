package net.id.paradise_lost.block.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ParadiseLostMushroomBlock extends MushroomBlock {

    private final HangType type;

    public ParadiseLostMushroomBlock(Properties settings, ResourceKey<ConfiguredFeature<?, ?>> feature, HangType type) {
        super(feature, settings);
        this.type = type;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos;
        if (type == HangType.ROOF) {
            blockPos = pos.above();
        } else {
            blockPos = pos.below();
        }
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        } else {
            return world.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(blockState, world, blockPos);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public boolean growMushroom(ServerLevel serverWorld, BlockPos pos, BlockState state, RandomSource random) {
        return false;
    }

    public enum HangType {
        FLOOR,
        ROOF,
        WALL
    }
}
