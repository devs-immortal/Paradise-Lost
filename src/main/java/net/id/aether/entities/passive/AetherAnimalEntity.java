package net.id.aether.entities.passive;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.items.AetherItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AetherAnimalEntity extends AnimalEntity {

    protected AetherAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    protected AetherAnimalEntity(World world) {
        this(null, world);
    }

    public static boolean isValidNaturalAetherSpawn(EntityType<? extends AetherAnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isOf(AetherBlocks.AETHER_GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 8;
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

    public void produceParticles(ParticleEffect parameters) {
        produceParticles(parameters, 5, 1);
    }

    public void produceParticles(ParticleEffect parameters, int amount, float yOffset) {
        for (int i = 0; i < amount; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(parameters, this.getParticleX(1.0D), this.getRandomBodyY() + yOffset, this.getParticleZ(1.0D), d, e, f);
        }

    }
}
