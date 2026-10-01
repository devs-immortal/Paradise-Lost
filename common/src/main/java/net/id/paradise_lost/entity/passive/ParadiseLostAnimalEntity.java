package net.id.paradise_lost.entity.passive;

import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public abstract class ParadiseLostAnimalEntity extends Animal {

    protected ParadiseLostAnimalEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    protected ParadiseLostAnimalEntity(Level world) {
        this(null, world);
    }

    public static boolean isValidNaturalParadiseLostSpawn(EntityType<? extends ParadiseLostAnimalEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ParadiseLostBlockTags.ANIMALS_PREFERRED) && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos.below()).is(ParadiseLostBlockTags.ANIMALS_PREFERRED) ? 10.0F : worldIn.getMaxLocalRawBrightness(pos) - 0.5F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ItemRegistry.BLACKCURRANT.get();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    public void produceParticles(ParticleOptions parameters) {
        produceParticles(parameters, 5, 1);
    }

    public void produceParticles(ParticleOptions parameters, int amount, float yOffset) {
        for (int i = 0; i < amount; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(parameters, this.getRandomX(1.0D), this.getRandomY() + yOffset, this.getRandomZ(1.0D), d, e, f);
        }
    }

    public void produceParticlesServer(ParticleOptions parameters, int rolls, int maxAmount, float yOffset) {
        if (level() instanceof ServerLevel server) {
            maxAmount = maxAmount + 1;
            for (int i = 0; i < rolls; ++i) {
                double d = this.random.nextGaussian() * 0.02D;
                double e = this.random.nextGaussian() * 0.02D;
                double f = this.random.nextGaussian() * 0.02D;
                server.sendParticles(parameters, this.getRandomX(1.0D), this.getRandomY() + yOffset, this.getRandomZ(1.0D), 1 + random.nextInt(maxAmount), d, e, f, 0);
            }
        }
    }
}
