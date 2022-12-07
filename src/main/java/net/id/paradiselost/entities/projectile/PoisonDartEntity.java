package net.id.paradiselost.entities.projectile;

import net.id.paradiselost.effect.condition.Conditions;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
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
        this(ParadiseLostEntityTypes.POISON_DART, owner, world);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        ConditionManager manager = ConditionAPI.getConditionManager(target);
        manager.add(Conditions.VENOM, Persistence.TEMPORARY, 120F); // this is probably an appropriate value
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ParadiseLostItems.POISON_DART);
    }
}
