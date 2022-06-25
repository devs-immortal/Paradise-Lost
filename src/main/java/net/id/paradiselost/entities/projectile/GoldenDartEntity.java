package net.id.paradiselost.entities.projectile;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GoldenDartEntity extends DartEntity {
    public GoldenDartEntity(EntityType<? extends GoldenDartEntity> entityType, World world) {
        super(entityType, world);
        setDamage(4);
    }

    public GoldenDartEntity(double x, double y, double z, World world) {
        super(ParadiseLostEntityTypes.GOLDEN_DART, x, y, z, world);
        setDamage(4);
    }

    public GoldenDartEntity(LivingEntity owner, World world) {
        super(ParadiseLostEntityTypes.GOLDEN_DART, owner, world);
        setDamage(4);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ParadiseLostItems.GOLDEN_DART);
    }
}
