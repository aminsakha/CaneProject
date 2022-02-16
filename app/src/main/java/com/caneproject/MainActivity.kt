package com.caneproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 9999
    }

    private lateinit var mLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application, "ad96d182-d226-4673-8ded-9bae9884afe5",
            Analytics::class.java, Crashes::class.java
        )
        mLayout = findViewById(R.id.main_layout)
        val button: Button = findViewById(R.id.ConnectionButton)
        button.setOnClickListener {
            //showCameraPreview()
            startConnection()
        }
    }

    private fun startConnection() {
        startActivity(Intent(this, HardWareConnection::class.java))
    }

    /* private fun requestCameraPermission() {
         // Permission has not been granted and must be requested.
         if (ActivityCompat.shouldShowRequestPermissionRationale(
                 this,
                 Manifest.permission.CAMERA
             )
         ) {
             // Provide an additional rationale to the user if the permission was not granted
             // and the user would benefit from additional context for the use of the permission.
             // Display a SnackBar with cda button to request the missing permission.
             Snackbar.make(
                 mLayout, R.string.camera_access_required,
                 Snackbar.LENGTH_INDEFINITE
             ).setAction(R.string.ok,
                 View.OnClickListener { view: View? ->
                     // Request the permission
                     ActivityCompat.requestPermissions(
                         this@MainActivity, arrayOf(Manifest.permission.CAMERA),
                         BLUETOOTH_PERMISSION_REQUEST_CODE
                     )
                 }).show()
         } else {
             Snackbar.make(mLayout, R.string.camera_unavailable, Snackbar.LENGTH_SHORT).show()
             // Request the permission. The result will be received in onRequestPermissionResult().
             ActivityCompat.requestPermissions(
                 this,
                 arrayOf(Manifest.permission.CAMERA),
                 BLUETOOTH_PERMISSION_REQUEST_CODE
             )
         }
     }*/

//    private fun showCameraPreview() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is already available, start camera preview
//            mLayout?.let {
//                Snackbar.make(
//                    it,
//                    R.string.camera_permission_available,
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//            startCamera()
//        } else {
//            // Permission is missing and must be requested.
//            requestCameraPermission()
//        }
//        // END_INCLUDE(startCamera)
//    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(
                    mLayout, R.string.camera_permission_granted,
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                startCamera()
            } else {
                // Permission request was denied.
                Snackbar.make(
                    mLayout, R.string.camera_permission_denied,
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }*/
}