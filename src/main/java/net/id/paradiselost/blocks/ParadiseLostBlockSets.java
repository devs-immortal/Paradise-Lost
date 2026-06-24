package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.BlockSetType;

public class ParadiseLostBlockSets {
    public static final BlockSetType AUREL = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("aurel"));
    public static final BlockSetType MOTHER_AUREL = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("mother_aurel"));
    public static final BlockSetType MENTH = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("menth"));
    public static final BlockSetType WISTERIA = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(ParadiseLost.locate("wisteria"));

    public static void init() {
    }
}
