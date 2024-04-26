package net.id.paradiselost.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class EnumExtender {
    private static final Map<Class<? extends Enum<?>>, BiFunction<String, Object[], ? extends Enum<?>>> extensibles = new HashMap<>();

    public static <T extends Enum<T>> void register(Class<T> extensible, BiFunction<String, Object[], T> callback) {
        extensibles.put(extensible, callback);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T add(Class<T> to, String name, Object... arguments) {
        if (extensibles.containsKey(to)) {
            try {
                return (T) extensibles.get(to).apply(name, arguments);
            } catch (ClassCastException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid arguments for entry " + name + " of enum " + to + ". Arguments must match the enum constructor.");
            }
        } else {
            throw new UnsupportedOperationException("Attempted to extend inextensible enum " + to + ". Create a mixin for it from the template in EnumExtender.");
        }
    }
}
