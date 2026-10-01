package net.id.paradise_lost.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ParadiseLostWoodTypes {
    public static final WoodType AUREL = register("aurel", ParadiseLostBlockSets.AUREL);
    public static final WoodType MOTHER_AUREL = register("mother_aurel", ParadiseLostBlockSets.MOTHER_AUREL);
    public static final WoodType MENTH = register("menth", ParadiseLostBlockSets.MENTH);
    public static final WoodType WISTERIA = register("wisteria", ParadiseLostBlockSets.WISTERIA);

    private ParadiseLostWoodTypes() {}

    public static void init() {}

    private static WoodType register(String name, BlockSetType setType) {
        return WoodType.register(new WoodType(name, setType));
    }
}
