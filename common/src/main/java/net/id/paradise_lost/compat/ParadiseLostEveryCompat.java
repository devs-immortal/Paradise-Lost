package net.id.paradise_lost.compat;

import net.id.paradise_lost.ModConstants;

import java.lang.reflect.Method;

public final class ParadiseLostEveryCompat {
    private ParadiseLostEveryCompat() {}

    public static void registerWoodAndLeaves() {
        try {
            Class<?> woodRegClass = Class.forName("net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry");
            Object woodReg = woodRegClass.getField("INSTANCE").get(null);
            Method addWood = woodRegClass.getMethod("addSimpleFinder", String.class, String.class);
            addWood.invoke(woodReg, ModConstants.MODID, "aurel");
            addWood.invoke(woodReg, ModConstants.MODID, "menth");
            addWood.invoke(woodReg, ModConstants.MODID, "mother_aurel");
            addWood.invoke(woodReg, ModConstants.MODID, "wisteria");

            Class<?> leafRegClass = Class.forName("net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry");
            Object leafReg = leafRegClass.getField("INSTANCE").get(null);
            Method addLeaf = leafRegClass.getMethod("addSimpleFinder", String.class, String.class);
            addLeaf.invoke(leafReg, ModConstants.MODID, "aurel");
            addLeaf.invoke(leafReg, ModConstants.MODID, "menth");
            addLeaf.invoke(leafReg, ModConstants.MODID, "mother_aurel");
            addLeaf.invoke(leafReg, ModConstants.MODID, "rose_wisteria");
            addLeaf.invoke(leafReg, ModConstants.MODID, "frost_wisteria");
            addLeaf.invoke(leafReg, ModConstants.MODID, "lavender_wisteria");

            Method mapLeaves = leafRegClass.getMethod("addLeavesToWoodMapping", String.class, String.class);
            String mod = ModConstants.MODID;
            mapLeaves.invoke(leafReg, mod + ":aurel", mod + ":aurel");
            mapLeaves.invoke(leafReg, mod + ":menth", mod + ":menth");
            mapLeaves.invoke(leafReg, mod + ":mother_aurel", mod + ":mother_aurel");
            mapLeaves.invoke(leafReg, mod + ":rose_wisteria", mod + ":wisteria");
            mapLeaves.invoke(leafReg, mod + ":frost_wisteria", mod + ":wisteria");
            mapLeaves.invoke(leafReg, mod + ":lavender_wisteria", mod + ":wisteria");
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void registerStoneTypes() {
        try {
            Class<?> stoneRegClass = Class.forName("net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry");
            Object stoneReg = stoneRegClass.getField("INSTANCE").get(null);
            Class<?> keysClass = Class.forName("net.mehvahdjukaar.stone_zone.api.set.VanillaRockChildKeys");
            Object cobble = keysClass.getField("COBBLESTONE").get(null);
            Object mossy = keysClass.getField("MOSSY_COBBLESTONE").get(null);
            Object bricks = keysClass.getField("BRICKS").get(null);
            Object polished = keysClass.getField("POLISHED").get(null);

            Method addSimpleFinder = stoneRegClass.getMethod("addSimpleFinder", String.class, String.class);
            Method stone = null;
            Method childBlock = null;

            Object floestone = addSimpleFinder.invoke(stoneReg, ModConstants.MODID, "floestone");
            stone = floestone.getClass().getMethod("stone", String.class);
            childBlock = floestone.getClass().getMethod("childBlock", Object.class, String.class);
            floestone = stone.invoke(floestone, "floestone");
            childBlock.invoke(floestone, cobble, "cobbled_floestone");
            childBlock.invoke(floestone, mossy, "mossy_floestone");
            childBlock.invoke(floestone, bricks, "floestone_brick");

            Object heliolith = addSimpleFinder.invoke(stoneReg, ModConstants.MODID, "heliolith");
            childBlock.invoke(heliolith, polished, "smooth_heliolith");

            Object levita = addSimpleFinder.invoke(stoneReg, ModConstants.MODID, "levita");
            childBlock.invoke(levita, polished, "levita_brick");
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
