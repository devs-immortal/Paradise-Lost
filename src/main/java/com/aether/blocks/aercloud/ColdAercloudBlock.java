package com.aether.blocks.aercloud;

import com.aether.blocks.AetherBlocks;
import com.aether.client.rendering.particle.AetherParticles;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ColdAercloudBlock extends BaseAercloudBlock {

    public ColdAercloudBlock() {
        super(FabricBlockSettings.of(Material.SNOW_BLOCK).sounds(BlockSoundGroup.SNOW));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (entity.getVelocity().y <= 0.0F) {
            entity.setVelocity(entity.getVelocity().multiply(1.0D, 0.005D, 1.0D));
        }
    }
}
