package net.id.aether.blocks.natural.aercloud;

import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;

@SuppressWarnings("deprecation")
public class RedstoneAercloudBlock extends ParticleEmittingAercloudBlock{
    public RedstoneAercloudBlock(Settings properties) {
        super(properties, new DustParticleEffect(new Vec3f(0.8F, 0.12F, 0.12F), 1F), false);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }
}
