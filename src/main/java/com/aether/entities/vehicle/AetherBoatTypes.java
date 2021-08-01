package com.aether.entities.vehicle;

import com.aether.blocks.AetherBlocks;
import net.minecraft.entity.vehicle.BoatEntity;

public class AetherBoatTypes {
    @SuppressWarnings("ConstantConditions")
    private static final AetherBoatEntityTypeExtensions boat = ((AetherBoatEntityTypeExtensions) (Object) BoatEntity.Type.OAK);
    public static final BoatEntity.Type SKYROOT = boat.addBoat("AETHER_SKYROOT", "aether_skyroot", AetherBlocks.SKYROOT_PLANKS);
    public static final BoatEntity.Type GOLDEN_OAK = boat.addBoat("AETHER_GOLDEN_OAK", "aether_golden_oak", AetherBlocks.GOLDEN_OAK_PLANKS);
    public static final BoatEntity.Type ORANGE = boat.addBoat("AETHER_ORANGE", "aether_orange", AetherBlocks.ORANGE_PLANKS);
    public static final BoatEntity.Type CRYSTAL = boat.addBoat("AETHER_CRYSTAL", "aether_crystal", AetherBlocks.CRYSTAL_PLANKS);
    public static final BoatEntity.Type WISTERIA = boat.addBoat("AETHER_WISTERIA", "aether_wisteria", AetherBlocks.WISTERIA_PLANKS);

    public static void init() {
        // N/A
    }
}