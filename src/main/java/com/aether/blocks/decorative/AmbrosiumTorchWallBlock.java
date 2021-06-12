package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import java.util.Random;

public class AmbrosiumTorchWallBlock extends WallTorchBlock {
    public AmbrosiumTorchWallBlock() {
        super(FabricBlockSettings.of(Material.DECORATION).collidable(false).breakByHand(true).randomTicks().luminance(15).sound(SoundType.WOOD).dropsLike(AetherBlocks.AMBROSIUM_TORCH),
                new DustParticleOptions(new Vector3f(0.886f, 0.871f, 0.125f), 0.7f));
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        int max = random.nextInt(3) + 2;
        for (int i = 0; i <= max; i++) {
            Direction direction = state.getValue(FACING).getOpposite();
            double e = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.23D + 0.3D * (double) direction.getStepX();
            double f = (double) pos.getY() + 0.6D + (random.nextDouble() - 0.5D) * 0.25D + 0.22D;
            double g = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.23D + 0.3D * (double) direction.getStepZ();
            world.addParticle(this.flameParticle, e, f, g, 0.0D, -4.0D, 0.0D);
        }
    }
}