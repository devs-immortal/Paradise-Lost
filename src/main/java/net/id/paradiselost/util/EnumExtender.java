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


/*
 * Enum mixin template.
 *
 * Replace TARGET with the enum class.
 * Replace VALUEARRAY with the synthetic field storing an array of TARGETs. Find the name via the bytecode.
 * Replace ARG0TYPE/NAME, ARG1TYPE/NAME, etc. with the enum argument types/names, adjusting the number of arguments to match the enum constructor.
 *
 *

@Mixin(TARGET.class)
public class TARGETMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Mutable
    @Final
    private static TARGET[] VALUEARRAY;

    private TARGETMixin(String valueName, int ordinal, ARG0TYPE ARG0NAME, ARG1TTYPE ARG1NAME, ARGNTYPE ARGNNAME) {
        throw new AssertionError();
    }

    static {
        EnumExtender.register(TARGET.class, (name, args) -> {
            TARGET entry = (TARGET) (Object) new TARGETMixin(name, VALUEARRAY.length, (ARG0TYPE) args[0], (ARG1TYPE) args[1], ARGNTYPE args[n]);
            VALUEARRAY = Arrays.copyOf(VALUEARRAY, VALUEARRAY.length + 1);
            return VALUEARRAY[VALUEARRAY.length - 1] = entry;
        });
    }
}

 */
