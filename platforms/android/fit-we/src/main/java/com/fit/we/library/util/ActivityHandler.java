package com.fit.we.library.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandler {

    private static List<Activity> activityList = new ArrayList<>();

    public static void push(Activity activity) {
        if (activityList != null) {
            activityList.add(activity);
        }
    }

    public static Activity getTop() {
        Activity activity = null;
        if (activityList != null && activityList.size() > 0) {
            activity = activityList.get(activityList.size() - 1);
        }
        return activity;
    }

    public static Activity pop() {
        Activity activity = null;
        if (activityList != null && activityList.size() > 0) {
            activity = activityList.get(activityList.size() - 1);
        }
        return activity;
    }

    public static void pop(Activity activity) {
        if (activityList != null && activity != null) {
            activityList.remove(activity);
        }
    }

    public static void clearExceptBottom() {
        if (activityList != null) {
            for (int i = activityList.size() - 1; i > 1; i--) {
                activityList.get(i).finish();
            }
        }
    }

    public static int getActivityDepth() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

}
