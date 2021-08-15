package com.aether.entities.vehicle;

import net.minecraft.entity.vehicle.BoatEntity.Type;

import static com.aether.blocks.AetherBlocks.*;
import static com.aether.util.EnumExtender.add;

public class AetherBoatTypes {
    public static final Type SKYROOT = add(Type.class, "AETHER_SKYROOT", SKYROOT_PLANKS, "aether_skyroot");
    public static final Type GOLDEN_OAK = add(Type.class, "AETHER_GOLDEN_OAK", GOLDEN_OAK_PLANKS, "aether_golden_oak");
    public static final Type ORANGE = add(Type.class, "AETHER_ORANGE", ORANGE_PLANKS, "aether_orange");
    public static final Type CRYSTAL = add(Type.class, "AETHER_CRYSTAL", CRYSTAL_PLANKS, "aether_crystal");
    public static final Type WISTERIA = add(Type.class, "AETHER_WISTERIA", WISTERIA_PLANKS, "aether_wisteria");
}