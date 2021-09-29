package net.id.aether.api;

import net.id.aether.effect.condition.Condition;
import net.id.aether.component.AetherComponents;
import net.id.aether.component.ConditionManager;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * An API intended to aid use of {@code Condition}s. <br>
 * <br>
 * ~ Jack
 * @author AzazelTheDemonLord
 */
@ApiStatus.Experimental
public class ConditionAPI {
    /**
     * @param type The {@code EntityType} to test
     * @return A list of all conditions the given entity is not immune to.
     */
    public static List<Condition> getValidConditions(EntityType<?> type) {
        return AetherRegistries.CONDITION_REGISTRY
                .stream()
                .filter(condition -> !condition.exempt.contains(type))
                .collect(Collectors.toList());
    }

    /**
     * @param id The unique {@code Identifier} of the {@code Condition}.
     * @return The {@code Condition} corresponding to the given {@code Identifier}
     */
    public static Condition getOrThrow(Identifier id) {
        return AetherRegistries.CONDITION_REGISTRY.getOrEmpty(id).orElseThrow((() -> new NoSuchElementException("No ConditionManager found registered for entry: " + id.toString())));
    }

    /**
     * @param condition The {@code Condition} to test
     * @param entity The entity to test
     * @return Whether the given {@code Condition} is currently outwardly
     * visible on the given entity.
     */
    public static boolean isVisible(Condition condition, LivingEntity entity) {
        if(!condition.isExempt(entity)) {
            return AetherComponents.CONDITION_MANAGER_KEY.get(entity).getScaledSeverity(condition) >= condition.visThreshold;
        }
        return false;
    }

    /**
     * @param entity The entity you want to get the {@code ConditionManager} of.
     * @return The {@code ConditionManager} of the given entity.
     */
    public static ConditionManager getConditionManager(LivingEntity entity) {
        return AetherComponents.CONDITION_MANAGER_KEY.get(entity);
    }

    /**
     * Sync's a given entities {@code ConditionManager}.
     * @param entity The entity whose {@code ConditionManager} you wish to sync.
     */
    public static void trySync(LivingEntity entity) {
        AetherComponents.CONDITION_MANAGER_KEY.sync(entity);
    }

    /**
     * @param condition The {@code Condition} you want the translation string of
     * @return The translation string of the given {@code Condition}
     */
    public static String getTranslationString(Condition condition) {
        return "condition.condition." + condition.getId().getPath();
    }
}
