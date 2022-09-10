package net.id.paradiselost.util;

import net.minecraft.block.MapColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MapColorCreator {

    private static final Constructor<MapColor> CONSTRUCTOR;

    static {
        //noinspection unchecked
        CONSTRUCTOR = (Constructor<MapColor>) MapColor.class.getDeclaredConstructors()[0];
        CONSTRUCTOR.setAccessible(true);
    }

    public static MapColor createMapColor(int id, int color) {
        try {
            return CONSTRUCTOR.newInstance(id, color);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
