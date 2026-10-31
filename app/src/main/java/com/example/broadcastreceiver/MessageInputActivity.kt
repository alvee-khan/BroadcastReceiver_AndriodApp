package com.example.broadcastreceiver

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MessageInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_message_input)

        InsetsHelper.applySystemBars(
            findViewById(R.id.screenRoot)
        )

        val toolbar = findViewById<MaterialToolbar>(
            R.id.toolbar
        )

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val inputLayout = findViewById<TextInputLayout>(
            R.id.messageInputLayout
        )

        val editMessage = findViewById<TextInputEditText>(
            R.id.editMessage
        )

        val sendButton = findViewById<MaterialButton>(
            R.id.btnSendMessage
        )

        sendButton.setOnClickListener {

            val message = editMessage.text
                ?.toString()
                ?.trim()
                .orEmpty()

            if (message.isEmpty()) {

                inputLayout.error = "Please enter a message"

            } else {

                inputLayout.error = null

                val intent = Intent(
                    this,
                    CustomReceiverActivity::class.java
                )

                intent.putExtra(
                    CustomReceiverActivity.EXTRA_MESSAGE,
                    message
                )

                startActivity(intent)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
