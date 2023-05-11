package net.id.paradiselost.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlaxweaveCushionBlock extends Block {

	public FlaxweaveCushionBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.handleFallDamage(fallDistance, 0.1F, DamageSource.FALL); // cancel most fall damage
		if (fallDistance > 3F && !world.isClient) {
			this.spawnBreakParticles(world, null, pos, state); // spawn particles
			world.playSound(null, pos, soundGroup.getHitSound(), SoundCategory.BLOCKS, 0.7F, 1.0F); // and play cushion sound
		}
	}

}
