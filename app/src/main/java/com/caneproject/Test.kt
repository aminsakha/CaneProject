//package com.caneproject
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.google.android.material.snackbar.Snackbar
//
//const val PERMISSION_REQUEST_CAMERA = 0
//
///**
// * Launcher Activity that demonstrates the use of runtime permissions for Android M.
// * This Activity requests permissions to access the camera
// * ([android.Manifest.permission.CAMERA])
// * when the 'Show Camera Preview' button is clicked to start  [CameraPreviewActivity] once
// * the permission has been granted.
// *
// * <p>First, the status of the Camera permission is checked using [ActivityCompat.checkSelfPermission]
// * If it has not been granted ([PackageManager.PERMISSION_GRANTED]), it is requested by
// * calling [ActivityCompat.requestPermissions]. The result of the request is
// * returned to the
// * [android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback], which starts
// * if the permission has been granted.
// *
// * <p>Note that there is no need to check the API level, the support library
// * already takes care of this. Similar helper methods for permissions are also available in
// * ([ActivityCompat],
// * [android.support.v4.content.ContextCompat] and [android.support.v4.app.Fragment]).
// */
//class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
//
//    private lateinit var layout: View
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        layout = findViewById(R.id.main_layout)
//
//        // Register a listener for the 'Show Camera Preview' button.
//        findViewById<Button>(R.id.button_open_camera).setOnClickListener { showCameraPreview() }
//    }
//
//
//
//
//
//    /**
//     * Requests the [android.Manifest.permission.CAMERA] permission.
//     * If an additional rationale should be displayed, the user has to launch the request from
//     * a SnackBar that includes additional information.
//     */
//
//}