package net.id.paradiselost.entities.passive;

import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class PopomEntity extends PigEntity {

    public PopomEntity(EntityType<? extends PopomEntity> entityType, World world) {
        super(entityType, world);
    }

    // Custom sounds for Popom
    @Override
    protected SoundEvent getAmbientSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_AMBIENT;
    }
/*    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_POPOM_HURT;
    }*/

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_POPOM_DEATH;
    }

    // Define attributes for Popom
    public static DefaultAttributeContainer.Builder createPopomAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        // Any custom data you want to save
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        // Any custom data you want to load
    }
}