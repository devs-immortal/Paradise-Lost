package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.block.WoodType;

public class ParadiseLostWoodTypes {

    public static WoodType AUREL = WoodTypeRegistry.register(ParadiseLost.locate("aurel"), ParadiseLostBlockSets.AUREL);
    public static WoodType MOTHER_AUREL = WoodTypeRegistry.register(ParadiseLost.locate("mother_aurel"), ParadiseLostBlockSets.MOTHER_AUREL);
    public static WoodType ORANGE = WoodTypeRegistry.register(ParadiseLost.locate("orange"), ParadiseLostBlockSets.ORANGE);
    public static WoodType WISTERIA = WoodTypeRegistry.register(ParadiseLost.locate("wisteria"), ParadiseLostBlockSets.WISTERIA);

    public static void init() {
    }
}
