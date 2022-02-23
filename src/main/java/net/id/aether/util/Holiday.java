package net.id.aether.util;

import java.util.Calendar;
import java.util.function.Predicate;

/**
 * A simple date-based holiday enumeration. A central place to add holiday related events.
 * <p>
 * The order of this enum is important, the first holiday (minus none) in this enum is picked first.
 * <p>
 * You can set the property the_aether.holiday_override to a valid holiday to override it.
 */
public enum Holiday {
    /**
     * No holiday, the most mundane state. :-(
     */
    NONE("normal", (calendar) -> false),
    
    /**
     * Christmas, from December 24 to December 26 (inclusive).
     * <p>
     * Minecraft uses this for chests.
     */
    CHRISTMAS("christmas", (calendar) -> {
        int date = calendar.get(Calendar.DATE);
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && date >= 24 && date <= 26;
    }),
    
    /**
     * Halloween, October 31st
     * <p>
     * Minecraft uses this to spawn bipeds with carved pumpkin headgear.
     */
    HALLOWEEN("holoween", (calendar) ->
        calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.DATE) == 31
    ),
    
    /**
     * Near Halloween, October 20 to November 3 (inclusive).
     * <p>
     * Minecraft uses this to spawn in more bats than normal.
     */
    NEAR_HALLOWEEN("near_halloween", (calendar) -> {
        var date = calendar.get(Calendar.DATE);
        var month = calendar.get(Calendar.MONTH);
        return (month == Calendar.OCTOBER && date >= 20) ||
            (month == Calendar.NOVEMBER && date <= 3);
    }),
    ;
    
    private final String name;
    private final Predicate<Calendar> predicate;
    
    Holiday(String prefix, Predicate<Calendar> predicate) {
        this.name = prefix;
        this.predicate = predicate;
    }
    
    private static final Holiday HOLIDAY;
    
    static {
        Holiday holiday = NONE;
        if (Config.HOLIDAY_OVERRIDE != null) {
            for (var value : values()) {
                if (value.getName().equals(Config.HOLIDAY_OVERRIDE)) {
                    holiday = value;
                    break;
                }
            }
        }
        
        if (holiday == NONE) {
            Calendar calendar = Calendar.getInstance();
            for (var value : values()) {
                if (value.predicate.test(calendar)) {
                    holiday = value;
                    break;
                }
            }
        }
        
        HOLIDAY = holiday;
    }
    
    /**
     * Gets the current holiday.
     *
     * @return The current holiday
     */
    public static Holiday get() {
        return HOLIDAY;
    }
    
    /**
     * Gets the name of this holiday in a form that is suitable for identifiers.
     *
     * @return Gets the name of this holiday
     */
    public String getName() {
        return name;
    }
}
