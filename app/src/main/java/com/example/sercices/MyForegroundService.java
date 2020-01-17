package com.example.sercices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.data.NotificationHelper;


public class MyForegroundService extends Service {

    public static final String IS_SERVICE_ACTIVE = "isActivated";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(IS_SERVICE_ACTIVE, false)){
            startForeground(1, NotificationHelper.getNotification(getApplicationContext()));
        } else {
            stopSelf();
        }
        return START_STICKY;
    }

    private void coord() {

    }
}
