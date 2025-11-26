package net.id.paradiselost.util;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ParadiseLostCriteria {


    public static final ItemCriterion XP_CIRCLET_CHARGED = register("xp_circlet_charged", new ItemCriterion());
    public static final ItemCriterion XP_CIRCLET_CHARGED_30 = register("xp_circlet_charged_30", new ItemCriterion());
    public static final ItemCriterion BLOOMED_BLADE_GOAL = register("bloomed_blade_goal", new ItemCriterion());


    public static <T extends Criterion<?>> T register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, ParadiseLost.locate(id), criterion);
    }


    public static void init() {
    }

}
