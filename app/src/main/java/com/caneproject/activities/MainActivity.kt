package com.caneproject.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.caneproject.R
import com.caneproject.db.UserDb
import com.caneproject.utils.*
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.coroutines.launch

var screenHeight = 0
var screenWidth = 0

class MainActivity : AppCompatActivity() {
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

        val aboveApi31Permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN
        )
        val underApi31Permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            checkPermission(aboveApi31Permissions)
        else
            checkPermission(underApi31Permissions)
        screenHeight = getScreenHeight()
        screenWidth = getScreenWidth()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * for checking if the permissions already granted or not
     * @param permissions list of permitions according to api level
     */
    private fun checkPermission(permissions: List<String>) {
        val missingPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty())
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)
    }
}
