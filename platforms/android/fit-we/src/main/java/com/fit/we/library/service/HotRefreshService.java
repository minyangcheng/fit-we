package com.fit.we.library.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.fit.we.library.FitWe;
import com.fit.we.library.container.FitContainerActivity;
import com.fit.we.library.container.FitContainerFragment;
import com.fit.we.library.util.ActivityHandler;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.FitUtil;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by minych on 18-3-26.
 */

public class HotRefreshService extends Service {

    private static final String TAG = HotRefreshService.class.getSimpleName();
    private static final String EVENT_CHAT = "chat";

    private Socket mSocket;

    public static void startService(Context context) {
        Intent intent = new Intent(context, HotRefreshService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FitLog.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (mSocket == null || !mSocket.connected()) {
            createWbSocketConn();
        }
        return START_STICKY;
    }

    private void createWbSocketConn() {
        try {
            String url = FitWe.getInstance().getConfiguration().getHostServer();
            if (TextUtils.isEmpty(url)) {
                return;
            }
            mSocket = IO.socket(url);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    FitLog.d(TAG, "EVENT_CONNECT");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    FitLog.d(TAG, "EVENT_DISCONNECT");
                }
            }).on(EVENT_CHAT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (args != null && args[0] != null) {
                        FitLog.d(TAG, args[0].toString());
                        if (TextUtils.equals(args[0].toString(), "refresh")) {
                            FitUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                }
                            });
                        }
                    }
                }

            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        Activity activity = ActivityHandler.getTop();
        if (activity instanceof FitContainerActivity) {
            FitContainerActivity containerActivity = (FitContainerActivity) activity;
            FitContainerFragment fragment = containerActivity.getContainerFragment();
            fragment.refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }
    }
}
