package net.id.aether.api;

import net.id.aether.effect.condition.ConditionProcessor;
import net.id.aether.component.AetherComponents;
import net.id.aether.component.ConditionManager;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ConditionAPI {
    public static List<Identifier> getValidProcessors(EntityType<?> type) {
        //noinspection ConstantConditions
        return AetherRegistries.CONDITION_REGISTRY.getIds()
                .stream()
                .filter(id -> !AetherRegistries.CONDITION_REGISTRY.get(id).exempt.contains(type))
                .collect(Collectors.toList());
    }

    public static ConditionProcessor getOrThrow(Identifier id) {
        return AetherRegistries.CONDITION_REGISTRY.getOrEmpty(id).orElseThrow((() -> new NoSuchElementException("No ConditionManager found registered for entry: " + id.toString())));
    }

    public static boolean isVisible(ConditionProcessor condition, LivingEntity entity) {
        if(!condition.isExempt(entity)) {
            return AetherComponents.CONDITION_MANAGER_KEY.get(entity).getScaledSeverity(condition) >= condition.visThreshold;
        }
        return false;
    }

    public static ConditionManager getConditionManager(LivingEntity entity) {
        return AetherComponents.CONDITION_MANAGER_KEY.get(entity);
    }

    public static void trySync(LivingEntity entity) {
        AetherComponents.CONDITION_MANAGER_KEY.sync(entity);
    }

    public static String getTranslationString(ConditionProcessor condition) {
        return "condition.processor." + condition.getId().getPath();
    }
}
