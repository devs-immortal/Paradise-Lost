package net.id.paradiselost.fluids;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SpringWaterFluid extends WaterFluid {

    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !(Boolean)state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, ParadiseLostSoundEvents.BLOCK_SPRING_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }

        } else if (random.nextInt(4) == 0) {
            world.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), random.nextDouble() / 25 - 0.02, 0.05, random.nextDouble() / 25 - 0.02);

            if(random.nextInt(3) == 0 && !world.isWater(pos.up())) {
                world.addParticle(ParticleTypes.CLOUD, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + 0.925 + random.nextDouble() / 100, (double)pos.getZ() + random.nextDouble(), 0.0D, random.nextDouble() / 25 + 0.01, 0.0D);
            }

            if (random.nextInt(12) == 0) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, ParadiseLostSoundEvents.BLOCK_SPRING_WATER_AMBIENT_2, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.35F, random.nextFloat() + 0.5F, true);
            }
        }
    }

    @Override
    protected boolean isInfinite() {
        return false;
    }

    @Override
    public boolean isStill(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    public BlockState toBlockState(FluidState state) {
        return ParadiseLostBlocks.SPRING_WATER.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    public Fluid getFlowing() {
        return ParadiseLostFluids.FLOWING_SPRING_WATER;
    }

    public Fluid getStill() {
        return ParadiseLostFluids.SPRING_WATER;
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid instanceof SpringWaterFluid;
    }



    public static class Flowing extends SpringWaterFluid {
        public Flowing() {
        }

        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends SpringWaterFluid {
        public Still() {
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
