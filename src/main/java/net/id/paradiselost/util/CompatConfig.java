package net.id.paradiselost.util;

import static net.id.incubus_core.util.Config.getBoolean;
import static net.id.incubus_core.util.Config.isLoaded;
import static net.id.paradiselost.ParadiseLost.locate;

public final class CompatConfig {
    private CompatConfig(){}
    
    // TODO: When sodium supports custom render layers, make this check the version
    public static final boolean SODIUM_WORKAROUND = getBoolean(locate("sodium_workaround"), isLoaded("sodium"));
    public static final boolean SPECTRUM_WORKAROUND = getBoolean(locate("spectrum_workaround"), isLoaded("spectrum"));
}
