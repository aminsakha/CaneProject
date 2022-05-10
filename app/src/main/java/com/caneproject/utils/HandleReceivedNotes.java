package com.caneproject.utils;

import static com.caneproject.fragment.GettingDataPageKt.setTextBoxText;
import static com.caneproject.utils.UtilityFunctionsKt.processOnString;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.caneproject.db.Data;
import com.caneproject.fragment.GettingDataPageKt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HandleReceivedNotes {
    static int counter = 1;
    static int dataCount = 1;
    public static Data[] currentData;

    public static void beginListenForData(BluetoothSocket socket, Context context) {
        final Handler handler = new Handler();
        currentData = new Data[]{new Data("", "", "", "", "", "", "", "", com.caneproject.utils.GlobalVariablesKt.getDateAndTime(), "", true, "")};
        dataCount = 1;
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
                            currentData[0].setDateAndTime(com.caneproject.utils.GlobalVariablesKt.getDateAndTime());
                            com.caneproject.utils.GlobalVariablesKt.getDataList().add(currentData[0]);
                            handler.post(() -> setTextBoxText(String.valueOf(dataCount-1)));
                            currentData[0] = new Data("", "", "", "", "", "", "", "", com.caneproject.utils.GlobalVariablesKt.getDateAndTime(), "", true, "");
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
