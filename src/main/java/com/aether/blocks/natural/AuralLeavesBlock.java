package com.aether.blocks.natural;

import com.aether.entities.util.RenderUtils;
import com.aether.util.DynamicBlockColorProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class AuralLeavesBlock extends AetherLeavesBlock implements DynamicBlockColorProvider {

    private final Vec3i[] gradientColors;

    public AuralLeavesBlock(Settings settings, boolean collidable, Vec3i[] gradientColors) {
        super(settings, collidable);
        if(gradientColors.length != 4) {
            throw new InstantiationError("color gradient must contain exactly four colors");
        }
        this.gradientColors = gradientColors;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockColorProvider getProvider() {
        return (state, world, pos, tintIndex) -> {
            long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
            double delta = (i * 0.075);
            int index = MathHelper.floor(delta);
            int index2 = (index + 1) & 3;
            delta -= index;
            index &= 3;

            Vec3i color1 = gradientColors[index];
            Vec3i color2 = gradientColors[index2];

            int r = MathHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
            int g = MathHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
            int b = MathHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));

            return RenderUtils.toHex(r, g, b);
        };
    }
}
