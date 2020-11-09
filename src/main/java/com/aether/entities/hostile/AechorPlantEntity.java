package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.passive.AetherAnimalEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.world.World;

public class AechorPlantEntity extends AetherAnimalEntity implements RangedAttackMob {

    public float sinage;
    public int poisonRemaining, size;

    public AechorPlantEntity(World world) {
        super(AetherEntityTypes.AECHOR_PLANT, world);

        this.size = this.random.nextInt(4) + 1;
        this.sinage = this.random.nextFloat() * 6F;
        this.poisonRemaining = this.random.nextInt(4) + 2;

        this.updatePosition(this.getX(), this.getY(), this.getZ());
    }

    // TODO: Implement rest of this thing

    @Override
    public void attack(LivingEntity target, float pullProgress) {

    }
}
