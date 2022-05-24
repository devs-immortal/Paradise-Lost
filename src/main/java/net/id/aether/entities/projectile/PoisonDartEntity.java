package net.id.aether.entities.projectile;

import net.id.aether.effect.condition.Conditions;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.items.AetherItems;
import net.id.incubus_core.condition.api.ConditionAPI;
import net.id.incubus_core.condition.api.Persistence;
import net.id.incubus_core.condition.base.ConditionManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PoisonDartEntity extends DartEntity {
    public PoisonDartEntity(EntityType<? extends DartEntity> entityType, World world) {
        super(entityType, world);
    }

    protected PoisonDartEntity(EntityType<? extends DartEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
    }

    protected PoisonDartEntity(EntityType<? extends DartEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
    }

    public PoisonDartEntity(LivingEntity owner, World world) {
        this(AetherEntityTypes.POISON_DART, owner, world);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        ConditionManager manager = ConditionAPI.getConditionManager(target);
        manager.add(Conditions.VENOM, Persistence.TEMPORARY, 120F); // this is probably an appropriate value
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(AetherItems.POISON_DART);
    }
}