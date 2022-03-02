package com.caneproject.utils;

import static com.caneproject.HardWareConnectionKt.*;
import static com.caneproject.utils.UtilityFunctionsKt.processOnString;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.caneproject.Data;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HandleReceivedNotes {
    static int counter = 1;

    public static void beginListenForData(BluetoothSocket socket) {
        final Handler handler = new Handler();
        final Data[] currentData = {new Data("", "", "", "")};
        getReceivedNotes().add(currentData[0]);
        handler.post(() -> getAdapter().notifyItemChanged(getReceivedNotes().size() - 1));
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
                            currentData[0] = new Data("", "", "", "");
                            getReceivedNotes().add(currentData[0]);
                            counter = 1;
                            handler.post(() -> getRecyclerView().smoothScrollToPosition(getAdapter().getItemCount()));
                        }
                        List<String> curStatus = processOnString(receivedString);
                        for (String status : curStatus) {
                            currentData[0].setDataAttribute(counter, status);
                            counter++;
                        }
                        handler.post(() -> getAdapter().notifyItemChanged(getReceivedNotes().size() - 1));
                    }
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
}