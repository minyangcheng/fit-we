package com.min.hybrid.library.util;

import android.util.Log;

public class L {

    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean writeLogs = true;

    private L() {
    }

    public static void writeLogs(boolean writeLogs) {
        L.writeLogs = writeLogs;
    }

    public static void d(String TAG, String message, Object... args) {
        log(Log.DEBUG,TAG, null, message, args);
    }

    public static void i(String TAG, String message, Object... args) {
        log(Log.INFO,TAG, null, message, args);
    }

    public static void w(String TAG, String message, Object... args) {
        log(Log.WARN,TAG, null, message, args);
    }

    public static void e(String TAG, Throwable ex) {
        log(Log.ERROR,TAG, ex, null);
    }

    public static void e(String TAG, String message, Object... args) {
        log(Log.ERROR, TAG, null, message, args);
    }

    public static void e(String TAG, Throwable ex, String message, Object... args) {
        log(Log.ERROR, TAG, ex, message, args);
    }

    private static void log(int priority,String TAG, Throwable ex, String message, Object... args) {
        if (!writeLogs) return;
        if (args.length > 0) {
            message = String.format(message, args);
        }

        String log;
        if (ex == null) {
            log = message;
        } else {
            String logMessage = message == null ? ex.getMessage() : message;
            String logBody = Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        Log.println(priority, TAG, log);
    }
}
