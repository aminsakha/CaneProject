package com.caneproject.utils;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class HandleReceivedNotes {

    public static void beginListenForData(BluetoothSocket socket, Context context) {
        final Handler handler = new Handler();
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int byteCount = socket.getInputStream().available();
                   // Log.d("beginListenForData", String.valueOf(byteCount));
                    if (byteCount > 0) {
                        Log.d("beginListenForData", "gotinto if");
                        byte[] rawBytes = new byte[byteCount];
                        socket.getInputStream().read(rawBytes);
                        String receivedString = new String(rawBytes, StandardCharsets.UTF_8);
                        Log.d("beginListenForData", "received: " + receivedString);
                    }
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
}