package com.aether.entities.passive;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class AetherAnimalEntity extends AnimalEntity {

    protected AetherAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView worldIn) {
        return worldIn.getBlockState(pos.down()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK ? 10.0F : worldIn.getLightLevel(pos) - 0.5F;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == AetherItems.BLUEBERRY;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
