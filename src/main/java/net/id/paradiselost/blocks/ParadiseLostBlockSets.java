package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeRegistry;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.BlockSetType;

public class ParadiseLostBlockSets {
    public static BlockSetType AUREL = BlockSetTypeRegistry.registerWood(ParadiseLost.locate("aurel"));
    public static BlockSetType MOTHER_AUREL = BlockSetTypeRegistry.registerWood(ParadiseLost.locate("mother_aurel"));
    public static BlockSetType ORANGE = BlockSetTypeRegistry.registerWood(ParadiseLost.locate("orange"));
    public static BlockSetType WISTERIA = BlockSetTypeRegistry.registerWood(ParadiseLost.locate("wisteria"));

    public static void init() {
    }
}
