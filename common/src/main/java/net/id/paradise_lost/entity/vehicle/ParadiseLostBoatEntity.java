package net.id.paradise_lost.entity.vehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.Boat;

public class ParadiseLostBoatEntity extends Boat {
    private final ParadiseLostBoatType wood;

    public ParadiseLostBoatEntity(EntityType<? extends Boat> entityType, Level level, ParadiseLostBoatType wood) {
        super(entityType, level);
        this.wood = wood;
    }

    public ParadiseLostBoatEntity(EntityType<? extends Boat> entityType, Level level, ParadiseLostBoatType wood, double x, double y, double z) {
        this(entityType, level, wood);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public ParadiseLostBoatType getWood() {
        return wood;
    }

    @Override
    public Item getDropItem() {
        return wood.boatItem();
    }
}
