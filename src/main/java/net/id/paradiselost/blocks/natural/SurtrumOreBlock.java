package net.id.paradiselost.blocks.natural;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SurtrumOreBlock extends ExperienceDroppingBlock {
    public SurtrumOreBlock(Settings settings, IntProvider experience) {
        super(settings, experience);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(3) == 0) {
            double d2 = (double) pos.getX() + random.nextDouble();
            double e2 = (double) pos.getY() + random.nextDouble() * 0.5 + 0.5;
            double f2 = (double) pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.LARGE_SMOKE, d2, e2, f2, 0.0, 0.0, 0.0);
        }
        if (random.nextBoolean()) {
            double d2 = (double) pos.getX() + random.nextDouble();
            double e2 = (double) pos.getY() + random.nextDouble() * 0.5 + 0.5;
            double f2 = (double) pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.SMOKE, d2, e2, f2, 0.0, 0.0, 0.0);
        }
    }
}
