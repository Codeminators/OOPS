package com.roalts.oops.receive;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.roalts.oops.Constants;
import com.roalts.oops.R;
import com.roalts.oops.core.AbstractDataReader;
import com.roalts.oops.core.TokenGenerator;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static android.content.ContentValues.TAG;

public class ReceiverService extends Service {

    public static final String NOTIFICATION_ID
            = "pl.edu.agh.mobilne.ultrasound.android.lib.receive.ReceiverService";

    public static final String BYTE_BUFFER_KEY = "bytebuffer";

    private AndroidDataReader dataReader;
    private Receiver receiver;

    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG, "Starting ReceiverService");
        try {
            outputStream = new PipedOutputStream();
            inputStream = new PipedInputStream(outputStream);
            receiver = new Receiver(outputStream);
            new Thread(receiver).start();

            dataReader = new AndroidDataReader(inputStream);
            new Thread(dataReader).start();
        } catch (IOException e) {
            Log.e(Constants.LOG, "Error while initializing buffers", e);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG, "Destroying ReceiverService");
        dataReader.stop();
        receiver.stop();

        //fixme might cause errors, streams should be closed somewhere else
        /*
        closeQuietly(outputStream);
        closeQuietly(inputStream);
        */
    }


    private void publishResult(byte[] byteBuffer) {
        Intent intent = new Intent(NOTIFICATION_ID);
        intent.putExtra(BYTE_BUFFER_KEY, byteBuffer);
        sendBroadcast(intent);
    }

    private class AndroidDataReader extends AbstractDataReader {

        protected AndroidDataReader(PipedInputStream inputStream) {
            super(inputStream);
        }

        @Override
        protected void onSuccess(byte[] output) {
            Log.d(TAG, "onSuccess: " + "lolololololol");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            builder.setContentTitle("Paytm")
                    .setContentText("Third party transaction successful - Rs." + TokenGenerator.convertFromByteArray(output))
                    .setSmallIcon(R.mipmap.ic_launcher);
            notificationManager.notify(69, builder.build());
            publishResult(output);
        }

        @Override
        protected void onFailure(byte[] output, byte[] outputWithCrc) {

        }
    }
}
