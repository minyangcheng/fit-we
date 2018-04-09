package com.fit.we.library.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class LifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private int mCount = 0;
    private OnTaskSwitchListener mOnTaskSwitchListener;

    public LifecycleHandler(OnTaskSwitchListener onTaskSwitchListener) {
        this.mOnTaskSwitchListener = onTaskSwitchListener;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mOnTaskSwitchListener.onActivityCreated(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mCount++ == 0) {
            mOnTaskSwitchListener.onTaskSwitchToForeground();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (--mCount == 0) {
            mOnTaskSwitchListener.onTaskSwitchToBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mOnTaskSwitchListener.onActivityDestroyed(activity);
    }

    public interface OnTaskSwitchListener {

        void onActivityCreated(Activity activity);

        void onActivityDestroyed(Activity activity);

        void onTaskSwitchToForeground();

        void onTaskSwitchToBackground();
    }

}
