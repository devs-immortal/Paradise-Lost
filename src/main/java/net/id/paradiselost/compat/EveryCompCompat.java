package net.id.paradiselost.compat;

import net.id.paradiselost.ParadiseLost;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.VanillaRockChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;

public class EveryCompCompat {

    public static void init() {
        WoodTypeRegistry woodReg = WoodTypeRegistry.INSTANCE;
        StoneTypeRegistry stoneReg = StoneTypeRegistry.INSTANCE;

//        woodReg.addSimpleFinder(ParadiseLost.MOD_ID, "aurel");
//        woodReg.addSimpleFinder(ParadiseLost.MOD_ID, "mother_aurel");
//        woodReg.addSimpleFinder(ParadiseLost.MOD_ID, "menth");
//        woodReg.addSimpleFinder(ParadiseLost.MOD_ID, "wisteria");
        stoneReg.addSimpleFinder(ParadiseLost.MOD_ID, "floestone")
                .stone("floestone")
                .childBlock(VanillaRockChildKeys.COBBLESTONE, "cobbled_floestone")
                .childBlock(VanillaRockChildKeys.MOSSY_COBBLESTONE, "mossy_floestone")
                .childBlock(VanillaRockChildKeys.BRICKS, "floestone_brick");
        stoneReg.addSimpleFinder(ParadiseLost.MOD_ID, "heliolith")
                .childBlock(VanillaRockChildKeys.POLISHED, "smooth_heliolith");
        stoneReg.addSimpleFinder(ParadiseLost.MOD_ID, "levita")
                .childBlock(VanillaRockChildKeys.POLISHED, "levita_brick");


    }

}
