package com.caneproject.utils;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.caneproject.Data;
import com.caneproject.HardWareConnectionKt;

import java.nio.charset.StandardCharsets;

public class HandleReceivedNotes {

    public static void beginListenForData(BluetoothSocket socket, Context context) {
        final Handler handler = new Handler();
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int byteCount = socket.getInputStream().available();
                    if (byteCount > 0) {
                        byte[] rawBytes = new byte[byteCount];
                        socket.getInputStream().read(rawBytes);
                        String receivedString = new String(rawBytes, StandardCharsets.UTF_8);
                        Log.d("beginListenForData", "received: " + receivedString);
                        String[] strings = receivedString.split("A");
                        HardWareConnectionKt.getReceivedNotes().add(new Data(strings[0], strings[1], strings[2], strings[2]));
                        handler.post(() -> HardWareConnectionKt.getAdapter().notifyDataSetChanged());
                    }
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
}