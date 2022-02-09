package com.caneproject.utils;

import static com.caneproject.HardWareConnectionKt.sendSignal;
import static com.caneproject.utils.UtilityFunctionsKt.toastShower;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.caneproject.HardWareConnection;
import com.caneproject.HardWareConnectionKt;
import com.caneproject.R;

import java.io.IOException;
import java.util.UUID;

//this is a asyncTask class that handle checking connection with module (the dialog that we see in hard mode)
@SuppressLint("StaticFieldLeak")
public class MakeConnection extends AsyncTask<Void, Void, Void> {
    private boolean ConnectSuccess = true;
    ProgressDialog progress;
    Activity activity;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    private boolean isConnected;
    private boolean isBluetoothOn;
    Context context;

    public boolean isBluetoothOn() {
        return isBluetoothOn;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public MakeConnection(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(context, context.getString(R.string.connect_to_module), context.getString(R.string.wait_message));
    }

    @Override
    protected Void doInBackground(Void... devices) {
        try {
            if (socket == null || !isConnected) {
                Log.d("passed", "passed background");
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String address = context.getString(R.string.Bluetooth_AddressNew);
                BluetoothDevice disposition = bluetoothAdapter.getRemoteDevice(address);
                socket = disposition.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                socket.connect();
            }
        } catch (IOException e) {
            ConnectSuccess = false;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (!ConnectSuccess && bluetoothAdapter.isEnabled()) {
            toastShower(context, "Connection Failed.Try again ");
            activity.onBackPressed();

        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
            activity.onBackPressed();
            isBluetoothOn=true;
        } else {
            toastShower(context, "Connected");
            isConnected = true;
            sendSignal("901G");
            HandleReceivedNotes.beginListenForData(socket, context);
        }
        progress.dismiss();
    }
}