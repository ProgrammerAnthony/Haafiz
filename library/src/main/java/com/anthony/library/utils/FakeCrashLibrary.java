package com.anthony.library.utils;

/**
 * Created by Anthony on 2016/7/20.
 * Class Note:Not a real crash reporting library!
 * Crashlytics can be used as a replacement
 */
public final class FakeCrashLibrary {
    public static void log(int priority, String tag, String message) {
        // TODO add log entry to circular buffer.
    }

    public static void logWarning(Throwable t) {
        // TODO report non-fatal warning.
    }

    public static void logError(Throwable t) {
        // TODO report non-fatal error.
    }

    private FakeCrashLibrary() {
        throw new AssertionError("No instances.");
    }
}