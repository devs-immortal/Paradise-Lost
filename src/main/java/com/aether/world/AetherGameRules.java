package com.aether.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class AetherGameRules {
    public static final GameRules.Key<DoubleRule> MAX_QUICKSOIL_SPEED = GameRuleRegistry.register("maxQuicksoilVelocity", GameRules.Category.MOBS, GameRuleFactory.createDoubleRule(8, 0.1));

    public static void init() {
        // N/A
    }
}
