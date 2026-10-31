package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

class BatteryActivity : AppCompatActivity() {

    private lateinit var batteryPercentage: TextView
    private lateinit var batteryProgress: ProgressBar
    private lateinit var batteryStatus: TextView

    private val batteryReceiver = object : BroadcastReceiver() {

        override fun onReceive(
            context: Context?,
            intent: Intent?
        ) {
            if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                updateBattery(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_battery)

        InsetsHelper.applySystemBars(
            findViewById(R.id.screenRoot)
        )

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        batteryPercentage = findViewById(
            R.id.txtBatteryPercentage
        )

        batteryProgress = findViewById(
            R.id.batteryProgressBar
        )

        batteryStatus = findViewById(
            R.id.txtBatteryStatus
        )
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(
            Intent.ACTION_BATTERY_CHANGED
        )

        val initialIntent = ContextCompat.registerReceiver(
            this,
            batteryReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        if (initialIntent != null) {
            updateBattery(initialIntent)
        }
    }

    private fun updateBattery(intent: Intent) {

        val level = intent.getIntExtra(
            BatteryManager.EXTRA_LEVEL,
            -1
        )

        val scale = intent.getIntExtra(
            BatteryManager.EXTRA_SCALE,
            -1
        )

        if (level >= 0 && scale > 0) {

            val percentage = level * 100 / scale

            batteryPercentage.text = "$percentage%"

            batteryProgress.progress = percentage

            batteryStatus.text = "Battery broadcast received"

        } else {

            batteryStatus.text = "Battery information unavailable"
        }
    }

    override fun onStop() {

        unregisterReceiver(batteryReceiver)

        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
