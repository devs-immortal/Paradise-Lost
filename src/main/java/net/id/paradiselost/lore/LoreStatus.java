package net.id.paradiselost.lore;

import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.Map;

public enum LoreStatus{
    HIDDEN,
    LOCKED,
    FREE,
    UNLOCKED,
    COMPLETED,
    ;
    
    private final String name;
    
    LoreStatus(){
        name = name().toLowerCase(Locale.ROOT);
    }
    
    public String getName(){
        return name;
    }
    
    private static final Map<String, LoreStatus> VALUES;
    static{
        var builder = ImmutableMap.<String, LoreStatus>builder();
        for(LoreStatus status : values()){
            builder.put(status.name, status);
        }
        VALUES = builder.build();
    }
    
    public static LoreStatus ofValue(String string){
        LoreStatus status = VALUES.get(string);
        if(status == null){
            throw new IllegalArgumentException("Unknown lore status: " + string);
        }
        return status;
    }
    
    public static LoreStatus ofValue(int ordinal){
        var values = values();
        if(ordinal >= values.length || ordinal < 0){
            throw new IllegalArgumentException("Unknown lore status ordinal: " + ordinal);
        }
        return values[ordinal];
    }
}
