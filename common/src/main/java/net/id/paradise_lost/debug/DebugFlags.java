package net.id.paradise_lost.debug;

public final class DebugFlags {
    public static final String MINECART_FLOAT_PROPERTY = "paradise_lost.debug.minecart_float";

    public static final String MINECART_ROTATION_PROPERTY = "paradise_lost.debug.minecart_rotation";

    public static final boolean MINECART_FLOAT = Boolean.getBoolean(MINECART_FLOAT_PROPERTY);
    public static final boolean MINECART_ROTATION = Boolean.getBoolean(MINECART_ROTATION_PROPERTY);

    private DebugFlags() {
    }
}
