package com.aether.util;

import com.aether.Aether;
import com.google.common.collect.Lists;
import net.minecraft.SharedConstants;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class EnumHelper {
    //Some enums are decompiled with extra arguments, so lets check for that
    private static final Class<?>[][] commonTypes =
            {
                    //{EnumAction.class},
                    //{ArmorMaterial.class, String.class, int.class, int[].class, int.class, SoundEvent.class, float.class},
                    //{EnumArt.class, String.class, int.class, int.class, int.class, int.class},
                    //{EnumCreatureAttribute.class},
                    //{EnumCreatureType.class, Class.class, int.class, Material.class, boolean.class, boolean.class},
                    //{Door.class},
                    //{EnumEnchantmentType.class, Predicate.class},
                    //{Sensitivity.class},
                    //{RayTraceResult.Type.class},
                    //{EnumSkyBlock.class, int.class},
                    //{SleepResult.class},
                    //{ToolMaterial.class, int.class, int.class, float.class, float.class, int.class},
                    {Rarity.class, Formatting.class},
                    //{HorseArmorType.class, String.class, int.class},
                    //{EntityLiving.SpawnPlacementType.class, BiPredicate.class}
            };
    private static Object reflectionFactory = null;
    private static Method newConstructorAccessor = null;
    private static Method newInstance = null;
    private static Method newFieldAccessor = null;
    private static Method fieldAccessorSet = null;
    private static boolean isSetup = false;

    static {
        if (!isSetup) {
            setup();
        }
    }

    private static void setup() {
        if (isSetup) {
            return;
        }

        try {
            Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory");
            reflectionFactory = getReflectionFactory.invoke(null);
            newConstructorAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newConstructorAccessor", Constructor.class);
            newInstance = Class.forName("sun.reflect.ConstructorAccessor").getDeclaredMethod("newInstance", Object[].class);
            newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, boolean.class);
            fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
        } catch (Exception e) {
            Aether.LOG.error("Error setting up EnumHelper.", e);
        }

        isSetup = true;
    }

    @Nullable
    public static Rarity addRarity(String name, Formatting color) {
        try {
            return addEnum(Rarity.class, name, color);
        } catch (Exception ex) {
            return Rarity.COMMON;
        }
    }

    /*
     * Everything below this is found at the site below, and updated to be able to compile in Eclipse/Java 1.6+
     * Also modified for use in decompiled code.
     * Found at: http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
     */
    private static Object getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return newConstructorAccessor.invoke(reflectionFactory, enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, @Nullable String value, int ordinal, Class<?>[] additionalTypes, @Nullable Object[] additionalValues) throws Exception {
        int additionalParamsCount = additionalValues == null ? 0 : additionalValues.length;
        Object[] params = new Object[additionalParamsCount + 2];
        params[0] = value;
        params[1] = ordinal;
        if (additionalValues != null) {
            System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
        }
        return enumClass.cast(newInstance.invoke(getConstructorAccessor(enumClass, additionalTypes), new Object[]{params}));
    }

    public static void setFailsafeFieldValue(Field field, @Nullable Object target, @Nullable Object value) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, field, false);
        fieldAccessorSet.invoke(fieldAccessor, target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws Exception {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                field.setAccessible(true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(Class<?> enumClass) throws Exception {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
        //Open J9
        blankField(enumClass, "enumVars");
    }

    @Nullable
    private static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Object... paramValues)
            throws Exception {
        setup();
        return addEnum(commonTypes, enumType, enumName, paramValues);
    }

    @Nullable
    protected static <T extends Enum<?>> T addEnum(Class<?>[][] map, Class<T> enumType, String enumName, Object... paramValues)
            throws Exception {
        for (Class<?>[] lookup : map) {
            if (lookup[0] == enumType) {
                Class<?>[] paramTypes = new Class<?>[lookup.length - 1];
                if (paramTypes.length > 0) {
                    System.arraycopy(lookup, 1, paramTypes, 0, paramTypes.length);
                }
                return addEnum(enumType, enumName, paramTypes, paramValues);
            }
        }
        return null;
    }

    //Tests an enum is compatible with these args, throws an error if not.
    public static void testEnum(Class<? extends Enum<?>> enumType, Class<?>[] paramTypes) throws Exception {
        addEnum(true, enumType, null, paramTypes, null);
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues)
            throws Exception {
        return addEnum(false, enumType, enumName, paramTypes, paramValues);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends Enum<?>> T addEnum(boolean test, final Class<T> enumType, @Nullable String enumName, final Class<?>[] paramTypes, @Nullable Object[] paramValues)
            throws Exception {
        if (!isSetup) {
            setup();
        }

        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) //Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
            {
                valuesField = field;
                break;
            }
        }

        int flags = (SharedConstants.isDevelopment ? Modifier.PUBLIC : Modifier.PRIVATE) | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
        if (valuesField == null) {
            String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));

            for (Field field : fields) {
                if ((field.getModifiers() & flags) == flags &&
                        field.getType().getName().replace('.', '/').equals(valueType)) //Apparently some JVMs return .'s and some don't..
                {
                    valuesField = field;
                    break;
                }
            }
        }

        if (valuesField == null) {
            final List<String> lines = Lists.newArrayList();
            lines.add(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
            lines.add(String.format("Runtime Deobf: %s", SharedConstants.isDevelopment));
            lines.add(String.format("Flags: %s", String.format("%16s", Integer.toBinaryString(flags)).replace(' ', '0')));
            lines.add("Fields:");
            for (Field field : fields) {
                String mods = String.format("%16s", Integer.toBinaryString(field.getModifiers())).replace(' ', '0');
                lines.add(String.format("       %s %s: %s", mods, field.getName(), field.getType().getName()));
            }

            for (String line : lines)
                Aether.LOG.fatal(line);

            if (test) {
                throw new Exception("Could not find $VALUES field for enum: " + enumType.getName());
            }
            return null;
        }

        if (test) {
            Object ctr = null;
            Exception ex = null;
            try {
                ctr = getConstructorAccessor(enumType, paramTypes);
            } catch (Exception e) {
                ex = e;
            }
            if (ctr == null || ex != null) {
                throw new Exception(String.format("Could not find constructor for Enum %s", enumType.getName()), ex);
            }
            return null;
        }

        valuesField.setAccessible(true);

        try {
            T[] previousValues = (T[]) valuesField.get(enumType);
            T newValue = makeEnum(enumType, enumName, previousValues.length, paramTypes, paramValues);
            setFailsafeFieldValue(valuesField, null, ArrayUtils.add(previousValues, newValue));
            cleanEnumCache(enumType);

            return newValue;
        } catch (Exception e) {
            Aether.LOG.error("Error adding enum with EnumHelper.", e);
            throw new RuntimeException(e);
        }
    }
}
