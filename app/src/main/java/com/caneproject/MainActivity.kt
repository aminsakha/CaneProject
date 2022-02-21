package com.caneproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application,
            "ad96d182-d226-4673-8ded-9bae9884afe5",
            Analytics::class.java,
            Crashes::class.java
        )
        val button: Button = findViewById(R.id.ConnectionButton)
        val arr = listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                checkPermission(arr)
            else
                startConnection()
        }
    }

    private fun checkPermission(permissions: List<String>) {
        val missingPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            Log.d("testt", " all")
            ActivityCompat.requestPermissions(this@MainActivity, permissions.toTypedArray(), 1)
        } else
            startConnection()
    }

    private fun startConnection() {
        startActivity(Intent(this, HardWareConnection::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.none { it != PackageManager.PERMISSION_GRANTED }) {
            // all permissions are granted
            startConnection()
        }
    }
}