package com.fit.we.library.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fit.we.library.FitConstants;
import com.fit.we.library.FitWe;
import com.fit.we.library.bean.RefreshWeexPage;
import com.fit.we.library.util.EventUtil;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.FitUtil;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by minych on 18-3-26.
 */

public class HotRefreshService extends Service {

    private static final String EVENT_CHAT = "chat";

    private Socket mSocket;

    public static void startService(Context context) {
        if (FitWe.getInstance().getConfiguration().isDebug()) {
            Intent intent = new Intent(context, HotRefreshService.class);
            context.startService(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mSocket == null || !mSocket.connected()) {
            createWbSocketConn();
        }
        return START_STICKY;
    }

    private void createWbSocketConn() {
        try {
            String url = FitWe.getInstance().getConfiguration().getFitWeServer();
            if (TextUtils.isEmpty(url)) {
                return;
            }
            mSocket = IO.socket(url);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    FitLog.d(FitConstants.LOG_TAG, "client has been connect debug server");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    FitLog.d(FitConstants.LOG_TAG, "EVENT_DISCONNECT");
                }
            }).on(EVENT_CHAT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (args != null && args[0] != null) {
                        FitLog.d(FitConstants.LOG_TAG, args[0].toString());
                        if (TextUtils.equals(args[0].toString(), "refresh")) {
                            refresh();
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
        EventUtil.post(new RefreshWeexPage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }
    }
}
