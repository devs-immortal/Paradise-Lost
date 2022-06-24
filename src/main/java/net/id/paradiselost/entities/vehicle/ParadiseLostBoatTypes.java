package net.id.paradiselost.entities.vehicle;

import net.minecraft.entity.vehicle.BoatEntity.Type;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;
import static net.id.paradiselost.util.EnumExtender.add;

public class ParadiseLostBoatTypes {
    public static final Type SKYROOT = add(Type.class, "PARADISE_LOST_SKYROOT", SKYROOT_PLANKS, "paradise_lost_skyroot");
    public static final Type GOLDEN_OAK = add(Type.class, "PARADISE_LOST_GOLDEN_OAK", GOLDEN_OAK_PLANKS, "paradise_lost_golden_oak");
    public static final Type ORANGE = add(Type.class, "PARADISE_LOST_ORANGE", ORANGE_PLANKS, "paradise_lost_orange");
    public static final Type CRYSTAL = add(Type.class, "PARADISE_LOST_CRYSTAL", CRYSTAL_PLANKS, "paradise_lost_crystal");
    public static final Type WISTERIA = add(Type.class, "PARADISE_LOST_WISTERIA", WISTERIA_PLANKS, "paradise_lost_wisteria");
}
