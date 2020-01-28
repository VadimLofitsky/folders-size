package ru.lofitsky.foldersSize.util;

import java.util.HashMap;
import java.util.Map;

public class ExecutionPerformance {
    private static ExecutionPerformance instance;
    private ExecutionPerformance() {}

    private final String defaultPrefix = "_default";
    private Map<String, PerformanceRecord> records = new HashMap<>();

    public static ExecutionPerformance getInstance() {
        if (instance == null) {
            return new ExecutionPerformance();
        }

        return instance;
    }

    public long start() {
        return start(defaultPrefix);
    }
    public long start(String prefix) {
        PerformanceRecord record;

        if (records.containsKey(prefix)) {
            record = records.get(prefix);
        } else {
            record = new PerformanceRecord();
            records.put(prefix, record);
        }

        return record.start();
    }

    public long stop() {
        return stop(defaultPrefix);
    }
    public long stop(String prefix) {
        PerformanceRecord record;

        if (!records.containsKey(prefix)) {
            throw new NullPointerException("No such prefix: " + prefix);
        }

        record = records.get(prefix);
        record.stop();
        return record.ending;
    }

    public long duration() {
        return duration(defaultPrefix);
    }
    public long duration(String prefix) {
        return records.get(prefix).getDuration();
    }

    public void clear(String prefix) {
        records.remove(prefix);
    }

    private class PerformanceRecord {
        long beginning, ending = -1L, duration;

        long start() {
            return beginning = System.nanoTime();
        }

        long stop() {
            ending = System.nanoTime();
            duration = ending - beginning;
            return ending;
        }

        public long getDuration() {
            duration = ending - beginning;
            return (ending == -1L) ? (System.nanoTime() - beginning) : duration;
        }
    }
}