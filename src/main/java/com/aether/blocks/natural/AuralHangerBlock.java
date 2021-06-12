package com.aether.blocks.natural;

import com.aether.util.DynamicBlockColorProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.Vec3i;

public class AuralHangerBlock extends AetherHangerBlock implements DynamicBlockColorProvider {

    private final Vec3i[] gradientColors;

    public AuralHangerBlock(Properties settings, Vec3i[] gradientColors) {
        super(settings);
        this.gradientColors = gradientColors;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockColor getProvider() {
        return (state, world, pos, tintIndex) -> AuralLeavesBlock.getAuralColor(pos, gradientColors);
    }

}
