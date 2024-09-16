package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.BlockSetType;

public class ParadiseLostBlockSets {
    public static BlockSetType AUREL = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("aurel"));
    public static BlockSetType MOTHER_AUREL = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("mother_aurel"));
    public static BlockSetType ORANGE = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("orange"));
    public static BlockSetType WISTERIA = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("wisteria"));

    public static void init() {
    }
}
