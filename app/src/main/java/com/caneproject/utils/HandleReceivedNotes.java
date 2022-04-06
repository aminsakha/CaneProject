package com.caneproject.utils;

import static com.caneproject.fragment.GettingDataPageKt.setTexts;
import static com.caneproject.utils.UtilityFunctionsKt.processOnString;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.caneproject.classes.Data;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HandleReceivedNotes {
    static int counter = 1;
    static int dataCount = 1;

    public static void beginListenForData(BluetoothSocket socket) {
        final Handler handler = new Handler();
        final Data[] currentData = {new Data("", "", "", "", "", "", "", "")};


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
                            setTexts(String.valueOf(dataCount), currentData[0].getLed(), currentData[0].getIr(), currentData[0].getWhite()
                                    , currentData[0].getK(), currentData[0].getRed(), currentData[0].getGreen(), currentData[0].getBlue(), currentData[0].getResultColor());
                            currentData[0] = new Data("", "", "", "", "", "", "", "");
                            counter = 1;
                            dataCount++;
                        }
                        List<String> curStatus = processOnString(receivedString);
                        for (String status : curStatus) {
                            if (counter == 1 && !status.endsWith("W"))
                                continue;
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
