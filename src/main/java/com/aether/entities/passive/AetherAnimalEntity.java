package com.aether.entities.passive;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public class AetherAnimalEntity extends Animal {

    protected AetherAnimalEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos.below()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK ? 10.0F : worldIn.getMaxLocalRawBrightness(pos) - 0.5F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AetherItems.BLUEBERRY;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}
