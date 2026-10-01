package net.id.paradise_lost.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ParadiseLostBlockSets {
    public static final BlockSetType AUREL = register("aurel");
    public static final BlockSetType MOTHER_AUREL = register("mother_aurel");
    public static final BlockSetType MENTH = register("menth");
    public static final BlockSetType WISTERIA = register("wisteria");

    private ParadiseLostBlockSets() {}

    public static void init() {}

    private static BlockSetType register(String name) {
        return BlockSetType.register(new BlockSetType(name));
    }
}
