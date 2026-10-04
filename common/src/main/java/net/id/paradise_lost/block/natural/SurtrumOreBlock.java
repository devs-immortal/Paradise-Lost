package net.id.paradise_lost.block.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SurtrumOreBlock extends DropExperienceBlock {
    public SurtrumOreBlock(IntProvider experience, Properties settings) {
        super(experience, settings);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
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
