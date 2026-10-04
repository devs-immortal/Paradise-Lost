package net.id.paradise_lost.util;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.core.registries.Registries;

public class ParadiseLostCriteria {
    private static final RegistrationProvider<CriterionTrigger<?>> TRIGGERS =
            RegistrationProvider.get(Registries.TRIGGER_TYPE, ModConstants.MODID);

    public static final ItemUsedOnLocationTrigger XP_CIRCLET_CHARGED = register("xp_circlet_charged", new ItemUsedOnLocationTrigger());
    public static final ItemUsedOnLocationTrigger XP_CIRCLET_CHARGED_30 = register("xp_circlet_charged_30", new ItemUsedOnLocationTrigger());
    public static final ItemUsedOnLocationTrigger BLOOMED_BLADE_GOAL = register("bloomed_blade_goal", new ItemUsedOnLocationTrigger());

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        TRIGGERS.register(id, () -> criterion);
        return criterion;
    }

    public static void init() {
    }
}
