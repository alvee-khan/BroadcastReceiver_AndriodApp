package com.example.broadcastreceiver

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class BroadcastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_broadcast,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(
            R.id.broadcastSpinner
        )

        val proceedButton = view.findViewById<MaterialButton>(
            R.id.btnProceed
        )

        proceedButton.setOnClickListener {

            when (spinner.selectedItemPosition) {

                0 -> {
                    val intent = Intent(
                        requireContext(),
                        MessageInputActivity::class.java
                    )

                    startActivity(intent)
                }

                1 -> {
                    val intent = Intent(
                        requireContext(),
                        BatteryActivity::class.java
                    )

                    startActivity(intent)
                }
            }
        }
    }
}
