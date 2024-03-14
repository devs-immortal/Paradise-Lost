package net.id.paradiselost.util;

import static net.id.incubus_core.util.Config.getBoolean;
import static net.id.incubus_core.util.Config.isLoaded;
import static net.id.paradiselost.ParadiseLost.locate;

public final class CompatConfig {
    private CompatConfig() {
    }

    public static final boolean SPECTRUM_WORKAROUND = getBoolean(locate("spectrum_workaround"), isLoaded("spectrum"));
}
