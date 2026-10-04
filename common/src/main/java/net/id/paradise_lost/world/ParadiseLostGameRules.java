package net.id.paradise_lost.world;

import net.id.paradise_lost.platform.Services;
import net.minecraft.world.level.GameRules;

public final class ParadiseLostGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> PARADISE_VOID_KILLS =
            Services.REGISTRATION.registerBooleanGameRule("paradiseVoidKills", GameRules.Category.PLAYER, false);
    public static final GameRules.Key<GameRules.BooleanValue> PARADISE_PORTAL_ENABLED =
            Services.REGISTRATION.registerBooleanGameRule("paradisePortalEnabled", GameRules.Category.UPDATES, true);

    private ParadiseLostGameRules() {}

    public static void init() {}
}
