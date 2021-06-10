package com.aether.blocks.natural;

import com.aether.blocks.AetherMushroomBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class CinnabarRoseBlock extends AetherMushroomBlock {

    public static final BooleanProperty BLOOMING = BooleanProperty.of("blooming");

    public CinnabarRoseBlock(Settings settings, Supplier<ConfiguredFeature<?, ?>> feature) {
        super(settings, feature, HangType.FLOOR);
        setDefaultState(getDefaultState().with(BLOOMING, false));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) {
            Random random = world.getRandom();
            for (int i = 0; i < 9; i++) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + random.nextDouble();
                double f = (double) pos.getZ() + random.nextDouble();
                world.addParticle(DustParticleEffect.DEFAULT, d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!state.get(BLOOMING) && random.nextInt(4) == 0) {
            world.setBlockState(pos, state.with(BLOOMING, true));
            return;
        }
        else if(state.get(BLOOMING) && random.nextInt(20) == 0) {
            world.setBlockState(pos, state.with(BLOOMING, false));
            return;
        }
        if (state.get(BLOOMING) && random.nextInt(12) == 0) {
            boolean goodFloor = world.getBlockState(pos.down()).isOf(Blocks.REDSTONE_ORE);
            int chance = goodFloor ? 15 : 7;
            int range = goodFloor ? 8 : 4;

            for (BlockPos blockPos : BlockPos.iterate(pos.add(-5, -1, -5), pos.add(5, 1, 5))) {
                if (world.getBlockState(blockPos).isOf(this)) {
                    --chance;
                    if (chance <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockPos2 = pos.add(random.nextInt(range) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(range) - 1);

            for(int k = 0; k < 4; ++k) {
                if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                    pos = blockPos2;
                }

                blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                world.setBlockState(blockPos2, state, 2);
            }
            world.setBlockState(pos, state.with(BLOOMING, false));
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(BLOOMING)) {
            for (int i = 0; i < 3; i++) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + random.nextDouble();
                double f = (double) pos.getZ() + random.nextDouble();
                world.addParticle(DustParticleEffect.DEFAULT, d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 1;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(BLOOMING) ? 7 : 0;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.canPlantOnTop(world.getBlockState(pos.down()), world, pos.down());
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return (BlockTags.LOGS.contains(floor.getBlock()) && ((WorldView) world).getBaseLightLevel(pos, 0) < 13) || floor.isOf(Blocks.REDSTONE_ORE);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BLOOMING);
    }
}
