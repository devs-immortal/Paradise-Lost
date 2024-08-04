package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.WoodType;

public class ParadiseLostWoodTypes {

    public static WoodType AUREL = WoodTypeBuilder.copyOf(WoodType.OAK).register(ParadiseLost.locate("aurel"), ParadiseLostBlockSets.AUREL);
    public static WoodType MOTHER_AUREL = WoodTypeBuilder.copyOf(WoodType.OAK).register(ParadiseLost.locate("mother_aurel"), ParadiseLostBlockSets.MOTHER_AUREL);
    public static WoodType ORANGE = WoodTypeBuilder.copyOf(WoodType.OAK).register(ParadiseLost.locate("orange"), ParadiseLostBlockSets.ORANGE);
    public static WoodType WISTERIA = WoodTypeBuilder.copyOf(WoodType.OAK).register(ParadiseLost.locate("wisteria"), ParadiseLostBlockSets.WISTERIA);

    public static void init() {}
}
