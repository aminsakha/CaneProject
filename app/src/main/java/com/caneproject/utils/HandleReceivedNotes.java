package com.caneproject.utils;

import static com.caneproject.fragment.GettingDataPageKt.setTexts;
import static com.caneproject.utils.UtilityFunctionsKt.processOnString;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.caneproject.classes.DataClass;
import com.caneproject.db.Data;
import com.caneproject.fragment.DataAnalyticPageKt;
import com.caneproject.fragment.GettingDataPageKt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HandleReceivedNotes {
    static int counter = 1;
    static int dataCount = 1;
    public static Data[] currentData;

    public static void beginListenForData(BluetoothSocket socket, Context context) {
        final Handler handler = new Handler();
        currentData = new Data[]{new Data("", "", "", "", "", "", "", "", "", true)};


        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int byteCount = socket.getInputStream().available();
                    if (byteCount > 0) {
                        byte[] rawBytes = new byte[byteCount];
                        socket.getInputStream().read(rawBytes);
                        String receivedString = new String(rawBytes, StandardCharsets.UTF_8);
                        Log.d("beginListenForData", "received: " + receivedString);
                        if (counter > 8) {
                            DataAnalyticPageKt.getDataList().add(currentData[0]);
                            handler.post(() -> setTexts(String.valueOf(dataCount)));
                            currentData[0] = new Data("", "", "", "", "", "", "", "", "", true);
                            counter = 1;
                            dataCount++;
                        }
                        List<String> curStatus = processOnString(receivedString);
                        for (String status : curStatus) {
                            if (counter == 1 && !status.endsWith("W"))
                                continue;
                            if (counter == 1 && status.endsWith("W"))
                                handler.post(() -> GettingDataPageKt.takingPhoto(context));


                            currentData[0].setDataAttribute(counter, status);
                            counter++;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
}
