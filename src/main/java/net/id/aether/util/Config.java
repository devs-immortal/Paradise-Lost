package net.id.aether.util;

public final class Config{
    private Config(){}
    
    // TODO: When sodium supports custom render layers, make this check the version
    public static final boolean SODIUM_WORKAROUND = getBoolean("sodium_workaround", isLoaded("sodium"));
    public static final boolean SPECTRUM_WORKAROUND = getBoolean("spectrum_workaround", isLoaded("spectrum"));
}
