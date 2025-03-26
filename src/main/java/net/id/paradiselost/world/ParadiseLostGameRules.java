package net.id.paradiselost.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ParadiseLostGameRules {

    public static void init() {
    }

    public static final GameRules.Key<GameRules.BooleanRule> PARADISE_VOID_KILLS = GameRuleRegistry.register("paradiseVoidKills", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> PARADISE_PORTAL_ENABLED = GameRuleRegistry.register("paradisePortalEnabled", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));


}
