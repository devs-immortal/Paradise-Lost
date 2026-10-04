package net.id.paradise_lost.block.natural.crop;

import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SwedrootCropBlock extends CropBlock {
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[] {
        Block.box(0, 14, 0, 16, 16, 16),
        Block.box(0, 12, 0, 16, 16, 16),
        Block.box(0, 10, 0, 16, 16, 16),
        Block.box(0, 8, 0, 16, 16, 16),
        Block.box(0, 6, 0, 16, 16, 16),
        Block.box(0, 4, 0, 16, 16, 16),
        Block.box(0, 2, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };

    public SwedrootCropBlock(Properties settings) {
        super(settings.offsetType(OffsetType.XZ));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return AGE_TO_SHAPE[state.getValue(getAgeProperty())];
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.getBlockState(pos.above().above()).is(Blocks.WATER) && world.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                if (random.nextInt(4) == 0) {
                    world.setBlock(pos, this.getStateForAge(i + 1), 2);
                }
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ItemRegistry.SWEDROOT.get();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return mayPlaceOn(Blocks.AIR.defaultBlockState(), world, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos.above()).is(ParadiseLostBlockTags.SWEDROOT_PLANTABLE);
    }

}
