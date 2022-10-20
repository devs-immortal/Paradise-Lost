package net.id.paradiselost.entities.passive;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class ParadiseLostAnimalEntity extends AnimalEntity {

    protected ParadiseLostAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    protected ParadiseLostAnimalEntity(World world) {
        this(null, world);
    }

    public static boolean isValidNaturalParadiseLostSpawn(EntityType<? extends ParadiseLostAnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isOf(ParadiseLostBlocks.HIGHLANDS_GRASS) && world.getBaseLightLevel(pos, 0) > 8;
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView worldIn) {
        return worldIn.getBlockState(pos.down()).getBlock() == ParadiseLostBlocks.HIGHLANDS_GRASS ? 10.0F : worldIn.getLightLevel(pos) - 0.5F;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == ParadiseLostItems.BLUEBERRY;
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

    public void produceParticlesServer(ParticleEffect parameters, int rolls, int maxAmount, float yOffset) {
        if(world instanceof ServerWorld server) {
            maxAmount = maxAmount + 1;
            for (int i = 0; i < rolls; ++i) {
                double d = this.random.nextGaussian() * 0.02D;
                double e = this.random.nextGaussian() * 0.02D;
                double f = this.random.nextGaussian() * 0.02D;
                server.spawnParticles(parameters, this.getParticleX(1.0D), this.getRandomBodyY() + yOffset, this.getParticleZ(1.0D), 1 + random.nextInt(maxAmount), d, e, f, 0);
            }
        }
    }
}
