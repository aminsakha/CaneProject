package com.caneproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.caneproject.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var layout: View
    private val BLUETOOTH_PERMISSION_REQUEST_CODE = 9999
    private val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    } else {
        listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
    }
    private val missingPermissions = requiredPermissions.filter { permission ->
        ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application,
            "ad96d182-d226-4673-8ded-9bae9884afe5",
            Analytics::class.java,
            Crashes::class.java
        )
        layout = findViewById(R.id.main_layout)
        val button: Button = findViewById(R.id.ConnectionButton)
        button.setOnClickListener {
            showCameraPreview()
        }
    }

    private fun showCameraPreview() {
        // Check if the Camera permission has been granted
        if (missingPermissions.isEmpty()) {
            // Permission is already available, start camera preview
            layout.showSnackbar(R.string.camera_permission_available, Snackbar.LENGTH_SHORT)
            startConnection()
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission()
        }
    }

    private fun startConnection() {
        startActivity(Intent(this, HardWareConnection::class.java))
    }

    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(
                R.string.camera_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.BLUETOOTH),
                    BLUETOOTH_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            layout.showSnackbar(R.string.camera_permission_not_available, Snackbar.LENGTH_SHORT)
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                BLUETOOTH_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                layout.showSnackbar(R.string.camera_permission_granted, Snackbar.LENGTH_SHORT)
                startConnection()
            } else {
                // Permission request was denied.
                layout.showSnackbar(R.string.camera_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }
}