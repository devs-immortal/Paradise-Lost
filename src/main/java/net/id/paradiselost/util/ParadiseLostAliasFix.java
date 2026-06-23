package net.id.paradiselost.util;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostAliasFix {

    public static void init() {
        Registries.BLOCK.addAlias(locate("orange_sapling"), locate("aurel_sapling"));
        Registries.BLOCK.addAlias(locate("orange_log"), locate("aurel_log"));
        Registries.BLOCK.addAlias(locate("orange_wood"), locate("aurel_wood"));
        Registries.BLOCK.addAlias(locate("stripped_orange_log"), locate("stripped_aurel_log"));
        Registries.BLOCK.addAlias(locate("stripped_orange_wood"), locate("stripped_aurel_wood"));
        Registries.BLOCK.addAlias(locate("orange_leaves"), locate("aurel_leaves"));
        Registries.BLOCK.addAlias(locate("orange_planks"), locate("aurel_planks"));
        Registries.BLOCK.addAlias(locate("orange_fence"), locate("aurel_fence"));
        Registries.BLOCK.addAlias(locate("orange_fence_gate"), locate("aurel_fence_gate"));
        Registries.BLOCK.addAlias(locate("orange_slab"), locate("aurel_slab"));
        Registries.BLOCK.addAlias(locate("orange_stairs"), locate("aurel_stairs"));
        Registries.BLOCK.addAlias(locate("orange_trapdoor"), locate("aurel_trapdoor"));
        Registries.BLOCK.addAlias(locate("orange_door"), locate("aurel_door"));
        Registries.BLOCK.addAlias(locate("orange_button"), locate("aurel_button"));
        Registries.BLOCK.addAlias(locate("orange_pressure_plate"), locate("aurel_pressure_plate"));
        Registries.BLOCK.addAlias(locate("orange_sign"), locate("aurel_sign"));
        Registries.BLOCK.addAlias(locate("orange_hanging_sign"), locate("aurel_hanging_sign"));

        Registries.ITEM.addAlias(locate("orange_sapling"), locate("aurel_sapling"));
        Registries.ITEM.addAlias(locate("orange_log"), locate("aurel_log"));
        Registries.ITEM.addAlias(locate("orange_wood"), locate("aurel_wood"));
        Registries.ITEM.addAlias(locate("stripped_orange_log"), locate("stripped_aurel_log"));
        Registries.ITEM.addAlias(locate("stripped_orange_wood"), locate("stripped_aurel_wood"));
        Registries.ITEM.addAlias(locate("orange_leaves"), locate("aurel_leaves"));
        Registries.ITEM.addAlias(locate("orange_planks"), locate("aurel_planks"));
        Registries.ITEM.addAlias(locate("orange_fence"), locate("aurel_fence"));
        Registries.ITEM.addAlias(locate("orange_fence_gate"), locate("aurel_fence_gate"));
        Registries.ITEM.addAlias(locate("orange_slab"), locate("aurel_slab"));
        Registries.ITEM.addAlias(locate("orange_stairs"), locate("aurel_stairs"));
        Registries.ITEM.addAlias(locate("orange_trapdoor"), locate("aurel_trapdoor"));
        Registries.ITEM.addAlias(locate("orange_door"), locate("aurel_door"));
        Registries.ITEM.addAlias(locate("orange_button"), locate("aurel_button"));
        Registries.ITEM.addAlias(locate("orange_pressure_plate"), locate("aurel_pressure_plate"));
        Registries.ITEM.addAlias(locate("orange_sign"), locate("aurel_sign"));
        Registries.ITEM.addAlias(locate("orange_hanging_sign"), locate("aurel_hanging_sign"));

        Registries.ITEM.addAlias(locate("orange"), Identifier.ofVanilla("apple"));
    }

}
