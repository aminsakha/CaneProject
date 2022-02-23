package com.caneproject.utils;

import static com.caneproject.HardWareConnectionKt.*;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.caneproject.Data;

import java.nio.charset.StandardCharsets;

public class HandleReceivedNotes {
    static int counter = 1;

    public static void beginListenForData(BluetoothSocket socket) {
        final Handler handler = new Handler();
        final Data[] currentData = {new Data("", "", "", "")};
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int byteCount = socket.getInputStream().available();
                    if (byteCount > 0) {
                        byte[] rawBytes = new byte[byteCount];
                        socket.getInputStream().read(rawBytes);
                        String receivedString = new String(rawBytes, StandardCharsets.UTF_8);
                        Log.d("beginListenForData", "received: " + receivedString);
                        if (counter > 4) {
                            getReceivedNotes().add(currentData[0]);
                            handler.post(() -> getAdapter().notifyItemChanged(getReceivedNotes().size() - 1));
                            currentData[0] = new Data("", "", "", "");
                        }
                        currentData[0].setDataAttribute(counter, receivedString);
                        counter++;
                    }
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
}