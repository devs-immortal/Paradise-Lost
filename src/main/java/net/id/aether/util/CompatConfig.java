package net.id.aether.util;

import static net.id.aether.Aether.locate;
import static net.id.incubus_core.util.Config.*;

public final class CompatConfig {
    private CompatConfig(){}
    
    // TODO: When sodium supports custom render layers, make this check the version
    public static final boolean SODIUM_WORKAROUND = getBoolean(locate("sodium_workaround"), isLoaded("sodium"));
    public static final boolean SPECTRUM_WORKAROUND = getBoolean(locate("spectrum_workaround"), isLoaded("spectrum"));
}
