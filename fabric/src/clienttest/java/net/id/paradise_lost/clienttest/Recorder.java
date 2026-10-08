package net.id.paradise_lost.clienttest;

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
    public static final AtomicInteger sips = new AtomicInteger();
    public static final AtomicInteger finalSips = new AtomicInteger();

    static final List<String> logErrors = new CopyOnWriteArrayList<>();
    static final List<String> logWarnings = new CopyOnWriteArrayList<>();

    private Recorder() {
    }

    static void register() {
        Appender appender = new AbstractAppender("ClientTestRecorder", null, null, true, Property.EMPTY_ARRAY) {
            @Override
            public void append(LogEvent event) {
                if (event.getLoggerName().equals("ClientTest")) return;
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

    static List<String> errorsSince(int start) {
        List<String> logged = List.copyOf(logErrors);
        return logged.subList(start, logged.size());
    }

    static void reset() {
        Stream.of(sips, finalSips).forEach(counter -> counter.set(0));
    }
}
