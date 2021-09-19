package net.id.aether.blocks.natural;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.util.DynamicBlockColorProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

public class AuralHangerBlock extends AetherHangerBlock implements DynamicBlockColorProvider {

    private final Vec3i[] gradientColors;

    public AuralHangerBlock(Settings settings, Vec3i[] gradientColors) {
        super(settings);
        this.gradientColors = gradientColors;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        this.handleFabulousGraphics(pos);
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockColorProvider getProvider() {
        return (state, world, pos, tintIndex) -> AuralLeavesBlock.getAuralColor(pos, gradientColors);
    }

}
