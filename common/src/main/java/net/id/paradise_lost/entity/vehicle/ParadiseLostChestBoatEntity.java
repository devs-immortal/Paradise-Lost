package net.id.paradise_lost.entity.vehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.ChestBoat;

public class ParadiseLostChestBoatEntity extends ChestBoat {
    private final ParadiseLostBoatType wood;

    public ParadiseLostChestBoatEntity(EntityType<? extends ChestBoat> entityType, Level level, ParadiseLostBoatType wood) {
        super(entityType, level);
        this.wood = wood;
    }

    public ParadiseLostChestBoatEntity(EntityType<? extends ChestBoat> entityType, Level level, ParadiseLostBoatType wood, double x, double y, double z) {
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
        return wood.chestBoatItem();
    }
}
