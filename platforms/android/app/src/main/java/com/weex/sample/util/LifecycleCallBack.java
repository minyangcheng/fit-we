package com.weex.sample.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class LifecycleCallBack implements Application.ActivityLifecycleCallbacks {
    private int mCount = 0;
    private OnTaskSwitchListener mOnTaskSwitchListener;

    public LifecycleCallBack register(Application application) {
        application.registerActivityLifecycleCallbacks(this);
        return this;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

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

    }

    public interface OnTaskSwitchListener {

        void onTaskSwitchToForeground();

        void onTaskSwitchToBackground();
    }

    public void setOnTaskSwitchListenner(OnTaskSwitchListener listenner) {
        this.mOnTaskSwitchListener = listenner;
    }
}
