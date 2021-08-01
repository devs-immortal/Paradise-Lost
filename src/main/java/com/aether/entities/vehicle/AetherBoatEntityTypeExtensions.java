package com.aether.entities.vehicle;

import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;

public interface AetherBoatEntityTypeExtensions {
    BoatEntity.Type addBoat(String id, String name, Block baseBlock);
}