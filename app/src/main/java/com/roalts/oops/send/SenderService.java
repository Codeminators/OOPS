package com.roalts.oops.send;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.roalts.oops.Constants;

import java.util.Arrays;

public class SenderService extends Service {
    public static final String NOTIFICATION_ID
            = "pl.edu.agh.mobilne.ultrasound.android.lib.send.SenderService";

    public static final String BYTE_BUFFER_KEY = "bytebuffer";

    private Sender sender;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG, "Starting SenderService");

        byte[] byteBuffer = intent.getByteArrayExtra(BYTE_BUFFER_KEY);
        Log.d(Constants.LOG, "Using token: " + Arrays.toString(byteBuffer));

        sender = new Sender(byteBuffer);
        new Thread(sender).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG, "Destroying SenderService");
        sender.stop();
    }
}
