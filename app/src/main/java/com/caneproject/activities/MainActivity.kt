package com.caneproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caneproject.R
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application,
            "ad96d182-d226-4673-8ded-9bae9884afe5",
            Analytics::class.java,
            Crashes::class.java
        )

    }
}
