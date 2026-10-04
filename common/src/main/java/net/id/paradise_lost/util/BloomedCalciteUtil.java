package net.id.paradise_lost.util;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BloomedCalciteUtil {

    public static void applyHealing(Entity applier, Level world, BlockPos blockPos, RandomSource random, ItemStack itemStack) {
        if (applier instanceof ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) applier, blockPos, itemStack);
        }

        world.setBlockAndUpdate(blockPos, BlockRegistry.BLOOMED_CALCITE.get().defaultBlockState());

        for (int i = 0; i < 16; i++) {
            double xOffset = random.nextDouble();
            double yOffset = random.nextDouble();
            double zOffset = random.nextDouble();
            world.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.97F, 0.15F, 0.14F), blockPos.getX() + xOffset, blockPos.getY() + yOffset, blockPos.getZ() + zOffset, 0, 0, 0);
        }
    }

}
