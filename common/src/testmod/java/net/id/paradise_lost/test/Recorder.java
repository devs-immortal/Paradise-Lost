package net.id.paradise_lost.test;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// counts events that tests check
public final class Recorder {
    public static final List<Vec3> explosions = new CopyOnWriteArrayList<>();
    public static final AtomicInteger sips = new AtomicInteger();
    public static final AtomicInteger finalSips = new AtomicInteger();
    public static final AtomicInteger cloudCalls = new AtomicInteger();
    public static final AtomicInteger frames = new AtomicInteger();
    public static final List<DamageSource> playerDamage = new CopyOnWriteArrayList<>();
    public static final List<DamageSource> playerDeaths = new CopyOnWriteArrayList<>();
    public static final List<ItemStack> itemActivations = new CopyOnWriteArrayList<>();
    public static final List<String> levelEvents = new CopyOnWriteArrayList<>();

    public static final List<String> logErrors = new CopyOnWriteArrayList<>();
    public static final List<String> logWarnings = new CopyOnWriteArrayList<>();

    private Recorder() {
    }

    public static void register() {
        Appender appender = new AbstractAppender("ClientTestRecorder", null, null, true, Property.EMPTY_ARRAY) {
            @Override
            public void append(LogEvent event) {
                if (event.getLoggerName().equals("ClientTest") || event.getLoggerName().equals("ServerTest")) return;
                if (event.getLoggerName().equals("com.mojang.blaze3d.audio.OpenAlUtil")) return;
                String line = "[" + event.getLoggerName() + "] " + event.getMessage().getFormattedMessage()
                        + (event.getThrown() != null ? " (" + event.getThrown() + ")" : "");
                if (event.getLevel().isMoreSpecificThan(Level.ERROR)) {
                    logErrors.add(line);
                } else if (event.getLevel() == Level.WARN && line.toLowerCase(Locale.ROOT).contains("paradise")) {
                    logWarnings.add(line);
                }
            }
        };
        appender.start();
        LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
        logContext.getConfiguration().getRootLogger().addAppender(appender, null, null);
        logContext.updateLoggers();
    }

    public static List<String> errorsSince(int start) {
        List<String> logged = List.copyOf(logErrors);
        return logged.subList(start, logged.size());
    }

    public static void reset() {
        Stream.of(sips, finalSips, cloudCalls, frames).forEach(counter -> counter.set(0));
        explosions.clear();
        playerDamage.clear();
        playerDeaths.clear();
        itemActivations.clear();
        levelEvents.clear();
    }

    public static long explosionsNear(BlockPos pos, double radius) {
        Vec3 center = Vec3.atCenterOf(pos);
        return explosions.stream().filter(e -> e.closerThan(center, radius)).count();
    }

    public static boolean playerHurtBy(ResourceKey<DamageType> type) {
        return Stream.concat(playerDamage.stream(), playerDeaths.stream()).anyMatch(source -> source.is(type));
    }

    public static boolean playerDiedFrom(ResourceKey<DamageType> type) {
        return playerDeaths.stream().anyMatch(source -> source.is(type));
    }
}
