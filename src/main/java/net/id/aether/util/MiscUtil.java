package net.id.aether.util;

public final class MiscUtil{
    private MiscUtil(){}
    
    /**
     * Should only be used by methods that are never actually called, used to make Accessors less painful.
     */
    @SuppressWarnings("unchecked")
    public static <T> T dummyObject(){
        return (T)new Object();
    }
}
