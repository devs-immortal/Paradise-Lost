package net.id.aether.effect.condition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.client.rendering.ui.AetherOverlayRegistrar;
import net.id.aether.registry.AetherRegistries;
import net.id.incubus_core.condition.IncubusCondition;
import net.id.incubus_core.condition.api.Condition;
import net.id.incubus_core.condition.api.ConditionAPI;
import net.minecraft.util.registry.Registry;

public class Conditions {
    public static final Condition VENOM = register("venom", new VenomCondition());

    private static Condition register(String id, Condition condition) {
        return Registry.register(IncubusCondition.CONDITION_REGISTRY, Aether.locate(id), condition);
    }

    public static void init() {}

    @Environment(EnvType.CLIENT)
    public static void clientInit(){
        AetherOverlayRegistrar.register(new AetherOverlayRegistrar.Overlay(
            Aether.locate("textures/hud/condition/venom.png"),
            (player)-> ConditionAPI.isVisible(VENOM, player),
            (player)->{
                var manager = ConditionAPI.getConditionManager(player);
                return manager.getScaledSeverity(VENOM);
            }
        ));
    }
}
