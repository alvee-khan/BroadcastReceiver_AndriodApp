package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

class CustomReceiverActivity : AppCompatActivity() {

    companion object {
        const val ACTION_CUSTOM_MESSAGE =
            "com.example.broadcastreceiver.ACTION_CUSTOM_MESSAGE"

        const val EXTRA_MESSAGE =
            "com.example.broadcastreceiver.EXTRA_MESSAGE"
    }

    private lateinit var receivedMessageText: TextView

    private val customReceiver = object : BroadcastReceiver() {

        override fun onReceive(
            context: Context?,
            intent: Intent?
        ) {

            if (intent?.action == ACTION_CUSTOM_MESSAGE) {

                val message = intent.getStringExtra(EXTRA_MESSAGE)
                    ?: "No message received"

                receivedMessageText.text = message
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_custom_receiver)

        InsetsHelper.applySystemBars(
            findViewById(R.id.screenRoot)
        )

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        receivedMessageText = findViewById(
            R.id.txtReceivedMessage
        )
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(ACTION_CUSTOM_MESSAGE)

        ContextCompat.registerReceiver(
            this,
            customReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        val message = intent.getStringExtra(EXTRA_MESSAGE)
            ?: ""

        val broadcastIntent = Intent(ACTION_CUSTOM_MESSAGE).apply {

            setPackage(packageName)

            putExtra(EXTRA_MESSAGE, message)
        }

        sendBroadcast(broadcastIntent)
    }

    override fun onStop() {

        unregisterReceiver(customReceiver)

        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
