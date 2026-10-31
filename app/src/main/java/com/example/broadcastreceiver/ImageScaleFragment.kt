package com.example.broadcastreceiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class ImageScaleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_image_scale,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ZoomableImageView>(
            R.id.zoomImageView
        )

        val resetButton = view.findViewById<MaterialButton>(
            R.id.btnResetZoom
        )

        val imageUrl =
            "https://picsum.photos/id/1018/1200/800"

        Glide.with(this)
            .load(imageUrl)
            .override(900, 600)
            .dontAnimate()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_dialog_alert)
            .into(imageView)

        resetButton.setOnClickListener {
            imageView.resetZoom()
        }
    }
}
