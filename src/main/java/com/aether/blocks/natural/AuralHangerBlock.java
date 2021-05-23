package com.aether.blocks.natural;

import com.aether.util.DynamicBlockColorProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.Vec3i;

public class AuralHangerBlock extends AetherHangerBlock implements DynamicBlockColorProvider {

    private final Vec3i[] gradientColors;

    public AuralHangerBlock(Settings settings, Vec3i[] gradientColors) {
        super(settings);
        this.gradientColors = gradientColors;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockColorProvider getProvider() {
        return (state, world, pos, tintIndex) -> AuralLeavesBlock.getAuralColor(pos, gradientColors);
    }

}
