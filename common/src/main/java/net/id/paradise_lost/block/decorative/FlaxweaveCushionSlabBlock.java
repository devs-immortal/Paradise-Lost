package net.id.paradise_lost.block.decorative;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlaxweaveCushionSlabBlock extends SlabBlock {
    public FlaxweaveCushionSlabBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 0.1F, world.damageSources().fall());
        if (fallDistance > 3F && !world.isClientSide) {
            this.spawnDestroyParticles(world, null, pos, state);
            world.playSound(null, pos, soundType.getHitSound(), SoundSource.BLOCKS, 0.7F, 1.0F);
        }
    }
}
