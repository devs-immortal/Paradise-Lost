package com.aether.blocks.natural;

import com.aether.blocks.AetherMushroomBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class CinnabarRoseBlock extends AetherMushroomBlock {

    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");

    public CinnabarRoseBlock(Properties settings, Supplier<ConfiguredFeature<?, ?>> feature) {
        super(settings, feature, HangType.FLOOR);
        registerDefaultState(defaultBlockState().setValue(BLOOMING, false));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClientSide()) {
            Random random = world.getRandom();
            for (int i = 0; i < 9; i++) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + random.nextDouble();
                double f = (double) pos.getZ() + random.nextDouble();
                world.addParticle(DustParticleOptions.REDSTONE, d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if(!state.getValue(BLOOMING) && random.nextInt(4) == 0) {
            world.setBlockAndUpdate(pos, state.setValue(BLOOMING, true));
            return;
        }
        else if(state.getValue(BLOOMING) && random.nextInt(20) == 0) {
            world.setBlockAndUpdate(pos, state.setValue(BLOOMING, false));
            return;
        }
        if (state.getValue(BLOOMING) && random.nextInt(12) == 0) {
            boolean goodFloor = world.getBlockState(pos.below()).is(Blocks.REDSTONE_ORE);
            int chance = goodFloor ? 15 : 7;
            int range = goodFloor ? 8 : 4;

            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-5, -1, -5), pos.offset(5, 1, 5))) {
                if (world.getBlockState(blockPos).is(this)) {
                    --chance;
                    if (chance <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockPos2 = pos.offset(random.nextInt(range) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(range) - 1);

            for(int k = 0; k < 4; ++k) {
                if (world.isEmptyBlock(blockPos2) && state.canSurvive(world, blockPos2)) {
                    pos = blockPos2;
                }

                blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (world.isEmptyBlock(blockPos2) && state.canSurvive(world, blockPos2)) {
                world.setBlock(blockPos2, state, 2);
            }
            world.setBlockAndUpdate(pos, state.setValue(BLOOMING, false));
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (state.getValue(BLOOMING)) {
            for (int i = 0; i < 3; i++) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + random.nextDouble();
                double f = (double) pos.getZ() + random.nextDouble();
                world.addParticle(DustParticleOptions.REDSTONE, d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return 1;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(BLOOMING) ? 7 : 0;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.mayPlaceOn(world.getBlockState(pos.below()), world, pos.below());
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return (BlockTags.LOGS.contains(floor.getBlock()) && ((LevelReader) world).getRawBrightness(pos, 0) < 13) || floor.is(Blocks.REDSTONE_ORE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BLOOMING);
    }
}
