package net.id.paradiselost.util;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BloomedCalciteUtil {

    public static void applyHealing(Entity applier, World world, BlockPos blockPos, Random random, ItemStack itemStack) {
        if (applier instanceof ServerPlayerEntity) {
            Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) applier, blockPos, itemStack);
        }

        world.setBlockState(blockPos, ParadiseLostBlocks.BLOOMED_CALCITE.getDefaultState());
        // particles
        for (int i = 0; i < 16; i++) {
            double xOffset = random.nextDouble();
            double yOffset = random.nextDouble();
            double zOffset = random.nextDouble();
            world.addParticle(ParticleTypes.ENTITY_EFFECT, blockPos.getX() + xOffset, blockPos.getY() + yOffset, blockPos.getZ() + zOffset, 0.97, 0.15, 0.14);
        }
    }
}
