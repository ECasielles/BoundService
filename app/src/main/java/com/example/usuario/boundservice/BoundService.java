package com.example.usuario.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Chronometer;

public class BoundService extends Service {

    private Chronometer chronometer;
    private IBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    /**
     * El servicio oferce la hora actual a una Activity
     * @return
     */
    public String getTimestamp() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hours, minutes, seconds, millis;
        hours = (int) elapsedMillis / 3600000;
        minutes = (int)(elapsedMillis - hours * 3600000) / 60000;
        seconds = (int)(elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        millis = (int)(elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + "." + millis;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chronometer.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Objeto que encapsula el servicio
    public class MyBinder extends Binder {

        BoundService getService() {
            return BoundService.this;
        }

    }
}
